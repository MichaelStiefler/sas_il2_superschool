package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;

public class J2F_Duck_Super extends Scheme1 implements TypeScout, TypeAmphibiousPlane {

    public J2F_Duck_Super() {
        this.startWithGearDown = false;
        J2F_Duck_Super.bChangedPit = true;
        this.arrestor = 0.0F;
        this.flapps = 0.0F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.6F);
        this.hierMesh().chunkSetLocate("GDblister1", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveAirBrake(float f) {
        this.moveGearAmf(f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, -30F * f, 0.0F);
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            J2F_Duck_Super.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            J2F_Duck_Super.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 5000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.CT.bHasArrestorControl = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -60F * f, 0.0F);
        this.arrestor = f;
    }

    public void update(float f) {
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-42F * this.FM.Gears.arrestorVSink) / 37F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
        super.update(f);
        if (this.startWithGearDown) {
            Reflection.setFloat(this.FM.CT, "airBrake", 1.0F);
            this.FM.CT.AirBrakeControl = 1.0F;
            this.startWithGearDown = false;
        }
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        if (!Engine.cur.land.isWater(point3d.x, point3d.y)) {
            this.moveGearAmf(1.0F);
            this.FM.CT.AirBrakeControl = 1.0F;
            this.startWithGearDown = true;
            point3d.z += 0.492422D;
            orient.increment(0.0F, 7.3528F, 0.0F);
        }
        super.setOnGround(point3d, orient, vector3d);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else {
                i = s.charAt(5) - 49;
            }
            this.hitFlesh(i, shot, byte0);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -31F) {
                    f = -31F;
                    flag = false;
                }
                if (f > 31F) {
                    f = 31F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 52F) {
                    f1 = 52F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void moveSteering(float f1) {
    }

    public static void moveGearAmf(HierMesh hiermesh, float f) {
        J2F_Duck_Super.moveGearAmf(hiermesh, Aircraft.cvt(f, 0.01F, 0.91F, 0.0F, 1.0F), 0, true);
        J2F_Duck_Super.moveGearAmf(hiermesh, Aircraft.cvt(f, 0.09F, 0.98F, 0.0F, 1.0F), 1, true);
    }

    protected static final void moveGearAmf(HierMesh hiermesh, float f, int i, boolean flag) {
        String s = i <= 0 ? "L" : "R";
        hiermesh.chunkSetAngles("Gear" + s + "2_D0", 0.0F, -87F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "3_D0", 0.0F, -90.5F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "5_D0", 0.0F, -110.5F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "2X_D0", 0.0F, -87F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "3X_D0", 0.0F, -115.5F * f, 0.0F);
    }

    protected void moveGearAmf(float f) {
        float f1 = Aircraft.cvt(f, 0.01F, 0.91F, 0.0F, 1.0F);
        J2F_Duck_Super.moveGearAmf(this.hierMesh(), f1, 0, true);
        J2F_Duck_Super.moveGearAmf(this.hierMesh(), f1, 1, true);
    }

    public static boolean bChangedPit = false;
    protected float       arrestor;
    protected float       flapps;
    private boolean       startWithGearDown;

    static {
        Class class1 = J2F_Duck_Super.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Duck");
        Property.set(class1, "meshName", "3DO/Plane/GrummanDuck/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/GrummanDuck.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDuckFront.class, CockpitDuck_TGunner.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09", "_ExternalBomb05" });
    }
}
