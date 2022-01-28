package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {


    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);





    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepo);


        // create first item
        Item firstItem = new Item();
        firstItem.setName("firstItem");
        firstItem.setId(0L);
        firstItem.setDescription("firstItemDescription");
        firstItem.setPrice(BigDecimal.valueOf(10.0));


        // create second item
        Item secItem = new Item();
        secItem.setName("secItem");
        secItem.setId(1L);
        secItem.setDescription("secItemDescription");
        secItem.setPrice(BigDecimal.valueOf(20.0));





        // add created items to items of cart
        List<Item> cartItems = new ArrayList<>();
       cartItems.add(firstItem);
        cartItems.add(secItem);


        when(itemRepo.findAll()).thenReturn(cartItems);

        when(itemRepo.findById(0L)).thenReturn(Optional.of(cartItems.get(0)));
        when(itemRepo.findById(1L)).thenReturn(Optional.of(cartItems.get(1)));

        when(itemRepo.findByName("firstItem")).thenReturn(cartItems.subList(0,1));
        when(itemRepo.findByName("secItem")).thenReturn(cartItems.subList(1,2));
    }



    @Test
    public void getItemsTest(){

        ResponseEntity<List<Item>> response = itemController.getItems();
        
        List <Item> items = response.getBody();

        
        // check items list is not null
        assertNotNull(items);
        
        // test if items number is as expected == 2
        assertEquals(2, items.size());

        // check the id of first item
        assertEquals(0, items.get(0).getId());


        // check the id of second item

        assertEquals(1, items.get(1).getId());


       // check the name of first item
        assertEquals("firstItem", items.get(0).getName());

        // check the name of second item
        assertEquals("secItem", items.get(1).getName());

        // check the name of first item
        assertEquals("firstItemDescription", items.get(0).getDescription());

        // check the description of second item
        assertEquals("secItemDescription", items.get(1).getDescription());

        // check the price of first item
        assertEquals(BigDecimal.valueOf(10.0), items.get(0).getPrice());

        // check the price of first item
        assertEquals(BigDecimal.valueOf(10.0), items.get(0).getPrice());

    }
// test get second Item by id
    @Test
    public void getItemById(){
        ResponseEntity<Item> response = itemController.getItemById(1L);


        Item item = response.getBody();

        // check items list is not null
        assertNotNull(item);

       //check id of second item
        assertEquals(1, item.getId());

        //check name of second item
        assertEquals("secItem", item.getName());

        // check description of second item
        assertEquals("secItemDescription", item.getDescription());

        // check the price of second item
        assertEquals(BigDecimal.valueOf(20.0), item.getPrice());


    }
    


}
