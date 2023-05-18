package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Taube2 extends TaubeX implements TypeFighter, TypeScout, TypeBomber, TypeStormovik {

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
                float fMax = -10F;
                if (f < -120F) {
                    f = -120F;
                } else if (f < -70F) {
                    fMax = 0F;
                } else if ((f >= -70F) && (f < -65F)) {
                    fMax = Aircraft.cvt(f, -70F, -65F, 0F, -10F);
                } else if ((f >= -51.5F) && (f < -46.5F)) {
                    fMax = Aircraft.cvt(f, -51.5F, -46.5F, -10F, 35F);
                } else if ((f >= -46.5F) && (f < -11.75F)) {
                    fMax = Aircraft.cvt(f, -46.5F, -11.75F, 35F, 45F);
                } else if ((f >= -11.75F) && (f < 23F)) {
                    fMax = Aircraft.cvt(f, -11.75F, 23F, 45F, 35F);
                } else if ((f >= 23F) && (f < 28F)) {
                    fMax = Aircraft.cvt(f, 23F, 28F, 35F, -10F);
                } else if ((f > 55F) && (f <= 60F)) {
                    fMax = Aircraft.cvt(f, 55F, 60F, -10F, 0F);
                } else if (f > 60F) {
                    fMax = 0F;
                }
                if (f > 100F) {
                    f = 100F;
                }
                if (f1 < fMax) {
                    f1 = fMax;
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
