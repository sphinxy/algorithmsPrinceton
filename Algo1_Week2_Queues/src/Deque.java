import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item>
{
    private Node first = null;
    private Node last = null;
    private int size = 0;
    // construct an empty deque
    public Deque()
    {
        size = 0;
    }
    // is the deque empty?
    public boolean isEmpty()
    {
        return size == 0;
    }
    // return the number of items on the deque
    public int size()
    {
        return size;
    }
    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
        {
            throw new NullPointerException();
        }

        size++;
        Node newNode = new Node();
        newNode.value = item;

        newNode.next = first;
        newNode.prev = null;

        if (first != null) {
            first.prev = newNode;
        }

        first = newNode;

        if (last == null) {
            last = first;
        }

    }
    // add the item to the end
    public void addLast(Item item)
    {
        if (item == null)
        {
            throw new NullPointerException();
        }

        size++;
        Node newNode = new Node();
        newNode.value = item;

        newNode.prev = last;
        newNode.next = null;

        if (last != null)
        {
            last.next = newNode;
        }

        last = newNode;

        if (first == null) {
            first = last;
        }
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (first == null)
        {
            throw new NoSuchElementException();
        }
        Item item = first.value;
        first.value = null;
        first = first.next;
        if (first != null)
        {
            first.prev = null;
        }
        size--;
        if (size == 0)
        {
            last = null;
        }

        return item;
    }
    // remove and return the item from the end
    public Item removeLast()
    {
        if (last == null)
        {
            throw new NoSuchElementException();
        }
        Item item = last.value;
        last = last.prev;
        if (last != null)
        {
            last.next = null;
        }
        size--;
        if (size == 0)
        {
            first = null;
        }

         return item;
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator()
    {
        return new ItemIterator();
    }
    // unit testing
    public static void main(String[] args)
    {
//        System.out.println("Deque runs");
//        Deque<Integer> deque = new Deque<>();
//        System.out.println("Deque size = "+ deque.size);
//        deque.addFirst(0);
//        deque.size();
//        System.out.println(deque.removeFirst());
//        deque.isEmpty();
//        deque.addLast(4);
//        System.out.println(deque.removeFirst());
//        deque.addFirst(6);
//        deque.size();
//        System.out.println(deque.removeLast());



//        deque.addFirst(2);
//        deque.addFirst(1);
//        deque.addLast(3);
//        deque.addLast(4);
//        deque.addFirst(0);
//        deque.addLast(5);
//        System.out.println("Deque size = "+ deque.size);
//        for (int item : deque)
//        {
//            System.out.println(item);
//        }
//        // System.out.println("Remove first "+deque.removeFirst());
//        System.out.println("Remove first "+deque.removeLast());
//        System.out.println("Remove last "+deque.removeLast());
//        System.out.println("Deque size after 2 remove = "+ deque.size);
//        for (int item : deque)
//        {
//            System.out.println(item);
//        }
//        System.out.println("Remove first "+deque.removeFirst());
//        System.out.println("Remove first "+deque.removeFirst());
//        System.out.println("Remove first "+deque.removeFirst());
//        System.out.println("Remove first "+deque.removeFirst());
//        System.out.println("Deque size after 4 remove = "+ deque.size);
//        for (int item : deque)
//        {
//            System.out.println(item);
//        }

    }

    private class ItemIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }

            Item item = current.value;
            current = current.next;
            return item;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Item value = null;
        private Node next = null;
        private Node prev = null;
    }
}