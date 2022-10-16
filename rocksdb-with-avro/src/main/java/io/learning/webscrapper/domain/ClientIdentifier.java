package io.learning.webscrapper.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClientIdentifier {
    private String hostname;
    private String ipAddress;
}
