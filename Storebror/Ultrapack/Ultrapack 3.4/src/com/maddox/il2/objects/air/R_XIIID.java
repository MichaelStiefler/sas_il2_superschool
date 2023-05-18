package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class R_XIIID extends R_XIIIxyz {
    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, 55F * f, 0.0F);
        Aircraft.xyz[2] = -0.63F * f;
        this.hierMesh().chunkSetLocate("GearL5_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -55F * f, 0.0F);
        Aircraft.xyz[2] = -0.63F * f;
        this.hierMesh().chunkSetLocate("GearR5_D0", Aircraft.xyz, Aircraft.ypr);
    }

    static {
        Class class1 = R_XIIID.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "R.XIII");
        Property.set(class1, "meshName", "3DO/Plane/R-XIIID(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_pl", "3DO/Plane/R-XIIID/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1939.5F);
        Property.set(class1, "FlightModel", "FlightModels/R-XIIID.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitR_XIIID.class, CockpitR_XIIID_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
