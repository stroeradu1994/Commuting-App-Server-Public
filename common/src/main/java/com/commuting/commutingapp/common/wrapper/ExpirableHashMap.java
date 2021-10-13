package com.commuting.commutingapp.common.wrapper;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpirableHashMap<K, V> extends HashMap<K, V> {

    private static final Logger logger
            = LoggerFactory.getLogger(ExpirableHashMap.class);

    Map<K, LocalDateTime> tracker = new HashMap<>();

    long expireInMinutes = 30;
    String cache = "Generic";

    public ExpirableHashMap(String cache, long expireInMinutes) {
        super();
        this.cache = cache;
        this.expireInMinutes = expireInMinutes;
    }

    public ExpirableHashMap(String cache) {
        super();
        this.cache = cache;
    }

    @Override
    public V get(Object key) {
        V v = super.get(key);
        checkAndRemove();
        return v;
    }

    @Override
    public V put(K key, V value) {
        tracker.put(key, DateTimeUtils.getTimeNow());
        checkAndRemove();
        return super.put(key, value);
    }

    void checkAndRemove() {
        List<K> keysToRemove = tracker.entrySet().stream().filter((entry) -> {
            return entry.getValue().plusMinutes(expireInMinutes).isAfter(DateTimeUtils.getTimeNow());
        }).map(Entry::getKey).collect(Collectors.toList());

        keysToRemove.forEach(k -> {
            tracker.remove(k);
            remove(k);
            logger.info("Removed from cache: " + cache + " key: " + k);
        });
    }
}
