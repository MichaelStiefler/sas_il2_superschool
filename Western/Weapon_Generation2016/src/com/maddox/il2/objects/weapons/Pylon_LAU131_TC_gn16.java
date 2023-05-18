// US LAU-131 , launcher for 7x 2.75inch rocket -- Mk4 FFAR and HYDRA70
// same to LAU-32 , LAU-68
// with tail fairing version

// Description about changing color is written in Pylon_LAU131_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU131_TC_gn16 extends Pylon
{

    public Pylon_LAU131_TC_gn16()
    {
    }

    public void matGreen()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance2", "Ordnance2green");
        mesh.materialReplace("Ordnance2p", "Ordnance2greenp");
        mesh.materialReplace("Ordnance2q", "Ordnance2greenq");
    }

    public void matBrown()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance2", "Ordnance2brown");
        mesh.materialReplace("Ordnance2p", "Ordnance2brownp");
        mesh.materialReplace("Ordnance2q", "Ordnance2brownq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU131_TC_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU131_gn16/monoTC.sim");
        Property.set(class1, "massa", 29.5F);
        Property.set(class1, "dragCx", 0.023F);  // stock Pylons is +0.035F
    }
}