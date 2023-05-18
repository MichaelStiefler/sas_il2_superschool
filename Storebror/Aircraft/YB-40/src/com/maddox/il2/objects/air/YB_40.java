package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class YB_40 extends YB_40abc {

    public YB_40() {
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.killPilot(this, 4);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 11; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
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
                if (f1 < Aircraft.cvt(f2, 125F, 180F, -1F, 30F)) {
                    f1 = Aircraft.cvt(f2, 125F, 180F, -1F, 30F);
                }
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
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = YB_40.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "YB-40");
        Property.set(class1, "meshName", "3DO/Plane/YB-40(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 1946.9F);
        Property.set(class1, "FlightModel", "FlightModels/YB-40.fmd:YB40_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYB40.class, CockpitYB40_FGunner.class, CockpitYB40_FLGunner.class, CockpitYB40_FRGunner.class, CockpitYB40_TGunner.class, CockpitYB40_TGunner2.class, CockpitYB40_AGunner.class, CockpitYB40_LGunner.class, CockpitYB40_RGunner.class, CockpitYB40_BGunner.class });
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 13, 13, 14, 14, 16, 16, 15, 15, 17, 17, 18, 18 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN10a", "_MGUN09", "_MGUN09a", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 16;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 1100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 1100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 1200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 1200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 500);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 500);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(16, "MGunBrowning50t", 600);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(16, "MGunBrowning50t", 600);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 600);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 600);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(17, "MGunBrowning50t", 600);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(17, "MGunBrowning50t", 600);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(18, "MGunBrowning50t", 1200);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(18, "MGunBrowning50t", 1200);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "reduced_ammo";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 400);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 400);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 400);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 400);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 500);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 500);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(16, "MGunBrowning50t", 600);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(16, "MGunBrowning50t", 600);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 600);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 600);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(17, "MGunBrowning50t", 500);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(17, "MGunBrowning50t", 500);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(18, "MGunBrowning50t", 400);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(18, "MGunBrowning50t", 400);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
        }
    }
}
