using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;

namespace WebAdmin.Pages.Admin
{
    public class IndexModel : PageModel
    {
        public List<Bill> billPaid { get; set; } = new List<Bill>();
        public List<Bill> billUnPaid { get; set; } = new List<Bill>();
        public double? totalPrice { get; set; } = 0;
        public List<Ingredient> ingredient { get; set; } = new List<Ingredient>();
        public double? materialCost { get; set; } = 0;
        public List<Booking> bookings { get; set; } = new List<Booking>();
        public async Task<IActionResult> OnGet()
        {
			var currentMonth = DateTime.Now.Month;
			var currentYear = DateTime.Now.Year;
			billPaid = await RestaurantContext.ins.Bills
	                        .Where(x => x.Paid == true && x.CreateAt.Value.Month == currentMonth && x.CreateAt.Value.Year == currentYear)
	                        .Include(x => x.BillInfors)
	                        .ToListAsync();
			billUnPaid = await RestaurantContext.ins.Bills
							.Where(x => x.Paid == false && x.CreateAt.Value.Month == currentMonth && x.CreateAt.Value.Year == currentYear)
							.Include(x => x.BillInfors)
							.ToListAsync();
			foreach (var b in billPaid)
            {
                foreach(var bi in b.BillInfors)
                {
                    totalPrice += bi.Price;
                }
            }
            ingredient = await RestaurantContext.ins.Ingredients
                .Where(x => x.CreateAt.Value.Month == currentMonth && x.CreateAt.Value.Year == currentYear)
                .ToListAsync();
            foreach(var i in ingredient)
            {
                materialCost += i.Price;
            }

            bookings = await RestaurantContext.ins.Bookings.OrderByDescending(x => x.StartDate).Take(7).ToListAsync();


            return Page();
        }
    }
}
