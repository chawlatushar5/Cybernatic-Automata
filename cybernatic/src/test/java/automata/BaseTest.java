package automata;

import automata.model.Alphabet;
import automata.model.State;
import automata.model.Transition;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@Getter
@ContextConfiguration(locations = "/test-context.xml")
public class BaseTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("state")
    private State state;

    @Autowired
    @Qualifier("alphabet")
    private Alphabet alphabet;
}
