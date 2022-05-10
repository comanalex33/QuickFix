using Server.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;

namespace Server.Controllers
{
    [Route("api/users")]
    [ApiController]
    public class UserController : ControllerBase
    {

        private readonly RoleManager<Role> _roleManager;
        private readonly UserManager<User> _userModel;
        private readonly AppDbContext _context;

        public UserController(UserManager<User> userModel, RoleManager<Role> roleManager, AppDbContext context)
        {
            _roleManager = roleManager;
            _userModel = userModel;
            _context = context;
        }

        [HttpGet]
        [Route("roles/{roleName}")]
        public async Task<ActionResult<IEnumerable<Role>>> GetUsersByRole(string roleName)
        {
            if (roleName.Equals("all"))
            {
                return Ok(await _userModel.Users.ToListAsync());
            }
            return Ok(await _userModel.GetUsersInRoleAsync(roleName));
        }

        [HttpGet]
        [Route("{username}")]
        public async Task<ActionResult<Role>> GetUserByName(string username)
        {
            var user = await _userModel.FindByNameAsync(username);
            if (user == null)
            {
                return BadRequest("User " + username + " not found");
            }
            return Ok(user);
        }

        [HttpGet]
        [Route("{username}/role")]
        public async Task<ActionResult<string>> GetUserRoles(string username)
        {
            var user = await _userModel.FindByNameAsync(username);
            if(user == null)
            {
                return BadRequest("User " + username + " not found");
            }
            var roles = await _userModel.GetRolesAsync(user);
            return Ok(roles[0]);
        }

        [HttpPost]
        [Route("{username}/buildings/{id}")]
        [Authorize(Roles ="admin,student,handyman")]
        public async Task<ActionResult<Role>> AddBuilding(string username, long id)
        {
            var user = await _userModel.FindByNameAsync(username);
            if (user == null)
            {
                return BadRequest("User " + username + " not found");
            }
            var building = await _context.Building.FindAsync(id);
            if (building == null)
            {
                return BadRequest("This building does not exist!");
            }

            user.buildingId = id;

            await _userModel.UpdateAsync(user);
            return Ok(user);
        }

        [HttpPost]
        [Route("{username}/makeadmin")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<string>> MakeAdmin(string username)
        {
            var user = await _userModel.FindByNameAsync(username);
            if(user == null)
            {
                return BadRequest("User does not exists");
            }
            var roles = await _userModel.GetRolesAsync(user);
            if(roles.Any())
            {
                return BadRequest("This user already has a role");
            }
            await _userModel.AddToRoleAsync(user, "admin");
            return Ok("User " + username + " is admin now");
        }

        [HttpPost]
        [Route("{username}/roles/{role}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult> AddRole(string username, string role)
        {
            var user = await _userModel.FindByNameAsync(username);
            if (user == null)
            {
                return NotFound("User does not exists");
            }
            var roles = await _userModel.GetRolesAsync(user);
            if (roles.Any())
            {
                return BadRequest("This user already has a role");
            }
            if (!role.Equals("student") && !role.Equals("handyman"))
            {
                return BadRequest("Not an existing role");
            }
            await _userModel.AddToRoleAsync(user, role);
            return Ok("User " + username + " is " + role + " now");
        }

        [HttpDelete]
        [Route("{username}/removeRole")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<string>> RemoveRole(string username)
        {
            var user = await _userModel.FindByNameAsync(username);
            if (user == null)
            {
                return NotFound("User does not exists");
            }
            var roles = await _userModel.GetRolesAsync(user);
            if (!roles.Any())
            {
                return BadRequest("This user doesn't has a role");
            }
            string[] list = new string[1];
            list[0] = roles[0];
            await _userModel.RemoveFromRolesAsync(user, list);
            return Ok("Removed role " + roles[0] + " from " + username);
        }

        [HttpDelete]
        [Route("{username}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<string>> DeleteUser(string username)
        {
            var user = await _userModel.FindByNameAsync(username);
            if (user == null)
            {
                return NotFound("User does not exists");
            }
            var result = await _userModel.DeleteAsync(user);
            if(result == null)
            {
                return NotFound();
            }
            return Ok("User " + username + " deleted");
        }
    }
}
