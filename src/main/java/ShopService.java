import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                return null;
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByOrderStatus(OrderStatus orderStatus) {
        return orderRepo.getOrders()
                .stream()
                .filter(order -> order.orderStatus().equals(orderStatus))
                .toList();
    }

    public void updateOrder(String orderId, OrderStatus newStatus)  {
        Order order = orderRepo.getOrderById(orderId);
        if (order != null) {
            orderRepo.removeOrder(orderId);

            Order newOrder = order.withOrderStatus(newStatus);
            orderRepo.addOrder(newOrder);
        } else {
            System.out.println("The order does not exist.");
        }
    }
}
