using Microsoft.EntityFrameworkCore;

namespace JetStoryAPI.Models
{
  public class StoryContext : DbContext
  {
    public StoryContext(DbContextOptions<StoryContext> options) : base(options)
    {
    }

    public DbSet<Story> Stories { get; set; }
  }
}