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
using Server.RequestModels;

namespace Server.Controllers
{
    [Route("api/category")]
    [ApiController]
    public class CategoryController : Controller
    {
        private readonly AppDbContext _context;

        public CategoryController(AppDbContext context)
        {
            _context = context;
        }


        [HttpGet]
        public async Task<ActionResult<IEnumerable<CategoryModel>>> GetAllCategories()
        {
            return await _context.Category.ToListAsync();
        }



        [HttpGet("{id}")]
        public async Task<ActionResult<CategoryModel>> Get(long id)
        {
            var category = await _context.Category.FindAsync(id);
            if (category == null)
            {
                return BadRequest("This category does not exist!");
            }
            return Ok(category);
        }

        [HttpPost]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<CategoryModel>> AddCategory(SimpleRequest requestModel)
        {
            var categoryCheck = _context.Category.Where(item => item.Name == requestModel.Name).FirstOrDefault();
            if (categoryCheck != null)
            {
                return BadRequest(new { message = "Category already exists" });
            }

            long Id = _context.Category.Count() + 1;

            categoryCheck = await _context.Category.FindAsync(Id);
            while (categoryCheck != null)
            {
                Id = Id + 1;
                categoryCheck = await _context.Category.FindAsync(Id);
            }

            CategoryModel category = new CategoryModel(Id, requestModel.Name);
            _context.Category.Add(category);
            await _context.SaveChangesAsync();

            return category;
        }


        [HttpDelete("{id}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<CategoryModel>> Delete(long id)
        {
            var category = await _context.Category.FindAsync(id);
            if (category == null)
            {
                return BadRequest("This category does not exist!");
            }
            _context.Category.Remove(category);
            await _context.SaveChangesAsync();

            return category;
        }

        [HttpPut("{id}/name/{name}")]
        [Authorize(Roles = "admin")]
        public async Task<ActionResult<CategoryModel>> UpdateName(string name, long id)
        {
            var category = await _context.Category.FindAsync(id);
            if (category == null)
            {
                return BadRequest("This category does not exist!");
            }

            category.Name = name;

            _context.Category.Update(category);
            _context.SaveChanges();

            return category;
        }
    }
}
