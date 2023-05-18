package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.rts.Property;

public class P_11F extends P_11 {

    public P_11F() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && World.cur().camouflage == 1) {
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL11_D0", true);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR11_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) for (int i = 0; i < aobj.length; i++)
            if (aobj[i] instanceof Bomb) {
                this.hierMesh().chunkVisible("RackL_D0", true);
                this.hierMesh().chunkVisible("RackR_D0", true);
            }
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = Aircraft.cvt(this.FM.Or.getTangage(), -30F, 30F, -30F, 30F);
            if (this.FM.Gears.onGround() && this.FM.CT.getGear() > 0.9F && this.FM.getSpeed() > 5F) {
                if (this.FM.Gears.gWheelSinking[0] > 0.0F) this.hierMesh().chunkSetAngles("GearL11_D0", World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F) - f1);
                else this.hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, -f1);
                if (this.FM.Gears.gWheelSinking[1] > 0.0F) this.hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F) - f1);
                else this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -f1);
            } else {
                this.hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, -f1);
                this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -f1);
            }
        }
    }

    static {
        Class class1 = P_11F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P.11");
        Property.set(class1, "meshName", "3DO/Plane/P-11f(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_ro", "3DO/Plane/P-11f(Romanian)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryRomania);
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-11f.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_11F.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
