package com.docler.ping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public final class Report {

    private final String host;

    private final String icmpPing;

    private final String tcpPing;

    private final String trace;

    @Override
    public String toString() {
        return "{" +
                       "\"host\":\"" + host + "\"," +
                       "\"icmp_ping\":\""+ icmpPing + "\"," +
                       "\"tcp_ping\":\"" + tcpPing + "\"," +
                       "\"trace\":\"" + trace + "\"" +
               "}";
    }

}
