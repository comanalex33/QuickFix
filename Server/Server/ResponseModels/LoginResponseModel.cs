using System;

namespace Server.ResponseModels
{
    public class LoginResponseModel
    {

        public string Token { get; set; }
        public DateTime Expiration { get; set; }
    }
}
