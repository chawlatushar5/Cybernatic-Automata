package automata.model;

import com.google.common.base.Objects;

import lombok.Data;

@Data
public class Outcome {
    // this class will be linked with the aincoming symbol of every state
    Alphabet output;
    double probability;
    State resultState;
    boolean isPermanent;
    private boolean isConditioned;

    public Outcome(State RS){
        this.resultState = RS;
    }
    public Outcome(State RS, Alphabet output, double prob, boolean isPermanent){
        this.isPermanent = isPermanent;
        this.probability = prob;
        this.output = output;
        this.resultState = RS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Outcome)) return false;
        Outcome outcome = (Outcome) o;
        return Double.compare(outcome.getProbability(), getProbability()) == 0 &&
                isPermanent() == outcome.isPermanent() &&
                Objects.equal(getOutput(), outcome.getOutput()) &&
                Objects.equal(getResultState(), outcome.getResultState());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOutput(), getProbability(), getResultState(), isPermanent());
    }
}
