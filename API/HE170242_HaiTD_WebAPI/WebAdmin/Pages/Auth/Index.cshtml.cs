using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace WebAdmin.Pages.Auth
{
    public class IndexModel : PageModel
    {
		IConfiguration config = new ConfigurationBuilder()
				.SetBasePath(Directory.GetCurrentDirectory())
				.AddJsonFile("appsettings.json", true, true)
				.Build();
		public void OnGet()
        {
        }
		public IActionResult OnPost()
		{
			string email = Request.Form["email"];
			string password = Request.Form["password"];
			if (email == config["AdminAccount:Email"] && password == config["AdminAccount:Password"])
			{
				return Redirect("/Admin/Index");
			}
			else
			{
				return Page();
			}
		}
    }
}
