package neuron.components;

public abstract class Neuron {
    private double[] weight;
    private double value;

    public Neuron() {
    }

    public Neuron(final int count) {
        weight = new double[count];
    }

    public double[] getWeight() {
        return weight;
    }

    public double getValue() {
        return value;
    }

    public void setWeight(final double[] weight) {
        this.weight = weight;
    }

    public void setValue(double value) {
        this.value = value;
    }
}