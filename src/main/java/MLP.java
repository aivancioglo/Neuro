public class MLP {
    // Входы Нейросети
    private double[] enters;

    // Скрытые слои Нейросети.
    private double[] hidden;

    // Выход нейросети.
    private double outer;

    // Все соединения Входных словём со Скрытыми.
    private double[][] enterWeights;

    // Все соединения Скрытых слоёв с Выходным слоем.
    private double[] hiddenWeights;

    // Входные данные для обучения Нейросети.
    double[][] patterns = {
            {0, 0}, {0, 1}, {1, 0}, {1, 1}
    };

    // Ответы для обучающих паттернов.
    double[] answers = {0, 1, 1, 0};

    // Конструктор.
    public MLP() {
        enters = new double[patterns[0].length];
        hidden = new double[2];
        enterWeights = new double[enters.length][hidden.length];
        hiddenWeights = new double[hidden.length];
    }


    public void showResults() {
        for (int p = 0; p < patterns.length; p++) {
            for (int i = 0; i < enters.length; i++) {
                enters[i] = patterns[p][i];
            }

            countOuter();

            System.out.println(outer);
        }
    }


    // Инициализация весового значения Входных и Скрытых слоёв.
    public void initWeights() {
        for (int i = 0; i < enterWeights.length; i++) {
            for (int j = 0; j < enterWeights[i].length; j++) {
                enterWeights[i][j] = Math.random() * .3 + .1;
            }
        }

        for (int i = 0; i < hiddenWeights.length; i++) {
            hiddenWeights[i] = Math.random() * .3 + .1;
        }
    }

    // Высчитываем входящие данные.
    public void countOuter() {
        for (int i = 0; i < hidden.length; i++) {
            hidden[i] = 0;
            for (int j = 0; j < enters.length; j++) {
                hidden[i] += enters[j] * enterWeights[j][i];
            }
            if (hidden[i] > .5) {
                hidden[i] = 1;
            } else {
                hidden[i] = 0;
            }
        }

        outer = 0;
        for (int i = 0; i < hidden.length; i++) {
            outer += hidden[i] * hiddenWeights[i];
        }

        if (outer > .5) {
            outer = 1;
        } else {
            outer = 0;
        }
    }

    // Обучаем нейрон.
    public void study() {
        double[] err = new double[hidden.length];
        double gError;
        int it = 0;

        do {
            it++;
            System.out.println("Количество итераций при обучении: " + it);

            gError = 0;

            for (int p = 0; p < patterns.length; p++) {
                for (int i = 0; i < enters.length; i++) {
                    enters[i] = patterns[p][i];
                }

                countOuter();

                double lErr = answers[p] - outer;
                gError += Math.abs(lErr);

                for (int i = 0; i < hidden.length; i++) {
                    err[i] = lErr * hiddenWeights[i];
                }

                for (int i = 0; i < enters.length; i++) {
                    for (int j = 0; j < hidden.length; j++) {
                        enterWeights[i][j] += .1 * err[j] * enters[i];
                    }
                }

                for (int i = 0; i < hidden.length; i++) {
                    hiddenWeights[i] += .1 * lErr * hidden[i];
                }
            }
        } while (gError != 0);
    }
}