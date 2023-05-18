package com.maddox.il2.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.maddox.rts.HomePath;
import com.maddox.rts.MainWin32;
import com.maddox.rts.RTSConf;
import com.maddox.rts.RTSConfWin;
import com.maddox.rts.Time;

class ConsoleGL0Render extends Render {

    public void exlusiveDraw() {
        if (RTSConf.cur instanceof RTSConfWin) {
            ((MainWin32) RTSConf.cur.mainWindow).loopMsgs();
        }
        GObj.DeleteCppObjects();
        this.renders().paint(this);
    }

    public void exlusiveDrawStep(String s, int i) {
        this.sstep = s;
        this.renders().paint(this);
    }

    private boolean getDOFActive() {
        try {
            Class testClass = Class.forName("com.maddox.il2.objects.vehicles.tanks.TankWWI");
            if (testClass != null) {
                testClass = Class.forName("com.maddox.il2.objects.air.PaintSchemeFMParWW1");
                if (testClass != null) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean getWAWActive() {
        try {
            Class testClass = Class.forName("com.maddox.il2.objects.air.PaintSchemeSPIT2A");
            if (testClass != null) {
                testClass = Class.forName("com.maddox.il2.objects.air.CockpitS_199");
                if (testClass != null) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean getJTWActive() {
        try {
            Class testClass = Class.forName("com.maddox.il2.objects.air.Su_25");
            if (testClass != null) {
                testClass = Class.forName("com.maddox.il2.objects.air.Mig_19");
                if (testClass != null) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean getTGAActive() {
        try {
            Class testClass = Class.forName("com.maddox.il2.objects.air.Breguet_CT");
            if (testClass != null) {
                testClass = Class.forName("com.maddox.il2.objects.air.Curtiss_T32");
                if (testClass != null) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private void setExtraCaption() {
        if (this.bExtraCaptionSet) {
            return;
        }
        this.bExtraCaptionSet = true;
        if (this.getWAWActive()) {
            BAT_MODULE = "World at War";
        } else if (this.getDOFActive()) {
            BAT_MODULE = "Dawn of Flight";
        } else if (this.getJTWActive()) {
            BAT_MODULE = "The Jet Age";
        } else if (this.getTGAActive()) {
            BAT_MODULE = "The Golden Age";
        }
        BufferedReader br = null;
        try {
            File file = new File(HomePath.toFileSystemName(BAT_VERSION_FILE, 0));
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                br = new BufferedReader(new InputStreamReader(fis));
                String line = br.readLine();
                if ((line != null) && (line.length() > 0)) {
                    VERSION_STRING = line;
                    System.out.println("BAT Version Info (from BAT.version file): " + VERSION_STRING);
                } else {
                    System.out.println("BAT.version file contained no Version Info.");
                }
            } else {
                System.out.println("No BAT.version file found, using default Version Info.");
            }
        } catch (Exception e) {
            System.out.println("Exception raised while trying to read BAT Version:");
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.out.println("Exception raised while trying to close BAT Version File:");
                e.printStackTrace();
            }
        }
    }

    public void render() {
        if (ConsoleGL0.backgroundMat != null) {
            if ((RendersMain.getViewPortWidth() / RendersMain.getViewPortHeight()) < 1) {
                float f = RendersMain.getViewPortWidth() * 0.75F;
                float f2 = (RendersMain.getViewPortHeight() - f) * 0.5F;
                Render.drawTile(0.0F, f2, RendersMain.getViewPortWidth(), f, 0.0F, ConsoleGL0.backgroundMat, -1, 0.0F, 1.0F, 1.0F, -1F);
            } else {
                Render.drawTile(0.0F, 0.0F, RendersMain.getViewPortWidth(), RendersMain.getViewPortHeight(), 0.0F, ConsoleGL0.backgroundMat, -1, 0.0F, 1.0F, 1.0F, -1F);
            }
            if (this.sstep != null) {
                this.setExtraCaption();
                int i = 0xff0000ff;
                if ("ru".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                    TTFont.font[2].output(i, RendersMain.getViewPortWidth() * 0.083F, RendersMain.getViewPortHeight() * 0.12F, 0.0F, this.sstep);
                    TTFont.font[2].output(i, RendersMain.getViewPortWidth() * 0.083F, ((RendersMain.getViewPortHeight() * 0.12F) + TTFont.font[2].height()) - TTFont.font[2].descender(), 0.0F, VERSION_STRING + " · \u043c\u043e\u0434\u0443\u043b\u044c: '" + BAT_MODULE + "'");
                } else {
                    String userLanguage = RTSConf.cur.locale.getLanguage().toLowerCase();
                    if (userLanguage == null) {
                        userLanguage = "us";
                    } else {
                        userLanguage = userLanguage.substring(0, 2);
                    }
                    if (userLanguage == "en") {
                        userLanguage = "us";
                    }
                    if (!"de".equals(userLanguage) && !"fr".equals(userLanguage) && !"cs".equals(userLanguage) && !"pl".equals(userLanguage) && !"hu".equals(userLanguage) && !"lt".equals(userLanguage) && !"us".equals(userLanguage)) {
                        userLanguage = "us";
                    }
                    String versionString = VERSION_STRING + " · Module: '" + BAT_MODULE + "'";
                    if ("de".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · Modul: '" + BAT_MODULE + "'";
                    } else if ("fr".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · Module: '" + BAT_MODULE + "'";
                    } else if ("cs".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · Modul: '" + BAT_MODULE + "'";
                    } else if ("pl".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · Modu\u0142: '" + BAT_MODULE + "'";
                    } else if ("hu".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · modul: '" + BAT_MODULE + "'";
                    } else if ("lt".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · modulis: '" + BAT_MODULE + "'";
                    } else if ("ja".equals(userLanguage)) {
                        versionString = VERSION_STRING + " · \u30e2\u30b8\u30e5\u30fc\u30eb: '" + BAT_MODULE + "'";
                    }
                    TTFont.font[2].output(i, RendersMain.getViewPortWidth() * 0.02F, RendersMain.getViewPortHeight() * 0.17F, 0.0F, this.sstep);
                    TTFont.font[2].output(i, RendersMain.getViewPortWidth() * 0.02F, ((RendersMain.getViewPortHeight() * 0.17F) + TTFont.font[2].height()) - TTFont.font[2].descender(), 0.0F, versionString);
                }
            }
            return;
        }
        if (ConsoleGL0.bActive || (ConsoleGL0.consoleListener != null)) {
            List list = RTSConf.cur.console.historyOut();
            int j = RTSConf.cur.console.startHistoryOut();
            if (!RTSConf.cur.console.isShowHistoryOut()) {
                list = RTSConf.cur.console.historyCmd();
                j = RTSConf.cur.console.startHistoryCmd();
            }
            int k = (this.getViewPortHeight() / ConsoleGL0.font.height()) - 1;
            if (ConsoleGL0.bActive) {
                int l = RTSConf.cur.console.editBuf.length();
                String s = RTSConf.cur.console.getPrompt();
                int l1 = s.length();
                int i2 = 0;
                if (l1 != 0) {
                    s.getChars(0, l1, this.buf, 0);
                }
                if ((l + l1) > 0) {
                    if ((l + l1) > this.buf.length) {
                        this.buf = new char[l + l1 + 16];
                    }
                    if (l != 0) {
                        RTSConf.cur.console.editBuf.getChars(0, l, this.buf, l1);
                        float f4 = this.getViewPortWidth() - ConsoleGL0.typeOffset;
                        do {
                            float f6 = ConsoleGL0.font.width(this.buf, 0, (RTSConf.cur.console.editPos - i2) + l1);
                            if (f6 < f4) {
                                break;
                            }
                            i2++;
                            RTSConf.cur.console.editBuf.getChars(i2, l, this.buf, l1);
                        } while (true);
                    }
                    ConsoleGL0.font.output(-1, ConsoleGL0.typeOffset, -ConsoleGL0.font.descender(), 0.0F, this.buf, 0, (l - i2) + l1);
                }
                if (((Time.endReal() / 500L) % 3L) != 0L) {
                    float f5 = 0.0F;
                    if ((RTSConf.cur.console.editPos + l1) > 0) {
                        f5 = ConsoleGL0.font.width(this.buf, 0, (RTSConf.cur.console.editPos - i2) + l1);
                    }
                    this.buf[0] = '|';
                    ConsoleGL0.font.output(-1, (ConsoleGL0.typeOffset + f5) - 1.0F, -ConsoleGL0.font.descender(), 0.0F, this.buf, 0, 1);
                }
            }
            if (RTSConf.cur.console.bWrap) {
                int i1 = j;
                int k1 = 1;
                for (; (k > 0) && (i1 < list.size()); i1++) {
                    String s1 = (String) list.get(i1);
                    int j2;
                    for (j2 = s1.length() - 1; (j2 >= 0) && (s1.charAt(j2) < ' '); j2--) {
                        ;
                    }
                    if (j2 > 0) {
                        j2++;
                        int l2 = 0;
                        int i3 = 0;
                        int j3 = j2;
                        this.ofs[l2] = i3;
                        do {
                            if ((j3 <= 0) || (ConsoleGL0.font.width(s1, i3, j3) <= 0.0F)) {
                                break;
                            }
                            int k3 = j3;
                            do {
                                if ((ConsoleGL0.font.width(s1, i3, j3) + ConsoleGL0.typeOffset) <= this.getViewPortWidth()) {
                                    break;
                                }
                                while ((--j3 > 0) && (s1.charAt(i3 + j3) != ' ')) {
                                    ;
                                }
                            } while (j3 != 0);
                            if (j3 == 0) {
                                for (j3 = k3; ((ConsoleGL0.font.width(s1, i3, j3) + ConsoleGL0.typeOffset) > this.getViewPortWidth()) && (--j3 != 0);) {
                                    ;
                                }
                            }
                            if ((l2 + 1) >= this.ofs.length) {
                                break;
                            }
                            l2++;
                            if (j3 == 0) {
                                this.ofs[l2] = j2;
                                break;
                            }
                            i3 += j3;
                            j3 = j2 - i3;
                            this.ofs[l2] = i3;
                        } while (true);
                        for (; (l2 > 0) && (k > 0); k--) {
                            ConsoleGL0.font.output(-1, ConsoleGL0.typeOffset, (ConsoleGL0.font.height() * k1) - ConsoleGL0.font.descender(), 0.0F, s1, this.ofs[l2 - 1], this.ofs[l2] - this.ofs[l2 - 1]);
                            k1++;
                            l2--;
                        }

                    }
                }

            } else {
                if (k > (list.size() - j)) {
                    k = list.size() - j;
                }
                for (int j1 = 0; j1 < k; j1++) {
                    float f3 = (ConsoleGL0.font.height() * (j1 + 1)) - ConsoleGL0.font.descender();
                    String s2 = (String) list.get(j1 + j);
                    int k2;
                    for (k2 = s2.length() - 1; (k2 >= 0) && (s2.charAt(k2) < ' '); k2--) {
                        ;
                    }
                    if (k2 > 0) {
                        ConsoleGL0.font.output(-1, ConsoleGL0.typeOffset, f3, 0.0F, s2, 0, k2 + 1);
                    }
                }

            }
        }
    }

    public ConsoleGL0Render(float f) {
        super(f);
        this.bExtraCaptionSet = false;
        this.buf = new char[128];
        this.ofs = new int[128];
        this.sstep = null;
        this.useClearDepth(false);
        this.useClearColor(false);
    }

    private static String   VERSION_STRING   = "IL-2 1946 · Battlefield-Airborne-Tactical · v1.0";
    private static String   BAT_MODULE       = "unknown";
    private static String   BAT_VERSION_FILE = "BAT.version";
    private boolean         bExtraCaptionSet;
    public static final int COLOR            = -1;
    private char            buf[];
    private int             ofs[];
    public String           sstep;

}
