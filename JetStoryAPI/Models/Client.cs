namespace ColorsAPI.Models
{
  public class Client
  {
    public Client(int id, string name, string email, string password)
    {
      this.Id = id;
      this.Name = name;
      this.Email = email;
      this.Password = password;
    }

    public int Id { get; set; }
    public string Name { get; set; }
    public string Email { get; set; }
    public string Password { get; set; }
  }
}