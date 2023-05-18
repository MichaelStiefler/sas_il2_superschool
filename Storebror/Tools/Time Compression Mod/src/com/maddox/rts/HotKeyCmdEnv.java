/* IL-2 Sturmovik Time Compression Mod
 * for enhanced Time Lapse / Slow Motion
 * 
 * Based on Original Game Code by 1C/Maddox
 * Modified by SAS~Storebror
 * 
 * In contrast to other mods of this kind, this mod
 * doesn't touch the "AircraftHotKeys" class.
 * Touching the "AircraftHotKeys" class is generally a bad
 * idea since this class not only gets modified with each
 * new "official" patch, it's also being modified by endless
 * mods out in the wild, so creating another mod for this
 * class means that you end up in the compatibility hell.
 * This mod in contrast touches the "HotKeyCmdEnv" class only,
 * a class that never got modified by any "official" patch
 * yet and, as far as known to the author of this mod,
 * never got touched by any other mod either.
 * 
 * This mod is supposed to work on any base game version
 * Just drop into your mods folder and set conf.ini values
 * if desired:
 * 
 * [Mods]
 * timeMaxMultiplier = 256
 * timeMaxDivider = 32
 * 
 * The values shown above are default values, they apply
 * if no other values are set (no need to explicitely set them).
 * "timeMaxMultiplier" sets the maximum time lapse value, it
 * should be a powers of 2.
 * 256 means that you can speed up the time lapse up to 256x.
 * "timeMaxDivider" sets the maximum slow motion divider, it
 * should be a powers of 2.
 * 32 means that you can slow down the motion to 1/32.
 */

package com.maddox.rts;

import java.util.List;
import java.util.Locale;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.TimeSkip;
import com.maddox.util.HashMapExt;

public final class HotKeyCmdEnv {

    public final String name() {
        return this.name;
    }

    public final boolean isEnabled() {
        return this.bEnabled;
    }

