package com.commuting.commutingapp.common.wrapper;

import com.commuting.commutingapp.common.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedHashMap<K, V> extends HashMap<K, V> {

    LinkedHashMap<K, LocalDateTime> tracker = new LinkedHashMap<>();

    long capacity = 1000;

    public LimitedHashMap(long capacity) {
        super();
        this.capacity = capacity;
    }

    public LimitedHashMap() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        tracker.put((K) key, DateTimeUtils.getTimeNow());
        return super.get(key);
    }

    @Override
    public V put(K key, V value) {
        tracker.put(key, DateTimeUtils.getTimeNow());
        if (tracker.size() > capacity) {
            removeMostUnusedItem();
        }
        return super.put(key, value);
    }

    void removeMostUnusedItem() {
        K key = tracker.entrySet().stream().sorted(Entry.comparingByValue()).findFirst().get().getKey();
        tracker.remove(key);
        remove(key);
    }
}
