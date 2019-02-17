package networking.structure;

import networking.neuron.Neuron;

public class FloatVolume extends Volume <Float>  {

    {
        myClass = Float.class;
    }
    public FloatVolume(float[] ar) {
        values = new Float[ar.length];
        for (int i=0;i<values.length;i++) {
            values[i] = ar[i];
        }
    }
    public FloatVolume(double[] ar) {
        values = new Float[ar.length];
        for (int i=0;i<values.length;i++) {
            values[i] = (float) ar[i];
        }
    }
    public FloatVolume(int size) {
        values = new Float[size];
    }

    public FloatVolume(FloatVolume toImmitate) {
        values = new Float[toImmitate.values.length];
        dimensions = toImmitate.dimensions;
    }

    public FloatVolume(Volume<Neuron> neuronVolume) {
        values = new Float[neuronVolume.values.length];
        dimensions = neuronVolume.dimensions;
        for (int i=0;i<values.length;i++) {
            values[i] = neuronVolume.values[i].value;
        }
    }
    public float dot(FloatVolume other) {
        float sum = 0;
        for (int i=0;i<other.values.length;i++) {
            sum += values[i] * other.values[i];
        }
        return sum;
    }

    public float dotRegion(FloatVolume other, int[] region) {
        float sum = 0;

        for (int i=0;i<other.values.length;i++) {
            sum += other.values[i] * get(addVec(region, other.getVec(i)));
        }

        return sum;
    }


    public static FloatVolume[] getArray(double[][] values) {
        FloatVolume[] ret = new FloatVolume[values.length];
        for (int i=0;i<ret.length;i++) {
            ret[i] = new FloatVolume(values[i]);
        }
        return ret;
    }

    public void randomize(float lowerBound, float upperBound) {
        for (int i=0;i<values.length;i++) {
            values[i] = (float) (Math.random() * (upperBound - lowerBound)) + lowerBound;
        }
    }

}
