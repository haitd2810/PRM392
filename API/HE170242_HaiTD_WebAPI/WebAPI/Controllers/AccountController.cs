using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;
using WebAPI.Request;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class AccountController : ControllerBase
	{
		// GET: api/<AccountController>
		//[HttpGet]
		//public IEnumerable<string> Get()
		//{
		//	return new string[] { "value1", "value2" };
		//}

		// GET api/<AccountController>/5
		//[HttpGet("{id}")]
		//public string Get(int id)
		//{
		//	return "value";
		//}

		// POST api/<AccountController>
		[HttpPost]
		public async Task<IActionResult> Post([FromBody] RegisterRequest request)
		{
			if(request.password.Length == 0 ||  request.repeatPassword.Length == 0 || request.username.Length == 0)
			{
				return BadRequest("Data cannot null");
			}

			if (!request.password.Equals(request.repeatPassword))
			{
				return BadRequest("password and repeat password must be the same");
			}
			Account acc = await RestaurantContext.ins.Accounts.FirstOrDefaultAsync(x => x.Username.ToLower().Equals(request.username.ToLower()));
			if(acc != null)
			{
				return BadRequest("Account is exist");
			}
            string hash = BCrypt.Net.BCrypt.HashPassword(request.password);
            acc = new Account();
			acc.Username = request.username;
			acc.Password = hash;
			acc.RoleId = 3;
			acc.Code = "";
			acc.IsActive = true;
			acc.CreateAt = DateTime.Now;
			RestaurantContext.ins.Accounts.Add(acc);
			await RestaurantContext.ins.SaveChangesAsync();
			return Ok();
		}

		// PUT api/<AccountController>/5
		//[HttpPut("{id}")]
		//public void Put(int id, [FromBody] string value)
		//{
		//}

		//// DELETE api/<AccountController>/5
		//[HttpDelete("{id}")]
		//public void Delete(int id)
		//{
		//}
	}
}
