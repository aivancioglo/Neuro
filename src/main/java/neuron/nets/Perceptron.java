package neuron.nets;

import neuron.Const;
import neuron.components.Enter;
import neuron.components.Hidden;
import neuron.components.Out;
import neuron.support.FileWorker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Класс персептрона.
 */
public class Perceptron {
    private final double E;
    private final double A;

    private Enter[] enters;
    private HiddenLayer[] hiddenLayers;
    private Out[] outs;

    private double[] answer;

    private boolean isFileEmpty = false;
    private boolean isFileExist = false;
    private String filePath = "src/main/resources/memory.txt";

    private ArrayList<double[]> allWeights;

    /**
     * Конструктор Персептрона.
     *
     * @param e            Скорость обучения (эпсилон).
     * @param a            Момент (альфа).
     * @param enters       Входной слой. Массив Сенсоров.
     * @param hiddenLayers Скрытые слои.
     * @param outs         Выходной слой. Массив Реагирующих элементов.
     */
    public Perceptron(double e, double a, int enters, int[] hiddenLayers, int outs) {
        E = e;
        A = a;
        this.enters = new Enter[enters];
        this.hiddenLayers = new HiddenLayer[hiddenLayers.length];
        this.outs = new Out[outs];

        for (int i = 0; i < enters; i++) {
            this.enters[i] = new Enter(hiddenLayers[0]);
        }

        for (int i = 0; i < hiddenLayers.length; i++) {
            if ((i + 1) < hiddenLayers.length)
                this.hiddenLayers[i] = new HiddenLayer(hiddenLayers[i], hiddenLayers[i + 1]);
            else
                this.hiddenLayers[i] = new HiddenLayer(hiddenLayers[i], outs);
        }
    }

