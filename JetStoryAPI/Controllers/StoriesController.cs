using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using JetStoryAPI.Models;
using Microsoft.AspNetCore.Mvc;

namespace JetStoryAPI.Controllers
{
    [Route("api/[controller]")]
    [Produces("application/json")]
    public class StoriesController : Controller
    {
       private readonly StoryContext _context;
       

        public StoriesController(StoryContext context)
        {
            _context = context;
            if(_context.Stories.Count()==0)
            {
            _context.Stories.Add(new Story{
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

        // GET api/values/5
        [HttpGet("{name}")]
        public IActionResult Get(string Name)
        {
            var story = _context.Stories.FirstOrDefault(t => t.Name == Name);
            return new ObjectResult(story);
        
        }

        // POST api/values
        [HttpPost]
        public void Post([FromBody]Story story)
        {
            _context.Stories.Add(story);
            _context.SaveChanges();
        }

        // PUT api/values/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
            var storie = _context.Stories.FirstOrDefault(t => t.Id == id);
            _context.Stories.Remove(storie);
            _context.SaveChanges();
        }
    }
}
