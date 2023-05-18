package com.maddox.rts;

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
        this.f.println("[" + this.longDate.format(calendar.getTime()) + "] -------------- END log session -------------");
        this.f.close();
    }

    public ConsoleLogFile(String s) throws FileNotFoundException {
        // TODO: +++ Instant Log by SAS~Storebror +++
        this.fileName = s;
        // TODO: --- Instant Log by SAS~Storebror ---
        this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
        Calendar calendar = Calendar.getInstance();
        this.longDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        // TODO: +++ Additional Log Settings by SAS~Storebror +++
//        this.shortDate = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        // TODO: --- Additional Log Settings by SAS~Storebror ---
        this.f.println();
        this.f.println("[" + this.longDate.format(calendar.getTime()) + "] ------------ BEGIN log session -------------");
        this.f.println();
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
    // TODO: --- Additional Log Settings by SAS~Storebror ---
}
