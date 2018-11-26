package automata.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Output {

    private String string;
    // used as strength and probability
    private double number;
    private boolean isChosen;

    public Output(String i, double numb){
        this.string = i;
        this.number = numb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Output output = (Output) o;
        return Objects.equals(string, output.string);
    }

    @Override
    public int hashCode() {

        return Objects.hash(string);
    }
}
