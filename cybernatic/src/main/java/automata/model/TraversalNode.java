package automata.model;

import com.google.common.base.Objects;

import automata.collection.set.SymbolStrengthPairs;
import lombok.Data;

@Data
public class TraversalNode {
    private TraversalNode previous;
    private boolean StopMarked;

    // not sure if we need the rest
    // c
    private State State;
    private SymbolStrengthPairs Input;
    private Output Output;

    public TraversalNode(State current){
        this.State = current;
    }

    public TraversalNode setOutput(Output alphabet){
        this.Output = alphabet;
        System.out.println("Output is :"+ alphabet.getString() + ", " + alphabet.getNumber());
        return this;
    }

    public TraversalNode(State currentState, SymbolStrengthPairs Input, Output Output){
        this.Input = Input;
        this.Output = Output;
        this.State = currentState;
    }

    public Alphabet getDominantInput(){
        if (Input!=null)
            return Input.getDominantAlphabet();
        return null;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TraversalNode)) return false;
        TraversalNode that = (TraversalNode) o;
        return Objects.equal(getPrevious(), that.getPrevious()) &&
                Objects.equal(getState(), that.getState()) &&
                Objects.equal(getInput(), that.getInput()) &&
                Objects.equal(getOutput(), that.getOutput());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPrevious(), getState(), getInput(), getOutput());
    }

    public automata.model.State getLastState() {
        if (this.previous!=null)
            return this.previous.getState();
        return null;
    }

    public Integer getPreviousDominantInputSymbol(){
        if (this.previous!=null)
            if (this.previous.getDominantInput()!=null)
            return this.previous.getDominantInput().getSymbol();
        return null;
    }


    public Output getLastOutput() {
        if (this.previous!=null)
            return this.previous.getOutput();
        return  null;
    }

    public SymbolStrengthPairs getLastInput() {
        if (this.previous!=null)
            return this.previous.getInput();
        return  null;
    }
}
