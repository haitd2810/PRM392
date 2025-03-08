using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;
using WebAPI.Request;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BookingController : ControllerBase
    {
        // GET: api/<BookingController>
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(int id)
        {
            List<Booking> bookings = RestaurantContext.ins.Bookings.Where(x => x.AccountId == id).OrderByDescending(x => x.StartDate).ToList();
            return Ok(bookings);
        }

        // GET api/<BookingController>/5
        [HttpGet("{id}/Detail")]
        public async Task<IActionResult> GetDetail(int id)
        {
            Booking book = await RestaurantContext.ins.Bookings.Where(x => x.Id == id).FirstOrDefaultAsync();
            if(book == null) return NotFound(); 
            return Ok(book);
        }

        // POST api/<BookingController>
        [HttpPost]
        public async  Task<IActionResult> Post([FromBody] MakingReservationRequest request)
        {
            List<Booking> bookingList = new List<Booking>();
            int numberOfBookings = 1;
            if(request.AccountId != null)
            {
                bookingList = await RestaurantContext.ins.Bookings.Where(x => x.AccountId == request.AccountId).ToListAsync();
                numberOfBookings = bookingList.Count + 1;
            }
            DateTime inputTime = DateTime.ParseExact(request.StartDate, "dd/MM/yyyy HH:mm", null);
            List<Booking> AllList = await RestaurantContext.ins.Bookings.ToListAsync();
            bool isOver45Minutes = AllList
            .Any(dbTime => Math.Abs((dbTime.StartDate.Value - inputTime).TotalMinutes) < 45);
            if (isOver45Minutes) {
                return BadRequest("this schedule has been booked by other person");
            }
            if(request.AccountId != null)
            {
                List<Booking> BookedList = await RestaurantContext.ins.Bookings.Where(x => x.Status == "booked"
                    && x.AccountId == request.AccountId).ToListAsync();
                bool isBookingNotUse = BookedList.Any(x => x.StartDate.Value.CompareTo(DateTime.Now) > 0);
                if (isBookingNotUse)
                {
                    return BadRequest("You have a reservation exist! Cannot make more reservation");
                }
            }
            

            Booking booking = new Booking();
            booking.StartDate = inputTime;
            booking.FullName = request.FullName;
            booking.Phone = request.Phone;
            booking.Status = "booked";
            if(request.AccountId != null)
            {
                booking.AccountId = request.AccountId;
                booking.NumberOfBooking = numberOfBookings;
            }
            RestaurantContext.ins.Add(booking);
            await RestaurantContext.ins.SaveChangesAsync();
            return Ok();
        }

        // PUT api/<BookingController>/5
        //[HttpPut("{id}")]
        //public void Put(int id, [FromBody] string value)
        //{
        //}

        //// DELETE api/<BookingController>/5
        [HttpDelete("{id}")]
        public async  Task<IActionResult> Delete(int id)
        {
            Booking book = await RestaurantContext.ins.Bookings.Where(x => x.Id == id).FirstOrDefaultAsync();
            if (book == null) return NotFound();
            RestaurantContext.ins.Bookings.Remove(book);
            await RestaurantContext.ins.SaveChangesAsync();
            return Ok();
        }
    }
}
