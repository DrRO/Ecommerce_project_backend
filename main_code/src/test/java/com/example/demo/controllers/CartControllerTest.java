package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    //create a mock object
    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);


    // implement setUp test before each test
    @Before
    public void setUp(){
        cartController = new CartController();
        // injcet userRepo into cartController
        TestUtils.injectObject(cartController, "userRepository", userRepo);
        // injcet cartRepo into cartController
        TestUtils.injectObject(cartController, "cartRepository", cartRepo);
        // injcet itemRepo into cartController
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);

    }

    @Test
    public void addToCartTest(){

        // Create new user
        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("password");
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);


        // add new item
        Item item = new Item();
        item.setId(0L);
        item.setDescription("firstItemDescription");
        item.setPrice(BigDecimal.valueOf(100.00));
        item.setName("firstItem");


        when(userRepo.findByUsername("test")).thenReturn(user);
        when(cartRepo.findByUser(user)).thenReturn(user.getCart());
        when(itemRepo.findById(0L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());

        Cart cartResponse = response.getBody();

        // test user name as a "test" is expected value
        assertEquals("test", cartResponse.getUser().getUsername());


        // test how many items are in the cart with expected value 5 items
        assertEquals(5, cartResponse.getItems().size());


        // test total price of items which in the cart with expected value 100.0*5 = 500.0
        assertEquals(BigDecimal.valueOf(500.0), cartResponse.getTotal());


    }


    @Test
    public void removeFromCartTest(){

        addToCartTest();
        // test remove one item from cart request
        ModifyCartRequest removeCartRequest = new ModifyCartRequest();
        removeCartRequest.setItemId(0);
        removeCartRequest.setQuantity(1);
        removeCartRequest.setUsername("test");

        ResponseEntity<Cart> responseRemoved = cartController.removeFromcart(removeCartRequest);

        // test the actual values

        assertEquals(200, responseRemoved.getStatusCodeValue());

        Cart cart = responseRemoved.getBody();

        // check user name
        assertEquals("test", cart.getUser().getUsername());

        // test how many items are left in the cart
        assertEquals(4, cart.getItems().size());

        // test the total price of items are left in the cart
        assertEquals(BigDecimal.valueOf(400.0), cart.getTotal());


    }



}
