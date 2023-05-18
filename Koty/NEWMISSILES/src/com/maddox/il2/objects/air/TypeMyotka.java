package com.maddox.il2.objects.air;
import com.maddox.il2.engine.Actor;
public abstract interface TypeMyotka
{
	public abstract float getMyotkaAnglesVertical();
	public abstract float getMyotkaAnglesHorizontal();
	public abstract Actor getMyotkaActor();
	public abstract int getMyotkaMode();
}