namespace WebAPI.Request
{
    public class UpdateOrderRequest
    {
        public int billInforId { get; set; }
        public int quantity { get; set; }
        public double price { get; set; }
        public int tableId { get; set; }
    }
}
