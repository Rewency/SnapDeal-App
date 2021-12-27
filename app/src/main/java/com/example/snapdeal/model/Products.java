package com.example.snapdeal.model;

public class Products
{
    private String Pname,description,Price,image,category,Pid,date,time;

    public Products()
    {

    }

    public Products(String pname, String description, String price, String image, String category, String pid, String date, String time) {
        Pname = pname;
        this.description = description;
        Price = price;
        this.image = image;
        this.category = category;
        Pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
