package automata.rule;

import automata.collection.set.States;
import automata.function.InputResultFunction;
import automata.model.*;

import static java.lang.Math.abs;

public class ApplyPunishment {

    private TraversalNode node;
    private Double K;
    private InputResultFunction inputResultFunction;
    private States allStates;
    private double V;

    public void setup (
            TraversalNode traversalNode,
            Double K,
            InputResultFunction inputResultFunction,
            States allStates,
            double v
    ) {
        this.node = traversalNode;
        this.K = K;
        this.inputResultFunction = inputResultFunction;
        this.allStates = allStates;
        this.V=v;

    }

    public void build() throws Exception {
        TraversalNode tmp = node;
        Double t=1.0;

        while (tmp != null && !tmp.isStopMarked()) {
            Integer inputProcessed = tmp.getDominantInput().getSymbol();
            for (Output out: inputResultFunction.getOutputList(tmp.getState(), tmp.getDominantInput().getSymbol())){
                Double denominator = 1 + t * (1 / inputResultFunction.getConfidence(tmp.getLastState(), tmp.getPreviousDominantInputSymbol()));
                if (out.equals(tmp.getOutput())) {
                    out.setNumber(out.getNumber() / denominator);
                } else {
                    out.setNumber((out.getNumber() + ((1 / (abs(out.getNumber()) - 1))) * denominator) / denominator);
                }
            }
            inputResultFunction.setConfidence(tmp.getState(), inputProcessed, t);
            for(State s: allStates.getStates()){
                for (Output out: inputResultFunction.getOutputList(s, inputProcessed)){
                    Double denominator = 1 + t * (1 / inputResultFunction.getConfidence(s, tmp.getPreviousDominantInputSymbol()));
                    if (out.equals(tmp.getOutput())) {
                        out.setNumber(out.getNumber() / denominator);
                    } else {
                        out.setNumber((out.getNumber() + ((1 / (abs(out.getNumber()) - 1))) * denominator) / denominator);
                    }
                }
                inputResultFunction.setConfidence(tmp.getState(), inputProcessed, V*t);
            }

            t = K*t;
            tmp = tmp.getPrevious();

        }
    }

}
