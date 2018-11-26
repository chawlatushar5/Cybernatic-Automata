package automata.function;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import automata.collection.map.ExpectationMap;
import automata.exception.UndefinedException;
import automata.model.Transition;

import java.util.HashMap;
import java.util.Map;

public class ExpectationFunction {

    private Map<Transition, ExpectationMap> function;

    public ExpectationFunction(Map<Transition, ExpectationMap> function) {
        this.function = function;
    }

    public ExpectationFunction() {
        function = new HashMap<Transition, ExpectationMap>();
    }

    public ExpectationFunction add(Transition transition, ExpectationMap map) {
        function.put(transition, map);
        return this;
    }

    public ExpectationFunction add(Transition transition, Transition input, Double output) {
        return add(
                transition,
                new ExpectationMap().add(input, output)
        );
    }

    public Double get(Transition transition, Transition input) throws UndefinedException {
        if (function.size()>0) {
            try {
                return function
                        .get(transition)
                        .get(input);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public ExpectationFunction updateExpectation(Transition transition, Transition input, Double expValue)
        throws UndefinedException {
        try {
            function.get(transition).add(input, expValue);
        } catch (Exception e) {
            throw new UndefinedException("Exception function for the transition has not been set", e);
        }
        return this;
    }

    public Boolean defined(Transition transition, Transition input) {

        if (function.containsKey(transition))
            if (function.get(transition).getMap().containsKey(input))
                return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpectationFunction)) return false;
        ExpectationFunction that = (ExpectationFunction) o;
        return Objects.equal(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(function);
    }
}
