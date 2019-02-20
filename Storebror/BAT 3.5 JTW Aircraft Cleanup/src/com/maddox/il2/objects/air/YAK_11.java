package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class YAK_11 extends YAK_7A
    implements TypeFighter, TypeTNBFighter, TypeScout, TypeStormovik
{

    public YAK_11()
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
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.45F, 0.0F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Stvorka01_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka02_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka03_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka04_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka05_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka06_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka07_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka08_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka09_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka10_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka11_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka12_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka13_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka14_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka15_D0", 0.0F, -109F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Stvorka16_D0", 0.0F, -109F * kangle, 0.0F);
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        super.update(f);
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunUBsi", 200);
        }
        catch(Exception exception) { }
        return a_lweaponslot;
    }

    private float kangle;
    public static boolean bChangedPit = false;

    static 
    {
        Class class1 = YAK_11.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/YAK-11/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1960F);
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitYAK_11per.class, CockpitYAK_11zad.class
        });
        Property.set(class1, "FlightModel", "FlightModels/Yak-11.fmd:gui/game/Yak11_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_ExternalDev01", "_ExternalDev02"
        });
    }
}
