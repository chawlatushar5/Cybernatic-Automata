package automata.rule;

import automata.collection.set.States;
import automata.collection.set.SymbolStrengthPairs;
import automata.function.ExpectationFunction;
import automata.function.InputResultFunction;
import automata.model.*;

public class UpdateConditioning {

    private TraversalNode node;
    private States states;
    private ExpectationFunction expectationFunction;
    private double Gamma;
    private SymbolStrengthPairs Inputs;
    private InputResultFunction inputResultFunction;

    private State q_prime;
    private Alphabet a_prime;
    private Double s_factor;

    public  void setup (
            TraversalNode traversalNode,
            InputResultFunction inputResultFunction,

            States allStates,
            SymbolStrengthPairs inputs,
            double gamma,
            ExpectationFunction eFunction,
            State q,
            Alphabet a,
            Double s
    ) {
        this.node = traversalNode;
        this.inputResultFunction = inputResultFunction;
        this.states = allStates;
        this.Gamma=gamma;
        this.Inputs = inputs;
        this.expectationFunction = eFunction;
        this.q_prime = q;
        this.a_prime = a;
        this.s_factor = s;
    }

    public void build() throws Exception {
        if (s_factor > 0) {
            for (Alphabet a: Inputs.getSet()) {
                if (expectationFunction.defined(new Transition(q_prime, a_prime.getSymbol()), new Transition(q_prime, a.getSymbol())) &&
                        inputResultFunction.isConditioned(q_prime, a.getSymbol())) {
                    for (Output out: inputResultFunction.getOutputList(node.getLastState(), a.getSymbol())) {
                        if (out.equals(node.getLastOutput())) {
                            double Prob_O_l = inputResultFunction.getOutputFromSymbol(q_prime, a.getSymbol(), out.getString()).getNumber();

                            double numerator = Prob_O_l + Gamma * (1 / inputResultFunction.getConfidence(q_prime, a.getSymbol()));
                            double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(q_prime, a.getSymbol()));

                            inputResultFunction.getOutputFromSymbol(q_prime, a.getSymbol(), out.getString()).setNumber(numerator / denominator);
                        }else{
                            double Prob_O_l = inputResultFunction.getOutputFromSymbol(q_prime, a.getSymbol(), out.getString()).getNumber();

                            double numerator = Prob_O_l;
                            double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(q_prime, a.getSymbol()));

                            inputResultFunction.getOutputFromSymbol(q_prime, a.getSymbol(), out.getString()).setNumber(numerator / denominator);
                        }
                    }
                }
            }
            for(State s: states.getStates()){
                for (Alphabet a: Inputs.getSet()){
                    if (inputResultFunction.getNextState(s, a.getSymbol())!=null && inputResultFunction.getNextState(s, a.getSymbol()).equals(q_prime)){
                        if (expectationFunction.defined(new Transition(q_prime, a_prime.getSymbol()), new Transition(s, a.getSymbol()))) {
                            for (Output out: inputResultFunction.getOutputList(q_prime, a_prime.getSymbol())) {
                                if (out.equals(node.getLastOutput())) {
                                    double Prob_O_l = inputResultFunction.getOutputFromSymbol(s, a.getSymbol(), out.getString()).getNumber();

                                    double numerator = Prob_O_l + Gamma * (1 / inputResultFunction.getConfidence(s,  a.getSymbol()));
                                    double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(s,  a.getSymbol()));

                                    inputResultFunction.getOutputFromSymbol(q_prime, a_prime.getSymbol(), out.getString()).setNumber(numerator / denominator);
                                }else{
                                    double Prob_O_l = inputResultFunction.getOutputFromSymbol(s, a.getSymbol(), out.getString()).getNumber();

                                    double numerator = Prob_O_l;
                                    double denominator = 1 + Gamma * (1 / inputResultFunction.getConfidence(s,  a.getSymbol()));

                                    inputResultFunction.getOutputFromSymbol(s, a.getSymbol(), out.getString()).setNumber(numerator / denominator);
                                }
                            }
                        }
                    }
                }
            }



            for (Alphabet a: Inputs.getSet()){
                if (expectationFunction.defined(new Transition(q_prime, a_prime.getSymbol()), new Transition(q_prime, a.getSymbol())) &&
                        node.getInput()!=null && node.getInput().hasAlphabetSymbol(a.getSymbol()) &&
                        !inputResultFunction.isConditioned(q_prime, a_prime.getSymbol())) {
                    inputResultFunction.setConfidence(q_prime, a.getSymbol(), Gamma);
                    UpdateConditioning UC = new UpdateConditioning();
                    UC.setup(node, inputResultFunction, states, Inputs, Gamma,
                            expectationFunction, q_prime, a, Gamma * (1 / inputResultFunction.getConfidence(q_prime, a.getSymbol())));
                    UC.build();
                }
            }

            for (State s:states.getStates()){
                for (Alphabet a: Inputs.getSet()){
                    if (inputResultFunction.getNextState(s, a.getSymbol())!=null && inputResultFunction.getNextState(s, a.getSymbol()).equals(node.getLastState())){
                        boolean cond = inputResultFunction.isConditioned(s, a.getSymbol());
                        if (expectationFunction.defined(new Transition(q_prime, a_prime.getSymbol()), new Transition(s, a.getSymbol())) && !cond){
                            inputResultFunction.setConfidence(s, a.getSymbol(), Gamma);
                            UpdateConditioning UC =new UpdateConditioning();
                            UC.setup(node, inputResultFunction, states, Inputs,
                                    Gamma, expectationFunction, s, a, Gamma * (1 / inputResultFunction.getConfidence(s, a.getSymbol())));
                            UC.build();
                        }
                    }
                }
            }

        }
    }
}
