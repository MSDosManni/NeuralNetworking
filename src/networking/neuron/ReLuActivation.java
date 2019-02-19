package networking.neuron;

public class ReLuActivation implements ActivationInterface {

    public double activate(double value) { return Math.max(0, value); }

    public double derive(double value) { return value>0 ? 1 : 0; }
}
