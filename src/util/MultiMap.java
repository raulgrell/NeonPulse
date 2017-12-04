package util;

import java.util.*;

public class MultiMap<K, V> {
    private Map<K, ArrayList<V>> map = new HashMap<>();

    public void put(K key, V value) {
        if (map.get(key) == null)
            map.put(key, new ArrayList<>());
        map.get(key).add(value);
    }

    public ArrayList<V> get(Object key) {
        return map.get(key);
    }

    public ArrayList<V> remove(Object key) {
        return map.remove(key);
    }

    public boolean remove(K key, V value) {
        if (map.get(key) != null)
            return map.get(key).remove(value);

        return false;
    }

    public int size() {
        int size = 0;
        for (ArrayList<V> value : map.values()) size += value.size();
        return size;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

    public boolean replace(K key, V old_val, V new_val) {
        if (map.get(key) != null) {
            if (map.get(key).remove(old_val))
                return map.get(key).add(new_val);
        }
        return false;
    }
}