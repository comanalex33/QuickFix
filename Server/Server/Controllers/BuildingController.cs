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
using Server.Services;
using Server.RequestModels;

namespace Server.Controllers
{
    [Route("api/buildings")]
    [ApiController]
    public class BuildingController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IEmailService _emailService;

        public BuildingController(AppDbContext context, IEmailService emailService)
        {
            _context = context;
            _emailService = emailService;
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
        public async Task<ActionResult<BuildingModel>> AddBuilding(SimpleRequest requestModel)
        {
            var buildingCheck = _context.Building.Where(item => item.Name == requestModel.Name).FirstOrDefault();
            if(buildingCheck != null)
            {
                return BadRequest(new { message = "Building already exists" });
            }

            long Id = _context.Building.Count() + 1;

            buildingCheck = await _context.Building.FindAsync(Id);
            while (buildingCheck != null)
            {
                Id = Id + 1;
                buildingCheck = await _context.Building.FindAsync(Id);
            }

            BuildingModel build = new BuildingModel(Id, requestModel.Name);
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

        [HttpPut("{id}/name/{name}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<BuildingModel>> UpdateName(string name, long id)
        {
            var building = await _context.Building.FindAsync(id);
            if (building == null)
            {
                return BadRequest("This building does not exist");
            }

            var buildingCheck = _context.Building.Where(item => item.Name == name).FirstOrDefault();
            if (buildingCheck != null)
            {
                return BadRequest( "This building already exists");
            }

            building.Name = name;

            _context.Building.Update(building);
            _context.SaveChanges();

            return building;
        }
    }
}
