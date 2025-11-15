package hotel.repository;

import java.util.List;

public interface MyMapInterface<K, V> {
    void put(K key, V value);
    V get(K key);
    List<K> keys();
    void remove(K key);
}
