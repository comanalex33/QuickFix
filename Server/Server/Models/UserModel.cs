using Server.RequestModels;
using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Server.Models
{
    public class UserModel
    {
        public UserModel() { }
        public UserModel(long _Id, UserRequestModel _requestModel)
        {
            Id = _Id;
            Name = _requestModel.Name;
            Email = _requestModel.Email;
            Password = _requestModel.Password;
            Role = _requestModel.Role;
        }

        public long Id { get; set; }

        public string Name { get; set; }

        public string Email { get; set; }

        public string Password { get; set; }

        public string Role { get; set; }
    }
}
