using Microsoft.AspNetCore.Mvc;
using System.Security.Principal;
using WebAPI.Models;
using WebAPI.Request;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ChangePassController : ControllerBase
    {
        // GET: api/<ChangePassController>
        //[HttpGet]
        //public IEnumerable<string> Get()
        //{
        //    return new string[] { "value1", "value2" };
        //}

        // GET api/<ChangePassController>/5
        //[HttpGet("{id}")]
        //public string Get(int id)
        //{
        //    return "value";
        //}

        // POST api/<ChangePassController>
        //[HttpPost]
        //public void Post([FromBody] string value)
        //{
        //}

        // PUT api/<ChangePassController>/5
        [HttpPut("{id}")]
        public IActionResult Put(int id, [FromBody] UpdatePasswordRequest request)
        {
            Account acc = RestaurantContext.ins.Accounts.Where(x => x.Id == id).FirstOrDefault();
            if (acc != null) {
                if (request.repeatPassword == request.newPassword)
                {
                    bool password = BCrypt.Net.BCrypt.Verify(request.oldPassword, acc.Password);
                    if (password == false)
                    {
                        return BadRequest("Old password is incorrect");
                    }
                    string hash = BCrypt.Net.BCrypt.HashPassword(request.newPassword);
                    acc.Password = hash;
                    RestaurantContext.ins.Accounts.Update(acc);
                    RestaurantContext.ins.SaveChanges();
                    return Ok("Password is changed");
                }
                return BadRequest("New password and repeat password is not the same!");
            }
            return NotFound("Account not found!");
        }

        // DELETE api/<ChangePassController>/5
        //[HttpDelete("{id}")]
        //public void Delete(int id)
        //{
        //}
    }
}
