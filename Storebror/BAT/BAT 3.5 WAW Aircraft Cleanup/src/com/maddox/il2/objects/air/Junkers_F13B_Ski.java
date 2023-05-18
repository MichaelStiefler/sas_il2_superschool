package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Junkers_F13B_Ski extends Junkers_F13 {

    public Junkers_F13B_Ski() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && (World.cur().camouflage == 1)) {
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL11_D0", true);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR11_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = Aircraft.cvt(this.FM.Or.getTangage(), -30F, 30F, -30F, 30F);
            if (this.FM.Gears.onGround() && (this.FM.CT.getGear() > 0.9F) && (this.FM.getSpeed() > 5F)) {
                if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                    this.hierMesh().chunkSetAngles("GearL11_D0", World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F) - f1);
                } else {
                    this.hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, -f1);
                }
                if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
                    this.hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F) - f1);
                } else {
                    this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -f1);
                }
            } else {
                this.hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, -f1);
                this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -f1);
            }
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    static {
        Class class1 = Junkers_F13B_Ski.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju.F13BSki");
        Property.set(class1, "meshName", "3DO/Plane/Junkers_F13B_Ski/F13Skihier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1923F);
        Property.set(class1, "yearExpired", 1957F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJunkers_F13B.class, CockpitJunkers_F13B_TGunner.class });
        Property.set(class1, "FlightModel", "FlightModels/JuF13.fmd:JuF13_FM");
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_BombSpawn01" });
    }
}
