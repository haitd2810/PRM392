package Models;


import java.util.*;

public class Menu implements Comparable<Menu>  {
    private int id;
    private String name;
    private String detail;
    private double price;
    private String img;
    private int quantity;
    private int cateId;
    private Boolean deleteFlag;
    private String createAt;
    private String upStringAt;
    private String deleteAt;

    public Menu() {
    }

    public Menu(int id, String name, String detail, double price, String img, int quantity, int cateId, Boolean deleteFlag, String createAt, String upStringAt, String deleteAt) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.img = img;
        this.quantity = quantity;
        this.cateId = cateId;
        this.deleteFlag = deleteFlag;
        this.createAt = createAt;
        this.upStringAt = upStringAt;
        this.deleteAt = deleteAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getcateId() {
        return cateId;
    }

    public void setcateId(int cateId) {
        cateId = cateId;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpStringAt() {
        return upStringAt;
    }

    public void setUpStringAt(String upStringAt) {
        this.upStringAt = upStringAt;
    }

    public String getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(String deleteAt) {
        this.deleteAt = deleteAt;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    @Override
    public int compareTo(Menu menu) {
        return Integer.compare(this.id, menu.id);
    }
}
