package com.maddox.il2.objects.weapons;

import java.security.SecureRandom;

import com.maddox.il2.ai.RangeRandom;

public class MGunBrowningM2 extends MGunBrowning50kAPIT {

	private static RangeRandom theRangeRandom;

	public MGunBrowningM2() {
	}

	private void initRandom() {
		if (theRangeRandom != null) return;
		long lTime = System.currentTimeMillis();
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(lTime);
		long lSeed1 = secRandom.nextInt();
		long lSeed2 = secRandom.nextInt();
		long lSeed = (lSeed1 << 32) + lSeed2;
		theRangeRandom = new RangeRandom(lSeed);
	}

	private int nextRandomInt(int iMin, int iMax) {
		this.initRandom();
		return theRangeRandom.nextInt(iMin, iMax);
	}

	public void init() {
		super.init();
		int iRandBullet = this.nextRandomInt(0, Integer.MAX_VALUE / 2) % this.prop.bullet.length + 1;
		for (int i = 0; i < iRandBullet; i++)
			this.nextIndexBulletType();
	}

}
