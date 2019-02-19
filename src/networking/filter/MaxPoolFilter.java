package networking.filter;

import networking.structure.Volume;
import networking.neuron.Neuron;

public class MaxPoolFilter extends Filter {
    private int lastMaxPosition;
    private boolean zeroOthersChange = true;

    public MaxPoolFilter(Neuron attachedTo) {
        this.attachedNeuron = attachedTo;
    }
    public MaxPoolFilter() {}

    public void applyFilter() {
        Volume<Neuron> previous = attachedNeuron.localConnection;
        double maxVal = Double.MIN_VALUE;
        for (int i=0;i<previous.values.length;i++) {
            if (previous.values[i].value > maxVal) {
                maxVal = previous.values[i].value;
                lastMaxPosition = i;
            }
        }
        attachedNeuron.value = maxVal;
    }

    public void backPropagate() {

        if (zeroOthersChange) {
            for (int i=0;i<attachedNeuron.localConnection.values.length;i++) {
                Neuron n = attachedNeuron.localConnection.values[i];
                if (zeroOthersChange && i != lastMaxPosition) {
                    n.costImpact = 0;
                } else {
                    n.costImpact = attachedNeuron.costImpact;
                }
            }
        }

    }

    public void applyChange(double learningRate) {}
}
