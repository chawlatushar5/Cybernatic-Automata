package automata.function;

import automata.collection.map.InputResultMap;
import automata.model.Alphabet;
import automata.model.InputResult;
import automata.model.Output;
import automata.model.State;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class InputResultFunction {

    private Map<State, InputResultMap> function;

    public InputResultFunction() {
        function = new HashMap<State, InputResultMap>();
    }

    public InputResultMap getInputResultMap(State s) throws Exception {
        checkStateExists(s);
        return function.get(s);
    }

    public boolean  isPermanent(State s, Integer input) throws Exception {
        checkStateAndInputExists(s, input);
        return function.get(s).getInputResult(input).isPermanent();
    }
    public void  setPermanent(State s, Integer input, boolean val) throws Exception {
        checkStateAndInputExists(s, input);
        function.get(s).getInputResult(input).setPermanent(val);
    }

    public double getConfidence(State s, Integer input) throws Exception {
        try {
            checkStateAndInputExists(s, input);
        }catch (Exception e){
            return 0;
        }
        return function.get(s).getInputToInputResultBiMap().get(input).getConfidence();
    }

    public State getNextState(State s, Integer input) throws Exception{
        try {
            checkStateAndInputExists(s, input);
        }catch (Exception e){
            return null;
        }
        return function.get(s).getInputToInputResultBiMap().get(input).getNextState();
    }

    public List<Output> getOutputList(State s, Integer input) throws Exception {
        try {
            checkStateAndInputExists(s, input);
        }catch (Exception e){
            return new ArrayList<Output>();
        }
        return function.get(s).getInputToInputResultBiMap().get(input).getOutputDist();
    }

    public Output getOutputFromSymbol(State state, Integer inpput, String s) throws Exception {
        checkStateAndInputExists(state, inpput);
        return function.get(state).getInputResult(inpput).getOutputFromString(s);
    }
    public void checkStateExists(State s) throws Exception {
        if (!function.containsKey(s)) {
            throw new Exception("State not found");
        }
        return;
    }
    public void checkStateAndInputExists(State s, Integer input) throws Exception{
        checkStateExists(s);
        if (!function.get(s).getInputToInputResultBiMap().containsKey(input)){
            throw new Exception("Input not found");
        }
        return;
    }

    public boolean hasDefinedTransaction(State s, Integer input) throws Exception{
        if(function.containsKey(s)) {
            if (function.get(s).getInputToInputResultBiMap().containsKey(input)) {
                if (function.get(s).getInputResult(input).getNextState() != null)
                    return true;
            }
        }
        return false;
    }

    public InputResultFunction setOutputMap(State s, Integer input, List<Output> list) throws Exception {
        checkStateAndInputExists(s, input);
        function.get(s).getInputToInputResultBiMap().get(input).setOutputDist(list);
        return this;
    }

    public String getProbableOutput(State s, Integer input) throws Exception {
        checkStateAndInputExists(s, input);
        return function.get(s).getInputResult(input).getProbableOutput();
    }



    public InputResultFunction addNewInputResultToExistingState(State s, Integer input, InputResult inputResult) throws Exception {
        checkStateExists(s);
        function.get(s).addInputResult(input, inputResult);
        return this;
    }

    public InputResultFunction addNewStateWithInputResults(State s, Integer input, InputResult inputResult) throws Exception {
        InputResultMap stateMapping = new InputResultMap().addInputResult(input, inputResult);
        function.put(s, stateMapping);
        return this;
    }

    public void removeTransition(State state, Integer input) {
        function.get(state).getInputToInputResultBiMap().remove(input);
    }

    public InputResultFunction addTransition(State state, Integer symbol, InputResult qn) throws Exception {
        if (function.containsKey(state)) {
            function.get(state).getInputToInputResultBiMap().put(symbol, qn);
        }
        else {
            InputResultMap mp = new InputResultMap();
            mp.getInputToInputResultBiMap().put(symbol, qn);
            function.put(state, mp);
        }
        return this;
    }

    public void setConfidence(State state, Integer symbol, double confidence) throws Exception {
        checkStateAndInputExists(state, symbol);
        function.get(state).getInputResult(symbol).setConfidence(confidence);
    }

    public boolean isConditioned(State ql, Integer symbol) throws Exception {
        checkStateAndInputExists(ql, symbol);
        return function.get(ql).getInputResult(symbol).isConditioned();
    }

    public InputResultFunction setConditioned(State ql, Integer symbol, boolean cond) throws Exception {
        checkStateAndInputExists(ql, symbol);
        function.get(ql).getInputResult(symbol).setConditioned(cond);
        return this;
    }
}
