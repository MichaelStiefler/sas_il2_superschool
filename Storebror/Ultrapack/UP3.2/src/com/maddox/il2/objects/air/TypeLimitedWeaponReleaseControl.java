package com.maddox.il2.objects.air;

public interface TypeLimitedWeaponReleaseControl {

	/**
	 * This class was introduced to UP3 in order of the 'Enhanced Weapon Release Control for UP3' Mod by
	 *
	 * @author SAS~Skylla in 10/17.
	 *
	 * @see com.maddox.il2.fm.AircraftState com.maddox.il2.fm.Controls com.maddox.il2.game.AircraftHotkeys com.maddox.il2.objects.air.TypeEnhancedWeaponOptionControl
	 *
	 *      This interface is to be used on AC which do - for whatever reason - not allow certain options to be chosen. See the method description for details.
	 **/

	/**
	 * @return Indicates whether the pilot can select which bomb he wants to release next. If 'false' gets returned, the default release order will apply. Please note that 'bombs' refers to instances of 'RocketGun', 'BombGun' or 'RocketBombGun' fired from
	 *         Trigger 3.
	 **/
	public boolean canSelectBomb();

	/**
	 * @return Indicates whether the pilot can select which rocket he wants to release next. If 'false' gets returned, the default release order will apply. Please note that 'rockets' refers to instances of 'RocketGun' fired from Trigger 2.
	 **/
	public boolean canSelectRocket();

	/**
	 * @return Indicates wheter the pilot can select the salvo size of his next bomb release. Please note that 'bombs' refers to instances of 'RocketGun', 'BombGun' or 'RocketBombGun' fired from Trigger 3.
	 **/
	public boolean canSelectBombSalvoSize();

	/**
	 * @return Indicates wheter the pilot can select the salvo size of his next rocket release. Please note that 'rockets' refers to instances of 'RocketGun' fired from Trigger 2.
	 **/
	public boolean canSelectRocketSalvoSize();

	/**
	 * @return Indicates wheter the pilot can select the release delay inbetween the bombs of his next bomb release. Please note that 'bombs' refers to instances of 'RocketGun', 'BombGun' or 'RocketBombGun' fired from Trigger 3.
	 **/
	public boolean canSelectBombReleaseDelay();

	/**
	 * @return Indicates wheter the pilot can select the release delay inbetween the rockets of his next rocket release. Please note that 'rockets' refers to instances of 'RocketGun' fired from Trigger 2.
	 **/
	public boolean canSelectRocketReleaseDelay();
}
