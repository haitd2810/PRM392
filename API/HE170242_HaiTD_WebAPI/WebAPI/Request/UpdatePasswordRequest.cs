namespace WebAPI.Request
{
    public class UpdatePasswordRequest
    {
        public int accountId { get; set; }
        public string oldPassword { get; set; }
        public string newPassword { get; set; }
        public string repeatPassword { get; set; }
    }
}
