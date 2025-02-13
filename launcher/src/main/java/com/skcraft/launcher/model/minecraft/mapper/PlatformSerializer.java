/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.model.minecraft.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skcraft.launcher.util.Platform;

import java.io.IOException;

public class PlatformSerializer extends JsonSerializer<Platform> {

    @Override
    public void serialize(Platform platform, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        switch (platform) {
            case WINDOWS:
                jsonGenerator.writeString("windows");
                break;
            case WINDOWS_ARM64:
                jsonGenerator.writeString("windows-arm64");
                break;
            case MAC_OS_X:
                jsonGenerator.writeString("osx");
                break;
            case MAC_OS_X_ARM64:
                jsonGenerator.writeString("osx-arm64");
                break;
            case LINUX:
                jsonGenerator.writeString("linux");
                break;
            case LINUX_ARM64:
                jsonGenerator.writeString("linux-arm64");
                break;
            case UNKNOWN:
                jsonGenerator.writeNull();
                break;
        }
    }

}
