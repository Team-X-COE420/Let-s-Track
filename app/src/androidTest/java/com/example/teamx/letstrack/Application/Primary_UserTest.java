package com.example.teamx.letstrack.Application;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Aranyak on 22-Nov-17.
 */
public class Primary_UserTest {

    private Primary_User user;

    @Before
    public void setup() throws Exception {
        user = new Primary_User("aranyakghosh@gmail.com", "+971501955212", "Password");
        user.setPhone_verified(true);
        user.updatePosition(new Position("Home", new LatLng(25.29, 50.78)));
        user.updatePosition(new Position("Work", new LatLng(25.31, 55.49)));
        user.updatePosition(new Position("Home", new LatLng(25.2829, 55.20)));

    }

    @Test
    public void updatePosition() throws Exception {

        Position Home = new Position("Home", new LatLng(20.27, 50.34));
        user.updatePosition(Home);
        Position p = user.getPosition_Array().get(0);
        assertEquals(Home, p);
    }

    @Test
    public void addContact() throws Exception {


    }

    @Test
    public void getContact() throws Exception {
    }

    @Test
    public void updatePositionTag() throws Exception {

    }

}