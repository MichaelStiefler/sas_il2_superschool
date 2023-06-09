package com.sas1946.fac.tools.dservercontroller;

import java.util.Timer;
import java.util.TimerTask;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

public class TickDelayWatcher {

    private class TickDelayTimer extends TimerTask {
        @Override
        public void run() {
            TickDelayWatcher.this.update();
        }
    }

    private static final TickDelayWatcher instance;
    private Runnable                      tickDelayUpdateCallback;
    private int                           timeout;
    private float                         tickDelay;
    private WinDef.HWND                   hwndTickDelay;
    private boolean                       keepRunning;
    private boolean                       isRunning;
    private Timer                         timer;

    private TickDelayWatcher() {
    }

    public static TickDelayWatcher getInstance() {
        return instance;
    }

    public void setTickDelayUpdateCallback(Runnable tickDelayUpdateCallback) {
        this.tickDelayUpdateCallback = tickDelayUpdateCallback;
        this.tickDelayUpdateCallback.run();
    }

    public float getTickDelay() {
        return this.tickDelay == Float.NEGATIVE_INFINITY ? 0.0F : this.tickDelay;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        this.stop();
        this.start();
    }

    public void stop() {
        this.keepRunning = false;
        timer.cancel();
        this.isRunning = false;
    }

    public void start() {
        this.keepRunning = true;
        if (!this.isRunning) {
            timer.schedule(new TickDelayTimer(), 100, this.timeout);
        }
    }

    private void update() {
        TickDelayWatcher.this.isRunning = true;
        if (!TickDelayWatcher.this.keepRunning) {
            TickDelayWatcher.this.isRunning = false;
            return;
        }
        if (TickDelayWatcher.this.hwndTickDelay != null) {
            if (!User32.INSTANCE.IsWindow(TickDelayWatcher.this.hwndTickDelay)) {
                TickDelayWatcher.this.hwndTickDelay = null;
            } else {
                int lenCaption = User32.INSTANCE.GetWindowTextLength(TickDelayWatcher.this.hwndTickDelay);
                if (lenCaption > 0) {
                    lenCaption++;
                    final char[] captionBuffer = new char[lenCaption];
                    if (User32.INSTANCE.GetWindowText(TickDelayWatcher.this.hwndTickDelay, captionBuffer, lenCaption) == 0) {
                        TickDelayWatcher.this.hwndTickDelay = null;
                    } else {
                        final String captionText = new String(captionBuffer).trim();
                        if ((captionText != null) && (captionText.trim().length() > 0)) {
                            try {
                                final float newTickDelay = Float.parseFloat(captionText);
                                if ((TickDelayWatcher.this.tickDelayUpdateCallback != null) && (newTickDelay != TickDelayWatcher.this.tickDelay)) {
                                    TickDelayWatcher.this.tickDelay = newTickDelay;
                                    TickDelayWatcher.this.tickDelayUpdateCallback.run();
                                }
                            } catch (final NumberFormatException e) {
                            }
                        }
                    }
                }
            }
        }
        if (!TickDelayWatcher.this.keepRunning) {
            TickDelayWatcher.this.isRunning = false;
            return;
        }
        if (TickDelayWatcher.this.hwndTickDelay == null) {

            final WinDef.HWND dserverWindow = User32.INSTANCE.FindWindow(null, "Il-2 Dedicated Server");
            if (dserverWindow != null) {

                User32.INSTANCE.EnumChildWindows(dserverWindow, new User32.WNDENUMPROC() {
                    boolean useNext = false;

                    @Override
                    public boolean callback(WinDef.HWND hWnd, Pointer userData) {
                        if (this.useNext) {
                            TickDelayWatcher.this.hwndTickDelay = hWnd;
                            return false;
                        }
                        int lenCaption = User32.INSTANCE.GetWindowTextLength(hWnd);
                        if (lenCaption > 0) {
                            lenCaption++;
                            final char[] childBuffer = new char[lenCaption];
                            User32.INSTANCE.GetWindowText(hWnd, childBuffer, lenCaption);
                            final String childText = new String(childBuffer).trim();
                            if (childText.equals("Tick delay:")) {
                                this.useNext = true;
                            }
                        }
                        return true;
                    }
                }, null);
            }
        }
    }

    static {
        instance = new TickDelayWatcher();
        instance.timeout = 1000;
        instance.tickDelayUpdateCallback = null;
        instance.tickDelay = Float.NEGATIVE_INFINITY;
        instance.hwndTickDelay = null;
        instance.keepRunning = false;
        instance.isRunning = false;
        instance.timer = new Timer();
        instance.start();
    }
}
