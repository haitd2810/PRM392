using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebAPI.Models;
using WebAPI.Request;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrdersController : ControllerBase
    {
        // GET: api/<OrdersController>
        //[HttpGet]
        //public IEnumerable<string> Get()
        //{
        //    return new string[] { "value1", "value2" };
        //}

        // GET api/<OrdersController>/5
        //[HttpGet("{id}")]
        //public string Get(int id)
        //{
        //    return "value";
        //}

        // POST api/<OrdersController>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] CreateOrderRequest request)
        {
            Bill bill = await RestaurantContext.ins.Bills.Where(x => x.TableId == request.tableId && x.Payed == false).FirstOrDefaultAsync();
            if (bill != null) {
                List<BillInfor> billInfor = await RestaurantContext.ins.BillInfors.Where(x => x.BillId == bill.Id).ToListAsync();
                for(int i=0; i < request.items.Count(); i++)
                {
                    BillInfor b = billInfor.Where(x => x.MenuId == request.items[i].menuId).FirstOrDefault();
                    if(b != null)
                    {
                        b.Quantity += request.items[i].quantity;
                        b.Price += request.items[i].price;
                        b.UpdateAt = DateTime.Now;
                        RestaurantContext.ins.BillInfors.Update(b);
                        await RestaurantContext.ins.SaveChangesAsync();
                    }
                    else
                    {
                        b = new BillInfor();
                        b.MenuId = request.items[i].menuId;
                        b.Quantity = request.items[i].quantity;
                        b.Price = request.items[i].price;
                        b.CreateAt = DateTime.Now;
                        b.BillId = bill.Id;
                        RestaurantContext.ins.Add(b);
                        await RestaurantContext.ins.SaveChangesAsync();
                    }
                }
            }
            else
            {
                bill = new Bill();
                bill.TableId = request.tableId;
                bill.Payed = false;
                bill.CreateAt = DateTime.Now;
                bill.Status = false;
                RestaurantContext.ins.Bills.Add(bill);
                await RestaurantContext.ins.SaveChangesAsync();
                for (int i = 0; i < request.items.Count(); i++)
                {
                    BillInfor b = new BillInfor();
                    b.MenuId = request.items[i].menuId;
                    b.Quantity = request.items[i].quantity;
                    b.Price = request.items[i].price;
                    b.CreateAt = DateTime.Now;
                    b.BillId = bill.Id;
                    RestaurantContext.ins.Add(b);
                    await RestaurantContext.ins.SaveChangesAsync();
                }
            }
            Table table = await RestaurantContext.ins.Tables.Where(x => x.Id == request.tableId).FirstOrDefaultAsync();
            if (table != null) {
                table.IsOrder = true;
                RestaurantContext.ins.Tables.Update(table);
                await RestaurantContext.ins.SaveChangesAsync();
            }
            return Ok();
        }

        // PUT api/<OrdersController>/5
        //[HttpPut("{id}")]
        //public void Put(int id, [FromBody] string value)
        //{
        //}

        //// DELETE api/<OrdersController>/5
        //[HttpDelete("{id}")]
        //public void Delete(int id)
        //{
        //}
    }
}
