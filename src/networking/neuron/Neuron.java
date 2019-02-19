package networking.neuron;

import networking.structure.Volume;
import networking.filter.Filter;

public class Neuron {
    public Volume<Neuron> localConnection;
    public Filter filter;

    public double value;
    public double costImpact;

    public Neuron() {}

    public Neuron(Volume<Neuron> lc) {
        localConnection = lc;
    }

    public void forward() {
        filter.attachedNeuron = this;
        filter.applyFilter();
    }
    public void backPropagate() {
        filter.attachedNeuron = this;
        filter.backPropagate();
    }


    public void zeroLoss() {
        costImpact = 0;
        filter.zeroLoss();
    }
    public void applyChange(double learningRate) {
        filter.applyChange(learningRate);
    }




}
