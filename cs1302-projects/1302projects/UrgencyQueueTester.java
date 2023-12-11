package cs1302.p3;

import cs1302.gen.UrgencyQueue;
import cs1302.gen.Node;
import cs1302.p3.BaseLinkedUrgencyQueue;
import cs1302.p3.LinkedUrgencyQueue;
import cs1302.p3.CustomLinkedUrgencyQueue;
import cs1302.oracle.OracleLinkedUrgencyQueue;
import cs1302.oracle.OracleCustomLinkedUrgencyQueue;
import java.util.function.Consumer;
import java.util.List;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.Comparator;

public class UrgencyQueueTester {

    public static void main(String[] args) {

        Comparator<Integer> greater = (Integer m, Integer n) -> {
            return m - n;
        };

        // new queue
        UrgencyQueue<Integer> queue = new CustomLinkedUrgencyQueue<Integer>(greater);

        System.out.println("empty queue size: " + queue.size());

        // enqueue some items
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(3);
        queue.enqueue(0);

        System.out.println("Urgency Queue:  " + queue.toString());
        System.out.println("queue size: " + queue.size());
        System.out.println("should be 4");

        System.out.println("peek: " + queue.peek());
        System.out.println("should be 3");

        Integer dequeued1 = queue.dequeue();

        System.out.println("Urgency Queue:  " + queue.toString());
        System.out.println("queue size: " + queue.size());
        System.out.println("should be 3");
        System.out.println("Dequeued: " + dequeued1);
        System.out.println("peek: " + queue.peek());
        System.out.println("should be 2");

        Consumer<Integer> doubleVal = (Integer integer) -> {
            System.out.println(integer + " in double val = " + integer.doubleValue());
        };

        queue.dequeue(doubleVal);

        System.out.println("peek: " + queue.peek());
        System.out.println("should be 1");

        // enqueue some items
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7);

        System.out.println("Urgency Queue:  " + queue.toString());
        System.out.println("queue size: " + queue.size());
        System.out.println("should be 6");

        queue.dequeueMany(3, doubleVal);

        System.out.println("Urgency Queue:  " + queue.toString());
        System.out.println("queue size: " + queue.size());
        System.out.println("should be 3");

        queue.clear();

        //System.out.println("Cleared Urgency Queue:  " + queue.toString());
        System.out.println("queue size: " + queue.size());
        System.out.println("should be 0");
        System.out.println("peek: " + queue.peek());
        System.out.println("should be null");

        // define some people
        Integer i = 11;
        Integer j = 13;
        Integer k = 17;
        ArrayList<Integer> prime = new ArrayList();
        prime.add(i);
        prime.add(j);
        prime.add(k);

        // enqueue some people
        queue.enqueueAll(prime);

        System.out.println("Urgency Queue:  " + queue.toString());
        System.out.println("queue size: " + queue.size());
        System.out.println("should be 3");

        IntFunction<Integer[]> generator = (int size) -> {
            return new Integer[size];
        };

        System.out.print("Array: ");
        Integer[] myArray = queue.toArray(generator);
        for (int x = 0; x < myArray.length; x++) {
            System.out.print(myArray[x] + " ");
        }
        System.out.println("");

        Predicate<Integer> greaterThan12 = (Integer integer) -> {
            if (integer > 12) return true;
            else return false;
        };

        UrgencyQueue<Integer> queue3 = queue.filter(greaterThan12);
        System.out.println("Urgency Queue:  " + queue3.toString());
        System.out.println("queue size: " + queue3.size());

        UrgencyQueue<Integer> queue2 = queue.dequeueMany(2);
        System.out.println("Urgency Queue:  " + queue2.toString());
        System.out.println("queue size: " + queue2.size());


    } // main

} // UrgencyQueueTester
