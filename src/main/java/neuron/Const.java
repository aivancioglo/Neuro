package neuron;

public interface Const {
    double e = Math.E;

    static double sigmoid(final double x) {
        return 1 / (1 + Math.pow(e, -x));
    }

    static double sigmoidDx(final double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }

    static double delta(final double err, final double x) {
        return err * sigmoidDx(x);
    }

    static double percent(final double err) {
        return Math.round(100 * Math.pow((1 - err), 2));
    }
}
