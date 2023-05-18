package com.maddox.rts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.maddox.il2.engine.Config;

class ConsoleLogFile implements ConsoleOut {

    public void flush() {
        this.f.flush();
        // TODO: +++ Instant Log by SAS~Storebror +++
        if (!Config.cur.bInstantLog) {
            return;
        }
        this.f.close();
        try {
            this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(this.fileName, 0), true));
        } catch (IOException theIOException) {
        }
        // TODO: --- Instant Log by SAS~Storebror ---
    }

    public void println(String s) {
        this.f.println(s);
        // TODO: +++ Instant Log by SAS~Storebror +++
        if (Config.cur.bInstantLog) this.flush();
        // TODO: --- Instant Log by SAS~Storebror ---
    }

    public void type(String s) {
        if (!this.isPiped && Config.cur != null && Config.cur.bPipedLog && this.fileName != null && this.fileName.length() > 0) {
            if (Time.current() > this.lastPipeAttempt + PIPE_ATTEMPT_DELAY) {
                if (this.pipeAttemptsRemain-- > 0) {
                    this.lastPipeAttempt = Time.current();
                    this.connectToPipe(this.fileName);
                    if (this.isPiped) {
                        Calendar calendar = Calendar.getInstance();
                        this.f.println("[" + this.longDate.format(calendar.getTime()) + SWITCH_TO_PIPE_LOG);
                    }
                }
            }
        }
        if (Console.bTypeTimeInLogFile) {
            // TODO: +++ Additional Log Settings by SAS~Storebror +++
            this.checkInit();
            // TODO: --- Additional Log Settings by SAS~Storebror ---
            Calendar calendar = Calendar.getInstance();
            this.f.print("[" + this.shortDate.format(calendar.getTime()) + "]\t");
            // TODO: +++ Additional Log Settings by SAS~Storebror +++
            if (Config.cur.bLogTicks) {
                this.f.print("dT:" + padNumber(Time.tickCounter() - this.lastTick, 5) + "\t");
                this.lastTick = Time.tickCounter();
            }
            // TODO: --- Additional Log Settings by SAS~Storebror ---
        }
        this.f.print(s);
        // TODO: +++ Instant Log by SAS~Storebror +++
        if (Config.cur.bInstantLog) this.flush();
        if (this.isPiped) this.f.flush();
        // TODO: --- Instant Log by SAS~Storebror ---
    }

    // TODO: +++ Additional Log Settings by SAS~Storebror +++
    private String padNumber(long number, int len) {
        String retVal = String.valueOf(number);
        int retValLen = retVal.length();
        if (retValLen < len)
            retVal = new String(new char[len-retValLen]).replace('\0', ' ') + retVal;
        return retVal;
    }
    // TODO: --- Additional Log Settings by SAS~Storebror ---

    public void close() {
        Calendar calendar = Calendar.getInstance();
        this.f.println();
        this.f.println("[" + this.longDate.format(calendar.getTime()) + END_LOG);
        this.f.close();
    }

    public ConsoleLogFile(String s) throws FileNotFoundException {
        // TODO: +++ Instant Log by SAS~Storebror +++
        this.fileName = s;
        this.lastPipeAttempt = -9999L;
        this.pipeAttemptsRemain = PIPE_ATTEMPTS;
        Calendar calendar = Calendar.getInstance();
        this.longDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        // TODO: --- Instant Log by SAS~Storebror ---

        //this.connectToPipe(s);
        this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));

        // TODO: +++ Additional Log Settings by SAS~Storebror +++
//        this.shortDate = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        // TODO: --- Additional Log Settings by SAS~Storebror ---
        this.f.println();
        this.f.println("[" + this.longDate.format(calendar.getTime()) + BEGIN_LOG);
        this.f.println();
    }

    private void connectToPipe(String s) {
        try {
            if (this.f != null) {
                this.f.flush();
                this.f.close();
                this.f = null;
            }
            this.f = new PrintWriter(new FileOutputStream(new File(PIPE_URL)));
            this.f.print(HomePath.toFileSystemName(s, 0) + PIPE_FLUSH_TIMEOUT + Config.cur.iLogFlushTimeout); // \u0000 = Separator, Flush Timeout = 1000ms default
//            this.f.print(HomePath.toFileSystemName(s, 0) + PIPE_FLUSH_TIMEOUT_1000); // \u0000 = Separator, Flush Timeout = 1000ms default
            this.f.flush();
            this.isPiped = true;
        } catch (FileNotFoundException fnfe1) {
            this.isPiped = false;
            this.f = null;
        }
        if (this.f == null) {
            try {
                this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
            } catch (FileNotFoundException fnfe2) {
                fnfe2.printStackTrace();
                //this.isPiped = false;
                System.exit(-1);
            }
        }
    }

    // TODO: +++ Additional Log Settings by SAS~Storebror +++
    public void checkInit() {
        if (this.shortDate != null) return;
        String shortDateFormat = "HH:mm:ss";
        if (Config.cur.bLogDate) shortDateFormat = "yyyy-MM-dd " + shortDateFormat;
        if (Config.cur.bLogMilliseconds) shortDateFormat = shortDateFormat + ".SSS";
        this.shortDate = new SimpleDateFormat(shortDateFormat);
        if (Config.cur.bLogTicks) {
            this.lastTick = Time.tickCounter();
        }
    }
    // TODO: --- Additional Log Settings by SAS~Storebror ---

    private PrintWriter f;
    private DateFormat  longDate;
    private DateFormat  shortDate;
    // TODO: +++ Instant Log by SAS~Storebror +++
    private String      fileName;
    // TODO: --- Instant Log by SAS~Storebror ---
    // TODO: +++ Additional Log Settings by SAS~Storebror +++
    private long        lastTick;
    private long        lastPipeAttempt;
    private int         pipeAttemptsRemain;
    private boolean     isPiped = false;
    private static final int PIPE_ATTEMPTS = 60;
    private static final long PIPE_ATTEMPT_DELAY = 1000;
    private static final String PIPE_URL = "\\\\.\\pipe\\SAS_PIPE_LOGGER";
    private static final String PIPE_FLUSH_TIMEOUT = "\u0000"; // \u0000 = Separator
//    private static final String PIPE_FLUSH_TIMEOUT_1000 = "\u00001000"; // \u0000 = Separator, Flush Timeout = 1000ms
    private static final String BEGIN_LOG = "] ------------ BEGIN log session -------------";
    private static final String END_LOG = "] -------------- END log session -------------";
    private static final String SWITCH_TO_PIPE_LOG = "] ----------- Switched to pipe mode ----------";
    // TODO: --- Additional Log Settings by SAS~Storebror ---
}
