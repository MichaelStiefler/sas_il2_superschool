package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtSAM;
import com.maddox.il2.ai.ground.TgtTank;

public abstract class ArtillerySAM
{

	public static class Strela_2M extends ArtilleryGeneric
	implements TgtFlak, AAA
	{
		public Strela_2M()
		{
		}
	}
	
    public static class Strela_1M extends ArtilleryGeneric
    implements TgtTank, TgtFlak, STank, TgtSAM
{

    public Strela_1M()
    {
    }
}
	
	public ArtillerySAM()
	{
	}

	static 
	{
		new ArtilleryGeneric.SPAWN(com.maddox.il2.objects.vehicles.artillery.ArtillerySAM.Strela_2M.class);
		new ArtilleryGeneric.SPAWN(com.maddox.il2.objects.vehicles.artillery.ArtillerySAM.Strela_1M.class);
	}
}