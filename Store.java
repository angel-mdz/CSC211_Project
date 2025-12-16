import java.util.ArrayList;

public class Store {
    private ArrayList<Product> products;
    private ArrayList<Order> orders;

    private int nextProductId;
    private int nextOrderId;

    public Store() {
        products = new ArrayList<Product>();
        orders = new ArrayList<Order>();
        nextProductId = 100;
        nextOrderId = 1;
        seed();
    }

    public ArrayList<Product> getProducts() { return products; }
    public ArrayList<Order> getOrders() { return orders; }

    public int newProductId() {
        int id = nextProductId;
        nextProductId++;
        return id;
    }

    public int newOrderId() {
        int id = nextOrderId;
        nextOrderId++;
        return id;
    }

    public void addProduct(Product p) { products.add(p); }
    public void addOrder(Order o) { orders.add(o); }

    public Product findProductById(int id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) return products.get(i);
        }
        return null;
    }

    private void seed() {
        products.add(new Electronics(newProductId(), "Headphones", 29.99, "Acme"));
        products.add(new Electronics(newProductId(), "Keyboard", 49.99, "KeyCo"));
        products.add(new Clothing(newProductId(), "T-Shirt", 14.99, "M"));
        products.add(new Clothing(newProductId(), "Hoodie", 39.99, "L"));
        products.add(new Grocery(newProductId(), "Rice Bag", 9.99, 2.00));
        products.add(new Grocery(newProductId(), "Olive Oil", 11.49, 0.75));
    }
}
