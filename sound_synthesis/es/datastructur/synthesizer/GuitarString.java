package es.datastructur.synthesizer;

//Note: This file will not compile until you complete task 1 (BoundedQueue).
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer((int) java.lang.Math.round(SR / frequency));
        for (int i = 0; i < buffer.capacity(); i += 1) {
            buffer.enqueue(0.0);
        }
    }

    /* Ensures every number added to buffer is unique. */
    private double pluckHelper(double r, double[] vals, int index) {
        for (int i = 0; i < index; i += 1) {
            if (r == vals[i]) {
                double num = java.lang.Math.random() - 0.5;
                return pluckHelper(num, vals, index);
            }
        }
        vals[index] = r;
        return r;
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        double[] randomVals = new double[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i += 1) {
            randomVals[i] = buffer.dequeue();
        }
        for (int k = 0; k < buffer.capacity(); k += 1) {
            double r = pluckHelper(java.lang.Math.random() - 0.5, randomVals, k);
            buffer.enqueue(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double newDouble = (buffer.dequeue() + buffer.peek()) * 0.5 * DECAY;
        buffer.enqueue(newDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
