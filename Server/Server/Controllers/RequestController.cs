using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Server.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Server.Controllers
{
    [Route("api/requests")]
    [ApiController]
    public class RequestController : ControllerBase
    {

        private readonly AppDbContext _context;

        public RequestController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<RequestModel>>> GetAllRequests()
        {
            return await _context.Request.ToListAsync();
        }
    }
}
