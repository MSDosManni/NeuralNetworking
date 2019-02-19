package networking.filter;

import networking.neuron.Neuron;

public abstract class Filter {

    public Neuron attachedNeuron;

    public abstract void applyFilter();

    public abstract void backPropagate();

    public abstract void applyChange(double learningRate);

    public void zeroLoss() {}
}
