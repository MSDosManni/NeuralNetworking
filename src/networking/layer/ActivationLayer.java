package networking.layer;

import networking.filter.ActivationFilter;
import networking.neuron.ActivationInterface;
import networking.neuron.Neuron;
import networking.structure.Volume;

public class ActivationLayer extends Layer {

    public ActivationLayer(Layer previous) {
        this.previous = previous;
        setupActivationLayer(null);

    }

    public ActivationLayer(Layer previous, ActivationInterface ai) {
        this.previous = previous;
        setupActivationLayer(ai);
    }

    void setupActivationLayer(ActivationInterface ai) {
        neuronVolume = previous.neuronVolume.copy();


        for (int i=0;i<neuronVolume.values.length;i++) {
            Volume<Neuron> connectionVolume = new Volume<Neuron>();
            connectionVolume.values = new Neuron[] { previous.neuronVolume.values[i] };
            connectionVolume.dimensions = new int[] {1,1,1};

            neuronVolume.values[i] = new Neuron();

            neuronVolume.values[i].localConnection = connectionVolume;
            if (ai!=null)
                neuronVolume.values[i].filter = new ActivationFilter(ai);
            else
                neuronVolume.values[i].filter = new ActivationFilter(neuronVolume.values[i]);
        }
    }
}
