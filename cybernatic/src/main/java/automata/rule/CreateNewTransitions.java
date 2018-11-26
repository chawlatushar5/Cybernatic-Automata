package automata.rule;

import automata.function.*;
import automata.model.*;
import automata.collection.set.States;
import automata.collection.set.SymbolStrengthPairs;

import java.util.ArrayList;
import java.util.List;

public class CreateNewTransitions {


    private double Eta;
    private InputResultFunction inputResultFunction;
    private States allstates;
    private List<String> allOutputs;
    private TraversalNode node;
    private SymbolStrengthPairs allInputs;


    public void setup(InputResultFunction inputResultFunction, States allStates, List<String> allOutputs,SymbolStrengthPairs allInputs, TraversalNode current, double Eta) {
        this.Eta = Eta;
        this.inputResultFunction = inputResultFunction;
        this.allstates = allStates;
        this.allOutputs =allOutputs;
        this.node =current;
        this.allInputs = allInputs;
    }



    public void build() throws Exception {

        if (inputResultFunction.hasDefinedTransaction(node.getState(),allInputs.getEpsilon().getSymbol()) && !inputResultFunction.isPermanent(node.getState(), allInputs.getEpsilon().getSymbol())){
            inputResultFunction.removeTransition(node.getState(), allInputs.getEpsilon().getSymbol());
        }
        for(Alphabet a: node.getInput().getSet()){
            if (!inputResultFunction.hasDefinedTransaction(node.getState(), a.getSymbol())){
                State Qn = allstates.buildUniqueState();

                inputResultFunction.addTransition(node.getState(), a.getSymbol(), new InputResult().setNextState(Qn));
                inputResultFunction.addTransition(Qn, allInputs.getEpsilon().getSymbol(), new InputResult().setNextState(allstates.getAnchorState()));
                inputResultFunction.setPermanent(Qn, allInputs.getEpsilon().getSymbol(), false);
                State matchedState=null;

                for(State state: allstates.getStates()){
                    if (!node.getState().equals(state)) {
                        if (inputResultFunction.hasDefinedTransaction(state, a.getSymbol())) {
                            matchedState = state;
                            break;
                        }
                    }
                }
                if(matchedState!=null){
                    inputResultFunction.setOutputMap(node.getState(), a.getSymbol(), inputResultFunction.getOutputList(matchedState, a.getSymbol()));
                    inputResultFunction.setConfidence(node.getState(), a.getSymbol(), inputResultFunction.getConfidence(matchedState, a.getSymbol()));
                    if(matchedState.isRewarding())
                        inputResultFunction.getNextState(node.getState(), a.getSymbol()).setRewarding(true);
                    if(matchedState.isPunishable())
                        inputResultFunction.getNextState(node.getState(), a.getSymbol()).setPunishable(true);
                }else{
                    List<Output> outputDist = new ArrayList<>();
                    for(String outSymbol: allOutputs){
                        outputDist.add(new Output(outSymbol, (1-Eta)/(allOutputs.size()-1)));
                    }
                    outputDist.add(new Output(null, Eta));
                    inputResultFunction.setOutputMap(node.getState(), a.getSymbol(), outputDist);
                    inputResultFunction.setConfidence(node.getState(), a.getSymbol(), 0.1);

                }

            }
        }
    }


}
