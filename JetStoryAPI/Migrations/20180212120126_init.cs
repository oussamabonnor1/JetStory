using Microsoft.EntityFrameworkCore.Migrations;

namespace ColorsAPI.Migrations
{
  public partial class init : Migration
  {
    protected override void Up(MigrationBuilder migrationBuilder)
    {
      migrationBuilder.CreateTable(
        name: "Client",
        columns: table => new
        {
          Id = table.Column<int>(nullable: false)
            .Annotation("Sqlite:Autoincrement", true),
          Email = table.Column<string>(nullable: true),
          Name = table.Column<string>(nullable: true),
          Password = table.Column<string>(nullable: true)
        },
        constraints: table => { table.PrimaryKey("PK_Client", x => x.Id); });

      migrationBuilder.CreateTable(
        name: "colors",
        columns: table => new
        {
          Id = table.Column<int>(nullable: false)
            .Annotation("Sqlite:Autoincrement", true),
          BodyColor = table.Column<string>(nullable: true),
          Category = table.Column<string>(nullable: true),
          ClientId = table.Column<int>(nullable: true),
          NavColor = table.Column<string>(nullable: true),
          Secondery = table.Column<string>(nullable: true),
          SubCategory = table.Column<string>(nullable: true)
        },
        constraints: table =>
        {
          table.PrimaryKey("PK_colors", x => x.Id);
          table.ForeignKey(
            name: "FK_colors_Client_ClientId",
            column: x => x.ClientId,
            principalTable: "Client",
            principalColumn: "Id",
            onDelete: ReferentialAction.Restrict);
        });

      migrationBuilder.CreateIndex(
        name: "IX_colors_ClientId",
        table: "colors",
        column: "ClientId");
    }

    protected override void Down(MigrationBuilder migrationBuilder)
    {
      migrationBuilder.DropTable(
        name: "colors");

      migrationBuilder.DropTable(
        name: "Client");
    }
  }
}