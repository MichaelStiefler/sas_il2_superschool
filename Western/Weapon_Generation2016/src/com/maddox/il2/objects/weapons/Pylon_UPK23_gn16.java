// Russian UPK-23 2x 23mm gunpod for Su-15 , Mi-8/17 , Mi-24/35 and others.
// Last Modified by: western0221 2019-10-19

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_UPK23_gn16 extends Pylon
{

    public Pylon_UPK23_gn16()
    {
    }

    public void matSilver()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1sv");
        mesh.materialReplace("Ordnance1p", "Ordnance1svp");
        mesh.materialReplace("Ordnance1q", "Ordnance1svq");
    }

    public void matOliveDrab()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1od");
        mesh.materialReplace("Ordnance1p", "Ordnance1odp");
        mesh.materialReplace("Ordnance1q", "Ordnance1odq");
    }

    static
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_UPK23_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/UPK23_gn16/mono.sim");
        Property.set(class1, "massa", 136.0F);    // UPK-23's empty weight 136kg (not including 250x round bullets @ 329g)
        Property.set(class1, "dragCx", 0.025F);  // stock Pylons is +0.035F
    }
}