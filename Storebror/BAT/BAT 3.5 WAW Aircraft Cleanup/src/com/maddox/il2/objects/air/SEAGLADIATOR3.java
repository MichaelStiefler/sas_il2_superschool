package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public class SEAGLADIATOR3 extends SEAGLADIATOR {

    public SEAGLADIATOR3() {
        this.arrestor = 0.0F;
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((!this.isNet() || !this.isNetMirror()) && !s.startsWith("Hook")) {
            super.msgCollision(actor, s, s1);
        }
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57F * f, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        this.arrestor = f;
    }

    public void onAircraftLoaded() {
        if (this.thisWeaponsName.startsWith("default")) {
            this.hierMesh().chunkVisible("LewisL", true);
            this.hierMesh().chunkVisible("LewisR", true);
        } else {
            this.hierMesh().chunkVisible("LewisL", false);
            this.hierMesh().chunkVisible("LewisR", false);
        }
        if (this.thisWeaponsName.startsWith("RP-3")) {
            this.hierMesh().chunkVisible("RackL", true);
            this.hierMesh().chunkVisible("RackR", true);
        } else {
            this.hierMesh().chunkVisible("RackL", false);
            this.hierMesh().chunkVisible("RackR", false);
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
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
        Class class1 = SEAGLADIATOR3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gladiator");
        Property.set(class1, "meshName", "3DO/Plane/SeaGladiatorMkIIMOD(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSEAGLAD());
        Property.set(class1, "meshName_gb", "3DO/Plane/SeaGladiatorMkIIMOD(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeSEAGLAD());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/SeaGladiatorMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSEAGLADIATOR3.class });
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
