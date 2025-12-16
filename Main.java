import java.util.ArrayList;

public class Main {

    private static Store store = new Store();

    public static void main(String[] args) {
        System.out.println("Simple E-Commerce Prototype (Basic Java)\n");

        while (true) {
            System.out.println("1) Customer");
            System.out.println("2) Admin");
            System.out.println("0) Exit");

            int choice = Input.readInt("Choose: ");
            if (choice == 0) break;

            if (choice == 1) customerMenu(new Customer(1, "customer"));
            else if (choice == 2) adminMenu(new Admin(99, "admin"));
            else System.out.println("Invalid option.\n");
        }

        System.out.println("Goodbye.");
    }

    // ---------------- CUSTOMER ----------------
    private static void customerMenu(Customer customer) {
        ArrayList<CartItem> cart = new ArrayList<CartItem>();

        // Lambdas for category filters
        ProductFilter electronicsFilter = (p) -> p instanceof Electronics;
        ProductFilter clothingFilter = (p) -> p instanceof Clothing;
        ProductFilter groceryFilter = (p) -> p instanceof Grocery;

        while (true) {
            System.out.println("\n--- Customer Menu (" + customer.getUsername() + ") ---");
            System.out.println("1) List products");
            System.out.println("2) Filter products by category (lambda)");
            System.out.println("3) Add to cart");
            System.out.println("4) View cart");
            System.out.println("5) Checkout");
            System.out.println("0) Logout");

            int c = Input.readInt("Choose: ");
            if (c == 0) return;

            if (c == 1) {
                printProducts(store.getProducts());

            } else if (c == 2) {
                System.out.println("1) Electronics  2) Clothing  3) Grocery");
                int cat = Input.readInt("Choose: ");

                ProductFilter chosen = null;
                if (cat == 1) chosen = electronicsFilter;
                else if (cat == 2) chosen = clothingFilter;
                else if (cat == 3) chosen = groceryFilter;

                ArrayList<Product> filtered = new ArrayList<Product>();
                for (int i = 0; i < store.getProducts().size(); i++) {
                    Product p = store.getProducts().get(i);
                    if (chosen == null || chosen.test(p)) filtered.add(p);
                }

                printProducts(filtered);

            } else if (c == 3) {
                addToCart(cart);

            } else if (c == 4) {
                printCart(cart);

            } else if (c == 5) {
                checkout(customer, cart);

            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static void addToCart(ArrayList<CartItem> cart) {
        printProducts(store.getProducts());
        int id = Input.readInt("Product id: ");
        Product p = store.findProductById(id);

        if (p == null) {
            System.out.println("Not found.");
            return;
        }

        int qty = Input.readInt("Quantity: ");
        if (qty <= 0) {
            System.out.println("Quantity must be > 0.");
            return;
        }

        // If already in cart, update quantity
        for (int i = 0; i < cart.size(); i++) {
            CartItem ci = cart.get(i);
            if (ci.getProduct().getId() == id) {
                ci.setQuantity(ci.getQuantity() + qty);
                System.out.println("Updated cart quantity.");
                return;
            }
        }

        cart.add(new CartItem(p, qty));
        System.out.println("Added to cart.");
    }

    private static void printCart(ArrayList<CartItem> cart) {
        if (cart.size() == 0) {
            System.out.println("Cart is empty.");
            return;
        }

        double subtotal = 0.0;
        System.out.println("Cart:");
        for (int i = 0; i < cart.size(); i++) {
            System.out.println("  " + cart.get(i));
            subtotal += cart.get(i).lineTotal();
        }
        System.out.println("Subtotal: $" + subtotal);
    }

    private static void checkout(Customer customer, ArrayList<CartItem> cart) {
        if (cart.size() == 0) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("Discount:");
        System.out.println("1) None");
        System.out.println("2) 10% off Electronics only");
        int d = Input.readInt("Choose: ");

        // Discount lambda
        DiscountPolicy discountPolicy;
        if (d == 2) {
            discountPolicy = (items, subtotal) -> {
                double elecSubtotal = 0.0;
                for (int i = 0; i < items.size(); i++) {
                    CartItem ci = items.get(i);
                    if (ci.getProduct() instanceof Electronics) {
                        elecSubtotal += ci.lineTotal();
                    }
                }
                return elecSubtotal * 0.10;
            };
        } else {
            discountPolicy = (items, subtotal) -> 0.0;
        }

        // Tax + Shipping lambdas
        ChargeRule taxRule = (amount) -> amount * 0.08875;
        ChargeRule shippingRule = (amount) -> {
            if (amount >= 50.0) return 0.0;
            return 7.99;
        };

        // Copy cart -> order items
        ArrayList<CartItem> orderItems = new ArrayList<CartItem>();
        for (int i = 0; i < cart.size(); i++) {
            CartItem ci = cart.get(i);
            orderItems.add(new CartItem(ci.getProduct(), ci.getQuantity()));
        }

        Order order = new Order(store.newOrderId(), customer, orderItems);
        order.calculateTotals(discountPolicy, taxRule, shippingRule);

        store.addOrder(order);
        cart.clear();

        order.printReceipt();
        System.out.println("Order placed!");
    }

    // ---------------- ADMIN ----------------
    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- Admin Menu (" + admin.getUsername() + ") ---");
            System.out.println("1) List products");
            System.out.println("2) Add product");
            System.out.println("3) List orders");
            System.out.println("0) Logout");

            int c = Input.readInt("Choose: ");
            if (c == 0) return;

            if (c == 1) {
                printProducts(store.getProducts());

            } else if (c == 2) {
                addProductAdmin();

            } else if (c == 3) {
                printOrders(store.getOrders());

            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static void addProductAdmin() {
        System.out.println("Type: 1) Electronics  2) Clothing  3) Grocery");
        int t = Input.readInt("Choose: ");

        String name = Input.readLine("Name: ");
        double price = Input.readDouble("Price: ");

        int id = store.newProductId();
        Product p = null;

        if (t == 1) {
            String brand = Input.readLine("Brand: ");
            p = new Electronics(id, name, price, brand);
        } else if (t == 2) {
            String size = Input.readLine("Size: ");
            p = new Clothing(id, name, price, size);
        } else if (t == 3) {
            double w = Input.readDouble("Weight(kg): ");
            p = new Grocery(id, name, price, w);
        }

        if (p == null) {
            System.out.println("Invalid type.");
            return;
        }

        store.addProduct(p);
        System.out.println("Added: " + p);
    }

    // ---------------- PRINT METHODS ----------------
    private static void printProducts(ArrayList<Product> list) {
        if (list.size() == 0) {
            System.out.println("(No products)");
            return;
        }
        System.out.println("Products:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + list.get(i));
        }
    }

    private static void printOrders(ArrayList<Order> list) {
        if (list.size() == 0) {
            System.out.println("(No orders)");
            return;
        }
        System.out.println("Orders:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + list.get(i).summary());
        }
    }
}
