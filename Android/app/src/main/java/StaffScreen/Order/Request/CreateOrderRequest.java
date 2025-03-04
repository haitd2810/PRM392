package StaffScreen.Order.Request;

import java.util.List;

public class CreateOrderRequest{
    private int tableId;
    private List<OrderItems> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(int tableId, List<OrderItems> items) {
        this.tableId = tableId;
        this.items = items;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }
}

