package StaffScreen.TableDetail.Request;

public class UpdateDetailRequest {
    private int billInforId;
    private int quantity;
    private double price;
    private int tableId;

    public UpdateDetailRequest() {
    }

    public int getBillInforId() {
        return billInforId;
    }

    public void setBillInforId(int billInforId) {
        this.billInforId = billInforId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
