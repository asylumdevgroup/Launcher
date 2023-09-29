package com.skcraft.launcher.launch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
    @JsonProperty("logShow")
    private String logShow;

    public String getLogShow() {
        return logShow;
    }
}