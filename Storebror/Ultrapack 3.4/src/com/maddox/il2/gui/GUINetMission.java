/*4.10.1 class*/
package com.maddox.il2.gui;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjState;
import com.maddox.rts.RTSConf;

public class GUINetMission extends GameState {
    public GUIClient                                      client;
    public DialogClient                                   dialogClient;
    public GUIInfoMenu                                    infoMenu;
    public GUIInfoName                                    infoName;
    public GUIButton                                      bVideo;
    public GUIButton                                      b3d;
    public GUIButton                                      bSound;
    public GUIButton                                      bControls;
    public GUIButton                                      bNet;
    public GUIButton                                      bReFly;
    public GUIButton                                      bExit;
    public GUIButton                                      bBack;
    public GUIButton                                      bTrack;
    protected boolean                                     bEnableReFly;
    private static com.maddox.il2.objects.air.Paratrooper playerParatrooper = null;

    public class DialogClient extends GUIDialogClient {
        public boolean notify(GWindow gwindow, int i, int i_0_) {
            if (i != 2) return super.notify(gwindow, i, i_0_);
            if (gwindow == GUINetMission.this.bVideo) {
                Main.stateStack().push(12);
                return true;
            }
            if (gwindow == GUINetMission.this.b3d) {
                Main.stateStack().push(11);
                return true;
            }
            if (gwindow == GUINetMission.this.bSound) {
                Main.stateStack().push(13);
                return true;
            }
            if (gwindow == GUINetMission.this.bControls) {
                Main.stateStack().push(20);
                return true;
            }
            if (gwindow == GUINetMission.this.bNet) {
                Main.stateStack().push(52);
                return true;
            }
            if (gwindow == GUINetMission.this.bReFly) {
                // TODO: ZUTI: release gunner position
                // --------------------------------------------------------
                ZutiSupportMethods_Air.destroyPlayerAircraft(false);
                // --------------------------------------------------------

                GUINetMission.this.doReFly();
                return true;
            }
            if (gwindow == GUINetMission.this.bExit) {
                // TODO: ZUTI: release gunner position
                // --------------------------------------------------------
                ZutiSupportMethods_Air.destroyPlayerAircraft(false);
                // --------------------------------------------------------

                GUINetMission.this.doExit();
                return true;
            }
            if (gwindow == GUINetMission.this.bBack) {
                GUINetMission.this.client.hideWindow();
                GUI.unActivate();
                return true;
            }
            if (gwindow == GUINetMission.this.bTrack) {
                if (NetMissionTrack.isRecording()) {
                    NetMissionTrack.stopRecording();
                    GUINetMission.this.client.hideWindow();
                    GUI.unActivate();
                } else Main.stateStack().push(59);
                return true;
            }
            return super.notify(gwindow, i, i_0_);
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(32.0F), this.y1024(368.0F), this.x1024(288.0F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(32.0F), this.y1024(544.0F), this.x1024(288.0F), 2.0F);
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(96.0F), this.y1024(32.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.VideoModes"));
            this.draw(this.x1024(96.0F), this.y1024(96.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.VideoOptions"));
            this.draw(this.x1024(96.0F), this.y1024(160.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.SoundSetup"));
            this.draw(this.x1024(96.0F), this.y1024(224.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.Controls"));
            this.draw(this.x1024(96.0F), this.y1024(288.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.Network"));
            this.draw(this.x1024(96.0F), this.y1024(576.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.Return2Miss"));
            if (NetMissionTrack.isRecording()) this.draw(this.x1024(96.0F), this.y1024(640.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.StopRecording"));
            else this.draw(this.x1024(96.0F), this.y1024(640.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUINetMission.this.i18n("miss.StartRecording"));
            GUINetMission.this.clientRender();
        }

        public void setPosSize() {
            this.set1024PosSize(352.0F, 32.0F, 352.0F, 720.0F);
            GUINetMission.this.bVideo.setPosC(this.x1024(56.0F), this.y1024(56.0F));
            GUINetMission.this.b3d.setPosC(this.x1024(56.0F), this.y1024(120.0F));
            GUINetMission.this.bSound.setPosC(this.x1024(56.0F), this.y1024(184.0F));
            GUINetMission.this.bControls.setPosC(this.x1024(56.0F), this.y1024(248.0F));
            GUINetMission.this.bNet.setPosC(this.x1024(56.0F), this.y1024(312.0F));
            GUINetMission.this.bReFly.setPosC(this.x1024(56.0F), this.y1024(424.0F));
            GUINetMission.this.bExit.setPosC(this.x1024(56.0F), this.y1024(488.0F));
            GUINetMission.this.bBack.setPosC(this.x1024(56.0F), this.y1024(600.0F));
            GUINetMission.this.bTrack.setPosC(this.x1024(56.0F), this.y1024(666.0F));
        }
    }

    public static void setPlayerParatrooper(com.maddox.il2.objects.air.Paratrooper paratrooper) {
        playerParatrooper = paratrooper;
    }

    private static boolean isStationaryOnCarrierDeck() {
        if (com.maddox.il2.ai.World.getPlayerFM() != null && com.maddox.il2.ai.World.getPlayerFM().Gears.isUnderDeck() && com.maddox.il2.ai.World.getPlayerFM().brakeShoeLastCarrier != null
                && com.maddox.il2.ai.World.getPlayerFM().brakeShoeLastCarrier instanceof com.maddox.il2.objects.ships.BigshipGeneric) {
            com.maddox.il2.objects.ships.BigshipGeneric bigshipgeneric = (com.maddox.il2.objects.ships.BigshipGeneric) com.maddox.il2.ai.World.getPlayerFM().brakeShoeLastCarrier;
            com.maddox.JGP.Vector3d vector3d = new Vector3d();
            bigshipgeneric.getSpeed(vector3d);
            com.maddox.JGP.Vector3d vector3d1 = new Vector3d();
            com.maddox.il2.ai.World.getPlayerFM().getSpeed(vector3d1);
            if (java.lang.Math.abs(vector3d1.length() - vector3d.length()) < 0.5D) return true;
        }
        return false;
    }

    public static boolean isOkToReFly() {
        com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.World.getPlayerAircraft();
        if (com.maddox.il2.ai.World.isPlayerParatrooper() && !com.maddox.il2.ai.World.isPlayerDead() && playerParatrooper != null && playerParatrooper.isChuteSafelyOpened()) return true;
        if (aircraft != null && !com.maddox.il2.ai.World.isPlayerParatrooper() && !com.maddox.il2.ai.World.isPlayerDead()) {
            if (com.maddox.il2.ai.World.getPlayerFM().brakeShoe || com.maddox.il2.gui.GUINetMission.isStationaryOnCarrierDeck()) return true;
            if (com.maddox.il2.ai.World.getPlayerFM().isCapableOfBMP() && com.maddox.il2.ai.World.getPlayerFM().isCapableOfACM() && com.maddox.il2.ai.World.getPlayerFM().Gears.onGround() && aircraft.getSpeed(null) < 1.0D) return true;
        }
        return false;
    }

    public static boolean isEnableReFly() {
        // TODO: Storebror: Track Users hitting refly where this shouldn't be possible
        if (RTSConf.cur.console.getEnv().existAtom("reflytest", true)) if (RTSConf.cur.console.getEnv().atom("reflytest").toString().trim().equalsIgnoreCase("on")) {
            System.out.println("GUINetMission overriding isEnableReFly() checks because reflytest is on");
            return true;
        }

        // TODO: Added by |ZUTI|
        // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // This flag is set if user selected unavailable aircraft
        if (com.maddox.il2.objects.air.NetAircraft.ZUTI_REFLY_OWERRIDE) return true;
        if (Mission.MDS_VARIABLES().zutiMisc_DisableReflyForMissionDuration) return false;
        if (Mission.MDS_VARIABLES().zutiMisc_EnableReflyOnlyIfBailedOrDied) {
            if (ZutiSupportMethods.ZUTI_KIA_COUNTER > Mission.MDS_VARIABLES().zutiMisc_MaxAllowedKIA) return false;
            // if ( (World.isPlayerDead() || World.isPlayerParatrooper() || World.isPlayerCaptured() || World.isPlayerRemoved()) && com.maddox.il2.ai.World.ZUTI_KIA_DELAY_CLEARED )
            if (ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED) return true;
            else return false;
        }
        // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        if (Main.cur().netServerParams.isCoop()) return false;
        if (!Actor.isValid(World.getPlayerAircraft())) return true;
        if (!World.getPlayerAircraft().isAlive()) return true;
        if (!World.cur().diffCur.Takeoff_N_Landing) return true;
        if (World.getPlayerFM().Gears.onGround() && World.getPlayerAircraft().getSpeed(null) < 1.0) return true;
        if (World.isPlayerDead()) return true;
        if (World.isPlayerParatrooper()) return true;
        // TODO: Added by |ZUTI|
        // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.World.getPlayerAircraft();

        if (aircraft != null && aircraft.FM.brakeShoe) return true;

        // If player landed but damaged the gears... refly possible.
        if (aircraft != null && aircraft.FM.Gears.isAnyDamaged() && aircraft.FM.Gears.onGround()) return true;

        /*
         * if( aircraft != null && aircraft.FM.getAltitude() < 25.0F && aircraft.FM.getSpeedKMH() < 70.0F) return true;
         */
        if (aircraft != null) {
            Point3d pos = aircraft.pos.getAbsPoint();

            if (pos != null && World.isPlayerGunner() && aircraft.FM.getSpeedKMH() < ZutiSupportMethods_AI.MAX_SPEED_FOR_DECK_BAILOUT && aircraft.FM.getAltitude() < ZutiSupportMethods_AI.MAX_HEIGHT_FOR_DECK_BAILOUT && World.land().isWater(pos.x, pos.y))
                return true;
        }
        // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        return false;
    }

    public void _leave() {
        this.client.hideWindow();
    }

    public void enterPop(GameState gamestate) {
        if (gamestate.id() == 59) {
            this.client.hideWindow();
            GUI.unActivate();
        } else this.client.activateWindow();
    }

    protected void checkCaptured() {
        if (!World.isPlayerDead()) if (World.isPlayerParatrooper()) {
            if (Actor.isAlive(Actor.getByName("_paraplayer_"))) {
                Paratrooper paratrooper = (Paratrooper) Actor.getByName("_paraplayer_");
                if (!paratrooper.isChecksCaptured()) paratrooper.checkCaptured();
            }
        } else if (Actor.isAlive(World.getPlayerAircraft())) {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!aircraft.FM.isOk() && Front.isCaptured(aircraft)) {
                World.setPlayerCaptured();
                if (Config.isUSE_RENDER()) HUD.log("PlayerCAPT");
                Chat.sendLog(1, "gore_captured", (NetUser) NetEnv.host(), null);
            }
        }
    }

    protected void destroyPlayerActor() {
        if (Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerGunner()) {
            if (World.getPlayerAircraft().isAlive()) {
                Aircraft aircraft = World.getPlayerAircraft();
                Actor actor = World.getPlayerAircraft();
                FlightModel flightmodel = World.getPlayerAircraft().FM;
                boolean bool = false;
                if (flightmodel.isWasAirborne()) if (flightmodel.isStationedOnGround()) {
                    if (flightmodel.isNearAirdrome()) {
                        if (flightmodel.isTakenMortalDamage()) Chat.sendLogRnd(2, "gore_crashland", aircraft, null);
                        else Chat.sendLogRnd(2, "gore_lands", aircraft, null);
                    } else {
                        actor = aircraft.getDamager();
                        bool = true;
                        if (!World.isPlayerParatrooper()) if (Engine.cur.land.isWater(flightmodel.Loc.x, flightmodel.Loc.y)) Chat.sendLogRnd(2, "gore_swim", aircraft, null);
                        else Chat.sendLogRnd(2, "gore_ditch", aircraft, null);
                    }
                } else if (flightmodel.isTakenMortalDamage() || !flightmodel.isCapableOfBMP()) {
                    actor = aircraft.getDamager();
                    bool = true;
                }
                if (!Actor.isValid(actor)) actor = World.getPlayerAircraft();
                World.onActorDied(World.getPlayerAircraft(), actor, bool);
            }
            ObjState.destroy(World.getPlayerAircraft());
        }
        if (Actor.isAlive(Actor.getByName("_paraplayer_"))) Actor.getByName("_paraplayer_").setName(null);
    }

    public void doQuitMission() {
        GUI.activate();
        this.client.activateWindow();
        this.bEnableReFly = isEnableReFly();
        if (this.bEnableReFly) this.bReFly.showWindow();
        else this.bReFly.hideWindow();
    }

    protected void doReFly() {
        /* empty */
    }

    protected void doExit() {
        /* empty */
    }

    protected void clientRender() {
        /* empty */
    }

    protected void init(GWindowRoot gwindowroot) {
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = "";
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.bVideo = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.b3d = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.bSound = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.bControls = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.bNet = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.bReFly = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.bExit = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
        this.bBack = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 192.0F, 48.0F, 48.0F));
        this.bTrack = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }

    public GUINetMission(int i) {
        super(i);
    }
}