using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Server.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.EntityFrameworkCore;
using System;

namespace Server.Controllers
{
    [Route("api/buildings")]
    [ApiController]
    public class BuildingController : ControllerBase
    {
        private readonly AppDbContext _context;
        
        public BuildingController(AppDbContext context)
        {
            _context = context;
        }

        
        [HttpGet]
        public async Task<ActionResult<IEnumerable<BuildingModel>>> GetAllBuildings()
        {
             return await _context.Building.ToListAsync();
        }
        
        [HttpGet("{id}")]
        public async Task<ActionResult<BuildingModel>> Get(long id)
        {
            var building = await _context.Building.FindAsync(id);
            if (building == null)
            {
                return BadRequest("This building does not exist!");
            }
            return Ok(building);    
        }

        [HttpPost]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<BuildingModel>> AddBuilding(string buildingName)
        {
            long Id = _context.Building.Count() + 1;

            var buildCheck = await _context.Building.FindAsync(Id);
            while (buildCheck != null)
            {
                Id = Id + 1;
                buildCheck = await _context.Building.FindAsync(Id);
            }

            BuildingModel build = new BuildingModel(Id, buildingName);
            _context.Building.Add(build);
            await _context.SaveChangesAsync();

            return build;
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<BuildingModel>> Delete(long id)
        {
            var building = await _context.Building.FindAsync(id);
            if (building == null)
            {
                return BadRequest("This building does not exist!");
            }
            _context.Building.Remove(building);
            await _context.SaveChangesAsync();

            return building;
        }
    }
}
