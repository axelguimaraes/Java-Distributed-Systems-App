package edu.estg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class ArrayListSync<T> extends ArrayList<T> {

    public ArrayListSync() {
        super();
    }

    public ArrayListSync(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return super.contains(o);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }

    @Override
    public synchronized Object[] toArray() {
        return super.toArray();
    }

    @Override
    public synchronized T get(int index) {
        return super.get(index);
    }

    @Override
    public synchronized T set(int index, T element) {
        return super.set(index, element);
    }

    @Override
    public synchronized boolean add(T t) {
        return super.add(t);
    }

    @Override
    public synchronized void add(int index, T element) {
        super.add(index, element);
    }

    @Override
    public synchronized T remove(int index) {
        return super.remove(index);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public synchronized Stream<T> stream() {
        return super.stream();
    }
}
