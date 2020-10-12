package com.example.demo.controller;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtil.injectObjects(orderController, "userRepository", userRepo);
        TestUtil.injectObjects(orderController, "orderRepository", orderRepo);

        User test_user = new User();
        test_user.setUsername("test_user");
        test_user.setPassword("test_user_password");
        test_user.setConfirmPassword("test_user_confirmed_password");

        Cart cart = new Cart();

        Item item1 = new Item();
        item1.setId(0L);
        item1.setName("item_1");
        item1.setDescription("desc1");
        item1.setPrice(new BigDecimal(11.00));

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("item_2");
        item2.setDescription("desc2");
        item2.setPrice(new BigDecimal(22.00));
        cart.addItem(item1);
        cart.addItem(item2);
        test_user.setCart(cart);

        Mockito.when(userRepo.findByUsername("test_user")).thenReturn(test_user);

    }

    @Test
    public void submit_order() {
        String username = "test_user";
        final ResponseEntity<UserOrder> r = orderController.submit(username);
        assertNotNull(r);
        assertEquals(200, r.getStatusCodeValue());
        UserOrder userOrder = r.getBody();

        assertEquals(new BigDecimal(33), userOrder.getTotal());
        assertEquals("item_1", userOrder.getItems().get(0).getName());
        assertEquals("item_2", userOrder.getItems().get(1).getName());


    }
}
