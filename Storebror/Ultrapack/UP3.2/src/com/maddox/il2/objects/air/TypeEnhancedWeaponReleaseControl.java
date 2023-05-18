package com.maddox.il2.objects.air;

public interface TypeEnhancedWeaponReleaseControl extends TypeLimitedWeaponReleaseControl {

	/**
	 * This class was introduced to UP3 in order of the 'Enhanced Weapon Release Control' Mod by
	 *
	 * @author SAS~Skylla in 10/17.
	 *
	 * @see com.maddox.il2.fm.AircraftState com.maddox.il2.fm.Controls com.maddox.il2.game.AircraftHotkeys com.maddox.il2.objects.air.TypeLimitedWeaponOptionControl
	 *
	 *      This interface is to be used on AC which allow different weapon options than the default hardcoded ones. See the method description for details.
	 **/

	/**
	 * @see canSelectBombSalvoSize()
	 * @return A list of integer indices indicating the possible bomb salvo sizes. Please note that the inherited canSelectBombSalvoSize() method must return 'true' in order for the values returned here to take effect. The default values are: -1 (default
	 *         fire), 0 (full salvo), 1 (single fire). Furthermore it is possible to add values from 2 - (the number of total bombs) which indicate the number of bombs released in a group release. Please note that values exceeding the total number of
	 *         bombs will not be displayed. If a value is in this list, it can be selected by the pilot. Please note that 'default fire' must be always available.
	 *
	 *         Please note that 'bombs' refers to instances of 'RocketGun', 'BombGun' or 'RocketBombGun' fired from Trigger 3.
	 *
	 *         The maximal possible length of the field returned is 256
	 **/
	public int[] getPossibleBombSalvoSizeOptions();

	/**
	 * @see canSelectRocketSalvoSize()
	 * @return A list of integer indices indicating the possible rocket salvo sizes. Please note that the inherited canSelectRocketSalvoSize() method must return 'true' in order for the values returned here to take effect. The default (and only possible
	 *         return-) values are: -1 (default fire), 0 (full salvo), 1 (single fire). If a value is in this list, it can be selected by the pilot. Please note that 'default fire' must be always available.
	 *
	 *         Please note that 'rockets' refers to instances of 'RocketGun' fired from Trigger 2.
	 *
	 *         The maximal possible length of the field returned is 256
	 **/
	public int[] getPossibleRocketSalvoSizeOptions();

	/**
	 * @see canSelectBombReleaseDelay
	 * @return A list of long values indicating the release delay between two bombs in milliseconds. The default values are 33, 125, 250, 500 and 1000. Please note that values must not be smaller than 33 and bigger than 15000. The values returned here
	 *         will show no effect if the inherited canSelectBombReleaseDelay() method returns 'false'.
	 *
	 *         Please note that 'bombs' refers to instances of 'RocketGun', 'BombGun' or 'RocketBombGun' fired from Trigger 3.
	 *
	 *         The maximal possible length of the field returned is 256
	 **/
	public long[] getPossibleBombReleaseDelayOptions();

	/**
	 * @see canSelectRocketReleaseDelay()
	 * @return A list of long values indicating the release delay between two rockets in milliseconds. The default values are 33, 125, 250, 500 and 1000. Please note that values must not be smaller than 33 and bigger than 15000. The values returned here
	 *         will show no effect if the inherited canSelectRocketReleaseDelay() method returns 'false'.
	 *
	 *         Please note that 'rockets' refers to instances of 'RocketGun' fired from Trigger 2.
	 *
	 *         The maximal possible length of the field returned is 256
	 **/
	public long[] getPossibleRocketReleaseDelayOptions();
}
