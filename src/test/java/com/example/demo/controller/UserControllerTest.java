package com.example.demo.controller;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtil.injectObjects(userController, "userRepository", userRepo);
        TestUtil.injectObjects(userController, "cartRepository", cartRepo);
        TestUtil.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User test_user = new User();
        test_user.setUsername("test_user");
        test_user.setPassword("test_user_password");
        test_user.setConfirmPassword("test_user_confirmed_password");
        when(userRepo.findByUsername("test_user")).thenReturn(test_user);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(test_user));
    }
    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void get_user_by_id() throws Exception {
        Long user_id = 0L;
        final ResponseEntity<User> response = userController.findById(user_id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test_user_password", u.getPassword());
    }


    @Test
    public void get_user_by_username() throws Exception {
        String user_name = "test_user";


        final ResponseEntity<User> response = userController.findByUserName(user_name);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test_user_password", u.getPassword());
    }


}
