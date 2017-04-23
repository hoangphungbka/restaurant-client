package hust.hoangpt.restaurantclient.model;

public class MenuItems {

    private int id, categoryId;
    private String name, image;
    private float price;

    public MenuItems(int id, int categoryId, String image, float price, String name) {
        this.id = id; this.categoryId = categoryId;
        this.image = image; this.price = price; this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
