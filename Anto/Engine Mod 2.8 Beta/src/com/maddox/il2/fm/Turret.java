/*Modified Turret class for the SAS Engine Mod*/
package com.maddox.il2.fm;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;

public class Turret {
	public int indexA;
	public int indexB;
	public Loc Lstart;
	public Actor target;
	public float tu[];
	public float tuLim[];
	public boolean bIsAIControlled;
	public boolean bIsNetMirror;
	public boolean bIsOperable;
	public long timeNext;
	public boolean bIsShooting;
	public int tMode;
	public float health;
	public int obsDir;
	public static final int FRONT = 1;
	public static final int REAR = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int TU_MO_SLEEP = 0;
	public static final int TU_MO_TRACKING = 1;
	public static final int TU_MO_FIRING_TRACKING = 2;
	public static final int TU_MO_FIRING_STOPPED = 3;
	public static final int TU_MO_PANIC = 4;
	public static final int TU_MO_STOPPED = 5;
	// TODO: New parameters
	// ----------------------------
	public float gunnerSkill;
	public int igunnerSkill;

	// ----------------------------

	public Turret() {
		Lstart = new Loc();
		tu = new float[3];
		tuLim = new float[3];
		bIsAIControlled = true;
		bIsNetMirror = false;
		bIsOperable = true;
		timeNext = 0L;
		bIsShooting = false;
		tMode = 0;
		health = 1.0F;
		obsDir = 0;
	}

	public void setHealth(float f) {
		health = f;
		if (f == 0.0F) bIsOperable = false;
	}

	public void setObservingDirection() {
		float f = Lstart.getAzimut();
		if (f > 360F) f -= 360F;
		if (f < 45F || f > 315F) obsDir = 1;
		else if (f < 135F && f > 45F) obsDir = 4;
		else if (f < 315F && f > 225F) obsDir = 3;
		else obsDir = 2;
	}
}
