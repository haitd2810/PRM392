using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;

namespace WebAdmin.Pages.Admin
{
    public class MenuModel : PageModel
    {
        private readonly RestaurantContext context = new RestaurantContext();
        public List<Menu> menus { get; set; }
        public void OnGet()
        {
            menus = context.Menus.Where(x => x.DeleteFlag == false).Include(x => x.Cate).ToList();
        }
		public IActionResult OnGetDelete(int id)
		{
			Menu menu = menus.FirstOrDefault(x => x.Id == id);
			menu.DeleteFlag = true;
			context.Menus.Update(menu);
			context.SaveChanges();
			return Redirect("/Admin/Menu");
		}
	}
}
