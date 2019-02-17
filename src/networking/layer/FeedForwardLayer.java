package networking.layer;

import networking.filter.FeedForwardFilter;
import networking.neuron.Neuron;
import networking.structure.Volume;

public class FeedForwardLayer extends Layer  {

    public FeedForwardLayer(int neuronNum) {
        neuronVolume = new Volume<>();
        neuronVolume.dimensions = new int[] {neuronNum,1,1};
        neuronVolume.values = new Neuron[neuronNum];

        for (int i=0;i<neuronNum;i++) {
            neuronVolume.values[i] = new Neuron(true);
        }
    }

    public FeedForwardLayer(Layer previous, int neuronNum) {
        neuronVolume = new Volume<>();
        neuronVolume.dimensions = new int[] {neuronNum,1,1};
        neuronVolume.values = new Neuron[neuronNum];

        for (int i=0;i<neuronNum;i++) {
            Neuron newNeuron = new Neuron(previous.neuronVolume);
            neuronVolume.values[i] = newNeuron;
            newNeuron.filter = new FeedForwardFilter(newNeuron);
            newNeuron.useActivation = true;
        }
    }


}
