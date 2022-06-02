using Server.RequestModels;
using System;
using System.ComponentModel.DataAnnotations;

namespace Server.Models
{
    public class RequestModel
    {
        public RequestModel() { }

        public RequestModel(long _Id, string _Username, string _Building, string _Handyman, string _Description, string _RoomNumber, string _Cause, string _Category, string _Priority, string _Status, DateTime _dateTime, DateTime _acceptedDate)
        {
            Id = _Id;
            Username = _Username;
            Building = _Building;
            Handyman = _Handyman;
            Description = _Description;
            RoomNumber = _RoomNumber;
            Cause = _Cause;
            Category = _Category;
            Priority = _Priority;
            Status = _Status;
            dateTime = _dateTime;
            acceptedDate = _acceptedDate;
        }

        public RequestModel(long _Id, RequestsCall _request)
        {
            Id= _Id;
            Username = _request.Username;
            Building = _request.Building;
            Handyman = null;
            Description= _request.Description;
            RoomNumber = _request.RoomNumber;
            Cause= _request.Cause;
            Category= _request.Category;
            Priority= _request.Priority;
            Status = _request.Status;
            dateTime = _request.dateTime;
            acceptedDate = new DateTime();
        }

        public long Id { get; set; }

        public string Username { get; set; }

        public string Building { get; set; }

        public string Handyman { get; set; }
        
        public string Description { get; set; }
        
        public string RoomNumber { get; set; }
        
        public string Cause { get; set; }
        
        public string  Category { get; set; }
        
        public string Priority { get; set; }

        public string Status { get; set; }

        public DateTime dateTime { get; set; }

        public DateTime acceptedDate { get; set; }  

    }
}
