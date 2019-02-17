package networking;

import networking.layer.Layer;
import networking.neuron.Neuron;
import networking.structure.FloatVolume;
import networking.structure.Volume;

import java.util.ArrayList;
import java.util.List;

public class Network {
    List<Layer> layers = new ArrayList<Layer>();
    public float learningRate = 0.003f;

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

    public void train(FloatVolume input, FloatVolume labels) {
        forward(input);

        for (int i=1;i<layers.size();i++) layers.get(i).zeroLoss();

        setLastLayerLoss(calculateLoss(labels));
        for (int i=layers.size()-1;i>0;i--) {
            layers.get(i).backPropagate();
        }

        for (int i=1;i<layers.size();i++) layers.get(i).applyChange(learningRate);
        //layers.get(1).applyChange(learningRate);
    }
    public void forward(FloatVolume input) {
        setInput(input);
        for (int i=1;i<layers.size();i++) {
            layers.get(i).forward();
        }
    }

    private void setLastLayerLoss(FloatVolume fv) {
        if (layers.size()>0) {
            Volume<Neuron> volToSet = layers.get(layers.size()-1).neuronVolume;
            for (int i=0;i<volToSet.values.length;i++) {
                volToSet.values[i].costImpact = fv.values[i];
            }
        }
    }
    private FloatVolume calculateLoss(FloatVolume trueLabels) {
        /*FloatVolume ret = new FloatVolume(trueLabels);
        FloatVolume output = getOutput();
        for (int i=0;i<ret.values.length;i++) {
            ret.values[i] = 2 * (output.values[i] - trueLabels.values[i]);
        }
        return ret;*/

        return pointwiseSub(softmax(getOutput()),  trueLabels.values);
    }

    public float calculateCost(FloatVolume trueLabels) {
        float sum = 0;
        FloatVolume out = getOutput();
        for (int i=0;i<trueLabels.values.length;i++) {
            sum += Math.pow(trueLabels.values[i] - out.values[i], 2);
        }
        return sum;
    }

    private void setInput(FloatVolume fv) {
        if (layers.size()>0) {
            Volume<Neuron> volToSet = layers.get(0).neuronVolume;
            for (int i=0;i<volToSet.values.length;i++) {
                volToSet.values[i].value = fv.values[i];
            }
        }
    }

    public FloatVolume getOutput() {
        if (layers.size()>0) {
            Volume<Neuron> volToGet = layers.get(layers.size()-1).neuronVolume;
            FloatVolume ret = new FloatVolume(volToGet);
            for (int i=0;i<volToGet.values.length;i++) {
                ret.values[i] = volToGet.values[i].value;
            }
            return ret;
        }
        return null;
    }

    public int getMaxIndex(FloatVolume fv) {
        int ret = -1;
        float maxVal = Float.MIN_VALUE;
        for (int i=0;i<fv.values.length;i++) {
            if (fv.values[i] > maxVal) {
                maxVal = fv.values[i];
                ret = i;
            }
        }
        return ret;
    }

    public float getAccuracy(FloatVolume[] fvs, FloatVolume[] labels) {
        int correctOnes = 0;
        for (int i=0;i<fvs.length;i++) {
            forward(fvs[i]);
            if (getMaxIndex(getOutput()) == getMaxIndex(labels[i])) correctOnes ++;
        }
        return (float) correctOnes / fvs.length;
    }


    private float[] softmax(FloatVolume in) {
        float _base = 0;
        float[] ret = new float[in.values.length];
        for (int i=0;i<in.values.length;i++) {
            ret[i] = (float) Math.exp(in.values[i]);
            _base+=ret[i];
        }
        for (int i=0;i<ret.length;i++) {
            ret[i] /= _base;
        }
        return ret;
    }
    private FloatVolume pointwiseSub(float[] a1, Float[] a2) {
        FloatVolume ret = new FloatVolume(a1.length);
        for (int i=0;i<a1.length;i++) {
            ret.values[i] = a1[i]-a2[i];
        }
        return ret;
    }
}
