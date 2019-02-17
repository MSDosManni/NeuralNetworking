package networking;

import networking.layer.ConvLayer;
import networking.layer.FeedForwardLayer;
import networking.layer.MaxPoolLayer;
import networking.neuron.Neuron;
import networking.structure.FloatVolume;
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
        myNetwork = convModel();
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
            System.out.println(myNetwork.getAccuracy(FloatVolume.getArray(testImages), FloatVolume.getArray(testLabels)));
            for (int o=0;o<1;o++) {
                System.out.println("Training step: " + o);
                myNetwork.forward(new FloatVolume(trainImages[0]));
                System.out.println("Cost: "+myNetwork.calculateCost(new FloatVolume(trainLabels[0])));
                for (int i = 0; i < trainImages.length/2; i++) {
                    if (i%500==0) {
                        System.out.println((float)i/trainImages.length*200+"%");
                        myNetwork.forward(new FloatVolume(testImages[0]));
                        System.out.println("Cost: "+myNetwork.calculateCost(new FloatVolume(testLabels[0])));
                    }
                    myNetwork.train(new FloatVolume(trainImages[i%100]), new FloatVolume(trainLabels[i%100]));
                }
                System.out.println("Overall accuracy");
                System.out.println(myNetwork.getAccuracy(FloatVolume.getArray(testImages), FloatVolume.getArray(testLabels)));
                myNetwork.forward(new FloatVolume(trainImages[0]));
                System.out.println("Cost: "+myNetwork.calculateCost(new FloatVolume(trainLabels[0])));
            }
        }

        }).start();





    }

    public static Network feedForwardModel() {
        Network myNetwork = new Network();
        myNetwork.addLayer(new FeedForwardLayer(196));
        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 32));
        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 32));
        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 10));
        return myNetwork;
    }

    public static Network convModel() {
        Network myNetwork = new Network();
        FeedForwardLayer first = new FeedForwardLayer(196);
        first.neuronVolume.dimensions = new int[]{14, 14, 1};
        myNetwork.addLayer(first);
        ConvLayer cl = new ConvLayer(4, 1, 15, myNetwork.getLast());
        myNetwork.addLayer(cl);

        myNetwork.addLayer(new MaxPoolLayer(3, myNetwork.getLast()));

        myNetwork.addLayer(new FeedForwardLayer(myNetwork.getLast(), 10));
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
        float xStart = width/2f - curVol.dimensions[0] / 2 * 10;
        float yStart = height/2f - curVol.dimensions[1] / 2 * 10;
        for (int i=0;i<curVol.dimensions[0];i++) {
            for (int o=0;o<curVol.dimensions[1];o++) {
                float val = curVol.get(new int[] {i, o, currentZ}).value;
                fill(255 - val * 255);
                rect(xStart + 10*i, yStart + 10*o, 10, 10);
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
