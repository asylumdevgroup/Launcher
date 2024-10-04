package com.skcraft.launcher.model.java;

import lombok.Data;

@Data
public class JavaManifest {
    private JavaAvailability availability;
    private JavaManifestFile manifest;
    private JavaVersion version;
}
