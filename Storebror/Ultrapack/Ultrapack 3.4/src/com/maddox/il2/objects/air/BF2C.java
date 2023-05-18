package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public class BF2C extends Hawk_3xyz {
    public BF2C() {
        this.arrestor = 0.0F;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -40F * f, 0.0F);
        this.arrestor = f;
    }

    public void update(float f) {
        super.update(f);
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
    }

    protected float arrestor;

    static {
        Class class1 = BF2C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BF2C");
        Property.set(class1, "meshName", "3DO/Plane/BF2C/BF2C_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Hawk3_nav.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHawk_3.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 3, 3, 9, 9, 3});
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb05" });
    }
}
