// Russian gunpod GUV-8700 with 1x 30mm granade AGS-17 for Mi-8/17 , Mi-24/35 and other helicopters.
// Last Modified by: western0221 2019-10-23

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_GUV8700AGS_gn16 extends Pylon
{

    public Pylon_GUV8700AGS_gn16()
    {
    }

    public void matSilver()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1sv");
        mesh.materialReplace("Ordnance1p", "Ordnance1svp");
        mesh.materialReplace("Ordnance1q", "Ordnance1svq");
    }

    static
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_GUV8700AGS_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/GUV8700_gn16/monoAGS.sim");
        Property.set(class1, "massa", 220.0F);    // GUV8700's empty container weight 140kg + granade launcher weight 80kg (not including bullets)
        Property.set(class1, "dragCx", 0.027F);  // stock Pylons is +0.035F
    }
}