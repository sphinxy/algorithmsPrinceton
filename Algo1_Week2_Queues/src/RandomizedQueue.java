
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] data;
    private int size = 0;
    // construct an empty randomized queue
    public RandomizedQueue()
    {
        data = new Object[1];
    }
    // is the queue empty?
    public boolean isEmpty()
    {
        return size == 0;
    }
    // return the number of items on the queue
    public int size()
    {
        return size;
    }
    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
        {
            throw new NullPointerException();
        }
        if (size == data.length) {
            resize(2 * size);
        }
        data[size++] = item;
    }

    private void resize(int capacity)
    {
        // System.out.println("Resize from "+ data.length + " to " +capacity);
        Object[] copy = new Object[capacity];
        for (int i = 0; i < size; i++)
        {
                copy[i] = data[i];
        }
        data = copy;
    }

    // remove and return a random item
    public Item dequeue()
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }

        int randomIndex = StdRandom.uniform(size);
        // System.out.println("randomIndex " + randomIndex + " for size "+size);
        Item item =  (Item) data[randomIndex];
        //data[randomIndex] = null;
        // shift data
        for (int i = randomIndex; i < size - 1; i++)
        {
            data[i] = data[i+1];
        }

        if (size > 0)
        {
            data[size-1] = 100;
        }

        size--;

        if (size > 0 && size == data.length/4) resize(data.length/2);

        return item;
    }
    // return (but do not remove) a random item
    public Item sample()
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }

        int randomIndex = StdRandom.uniform(size);

        return (Item) data[randomIndex];
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<Item> {
        private int current = 0;
        private int[] randomIterator = new int[size];

        public ItemIterator()
        {
            int iteratorIndex = 0;
            for (int i = 0; i < size; i++)
            {
                    randomIterator[iteratorIndex] = i;
                    iteratorIndex++;
            }
            StdRandom.shuffle(randomIterator);
        }
        @Override
        public boolean hasNext() {
            return current != size;
        }

        @Override
        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            Item item = (Item) data[randomIterator[current]];
            current++;
            return item;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
    // unit testing
    public static void main(String[] args)
    {
        System.out.println("RandomizedQueue runs");
        RandomizedQueue<Integer> randQ = new RandomizedQueue<>();
        System.out.println("randQ size = "+ randQ.size);

        randQ.enqueue(33);
        randQ.enqueue(12);
        randQ.enqueue(32);
        randQ.dequeue();
        randQ.dequeue();
//        for (int i = 0; i < 10; i++)
//        {
//            randQ.enqueue(i);
//        }
//
//        for (int i = 0; i < 100; i++)
//        {
//            System.out.println("randQ sample = "+ randQ.sample());
//        }
//
//        for (int i = 0; i < 5; i++)
//        {
//            System.out.println("randQ dequeue = "+ randQ.dequeue());
//        }
//
//        for (int i = 0; i < 100; i++)
//        {
//            System.out.println("randQ sample = "+ randQ.sample());
//        }
//
//        System.out.println("randQ size = "+ randQ.size);
//        for (int item : randQ) {
//            System.out.println(item);
//        }


    }
}