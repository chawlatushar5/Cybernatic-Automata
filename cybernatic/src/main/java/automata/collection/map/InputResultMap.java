package automata.collection.map;

import automata.model.InputResult;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Data;

@Data
public class InputResultMap {

    private BiMap<Integer, InputResult> inputToInputResultBiMap;

    public InputResultMap() {
        inputToInputResultBiMap = HashBiMap.create();
    }

    public InputResultMap(BiMap<Integer, InputResult> map) {
        inputToInputResultBiMap = map;
    }

    public InputResult getInputResult(int input) throws Exception {
        checkInputExists(input);
        return inputToInputResultBiMap.get(input);
    }


    public InputResultMap addInputResult(int input, InputResult inputResult) throws Exception {
        checkInputDNExists(input);
        inputToInputResultBiMap.put(input, inputResult);
        return this;
    }

    private void checkInputExists(int input) throws Exception{
        if (!inputToInputResultBiMap.containsKey(input))
            throw new Exception("Input Already Exists");
    }
    private void checkInputDNExists(int input) throws Exception{
        if (inputToInputResultBiMap.containsKey(input))
            throw new Exception("Input Already Exists");
    }

}
