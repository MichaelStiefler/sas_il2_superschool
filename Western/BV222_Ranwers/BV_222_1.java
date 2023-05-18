
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Turret;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            BV_222, PaintSchemeBMPar02, TypeBomber, NetAircraft, 
//            PaintScheme, Aircraft

public class BV_222_1 extends BV_222
    implements TypeBomber
{

    public BV_222_1()
    {
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            if(f < -10F)
            {
                f = -10F;
                flag = false;
            }
            if(f > 60F)
            {
                f = 60F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;

        case 1: // '\001'
            if(f < -60F)
            {
                f = -60F;
                flag = false;
            }
            if(f > 10F)
            {
                f = 10F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;

        case 2: // '\002'
            if(f < -22F)
            {
                f = -22F;
                flag = false;
            }
            if(f > 22F)
            {
                f = 22F;
                flag = false;
            }
            if(f1 < -57F)
            {
                f1 = -57F;
                flag = false;
            }
            if(f1 > 33F)
            {
                f1 = 33F;
                flag = false;
            }
            break;

        case 3: // '\003'
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            if(f1 > 50F)
            {
                f1 = 50F;
                flag = false;
            }
            break;

        case 4: // '\004'
            if(f < -25F)
            {
                f = -25F;
                flag = false;
            }
            if(f > 25F)
            {
                f = 25F;
                flag = false;
            }
            if(f1 < -25F)
            {
                f1 = -25F;
                flag = false;
            }
            if(f1 > 25F)
            {
                f1 = 25F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2: // '\002'
            FM.turret[0].setHealth(f);
            break;

        case 3: // '\003'
            FM.turret[1].setHealth(f);
            FM.turret[2].setHealth(f);
            break;

        case 4: // '\004'
            FM.turret[3].setHealth(f);
            break;

        case 5: // '\005'
            FM.turret[4].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        if(i > 9)
        {
            return;
        } else
        {
            hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
            hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
            hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
            if(i == 0)  // Pilot2 ~ Pilot9 don't have "Head" msh
                hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
            return;
        }
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.air.BV_222.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BV-222");
        Property.set(class1, "meshName", "3Do/Plane/BV-222/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/BV-222.fmd:BV222FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitBV_222.class,           com.maddox.il2.objects.air.CockpitBV_222_NGunner.class,   com.maddox.il2.objects.air.CockpitBV_222_TGunner.class,
            com.maddox.il2.objects.air.CockpitBV_222_FLGunner.class,  com.maddox.il2.objects.air.CockpitBV_222_FRGunner.class,  com.maddox.il2.objects.air.CockpitBV_222_RLGunner.class, com.maddox.il2.objects.air.CockpitBV_222_RRGunner.class,
            com.maddox.il2.objects.air.CockpitBV_222_WiLGunner.class, com.maddox.il2.objects.air.CockpitBV_222_WiRGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 13, 14, 15, 16, 17, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalBomb01"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 9;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunMG131t", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunMG131t", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunMG131t", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(13, "MGunMG131t", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(14, "MGunMG15120t", 350);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(15, "MGunMG15120t", 350);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(16, "MGunMG15120t", 350);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(17, "MGunMG131t", 350);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "92xPara";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunMG131t", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunMG131t", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunMG131t", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(13, "MGunMG131t", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(14, "MGunMG15120t", 350);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(15, "MGunMG15120t", 350);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(16, "MGunMG15120t", 350);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(17, "MGunMG131t", 350);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunPara", 92);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xCargoA";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunMG131t", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunMG131t", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunMG131t", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(13, "MGunMG131t", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(14, "MGunMG15120t", 350);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(15, "MGunMG15120t", 350);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(16, "MGunMG15120t", 350);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(17, "MGunMG131t", 350);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 12);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int i = 0; i < byte0; i++)
                a_lweaponslot[i] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}