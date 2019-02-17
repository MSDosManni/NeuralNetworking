package networking.structure;

import networking.neuron.Neuron;

public class Volume <T> {
    public T[] values;
    public int[] dimensions;
    public Class myClass = Neuron.class;



    public Volume() {}
    public Volume(Class<T> thisClass) {
        myClass = thisClass;
    }

    public T get(int[] vec) {
        return values[getIndex(vec)];
    }

    public int getIndex(int[] vec) {
        int dimSize = 1;
        int posSum = 0;
        for (int i=0;i<vec.length;i++) {
            posSum += dimSize * vec[i];
            dimSize *= dimensions[i];
        }
        return posSum;
    }

    public int[] getVec(int pos) {
        int[] vec = new int[dimensions.length];
        int up = 1;

        for (int i: dimensions) up *= i;

        for (int i=dimensions.length-1;i>=0;i--) {
            up /= dimensions[i];
            vec[i] = pos / up;
            pos %= up;

        }
        return vec;
    }

    public Volume<T> getSubVolume(int[] vec , int[] dims) {
        Volume<T> ret = new Volume<>(myClass);

        ret.values = (T[]) java.lang.reflect.Array.newInstance(myClass, dims[0] * dims[1] * dims[2]);

        ret.dimensions = dims;
        int iterator = 0;
        for (int u=0;u<dims[2];u++) {
            for (int o=0;o<dims[1];o++) {
                for (int i=0; i<dims[0]; i++) {
                    ret.values[iterator] = get(addVec(vec, new int[] {i,o,u}));
                    iterator++;
                }
            }
        }
        return ret;
    }

    public int[] addVec(int[] first, int[] second) {
        int[] ret = new int[first.length];
        for (int i=0;i<ret.length;i++) {
            ret[i] = first[i]+second[i];
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder();
        for (T value: values) {
            r.append(value.toString() + "\n");
        }
        return r.toString();
    }

}


