package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class OH58 extends HueyX implements TypeScout, TypeTransport {

    public OH58() {
        this.hoverThrustFactor1 = 1.05F;
        this.hoverThrustFactor2 = 1.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_MGUN03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("miniguns", false);
        }
        if ((this.getBulletEmitterByHookName("_ExternalRock01") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock02") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock03") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock04") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock05") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock06") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock07") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock08") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock09") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock10") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock11") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock12") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("Rocklaunch", false);
        }
        if ((this.getBulletEmitterByHookName("_MGUN03") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalRock01") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("Mount", false);
        }
        if (this.getBulletEmitterByHookName("_MGUN01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("M60L", false);
            this.hierMesh().chunkVisible("M60R", false);
            this.hierMesh().chunkVisible("Gunmount", false);
            this.hierMesh().chunkVisible("Gunmount2", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
        }
        if (World.cur().camouflage == 2) {
            this.hierMesh().chunkVisible("filter", true);
        } else {
            this.hierMesh().chunkVisible("filter", false);
        }
    }

    public void computeMass() {
        this.FM.M.massEmpty = Aircraft.cvt(this.FM.getSpeedKMH(), 100F, 180F, 2365F, 3000F);
    }

    static {
        Class class1 = OH58.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "OH58");
        Property.set(class1, "meshName", "3DO/Plane/OH-58/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/UH-1D.fmd:Huey_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHuey1B.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 9, 1, 1, 2, 2, 2, 2, 9, 9, 1, 1, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev12", "_ExternalDev13", "_MGUN09", "_MGUN10", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19" });
    }
}
