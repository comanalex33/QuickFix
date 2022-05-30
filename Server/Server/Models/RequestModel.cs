﻿using Server.RequestModels;
using System.ComponentModel.DataAnnotations;

namespace Server.Models
{
    public class RequestModel
    {
        public RequestModel() { }

        public RequestModel(long _Id, string _UserId, string _Description, string _RoomNumber, string _Cause, long _CategoryId, string _Priority)
        {
            Id = _Id;
            UserId = _UserId;
            Description = _Description;
            RoomNumber = _RoomNumber;
            Cause = _Cause;
            CategoryId = _CategoryId;
            Priority = _Priority;         
        }

        public RequestModel(long _Id, RequestsCall _request)
        {
            Id= _Id;
            UserId = _request.UserId;
            Description= _request.Description;
            RoomNumber = _request.RoomNumber;
            Cause= _request.Cause;
            CategoryId= _request.CategoryId;
            Priority= _request.Priority;
        }

        public long Id { get; set; }

        public string UserId { get; set; }
        
        public string Description { get; set; }
        
        public string RoomNumber { get; set; }
        
        public string Cause { get; set; }
        
        public long  CategoryId { get; set; }
        
        public string Priority { get; set; }

    }
}