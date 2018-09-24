package net.cavitos.wcapture.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
public class CaptureHistoryResource {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private final Date createdDate;

    private final String url;

}
