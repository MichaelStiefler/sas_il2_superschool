//
// Decompiled by Procyon v0.5.29
//

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

/**
 * This class was modified by SAS~Skylla in the course of the AC-47 rework.
 *
 * @see AC_47, MGunAdjustableMiniGun, MGunMiniGun3000
 **/

public class MGunMiniGun6000 extends MGunAdjustableMiniGun {

	public MGunMiniGun6000() {
		this.rpm = 6000F;
		this.maxRPM = 6000F;
		this.minRPM = 3000F;
		this.stepRPM = 3000F;
	}

	public GunProperties createProperties() {
		final GunProperties properties = super.createProperties();
		properties.shotFreq = 100.0f;
		return properties;
	}
}
