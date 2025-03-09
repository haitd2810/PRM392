namespace WebAPI.Request
{
	public class RegisterRequest
	{
		public string username {  get; set; }
		public string password { get; set; }
		public string repeatPassword { get; set; }
	}
}
