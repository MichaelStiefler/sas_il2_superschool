// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2017/11/19 3:05:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SU_26M2.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            IAR_8X, PaintSchemeFMPar01, TypeFighter, TypeTNBFighter, 
//            Cockpit, Aircraft, NetAircraft

public class SU_26M2 extends SU_26XX
    implements TypeFighter, TypeTNBFighter
{

    public SU_26M2()
    {
        kangle = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 110F * f);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Stvorka1_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka2_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka3_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka4_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka5_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka6_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka7_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka8_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka9_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka10_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka11_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka12_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka13_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka14_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka15_D0", 0.0F, -73F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka16_D0", 0.0F, -73F * kangle, 0.0F);
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        super.update(f);
    }

    private float kangle;
    public static boolean bChangedPit = false;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.SU_26M2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-26");
        Property.set(class1, "meshName", "3DO/Plane/SU_26/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1989F);
        Property.set(class1, "yearExpired", 2050F);
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSU26SAS.class
        });
        Property.set(class1, "FlightModel", "FlightModels/Su-26.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 1;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}