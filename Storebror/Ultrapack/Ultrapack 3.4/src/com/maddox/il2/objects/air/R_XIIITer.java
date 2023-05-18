package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class R_XIIITer extends R_XIIIxyz
{
    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 4F, 0.0F, 4F);
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, 55F * f, 0.0F);
        Aircraft.xyz[2] = -0.63F * f;
        hierMesh().chunkSetLocate("GearL5_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 4F, 0.0F, 4F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -55F * f, 0.0F);
        Aircraft.xyz[2] = -0.63F * f;
        hierMesh().chunkSetLocate("GearR5_D0", Aircraft.xyz, Aircraft.ypr);
    }

    static 
    {
        Class class1 = R_XIIITer.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "R.XIII");
        Property.set(class1, "meshName", "3DO/Plane/R-XIIITer(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_pl", "3DO/Plane/R-XIIITer/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1939.5F);
        Property.set(class1, "FlightModel", "FlightModels/R-XIIID.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitR_XIIITer.class, CockpitR_XIIITer_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01"
        });
    }
}
