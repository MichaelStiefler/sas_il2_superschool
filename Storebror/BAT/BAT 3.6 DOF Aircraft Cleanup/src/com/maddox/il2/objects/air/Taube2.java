package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Taube2 extends TaubeX implements TypeFighter, TypeScout, TypeBomber, TypeStormovik {

    public Taube2() {
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                float f2 = -10F;
                if (f < -147F) {
                    f = -147F;
                } else if (f < -97F) {
                    f2 = 0.0F;
                } else if ((f >= -97F) && (f < -92F)) {
                    f2 = Aircraft.cvt(f, -97F, -92F, 0.0F, -10F);
                } else if ((f >= -78.5F) && (f < -73.5F)) {
                    f2 = Aircraft.cvt(f, -78.5F, -73.5F, -10F, 35F);
                } else if ((f >= -73.5F) && (f < -38.75F)) {
                    f2 = Aircraft.cvt(f, -73.5F, -38.75F, 35F, 45F);
                } else if ((f >= -38.75F) && (f < -4F)) {
                    f2 = Aircraft.cvt(f, -38.75F, -4F, 45F, 35F);
                } else if ((f >= -4F) && (f < 1.0F)) {
                    f2 = Aircraft.cvt(f, -4F, 1.0F, 35F, -10F);
                } else if ((f > 28F) && (f <= 33F)) {
                    f2 = Aircraft.cvt(f, 28F, 33F, -10F, 0.0F);
                } else if (f > 33F) {
                    f2 = 0.0F;
                }
                if (f > 73F) {
                    f = 73F;
                }
                if (f1 < f2) {
                    f1 = f2;
                }
                if (f1 > 80F) {
                    f1 = 80F;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    static {
        Class class1 = Taube2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Taube");
        Property.set(class1, "meshName", "3do/plane/Taube2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/Taube.fmd:Taube_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTaube2_PIL.class, CockpitTaube2_Ob.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalBomb01", "_MGUN02" });
    }
}
