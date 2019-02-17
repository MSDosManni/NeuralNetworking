package networking.filter;

import networking.structure.FloatVolume;
import networking.neuron.Neuron;
import networking.structure.Volume;

public class ConvFilter extends Filter {

    public FloatVolume weights;

    public FloatVolume weightChanges;
    public int weightChangeProposals;

    public ConvFilter() {}
    public ConvFilter(int widthSize, int depthSize) {
        weights = new FloatVolume(widthSize*widthSize*depthSize);
        weights.randomize(-1,1);
        weights.dimensions = new int[] {widthSize, widthSize, depthSize};
        weightChanges = new FloatVolume(weights);
    }


    public void applyFilter() {
        attachedNeuron.value = weights.dot(new FloatVolume(attachedNeuron.localConnection));
    }

    public void backPropagate() {
        Volume<Neuron> previousNeurons = attachedNeuron.localConnection;
        for (int i=0;i<previousNeurons.values.length;i++) {
            //Impact der Vorgängerneurons hochziehen
            Neuron prevNeuron = previousNeurons.values[i];
            prevNeuron.costImpact += attachedNeuron.costImpact * attachedNeuron.derivedValue * weights.values[i];

            //changes für die weights hochziehen
            weightChanges.values[i] += attachedNeuron.costImpact * attachedNeuron.derivedValue * prevNeuron.value;
        }
        weightChangeProposals ++;
    }

    public void applyChange(float learningRate) {
        for (int i=0;i<weights.values.length;i++) {
            weights.values[i] -= weightChanges.values[i] * learningRate;// / weightChangeProposals;
            //System.out.println("Changing weight by: "+weightChanges.values[i]);
        }
    }

    @Override
    public void zeroLoss() {
        for (int i=0;i<weightChanges.values.length;i++) {
            weightChanges.values[i] = 0f;
        }
        weightChangeProposals = 0;

    }
}
