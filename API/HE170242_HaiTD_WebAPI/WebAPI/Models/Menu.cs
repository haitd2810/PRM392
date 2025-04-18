﻿using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace WebAPI.Models
{
    public partial class Menu
    {
        public Menu()
        {
            BillInfors = new HashSet<BillInfor>();
            Feedbacks = new HashSet<Feedback>();
        }

        public int Id { get; set; }
        public string? Name { get; set; }
        public string? Detail { get; set; }
        public double? Price { get; set; }
        public string? Img { get; set; }
        public int? Quantity { get; set; }
        public int? CateId { get; set; }
        public bool? DeleteFlag { get; set; }
        public DateTime? CreateAt { get; set; }
        public DateTime? UpdateAt { get; set; }
        public DateTime? DeleteAt { get; set; }

        public virtual Category? Cate { get; set; }
        [JsonIgnore]
        public virtual ICollection<BillInfor> BillInfors { get; set; }
        [JsonIgnore]
        public virtual ICollection<Feedback> Feedbacks { get; set; }
    }
}
