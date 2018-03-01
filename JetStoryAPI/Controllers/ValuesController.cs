using System.Collections.Generic;
using System.Linq;
using ColorsAPI.Models;
using Microsoft.AspNetCore.Mvc;

namespace ColorsAPI.Controllers
{
  [Route("api/[controller]")]
  public class ValuesController : Controller
  {
    private readonly List<Colors> _theRules =
      new List<Colors>
      {
        new Colors(
          1,
          "cuisine",
          "tarte",
          "blue",
          "black",
          "white",
          new Client(1, "amine", "a@a.com", "0000")
        ),
        new Colors(
          2,
          "qqchose",
          "subcat",
          "red",
          "black",
          "white",
          new Client(2, "Loubna", "email", "pw")
        )
      };

    ColorsContext _context;

    // GET api/values
    [HttpGet]
    public IEnumerable<Colors> Get()
    {
      return _context.colors.ToList();
    }

    // GET api/values/5
    [HttpGet("{id}")]
    public Colors Get(int id)
    {
      if (id < _theRules.Count && id >= 0)
      {
        return _theRules.ElementAt(id);
      }
      else
      {
        return null;
      }
    }

    // POST api/values
    [HttpPost]
    public void Post([FromBody] string value)
    {
    }

    // PUT api/values/5
    [HttpPut("{id}")]
    public void Put(int id, [FromBody] string value)
    {
    }

    // DELETE api/values/5
    [HttpDelete("{id}")]
    public void Delete(int id)
    {
    }
  }
}