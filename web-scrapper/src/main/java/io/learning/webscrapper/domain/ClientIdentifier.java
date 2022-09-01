package io.learning.webscrapper.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientIdentifier {
    private String hostname;
    private String ipAddress;
}
