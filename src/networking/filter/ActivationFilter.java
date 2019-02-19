package networking.filter;

import networking.neuron.ActivationInterface;
import networking.neuron.Neuron;
import networking.neuron.ReLuActivation;
import networking.neuron.SigmoidActivation;

public class ActivationFilter extends Filter {
    private ActivationInterface activationFunctions = new ReLuActivation();// SigmoidActivation();

    public ActivationFilter(Neuron attachedNeuron) { this.attachedNeuron = attachedNeuron; }
    public ActivationFilter(ActivationInterface ai) { activationFunctions = ai; };

    public void applyFilter() {
        attachedNeuron.value = activationFunctions.activate(attachedNeuron.localConnection.values[0].value);
    }

    public void backPropagate() {
        attachedNeuron.localConnection.values[0].costImpact = attachedNeuron.costImpact * activationFunctions.derive(attachedNeuron.value);
    }

    public void applyChange(double learningRate) {

    }
}
