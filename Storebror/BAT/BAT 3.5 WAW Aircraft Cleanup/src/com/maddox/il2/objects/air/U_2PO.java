package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.objects.sounds.MotorSound;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.sound.SoundFX;

public class U_2PO extends U_2 {

    public U_2PO() {
        U_2PO.bChangedPit = true;
        this.lLightHook = new Hook[4];
        this.lightTime = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            U_2PO.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            U_2PO.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    protected void moveFan(float f) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            super.moveFan(f);
            float f1 = this.FM.CT.getAileron();
            float f2 = this.FM.CT.getElevator();
            float f3 = this.FM.CT.getPowerControl();
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, -12F * f1, Aircraft.cvt(-f2, -1F, 1.0F, -12F, 18F));
            this.hierMesh().chunkSetAngles("Pilotarm2R_D0", Aircraft.cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, (-20F + Aircraft.cvt(f1, -1F, 1.0F, 6F, -8F)) - (Aircraft.cvt(f2, -1F, 0.0F, -36F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            this.hierMesh().chunkSetAngles("Pilotarm1R_D0", 0.0F, 0.0F, 20F + Aircraft.cvt(f1, -1F, 1.0F, -16F, 14F) + Aircraft.cvt(f2, -1F, 0.0F, -62F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
            this.hierMesh().chunkSetAngles("Pilotarm2L_D0", 0.0F, 0.0F, -30F + (10F * f3));
            this.hierMesh().chunkSetAngles("Pilotarm1L_D0", 0.0F, 0.0F, 20F - (10F * f3));
            return;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {

        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasBrakeControl = false;
        this.replaceSounds();
        UserCfg usercfg = World.cur().userCfg;
        String s = usercfg.getWeapon("U-2PO");
        if (s.equals("cargo")) {
            U_2PO.bSecondPilot = true;
        } else {
            U_2PO.bSecondPilot = false;
        }
        if (U_2PO.bSecondPilot) {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.FM.setGCenter(-0.2F);
        } else {
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.FM.setGCenter(-0.1F);
        }
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof Bomb) {
                    this.hierMesh().chunkVisible("RackL_D0", true);
                    this.hierMesh().chunkVisible("RackR_D0", true);
                }
            }

        }
        String s1 = usercfg.getSkin("U-2PO");
        if (s1 == null) {
            return;
        }
        String s2 = "GearL1_D0";
        int j = this.hierMesh().chunkFind(s2);
        String s3 = "GearR1_D0";
        int k = this.hierMesh().chunkFind(s3);
        String s4 = "WheelDisc";
        int l = this.hierMesh().materialFindInChunk(s4, j);
        int i1 = this.hierMesh().materialFindInChunk(s4, k);
        if ((l > 0) && (i1 > 0)) {
            if (s1.indexOf("white_blue") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc3");
                this.hierMesh().materialReplace(i1, "WheelDsc3");
            } else if (s1.indexOf("white_red") > -1) {
                this.hierMesh().materialReplace(l, "WheelDisc");
                this.hierMesh().materialReplace(i1, "WheelDisc");
            } else if (s1.indexOf("blue_ins") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc1");
                this.hierMesh().materialReplace(i1, "WheelDsc1");
            } else if (s1.indexOf("blue_stripes") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc1");
                this.hierMesh().materialReplace(i1, "WheelDsc1");
            } else if (s1.indexOf("blue") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc1");
                this.hierMesh().materialReplace(i1, "WheelDsc1");
            } else if (s1.indexOf("mgreen") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc4");
                this.hierMesh().materialReplace(i1, "WheelDsc4");
            } else if (s1.indexOf("green") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc2");
                this.hierMesh().materialReplace(i1, "WheelDsc2");
            } else if (s1.indexOf("green_ins") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc2");
                this.hierMesh().materialReplace(i1, "WheelDsc2");
            } else if (s1.indexOf("U2-VS") > -1) {
                this.hierMesh().materialReplace(l, "WheelDsc4");
                this.hierMesh().materialReplace(i1, "WheelDsc4");
            } else {
                this.hierMesh().materialReplace(l, "WheelDisc");
                this.hierMesh().materialReplace(i1, "WheelDisc");
            }
        }
        if (s1.indexOf("mgreen") > -1) {
            int j1 = this.hierMesh().materialFind("Gloss1D0o");
            Mat mat = this.hierMesh().material(j1);
            if (mat != null) {
                try {
                    mat.set((byte) 22, 0.25F);
                } catch (Exception exception) {
                }
            }
        }
    }

    public void updateLLights() {
        this.pos.getRender(Actor._tmpLoc);
        if (this.lLight == null) {
            if (Actor._tmpLoc.getX() >= 1.0D) {
                this.lLight = new LightPointWorld[4];
                for (int i = 0; i < 4; i++) {
                    this.lLight[i] = new LightPointWorld();
                    this.lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    this.lLight[i].setEmit(0.0F, 0.0F);
                    try {
                        this.lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    } catch (Exception exception) {
                    }
                }

            }
        } else {
            for (int j = 0; j < 4; j++) {
                if (this.FM.AS.astateLandingLightEffects[j] != null) {
                    U_2PO.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, U_2PO.lLightLoc1);
                    U_2PO.lLightLoc1.get(U_2PO.lLightP1);
                    U_2PO.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, U_2PO.lLightLoc1);
                    U_2PO.lLightLoc1.get(U_2PO.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(U_2PO.lLightP1, U_2PO.lLightP2, U_2PO.lLightPL)) {
                        U_2PO.lLightPL.z++;
                        U_2PO.lLightP2.interpolate(U_2PO.lLightP1, U_2PO.lLightPL, 0.95F);
                        this.lLight[j].setPos(U_2PO.lLightP2);
                        float f = (float) U_2PO.lLightP1.distance(U_2PO.lLightPL);
                        float f1 = (f * 0.5F) + 60F;
                        float f2 = 0.7F - ((0.8F * f * this.lightTime) / 2000F);
                        this.lLight[j].setEmit(f2, f1);
                    } else {
                        this.lLight[j].setEmit(0.0F, 0.0F);
                    }
                    continue;
                }
                if (this.lLight[j].getR() != 0.0F) {
                    this.lLight[j].setEmit(0.0F, 0.0F);
                }
            }

        }
    }

    public void sfxWheels() {
        SoundFX soundfx = this.newSound("aircraft.csswheels", true);
        if (soundfx != null) {
            soundfx.setPosition(new Point3d(0.0D, 0.0D, -1.5D));
            soundfx.setParent(this.getRootFX());
        }
    }

    private void replaceSounds() {
        this.breakSounds();
        this.sndRoot = this.newSound("aircraft.csscommon", true);
        this.motorSnd[0] = new MotorSound(this.FM.EI.engines[0], this);
        this.sfxInit(0);
    }

    public static boolean   bChangedPit    = false;
    public static boolean   bSecondPilot   = false;
    public static boolean   bSecondCockpit = false;
    private LightPointWorld lLight[];
    private Hook            lLightHook[];
    private static Loc      lLightLoc1     = new Loc();
    private static Point3d  lLightP1       = new Point3d();
    private static Point3d  lLightP2       = new Point3d();
    private static Point3d  lLightPL       = new Point3d();
    private float           lightTime;

    static {
        Class class1 = U_2PO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3DO/Plane/U-2PO/PlaneU2PO.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1967F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitU2PF.class, CockpitU2PR.class });
        Property.set(class1, "FlightModel", "FlightModels/U-2CSS.fmd:U2CSS");
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01" });
    }
}
