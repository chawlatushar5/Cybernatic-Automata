package automata.model;

import com.google.common.base.Objects;

import lombok.Data;

@Data
public class Transition {


    // this state is a to-state for Function
    private State state;
    private Integer input;
    //private Alphabet alphabet;

    private boolean temporary;
    private boolean permanent;

    public Transition(State state, Integer input) {
        this.state = state;
        this.input = input;
        this.temporary = false;
        this.permanent = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition)) return false;
        Transition that = (Transition) o;
        return Objects.equal(getState(), that.getState()) &&
                Objects.equal(getInput(), that.getInput());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getState(), getInput());
    }
}
