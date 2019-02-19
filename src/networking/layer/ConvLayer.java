package networking.layer;

import networking.neuron.ConvNeuron;
import networking.neuron.Neuron;
import networking.structure.Volume;
import networking.filter.ConvFilter;

public class ConvLayer extends Layer {

    public ConvLayer(int size, int steps, int num, Layer previous) {
        this.previous = previous;
        ConvFilterArguments args = new ConvFilterArguments();
        args.size = size;
        args.steps = steps;
        args.num = num;
        neuronVolume = new Volume<Neuron>();
        int[] prevDims = previous.neuronVolume.dimensions;
        neuronVolume.dimensions = new int[] {prevDims[0]-args.size+1, prevDims[1]-args.size+1, args.num};
        neuronVolume.values = new Neuron[neuronVolume.dimensions[0] * neuronVolume.dimensions[1]* args.num];
        connectNeurons(args);
    }





    private void connectNeurons(ConvFilterArguments args) {
        ConvFilter[] convFilter = new ConvFilter[args.num];
        for (int i = 0;i<convFilter.length;i++) {
            convFilter[i] = new ConvFilter(args.size, previous.neuronVolume.dimensions[2]);
        }

        for (int i=0;i<neuronVolume.dimensions[0];i++) {
            for (int o=0;o<neuronVolume.dimensions[1];o++) {

                for (int u=0;u<neuronVolume.dimensions[2];u++) {
                    Neuron toSet = new Neuron();
                    neuronVolume.values[neuronVolume.getIndex((new int[]{i,o,u}))] = toSet;
                    toSet.localConnection = previous.neuronVolume.getSubVolume(new int[]{i,o,0}, new int[]{args.size, args.size, previous.neuronVolume.dimensions[2]});
                    toSet.filter = convFilter[u];
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
