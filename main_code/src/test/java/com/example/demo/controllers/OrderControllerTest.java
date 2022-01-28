package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    //create a mock object
    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);


    // implement setUp test before each test
    @Before
    public void setUp(){
        orderController = new OrderController();
        // injcet userRepo into cartController
        TestUtils.injectObject(orderController, "userRepository", userRepo);
        // injcet cartRepo into cartController
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);

    }


// Test submit order test
    @Test
    public void submitOrderTest(){


// Create new user with id = 5
        User user = new User();
        user.setId(5L);
        user.setUsername("test5");
        user.setPassword("password");


        // add new item to cart
        Item item = new Item();
        item.setName("firstItem");
        item.setId(0L);
        item.setDescription("firstItemDescription");
        item.setPrice(BigDecimal.valueOf(10.0));

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> items = new ArrayList<>();

        // add created item to items of cart
        items.add(item);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(10.0));


        user.setCart(cart);

        when(userRepo.findByUsername("test5")).thenReturn(user);



        ResponseEntity<UserOrder> response = orderController.submit("test5");

     // check status code value
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

     // check if userorder not null
        assertNotNull(userOrder);

      // test user name is as expected = "test5"
        assertEquals("test5", userOrder.getUser().getUsername());

        // test the total price of items is as expected == 10.0
        assertEquals(BigDecimal.valueOf(10.0), userOrder.getTotal());


        // check the number of items in cart , expected is 1
        assertEquals(1, userOrder.getItems().size());

    }



}
