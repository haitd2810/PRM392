using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;

namespace WebAdmin.Pages.Admin
{
    public class AccountListModel : PageModel
    {
        private readonly RestaurantContext context = new RestaurantContext();
        public List<Account> account { get; set; }
        public void OnGet()
        {
            account = context.Accounts.Where(x => x.IsActive == true).Include(x => x.Role).ToList();
        }
		public IActionResult OnGetDelete(int id)
		{
			Account acc = context.Accounts.Where(x => x.Id == id).FirstOrDefault();
            acc.IsActive = false;
            context.Accounts.Update(acc);
			context.SaveChanges();
			return Redirect("/Admin/Account");
		}
	}
}
