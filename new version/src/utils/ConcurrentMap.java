package utils;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConcurrentMap<K, V> {

    private Map<K, V> map = new HashMap<>();

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void replace(K key, V value) {
        map.replace(key, value);
    }

    public synchronized void delete(K key) {
        map.remove(key);
    }

    public synchronized Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public String toString() {
        return map.toString();
    }

}