using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace Server.Migrations
{
    public partial class UpdatedRequestsFields : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Building",
                table: "Request",
                type: "text",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "Handyman",
                table: "Request",
                type: "text",
                nullable: true);

            migrationBuilder.AddColumn<DateTime>(
                name: "acceptedDate",
                table: "Request",
                type: "timestamp without time zone",
                nullable: false,
                defaultValue: new DateTime(1, 1, 1, 0, 0, 0, 0, DateTimeKind.Unspecified));
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Building",
                table: "Request");

            migrationBuilder.DropColumn(
                name: "Handyman",
                table: "Request");

            migrationBuilder.DropColumn(
                name: "acceptedDate",
                table: "Request");
        }
    }
}
