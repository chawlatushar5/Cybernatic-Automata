package automata.collection.set;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import automata.model.Alphabet;
import lombok.Data;

@Data
public class SymbolStrengthPairs {

    private Set<Alphabet> set;
    private Timestamp lastupdated;

    public SymbolStrengthPairs() {
        set = Sets.newHashSet();
    }

    public SymbolStrengthPairs(Alphabet input) {
        set = Sets.newHashSet();
        set.add(input);
    }

    public Alphabet getEpsilon(){
        return new Alphabet(-1, 0);
    }

//    public SymbolStrengthPairs mockData(){
//        set.add(new Alphabet("a", 0.1, false));
//        set.add(new Alphabet("b", 0.2, false));
//        set.add(new Alphabet("c", 0.4, false));
//        set.add(new Alphabet("d", 0.1, false));
//        set.add(new Alphabet("e", 0.5, false));
//        return this;
//
//    }

    public Set<Alphabet> getSet(){
        return  set;
    }

    public Alphabet getDominantAlphabet(){
        Alphabet result = null;
        for (Alphabet b: set
             ) {
            if (result == null || result.getStrength()<b.getStrength()){
                result = b;
            }
        }
        return result;
    }

//    public Alphabet getMarkedAlphabet(){
//        for (Alphabet b : set){
//            if (b.isMarked())
//                return b;
//        }
//        return null;
//    }



    public SymbolStrengthPairs add(Alphabet... alphabets) {
        set.addAll(Arrays.asList(alphabets));
        lastupdated = new Timestamp(System.currentTimeMillis());
        return this;
    }

    public SymbolStrengthPairs AddPairs(SymbolStrengthPairs pairs){
        for (Alphabet a: pairs.getSet()){
            this.Add(a);
        }
        return this;
    }

    public Alphabet Add(Alphabet a){
        if (!set.contains(a))
            set.add(a);
        lastupdated = new Timestamp(System.currentTimeMillis());
        return a;
    }

    public Alphabet addNewAlphabet(Integer symbol, Double strength){
        Alphabet a = new Alphabet(symbol, strength);
        this.Add(a);
        return a;
    }

    public boolean hasAlphabetSymbol(int symbol){
        for (Alphabet a: set){
            if ((a.getSymbol()==symbol))
                return true;
        }
        return false;
    }

    public boolean hasAlphabet(Alphabet alph){
        for (Alphabet a: set){
            if (a.equals(alph))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SymbolStrengthPairs)) return false;
        SymbolStrengthPairs that = (SymbolStrengthPairs) o;
        return Objects.equal(getSet(), that.getSet());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSet());
    }
}
