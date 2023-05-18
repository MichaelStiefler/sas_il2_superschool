package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;

public interface TypeJazzPlayer {

	public abstract boolean hasCourseWeaponBullets();

	public abstract boolean hasSlantedWeaponBullets();

	public abstract Vector3d getAttackVector();
}
