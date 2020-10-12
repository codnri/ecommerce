package com.example.demo.controller;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        Item item1 = new Item();
        Item item2 = new Item();
        Item item3 = new Item();


        item1.setId(0L);
        item1.setName("item_1");
        item1.setDescription("desc1");
        item1.setPrice(new BigDecimal(11.00));


        item2.setId(1L);
        item2.setName("item_2");
        item2.setDescription("desc2");
        item2.setPrice(new BigDecimal(22.00));

        item3.setName("item_3");
        item3.setDescription("desc3");
        item3.setPrice(new BigDecimal(33.00));
        item3.setId(3L);

        TestUtil.injectObjects(itemController, "itemRepository", itemRepo);

        List<Item> test_item_list = new ArrayList<>();
        test_item_list.add(item1);
        test_item_list.add(item2);
        test_item_list.add(item3);

        List<Item> test_items = new ArrayList<>();
        test_items.add(item1);
        test_items.add(item3);

        Mockito.when(itemRepo.findAll()).thenReturn(test_item_list);
        Mockito.when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item2));
        Mockito.when(itemRepo.findByName("item_1")).thenReturn(test_items);
    }
    @Test
    public void get_all_items() throws Exception {
        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
        assertEquals("item_1", itemList.get(0).getName());
        assertEquals("desc1", itemList.get(0).getDescription());
        assertEquals(new BigDecimal(11.00), itemList.get(0).getPrice());

        assertEquals("item_2", itemList.get(1).getName());
        assertEquals("desc2", itemList.get(1).getDescription());
        assertEquals(new BigDecimal(22.00), itemList.get(1).getPrice());

    }



    @Test
    public void get_one_item() throws Exception {
        Long id = 1L;
        final ResponseEntity<Item> response = itemController.getItemById(id);

        assertNotNull(response);
        Item item = response.getBody();


        assertEquals(200, response.getStatusCodeValue());
        assertEquals("item_2", item.getName());
        assertEquals("desc2", item.getDescription());
        assertEquals(new BigDecimal(22.00), item.getPrice());

    }

    @Test
    public void get_item_by_name() throws Exception {
        String name = "item_1";

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(name);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertEquals("item_1", items.get(0).getName());
        assertEquals("desc1", items.get(0).getDescription());
        assertEquals(new BigDecimal(11.00), items.get(0).getPrice());

        assertEquals("item_3", items.get(1).getName());
        assertEquals("desc3", items.get(1).getDescription());
        assertEquals(new BigDecimal(33.00), items.get(1).getPrice());
    }
}
