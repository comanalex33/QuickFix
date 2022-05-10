using Microsoft.AspNetCore.Identity;

namespace Server.Models
{
    public class User : IdentityUser
    {
        public long buildingId { get; set; }
    }
}
