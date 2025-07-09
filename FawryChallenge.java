import java.util.*;

interface Shippable {
    String getName();
    double getWeight();
}

class Product {
    String name;
    double price;
    int quantity;
    boolean Expiring;
    boolean needsShipping;
    double weight;
    Date expiryDate;

    Product(String name, double price, int quantity, boolean Expiring, boolean needsShipping, double weight,Date expiryDate) 
    {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.Expiring = Expiring;
        this.needsShipping = needsShipping;
        this.weight = weight;
        this.expiryDate = expiryDate;
    }
   
    boolean Expired() {
        return Expiring && expiryDate != null && expiryDate.before(new Date());
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;}
}

class Cart {
    List<CartItem> items = new ArrayList<>();

    void add(Product product, int quantity) {
        if (quantity > product.quantity) {
            System.out.println(" No enough stock for: " + product.name);
            return;
        }
        items.add(new CartItem(product, quantity));
    }

    boolean isEmpty() {
        return items.isEmpty();
    }
}

class Customer {
    String name;
    double balance;

    Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}

class Shipping {
    static void ship(List<Product> productsToShip) {
        System.out.println("---Shipment notice---");
        double totalWeight = 0;
        for (Product p : productsToShip) {
            System.out.printf("1x %s %.0fg\n", p.name, p.weight * 1000);
            totalWeight += p.weight;
        }
        System.out.printf("Total package weight is %.1fkg\n", totalWeight);
    }
}

class  Checkout {
    static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        List<Product> productsToShip = new ArrayList<>();
        double subtotal = 0;

        for (CartItem item : cart.items) {
            Product p = item.product;
            if (p.Expired()) {
                System.out.println( p.name + "is expired: " );
                return;
            }
            if (item.quantity > p.quantity) {
                System.out.println(p.name + "is out of stock: " );
                return;
            }
            subtotal += p.price * item.quantity;
            if (p.needsShipping) {
                for (int i = 0; i < item.quantity; i++) {
                    productsToShip.add(p);
                }
            }
        }

        double shipping = productsToShip.isEmpty() ? 0 : 30;
        double total = subtotal + shipping;

        if (customer.balance < total) {
            System.out.println("No enough balance");
            return;
        }

        if (!productsToShip.isEmpty()) {
            Shipping.ship(productsToShip);
        }

        System.out.println("---Checkout receipt---");
        for (CartItem item : cart.items) {
            System.out.printf("%dx %s %.0f\n", item.quantity, item.product.name, item.product.price * item.quantity);
        }
        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f\n", subtotal);
        System.out.printf("Shipping %.0f\n", shipping);
        System.out.printf("Total %.0f\n", total);

        customer.balance -= total;
        System.out.printf("Customer Balance %.0f\n", customer.balance);
    }
}

public class FawryChallenge {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 5);
        Date futureDate = cal.getTime();

        Product cheese = new Product("Cheese", 100, 5, true, true, 0.4, futureDate);
        Product biscuits = new Product("Biscuits", 150, 2, true, true, 0.7, futureDate);
        Product tv = new Product("TV", 300, 3, false, true, 8.0, null);
        Product scratchCard = new Product("Scratch Card", 50, 10, false, false, 0.0, null);

        Customer customer = new Customer("Abdulreheem", 800);
        Cart cart = new Cart();

        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(tv, 1);
        cart.add(scratchCard, 1); 

        Checkout.checkout(customer, cart);
    }
}
