package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.MGunB20k;
import com.maddox.il2.objects.weapons.MGunM4k;
import com.maddox.il2.objects.weapons.MGunMG213C20s;
import com.maddox.il2.objects.weapons.MGunMK108k;
import com.maddox.il2.objects.weapons.MGunNS37k;
import com.maddox.rts.Property;

public class Dragonfly46 extends DragonflyX {

    public Dragonfly46() {
    }

    protected void moveRudder(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 0.0632F);
        if (this.FM.CT.getGear() > 0.99F) {
            Aircraft.ypr[1] = 40F * this.FM.CT.getRudder();
        }
        this.hierMesh().chunkSetLocate("GearC25_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearC27_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, -15F), 0.0F);
        this.hierMesh().chunkSetAngles("GearC28_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 30F), 0.0F);
        this.updateControlsVisuals();
    }

    protected void moveElevator(float f) {
        this.updateControlsVisuals();
    }

    private final void updateControlsVisuals() {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, (-21.25F * this.FM.CT.getElevator()) - (21.25F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, (-21.25F * this.FM.CT.getElevator()) + (21.25F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorL2_D0", 0.0F, (21.25F * this.FM.CT.getElevator()) + (0.0F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorR2_D0", 0.0F, (21.25F * this.FM.CT.getElevator()) - (0.0F * this.FM.CT.getRudder()), 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof MGunMK108k) {
            this.FM.M.massEmpty += 40F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunMG213C20s) {
            this.FM.M.massEmpty += 80F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunB20k) {
            this.FM.M.massEmpty -= 30F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunNS37k) {
            this.FM.M.massEmpty += 200F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunM4k) {
            this.FM.M.massEmpty += 120F;
        }
    }

    static {
        Class class1 = Dragonfly46.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly46/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly46.fmd:Dragonfly46_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly46.class });
        Property.set(class1, "LOSElevation", 0.5099F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
