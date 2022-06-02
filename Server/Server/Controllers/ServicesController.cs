using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Server.ResponseModels;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace Server.Controllers
{
    [Route("api/services")]
    [ApiController]
    public class ServicesController : ControllerBase
    {
        [HttpPost]
        [Route("notify/title/{title}/message/{message}/topic/{topic}")]
        [Authorize(Roles = "admin,student,handyman")]
        public async Task<ActionResult<GenericResponseModel>> notify(string title, string message, string topic)
        {
            var notification = new Message()
            {
                Data = new Dictionary<string, string>()
                {
                    ["FirstName"] = "Quick",
                    ["LastName"] = "Fix"
                },
                Notification = new Notification
                {
                    Title = title,
                    Body = message
                },
                Topic = topic
            };

            var messaging = FirebaseMessaging.DefaultInstance;
            var result = await messaging.SendAsync(notification);

            return Ok(new { message = result});
        }
    }
}
