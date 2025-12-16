public class Grocery extends Product {
    private double weightKg;

    public Grocery(int id, String name, double price, double weightKg) {
        super(id, name, price);
        this.weightKg = weightKg;
    }

    public String getCategory() { return "Grocery"; }
    public String details() { return "weight=" + weightKg + "kg"; }
}
