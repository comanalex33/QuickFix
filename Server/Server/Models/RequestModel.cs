namespace Server.Models
{
    public class RequestModel
    {
        public RequestModel() { }

        public RequestModel(long _RequestId, long _UserId, string _Description, string _RoomNumber, string _Cause, long _CategoryId, string _Priority)
        {
            RequestId = _RequestId;
            UserId = _UserId;
            Description = _Description;
            RoomNumber = _RoomNumber;
            Cause = _Cause;
            CategoryId = _CategoryId;
            Priority = _Priority;         
        }

        public long RequestId { get; set; }

        public long UserId { get; set; }
        
        public string Description { get; set; }
        
        public string RoomNumber { get; set; }
        
        public string Cause { get; set; }
        
        public long  CategoryId { get; set; }
        
        public string Priority { get; set; }

    }
}
