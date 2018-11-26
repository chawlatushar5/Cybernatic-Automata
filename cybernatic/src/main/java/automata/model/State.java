package automata.model;

import com.google.common.base.Objects;

import lombok.Data;

@Data
public class State {

    private final int number;
    private boolean initial;
    private boolean accepting;
    private boolean rewarding;
    private boolean punishable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return Objects.equal(getNumber(), state.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNumber());
    }
}
