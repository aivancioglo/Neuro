package neuron.support;

import java.io.*;
import java.util.ArrayList;

public class FileWorker {
    private ArrayList<double[]> weights = new ArrayList<>();

    public ArrayList<double[]> readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String data;

            while ((data = reader.readLine()) != null) {
                String[] sequence = data.split(" ");
                double[] neuronWeights = new double[sequence.length];

                for (int i = 0; i < sequence.length; i++) {
                    neuronWeights[i] = Double.valueOf(sequence[i]);
                }

                weights.add(neuronWeights);
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return weights;
    }
}
