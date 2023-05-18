package com.maddox.rts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.maddox.il2.engine.Config;

class ConsoleLogFile
    implements ConsoleOut
{

    public void flush()
    {
        f.flush();
    }

    public void println(String s)
    {
        f.println(s);
    }

    public void type(String s)
    {
        if (!this.isPiped && Config.cur != null && this.fileName != null && this.fileName.length() > 0) {
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
        if(Console.bTypeTimeInLogFile)
        {
            // TODO: +++ Additional Log Settings by SAS~Storebror +++
            this.checkInit();
            // TODO: --- Additional Log Settings by SAS~Storebror ---
            Calendar calendar = Calendar.getInstance();
            this.f.print("[" + this.shortDate.format(calendar.getTime()) + "]\t");
            // TODO: +++ Additional Log Settings by SAS~Storebror +++
            this.f.print("dT:" + padNumber(Time.tickCounter() - this.lastTick, 5) + "\t");
            this.lastTick = Time.tickCounter();
            // TODO: --- Additional Log Settings by SAS~Storebror ---
        }
        this.f.print(s);
        if (this.isPiped) this.f.flush();
        this.flush();
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

    public void close()
    {
        Calendar calendar = Calendar.getInstance();
        this.f.println();
        f.println("[" + longDate.format(calendar.getTime()) + END_LOG);
        f.close();
    }

    public ConsoleLogFile(String s)
        throws FileNotFoundException
    {
        this.fileName = s;
        this.lastPipeAttempt = -9999L;
        this.pipeAttemptsRemain = PIPE_ATTEMPTS;
        Calendar calendar = Calendar.getInstance();
        this.longDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
        this.f.println();
        this.f.println("[" + this.longDate.format(calendar.getTime()) + BEGIN_LOG);
        this.f.println();
    }

    // TODO: +++ Instant Log Enhanced by SAS~Storebror +++
    private void connectToPipe(String s) {
        try {
            if (this.f != null) {
                this.f.flush();
                this.f.close();
                this.f = null;
            }
            this.f = new PrintWriter(new FileOutputStream(new File(PIPE_URL)));
            this.f.print(HomePath.toFileSystemName(s, 0) + PIPE_FLUSH_TIMEOUT_1000);
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
                System.exit(-1);
            }
        }
    }

    public void checkInit() {
        if (this.shortDate != null) return;
        String shortDateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
        this.shortDate = new SimpleDateFormat(shortDateFormat);
        this.lastTick = Time.tickCounter();
    }

    private long        lastTick;
    private long        lastPipeAttempt;
    private int         pipeAttemptsRemain;
    private boolean     isPiped = false;
    private static final int PIPE_ATTEMPTS = 60;
    private static final long PIPE_ATTEMPT_DELAY = 1000;
    private static final String PIPE_URL = "\\\\.\\pipe\\SAS_PIPE_LOGGER";
    private static final String PIPE_FLUSH_TIMEOUT_1000 = "\u00001000"; // \u0000 = Separator, Flush Timeout = 1000ms
    private static final String BEGIN_LOG = "] ------------ BEGIN log session -------------";
    private static final String END_LOG = "] -------------- END log session -------------";
    private static final String SWITCH_TO_PIPE_LOG = "] ----------- Switched to pipe mode ----------";
    // TODO: --- Instant Log Enhanced by SAS~Storebror ---
    
    private PrintWriter f;
    private DateFormat longDate;
    private DateFormat shortDate;
    private String fileName;
}
