package com.maddox.opengl.util;

// TODO: Storebror: New Screenshots from native DT.dll

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.maddox.il2.engine.Config;
import com.maddox.rts.HomePath;

public class ScrShot {

    public void grab() {
        this.getScreenShot(Config.cur.ini.get("game", "ScreenshotType", 1, 0, 2), this.scrName(0), this.scrName(1), (int) (this.jpgQuality * 100F));
    }

    private String scrName(int i) {
        String str = SCREENSHOTS_DIR + new SimpleDateFormat("yyyy.MM.dd hh-mm-ss").format(new Date()) + (i <= 0 ? ".tga" : ".jpg");
        System.out.println("Screenshot: " + str);
        str = str.replace('\\', '-');

        str = str.replace(':', '-');
        str = str.replace('*', '-');
        str = str.replace('?', '-');
        str = str.replace('"', '-');
        str = str.replace('<', '-');
        str = str.replace('>', '-');
        str = str.replace('|', '-');

        return str;
    }

    public ScrShot(String s) {
        this.prefixName = s;
        this.jpgQuality = Config.cur.ini.get("game", "jpgQuality", 1.0F, 0.0F, 1.0F);
        this.scrDir = new File(HomePath.toFileSystemName(SCREENSHOTS_DIR, 0));
        if (!this.scrDir.exists()) this.scrDir.mkdirs();
    }

//    public static final void loadNative() {
//        if (!libLoaded) {
//            System.loadLibrary("DT");
//            System.out.println("  *** Library **** DT.dll ****  Loaded *** ");
//            libLoaded = true;
//        }
//    }
//
//    static {
//        loadNative();
//    }

    private static boolean libLoaded = false;

    private native void getScreenShot(int i, String s, String s1, int j);

    private String              prefixName;
    private float               jpgQuality;
    private static final String TGA             = ".tga";
    private static final String JPG             = ".jpg";
    private static final String SCREENSHOTS_DIR = "MyScreenShots/";
    private File                scrDir;
}
