using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace WebAPI.Models
{
    public partial class Account
    {
        public Account()
        {
            Bookings = new HashSet<Booking>();
            Feedbacks = new HashSet<Feedback>();
            Tokens = new HashSet<Token>();
        }

        public int Id { get; set; }
        public string? Username { get; set; }
        public string? Password { get; set; }
        public string? Code { get; set; }
        public int? RoleId { get; set; }
        public bool? IsActive { get; set; }
        public DateTime? CreateAt { get; set; }
        public DateTime? UpdateAt { get; set; }
        public DateTime? DeleteAt { get; set; }

        public virtual Role? Role { get; set; }
        public virtual ICollection<Booking> Bookings { get; set; }
        [JsonIgnore]
        public virtual ICollection<Feedback> Feedbacks { get; set; }
        [JsonIgnore]
        public virtual ICollection<Token> Tokens { get; set; }
    }
}
