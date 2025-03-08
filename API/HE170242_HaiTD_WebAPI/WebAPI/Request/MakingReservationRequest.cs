namespace WebAPI.Request
{
    public class MakingReservationRequest
    {
        public string StartDate { get; set; }
        public string? Status { get; set; }
        public string? Phone { get; set; }
        public string? FullName { get; set; }
        public int? AccountId { get; set; }
    }
}
