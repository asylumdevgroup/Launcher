package com.skcraft.launcher.model.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JavaFileDownload {
    private String url;

    private int size;

    @JsonProperty("sha1")
    private String hash;
}
