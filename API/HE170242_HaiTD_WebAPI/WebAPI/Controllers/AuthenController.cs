using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;
using WebAPI.Request;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthenController : ControllerBase
    {
        //// GET: api/<AuthenController>
        //[HttpGet]
        //public IEnumerable<string> Get()
        //{
        //    return new string[] { "value1", "value2" };
        //}

        // GET api/<AuthenController>/5
        //[HttpGet("{id}")]
        //public string Get(int id)
        //{
        //    return "value";
        //}

        // POST api/<AuthenController>
        [HttpPost]
        public IActionResult Post([FromBody] LoginRequest request)
        {
            var account = RestaurantContext.ins.Accounts.Include(x => x.Role).FirstOrDefault(a => 
            a.Username.ToLower().Equals(request.email.ToLower()) && a.Password.Equals(request.password));
            if (account == null) return BadRequest("Email or Password is wrong");
            return Ok(account);
        }

        // PUT api/<AuthenController>/5
        //[HttpPut("{id}")]
        //public void Put(int id, [FromBody] string value)
        //{
        //}

        //// DELETE api/<AuthenController>/5
        //[HttpDelete("{id}")]
        //public void Delete(int id)
        //{
        //}
    }
}
