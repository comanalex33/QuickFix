namespace Server.Models
{
    public class CategoryModel
    {
        public CategoryModel() { }
        public CategoryModel(long _Id, string _Name)
        {
            Id = _Id;
            Name = _Name;
        }
        public long Id { get; set; }
        public string Name { get; set; }
    }
}
