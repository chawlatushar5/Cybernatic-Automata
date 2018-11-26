package automata.rule;

import automata.Examples.FirstOrderConditioning;
import automata.function.InputResultFunction;
import automata.model.*;
import automata.collection.set.States;
import automata.collection.set.SymbolStrengthPairs;
import automata.function.ExpectationFunction;

import java.util.List;
import java.util.Scanner;

public class CybernaticAutomatonOperation {
    private ExpectationFunction expectationFunction;
    private InputResultFunction inputResultFunction;
    private TraversalNode Node;
    private States AllStates;
    private SymbolStrengthPairs AllInputs;
    private List<String> AllOutputStrings;
    private double Alpha ;
    private double Beta ;
    private double Gamma;
    private double Eta;
    private double Tao;
    private double V;
    private double K;

    private CreateNewTransitions CNT;
    private UpdateExpectaions UE;
    private ApplyReward AR;
    private ApplyPunishment AP;
    private ApplyConditioning AC;

    public void setInitialState(InputResultFunction inputResultFunction,
                                ExpectationFunction expectationFunction,
                                States allStates,
                                SymbolStrengthPairs allInputs,
                                TraversalNode node,
                                double Eta,
                                double Alpha ,
                                double Beta ,
                                double Gamma,
                                double Tao,
                                double V,
                                double K,
                                List<String> outputs
                                ){
        this.inputResultFunction = inputResultFunction;
        this.expectationFunction = expectationFunction;
        this.AllInputs = allInputs;
        this.AllStates = allStates;
        this.Node = node;
        this.Alpha =Alpha ;
        this.Beta =Beta ;
        this.Gamma=Gamma;
        this.Eta=Eta;
        this.Tao=Tao;
        this.V=V;
        this.K=K;
        this.AllOutputStrings = outputs;
    }


    public void process(SymbolStrengthPairs inputRecieved) throws Exception {
        InitializeRules();
        Node.setInput(inputRecieved);

        //Node = new TraversalNode(AllStates.getInitialState(), new SymbolStrengthPairs(AllInputs.getEpsilon()), null);
        step2();


        Alphabet dominantInput = Node.getDominantInput();
        if (dominantInput!=null) {


            CNT.setup(inputResultFunction, AllStates, AllOutputStrings, AllInputs, Node, Eta);
            CNT.build();
            // create transition here
            System.out.println("Input recieved : " + dominantInput.getSymbol());
            String outputString = inputResultFunction.getProbableOutput(Node.getState(), dominantInput.getSymbol());
            if (outputString != null) {
                double outputStrength = (dominantInput.getStrength() * inputResultFunction.getConfidence(Node.getState(), dominantInput.getSymbol())) / (1 + inputResultFunction.getConfidence(Node.getState(), dominantInput.getSymbol()));
                Node.setOutput(new Output(outputString, outputStrength));
            }

            // Update Expectation
            UE.setup(Alpha, Beta, AllInputs, expectationFunction, inputResultFunction, AllStates, AllOutputStrings, Node);
            UE.build();
            TraversalNode newNode = new TraversalNode(inputResultFunction.getNextState(Node.getState(), Node.getDominantInput().getSymbol()));
            //Node.setNext(newNode);
            newNode.setPrevious(Node);
            Node = newNode;
            //Node = new TraversalNode(Node, inputResultFunction.getNextState(Node.getState(), Node.getDominantInput().getSymbol()));
            if (Node.getState().isRewarding()) {
                AR.setup(Node, K, inputResultFunction, AllStates, V);
                AR.build();
                // reward here
            } else if (Node.getState().isPunishable()) {
                AP.setup(Node, K, inputResultFunction, AllStates, V);
                AP.build();
            } else {
                AC.setup(Node, inputResultFunction, AllStates, AllInputs, Gamma, expectationFunction);
                AC.build();
            }
        }


    }

    private void InitializeRules() {
        CNT = new CreateNewTransitions();
        AC = new ApplyConditioning();
        UE = new UpdateExpectaions();
        AR = new ApplyReward();
        AP = new ApplyPunishment();
    }

    public void step2() throws Exception {
        if (Node.getInput()!=null && Node.getInput().getDominantAlphabet().getSymbol()==FirstOrderConditioning.Epsilon){
            System.out.println("Taking Epsilon Transition for no input received");
            if (inputResultFunction.hasDefinedTransaction(Node.getState(), AllInputs.getEpsilon().getSymbol())){
                if (!inputResultFunction.isPermanent(Node.getState(), AllInputs.getEpsilon().getSymbol())){
                    inputResultFunction.setPermanent(Node.getState(), AllInputs.getEpsilon().getSymbol(), true);
                }
                TraversalNode newNode = new TraversalNode(inputResultFunction.getNextState(Node.getState(), AllInputs.getEpsilon().getSymbol()));
                newNode.setPrevious(Node);
                Node = newNode;
            }
            AllStates.setAnchorState(Node.getState());
            Node.setStopMarked(true);
            step2();
        }
    }
}
