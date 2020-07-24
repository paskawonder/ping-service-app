package com.docler.ping.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class Task {

    private final String id;
    
    private final String uri;

    private final Operation operation;

    private final long period;

    @JsonCreator
    public Task(@JsonProperty("id") final String id,
                @JsonProperty("uri") final String uri,
                @JsonProperty("operation") final Operation operation,
                @JsonProperty("period") final long period) {
        this.id = id;
        this.uri = uri;
        this.operation = operation;
        this.period = period;
    }

}
