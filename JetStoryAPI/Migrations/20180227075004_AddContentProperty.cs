﻿using Microsoft.EntityFrameworkCore.Migrations;

namespace JetStoryAPI.Migrations
{
  public partial class AddContentProperty : Migration
  {
    protected override void Up(MigrationBuilder migrationBuilder)
    {
      migrationBuilder.AddColumn<string>(
        name: "Content",
        table: "Stories",
        nullable: true);
    }

    protected override void Down(MigrationBuilder migrationBuilder)
    {
      migrationBuilder.DropColumn(
        name: "Content",
        table: "Stories");
    }
  }
}