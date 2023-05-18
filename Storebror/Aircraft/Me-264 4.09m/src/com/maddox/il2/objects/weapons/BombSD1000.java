package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class BombSD1000 extends Bomb
{

    public BombSD1000()
    {
        if(Config.isUSE_RENDER() && World.Rnd().nextInt(0, 99) < 25)
        {
            setMesh(Property.stringValue(getClass(), "mesh"));
            mesh.materialReplace("Ordnance1", "alhambra" + World.Rnd().nextInt(1, 1));
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombSD1000.class;
        Property.set(class1, "mesh", "3DO/Arms/SD-1000/mono.sim");
        Property.set(class1, "power", 550F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 1090F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
