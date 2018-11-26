package automata.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import automata.BaseTest;

import static org.testng.Assert.assertEquals;

public class AlphabetTest extends BaseTest {

    private Alphabet alphabet;

    @BeforeMethod
    public void setUp() {
        alphabet = getAlphabet();
    }

    @Test
    public void testEquals() {
        Alphabet temp = new Alphabet("a");
        assertEquals(alphabet, temp);
        assertEquals(alphabet.hashCode(), temp.hashCode());
    }

    @Test
    public void testSetStrength() {
        assertEquals(alphabet.setStrength(1.0).getStrength(), 1.0);
    }
}