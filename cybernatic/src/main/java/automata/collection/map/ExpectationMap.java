package automata.collection.map;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import automata.exception.UndefinedException;
import automata.model.Transition;
import lombok.Data;

@Data
public class ExpectationMap {

    private BiMap<Transition, Double> map;

    public ExpectationMap(BiMap<Transition, Double> map) {
        this.map = map;
    }

    public ExpectationMap() {
        map = HashBiMap.create();
    }

    public ExpectationMap add(Transition transition, Double confidence) {
        map.put(transition, confidence);
        return this;
    }

    public Double get(Transition transition) throws UndefinedException {
        try {
            return map.get(transition);
        } catch (Exception e) {
            throw new UndefinedException("Transition -> Confidence mapping has not been specified", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpectationMap)) return false;
        ExpectationMap that = (ExpectationMap) o;
        return Objects.equal(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map);
    }
}
