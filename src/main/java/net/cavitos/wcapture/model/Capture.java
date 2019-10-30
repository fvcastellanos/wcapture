package net.cavitos.wcapture.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Capture {

    private final String captureId;
    private final String storedPath;

}
