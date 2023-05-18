package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class B_17F extends B_17 implements TypeBomber {

    public B_17F() {
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
        for (int i = 1; i < 7; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            B_17.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            B_17.bChangedPit = true;
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
                if (f < -23F) {
                    f = -23F;
                    flag = false;
                }
                if (f > 23F) {
                    f = 23F;
                    flag = false;
                }
                if (f1 < -15F) {
                    f1 = -15F;
                    flag = false;
                }
                if (f1 > 10F) {
                    f1 = 10F;
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
                if (f < -70F) {
                    f = -70F;
                    flag = false;
                }
                if (f > 54F) {
                    f = 54F;
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
                if (f < -63F) {
                    f = -63F;
                    flag = false;
                }
                if (f > 15F) {
                    f = 15F;
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
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                break;

            case 3:
                this.FM.turret[1].setHealth(f);
                break;

            case 4:
                this.FM.turret[2].setHealth(f);
                break;

            case 5:
                this.FM.turret[3].setHealth(f);
                this.FM.turret[4].setHealth(f);
                break;
        }
    }

    static {
        Class class1 = B_17F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-17");
        Property.set(class1, "meshName", "3DO/Plane/B-17F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/B-17F(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar03());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-17F.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB17.class, CockpitB17F_Bombardier.class, CockpitB17F_FGunner.class, CockpitB17_TGunner.class, CockpitB17_RGunner.class, CockpitB17_LGunner.class, CockpitB17_AGunner.class, CockpitB17F_BGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 14, 15, 16, 17, 17, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN09", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02" });
    }
}
