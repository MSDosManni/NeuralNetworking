package networking.neuron;

import networking.structure.Volume;
import networking.filter.Filter;

public class Neuron {
    public Volume<Neuron> localConnection;
    public Filter filter;

    public float value;
    public float derivedValue;
    public float costImpact;

    public boolean useActivation = false;
    public Neuron() {}
    public Neuron(boolean useActivation) {
        this.useActivation = useActivation;
    }
    public Neuron(Volume<Neuron> lc) {
        localConnection = lc;
    }

    public void forward() {
        filter.attachedNeuron = this;
        filter.applyFilter();

        if (useActivation) {
            value = activate(value);
            derivedValue = derive(value);
        }
    }
    public void backPropagate() {
        filter.attachedNeuron = this;
        filter.backPropagate();
    }


    public void zeroLoss() {
        costImpact = 0;
        filter.zeroLoss();
    }
    public void applyChange(float learningRate) {
        filter.applyChange(learningRate);
    }

    private float activate(float value) {
        return (float) (1f / (1 + Math.exp(-value)));
    }
    private float derive(float value) {
        return value * (1f-value);
    }


}
