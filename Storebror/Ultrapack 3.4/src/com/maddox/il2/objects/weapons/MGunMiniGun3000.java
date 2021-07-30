//
// Decompiled by Procyon v0.5.29
//

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

/**
 * This class was modified by SAS~Skylla in the course of the AC-47 rework.
 *
 * @see AC_47, MGunAdjustableMiniGun, MGunMiniGun6000
 **/

public class MGunMiniGun3000 extends MGunAdjustableMiniGun {

    public MGunMiniGun3000() {
        this.rpm = 4000F;
        this.maxRPM = 4000F;
        this.minRPM = 2000F;
        this.stepRPM = 2000F;
    }

    public GunProperties createProperties() {
        final GunProperties properties = super.createProperties();
        properties.shotFreq = 50.0f;
        return properties;
    }
}
