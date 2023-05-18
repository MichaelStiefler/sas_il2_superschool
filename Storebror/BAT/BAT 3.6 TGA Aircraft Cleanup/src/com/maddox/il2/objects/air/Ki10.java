package com.maddox.il2.objects.air;

import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class Ki10 extends Avia_B5xx {

    public Ki10() {
    }

    public void update(float f) {
        float f1 = Atmosphere.temperature((float) this.FM.Loc.z) - 273.15F;
        float f2 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = (((this.FM.EI.engines[0].getControlRadiator() * f * f2) / (f2 + 50F)) * (this.FM.EI.engines[0].tWaterOut - f1)) / 256F;
        this.FM.EI.engines[0].tWaterOut -= f3;
        float f4 = this.FM.EI.engines[0].getControlRadiator();
        f4 = (((-36F * f4) - 30F) * f4) + 30F;
        this.kangle = (0.95F * this.kangle) + (0.05F * f4);
        this.hierMesh().chunkSetAngles("radiator1_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator2_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator3_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator4_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator5_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator6_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator7_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator8_D0", 0.0F, this.kangle, 0.0F);
        super.update(f);
    }

    protected float kangle;
    public boolean  bChangedPit;
    float           suspR;
    float           suspL;
    static {
        Class class1 = Ki10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki10");
        Property.set(class1, "meshName", "3DO/Plane/Ki10(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki10(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Ki10.fmd:Ki10_FM");
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "cockpitClass", new Class[] { CockpitKi10.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
