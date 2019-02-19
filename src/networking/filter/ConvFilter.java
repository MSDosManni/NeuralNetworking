package networking.filter;

import networking.structure.DoubleVolume;
import networking.neuron.Neuron;
import networking.structure.Volume;

public class ConvFilter extends Filter {

    public DoubleVolume weights;

    public DoubleVolume weightChanges;
    public int weightChangeProposals;

    public ConvFilter() {}
    public ConvFilter(int widthSize, int depthSize) {
        weights = new DoubleVolume(widthSize*widthSize*depthSize);
        weights.randomize(-1,1);
        weights.dimensions = new int[] {widthSize, widthSize, depthSize};
        weightChanges = new DoubleVolume(weights);
    }


    public void applyFilter() {
        attachedNeuron.value = weights.dot(new DoubleVolume(attachedNeuron.localConnection));
    }

    public void backPropagate() {
        Volume<Neuron> previousNeurons = attachedNeuron.localConnection;
        for (int i=0;i<previousNeurons.values.length;i++) {
            //Impact der Vorgängerneurons hochziehen
            Neuron prevNeuron = previousNeurons.values[i];
            prevNeuron.costImpact += attachedNeuron.costImpact * weights.values[i];

            //changes für die weights hochziehen
            weightChanges.values[i] += attachedNeuron.costImpact * prevNeuron.value;
        }
        weightChangeProposals ++;
    }

    public void applyChange(double learningRate) {
        for (int i=0;i<weights.values.length;i++) {
            weights.values[i] -= weightChanges.values[i] * learningRate;// / weightChangeProposals;
            //System.out.println("Changing weight by: "+weightChanges.values[i]);
        }
    }

    @Override
    public void zeroLoss() {
        for (int i=0;i<weightChanges.values.length;i++) {
            weightChanges.values[i] = 0.0;
        }
        weightChangeProposals = 0;

    }
}
