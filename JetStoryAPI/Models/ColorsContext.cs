using Microsoft.EntityFrameworkCore;

namespace ColorsAPI.Models
{
  public class ColorsContext : DbContext
  {
    public ColorsContext(DbContextOptions<ColorsContext> options) : base(options)
    {
    }

    public DbSet<Colors> colors { get; set; }
  }
}