using Server.Models;
using Server.RequestModels;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {

        private readonly AppDbContext _context;

        public UserController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<UserModel>>> GetAllUsers()
        {
            return await _context.Users.ToListAsync();
        }

        [HttpPost]
        public async Task<ActionResult<UserModel>> AddUser(UserRequestModel requestUser)
        {
            long Id = _context.Users.Count() + 1;

            var userIdCheck = await _context.Users.FindAsync(Id);
            while(userIdCheck != null)
            {
                Id = Id + 1;
                userIdCheck = await _context.Users.FindAsync(Id);
            }

            UserModel user = new UserModel(Id, requestUser);
            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            return user;
        }
    }
}
