package networking.structure;

import networking.neuron.Neuron;

public class DoubleVolume extends Volume <Double>  {

    {
        myClass = Double.class;
    }
    public DoubleVolume(double[] ar) {
        values = new Double[ar.length];
        for (int i=0;i<values.length;i++) {
            values[i] = ar[i];
        }
    }
    /*public DoubleVolume(double[] ar) {
        values = new Double[ar.length];
        for (int i=0;i<values.length;i++) {
            values[i] = (double) ar[i];
        }
    }*/
    public DoubleVolume(int size) {
        values = new Double[size];
    }

    public DoubleVolume(DoubleVolume toImmitate) {
        values = new Double[toImmitate.values.length];
        dimensions = toImmitate.dimensions;
    }

    public DoubleVolume(Volume<Neuron> neuronVolume) {
        values = new Double[neuronVolume.values.length];
        dimensions = neuronVolume.dimensions;
        for (int i=0;i<values.length;i++) {
            values[i] = neuronVolume.values[i].value;
        }
    }
    public double dot(DoubleVolume other) {
        double sum = 0;
        for (int i=0;i<other.values.length;i++) {
            sum += values[i] * other.values[i];
        }
        return sum;
    }

    public double dotRegion(DoubleVolume other, int[] region) {
        double sum = 0;

        for (int i=0;i<other.values.length;i++) {
            sum += other.values[i] * get(addVec(region, other.getVec(i)));
        }

        return sum;
    }


    public static DoubleVolume[] getArray(double[][] values) {
        DoubleVolume[] ret = new DoubleVolume[values.length];
        for (int i=0;i<ret.length;i++) {
            ret[i] = new DoubleVolume(values[i]);
        }
        return ret;
    }

    public void randomize(double lowerBound, double upperBound) {
        for (int i=0;i<values.length;i++) {
            values[i] = (new java.util.Random().nextDouble() * (upperBound - lowerBound)) + lowerBound;
        }
    }

}
