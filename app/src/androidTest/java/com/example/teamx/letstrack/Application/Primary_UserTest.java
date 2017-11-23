package com.example.teamx.letstrack.Application;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Aranyak on 22-Nov-17.
 */
public class Primary_UserTest {

    private Primary_User user;

    @Before
    public void setup() throws Exception {
        user = new Primary_User("aranyakghosh@gmail.com", "+971501955212", "Password");
        user.setPhone_verified(true);
        user.updatePosition(new Position("Home", new LatLng(20.29, 50.78)));
        user.updatePosition(new Position("Work", new LatLng(25.31, 55.49)));
        user.updatePosition(new Position("Gym", new LatLng(30.2829, 47.20)));

    }

    @Test
    public void updatePosition() throws Exception {

        Position Home = new Position("Home", new LatLng(20.27, 50.34));
        user.updatePosition(Home);
        Position p = user.getPosition_Array().get(0);
        assertEquals(Home.getPosition_Name(), p.getPosition_Name());
        assertEquals(Home.getLocation().latitude, p.getLocation().latitude, 0.002);
        assertEquals(Home.getLocation().longitude, p.getLocation().longitude, 0.002);

    }

    @Test
    public void isUserCreated() throws Exception {
        assertNotNull(user);
    }

    @Test
    public void updatePositionTag() throws Exception {
        String position = user.updatePositionTag(new LatLng(20.27, 50.341));
        assertEquals("Home", position);
        position = user.updatePositionTag(new LatLng(25.31, 55.49));
        assertEquals("Work", position);
        position = user.updatePositionTag(new LatLng(30.2829, 47.20));
        assertEquals("Gym", position);
        position = user.updatePositionTag(new LatLng(0, 0));
        assertEquals("Other", position);
    }

}