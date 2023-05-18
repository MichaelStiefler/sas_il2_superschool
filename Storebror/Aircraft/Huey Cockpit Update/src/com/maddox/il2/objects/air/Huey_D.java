package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Huey_D extends HueyX implements TypeScout, TypeTransport {

    public Huey_D() {
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
        final Class class1 = Huey_D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Huey_D");
        Property.set(class1, "meshName", "3DO/Plane/Huey_D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/UH-1D.fmd:Huey_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHuey1B.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 11, 11, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev01", "_ExternalDev02" });
        try {
            final ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            final HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            final byte byte0 = 18;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Gunners&Wounded";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "stretcherRX", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "stretcherLX", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Miniguns";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Rockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Miniguns&Rockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (final Exception exception) {
        }
    }
}
