package com.maddox.il2.engine;

import com.maddox.JGP.Color4f;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.opengl.Provider;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class TextScr extends Render {

    public void preRender() {
    }

    public void render() {
        Provider.setPauseProfile(true);
        for (HashMapIntEntry hashmapintentry = this.context.nextEntry(null); hashmapintentry != null; hashmapintentry = this.context.nextEntry(hashmapintentry)) {
            int i = hashmapintentry.getKey();
            TextScrItem textscritem = (TextScrItem) hashmapintentry.getValue();
            textscritem.font.output((((int) (textscritem.color.w * 255F) & 0xff) << 24) | (((int) (textscritem.color.x * 255F) & 0xff) << 0) | (((int) (textscritem.color.y * 255F) & 0xff) << 8) | (((int) (textscritem.color.z * 255F) & 0xff) << 16), i >> 16, i & 0xffff, 0.0F, textscritem.str);
        }

        this.context.clear();
        Provider.setPauseProfile(false);
    }

    public static void output(int i, int j, String s) {
        if ((s == null) || "".equals(s)) {
            TextScr.scr.context.remove(((i & 0xffff) << 16) | (j & 0xffff));
        } else {
            TextScr.scr.context.put(((i & 0xffff) << 16) | (j & 0xffff), new TextScrItem(TextScr.scr.color, TextScr.scr.font, s));
        }
    }

    public static void output(float f, float f1, String s) {
        if ((s == null) || "".equals(s)) {
            TextScr.scr.context.remove((((int) f & 0xffff) << 16) | ((int) f1 & 0xffff));
        } else {
            TextScr.scr.context.put((((int) f & 0xffff) << 16) | ((int) f1 & 0xffff), new TextScrItem(TextScr.scr.color, TextScr.scr.font, s));
        }
    }

    public static TTFont font() {
        if (TextScr.scr == null) {
            TextScr.scr = new TextScr();
            CameraOrtho2D cameraortho2d = new CameraOrtho2D();
            cameraortho2d.set(0.0F, TextScr.scr.getViewPortWidth(), 0.0F, TextScr.scr.getViewPortHeight());
            TextScr.scr.setCamera(cameraortho2d);
            TextScr.scr.setName("renderTextScr");
        }
        return TextScr.scr.font;
    }

    public static void setFont(TTFont ttfont) {
        TextScr.scr.font = ttfont;
    }

    public static void setFont(String s) {
        TextScr.scr.font = TTFont.get(s);
    }

    public static void setColor(Color4f color4f) {
        TextScr.scr.color = new Color4f(color4f);

        // =============================================================================
        // WalkerPilot Mike Mod +++
        // -----------------------------------------------------------------------------
        TextScr.initwalkerPilotHotkey();
        // =============================================================================
        // WalkerPilot Mike Mod ---
        // -----------------------------------------------------------------------------
    }

    public static Color4f color() {
        return new Color4f(TextScr.scr.color);
    }

    private TextScr() {
        super(-1F);
        this.context = new HashMapInt();
        this.font = TTFont.font[1];
        this.color = new Color4f(0.0F, 0.0F, 0.0F, 1.0F);
        this.useClearDepth(false);
        this.useClearColor(false);
    }

    protected void contextResize(int i, int j) {
        super.contextResize(i, j);
        this.context.clear();
    }

    public static TextScr This() {
        return TextScr.scr;
    }

    private HashMapInt     context;
    private TTFont         font;
    private Color4f        color;
    private static TextScr scr;

    // =============================================================================
    // WalkerPilot Mike Mod +++
    // -----------------------------------------------------------------------------
    private static boolean walkerPilotHotkeyInitdone = false;

    private static void initwalkerPilotHotkey() {
        if (TextScr.walkerPilotHotkeyInitdone || !(Main.cur() instanceof Main3D) || (((Main3D) Main.cur()).aircraftHotKeys == null)) {
            return;
        }
        TextScr.walkerPilotHotkeyInitdone = true;
        HotKeyCmdEnv.setCurrentEnv("misc");
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ejectPilot", "02") {

            public void created() {
                this.setRecordId(272);
            }

            public void begin() {
                if (!Actor.isAlive(World.getPlayerAircraft()) || World.isPlayerParatrooper() || World.isPlayerDead() || !Mission.isPlaying()) {
                    return;
                }
                if (World.isPlayerGunner() || !(World.getPlayerFM() instanceof RealFlightModel)) {
                    return;
                }
                RealFlightModel realflightmodel = (RealFlightModel) World.getPlayerFM();
                if (!realflightmodel.isRealMode() || realflightmodel.AS.bIsAboutToBailout || !realflightmodel.AS.bIsEnableToBailout) {
                    return;
                }

                // =============================================================================
                // WalkerPilot
                // -----------------------------------------------------------------------------

                if (!Mission.isNet() && !NetMissionTrack.isRecording() && !NetMissionTrack.isPlaying() && !Main3D.cur3D().isDemoPlaying() && World.getPlayerFM().isStationedOnGround()) {
                    try {
                        Aircraft aircraft = World.getPlayerAircraft();
                        Actor actor = aircraft;
                        Hook hook = actor.findHook("_ExternalBail01");
                        if (hook != null) {
                            if (aircraft.FM.CT.bHasCockpitDoorControl && (aircraft.FM.CT.cockpitDoorControl < 0.5F) && (aircraft.FM.CT.getCockpitDoor() < 0.01F)) {
                                aircraft.FM.AS.setCockpitDoor(aircraft, 1);
                            }
                            Paratrooper.doRemoveBodyFromPlane(aircraft.hierMesh(), 1, false);
                            // why? - dunno
//                                    aircraft.FM.AS.astateBailoutStep++;
                            Loc loc = new Loc(0.0D, 0.0D, 0.0D, World.Rnd().nextFloat(-15F, 15F), 0.0F, 0.0F);
                            hook.computePos(actor, actor.pos.getAbs(), loc);
                            new Paratrooper(actor, actor.getArmy(), 0, loc, aircraft.FM.Vwld);
                            aircraft.FM.CT.setPowerControl(0.0F);
                            aircraft.FM.CT.WeaponControl[0] = false;
                            aircraft.FM.CT.WeaponControl[1] = false;
                            // Disable pilot, two options
//                                    realflightmodel.AS.disablePilot(realflightmodel.AS.astatePlayerIndex);
                            realflightmodel.AS.astatePilotStates[realflightmodel.AS.astatePlayerIndex] = 100;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                // -----------------------------------------------------------------------------
                // WalkerPilot
                // =============================================================================

                AircraftState.bCheckPlayerAircraft = false;
                ((Aircraft) realflightmodel.actor).hitDaSilk();
                AircraftState.bCheckPlayerAircraft = true;
                Voice.cur().SpeakBailOut[(realflightmodel.actor.getArmy() - 1) & 1][((Aircraft) realflightmodel.actor).aircIndex()] = (int) (Time.current() / 60000L) + 1;
                new MsgAction(true) {
                    public void doAction() {
                        if (!Main3D.cur3D().isDemoPlaying() || !HotKeyEnv.isEnabled("aircraftView")) {
                            HotKeyCmd.exec("aircraftView", "OutsideView");
                        }
                    }
                };
                return;

            }

        });
    }
    // =============================================================================
    // WalkerPilot Mike Mod ---
    // -----------------------------------------------------------------------------
}
