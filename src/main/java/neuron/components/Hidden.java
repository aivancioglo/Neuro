package neuron.components;

/**
 * Класс Ассоциативного элемента.
 */
public class Hidden extends Neuron {

    public Hidden(final int count) {
        super(count);
    }

    public void setWeight(final double[] weight) {
        super.setWeight(weight);
    }

    public void setValue(final double value) {
        super.setValue(value);
    }

    public double[] getWeight() {
        return super.getWeight();
    }

    public double getValue() {
        return super.getValue();
    }
}
