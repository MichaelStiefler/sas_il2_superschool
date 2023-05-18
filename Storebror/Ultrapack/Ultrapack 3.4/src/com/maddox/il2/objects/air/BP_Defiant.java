package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BP_Defiant extends BP_Defiantxyz implements TypeTNBFighter {

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -135F) f = -135F;
                if (f > 135F) f = 135F;
                if (f1 < -69F) {
                    f1 = -69F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                float f2;
                for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F)
                    ;
                if (f1 < -Aircraft.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af)) f1 = -Aircraft.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af);
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask2_D0", false);
        else this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    static {
        Class class1 = BP_Defiant.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Defiant");
        Property.set(class1, "meshName", "3DO/Plane/BPDefiant(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/DefiantMkI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDefiant.class, CockpitDefiant_AGunner.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 10, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
