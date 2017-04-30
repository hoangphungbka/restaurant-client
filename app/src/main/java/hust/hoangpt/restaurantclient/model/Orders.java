package hust.hoangpt.restaurantclient.model;

public class Orders {

    private int id, totalAmount, tableNumber, employeeId, status;

    public Orders(int id, int totalAmount, int tableNumber, int employeeId, int status) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.tableNumber = tableNumber;
        this.employeeId = employeeId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Table Number: " + tableNumber;
    }
}
