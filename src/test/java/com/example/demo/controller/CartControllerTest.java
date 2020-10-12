package com.example.demo.controller;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class CartControllerTest {
    private CartController cartController;
    private Cart cart;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private User user = mock(User.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        cart = new Cart();
        TestUtil.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtil.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtil.injectObjects(cartController, "userRepository", userRepo);

        Item item1 = new Item();
        item1.setName("item_1");
        item1.setDescription("desc1");
        item1.setPrice(new BigDecimal(111.00));
        item1.setId(0L);
        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("item_2");
        item2.setDescription("desc2");
        item2.setPrice(new BigDecimal(222.00));

        user.setUsername("johndoe");
        user.setPassword("p@ssw0rd");
        user.setConfirmPassword("p@ssw0rd");
        user.setCart(cart);

        cart.addItem(item1);
        cart.addItem(item2);
        cartRepo.save(cart);

        Mockito.when(userRepo.findByUsername("johndoe")).thenReturn(user);
        Mockito.when(user.getCart()).thenReturn(cart);
        Mockito.when(itemRepo.findById(0L)).thenReturn(java.util.Optional.of(item1));
        Mockito.when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item2));

    }
    @Test
    public void add_item_to_cart() throws Exception {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setUsername("johndoe");

        r.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();

        assertEquals("item_2", cart.getItems().get(2).getName());
        assertEquals("desc2", cart.getItems().get(2).getDescription());
        assertEquals(new BigDecimal(555.00), cart.getTotal());

    }

    @Test
    public void remove_item_from_cart() throws Exception {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername("johndoe");
        req.setItemId(1L);
        req.setQuantity(1);


        final ResponseEntity<Cart> res = cartController.removeFromcart(req);

        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        assertEquals("0", cart.getItems().get(0).getId().toString());
        assertEquals(new BigDecimal(111), cart.getTotal());

    }

}
