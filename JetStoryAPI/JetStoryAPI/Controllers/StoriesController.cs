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
        }


        [HttpGet]
        public async Task<ActionResult<IEnumerable<Story>>> Get()
        {
            return await _context.Stories.ToListAsync();
        }

        [HttpGet("{id:int}")]
        public async Task<ActionResult<Story>> Get(int id)
        {
            var story = await _context.Stories.FindAsync(id);
            if (story == null) {
                return NotFound ("The item: " + id + " does not exist!");
            }

            return story;
        }

        [HttpGet("{category}")]
        public async Task<ActionResult<IEnumerable<Story>>> Get(string category)
        {
            var stories = await _context.Stories.Where(story => story.Category == category).ToListAsync();
            if (stories == null) {
                return NotFound ("The category: " + category + " does not exist!");
            }
            return stories;
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