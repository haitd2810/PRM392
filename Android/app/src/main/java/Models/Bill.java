package Models;

import java.util.List;

public class Bill {
    private int id;
    private int tableId;
    private boolean paid;
    private String createAt;
    private String updateAt;
    private int updateBy;
    private boolean status;
    private Table table;
    private List<BillInfor> billInfors;

    public Bill() {
    }

    public Bill(int id, int tableId, boolean paid, String createAt, String updateAt, int updateBy, boolean status, Table table, List<BillInfor> billInfors) {
        this.id = id;
        this.tableId = tableId;
        this.paid = paid;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.updateBy = updateBy;
        this.status = status;
        this.table = table;
        this.billInfors = billInfors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public boolean ispaid() {
        return paid;
    }

    public void setpaid(boolean paid) {
        this.paid = paid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<BillInfor> getBillInfors() {
        return billInfors;
    }

    public void setBillInfors(List<BillInfor> billInfors) {
        this.billInfors = billInfors;
    }
}
