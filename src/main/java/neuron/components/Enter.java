package neuron.components;

/**
 * Класс Сенсора.
 */
public class Enter extends Neuron {
    private int input;

    public Enter(final int count) {
        super(count);
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getInput() {
        return input;
    }

    public void setWeight(final double[] weight) {
        super.setWeight(weight);
    }

    public double[] getWeight() {
        return super.getWeight();
    }
}