import neuron.Const;
import neuron.nets.Perceptron;

import java.io.IOException;

public class Main implements Const {
    public static void main(String[] args) throws IOException {
        Perceptron perceptron = new Perceptron(.1, .1, 2, new int[]{3,3,3}, 2);

        perceptron.init();
        perceptron.enter(1, 0);
        perceptron.showAllInfo();

//        MLP mlp = new MLP();
//
//        mlp.initWeights();
//        mlp.showResults();
//        mlp.study();
//        mlp.showResults();
    }
}