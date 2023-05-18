package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class TU_116 extends TU_95 {

    public TU_116() {
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

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TU_95.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TU_95.bChangedPit = true;
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
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 73F) {
                    f1 = 73F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -38F) {
                    f = -38F;
                    flag = false;
                }
                if (f > 38F) {
                    f = 38F;
                    flag = false;
                }
                if (f1 < -41F) {
                    f1 = -41F;
                    flag = false;
                }
                if (f1 > 43F) {
                    f1 = 43F;
                    flag = false;
                }
                break;

            case 3:
                if (f < -85F) {
                    f = -85F;
                    flag = false;
                }
                if (f > 22F) {
                    f = 22F;
                    flag = false;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                    flag = false;
                }
                if (f1 > 32F) {
                    f1 = 32F;
                    flag = false;
                }
                break;

            case 4:
                if (f < -34F) {
                    f = -34F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 32F) {
                    f1 = 32F;
                    flag = false;
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
                break;

            case 4:
                this.FM.turret[2].bIsOperable = false;
                break;

            case 5:
                this.FM.turret[3].bIsOperable = false;
                this.FM.turret[4].bIsOperable = false;
                break;
        }
    }

    static {
        Class class1 = TU_116.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tu116");
        Property.set(class1, "meshName", "3DO/Plane/Tu-116/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1947F);
        Property.set(class1, "yearExpired", 1960F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitTU126.class });
        Property.set(class1, "FlightModel", "FlightModels/TU_95FM.fmd:TU95_FM");
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12" });
    }
}
