package Models;

import java.io.Serializable;

public class Order implements Serializable {
    private Menu menu;
    private int quantity;
    private int tableId;

    public Order() {
    }

    public Order(Menu menu, int quantity, int tableId) {
        this.menu = menu;
        this.quantity = quantity;
        this.tableId = tableId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
