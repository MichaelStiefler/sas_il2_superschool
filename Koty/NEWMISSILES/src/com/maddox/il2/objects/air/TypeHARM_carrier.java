package com.maddox.il2.objects.air;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
public abstract interface TypeHARM_carrier
{
	public abstract String getHarmMode();
	public abstract Actor getHarmVictim();
	public abstract Point3d getHarmWP();
	public abstract float getHarmLOFT();
	public abstract void setNewHARMVictim();
}