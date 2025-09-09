package edu.birzeit.project1;

public class Product {
    private int ID;
    private String name;
    private String iconURL;
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIconURL() {
        return iconURL;
    }
    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
    @Override
    public String toString() {
        return "Product{" +
                "\nID= " + ID +
                "\nname= " + name +
                "\niconURL= " + iconURL +
                +'\n'+'}'+'\n';
    }
}