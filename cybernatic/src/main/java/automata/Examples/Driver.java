package automata.Examples;

public class Driver {
    public static void main(String [ ] args) throws Exception {
        System.out.println("\nCS 481 Condtioning Experiments \n");
        FirstOrderConditioning.FOC();
        FirstOrderConditioning_Extinction.FOCExtinction();
        SecondOrderConditioning.SOC();
        SecondOrederConditioning_Extinction.SOCExtinction();

    }
}