    public void init() {
        File memory = new File(filePath);

        try {
            if (!memory.exists()) {
                System.out.println("Создан файл с памятью Нейронной сети");
                initEntersWeights();
                initHiddensWeights();
                isFileExist = memory.createNewFile();
                saveMemory();
            } else {
                System.out.println("Считывание существующего файла с памятью Нейронной сети...");
                isFileExist = true;
                FileWorker worker = new FileWorker();
                allWeights = worker.readFile(filePath);
                initEntersWeights();
                initHiddensWeights();
            }

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enter(int... arg) {
        if (arg.length != enters.length) {
            System.out.println("Incorrect arguments!");
            return;
        }

        for (int i = 0; i < enters.length; i++) {
            enters[i].setInput(arg[i]);
        }

        calcHiddensValue();

        answer = new double[outs.length];

        for (int i = 0; i < outs.length; i++) {
            int count = hiddenLayers.length - 1;
            double exit = 0;

            for (int j = 0; j < hiddenLayers[count].getHiddens().length; j++) {
                exit += hiddenLayers[count].getHiddens()[j].getWeight()[i] * hiddenLayers[count].getHiddens()[j].getValue();
            }

            answer[i] = Const.sigmoid(exit);
        }
    }

    private void saveMemory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
            for (int i = 0; i < enters.length; i++) {
                for (int j = 0; j < hiddenLayers[0].getHiddens().length; j++) {
                    writer.write(enters[i].getWeight()[j] + " ");
                }

                writer.newLine();
            }

            for (int i = 0; i < hiddenLayers.length; i++) {
                for (int j = 0; j < hiddenLayers[i].getHiddens().length; j++) {
                    for (int k = 0; k < hiddenLayers[i].getHiddens()[j].getWeight().length; k++) {
                        writer.write(hiddenLayers[i].getHiddens()[j].getWeight()[k] + " ");
                    }
                    writer.newLine();
                }
            }

            isFileEmpty = false;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * Вычисляем значения внутреннего слоя.
     */
    private void calcHiddensValue() {
        for (int i = 0; i < hiddenLayers.length; i++) {
            for (int j = 0; j < hiddenLayers[i].getHiddens().length; j++) {
                double value = 0;

                if (i == 0) {
                    for (int k = 0; k < enters.length; k++) {
                        value += (enters[k].getInput() * enters[k].getWeight()[j]);
                    }
                } else {
                    for (int k = 0; k < hiddenLayers[i - 1].getHiddens().length; k++) {
                        value += (hiddenLayers[i - 1].getHiddens()[k].getWeight()[j] * hiddenLayers[i - 1].getHiddens()[k].getValue());
                    }
                }

                hiddenLayers[i].getHiddens()[j].setValue(Const.sigmoid(value));
            }
        }
    }

    private void initEntersWeights() {
        if (isFileEmpty || !isFileExist) {
            for (int i = 0; i < enters.length; i++) {
                double[] weights = new double[hiddenLayers[0].getHiddens().length];

                for (int j = 0; j < weights.length; j++) {
                    weights[j] = new Random().nextDouble();
                }

                enters[i].setWeight(weights);
            }
        } else {
            for (int i = 0; i < enters.length; i++) {
                enters[i].setWeight(allWeights.get(i));
            }
        }
    }

    private void initHiddensWeights() {
        if (isFileEmpty || !isFileExist) {
            for (int i = 0; i < hiddenLayers.length; i++) {
                for (int j = 0; j < hiddenLayers[i].getHiddens().length; j++) {
                    double[] weights;

                    if ((i + 1) == hiddenLayers.length) weights = new double[outs.length];
                    else weights = new double[hiddenLayers[i + 1].getHiddens().length];

                    for (int k = 0; k < weights.length; k++) {
                        weights[k] = new Random().nextDouble();
                    }

                    hiddenLayers[i].getHiddens()[j].setWeight(weights);
                }
            }
        } else {
            int count = enters.length;

            for (int i = 0; i < hiddenLayers.length; i++) {
                for (int j = 0; j < hiddenLayers[i].getHiddens().length; j++) {
                    hiddenLayers[i].getHiddens()[j].setWeight(allWeights.get(count));
                    count++;
                }
            }
        }
    }

    /**
     * Класс Скрытого слоя.
     */
    private class HiddenLayer {
        private Hidden[] hiddens;

        /**
         * Конструктор скрытых слоёв.
         *
         * @param count Количество Ассоциативных элементов.
         */
        public HiddenLayer(final int count, final int connects) {
            this.hiddens = new Hidden[count];

            for (int i = 0; i < count; i++) {
                hiddens[i] = new Hidden(connects);
            }
        }


        /**
         * Возвращает Ассоциативные элементы.
         *
         * @return Массив Ассоциативных элементов.
         */
        public Hidden[] getHiddens() {
            return hiddens;
        }

        public void setHiddens(final int count, final int connects) {
            for (int i = 0; i < count; i++) {
                hiddens[i] = new Hidden(connects);
            }
        }
    }

    public void showAllInfo() {
        System.out.println("=====================");
        System.out.println("--Main--Information--");
        System.out.println("=====================");

        System.out.println("Память существует: " + isFileExist);
        System.out.println("Память пуста: " + isFileEmpty);

        System.out.print("Enters: " + enters.length);
        if (enters.length == 0) System.out.print(" neuron");
        else System.out.print(" neurons");

        System.out.print(" (" + enters[0].getWeight().length + " - ");
        if (enters[0].getWeight().length == 1) System.out.println("connect)");
        else System.out.println("connects)");

        System.out.println("=====================");
        System.out.println("Hidden Layers: ");

        for (int i = 0; i < hiddenLayers.length; i++) {
            System.out.print(1 + i + " layer : " + hiddenLayers[i].getHiddens().length);
            if (hiddenLayers[i].getHiddens().length == 1) System.out.print(" neuron");
            else System.out.print(" neurons ");

            System.out.print("(" + hiddenLayers[i].getHiddens()[0].getWeight().length + " - ");
            if (hiddenLayers[i].getHiddens()[0].getWeight().length == 1) System.out.println("connect)");
            else System.out.println("connects)");
        }

        System.out.println("=====================");
        System.out.print("Outs: " + outs.length);
        if (outs.length == 1) System.out.println(" neuron");
        else System.out.println(" neurons");
        System.out.println("=====================");
        System.out.println();

        System.out.println("=====================");
        System.out.println("Enters weights:");
        for (int i = 0; i < enters.length; i++) {
            double[] weights = enters[i].getWeight();

            System.out.print((i + 1) + ": { ");

            for (int j = 0; j < weights.length; j++) {
                System.out.print(weights[j] + " ");
            }

            System.out.println("}");
        }
        System.out.println();

        System.out.println("Hidden layers:");
        for (int i = 0; i < hiddenLayers.length; i++) {
            System.out.println(1 + i + " layer value - weights:");
            for (int j = 0; j < hiddenLayers[i].getHiddens().length; j++) {
                double[] weights = hiddenLayers[i].getHiddens()[j].getWeight();

                System.out.print("   " + (j + 1) + ": { ");
                System.out.print(hiddenLayers[i].getHiddens()[j].getValue() + " - | ");
                for (int k = 0; k < weights.length; k++) {
                    System.out.print(weights[k] + " | ");
                }
                System.out.println("}");
            }
            System.out.println();
        }
        System.out.println("=====================");

        System.out.print("In: ");
        for (int i = 0; i < enters.length; i++) {
            System.out.print(enters[i].getInput() + " ");
        }
        System.out.println();
        System.out.println("=====================");

        System.out.println("Out: ");
        for (int i = 0; i < answer.length; i++) {
            System.out.print(answer[i] + " ");
        }
        System.out.println();
        System.out.println("=====================");
    }
}