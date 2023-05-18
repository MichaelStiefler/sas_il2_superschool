
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.*;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class DC_10_10F extends DC_10family
{
    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "DC10F_";
    }

    public DC_10_10F()
    {
        bHasCenterGear = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasSideDoor = true;
    }

    public void msgShot(Shot shot)
    {
        super.msgShot(shot);
        if(FM.AS.astateEngineStates[0] > 2 && FM.AS.astateEngineStates[1] > 2 && FM.AS.astateEngineStates[2] > 2)
            FM.setCapableOfBMP(false, shot.initiator);
    }

    public static void moveGear(HierMesh hiermesh, float fL, float fR, float fC)
    {
        hiermesh.chunkSetAngles("GearC_CoverL2", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, -85F));
        hiermesh.chunkSetAngles("GearC_CoverR2", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, 85F));
        if(fC < 0.10F)
        {
            hiermesh.chunkSetAngles("GearC_CoverL1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, -85F));
            hiermesh.chunkSetAngles("GearC_CoverR1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, 85F));
        }
        if(fC > 0.90F)
        {
            hiermesh.chunkSetAngles("GearC_CoverL1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.91F, 0.98F, -85F, 0.0F));
            hiermesh.chunkSetAngles("GearC_CoverR1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.91F, 0.98F, 85F, 0.0F));
        }

        if(fL < 0.10F)
            hiermesh.chunkSetAngles("GearL_CoverC", 0.0F, 0.0F, Aircraft.cvt(fL, 0.02F, 0.09F, 0.0F, 88F));
        if(fL > 0.90F)
            hiermesh.chunkSetAngles("GearL_CoverC", 0.0F, 0.0F, Aircraft.cvt(fL, 0.92F, 0.99F, 88F, 0.0F));
        hiermesh.chunkSetAngles("GearL_CoverW", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -90F));

        if(fR < 0.10F)
            hiermesh.chunkSetAngles("GearR_CoverC", 0.0F, 0.0F, Aircraft.cvt(fR, 0.02F, 0.09F, 0.0F, -88F));
        if(fR > 0.90F)
            hiermesh.chunkSetAngles("GearR_CoverC", 0.0F, 0.0F, Aircraft.cvt(fR, 0.92F, 0.99F, -88F, 0.0F));
        hiermesh.chunkSetAngles("GearR_CoverW", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 90F));

        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(fC, 0.15F, 0.85F, 0.0F, -92F), 0.0F);

        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -5.0F));
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, 0.514681F);
        hiermesh.chunkSetLocate("GearL42_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL43_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -46.306F));
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, 164.923F));

        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 90F));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 5.0F));
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 0.514681F);
        hiermesh.chunkSetLocate("GearR42_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR43_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 46.306F));
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, -164.923F));

        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fL, 0.80F, 0.98F, 0.0F, -0.455245F);
        hiermesh.chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fR, 0.80F, 0.98F, 0.0F, -0.455245F);
        hiermesh.chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    private static void resetXYZYPR()
    {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    static
    {
        Class class1 = DC_10_10F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/DC10-10fake.fmd:KC10fakeFM");
        Property.set(class1, "meshName", "3do/plane/KC-10(Multi1)/hierDC1010F.him");
        Property.set(class1, "iconFar_shortClassName", "DC-10");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1984F);
        Property.set(class1, "yearExpired", 2025F);
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitKC_10.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_BombSpawn01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = 1;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
        }
    }
}
