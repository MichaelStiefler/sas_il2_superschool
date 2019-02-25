package com.maddox.il2.objects.air;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.GunNull;
import com.maddox.rts.Property;

public class F_86D40 extends F_86D {

    public F_86D40() {
    }

    public void update(float f) {
        super.update(f);
        this.computeJ47GE17B();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.setGunNullOwner();
        this.FM.CT.bHasBayDoors = true;
    }

    protected void moveBayDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 1.0F, 0.0F, -0.22F);
        this.hierMesh().chunkSetLocate("Launcher_D0", Aircraft.xyz, Aircraft.ypr);
    }

    private void setGunNullOwner() {
        try {
            for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
                if (this.FM.CT.Weapons[i] != null) {
                    for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                        if ((this.FM.CT.Weapons[i][j] != null) && (this.FM.CT.Weapons[i][j] instanceof GunNull)) {
                            ((GunNull) this.FM.CT.Weapons[i][j]).setOwner(this);
                        }
                    }

                }
            }

        } catch (Exception exception) {
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        Maneuver maneuver = (Maneuver) this.FM;
        F_86D40.targeted = War.GetNearestEnemyAircraft(((Interpolate) (this.FM)).actor, 2000F, 9);
        if (!this.FM.CT.Weapons[2][23].haveBullets()) {
            ((GunNull) this.FM.CT.Weapons[0][0]).emptyGun();
            ((GunNull) this.FM.CT.Weapons[1][0]).emptyGun();
        }
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            this.FM.CT.weaponFireMode = 2;
            if ((((Maneuver) this.FM).get_maneuver() == 3) || ((((Maneuver) this.FM).get_maneuver() == 63) && (maneuver.target != null) && (((Interpolate) (this.FM)).actor.pos.getAbsPoint().distance(F_86D40.targeted.pos.getAbsPoint()) < 1200D))) {
                this.FM.CT.BayDoorControl = 1.0F;
            } else if ((maneuver.target == null) || (((Interpolate) (this.FM)).actor.pos.getAbsPoint().distance(F_86D40.targeted.pos.getAbsPoint()) >= 1500D)) {
                this.FM.CT.BayDoorControl = 0.0F;
            }
        }
    }

    public void computeJ47GE17B() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 7000D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 10D) {
                f1 = 7F;
            } else {
                f1 = 0.7F * f;
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private static Actor targeted = null;

    static {
        Class class1 = F_86D40.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-86D");
        Property.set(class1, "meshName", "3DO/Plane/F_86D(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_us", "3DO/Plane/F_86D(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/F-86D-40.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_86K.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_InternalRock01", "_InternalRock02", "_InternalRock03", "_InternalRock04", "_InternalRock05", "_InternalRock06", "_InternalRock07", "_InternalRock08", "_InternalRock09", "_InternalRock10", "_InternalRock11", "_InternalRock12", "_InternalRock13", "_InternalRock14", "_InternalRock15", "_InternalRock16", "_InternalRock17", "_InternalRock18", "_InternalRock19", "_InternalRock20", "_InternalRock21", "_InternalRock22", "_InternalRock23", "_InternalRock24", "_MGUN01", "_MGUN02" });
    }
}
