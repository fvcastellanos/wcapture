package net.cavitos.wcapture.client.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends BaseApiModel {

    private String error;
}
