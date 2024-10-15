/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.model.minecraft.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.skcraft.launcher.util.Platform;

import java.io.IOException;

public class PlatformDeserializer extends JsonDeserializer<Platform> {

    @Override
    public Platform deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String text = jsonParser.getText();
        if (text.equalsIgnoreCase("windows")) {
            return Platform.WINDOWS;
        } else if (text.equalsIgnoreCase("windows-arm64")) {
            return Platform.WINDOWS_ARM64;
        } else if (text.equalsIgnoreCase("linux")) {
            return Platform.LINUX;
        } else if (text.equalsIgnoreCase("linux-arm64")) {
            return Platform.LINUX_ARM64;
        } else if (text.equalsIgnoreCase("solaris")) {
            return Platform.SOLARIS;
        } else if (text.equalsIgnoreCase("osx")) {
            return Platform.MAC_OS_X;
        } else if (text.equalsIgnoreCase("osx-arm64")) {
            return Platform.MAC_OS_X_ARM64;        
        } else {
            throw new IOException("Unknown platform: " + text);
        }
    }

}
