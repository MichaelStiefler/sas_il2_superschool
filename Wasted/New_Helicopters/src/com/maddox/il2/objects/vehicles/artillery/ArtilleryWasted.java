package com.maddox.il2.objects.vehicles.artillery;

public abstract class ArtilleryWasted
{
    public static class SoldierAirborn extends ArtilleryGeneric
    {
        public SoldierAirborn()
        {
        }
    }


    public ArtilleryWasted()
    {
    }

    static 
    {
        new ArtilleryGeneric.SPAWN(com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.SoldierAirborn.class);
    }
}
