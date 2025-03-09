using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using WebAPI.Models;

namespace WebAdmin.Pages.Admin
{
    public class TableListModel : PageModel
    {
        private readonly RestaurantContext context = new RestaurantContext();
        public List<Table> tables { get; set; }
        public void OnGet()
        {
            tables = context.Tables.Where(x => x.DeleteFlag == false).ToList();
        }
        public IActionResult OnPost() {
            Table table = new Table();
            table.CreateAt = DateTime.Now;
            table.IsOrder = false;
            table.ForBooking = false;
            table.DeleteFlag = false;
            context.Tables.Add(table);
            context.SaveChanges();
            return Redirect("/Admin/Table");
        }
        public IActionResult OnGetDelete(int id)
        {
            Table table = context.Tables.Where(x => x.Id == id).FirstOrDefault();
            context.Tables.Remove(table);
            context.SaveChanges();
            return Redirect("/Admin/Table");
        }
    }
}
