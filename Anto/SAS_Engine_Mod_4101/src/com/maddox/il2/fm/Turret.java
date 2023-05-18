/*4.10.1 class*/
package com.maddox.il2.fm;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;

public class Turret
{
	public int indexA;
	public int indexB;
	public Loc Lstart = new Loc();
	public Actor target;
	public float[] tu = new float[3];
	public float[] tuLim = new float[3];
	public boolean bIsAIControlled = true;
	public boolean bIsNetMirror = false;
	public boolean bIsOperable = true;
	public long timeNext = 0L;
	public boolean bIsShooting = false;
	public int tMode = 0;
	public float health = 1.0F;
	public static final int TU_MO_SLEEP = 0;
	public static final int TU_MO_TRACKING = 1;
	public static final int TU_MO_FIRING_TRACKING = 2;
	public static final int TU_MO_FIRING_STOPPED = 3;
	public static final int TU_MO_PANIC = 4;
	public static final int TU_MO_STOPPED = 5;
	
	//TODO: Lutz mod
	//----------------------------
	public float gunnerSkill;
    public int igunnerSkill;
    //----------------------------
	
	public void setHealth(float f)
	{
		health = f;
		if (f == 0.0F)
			bIsOperable = false;
	}
}