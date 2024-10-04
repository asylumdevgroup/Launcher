package com.skcraft.launcher.util;

import com.skcraft.launcher.Launcher;

import java.io.File;

public class BundledJava {

    // JVM Path:
    // $rootPath/runtime/{component}/{osarch}/
    public static File getJavaDir(Launcher l, String component) throws Exception {
        return new File(l.getBaseDir(), "runtime/" + component + "/" + Environment.getInstance().getMojangOs());
    }

}
