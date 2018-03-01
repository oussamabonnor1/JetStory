namespace ColorsAPI.Models
{
  public class Colors
  {
    public Colors(int id, string category, string subCategory, string navColor, string bodyColor, string secondery,
      Client client)
    {
      this.Id = id;
      this.Category = category;
      this.SubCategory = subCategory;
      this.NavColor = navColor;
      this.BodyColor = bodyColor;
      this.Secondery = secondery;
      this.Client = client;
    }

    public int Id { get; set; }
    public string Category { get; set; }
    public string SubCategory { get; set; }
    public string NavColor { get; set; }
    public string BodyColor { get; set; }
    public string Secondery { get; set; }
    public Client Client { get; set; }
  }
}