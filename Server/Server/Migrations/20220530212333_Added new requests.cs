using Microsoft.EntityFrameworkCore.Migrations;

namespace Server.Migrations
{
    public partial class Addednewrequests : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "UserId",
                table: "Request",
                newName: "Username");

            migrationBuilder.RenameColumn(
                name: "CategoryId",
                table: "Request",
                newName: "Category");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Username",
                table: "Request",
                newName: "UserId");

            migrationBuilder.RenameColumn(
                name: "Category",
                table: "Request",
                newName: "CategoryId");
        }
    }
}
