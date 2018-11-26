package automata.Examples;

import automata.collection.set.States;
import automata.collection.set.SymbolStrengthPairs;
import automata.function.ExpectationFunction;
import automata.function.InputResultFunction;
import automata.model.*;
import automata.rule.CybernaticAutomatonOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondOrderConditioning {


    static public int UCSPlus = 1;
    static public int UCSMinus = 2;
    static public int CS1PLus = 3;
    static public int CS1Minus = 4;
    static public int CS2PLus = 5;
    static public int CS2Minus = 6;
    static public int Epsilon = -1;

    static public String UCRPlus = "UCR+";
    static public String UCRMinus = "UCR-";



    static private double Alpha = 0.05;
    static private double Beta = 0.05;
    static private double Gamma = 0.2;
    static private double Eta = 1.0;
    static private double Tao = 0.0;
    static private double V = 0.0;
    static private double K = 0.0;

    private static SymbolStrengthPairs allInputs;
    private static States allStates;
    private static InputResultFunction inputResultFunction;
    private static ExpectationFunction expectationFunction;
    private static List<String> allOutputs ;

    private static TraversalNode traversalNode;

    static private int LongTimeInput = 0;

    public static void SOC() throws Exception {
        InitializeComponents();
        State q0 = allStates.buildUniqueState().setInitial(true);
        allStates.setAnchorState(q0);
        State q1 = allStates.buildUniqueState();
        Double initialConfidence = 1000000.0;
        List<Output> outputsPlus = new ArrayList<>(Arrays.asList(new Output(UCRPlus, 100)));

        inputResultFunction.addNewStateWithInputResults(q0,UCSPlus, new InputResult(q1, initialConfidence, outputsPlus));

        State q2 = allStates.buildUniqueState();
        List<Output> outputsMinus = new ArrayList<>(Arrays.asList(new Output(UCRMinus, 100)));

        inputResultFunction.addNewStateWithInputResults(q1, UCSMinus, new InputResult(q2, initialConfidence, outputsMinus));

        inputResultFunction.addNewStateWithInputResults(q2, Epsilon, new InputResult(allStates.getAnchorState(), initialConfidence, null));
        traversalNode = new TraversalNode(allStates.getInitialState()).setInput(new SymbolStrengthPairs(allInputs.getEpsilon()));

        CybernaticAutomatonOperation Cybernatic = new CybernaticAutomatonOperation();


        Cybernatic.setInitialState(inputResultFunction, expectationFunction, allStates, allInputs, traversalNode, Eta,
                Alpha, Beta, Gamma, Tao, V, K, allOutputs);

        for (int i=0; i<60; i++) {
            System.out.println("Starting Experiment : "+ Math.addExact(i,1));

            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(CS1PLus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(UCSPlus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(CS1Minus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(UCSMinus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(Epsilon, 100.0)));

            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(CS2PLus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(CS1PLus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(CS2Minus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(CS1Minus, 100.0)));
            Cybernatic.process(new SymbolStrengthPairs(allInputs.addNewAlphabet(Epsilon, 100.0)));
        }



    }

    private static void InitializeComponents() {
        allStates = new States();
        allInputs = new SymbolStrengthPairs();
        inputResultFunction = new InputResultFunction();
        expectationFunction = new ExpectationFunction();
        allOutputs = new ArrayList<String>(Arrays.asList( UCRPlus, UCRMinus));
    }

}
