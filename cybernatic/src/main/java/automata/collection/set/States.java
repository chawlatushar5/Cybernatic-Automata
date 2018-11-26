package automata.collection.set;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import automata.model.State;
import lombok.Data;

@Data
public class States {

    private Set<State> states;
    private State anchorState;

    public States() {
        states = Sets.newHashSet();
    }

    public States(Set<State> states) {
        this.states = states;
    }

    public State getInitialState(){
        for (State s: states){
            if (s.isInitial())
                return s;
        }
        return null;
    }

    public States addStates(State... states) {
        this.states.addAll(Arrays.asList(states));
        return this;
    }

    public States buildRewardingStates() {
        return new States(
                states.parallelStream()
                        .filter(state -> state.isRewarding())
                        .collect(Collectors.toSet())
        );
    }

    public States buildPunishableStates() {
        return new States(
                states.parallelStream()
                        .filter(state -> state.isPunishable())
                        .collect(Collectors.toSet())
        );
    }

    public State buildUniqueState() {
        int length = states.parallelStream()
                .map(state -> state.getNumber())
                .collect(Collectors.toList())
                .size();
        State state = new State(length + 1);
        states.add(state);
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof States)) return false;
        States states1 = (States) o;
        return Objects.equal(getStates(), states1.getStates());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getStates());
    }
}
