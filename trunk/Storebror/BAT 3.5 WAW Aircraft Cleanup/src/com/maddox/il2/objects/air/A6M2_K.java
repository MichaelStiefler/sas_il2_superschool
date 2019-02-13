package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class A6M2_K extends A6M_TEN {

    public A6M2_K() {
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
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
        super.onAircraftLoaded();
        if (super.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister0", false);
                this.hierMesh().chunkVisible("Blister1_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister0", true);
                this.hierMesh().chunkVisible("Blister1_D0", true);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19: // '\023'
                this.FM.CT.bHasArrestorControl = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = A6M2_K.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M2-K(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/A6M2-K(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M2K.fmd:A6M2K_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA6M2K.class, CockpitA6M2KT.class });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
