package networking.neuron;

public class SigmoidActivation implements ActivationInterface {

    public double activate(double value) { return (double) (1f/(1+Math.exp(-value))); }

    public double derive(double value) { return value * (1f - value); }
}
