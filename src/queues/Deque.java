/* *****************************************************************************
 *  Name: Deque
 *  Date: 05/07/20
 *  Description: A double-ended queue or deque (pronounced “deck”) is a
 *  generalization of a stack and a queue. Here a doubly linked list is implemented.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        // first.prev = null;
        last = null;
        // last.next = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null || last == null);
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // validate the value that is added
    private void validate(Item item) {
        if (item == null)
            throw new IllegalArgumentException("You need to specify an item");
    }

    // private method that deals with an empty deque
    private void addtoEmpty(Item item) {
        if (isEmpty()) {
            first = new Node<Item>();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
            n = 1;
        }
    }

    // add the item to the front
    public void addFirst(Item item) {
        validate(item);
        if (!isEmpty()) { // Need to make sure that the deque is never empty in test client (or else?)
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
            first.prev = null;
            oldfirst.prev = first;
            n++;
        }
        addtoEmpty(item);
    }

    // add the item to the back
    public void addLast(Item item) {
        validate(item);
        if (!isEmpty()) { // Need to make sure that the deque is never empty in test client (or else?)
            Node<Item> oldlast = last;
            last = new Node<Item>();
            last.item = item;
            last.prev = oldlast;
            last.next = null;
            oldlast.next = last;
            n++;
        }
        addtoEmpty(item);
    }

    // remove and return the item from the front
    public Item removeFirst() { // what if the deque is empty?
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first != null)
            first.prev = null;
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() { // what if the deque is empty?
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (last != null)
            last.next = null; // handle the second pointers
        n--;
        return item;
    }

    // return an iterator over items in order from front to back: from FIRST to LAST.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;
        // private Node<Item> currentLast;

        public DequeIterator() {
            current = first;
        }

        public boolean hasNext() {
            return (current != null);
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        // Not supported
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> d = new Deque<String>();

        d.addFirst("A");
        StdOut.println(d.removeFirst());

        /*while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            // StdOut.println(word);
            d.addFirst(word);
        }
        for (int i = 0; i < 8; i++)
            StdOut.println(d.removeLast());
        // d.removeFirst();
        StdOut.println("We have " + d.size() + " word in deque.");*/

        //     for (String nice : d)
        //         StdOut.print(nice + " ");
        //     StdOut.println();
    }
}
