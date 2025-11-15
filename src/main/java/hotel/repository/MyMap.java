package hotel.repository;

import java.util.ArrayList;
import java.util.List;

public class MyMap<K, V> implements MyMapInterface<K, V> {

    private final List<K> keys = new ArrayList<>();
    private final List<V> values = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        int index = keys.indexOf(key);
        if (index >= 0) {
            values.set(index, value);
        } else {
            keys.add(key);
            values.add(value);
        }
    }

    @Override
    public V get(K key) {
        int index = keys.indexOf(key);
        if (index >= 0) return values.get(index);
        return null;
    }

    @Override
    public List<K> keys() {
        return new ArrayList<>(keys);
    }

    @Override
    public void remove(K key) {
        int index = keys.indexOf(key);
        if (index >= 0) {
            keys.remove(index);
            values.remove(index);
        }
    }
}
