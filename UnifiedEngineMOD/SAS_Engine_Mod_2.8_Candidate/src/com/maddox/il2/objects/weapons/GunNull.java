/*
 * Changelog:
 * 2014.08.11: doSetRocketHook method changed to make this class independent from Guided Missile Mod
 */

// Source File Name: GunNull.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.GuidedMissileInterop;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.sas1946.il2.util.Reflection;

public class GunNull extends Gun {

	private boolean hasBullets = true;

	public GunNull() {
		this.flags = this.flags | 0x4004;
		this.pos = new ActorPosMove(this);
	}

	public Class bulletClass() {
		return null;
	}

	public float bulletMassa() {
		return 0.0F;
	}

	public float bulletSpeed() {
		return 0.0F;
	}

	public int countBullets() {
		if (this.getOwner() != null) {
			// TODO: ++ Changed Code to make Engine Mod independent of Guided Missiles Mod ++
			if (GuidedMissileInterop.getGuidedMissileModExists()) {
				if (GuidedMissileInterop.getTypeGuidedMissileCarrierAbstract().isInstance(this.getOwner())) {
					Object guidedMissileUtils = Reflection.invokeMethod(this.getOwner(), "getGuidedMissileUtils");
					boolean hasMissiles = ((Boolean)Reflection.invokeMethod(guidedMissileUtils, "hasMissiles")).booleanValue();
					return hasMissiles ? Integer.MAX_VALUE : 0;
				}
			}
//			if (this.getOwner() instanceof TypeGuidedMissileCarrier) return ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().hasMissiles() ? Integer.MAX_VALUE : 0;
			// TODO: -- Added/changed Code Multiple Missile Type Selection --
		}
		return this.hasBullets ? Integer.MAX_VALUE : 0;
	}

	public BulletEmitter detach(HierMesh hiermesh, int i) {
		return this;
	}

	public void emptyGun() {
		this.hasBullets = false;
	}

	public String getHookName() {
		return "Body";
	}

	public boolean haveBullets() {
		if (this.getOwner() != null) {
			// TODO: ++ Changed Code to make Engine Mod independent of Guided Missiles Mod ++
			if (GuidedMissileInterop.getGuidedMissileModExists()) {
				if (GuidedMissileInterop.getTypeGuidedMissileCarrierAbstract().isInstance(this.getOwner())) {
					Object guidedMissileUtils = Reflection.invokeMethod(this.getOwner(), "getGuidedMissileUtils");
					return ((Boolean)Reflection.invokeMethod(guidedMissileUtils, "hasMissiles")).booleanValue();
				}
			}
//			if (this.getOwner() instanceof TypeGuidedMissileCarrier) return ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().hasMissiles();
			// TODO: -- Added/changed Code Multiple Missile Type Selection --
		}
		return this.hasBullets;
	}

	public void initRealisticGunnery(boolean flag) {
	}

	public boolean isEnablePause() {
		return false;
	}

	public boolean isShots() {
		return false;
	}

	public void loadBullets() {
		com.maddox.il2.ai.EventLog.type("loadBullets");
		this.hasBullets = true;
	}

	public void loadBullets(int i) {
		com.maddox.il2.ai.EventLog.type("loadBullets " + i);
		this.hasBullets = true;
	}

	public void set(Actor actor, String s) {
	}

	public void set(Actor actor, String s, Loc loc) {
	}

	public void set(Actor actor, String s, String s1) {
	}

	public void setBulletClass(Class class1) {
	}

	public void shots(int i) {
	}

	public void shots(int i, float f) {
	}
}
