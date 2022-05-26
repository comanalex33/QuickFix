namespace Server.RequestModels
{
    public class RequestsCall
    {
        public long UserId { get; set; }
        
        public string Description { get; set; }
        
        public string RoomNumber { get; set; }
        
        public string Cause { get; set; }
        
        public long CategoryId { get; set; }
        
        public string Priority { get; set; }

    }
}
