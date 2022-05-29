using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using Server.Models;
using Server.RequestModels;
using Server.ResponseModels;
using Server.Services;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace Server.Controllers
{
    [Route("api/auth")]
    [ApiController]
    public class AuthController : ControllerBase
    {

        private readonly IConfiguration _configuration;
        private readonly RoleManager<Role> _roleManager;
        private readonly UserManager<User> _userModel;
        private readonly IEmailService _emailService;

        public AuthController(UserManager<User> userModel, RoleManager<Role> roleManager, IConfiguration configuration, IEmailService emailService)
        {
            _configuration = configuration;
            _roleManager = roleManager;
            _userModel = userModel;
            _emailService = emailService;
        }

        [HttpPost]
        [Route("login")]
        public async Task<ActionResult<LoginResponseModel>> Login(LoginViewModel model)
        {
            var user = await _userModel.FindByNameAsync(model.Username);
            var wrongPassword = !await _userModel.CheckPasswordAsync(user, model.Password);

            if (user == null || wrongPassword)
            {
                return Unauthorized();
            }

            var userRoles = await _userModel.GetRolesAsync(user);

            var authClaims = new List<Claim>
            {
                new ("name", user.UserName),
                new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
            };

            authClaims.AddRange(userRoles.Select(userRole => new Claim("roles", userRole)));

            var authSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));

            var token = new JwtSecurityToken(
                _configuration["JWT:ValidIssuer"],
                _configuration["JWT:ValidAudience"],
                expires: DateTime.Now.AddHours(5),
                claims: authClaims,
                signingCredentials: new SigningCredentials(authSigningKey, SecurityAlgorithms.HmacSha256)
            );

            return Ok(new LoginResponseModel
            {
                Token = new JwtSecurityTokenHandler().WriteToken(token),
                Expiration = token.ValidTo
            });
        }

        [HttpPost]
        [Route("register")]
        public async Task<ActionResult<UserViewModel>> Register(RegisterViewModel model)
        {
            var rolesDoesNotExist = !await _roleManager.RoleExistsAsync("admin");
            var admins = await _userModel.GetUsersInRoleAsync("admin");

            if (rolesDoesNotExist || admins.Count() == 0)
            {
                await AddRoles("admin");
                await AddRoles("student");
                await AddRoles("handyman");

                var returnAdmin = await AddUser(model, true);
                var admin = await _userModel.FindByNameAsync(model.Username);
                SendConfirmationEmail(admin, Request.Headers["origin"]);

                return returnAdmin;
            }

            var userWithSameName = await _userModel.FindByNameAsync(model.Username);
            var userWithSameEmail = await _userModel.FindByEmailAsync(model.Email);
            if (userWithSameName != null || userWithSameEmail != null)
            {
                return BadRequest("User already exists, please login");
            }

            var returnUser = await AddUser(model, false);
            var user = await _userModel.FindByNameAsync(model.Username);
            SendConfirmationEmail(user, "http://18.196.144.212");

            return returnUser;
        }

        [HttpGet]
        [Route("verify-email")]
        public async Task<ActionResult<GenericResponseModel>> VerifyEmail(string code)
        {
            if(IsBase64String(code) == false)
            {
                return Ok(new { message = "Verification failed" });
            }
            var base64EncodedBytes = Convert.FromBase64String(code);
            var user = await _userModel.FindByIdAsync(Encoding.UTF8.GetString(base64EncodedBytes));

            if(user == null)
            {
                return Ok(new { message = "Verification failed" });
            }

            if(user.EmailConfirmed == true)
            {
                return BadRequest(new { message = "Email already confirmed" });
            }

            user.EmailConfirmed = true;

            await _userModel.UpdateAsync(user);

            return Ok(new { message = "Verification successful, you can now login" });
        }

        private async Task AddRoles(string role)
        {
            var roleValue = new Role { Id = Guid.NewGuid().ToString(), Name = role };
            var result = await _roleManager.CreateAsync(roleValue);
            if(!result.Succeeded)
            {
                Console.WriteLine(result.Errors);
            }
        }

        private async Task<ActionResult<UserViewModel>> AddUser(RegisterViewModel model, bool makeAdmin)
        {
            var puser = new User { UserName = model.Username, Email = model.Email };
            var result = await _userModel.CreateAsync(puser, model.Password);
            if(!result.Succeeded)
            {
                return new UserViewModel { Errors = result.Errors.Select(err => err.Description) };
            }

            var user = await _userModel.FindByNameAsync(puser.UserName);
            if(makeAdmin)
            {
                await _userModel.AddToRoleAsync(user, "admin");
            }

            return Created("", new UserViewModel { Id = user.Id, Email = user.Email, UserName = user.UserName });
        }

        private bool IsBase64String(string text)
        {
            Span<byte> buffer = new Span<byte>(new byte[text.Length]);
            return Convert.TryFromBase64String(text, buffer, out int bytesParsed);
        }

        private void SendConfirmationEmail(User user, string origin)
        {
            var plainTextBytes = Encoding.UTF8.GetBytes(user.Id);
            var base64DecodedBytes = Convert.ToBase64String(plainTextBytes);

            var verifyUrl = $"{origin}/api/auth/verify-email?code={base64DecodedBytes}";
            var message = $@"<p>Please click the below link to verify your email address:</p>
                             <p><a href=""{verifyUrl}"">{verifyUrl}</a></p>";

            _emailService.Send(
                to: user.Email,
                subject: "Sign-up Verification API - Verify Email",
                html: $@"<h4>Verify Email</h4>
                         <p>Thanks for registering!</p>
                         {message}"
            );
        }
    }
}
