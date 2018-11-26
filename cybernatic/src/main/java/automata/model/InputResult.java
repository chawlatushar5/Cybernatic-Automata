package automata.model;

import com.google.common.collect.BiMap;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class InputResult {

    private double Confidence;
    private State nextState;
    private List<Output> outputDist;
    private boolean isPermanent;
    private boolean isConditioned;

    public InputResult(){
        outputDist = new ArrayList<>();
        isPermanent = false;
        isPermanent = false;
    }

    public InputResult(State nextState, Double confidence, List<Output> outputs){
        outputDist = new ArrayList<>();
        this.nextState = nextState;
        this.Confidence = confidence;
        this.outputDist = outputs;
        isPermanent = false;
        isConditioned = false;
    }


    public String getProbableOutput(){
        double rand = Math.random();
        double cummulativeProb = 0.0;
        Collections.sort(outputDist, new Comparator<Output>() {
            @Override
            public int compare(Output o1, Output o2) {
                return (int) (o1.getNumber() - o2.getNumber());
            }
        });
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (Output item : outputDist) {
            cumulativeProbability += item.getNumber();
            if (p <= cumulativeProbability) {
                return item.getString();
            }
        }
        return null;
    }


    public InputResult addOutputWithPD(Output o) throws Exception{
        if (outputDist.stream().anyMatch(x -> x.getString().equals(o.getString())))
            throw new Exception("Output for that String already exists.");
        outputDist.add(o);
        return this;
    }

    public Output getOutputFromString(String s) {
        for (Output o: outputDist){
            if (o.getString().equals(s))
                return o;
        }
        return null;
    }
}