    public static boolean isEnabled(String s) {
        HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv) RTSConf.cur.hotKeyCmdEnvs.envs.get(s);
        if (hotkeycmdenv == null)
            return false;
        else
            return hotkeycmdenv.bEnabled;
    }

    public final void enable(boolean flag) {
        this.bEnabled = flag;
    }

    public static void enable(String s, boolean flag) {
        HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv) RTSConf.cur.hotKeyCmdEnvs.envs.get(s);
        if (hotkeycmdenv == null) {
            return;
        } else {
            hotkeycmdenv.bEnabled = flag;
            return;
        }
    }

    public final HashMapExt all() {
        return this.cmds;
    }

    public final HotKeyCmd get(String s) {
        return (HotKeyCmd) this.cmds.get(s);
    }

    public final void add(HotKeyCmd hotkeycmd) {
        this.cmds.put(hotkeycmd.name(), hotkeycmd);
        hotkeycmd.hotKeyCmdEnv = this;
    }

    public final String toString() {
        return this.name();
    }

    public static final void addCmd(String s, HotKeyCmd hotkeycmd) {
        HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv) RTSConf.cur.hotKeyCmdEnvs.envs.get(s);
        if (hotkeycmdenv == null)
            hotkeycmdenv = new HotKeyCmdEnv(s);
        hotkeycmdenv.cmds.put(hotkeycmd.name(), hotkeycmd);
        hotkeycmd.hotKeyCmdEnv = hotkeycmdenv;
    }

    public static final void addCmd(HotKeyCmd hotkeycmd) {
        // TODO: +++ Time Compression Mod +++
        
        // This is the place where we apply the new "Time Lapse" and "Slow Motion"
        // limits, without touching the AircraftHotKeys class where these functions
        // usually are defined.
        
        // First check whether the game is currently about to create time compression
        // hotkeys at all...
        if (RTSConf.cur.hotKeyCmdEnvs.cur.name.equals(TIME_COMPRESSION)) {
            // it does, so check whether the game is about to create the time lapse hotkey
            if (hotkeycmd.name().equals(TIME_SPEED_UP)) {
                // it does.
                // Throw away the hotkey definition from AircraftHotKeys class and replace
                // it with our new, modified one.
                hotkeycmd = new HotKeyCmd(true, "timeSpeedUp", "0") {
                    public void begin() {
                        if (!TimeSkip.isDo()) {
                            if (Time.isEnableChangeSpeed()) {
                                float f = Time.nextSpeed() * 2.0F;
                                // read max multiplier value from conf.ini, default to 256.
                                int maxMult = Config.cur.ini.get("Mods", "timeMaxMultiplier", 256);
                                if (f <= maxMult) {
                                    Time.setSpeed(f);
                                    HotKeyCmdEnv.showTimeSpeed(f);
                                }
                            }
                        }
                    }

                    public void created() {
                        this.setRecordId(25);
                    }
                };
            // the game is not creating the time lapse hotkey, but maybe the slow motion one?
            } else if (hotkeycmd.name().equals(TIME_SPEED_DOWN)) {
                // it does.
                // Throw away the hotkey definition from AircraftHotKeys class and replace
                // it with our new, modified one.
                hotkeycmd = new HotKeyCmd(true, "timeSpeedDown", "2") {
                    public void begin() {
                        if (!TimeSkip.isDo()) {
                            if (Time.isEnableChangeSpeed()) {
                                float f = Time.nextSpeed() / 2.0F;
                                // read max divider value from conf.ini, default to 32.
                                int maxDiv = Config.cur.ini.get("Mods", "timeMaxDivider", 32);
                                if (f >= 1F/maxDiv) {
                                    Time.setSpeed(f);
                                    HotKeyCmdEnv.showTimeSpeed(f);
                                }
                            }
                        }
                    }

                    public void created() {
                        this.setRecordId(26);
                    }
                };
            }
        }
        // TODO: --- Time Compression Mod ---

        RTSConf.cur.hotKeyCmdEnvs.cur.cmds.put(hotkeycmd.name(), hotkeycmd);
        hotkeycmd.hotKeyCmdEnv = RTSConf.cur.hotKeyCmdEnvs.cur;
    }

    public static final void setCurrentEnv(String s) {
        HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv) RTSConf.cur.hotKeyCmdEnvs.envs.get(s);
        if (hotkeycmdenv == null)
            hotkeycmdenv = new HotKeyCmdEnv(s);
        RTSConf.cur.hotKeyCmdEnvs.cur = hotkeycmdenv;
    }

    public static final HotKeyCmdEnv currentEnv() {
        return RTSConf.cur.hotKeyCmdEnvs.cur;
    }

    public static final List allEnv() {
        return RTSConf.cur.hotKeyCmdEnvs.lst;
    }

    public static final HotKeyCmdEnv env(String s) {
        return (HotKeyCmdEnv) RTSConf.cur.hotKeyCmdEnvs.envs.get(s);
    }

    public HotKeyEnv hotKeyEnv() {
        return this.hotKeyEnv;
    }

    protected HotKeyCmdEnv(String s) {
        this.bEnabled = true;
        this.name = s;
        this.cmds = new HashMapExt();
        RTSConf.cur.hotKeyCmdEnvs.envs.put(s, this);
        RTSConf.cur.hotKeyCmdEnvs.lst.add(this);
        this.hotKeyEnv = (HotKeyEnv) RTSConf.cur.hotKeyEnvs.envs.get(s);
        if (this.hotKeyEnv == null)
            this.hotKeyEnv = new HotKeyEnv(s);
    }

    // TODO: +++ Time Compression Mod +++
    
    // Show the Time Lapse / Slow Motion value just selected by pressing the corresponding hotkey.
    private static void showTimeSpeed(float f) {
        int i = (int) Math.floor(f * 4.0F);
        
        // In the range of 1/4 through 8x, localized hud log messages exist already, show them.
        if (i > 0 && i < 64) {
            switch (i) {
                case 4:
                    Main3D.cur3D().hud._log(0, "TimeSpeedNormal");
                    break;
                case 8:
                    Main3D.cur3D().hud._log(0, "TimeSpeedUp2");
                    break;
                case 16:
                    Main3D.cur3D().hud._log(0, "TimeSpeedUp4");
                    break;
                case 32:
                    Main3D.cur3D().hud._log(0, "TimeSpeedUp8");
                    break;
                case 2:
                    Main3D.cur3D().hud._log(0, "TimeSpeedDown2");
                    break;
                case 1:
                    Main3D.cur3D().hud._log(0, "TimeSpeedDown4");
            }
        // Outside the 1/4 through 8x boundaries, we have to create the hud message ourselves.
        } else {
            
            // Localize the hud message
            String part1 = "";
            String part2 = "";
            if ("ru".equals(Locale.getDefault().getLanguage())) {
                part1 = "\u0432\u0440\u0435\u043c\u044f";
                part2 = "\u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c";
            } else if ("cs".equals(Locale.getDefault().getLanguage())) {
                part1 = "\u010Cas";
                part2 = "rychlost";
            } else if ("pl".equals(Locale.getDefault().getLanguage())) {
                part1 = "Czas";
                if (f>=1.0F) {
                    part2 = "nominalny";
                } else {
                    part2 = "nominalnego";
                }
            } else if ("hu".equals(Locale.getDefault().getLanguage())) {
                part1 = "Id\u0151";
                part2 = "sebess\u00E9g";
            } else if ("lt".equals(Locale.getDefault().getLanguage())) {
                part1 = "Laikas";
                part2 = "greitis";
            } else if ("ja".equals(Locale.getDefault().getLanguage())) {
                part1 = "\u6642\u9593";
                part2 = "\u901f\u5ea6";
            } else if ("de".equals(Locale.getDefault().getLanguage())) {
                part1 = "Zeit";
                part2 = "Geschwindigkeit";
            } else if ("it".equals(Locale.getDefault().getLanguage())) {
                part1 = "Tempo";
                part2 = "velocità";
            } else {
                part1 = "Time";
                part2 = "Speed";
            }
            String logLine = "";
            // Add multiplier or divider in fraction notation
            if (f>=1.0F) {
                logLine = part1 + ":" + Math.round(f) + "x " + part2;
            } else {
                logLine = part1 + ": 1/" + Math.round(1.0F/f) + " " + part2;
            }
            // Show the full localized hud log message, it's in the same format
            // as the default ones from the game.
            Main3D.cur3D().hud._log(0, logLine);
        }
        
    }
    // TODO: --- Time Compression Mod ---

    public static final String DEFAULT          = "default";
    public static final String CMDRUN           = "cmdrun";
    private String             name;
    private boolean            bEnabled;
    private HashMapExt         cmds;
    private HotKeyEnv          hotKeyEnv;

    // TODO: +++ Time Compression Mod +++
    // Static definitions for time compression mod
    private static String      TIME_COMPRESSION = "timeCompression";
    private static String      TIME_SPEED_UP    = "timeSpeedUp";
    private static String      TIME_SPEED_DOWN  = "timeSpeedDown";
    // TODO: --- Time Compression Mod ---
}
