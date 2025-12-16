public class Clothing extends Product {
    private String size;

    public Clothing(int id, String name, double price, String size) {
        super(id, name, price);
        this.size = size;
    }

    public String getCategory() { return "Clothing"; }
    public String details() { return "size=" + size; }
}
