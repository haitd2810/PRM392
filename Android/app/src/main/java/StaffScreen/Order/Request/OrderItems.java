package StaffScreen.Order.Request;

public class OrderItems{
    private int menuId;
    private int quantity;
    private double price;

    public OrderItems() {
    }

    public OrderItems(int menuId, int quantity, double price) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
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
}
