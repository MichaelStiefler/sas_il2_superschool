package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

public class Firefly_FR1 extends Firefly {

    public Firefly_FR1() {
        this.arrestor = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.isNet() && this.isNetMirror()) return;
        if (s.startsWith("Hook")) return;
        else {
            super.msgCollision(actor, s, s1);
            return;
        }
    }

    protected void moveFlap(float f) {
        float f1 = -85F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -12F * f, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        this.hierMesh().chunkSetLocate("Hook3_D0", Aircraft.xyz, Aircraft.ypr);
        this.arrestor = f;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.68F);
        this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.2F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            this.arrestor = 0.8F * this.arrestor + 0.2F * f1;
            this.moveArrestorHook(this.arrestor);
        } else {
            float f2 = -33F * this.FM.Gears.arrestorVSink / 57F;
            if (f2 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f2 > 0.0F && this.FM.CT.getArrestor() < 0.95F) f2 = 0.0F;
            if (f2 > 0.2F) f2 = 0.2F;
            if (f2 > 0.0F) this.arrestor = 0.7F * this.arrestor + 0.3F * (this.arrestor + f2);
            else this.arrestor = 0.3F * this.arrestor + 0.7F * (this.arrestor + f2);
            if (this.arrestor < 0.0F) this.arrestor = 0.0F;
            else if (this.arrestor > 1.0F) this.arrestor = 1.0F;
            this.moveArrestorHook(this.arrestor);
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -112F * f, 0.0F);
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, -112F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -112F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -112F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
        AircraftTools.updateExternalWeaponHooks(this);
//        if (f < 0.001F) {
//            this.setGunPodsOn(true);
//            this.hideWingWeapons(false);
//        } else {
//            this.setGunPodsOn(false);
//            this.FM.CT.WeaponControl[0] = false;
//            this.hideWingWeapons(true);
//        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.CT.bHasArrestorControl = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    private float arrestor;

    static {
        Class class1 = Firefly_FR1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Firefly");
        Property.set(class1, "meshName", "3DO/Plane/FireflyFR1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Firefly.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFirefly.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 3, 9, 9, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON03", "_CANNON04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev04",
                "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev08" });
    }
}
