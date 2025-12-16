public class Electronics extends Product {
    private String brand;

    public Electronics(int id, String name, double price, String brand) {
        super(id, name, price);
        this.brand = brand;
    }

    public String getCategory() { return "Electronics"; }
    public String details() { return "brand=" + brand; }
}
