namespace JetStoryAPI.Models
{
  public class Story
  {
    public int Id { get; set; }
    public string Name { get; set; }
    public string Content { get; set; }
    public string Category { get; set; }
    public int Time { get; set; }
    public string writer { get; set; }
    public string publishedDate { get; set; }
  }
}