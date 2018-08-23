package net.cavitos.wcapture.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class CaptureHistory {

    private final long id;
    private final Instant createdDate;
    private final Instant modifiedDate;
    private final String url;
    private final String filename;

}
