public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { quantity = q; }

    public double lineTotal() {
        return product.getPrice() * quantity;
    }

    public String toString() {
        return "#" + product.getId() + " | " + product.getName() + " | qty=" + quantity
                + " | $" + String.format("%.2f", lineTotal());
    }
}
