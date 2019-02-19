package networking.layer;

import networking.neuron.Neuron;
import networking.structure.Volume;

public abstract class Layer {

    public Layer previous;

    public Volume<Neuron> neuronVolume;

    public void forward() {
        for (Neuron n: neuronVolume.values) {
            n.forward();
        }
    }

    public void backPropagate() {
        for (Neuron n: neuronVolume.values) {
            n.backPropagate();
        }
    }

    public void zeroLoss() {
        for (Neuron n: neuronVolume.values) {
            n.zeroLoss();
        }
    }
    public void applyChange(double learningRate) {
        for (Neuron n: neuronVolume.values) {
            n.applyChange(learningRate);
        }
    }

    public void setDimensions(int[] dims) {
        neuronVolume.dimensions = dims;
    }
}
