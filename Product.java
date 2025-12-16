public abstract class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public abstract String getCategory();
    public abstract String details();

    public String toString() {
        return "#" + id + " | " + getCategory() + " | " + name + " | $" + price + " | " + details();
    }
}
