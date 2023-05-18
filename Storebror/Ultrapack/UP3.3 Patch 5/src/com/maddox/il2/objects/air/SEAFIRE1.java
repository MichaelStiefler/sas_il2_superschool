package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class SEAFIRE1 extends SPITFIRE {

    public SEAFIRE1() {
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
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, (float) Math.PI));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
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
        Class class1 = SEAFIRE1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Seafire");
        Property.set(class1, "meshName", "3DO/Plane/SeafireMkI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SeafireI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSea2.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01" });
    }
}
