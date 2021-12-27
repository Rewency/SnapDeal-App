package com.example.snapdeal.model;

public class Cart
{
  private String Pid,Pname,Price,quantity,discount;

    public Cart()
    {

    }

    public Cart(String pid, String pname, String price, String quantity, String discount) {
        Pid = pid;
        Pname = pname;
        Price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
