package hust.hoangpt.restaurantclient.model;

public class DiningTables {

    private int tableNumber, chairsCount, status;

    public DiningTables(int tableNumber, int chairsCount, int status) {
        this.tableNumber = tableNumber;
        this.chairsCount = chairsCount;
        this.status = status;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getChairsCount() {
        return chairsCount;
    }

    public void setChairsCount(int chairsCount) {
        this.chairsCount = chairsCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String statusString = (status == 0) ? "unoccupied" : "occupied";
        return "Table: " + tableNumber + ". Chairs: " + chairsCount + ". Status: " + statusString;
    }
}
