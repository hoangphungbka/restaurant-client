package hust.hoangpt.restaurantclient.model;

import java.io.Serializable;

public class Employees implements Serializable {

    private int id, status;
    private String username, email, phone, address;

    public int getId() {
        return id;
    }

    public Employees(int id, String username, String email, String phone, String address, int status) {
        this.id = id;
        this.status = status;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
