using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TableDetailsController : ControllerBase
    {
        // GET: api/<TableDetailsController>
        //[HttpGet]
        //public IEnumerable<string> Get()
        //{
        //    return new string[] { "value1", "value2" };
        //}

        // GET api/<TableDetailsController>/5
        [HttpGet("{id}")]
        public IActionResult Get(int id)
        {
            List<Bill> bill = RestaurantContext.ins.Bills.Where(b => b.TableId == id).Include(b => b.Table).Include(b => b.BillInfors).ThenInclude(b => b.Menu).Where(b => b.Paid == false).ToList();
            return Ok(bill);
        }

        // POST api/<TableDetailsController>
        //[HttpPost]
        //public void Post([FromBody] string value)
        //{
        //}

        //// PUT api/<TableDetailsController>/5
        //[HttpPut("{id}")]
        //public void Put(int id, [FromBody] string value)
        //{
        //}

        //// DELETE api/<TableDetailsController>/5
        //[HttpDelete("{id}")]
        //public void Delete(int id)
        //{
        //}
    }
}
