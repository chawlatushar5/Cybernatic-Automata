package automata.model;

import com.google.common.base.Objects;

import lombok.Data;

@Data
public class Alphabet {

    private final Integer symbol;
    private double strength;
    //private boolean isMarked;


    public Alphabet(){
        symbol=null;
    }
    public Alphabet(Integer _symbol){
        this.symbol = _symbol;
    }

    public Alphabet(Integer _symbol, double strength){
        this.strength= strength;
        this.symbol = _symbol;
       // this.isMarked = isMarked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alphabet)) return false;
        Alphabet alphabet = (Alphabet) o;
        return Objects.equal(getSymbol(), alphabet.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSymbol());
    }
}
