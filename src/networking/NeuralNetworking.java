package networking;

import networking.layer.ActivationLayer;
import networking.layer.ConvLayer;
import networking.layer.FeedForwardLayer;
import networking.layer.MaxPoolLayer;
import networking.neuron.Neuron;
import networking.neuron.ReLuActivation;
import networking.neuron.SigmoidActivation;
import networking.structure.DoubleVolume;
import networking.structure.Interface;
import networking.structure.Volume;
import processing.core.*;

public class NeuralNetworking extends PApplet {

    public static Network myNetwork;

    public static int currentLayer;
    public static int currentZ;
    public static void main(String... args) {
        testSetup();
        PApplet.main("networking.NeuralNetworking");
    }

    private static void testSetup() {
        //myNetwork = feedForwardModel();
        //myNetwork = convModel();
        myNetwork = deepConvModel();

        myNetwork.learningRate = 0.0005f;

        System.out.println("Neurons connected");

        Interface.loadData();
        System.out.println("Data loaded");

        double[][] trainImages = Interface.getTrainImages();
        double[][] testImages = Interface.getTestImages();
        double[][] trainLabels = Interface.getTrainLabels();
        double[][] testLabels = Interface.getTestLabels();

        (new Thread() {
        public void run() {
            System.out.println("Overall accuracy");
            System.out.println(myNetwork.getAccuracy(DoubleVolume.getArray(testImages), DoubleVolume.getArray(testLabels)));
            double toReach = 20 * trainImages.length;
            for (int o=0;o<20;o++) {
                System.out.println("Training step: " + o);
                myNetwork.forward(new DoubleVolume(trainImages[0]));
                System.out.println("Cost: "+myNetwork.calculateCost(new DoubleVolume(trainLabels[0])));
                for (int i = 0; i < trainImages.length; i++) {
                    if (i%1000==0) {
                        System.out.println((trainImages.length*o+i)/toReach*100+"%");
                        myNetwork.forward(new DoubleVolume(trainImages[0]));
                        System.out.println("Cost: "+myNetwork.calculateCost(new DoubleVolume(trainLabels[0])));
                    }
                    myNetwork.train(new DoubleVolume(trainImages[i]), new DoubleVolume(trainLabels[i]));
                }
                System.out.println("Overall accuracy");
                System.out.println(myNetwork.getAccuracy(DoubleVolume.getArray(testImages), DoubleVolume.getArray(testLabels)));
                myNetwork.forward(new DoubleVolume(trainImages[0]));
                System.out.println("Cost: "+myNetwork.calculateCost(new DoubleVolume(trainLabels[0])));
            }
        }

        }).start();





    }

    public static Network feedForwardModel() {
        Network myNetwork = new Network();
        myNetwork.addLayer(new FeedForwardLayer(196));
        myNetwork.getLast().setDimensions(new int[] {14,14,1});
        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 32));
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast()));

        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 32));
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast()));

        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 10));
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast(), new ReLuActivation()));

        return myNetwork;
    }

    public static Network convModel() {
        Network myNetwork = new Network();
        FeedForwardLayer first = new FeedForwardLayer(196);
        first.neuronVolume.dimensions = new int[]{14, 14, 1};
        myNetwork.addLayer(first);
        ConvLayer cl = new ConvLayer(4, 1, 15, myNetwork.getLast());
        myNetwork.addLayer(cl);
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast()));
        myNetwork.addLayer(new MaxPoolLayer(3, myNetwork.getLast()));

        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 10));
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast(), new SigmoidActivation()));

        return myNetwork;
    }
    public static Network deepConvModel() {
        Network myNetwork = new Network();

        FeedForwardLayer first = new FeedForwardLayer(196);
        first.neuronVolume.dimensions = new int[]{14, 14, 1};
        myNetwork.addLayer(first);
        ConvLayer cl = new ConvLayer(5, 1, 15, myNetwork.getLast());
        myNetwork.addLayer(cl);
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast()));

        myNetwork.addLayer(new MaxPoolLayer(2, myNetwork.getLast()));

        myNetwork.addLayer(new ConvLayer(3,1,15, myNetwork.getLast()));
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast()));

        //myNetwork.addLayer(new MaxPoolLayer(2, myNetwork.getLast()));

        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 10));
        myNetwork.addLayer(new ActivationLayer(myNetwork.getLast(), new SigmoidActivation()));
        return myNetwork;
    }

    public void settings() {
        size(640, 480);
    }

    public void draw() {
        background(255);

        fill(0,0,255);
        textSize(40);
        text(currentLayer, 100,100);
        text(currentZ, 100, 250);
        text("Out: "+myNetwork.getMaxIndex(myNetwork.getOutput()),100,350);

        Volume<Neuron> curVol = myNetwork.layers.get(currentLayer).neuronVolume;
        double xStart = width/2f - curVol.dimensions[0] / 2 * 10;
        double yStart = height/2f - curVol.dimensions[1] / 2 * 10;
        for (int i=0;i<curVol.dimensions[0];i++) {
            for (int o=0;o<curVol.dimensions[1];o++) {
                double val = curVol.get(new int[] {i, o, currentZ}).value;
                fill((float) (255 - val * 255));
                rect((float)xStart + 10*i, (float)yStart + 10*o, 10, 10);
            }
        }
    }

    public void keyPressed() {
        switch (key) {
            case 'w':
                currentZ++;
                break;
            case 's':
                currentZ--;
                currentZ += myNetwork.layers.get(currentLayer).neuronVolume.dimensions[2];
                break;
            case 'a':
                currentLayer--;
                currentLayer += myNetwork.layers.size();
                break;
            case 'd':
                currentLayer++;
                break;
        }

        currentLayer %= myNetwork.layers.size();
        currentZ %= myNetwork.layers.get(currentLayer).neuronVolume.dimensions[2];
    }
}
