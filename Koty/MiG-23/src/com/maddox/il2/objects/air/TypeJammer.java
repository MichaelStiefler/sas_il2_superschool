package com.maddox.il2.objects.air;
import com.maddox.il2.engine.Actor;
public abstract interface TypeJammer
{
	public abstract float getJammerFlood(char Band, Actor actor);
	public abstract float getJammerSpecial(int Type, char Band, Actor actor);
	//1 = cone
	//2 = mono pulse
	//3 = flood broad
	public abstract float[] getJammerGhost(char Band, Actor actor);
	public abstract float getJammerRCS(Actor actor);
	public abstract void setJammerRWR(char Band, Actor actor, float power);
	
	
}