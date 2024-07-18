import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() throws ProductNotAvailableException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //THEN
        try {
            //WHEN
            shopService.addOrder(productsIds);
            fail("Expected ProductNotAvailableException is not thrown, even though product 2 was ordered, which deos not exist.");
        } catch (ProductNotAvailableException e ) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getOrdersByOrderStatusTest() throws ProductNotAvailableException {
        // GIVEN
        ShopService shopService = new ShopService();
        OrderStatus statusProcessing = OrderStatus.PROCESSING;
        OrderStatus statusInDelivery = OrderStatus.IN_DELIVERY;
        OrderStatus statusCompleted = OrderStatus.COMPLETED;

        List<String> productsIds = List.of("1");
        shopService.addOrder(productsIds);

        // WHEN
        List<Order> actualMitProcessing = shopService.getOrdersByOrderStatus(statusProcessing);
        List<Order> actualMitInDelivery = shopService.getOrdersByOrderStatus(statusInDelivery);
        List<Order> actualMitCompleted = shopService.getOrdersByOrderStatus(statusCompleted);

        // THEN
        List<Order> expected = new ArrayList<>();

        assertNotEquals(actualMitProcessing, expected);
        assertEquals(actualMitInDelivery, expected);
        assertEquals(actualMitCompleted, expected);
    }

    @Test
    void updateOrderTest_whenNewStatusGiven_thenReturnUpdateOrderHasTheNewStatus() throws ProductNotAvailableException {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        shopService.addOrder(productsIds);

        String tempId = shopService.getOrderRepo().getOrders().get(0).id();
        OrderStatus newOrderStatus = OrderStatus.COMPLETED;

        // WHEN
        shopService.updateOrder(tempId, newOrderStatus);
        OrderStatus updatedStatus = shopService.getOrderRepo().getOrders().get(0).orderStatus();

        // THEN
        assertEquals(newOrderStatus, updatedStatus);
    }

    @Test
    void updateOrderTest_whenStatusFromProcessingToInDelivery_thenOrderWithProcessingDoesNotExist() throws ProductNotAvailableException {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        shopService.addOrder(productsIds);

        String tempId = shopService.getOrderRepo().getOrders().get(0).id();
        OrderStatus newOrderStatus = OrderStatus.IN_DELIVERY;

        // WHEN
        shopService.updateOrder(tempId, newOrderStatus);

        // THEN
        List<Order> ordersWithProcessing = shopService.getOrdersByOrderStatus(OrderStatus.PROCESSING);
        assertEquals(0, ordersWithProcessing.size());

        List<Order> ordersWithInDelivery = shopService.getOrdersByOrderStatus(OrderStatus.IN_DELIVERY);
        assertEquals(1, ordersWithInDelivery.size());
    }
}
