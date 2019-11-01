package net.cavitos.wcapture.client.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@EqualsAndHashCode
public abstract class BaseApiModel {

    protected String requestId;

    @Override
    public String toString() {

        return ReflectionToStringBuilder
                .toString(this, ToStringStyle.JSON_STYLE);
    }
}
