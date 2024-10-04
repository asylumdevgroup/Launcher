/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.launch;

import com.google.common.base.Function;
import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.dialog.ProcessConsoleFrame;
import com.skcraft.launcher.swing.MessageLog;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Handles post-process creation during launch.
 */
@Log
public class LaunchProcessHandler implements Function<Process, ProcessConsoleFrame> {

    private static final int CONSOLE_NUM_LINES = 10000;

    private final Launcher launcher;
    private ProcessConsoleFrame consoleFrame;

    public LaunchProcessHandler(@NonNull Launcher launcher) {
        this.launcher = launcher;
        this.consoleFrame = new ProcessConsoleFrame(CONSOLE_NUM_LINES, true);
    }

    @Override
    public ProcessConsoleFrame apply(final Process process) {
        log.info("Watching process " + process);
    
        MessageLog messageLog = consoleFrame.getMessageLog();
    
        // Create piped streams to combine output and error streams
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
    
        // Start a thread to read from the process's output and error streams
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
    
                String line;
                // Read standard output
                while ((line = reader.readLine()) != null) {
                    messageLog.log(line, messageLog.asHighlighted());
                    pipedOutputStream.write((line + "\n").getBytes());
                }
    
                // Read error output
                while ((line = errorReader.readLine()) != null) {
                    messageLog.log(line, messageLog.asError());
                    pipedOutputStream.write((line + "\n").getBytes());
                }
            } catch (IOException e) {
                log.warning("Error reading process output: " + e.getMessage());
            } finally {
                try {
                    pipedOutputStream.close();
                } catch (IOException e) {
                    log.warning("Error closing piped output stream: " + e.getMessage());
                }
            }
        }).start();
    
        // Now consume the combined output
        messageLog.consume(pipedInputStream);
    
        // Continue with your existing logic...
        return consoleFrame;
    }

}
