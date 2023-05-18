package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.sound.AudioStream;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class MS_500 extends Scheme1 implements TypeScout, TypeTransport {

    public MS_500() {
        this.lLightHook = new Hook[4];
        this.lightTime = 0.0F;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 5F);
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, AircraftLH.floatindex(f, MS_500.gearL2), 0.0F);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, AircraftLH.floatindex(f, MS_500.gearL4), 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, AircraftLH.floatindex(f, MS_500.gearL5), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 0.5F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 5F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -AircraftLH.floatindex(f, MS_500.gearL2), 0.0F);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, -AircraftLH.floatindex(f, MS_500.gearL4), 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, -AircraftLH.floatindex(f, MS_500.gearL5), 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -70F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveFan(float f) {
        super.moveFan(-f);
    }

    public void sfxFlaps(boolean flag) {
        if (this.sndFlapsNew != null) {
            this.sndFlapsNew.setPlay(flag);
        }
    }

    public void sfxWheels() {
        SoundFX soundfx = this.newSound("aircraft.mswheels", true);
        if (soundfx != null) {
            soundfx.setPosition(new Point3d(0.0D, 0.0D, -1.5D));
            soundfx.setParent(this.getRootFX());
        }
    }

    private void sfxInitNew() {
        if (this.sndRoot != null) {
            SectFile sectfile = new SectFile("presets/sounds/aircraft.ms.prs");
            this.sndFlapsNew = (new Sample(sectfile, "flaps")).get();
            if (this.sndFlapsNew != null) {
                System.out.println("*** Flaps sound loaded");
                this.sndRoot.add(this.sndFlapsNew);
            }
        }
    }

    public void destroy() {
        if (this.isDestroyed()) {
            return;
        }
        super.destroy();
        if (this.sndFlapsNew != null) {
            this.sndFlapsNew.cancel();
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.sfxInitNew();
        UserCfg usercfg = World.cur().userCfg;
        String s = usercfg.getSkin("MS-500");
        if ((s != null) && (s.indexOf("MRAZ") > -1) && (s.indexOf("white") > -1)) {
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Head2_D0", false);
            this.hierMesh().chunkVisible("HMask22_D0", false);
            this.hierMesh().chunkVisible("RearSeat", false);
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
                    MS_500.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, MS_500.lLightLoc1);
                    MS_500.lLightLoc1.get(MS_500.lLightP1);
                    MS_500.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, MS_500.lLightLoc1);
                    MS_500.lLightLoc1.get(MS_500.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(MS_500.lLightP1, MS_500.lLightP2, MS_500.lLightPL)) {
                        MS_500.lLightPL.z++;
                        MS_500.lLightP2.interpolate(MS_500.lLightP1, MS_500.lLightPL, 0.95F);
                        this.lLight[j].setPos(MS_500.lLightP2);
                        float f = (float) MS_500.lLightP1.distance(MS_500.lLightPL);
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

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Pilot2")) {
            this.killPilot(shot.initiator, 1);
        }
        if (shot.chunkName.startsWith("Engine") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        super.msgShot(shot);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);

            case 37:
                return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void doKillPilot(int i) {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;
        }
    }

    private static final float gearL2[]   = { 0.0F, 1.0F, 2.0F, 2.9F, 3.2F, 3.35F };
    private static final float gearL4[]   = { 0.0F, 7.5F, 15F, 22F, 29F, 35.5F };
    private static final float gearL5[]   = { 0.0F, 1.5F, 4F, 7.5F, 10F, 11.5F };
    protected AudioStream      sndFlapsNew;
    private LightPointWorld    lLight[];
    private Hook               lLightHook[];
    private static Loc         lLightLoc1 = new Loc();
    private static Point3d     lLightP1   = new Point3d();
    private static Point3d     lLightP2   = new Point3d();
    private static Point3d     lLightPL   = new Point3d();
    private float              lightTime;

    static {
        Class class1 = MS_500.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MS-500");
        Property.set(class1, "meshName", "3do/plane/MS-500/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/MS500.fmd:MS500");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMS_500.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
