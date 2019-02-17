package networking.layer;

import networking.filter.MaxPoolFilter;
import networking.neuron.Neuron;
import networking.structure.Volume;

public class MaxPoolLayer extends Layer {

    public MaxPoolLayer(int size, Layer previous) {
        this.previous = previous;
        neuronVolume = new Volume<Neuron>();
        int[] prevDims = previous.neuronVolume.dimensions;
        neuronVolume.dimensions = new int[] {prevDims[0] / size, prevDims[1] / size, prevDims[2]};
        neuronVolume.values = new Neuron[neuronVolume.dimensions[0] * neuronVolume.dimensions[1]* neuronVolume.dimensions[2]];

        connectNeurons(size);

    }



    private void connectNeurons(int size) {
        for (int i=0;i<neuronVolume.dimensions[0];i++) {
            for (int o=0;o<neuronVolume.dimensions[1];o++) {
                for (int u=0;u<neuronVolume.dimensions[2];u++) {
                    Neuron toSet = new Neuron();
                    neuronVolume.values[neuronVolume.getIndex((new int[] {i,o,u}))] = toSet;
                    toSet.localConnection = previous.neuronVolume.getSubVolume(new int[] {i*size, o*size, u}, new int[] {size, size, 1});
                    toSet.filter = new MaxPoolFilter();
                }
            }
        }
    }

    class ConvFilterArguments {
        int size;
        int steps;
        int num;
    }
}
