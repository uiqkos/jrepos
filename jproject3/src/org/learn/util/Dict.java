package org.learn.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class Dict<Key, Value> extends HashMap<Key, Value> {
    @Getter
    private List<Key> keys;
    @Getter
    private List<Value> values;

    public Dict(List<Key> keys, List<Value> values) {
        this.keys = keys;
        this.values = values;

        IntStream
            .range(0, keys.size())
            .forEach(i -> put(keys.get(i), values.get(i)));
    }
}

