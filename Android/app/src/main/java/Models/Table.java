package Models;

public class Table {
    private int id;
    private boolean isOrder;
    private boolean forBooking;
    private boolean deleteFlag;
    private String createAt;
    private String updateAt;
    private String deleteAt;

    public Table(int id, boolean isOrder, boolean forBooking,boolean deleteFlag, String createAt, String updateAt, String deleteAt) {
        this.id = id;
        this.isOrder = isOrder;
        this.forBooking = forBooking;
        this.deleteFlag = deleteFlag;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.deleteAt = deleteAt;
    }

    public boolean isdeleteFlag() {
        return deleteFlag;
    }

    public void setdeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Table() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrdered(boolean ordered) {
        isOrder = ordered;
    }

    public boolean isforBooking() {
        return forBooking;
    }

    public void setforBooking(boolean forBooking) {
        this.forBooking = forBooking;
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

    public String getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(String deleteAt) {
        this.deleteAt = deleteAt;
    }
}
