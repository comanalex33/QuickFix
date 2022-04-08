using Microsoft.EntityFrameworkCore;

namespace Server.Models
{
    public class AppDbContext: DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options): base(options) { }

        public DbSet<UserModel> Users { get; set; }
    }
}
