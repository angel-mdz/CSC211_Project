public class Customer extends User {
    public Customer(int id, String username) {
        super(id, username);
    }

    public String role() { return "Customer"; }
}
