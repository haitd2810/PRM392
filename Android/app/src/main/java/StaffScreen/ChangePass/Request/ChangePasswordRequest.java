package StaffScreen.ChangePass.Request;

public class ChangePasswordRequest {
    private int accountId;
    private String oldPass;
    private String newPass;
    private String repeatPass;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(int accountId, String oldPass, String newPass, String repeatPass) {
        this.accountId = accountId;
        this.oldPass = oldPass;
        this.newPass = newPass;
        this.repeatPass = repeatPass;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getRepeatPass() {
        return repeatPass;
    }

    public void setRepeatPass(String repeatPass) {
        this.repeatPass = repeatPass;
    }
}
