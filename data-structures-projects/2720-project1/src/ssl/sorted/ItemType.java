package ssl.sorted;

public class ItemType {

    private int value;

    public int compareTo(ItemType item) {
        if (item.getValue() > this.getValue()) {
            return -1;
        } else if (item.getValue() < this.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getValue() {
        return value;
    }

    public void initialize(int num) {
        this.value = num;
    }
} //ItemType
