package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class LATE_298 extends LATE_xyz {
    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    LATE_298.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    LATE_298.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(LATE_298.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                if ((f > -2.5F) && (f < 2.5F) && (f1 < 10F)) {
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
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        if (i > 3) {
            return;
        } else {
            this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
            this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
            this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
            this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
            return;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.53F);
        this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.95F);
        this.hierMesh().chunkSetLocate("Blister8_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.1F);
        this.hierMesh().chunkSetLocate("Turret1A_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.1F);
        this.hierMesh().chunkSetLocate("TurretBase_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.02F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.05F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -45F);
        this.hierMesh().chunkSetLocate("Blister6_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("Blister5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 25F));
        this.hierMesh().chunkSetAngles("Turret1B_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 15F), 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    private static Point3d tmpp = new Point3d();

    static {
        Class class1 = LATE_298.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Late298.fmd");
        Property.set(class1, "meshName", "3DO/Plane/Late298/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Late298");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1937.5F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitLate298.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 3, 9, 3, 9, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN10", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev03", "_ExternalBomb03", "_ExternalDev04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15" });
    }
}
