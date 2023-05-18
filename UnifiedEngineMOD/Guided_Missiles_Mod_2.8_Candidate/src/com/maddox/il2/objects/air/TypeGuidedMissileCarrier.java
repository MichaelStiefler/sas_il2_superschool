package com.maddox.il2.objects.air;
// Source File Name: TypeGuidedMissileCarrier.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01


public interface TypeGuidedMissileCarrier {

	public abstract com.maddox.il2.objects.weapons.GuidedMissileUtils getGuidedMissileUtils();

	// Old abstracts, no longer in place since Jetwar 1.2 implementation
	
	// public abstract com.maddox.il2.engine.Actor getMissileTarget();
	//
	// public abstract boolean hasMissiles();
	//
	// public abstract void shotMissile();
	//
	// public abstract int getMissileLockState();
	//
	// public abstract com.maddox.JGP.Point3f getMissileTargetOffset();
}
