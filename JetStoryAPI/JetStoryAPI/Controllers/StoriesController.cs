using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using JetStoryAPI.Models;
using Microsoft.AspNetCore.Mvc;

namespace JetStoryApi.Controllers
{
    [Route("api/[controller]")]
    [Produces("application/json")]
    public class StoriesController : Controller
    {
        private readonly StoryContext _context;

        public StoriesController(StoryContext context)
        {
            _context = context;
            if (_context.Stories.Count() == 0)
            {
                _context.Stories.Add(new Story
                {
                    Name = "something"
                });
                _context.SaveChanges();
            }
        }


        [HttpGet]
        public async Task<ActionResult<IEnumerable<Story>>> GetAll()
        {
            return await _context.Stories.ToListAsync();
        }

        [HttpGet("{name}")]
        public async Task<ActionResult<Story>> Get(int id)
        {
            var story = await _context.Stories.FindAsync(id);
            if (story == null) {
                return NotFound ("The item: " + id + " does not exist!");
            }

            return story;
        }

        [HttpPost]
        public async Task<ActionResult<Story>> Post([FromBody] Story story)
        {
             if (!ModelState.IsValid) {
                return BadRequest (ModelState);
            }
            _context.Stories.Add(story);
            await _context.SaveChangesAsync();
            return CreatedAtAction ("get", new { story.Id }, story);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id, [FromBody] Story story)
        {
            if (id != story.Id) {
                return BadRequest ("Ids doesn't match");
            }
            _context.Entry (story).State = EntityState.Modified;
            await _context.SaveChangesAsync ();
            return Ok ();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
           Story temp = await _context.Stories.FindAsync (id);
            if (temp == null) {
                return NotFound ("Object not found, Id: " + id);
            }
            _context.Remove (temp);
            await _context.SaveChangesAsync ();
            return Ok ();
        }
    }
}