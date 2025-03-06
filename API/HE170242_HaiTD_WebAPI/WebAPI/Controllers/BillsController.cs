using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Globalization;
using WebAPI.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BillsController : ControllerBase
    {
        // GET: api/<BIllsController>
        [HttpGet("{date}")]
        public IActionResult Get(string date)
        {
            date = Uri.UnescapeDataString(date).Trim();
            if (!DateTime.TryParseExact(date, "dd/MM/yyyy", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime dateTime))
            {
                return BadRequest("Invalid date format. Expected format: dd/MM/yyyy");
            }

            List<Bill> bills = RestaurantContext.ins.Bills
                .Include(x => x.BillInfors)
                .Where(x => x.CreateAt.Value.Date == dateTime.Date && x.Payed == true)
                .ToList();

            return Ok(bills);
        }

        //// GET api/<BIllsController>/5
        //[HttpGet("{id}")]
        //public string Get(int id)
        //{
        //    return "value";
        //}

        //// POST api/<BIllsController>
        //[HttpPost]
        //public void Post([FromBody] string value)
        //{
        //}

        //// PUT api/<BIllsController>/5
        //[HttpPut("{id}")]
        //public void Put(int id, [FromBody] string value)
        //{
        //}

        //// DELETE api/<BIllsController>/5
        //[HttpDelete("{id}")]
        //public void Delete(int id)
        //{
        //}
    }
}
