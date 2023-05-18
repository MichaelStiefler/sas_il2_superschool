
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class EA_6B extends A_6fuelReceiver
{

    public EA_6B()
    {
        ratdeg = 0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        super.fuelReceiveRate = 10.093F;
        if(thisWeaponsName.endsWith("__ALQ3"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_3", true);
            hierMesh().chunkVisible("ALQ99prop_3", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 482F * 3F;
            FM.Sq.dragProducedCx += 0.028F * 3F;
        }
        if(thisWeaponsName.endsWith("__ALQ4"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_2", true);
            hierMesh().chunkVisible("ALQ99prop_2", true);
            hierMesh().chunkVisible("ALQ99Body_4", true);
            hierMesh().chunkVisible("ALQ99prop_4", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 482F * 4F;
            FM.Sq.dragProducedCx += 0.028F * 4F;
        }
        if(thisWeaponsName.endsWith("__ALQ5"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_2", true);
            hierMesh().chunkVisible("ALQ99prop_2", true);
            hierMesh().chunkVisible("ALQ99Body_3", true);
            hierMesh().chunkVisible("ALQ99prop_3", true);
            hierMesh().chunkVisible("ALQ99Body_4", true);
            hierMesh().chunkVisible("ALQ99prop_4", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 482F * 5F;
            FM.Sq.dragProducedCx += 0.028F * 5F;
        }
    }

    public void update(float f)
    {
        if(FM.getSpeedKMH() > 185F)
            RATrot();

        super.update(f);
    }

    public void missionStarting()
    {
        super.missionStarting();
    }

    private void RATrot()
    {
        if(FM.getSpeedKMH() < 250F)
            ratdeg += 10F;
        else if(FM.getSpeedKMH() < 400F)
            ratdeg += 20F;
        else if(FM.getSpeedKMH() < 550F)
            ratdeg += 25F;
        else
            ratdeg += 31F;
        if(ratdeg > 720F) ratdeg -= 1440F;
        for(int i=1; i<6; i++)
        {
            hierMesh().chunkSetAngles("ALQ99prop_" + i, 0.0F, 0.0F, ratdeg);

            if(hierMesh().isChunkVisible("ALQ99Body_"+ i))
            {
                if(FM.getSpeedKMH() > 300F)
                {
                    hierMesh().chunkVisible("ALQ99proprot_" + i, true);
                    hierMesh().chunkVisible("ALQ99prop_" + i, false);
                }
                else
                {
                    hierMesh().chunkVisible("ALQ99proprot_" + i, false);
                    hierMesh().chunkVisible("ALQ99prop_" + i, true);
                }
            }
        }
    }

    private float ratdeg;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "EA-6B");
        Property.set(class1, "meshName", "3DO/Plane/EA-6B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 2002F);
        Property.set(class1, "FlightModel", "FlightModels/EA6B.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitEA_6B.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 
            2, 2, 2, 2, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev06",  "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36",
            "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01",        "_Chaff01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = 16;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[14] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkV2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkV2_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkV2_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300Dt+3xAN_ALQ99__ALQ3";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - EA_6B : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}