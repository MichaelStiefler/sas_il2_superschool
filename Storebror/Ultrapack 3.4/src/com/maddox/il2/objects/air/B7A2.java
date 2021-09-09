package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class B7A2 extends B7A {

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -33F) {
                    f = -33F;
                    flag = false;
                }
                if (f > 33F) {
                    f = 33F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    public void doWoundPilot(int i, float f) {
        super.doWoundPilot(i, f);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    static {
        Class class1 = B7A2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B7A2");
        Property.set(class1, "meshName", "3DO/Plane/B7A2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/B7A2(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/B7A1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB7A1.class, CockpitB7A1_Bombardier.class, CockpitB7A1_TGunner.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 3, 9, 3, 9, 3, 3, 3, 9, 3, 3, 3, 9, 9, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_BombSpawn01", "_ExternalDev03", "_BombSpawn02", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev04", "_BombSpawn03", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_CANNON01", "_CANNON02" });
    }
}
