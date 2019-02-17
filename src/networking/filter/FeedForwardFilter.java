package networking.filter;

import networking.neuron.Neuron;
import networking.structure.FloatVolume;

public class FeedForwardFilter extends ConvFilter {

    public FeedForwardFilter(Neuron attachedTo) {
        attachedNeuron = attachedTo;
        weights = new FloatVolume(attachedNeuron.localConnection);
        weights.randomize(-1, 1);
        weightChanges = new FloatVolume(attachedNeuron.localConnection);
    }
}
