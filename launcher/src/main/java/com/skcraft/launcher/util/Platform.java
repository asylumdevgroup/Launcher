/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.util;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Indicates the platform.
 */
public enum Platform {
    @XmlEnumValue("windows") WINDOWS,
    @XmlEnumValue("windows_arm64") WINDOWS_ARM64,
    @XmlEnumValue("mac_os_x") MAC_OS_X,
    @XmlEnumValue("mac_os_x_arm64") MAC_OS_X_ARM64,
    @XmlEnumValue("linux") LINUX,
    @XmlEnumValue("linux_arm64") LINUX_ARM64,
    @XmlEnumValue("solaris") SOLARIS,
    @XmlEnumValue("unknown") UNKNOWN
}