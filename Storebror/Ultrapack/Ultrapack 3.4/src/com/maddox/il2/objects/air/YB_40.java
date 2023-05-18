package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class YB_40 extends YB_40abc {

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 11; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) YB_40.bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) YB_40.bChangedPit = true;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -70F) {
                    f = -70F;
                    flag = false;
                }
                if (f > 70F) {
                    f = 70F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -20F) {
                    f = -20F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -20F) {
                    f1 = -20F;
                    flag = false;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -20F) {
                    f = -20F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -20F) {
                    f1 = -20F;
                    flag = false;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                    flag = false;
                }
                break;

            case 3:
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 66F) {
                    f1 = 66F;
                    flag = false;
                }
                break;

            case 4:
                if (f1 < -75F) {
                    f1 = -75F;
                    flag = false;
                }
                if (f1 > 6F) {
                    f1 = 6F;
                    flag = false;
                }
                break;

            case 5:
                if (f < -32F) {
                    f = -32F;
                    flag = false;
                }
                if (f > 84F) {
                    f = 84F;
                    flag = false;
                }
                if (f1 < -17F) {
                    f1 = -17F;
                    flag = false;
                }
                if (f1 > 43F) {
                    f1 = 43F;
                    flag = false;
                }
                break;

            case 6:
                if (f < -80F) {
                    f = -80F;
                    flag = false;
                }
                if (f > 39F) {
                    f = 39F;
                    flag = false;
                }
                if (f1 < -28F) {
                    f1 = -28F;
                    flag = false;
                }
                if (f1 > 40F) {
                    f1 = 40F;
                    flag = false;
                }
                break;

            case 7:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 25F) {
                    f1 = 25F;
                    flag = false;
                }
                break;

            case 8:
                if (f1 > 89F) {
                    f1 = 89F;
                    flag = false;
                }
                float f2 = Math.abs(f);
                if (f1 < Aircraft.cvt(f2, 125F, 180F, -1F, 30F)) f1 = Aircraft.cvt(f2, 125F, 180F, -1F, 30F);
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[1].bIsOperable = false;
                this.FM.turret[2].bIsOperable = false;
                break;

            case 4:
                this.FM.turret[3].bIsOperable = false;
                break;

            case 5:
                this.FM.turret[4].bIsOperable = false;
                break;

            case 6:
                this.FM.turret[5].bIsOperable = false;
                break;

            case 7:
                this.FM.turret[6].bIsOperable = false;
                break;

            case 8:
                this.FM.turret[7].bIsOperable = false;
                break;

            case 9:
                this.FM.turret[8].bIsOperable = false;
                break;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = YB_40.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-40");
        Property.set(class1, "meshName", "3DO/Plane/YB-40(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 1946.9F);
        Property.set(class1, "FlightModel", "FlightModels/YB-40.fmd");
        Property.set(class1, "cockpitClass",
                new Class[] { CockpitYB40.class, CockpitYB40_FGunner.class, CockpitYB40_TGunner.class, CockpitYB40_TGunner2.class, CockpitYB40_LWGunner.class, CockpitYB40_RWGunner.class, CockpitYB40_AGunner.class, CockpitYB40_BGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 13, 13, 14, 14, 16, 16, 15, 15, 17, 17, 18, 18 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN11", "_MGUN12", "_MGUN09", "_MGUN10", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16" });
    }
}
