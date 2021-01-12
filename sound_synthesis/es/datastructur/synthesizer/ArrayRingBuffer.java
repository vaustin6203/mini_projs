package es.datastructur.synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T>  implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        fillCount = 0;
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
    }

    /**
     * Resets first or last if equal to capacity.
     */
    private boolean resetIndex(int index) {
        return index == rb.length;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (fillCount == rb.length) {
            throw new RuntimeException("Ring Buffer overflow");
        }
        rb[last] = x;
        fillCount += 1;
        last += 1;
        if (resetIndex(last)) {
            last = 0;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        T oldest = rb[first];
        rb[first] = null;
        fillCount -= 1;
        first += 1;
        if (resetIndex(first)) {
            first = 0;
        }
        return oldest;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        return rb[first];
    }

    /**
     * Returns the capacity an ArrayRingBuffer instance has.
     */
    @Override
    public int capacity() {
        return rb.length;
    }

    /**
     * Returns the number of items in an ArrayRingBuffer insatnce.
     */
    @Override
    public int fillCount() {
        return fillCount;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    @Override
    public boolean equals(Object o) {
        ArrayRingBuffer other = (ArrayRingBuffer<T>) o;
        if (this.fillCount != other.fillCount) {
            return false;
        }
        int thisIndex = first;
        int otherIndex = other.first;
        for (int i = 0; i < fillCount; i += 1) {
            if (other.rb[otherIndex] != rb[thisIndex]) {
                return false;
            }
            thisIndex += 1;
            otherIndex += 1;
            if (resetIndex(thisIndex)) {
                thisIndex = 0;
            }
            if (resetIndex(otherIndex)) {
                otherIndex = 0;
            }
        }
        return true;
    }

    private class ArrayRingBufferIterator implements Iterator<T> {
        private int position;

        ArrayRingBufferIterator() {
            position = 0;
        }

        public boolean hasNext() {
            return position < capacity();
        }

        public T next() {
            T returnItem = rb[position];
            position += 1;
            return returnItem;
        }
    }

}
