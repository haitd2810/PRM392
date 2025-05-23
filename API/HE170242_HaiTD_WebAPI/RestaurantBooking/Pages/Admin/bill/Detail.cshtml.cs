using DataLibrary.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;

namespace RestaurantBooking.Pages.Admin.bill
{
    public class DetailModel : PageModel
    {
        public List<BillInfor> billInfors { get; set; } = new List<BillInfor>();
        public Bill BillDetail { get; set; }
        public IActionResult OnGet(string? id)
        {
            if (HttpContext.Session.GetString("role") == null ||
                HttpContext.Session.GetString("role") != "Admin") return Redirect("/Restaurant");
            if (id != null)
            {
                BillDetail = RestaurantContext.Ins.Bills.Find(int.Parse(id));
                billInfors = RestaurantContext.Ins.BillInfors.Include(x => x.Menu).Include(x => x.Bill).Where(x => x.Bill.Id == BillDetail.Id).ToList();
                float? total = 0;
                for (int i=0;i<billInfors.Count;i++)
                {
                    total += (float)billInfors[i].Price;
                }
                ViewData["total"]=total;
            }
            return Page();
        }
    }
}
