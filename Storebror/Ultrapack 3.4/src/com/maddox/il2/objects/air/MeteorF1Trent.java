package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.rts.Property;

public class MeteorF1Trent extends MeteorF3 {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.engineVectors = new Vector3f[this.FM.EI.getNum()];
        for (int engineNum = 0; engineNum < this.FM.EI.getNum(); engineNum++) {
            this.engineVectors[engineNum] = new Vector3f();
            this.engineVectors[engineNum].set(this.FM.EI.engines[engineNum].getEngineVector());
        }
    }

    public void update(float f) {
        super.update(f);
        for (int engineNum = 0; engineNum < this.FM.EI.getNum(); engineNum++) {
            float powerMultiplier = cvt(this.FM.getSpeedKMH(), 0F, 600F, 3.0F, 1.0F) * cvt(this.FM.getAltitude(), 4000F, 7000F, 1.0F, 0.0F);
            if (powerMultiplier < 1.0F)
                powerMultiplier = 1.0F;
            this.FM.EI.engines[engineNum].getEngineVector().set(this.engineVectors[engineNum]);
            this.FM.EI.engines[engineNum].getEngineVector().scale(powerMultiplier);
        }
    }

    private Vector3f[] engineVectors;

    static {
        Class class1 = MeteorF1Trent.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Meteor");
        Property.set(class1, "meshName", "3DO/Plane/MeteorF1Trent/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/MeteorF1Trent.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMeteor.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02",
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12",
            "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalRock17",
            "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17" });
    }
}
