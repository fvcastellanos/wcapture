package net.cavitos.wcapture.client.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CaptureResponse extends BaseApiModel {

    private String storedPath;
    private String targetUrl;
}
