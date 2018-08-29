package net.cavitos.wcapture.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
@AllArgsConstructor
public class CaptureHistory {

    private final Long id;
    private final Instant createdDate;
    private final Instant modifiedDate;
    private final String url;
    private final String filename;

}
