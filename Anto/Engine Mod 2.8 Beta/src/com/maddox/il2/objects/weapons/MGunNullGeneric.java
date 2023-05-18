// Source File Name: MGunNullGeneric.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Loc;

public class MGunNullGeneric extends GunNull implements BulletEmitter, BulletAimer {

	public MGunNullGeneric() {
		_index = -1;
	}

	public String getDayProperties(String s) {
		return null;
	}

	public void createdProperties() {
		super.createdProperties();
	}

	public String prop_fireMesh() {
		return null;
	}

	public String prop_fire() {
		return null;
	}

	public String prop_sprite() {
		return null;
	}

	public void setConvDistance(float f, float f1) {
	}

	public void init() {
		int i = prop.bullet.length;
		bulletAG = new float[i];
		bulletKV = new float[i];
		initRealisticGunnery();
	}

	public void initRealisticGunnery(boolean flag) {
		if (prop == null) return;
		if (prop.bullet == null) return;
		int i = prop.bullet.length;
		for (int j = 0; j < i; j++) {
			bulletAG[j] = 0.0F;
			bulletKV[j] = 0.0F;
		}

	}

	public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l) {
		bullet = new BulletAircraftGeneric(vector3d, i, gungeneric, loc, vector3d1, l);
		if (!World.cur().diffCur.Realistic_Gunnery) {
			if (!isContainOwner(World.getPlayerAircraft()))
			;
		}
		return bullet;
	}

	public void loadBullets(int i) {
		super.loadBullets(i);
		resetGuard();
	}

	public void _loadBullets(int i) {
		super._loadBullets(i);
		resetGuard();
	}

	private void resetGuard() {
		guardBullet = null;
	}

	public int nextIndexBulletType() {
		_index++;
		if (_index == prop.bullet.length) {
			_index = 0;
		}
		return _index;
	}

	protected static final boolean DEBUG = false;
	protected BulletAircraftGeneric guardBullet;
	private static Bullet bullet;
	private int _index;
}
