package hust.hoangpt.restaurantclient.model;

public class OrderDetails {

    private int id, quantity, status;
    private float amount, item_price;
    private String item_name;

    public OrderDetails(int id, int quantity, int status, float amount, float item_price, String item_name) {
        this.id = id;
        this.quantity = quantity;
        this.status = status;
        this.amount = amount;
        this.item_price = item_price;
        this.item_name = item_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getItem_price() {
        return item_price;
    }

    public void setItem_price(float item_price) {
        this.item_price = item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    @Override
    public String toString() {
        String statusString = "";
        if (status == 0) {
            statusString = "Unserved.";
        } else if (status == 1) {
            statusString = "Processing.";
        } else if (status == 2) {
            statusString = "Served.";
        } else if (status == 3) {
            statusString = "Out Of Materials.";
        }
        return item_name + " - " + statusString;
    }
}
