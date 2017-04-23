package hust.hoangpt.restaurantclient.model;

import java.util.ArrayList;

public class Categories {

    private int id;
    private String name;
    private ArrayList<MenuItems> listMenuItems;

    public Categories(int id, String name) {
        this.id = id;
        this.name = name;
        listMenuItems = new ArrayList<>();
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

    public boolean addMenuItems(MenuItems menuItems) {
        return this.listMenuItems.add(menuItems);
    }

    public ArrayList<MenuItems> getListMenuItems() {
        return listMenuItems;
    }

    @Override
    public String toString() {
        return name;
    }
}
