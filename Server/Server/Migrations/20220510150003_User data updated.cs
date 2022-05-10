using Microsoft.EntityFrameworkCore.Migrations;

namespace Server.Migrations
{
    public partial class Userdataupdated : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<long>(
                name: "buildingId",
                table: "AspNetUsers",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "buildingId",
                table: "AspNetUsers");
        }
    }
}
