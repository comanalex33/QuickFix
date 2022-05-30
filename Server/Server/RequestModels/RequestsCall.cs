using System;

namespace Server.RequestModels
{
    public class RequestsCall
    {
        public string Username { get; set; }
        
        public string Description { get; set; }
        
        public string RoomNumber { get; set; }
        
        public string Cause { get; set; }
        
        public string Category { get; set; }
        
        public string Priority { get; set; }

        public string Status { get; set; }

        public DateTime dateTime { get; set; }

    }
}
