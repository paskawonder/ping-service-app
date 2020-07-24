package com.docler.ping.data;

import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DataService {

    private final Map<String, Map<Operation, ExecutionResultEntity>> data;

    public DataService() {
        this.data = new ConcurrentHashMap<>();
    }

    public void persist(final String id, final Operation operation, final ExecutionResultEntity result) {
        data.computeIfAbsent(id, k -> new ConcurrentHashMap<>());
        data.get(id).put(operation, result);
    }

    @SuppressWarnings("java:S1640")
    public Map<Operation, ExecutionResultEntity> get(final String id) {
        return new HashMap<>(data.getOrDefault(id, Collections.emptyMap()));
    }

}
