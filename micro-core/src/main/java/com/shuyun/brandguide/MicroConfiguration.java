package com.shuyun.brandguide;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shuyun.brandguide.props.LilyPosProperties;
import com.shuyun.motor.dropwizard.MotorConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Component:
 * Description:
 * Date: 15/9/28
 *
 * @author yue.zhang
 */
public class MicroConfiguration extends MotorConfiguration {

    @Valid
    @NotNull
    private LilyPosProperties lilyPosProperties = new LilyPosProperties();

    @JsonProperty("pos")
    public LilyPosProperties getLilyPosProperties() {
        return lilyPosProperties;
    }

    @JsonProperty("pos")
    public void setLilyPosProperties(LilyPosProperties lilyPosProperties) {
        this.lilyPosProperties = lilyPosProperties;
    }
}
