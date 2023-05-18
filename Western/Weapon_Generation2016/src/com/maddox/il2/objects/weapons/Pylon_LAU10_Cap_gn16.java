// aero dynamics "Fairing" part of "Pylon_LAU10_gn16" (Launcher of 4x 5inch Zuni rocket).

// Description about changing color is written in Pylon_LAU10_gn16.java


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU10_Cap_gn16 extends Pylon
{

    public Pylon_LAU10_Cap_gn16()
    {
    }

    public void matHighvis()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("LAU_10", "LAU_10HV");
        mesh.materialReplace("LAU_10p", "LAU_10HVp");
        mesh.materialReplace("LAU_10q", "LAU_10HVq");
    }

    public void jettisonCap()
    {
        drawing(false);
        setMassa(0.000001F);   // When equal just 0.0F, Mass.class ignores its value and treat as default mass.
        setDragCx(0.000001F);  // When equal just 0.0F, Squre.class ignores its value and treat as default dragCx.
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU10_gn16/monocap.sim");
        Property.set(class1, "massa", 8F);
        Property.set(class1, "dragCx", 0.011F);  // stock Pylons is +0.035F
        Property.set(class1, "bMinusDrag", 1);  // working inverting dragCx value into minus drag as -0.01F
    }
}