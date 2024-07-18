import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws ProductNotAvailableException {
        Product product1 = new Product("3", "Avocado");
        Product product2 = new Product("4", "pear");

        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(product1);
        productRepo.addProduct(product2);

        Optional<Product> productById = productRepo.getProductById("5");


        Order order3 = new Order("3", Arrays.asList(product1, product2), OrderStatus.PROCESSING);
        Order order4 = new Order("4", List.of(product1), OrderStatus.PROCESSING);

        OrderListRepo orderListRepo = new OrderListRepo();
        OrderMapRepo orderMapRepo = new OrderMapRepo();
        orderMapRepo.addOrder(order3);

        ShopService shopService = new ShopService();
        shopService.addOrder(List.of("1"));
        //shopService.addOrder(List.of("3"));

        // Check updateOrderStatus
        System.out.println(shopService.getOrderRepo().getOrders().get(0));
        String tempId = shopService.getOrderRepo().getOrders().get(0).id();
        shopService.updateOrder(tempId, OrderStatus.IN_DELIVERY);
        System.out.println(shopService.getOrderRepo().getOrders().get(0));
    }
}
