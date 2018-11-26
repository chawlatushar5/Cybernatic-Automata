package automata.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import automata.BaseTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StateTest extends BaseTest {

    private State state;

    @BeforeMethod
    public void setUp() {
        state = getState();
    }

    @Test
    public void testGetNumber() {
        assertEquals(state.getNumber(), 1);
    }

    @Test
    public void testSetInitial() {
        assertFalse(state.isInitial());
        assertTrue(state.setInitial(true).isInitial());
    }

    @Test
    public void testSetAccepting() {
        assertFalse(state.isAccepting());
        assertTrue(state.setAccepting(true).isAccepting());
    }

    @Test
    public void testSetRewarding() {
        assertFalse(state.isRewarding());
        assertTrue(state.setRewarding(true).isRewarding());
    }

    @Test
    public void testSetPunishable() {
        assertFalse(state.isPunishable());
        assertTrue(state.setPunishable(true).isPunishable());
    }

    @Test
    public void equals() {
        State temp = new State(1);
        assertEquals(state, temp);
        assertEquals(state.hashCode(), temp.hashCode());
    }
}