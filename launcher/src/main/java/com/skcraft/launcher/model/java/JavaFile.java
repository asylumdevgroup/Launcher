package com.skcraft.launcher.model.java;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class JavaFile {
    @JsonIgnore
    private String path;

    private String type;
    private Downloads downloads;
    private String target; // Unused as I won't implement symlinks
    private boolean executable;
}