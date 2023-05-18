package com.maddox.il2.objects.air;

import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class F8F2 extends F8F {

    public F8F2() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && this.FM.Gears.onGround()) {
            if (this.FM.getSpeedKMH() < 20.0F) {
              this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
              this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 5; i++)
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);

        }
    }

    private float flapps;

    static {
        Class class1 = F8F2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F8F");
        Property.set(class1, "meshName", "3DO/Plane/F8F-2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F8F-2/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1959.5F);
        Property.set(class1, "noseart", 1);
        Property.set(class1, "FlightModel", "FlightModels/F8F-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF8F2.class });
        Property.set(class1, "LOSElevation", 1.16055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", "_ExternalDev04",
                "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
    }
}
