using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Server.Models;
using Server.RequestModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Server.Controllers
{
    [Route("api/requests")]
    [ApiController]
    public class RequestController : ControllerBase
    {

        private readonly AppDbContext _context;
        private readonly UserManager<User> _userModel;

        public RequestController(AppDbContext context, UserManager<User> userModel)
        {
            _context = context;
            _userModel = userModel;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<RequestModel>>> GetAllRequests()
        {
            return await _context.Request.ToListAsync();
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<RequestModel>> Get(long id)
        {
            var request = await _context.Request.FindAsync(id);
            if (request == null)
            {
                return BadRequest("This request does not exist");
            }
            return Ok(request);
        }

        [HttpGet]
        [Route("users/{username}")]
        public async Task<ActionResult<IEnumerable<RequestModel>>> GetRequestByName(string username)
        {

            var user = await _userModel.FindByNameAsync(username);
            if (user == null)
            {
                return BadRequest("This user does not exist");
            }

            var requests = _context.Request.Where(item => item.Username == username);
  
            if (requests == null)
            {
                requests = Enumerable.Empty<RequestModel>().AsQueryable();
            }
            return Ok(requests);
        }

        [HttpPost]
        [Authorize(Roles = "admin,student,handyman")]
        public async Task<ActionResult<RequestModel>> AddRequest(RequestsCall requestModel)
        {
            long Id = _context.Request.Count() + 1;

            var user = await _userModel.FindByNameAsync(requestModel.Username);
            if (user == null)
            {
                return BadRequest("This user does not exist");
            }

            var building = await _context.Building.FirstOrDefaultAsync(item => item.Name == requestModel.Building);
            if (building == null)
            {
                return BadRequest("This building does not exist");
            }

            var category = await _context.Category.FirstOrDefaultAsync(item => item.Name == requestModel.Category);
            if (category == null)
            {
                return BadRequest("This category does not exist");
            }

            var requestCheck = await _context.Request.FindAsync(Id);
            while (requestCheck != null)
            {
                Id = Id + 1;
                requestCheck = await _context.Request.FindAsync(Id);
            }

            RequestModel request = new RequestModel(Id, requestModel.Username, requestModel.Building, null, requestModel.Description, requestModel.RoomNumber, requestModel.Cause, requestModel.Category, requestModel.Priority, requestModel.Status, requestModel.dateTime, new System.DateTime());
            _context.Request.Add(request);
            await _context.SaveChangesAsync();

            return request;
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<RequestModel>> Delete(long id)
        {
            var request = await _context.Request.FindAsync(id);
            if (request == null)
            {
                return BadRequest("This request does not exist!");
            }
            _context.Request.Remove(request);
            await _context.SaveChangesAsync();

            return request;
        }

        [HttpPut("{id}/status/{status}")]
        [Authorize(Roles = "handyman")]
        public async Task<ActionResult<RequestModel>> UpdateStatus(string status, long id)
        {
            var request = await _context.Request.FindAsync(id);
            if (request == null)
            {
                return BadRequest("This request does not exist!");
            }

            request.Status = status;

            _context.Request.Update(request);
            _context.SaveChanges();

            return request;
        }

        [HttpPut("{id}/status/{status}/handyman/{handyman}/time/{dateTime}")]
        [Authorize(Roles = "handyman")]
        public async Task<ActionResult<RequestModel>> UpdateMoreStatus(string status, long id, string handyman, DateTime dateTime)
        {
            var request = await _context.Request.FindAsync(id);
            if (request == null)
            {
                return BadRequest("This request does not exist!");
            }

            var user = await _userModel.FindByNameAsync(handyman);
            if (user == null)
            {
                return BadRequest("This handyman does not exist");
            }

            request.Status = status;
            request.Handyman = handyman;
            request.acceptedDate = dateTime;

            _context.Request.Update(request);
            _context.SaveChanges();

            return request;
        }
    }
}
