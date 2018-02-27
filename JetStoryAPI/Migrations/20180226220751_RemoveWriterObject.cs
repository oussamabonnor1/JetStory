using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using System;
using System.Collections.Generic;

namespace JetStoryAPI.Migrations
{
    public partial class RemoveWriterObject : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Stories_Writers_writerId",
                table: "Stories");

            migrationBuilder.DropTable(
                name: "Writers");

            migrationBuilder.DropIndex(
                name: "IX_Stories_writerId",
                table: "Stories");

            migrationBuilder.DropColumn(
                name: "writerId",
                table: "Stories");

            migrationBuilder.AddColumn<string>(
                name: "writer",
                table: "Stories",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "writer",
                table: "Stories");

            migrationBuilder.AddColumn<int>(
                name: "writerId",
                table: "Stories",
                nullable: true);

            migrationBuilder.CreateTable(
                name: "Writers",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    Name = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Writers", x => x.Id);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Stories_writerId",
                table: "Stories",
                column: "writerId");

            migrationBuilder.AddForeignKey(
                name: "FK_Stories_Writers_writerId",
                table: "Stories",
                column: "writerId",
                principalTable: "Writers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
