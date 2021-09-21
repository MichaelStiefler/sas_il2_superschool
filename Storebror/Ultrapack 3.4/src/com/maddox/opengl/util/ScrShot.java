package com.maddox.opengl.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.maddox.il2.engine.Config;
import com.maddox.rts.HomePath;
import com.maddox.sas1946.il2.util.CommonTools;

public class ScrShot {

    public void grab() {
        this.getScreenShot(this.screenshotType, this.scrName(0), this.scrName(1), (int) (this.jpgQuality * 100F));
    }

    public void grab(String fileName) {
        System.out.println("Screenshot: " + fileName);
        this.getScreenShot(this.screenshotType, SCREENSHOTS_DIR + fileName + types[0], SCREENSHOTS_DIR + fileName + types[1], (int) (this.jpgQuality * 100F));
    }

    private String scrName(int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, CommonTools.getTimeZoneBiasMinutes());
        String str = SCREENSHOTS_DIR + this.shortDate.format(calendar.getTime()) + types[type];
        str = str.replace('\\', '-');

        str = str.replace(':', '-');
        str = str.replace('*', '-');
        str = str.replace('?', '-');
        str = str.replace('"', '-');
        str = str.replace('<', '-');
        str = str.replace('>', '-');
        str = str.replace('|', '-');
        
        if (this.screenshotType == 2 || this.screenshotType == type) System.out.println("Screenshot: " + str);

        return str;
    }
    
    public ScrShot(String s) {
        this.shortDate = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss.SSS");
        this.screenshotType = Config.cur.ini.get("game", "ScreenshotType", 1, 0, 2);
        this.jpgQuality = Config.cur.ini.get("game", "jpgQuality", 1.0F, 0.0F, 1.0F);
        this.scrDir = new File(HomePath.toFileSystemName(SCREENSHOTS_DIR, 0));
        if (!this.scrDir.exists()) this.scrDir.mkdirs();
    }

    private static boolean libLoaded = false;

    private native void getScreenShot(int i, String s, String s1, int j);

    private DateFormat          shortDate;
    private int                 screenshotType;
    private float               jpgQuality;
    private static final String TGA             = ".tga";
    private static final String JPG             = ".jpg";
    private static final String[] types= {TGA, JPG};
    private static final String SCREENSHOTS_DIR = "MyScreenShots/";
    private File                scrDir;
}
