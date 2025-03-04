namespace WebAPI.Request
{
    public class CreateOrderRequest
    {
        public int tableId { get; set; }
        public List<billlItems> items { get; set; }
    }
    public class billlItems
    {
        public int menuId { get; set; }
        public int quantity { get; set; }
        public double price { get; set; }
    }
}
