package io.learning.webscrapper.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AvroHttpRequest {
    private long requestTime;
    private ClientIdentifier clientIdentifier;
    private List<String> employeeNames;
    private Active active;
}
