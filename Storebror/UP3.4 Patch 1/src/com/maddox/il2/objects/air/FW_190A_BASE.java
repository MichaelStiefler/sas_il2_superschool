package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GunEmpty;

public class FW_190A_BASE extends FW_190 {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
            this.FM.M.massEmpty -= 22F;
        }
        if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) {
            this.FM.M.massEmpty -= 49F;
        }
        if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) {
            this.FM.M.massEmpty -= 49F;
        }
        if (!this.applyLoadoutVisibility) {
            return;
        }
        FW_190A_BASE.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
//        StringTokenizer package_parts = new StringTokenizer(aircraftClass.getName(), ".");
//        String package_part = "";
//        while (package_parts.hasMoreTokens()) package_part = package_parts.nextToken();
//        if (package_part.length() < 8) return;
//        package_part = package_part.substring(6);
//        if (package_part.startsWith("A8") || package_part.startsWith("A9") || package_part.startsWith("D")) return;

        boolean winter = Config.isUSE_RENDER() && (World.cur().camouflage == 1);
        hierMesh.chunkVisible("GearL5_D0", !winter);
        hierMesh.chunkVisible("GearR5_D0", !winter);

        String planeVersion = aircraftClass.getName().substring(33);
//        System.out.println("190 Version = " + planeVersion);
        if (planeVersion.startsWith("D")) {
            return;
        }

        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);
        if ((weaponSlotsRegistered == null) || (weaponSlotsRegistered.length < 2)) {
            return;
        }
        if (planeVersion.startsWith("A8R11")) {
            FW_190NEW.prepareWeapons(aircraftClass, hierMesh, thisWeaponsName);
            return;
        }
        if (planeVersion.startsWith("A8MSTL")) {
            hierMesh.chunkVisible("ETC_501", false);
            hierMesh.chunkVisible("7mmC_D0", weaponSlotsRegistered[0] != null);
            hierMesh.chunkVisible("7mmCowl_D0", weaponSlotsRegistered[0] == null);
            hierMesh.chunkVisible("GuncoverL_D0", false);
            hierMesh.chunkVisible("GuncoverR_D0", false);
            return;
        }
        if (planeVersion.startsWith("A8") || planeVersion.startsWith("A9")) {
            hierMesh.chunkVisible("ETC_501", weaponSlotsRegistered[4] != null || weaponSlotsRegistered[31] != null);
            hierMesh.chunkVisible("7mmC_D0", weaponSlotsRegistered[0] != null);
            hierMesh.chunkVisible("7mmCowl_D0", weaponSlotsRegistered[0] == null);
            hierMesh.chunkVisible("GuncoverL_D0", weaponSlotsRegistered[27] != null);
            hierMesh.chunkVisible("GuncoverR_D0", weaponSlotsRegistered[28] != null);
            return;
        }

        hierMesh.chunkVisible("7mmC_D0", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("7mmCowl_D0", weaponSlotsRegistered[0] == null);
        if (weaponSlotsRegistered.length < 6) {
            return;
        }
        if (hierMesh.chunkFindCheck("20mmL1_D0") > 0) {
            hierMesh.chunkVisible("20mmL1_D0", weaponSlotsRegistered[2] != null);
        }
        if (hierMesh.chunkFindCheck("20mmR1_D0") > 0) {
            hierMesh.chunkVisible("20mmR1_D0", weaponSlotsRegistered[3] != null);
        }
        hierMesh.chunkVisible("20mmL_D0", weaponSlotsRegistered[4] != null);
        hierMesh.chunkVisible("20mmR_D0", weaponSlotsRegistered[5] != null);
        if (weaponSlotsRegistered.length < 10) {
            return;
        }
        hierMesh.chunkVisible("Flap01_D0", weaponSlotsRegistered[8] == null);
        hierMesh.chunkVisible("Flap01Holed_D0", weaponSlotsRegistered[8] != null);
        hierMesh.chunkVisible("Flap04_D0", weaponSlotsRegistered[9] == null);
        hierMesh.chunkVisible("Flap04Holed_D0", weaponSlotsRegistered[9] != null);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 20F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        FW_190A_BASE.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        }
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f) {
        if (this.FM.isPlayers() && this.FM.EI.engines[0].getControlAfterburner()) {
            HUD.logRightBottom("Start- und Notleistung ENABLED!");
        }
        super.update(f);
    }

    boolean applyLoadoutVisibility = true;

}
