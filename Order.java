import java.util.ArrayList;

public class Order {
    private int orderId;
    private Customer customer;
    private ArrayList<CartItem> items;

    private double subtotal;
    private double discount;
    private double tax;
    private double shipping;
    private double total;

    public Order(int orderId, Customer customer, ArrayList<CartItem> items) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
    }

    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public double getTotal() { return total; }

    public void calculateTotals(DiscountPolicy discountPolicy, ChargeRule taxRule, ChargeRule shippingRule) {
        subtotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            subtotal += items.get(i).lineTotal();
        }

        discount = discountPolicy.apply(items, subtotal);

        double afterDiscount = subtotal - discount;
        if (afterDiscount < 0) afterDiscount = 0;

        tax = taxRule.apply(afterDiscount);
        shipping = shippingRule.apply(afterDiscount);

        total = afterDiscount + tax + shipping;
    }

    public String summary() {
        return "Order #" + orderId + " | user=" + customer.getUsername() + " | total=$" + total;
    }

    public void printReceipt() {
        System.out.println("----- RECEIPT -----");
        System.out.println(summary());
        System.out.println("Items:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("  " + items.get(i));
        }
        System.out.println("Subtotal:  $" + subtotal);
        System.out.println("Discount: -$" + discount);
        System.out.println("Tax:       $" + tax);
        System.out.println("Shipping:  $" + shipping);
        System.out.println("TOTAL:     $" + total);
        System.out.println("-------------------");
    }
}
