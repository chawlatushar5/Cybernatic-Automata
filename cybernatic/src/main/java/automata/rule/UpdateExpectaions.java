package automata.rule;

import automata.function.InputResultFunction;
import automata.model.TraversalNode;
import automata.collection.set.States;
import automata.collection.set.SymbolStrengthPairs;
import automata.function.ExpectationFunction;
import automata.model.Alphabet;
import automata.model.State;
import automata.model.Transition;

import java.util.List;

public class UpdateExpectaions {
    private Double Alpha;
    private Double Beta;
    private SymbolStrengthPairs allInputs;
    private ExpectationFunction expectationFunction;
    private InputResultFunction inputResultFunction;
    private States allstates;
    private List<String> allOutputs;
    private TraversalNode node;

    public void setup(
            Double Alpha,
            Double Beta,
            SymbolStrengthPairs allInputs,
            ExpectationFunction expectationFunction,
            InputResultFunction inputResultFunction,
            States allstates,
            List<String> allOutputs,
            TraversalNode node


    ){
        this.Alpha = Alpha;
        this.allInputs = allInputs;
        this.Beta = Beta;
        this.expectationFunction = expectationFunction;
        this.inputResultFunction = inputResultFunction;
        this.allstates = allstates;
        this.allOutputs = allOutputs;
        this.node = node;
    }



    public void build() throws Exception {
        if (node.getPrevious()==null || node.getState()==null){
            return;
        }
        Transition n1 = new Transition(node.getLastState(), node.getPreviousDominantInputSymbol());
        Transition n2 = new Transition(node.getState(), node.getDominantInput().getSymbol());
        if (expectationFunction.defined(n1, n2)){
            Double currentExpectation = expectationFunction.get(n1, n2)!=null ? expectationFunction.get(n1, n2) : 0;
            Double expectationChange = currentExpectation!=null ? Alpha*(1-currentExpectation): 0;
            expectationFunction.add(n1, n2, currentExpectation+expectationChange);
            Double newConfidence = inputResultFunction.getConfidence(node.getLastState(), node.getPreviousDominantInputSymbol())*(1-(Beta/expectationChange));
            inputResultFunction.setConfidence(node.getLastState(), node.getPreviousDominantInputSymbol(), newConfidence);
            currentExpectation = expectationFunction.get(n2, n1)!=null ? expectationFunction.get(n1, n2) : 0;
            expectationChange = currentExpectation!=null ? Alpha*(1-currentExpectation): 0;
            expectationFunction.add(n2, n1, currentExpectation+expectationChange);
            newConfidence = inputResultFunction.getConfidence(node.getState(), node.getDominantInput().getSymbol())*(1-(Beta/expectationChange));
            inputResultFunction.setConfidence(node.getState(), node.getDominantInput().getSymbol(), newConfidence);
        }else{
            expectationFunction.add(n1, n2, Alpha);
            expectationFunction.add(n2, n1, Alpha);
        }
        n1 = new Transition(node.getLastState(), node.getPreviousDominantInputSymbol());
        for (Alphabet a : allInputs.getSet()){
            n2 = new Transition(node.getState(), a.getSymbol());
            if (expectationFunction.defined(n1, n2) && !node.getInput().hasAlphabetSymbol(a.getSymbol())){
                Double currentExpec = expectationFunction.get(n1, n2)!=null ? expectationFunction.get(n1, n2) : 0 ;
                Double changeExpec = ((Double) (-Alpha*currentExpec))!=null ? (-Alpha*currentExpec) : 0 ;
                expectationFunction.add(n1,n2, currentExpec+changeExpec);
                Double currentConf = inputResultFunction.getConfidence(node.getLastState(), node.getPreviousDominantInputSymbol());
                inputResultFunction.setConfidence(node.getLastState(), node.getPreviousDominantInputSymbol(), currentConf*(1-(Beta/changeExpec)));

                currentExpec = expectationFunction.get(n2, n1);
                changeExpec = (-Alpha*currentExpec);
                expectationFunction.add(n2,n1, currentExpec+changeExpec);
                currentConf = inputResultFunction.getConfidence(node.getState(), a.getSymbol());
                inputResultFunction.setConfidence(node.getState(), a.getSymbol(), currentConf*(1-(Beta/changeExpec)));
            }
        }
        for (State s : allstates.getStates()){
            for (Alphabet a: allInputs.getSet()){
                if (!s.equals(node.getLastState()) && a.equals(node.getPreviousDominantInputSymbol())){
                    n1 = new Transition(node.getState(), node.getDominantInput().getSymbol());
                    n2 = new Transition(s, a.getSymbol());
                    if (expectationFunction.defined(n1, n2)){
                        Double currentExpec = expectationFunction.get(n1, n2);
                        Double changeExpec = ((Double) (-Alpha*currentExpec))!=null ? (-Alpha*currentExpec) : 0 ;
                        expectationFunction.add(n1,n2, currentExpec+changeExpec);
                        Double currentConf = inputResultFunction.getConfidence(node.getState(), node.getDominantInput().getSymbol());
                        inputResultFunction.setConfidence(node.getState(), node.getDominantInput().getSymbol(), currentConf*(1-(Beta/changeExpec)));

                        currentExpec = expectationFunction.get(n2, n1)!=null ? expectationFunction.get(n1, n2) : 0 ;
                        changeExpec = ((Double) (-Alpha*currentExpec))!=null ? (-Alpha*currentExpec) : 0 ;
                        expectationFunction.add(n2,n1, currentExpec+changeExpec);
                        currentConf = inputResultFunction.getConfidence(s, a.getSymbol());
                        inputResultFunction.setConfidence(s, a.getSymbol(), currentConf*(1-(Beta/changeExpec)));
                    }
                }
            }
        }

        for (Alphabet a: allInputs.getSet()){
            for (Alphabet b: allInputs.getSet()){
                if (!a.equals(b)) {
                    if (node.getInput().hasAlphabetSymbol(b.getSymbol()) && node.getInput().hasAlphabetSymbol(a.getSymbol())) {
                        n1 = new Transition(node.getState(), a.getSymbol());
                        n2 = new Transition(node.getState(), b.getSymbol());
                        if (expectationFunction.defined(n1, n2)) {
                            Double currentExpec = expectationFunction.get(n1, n2) != null ? expectationFunction.get(n1, n2) : 0;
                            Double changeEpec = Alpha * (1 - currentExpec);
                            expectationFunction.add(n1, n2, currentExpec + changeEpec);
                            inputResultFunction.setConfidence(node.getState(), a.getSymbol(), inputResultFunction.getConfidence(node.getState(), a.getSymbol()) * (1 - (Beta / changeEpec)));

                        } else {
                            expectationFunction.add(n1, n2, Alpha);
                        }
                    } else if (node.getInput().hasAlphabetSymbol(b.getSymbol()) || node.getInput().hasAlphabetSymbol(a.getSymbol())) {
                        n1 = new Transition(node.getState(), a.getSymbol());
                        n2 = new Transition(node.getState(), b.getSymbol());
                        if (expectationFunction.defined(n1, n2)) {
                            Double currentExpec = expectationFunction.get(n1, n2) != null ? expectationFunction.get(n1, n2) : 0;
                            Double changeEpec = ((Double) (-Alpha * currentExpec)) != null ? -Alpha * currentExpec : 0;
                            expectationFunction.add(n1, n2, currentExpec + changeEpec);
                            inputResultFunction.setConfidence(node.getState(), a.getSymbol(), inputResultFunction.getConfidence(node.getState(), a.getSymbol()) * (1 - (Beta / changeEpec)));
                        }
                    }
                }
            }
        }

    }
}
