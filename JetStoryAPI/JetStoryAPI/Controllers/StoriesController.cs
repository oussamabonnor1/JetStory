using System.Collections.Generic;
using System.Linq;
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
        public IEnumerable<Story> GetAll()
        {
            return _context.Stories.ToList();
        }

        [HttpGet("{name}")]
        public IActionResult Get(string Name)
        {
            var story = _context.Stories.FirstOrDefault(t => t.Name == Name);
            return new ObjectResult(story);
        }

        [HttpPost]
        public void Post([FromBody] Story story)
        {
            _context.Stories.Add(story);
            _context.SaveChanges();
        }

        [HttpPut("{id}")]
        public void Edit(int id, [FromBody] Story story)
        {
            var storie = _context.Stories.FirstOrDefault(t => t.Id == id);
            storie.Name = story.Name;
            storie.Category = story.Category;
            storie.Content = story.Content;
            storie.Time = story.Time;
            storie.publishedDate = story.publishedDate;
            storie.writer = story.writer;
            _context.Stories.Update(storie);
            _context.SaveChanges();
        }

        [HttpDelete("{id}")]
        public void Delete(int id)
        {
            var storie = _context.Stories.FirstOrDefault(t => t.Id == id);
            _context.Stories.Remove(storie);
            _context.SaveChanges();
        }
    }
}