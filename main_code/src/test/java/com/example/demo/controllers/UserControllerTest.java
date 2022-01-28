package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.ws.Response;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    //create a mock object
    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);


    // implement setUp test before each test
    @Before
    public void setUp() {
    userController = new UserController();

    // injcet userRepo into UserController
        TestUtils.injectObject(userController, "userRepository", userRepo);
        // injcet cartRepo into UserController
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
    // injcet bCryptPasswordEncoder into UserController
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }


    // create_user_happy_path as in Udacity course video
    @Test
    public void create_user_happy_path() throws Exception{


// create new user with placeholder data as in Udacity course video

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");


        final ResponseEntity<User> response =
        userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        // check if username is not null
        assertNotNull(user);

        // get id value
        assertEquals(0, user.getId());


        // check if username is as expected = "test"
        assertEquals("test", user.getUsername());

        // add thisIsHashed value to test password
        assertEquals("thisIsHashed", user.getPassword());


    }



    // Test getUser by id with data id=10

    @Test
    public void getuserByidTest(){

        // create new user
        User user = new User();
        user.setId(10);
        user.setUsername("Rasha");
        user.setPassword("1234567890");

        when(userRepo.findById(10L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(10L);

        User u = response.getBody();

        // check if username is not null
        assertNotNull(u);

        //check if actual id as expected = 10
        assertEquals(10, u.getId());

        //check if actual user name as expected
        assertEquals("Rasha", u.getUsername());

        //check if actual password as expected
        assertEquals("1234567890", u.getPassword());
    }
    }


