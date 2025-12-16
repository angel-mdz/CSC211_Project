import java.util.ArrayList;

@FunctionalInterface
public interface DiscountPolicy {
    double apply(ArrayList<CartItem> items, double subtotal);
}
