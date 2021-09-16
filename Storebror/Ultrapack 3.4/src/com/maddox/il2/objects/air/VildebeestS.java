package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.rts.Property;

public class VildebeestS extends Biplanexyz implements TypeSailPlane, TypeStormovikArmored {

    public VildebeestS() {
        this.tmpp = new Point3d();
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("FSteerL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("FSteerR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    this.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    this.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(this.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    private Point3d tmpp;

    static {
        Class class1 = VildebeestS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "VildebeestS");
        Property.set(class1, "meshName", "3DO/Plane/Vildebeest/hierS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1931F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/VildebeestS.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitVildebeestS.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev11", "_ExternalBomb17" });
    }
}
