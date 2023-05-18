package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class SpitfireMKVc4xHLand extends SPITFIRE {

    public SpitfireMKVc4xHLand() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
    }

    protected void moveGear(float f) {
        SpitfireMKVc4xHLand.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    static {
        Class var_class = SpitfireMKVc4xHLand.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "Spit");
        Property.set(var_class, "meshName", "3DO/Plane/SpitfireMKVc4xHLand(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(var_class, "meshName_gb", "3DO/Plane/SpitfireMKVc4xHLand(GB)/hier.him");
        Property.set(var_class, "PaintScheme_gb", new PaintSchemeFMPar04());
        Property.set(var_class, "yearService", 1943F);
        Property.set(var_class, "yearExpired", 1946.5F);
        Property.set(var_class, "FlightModel", "FlightModels/SpitfireVIII.fmd");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitSpit5C.class });
        Property.set(var_class, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 1, 1, 1, 1 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04" });
    }
}
