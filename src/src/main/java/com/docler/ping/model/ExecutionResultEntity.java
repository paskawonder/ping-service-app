package com.docler.ping.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public final class ExecutionResultEntity {

    private final long timestamp;

    private final String payload;

}
