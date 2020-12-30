/* *****************************************************************************
 *  Name: Randomized Queue
 *  Date: 06/07/20
 *  Description: Constructing a dynamic array that handles randomized queue.
 *  Compile: javac-algs4 RandomizedQueue.java
 *  Run: java-algs4 RandomizedQueue
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;
    private int k;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // private method to resize the array - from ArrayStack.java
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NoSuchElementException();
        if (n == a.length)
            resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int rand = StdRandom.uniform(n);

        // need to swap!!
        Item item = a[rand];
        a[rand] = a[n - 1];
        a[n - 1] = null;
        n--;

        // shrink size if necessary
        if (n > 0 && n == a.length / 4)
            resize(a.length / 2);
        return item;
    }

    // return a random item (but do not remove it): Restructure this
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        // int rand = StdRandom.uniform(n);
        // return a[rand];
        /*enqueue(dequeue());
        // return a[n - 1];
        n = n - 1;
        return a[n];*/

        int rand = StdRandom.uniform(k + 1);
        Item item = a[rand];
        a[rand] = a[k];
        a[k] = item;
        if (k >= 1)
            k--;
        return item;
    }


    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;

        public RandomizedQueueIterator() {
            i = 0;
            k = n - 1;
        }

        public boolean hasNext() {
            return i <= n - 1;
            // return n >= 1;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            // StdOut.println(n);
            // int rand = StdRandom.uniform(0, i + 1);
            // return a[i++];
            i++;
            // StdOut.println(i);
            return sample();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        /* RandomizedQueue<String> rq = new RandomizedQueue<String>();
        // while (!StdIn.isEmpty()) {
        //     String word = StdIn.readString();
        //     StdOut.println(word);
        //     rq.enqueue(word);
        // }

        for (int i = 0; i < args.length; i++)
            rq.enqueue(args[i]);

        for (int i = 0; i < args.length; i++) {
            StdOut.print(rq.dequeue() + " ");
        } */

        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        // for (int a : queue) {
        //     StdOut.print(a);
        // }
        // StdOut.println();
        // for (int b : queue) {
        //     StdOut.print(b);
        // }
        for (int a : queue) {
            for (int b : queue) // this must be independent of the first iteration
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
