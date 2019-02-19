package networking.filter;

import networking.neuron.Neuron;
import networking.structure.DoubleVolume;

public class FeedForwardFilter extends ConvFilter {

    public FeedForwardFilter(Neuron attachedTo) {
        attachedNeuron = attachedTo;
        weights = new DoubleVolume(attachedNeuron.localConnection);
        weights.randomize(-1, 1);
        weightChanges = new DoubleVolume(attachedNeuron.localConnection);
    }
}
