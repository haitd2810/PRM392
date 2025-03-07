using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace WebAPI.Models
{
    public partial class Booking
    {
        public int Id { get; set; }
        public int? TableId { get; set; }
        public DateTime? StartDate { get; set; }
        public string? Status { get; set; }
        public string? Email { get; set; }
        public string? Phone { get; set; }
        public string? FullName { get; set; }
        public DateTime? CreateAt { get; set; }
        public int? NumberOfBooking { get; set; }
        public int? AccountId { get; set; }
        [JsonIgnore]
        public virtual Account? Account { get; set; }
        [JsonIgnore]
        public virtual Table? Table { get; set; }
    }
}
