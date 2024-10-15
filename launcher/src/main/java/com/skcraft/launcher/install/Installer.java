/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.install;

import com.skcraft.concurrency.ProgressObservable;
import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.util.SharedLocale;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import static com.skcraft.launcher.LauncherUtils.checkInterrupted;
import static com.skcraft.launcher.util.SharedLocale.tr;

@Log
public class Installer implements ProgressObservable {

    @Getter
    private final File tempDir;
    private final HttpDownloader downloader;

    private final TaskQueue mainQueue = new TaskQueue();
    private final TaskQueue lateQueue = new TaskQueue();

    private transient TaskQueue activeQueue;

    public Installer(@NonNull File tempDir) {
        this.tempDir = tempDir;
        this.downloader = new HttpDownloader(tempDir);
    }

    public void queue(@NonNull InstallTask runnable) {
        mainQueue.queue(runnable);
    }

    public void queueLate(@NonNull InstallTask runnable) {
        lateQueue.queue(runnable);
    }

    public void download() throws IOException, InterruptedException {
        downloader.execute();
    }

    public void execute(Launcher launcher) throws Exception {
        activeQueue = mainQueue;
        mainQueue.execute(launcher);
        activeQueue = null;
    }

    public void executeLate(Launcher launcher) throws Exception {
        activeQueue = lateQueue;
        lateQueue.execute(launcher);
        activeQueue = null;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    @Override
    public double getProgress() {
        if (activeQueue == null) return 0.0;
        return activeQueue.getFinished() / (double) activeQueue.getCount();
    }

    @Override
    public String getStatus() {
        if (activeQueue != null && activeQueue.getRunning() != null) {
            InstallTask running = activeQueue.getRunning();
            String status = running.getStatus();
            if (status == null) {
                status = running.toString();
            }
            return tr("installer.executing", activeQueue.getCount() - activeQueue.getFinished()) + "\n" + status;
        } else {
            return SharedLocale.tr("installer.installing");
        }
    }

    public static class TaskQueue {
        private final ConcurrentLinkedQueue<InstallTask> queue = new ConcurrentLinkedQueue<>();
        private final AtomicInteger count = new AtomicInteger(0);
        private final AtomicInteger finished = new AtomicInteger(0);
        private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        @Getter
        private volatile InstallTask running;

        public void queue(@NonNull InstallTask runnable) {
            queue.add(runnable);
            count.incrementAndGet();
        }

        public void execute(Launcher launcher) throws Exception {
            try {
                for (InstallTask runnable : queue) {
                    checkInterrupted();
                    running = runnable;
                    executor.submit(() -> {
                        try {
                            runnable.execute(launcher);
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "Failed to execute task: " + runnable, e);
                        }
                        finished.incrementAndGet();
                    });
                }
                executor.shutdown();
                while (!executor.isTerminated()) {
                    checkInterrupted();
                    Thread.sleep(100);
                }
            } finally {
                running = null;
            }
        }

        public int getCount() {
            return count.get();
        }

        public int getFinished() {
            return finished.get();
        }

    }
}