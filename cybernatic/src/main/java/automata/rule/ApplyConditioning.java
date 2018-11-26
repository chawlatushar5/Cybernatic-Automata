package automata.rule;

import automata.collection.set.States;
import automata.collection.set.SymbolStrengthPairs;
import automata.function.ExpectationFunction;
import automata.function.InputResultFunction;
import automata.model.*;

public class ApplyConditioning {

    private TraversalNode node;
    private InputResultFunction inputResultFunction;
    private States allStates;
    private ExpectationFunction expectationFunction;
    private double Gamma;
    private SymbolStrengthPairs allInputs;


    public void setup (
            TraversalNode traversalNode,
            InputResultFunction inputResultFunction,
            States allStates,
            SymbolStrengthPairs inputs,
            double gamma,
            ExpectationFunction eFunction
    ) {
        this.node = traversalNode;
        this.allStates = allStates;
        this.inputResultFunction = inputResultFunction;
        this.Gamma=gamma;
        this.allInputs = inputs;
        this.expectationFunction = eFunction;
    }

    public void build() throws Exception
    {
        State c = node.getState();
        State Ql = node.getLastState();
        Integer Al = node.getPreviousDominantInputSymbol();
        Integer A = node.getInput()!=null ? node.getInput().getDominantAlphabet().getSymbol(): null;
        Transition n1, n2;

        if (node.getLastOutput()!=null && (!node.getLastOutput().equals(node.getOutput()))){
        //if (!allInputs.hasAlphabet(node.getLastOutput().getOutput ()) && !node.getLastOutput().equals(node.getOutput())){
            for(Alphabet a: allInputs.getSet()){
                n1 = new Transition(Ql, Al);
                n2 = new Transition(Ql, a.getSymbol());
                if (expectationFunction.defined(n1, n2) && node.getLastInput().hasAlphabetSymbol(a.getSymbol())){

                    for (Output out: inputResultFunction.getOutputList(node.getLastState(), a.getSymbol())) {
                        if (out.equals(node.getLastOutput())) {
                            double Prob_O_l = inputResultFunction.getOutputFromSymbol(Ql, a.getSymbol(), out.getString()).getNumber();

                            double numerator = Prob_O_l + Gamma * (1 / inputResultFunction.getConfidence(Ql, a.getSymbol()));
                            double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(Ql, a.getSymbol()));

                            inputResultFunction.getOutputFromSymbol(Ql, a.getSymbol(), out.getString()).setNumber(numerator / denominator);
                        }else{
                            double Prob_O_l = inputResultFunction.getOutputFromSymbol(Ql, a.getSymbol(), out.getString()).getNumber();

                            double numerator = Prob_O_l;
                            double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(Ql, a.getSymbol()));

                            inputResultFunction.getOutputFromSymbol(Ql, a.getSymbol(), out.getString()).setNumber(numerator / denominator);

                        }
                    }


                }
            }
            for(State s: allStates.getStates()){
                for (Alphabet a: allInputs.getSet()){
                    if (inputResultFunction.getNextState(s, a.getSymbol())!=null && inputResultFunction.getNextState(s, a.getSymbol()).equals(node.getLastState())){
                        n1 = new Transition(Ql, Al);
                        n2 = new Transition(s, a.getSymbol());
                        if (expectationFunction.defined(n1, n2)){
                            for (Output out: inputResultFunction.getOutputList(s, node.getPreviousDominantInputSymbol())) {
                                if (out.equals(node.getLastOutput())) {
                                    double Prob_O_l = inputResultFunction.getOutputFromSymbol(s, node.getPreviousDominantInputSymbol(), out.getString()).getNumber();

                                    double numerator = Prob_O_l + Gamma * (1 / inputResultFunction.getConfidence(s,  node.getPreviousDominantInputSymbol()));
                                    double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(s,  node.getPreviousDominantInputSymbol()));

                                    inputResultFunction.getOutputFromSymbol(s, node.getPreviousDominantInputSymbol(), out.getString()).setNumber(numerator / denominator);
                                }else{
                                    double Prob_O_l = inputResultFunction.getOutputFromSymbol(s, node.getPreviousDominantInputSymbol(), out.getString()).getNumber();

                                    double numerator = Prob_O_l;
                                    double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(s,  node.getPreviousDominantInputSymbol()));

                                    inputResultFunction.getOutputFromSymbol(s, node.getPreviousDominantInputSymbol(), out.getString()).setNumber(numerator / denominator);

                                }
                            }
                        }
                    }
                }
            }

            for (Alphabet a: allInputs.getSet()){
                n1 = new Transition(Ql, Al);
                n2 = new Transition(Ql, a.getSymbol());
                if (expectationFunction.defined(n1, n2) && node.getLastInput().hasAlphabetSymbol(a.getSymbol()) && !inputResultFunction.isConditioned(Ql, a.getSymbol())){
                    inputResultFunction.setConfidence(Ql, a.getSymbol(), Gamma);
                    //UpdateConditioning uc = new UpdateConditioning();
                    //uc.setup(node.getLastState(), a, 1.02)
                    UpdateConditioning UC = new UpdateConditioning();
                    UC.setup(node, inputResultFunction, allStates, allInputs,
                            Gamma, expectationFunction, Ql, a, Gamma * (1 / inputResultFunction.getConfidence(Ql, a.getSymbol())));
                    UC.build();
                }
            }
            for (State s: allStates.getStates()){
                for (Alphabet a: allInputs.getSet()){
                    if (inputResultFunction.getNextState(s, a.getSymbol())!=null && inputResultFunction.getNextState(s, a.getSymbol()).equals(node.getLastState())){
                        n1 = new Transition(Ql, Al);
                        n2 = new Transition(s, a.getSymbol());
                        boolean cond = inputResultFunction.isConditioned(s, a.getSymbol());
                        if (expectationFunction.defined(n1, n2) && !cond){
                            inputResultFunction.setConfidence(s, a.getSymbol(), Gamma);
                            UpdateConditioning UC = new UpdateConditioning();
                            UC.setup(node, inputResultFunction, allStates, allInputs,
                                    Gamma, expectationFunction, s, a, Gamma * (1 / inputResultFunction.getConfidence(s, a.getSymbol())));
                            UC.build();
                        }
                    }
                }
            }
        }
    }

}
