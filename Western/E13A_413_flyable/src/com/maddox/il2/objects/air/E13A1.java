
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;


public class E13A1 extends E13A
{

    public E13A1()
    {
    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(FM.Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    FM.Gears.clpGearEff[i][j].pos.reset();
                }

        }

    }

    private static Point3d tmpp = new Point3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.air.E13A1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/E13A1.fmd");
        Property.set(class1, "iconFar_shortClassName", "E13A");
        Property.set(class1, "meshName", "3DO/Plane/E13A(MULTI1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/E13A(JA)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1941.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitE13A1.class, com.maddox.il2.objects.air.CockpitE13A1_Bombardier.class, com.maddox.il2.objects.air.CockpitE13A1_Gunner.class
        });
        weaponTriggersRegister(class1, new int[] {
            10, 3, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02"
        });
        weaponsRegister(class1, "default", new String[] {
            "MGunVikkersKt 582", null, null, null, null
        });
        weaponsRegister(class1, "2x30", new String[] {
            "MGunVikkersKt 582", null, null, "BombGun30kgJ 1", "BombGun30kgJ 1"
        });
        weaponsRegister(class1, "2x50", new String[] {
            "MGunVikkersKt 582", null, null, "BombGun50kgJ 1", "BombGun50kgJ 1"
        });
        weaponsRegister(class1, "4x30", new String[] {
            "MGunVikkersKt 582", "BombGun30kgJ 1", "BombGun30kgJ 1", "BombGun30kgJ 1", "BombGun30kgJ 1"
        });
        weaponsRegister(class1, "4x50", new String[] {
            "MGunVikkersKt 582", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1"
        });
        weaponsRegister(class1, "2x100", new String[] {
            "MGunVikkersKt 582", "BombGun100kgJ 1", "BombGun100kgJ 1", null, null
        });
        weaponsRegister(class1, "none", new String[] {
            null, null, null, null, null
        });
    }
}
