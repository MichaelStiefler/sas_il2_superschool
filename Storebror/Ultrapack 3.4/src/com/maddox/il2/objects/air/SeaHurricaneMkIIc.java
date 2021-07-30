package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class SeaHurricaneMkIIc extends Hurricane {

    public SeaHurricaneMkIIc() {
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
        if ((!this.isNet() || !this.isNetMirror()) && !s.startsWith("Hook")) super.msgCollision(actor, s, s1);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57F * f, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        this.arrestor = f;
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("CannonL_D0", false);
            this.hierMesh().chunkVisible("CannonR_D0", false);
        }
        if (this.getBulletEmitterByHookName("_ExternalDev01") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb01") instanceof GunEmpty) this.hierMesh().chunkVisible("PylonL_D0", false);
        if (this.getBulletEmitterByHookName("_ExternalDev02") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb02") instanceof GunEmpty) this.hierMesh().chunkVisible("PylonR_D0", false);
        super.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 1.0F;
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

    private float         arrestor;
    public static boolean bChangedPit = false;

    static {
        Class class1 = SeaHurricaneMkIIc.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/SeaHurricaneMkIIc(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/SeaHurricaneMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSEAHURRICANE2.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01",
                        "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock06", "_ExternalRock06", "_ExternalRock05", "_ExternalRock05", "_ExternalRock04", "_ExternalRock04", "_ExternalRock07",
                        "_ExternalRock07", "_ExternalRock08", "_ExternalRock08" });
    }
}
