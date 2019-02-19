package networking;

import networking.layer.Layer;
import networking.neuron.Neuron;
import networking.structure.DoubleVolume;
import networking.structure.Volume;

import java.util.ArrayList;
import java.util.List;

public class Network {
    List<Layer> layers = new ArrayList<Layer>();
    public double learningRate = 0.03f;

    public void addLayer(Layer l) {
        layers.add(l);
    }

    public Layer getLayer(int index) {
        return layers.get(index);
    }

    public Layer getLast() {
        if (layers.size()>0) {
            return layers.get(layers.size()-1);
        } else {
            return null;
        }
    }

    public void train(DoubleVolume input, DoubleVolume labels) {
        forward(input);

        for (int i=1;i<layers.size();i++) layers.get(i).zeroLoss();

        setLastLayerLoss(calculateLoss(labels));
        for (int i=layers.size()-1;i>0;i--) {
            layers.get(i).backPropagate();
        }

        for (int i=1;i<layers.size();i++) layers.get(i).applyChange(learningRate);
        //layers.get(1).applyChange(learningRate);
    }
    public void forward(DoubleVolume input) {
        setInput(input);
        for (int i=1;i<layers.size();i++) {
            layers.get(i).forward();
        }
    }

    private void setLastLayerLoss(DoubleVolume fv) {
        if (layers.size()>0) {
            Volume<Neuron> volToSet = layers.get(layers.size()-1).neuronVolume;
            for (int i=0;i<volToSet.values.length;i++) {
                volToSet.values[i].costImpact = fv.values[i];
            }
        }
    }
    private DoubleVolume calculateLoss(DoubleVolume trueLabels) {
        /*DoubleVolume ret = new DoubleVolume(trueLabels);
        DoubleVolume output = getOutput();
        for (int i=0;i<ret.values.length;i++) {
            ret.values[i] = 2 * (output.values[i] - trueLabels.values[i]);
        }
        return ret;*/

        return pointwiseSub(softmax(getOutput()),  trueLabels.values);
    }

    public double calculateCost(DoubleVolume trueLabels) {
        double sum = 0;
        DoubleVolume out = getOutput();
        for (int i=0;i<trueLabels.values.length;i++) {
            sum += Math.pow(trueLabels.values[i] - out.values[i], 2);
        }
        return sum;
    }

    private void setInput(DoubleVolume fv) {
        if (layers.size()>0) {
            Volume<Neuron> volToSet = layers.get(0).neuronVolume;
            for (int i=0;i<volToSet.values.length;i++) {
                volToSet.values[i].value = fv.values[i];
            }
        }
    }

    public DoubleVolume getOutput() {
        if (layers.size()>0) {
            Volume<Neuron> volToGet = layers.get(layers.size()-1).neuronVolume;
            DoubleVolume ret = new DoubleVolume(volToGet);
            for (int i=0;i<volToGet.values.length;i++) {
                ret.values[i] = volToGet.values[i].value;
            }
            return ret;
        }
        return null;
    }

    public int getMaxIndex(DoubleVolume fv) {
        int ret = -1;
        double maxVal = Double.MIN_VALUE;
        for (int i=0;i<fv.values.length;i++) {
            if (fv.values[i] > maxVal) {
                maxVal = fv.values[i];
                ret = i;
            }
        }
        return ret;
    }

    public double getAccuracy(DoubleVolume[] fvs, DoubleVolume[] labels) {
        int correctOnes = 0;
        for (int i=0;i<fvs.length;i++) {
            forward(fvs[i]);
            if (getMaxIndex(getOutput()) == getMaxIndex(labels[i])) correctOnes ++;
        }
        return (double) correctOnes / fvs.length;
    }


    private double[] softmax(DoubleVolume in) {
        double _base = 0;
        double[] ret = new double[in.values.length];
        for (int i=0;i<in.values.length;i++) {
            ret[i] = (double) Math.exp(in.values[i]);
            _base+=ret[i];
        }
        for (int i=0;i<ret.length;i++) {
            ret[i] /= _base;
        }
        return ret;
    }
    private DoubleVolume pointwiseSub(double[] a1, Double[] a2) {
        DoubleVolume ret = new DoubleVolume(a1.length);
        for (int i=0;i<a1.length;i++) {
            ret.values[i] = a1[i]-a2[i];
        }
        return ret;
    }
}
