using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using WebAPI.Models;

namespace WebAdmin.Pages.Admin
{
    public class IngredientsModel : PageModel
    {
        private readonly RestaurantContext context = new RestaurantContext();
        public List<Ingredient> ingredient { get; set; }
        public void OnGet()
        {
            ingredient = context.Ingredients.ToList();
        }
		public IActionResult OnGetDelete(int id)
		{
			Ingredient ing = context.Ingredients.Where(x => x.Id == id).FirstOrDefault();
			context.Ingredients.Remove(ing);
			context.SaveChanges();
			return Redirect("/Admin/Ingredient");
		}
	}
}
