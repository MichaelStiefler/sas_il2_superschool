package com.maddox.sas1946.il2.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Aircraft._WeaponSlot;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.rts.Finger;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;

/**
 * "AircraftTools" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide helpers and missing functions for Aircrafts in IL-2 Sturmovik 1946.
 * <p>
 * 
 * @version 1.1.3
 * @since 1.0.4
 * @author SAS~Storebror
 */
public class AircraftTools {
    
	/**
	 * Override default Constructor to avoid instantiation
	 * 
	 * @throws Exception
	 * @since 1.0.4
	 */
	private AircraftTools() throws Exception {
		throw new Exception("Class com.maddox.sas1946.il2.util.Aircraft cannot be instanciated!");
	}

	// *****************************************************************************************************************************************************************************************************
	// Public interface section.
	// Methods and Arguments are supposed to be final here.
	// Take care of encapsulation and don't modify methods or arguments declared on this interface
	// to ensure future backward compatibility
	// If you need a new method with the same name but different parameters or return types,
	// simply overload the given methods in this interface.
	// *****************************************************************************************************************************************************************************************************

	/**
	 * This method provides support for creation of aircraft weapon slots with a single line of code.<br>
	 * It provides the implementation of the missing method which is called by lines like this:<br>
	 * &nbsp;<br>
	 * <code>weaponsRegister(class1, "default", new String[] { "MGunBrowning50k 250", "MGunBrowning50k 250" });</code><br>
	 * &nbsp;<br>
	 * The method "weaponsRegister" was part of former IL-2 versions (Pacific Fighters or earlier) but was removed and replaced by an empty method in IL-2 1946, therefore these lines in aircraft classes currently serve no purpose and weapon slots have to
	 * be either created manually by a lot of Java code lines or by providing the weapon slot declaration in a proprietary encrypted "cod" format.<br>
	 * This method brings back the old functionality so you can create weapon slots with a single line of code again.<br>
	 * &nbsp;<br>
	 * 
	 * Notice for the "theWeapons" Parameter:<br>
	 * This parameter holds an array of strings defining the weapons available in this slot.<br>
	 * Each string defines one weapon in the slot.<br>
	 * Each string consists of 2 or 3 parameters separated by spaces.<br>
	 * If the string consists of 3 parameters, then the first parameter defines the trigger index for this weapon, e.g. "0" for guns, "1" for cannons, "2" for rockets, "3" for bombs etc.<br>
	 * If the string consists of 2 parameters, then the trigger index for this weapon will be taken from the triggers defined in a previous call of the "weaponTriggersRegister" method.<br>
	 * The next parameter defines the weapon's class short name.<br>
	 * The last parameter defines the number of bullets for this weapon.<br>
	 * &nbsp;<br>
	 *
	 * @param airClass
	 *            The aircraft's Class where you want to create a weapon slot for
	 * @param slotName
	 *            The string defining the name of the weapon slot, e.g. "default", "empty", "2x100lb" etc.
	 * @param theWeapons
	 *            The weapons available in this slot.
	 * @since 1.0.4
	 */
	public final static void weaponsRegister(Class airClass, String slotName, String theWeapons[]) {
		doWeaponsRegister(airClass, slotName, theWeapons);
	}
	
    /**
     * This method provides a simple means of keeping wing weapons visible and usable while<br>
     * an Aircraft is folding its wings.<br>
     * &nbsp;<br>
     * In Stock game (and most mods up to the year 2021), weapons residing on wing stores become hidden<br>
     * and unusable while wings are being folded.<br>
     * This is the Java Code found in such Aircraft Classes, the responsible method is "moveWingFold":<br>
     * &nbsp;<br>
     * <code>public void moveWingFold(float f) {<br>
        &nbsp;&nbsp;if (f &lt; 0.001F) {<br>
        &nbsp;&nbsp;&nbsp;&nbsp;this.setGunPodsOn(true);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;this.hideWingWeapons(false);<br>
        &nbsp;&nbsp;} else {<br>
        &nbsp;&nbsp;&nbsp;&nbsp;this.setGunPodsOn(false);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;this.FM.CT.WeaponControl[0] = false;<br>
        &nbsp;&nbsp;&nbsp;&nbsp;this.hideWingWeapons(true);<br>
        &nbsp;&nbsp;}<br>
        &nbsp;&nbsp;this.moveWingFold(this.hierMesh(), f);<br>
    }</code><br>
     * &nbsp;<br>
     * Using this helper Method, the Code changes to:<br>
     * &nbsp;<br>
     * <code>public void moveWingFold(float f) {<br>
        &nbsp;&nbsp;this.moveWingFold(this.hierMesh(), f);<br>
        &nbsp;&nbsp;AircraftTools.updateExternalWeaponHooks(this);<br>
    }</code><br>
     * &nbsp;<br>
     * This causes the wing weapons to remain visible and usable while wings are being folded/unfolded.<br>
     * &nbsp;<br>
     *
     * @param aircraft
     *            The aircraft which needs to have its wing weapons updated while folding wings
     * @since 1.1.3
     */
    public final static void updateExternalWeaponHooks(Aircraft aircraft) {
        doUpdateExternalWeaponHooks(aircraft);
    }
    

    // *****************************************************************************************************************************************************************************************************
	// Private implementation section.
	// Do whatever you like here but keep it private to this class.
	// *****************************************************************************************************************************************************************************************************

	private static Method aircraftWeaponsListProperty = null;
	private static Method aircraftWeaponsMapProperty = null;
	private static Method aircraftGetWeaponTriggersRegistered = null;

	private static void doWeaponsRegister(Class aircraftClass, String slotName, String theWeapons[]) {
		int theWeaponsLen = 0;
		if (slotName == null)
			return;
		if (slotName.length() < 1)
			return;
		if (aircraftClass == null)
			return;
		if (theWeapons != null)
			theWeaponsLen = theWeapons.length;
		if (!(Aircraft.class.isAssignableFrom(aircraftClass)))
			return;
		ArrayList registeredWeaponsList = weaponsListProperty(aircraftClass);
		HashMapInt weaponsHashMap = weaponsMapProperty(aircraftClass);
		int weaponTriggersRegistered[] = getWeaponTriggersRegistered(aircraftClass);
		_WeaponSlot a_lweaponslot[] = new _WeaponSlot[theWeaponsLen];
		for (int index = 0; index < theWeaponsLen; index++) {
			if (theWeapons[index] == null)
				continue;
			NumberTokenizer numbertokenizer = new NumberTokenizer(theWeapons[index]);
			int weaponHookNumber = 0;
			switch (numbertokenizer.countTokens()) {
			case 2:
				weaponHookNumber = weaponTriggersRegistered[index];
				break;
			case 3:
				weaponHookNumber = numbertokenizer.next(9);
				break;
			default:
				continue;
			}
			try {
				a_lweaponslot[index] = new _WeaponSlot(weaponHookNumber, numbertokenizer.next(null),
						numbertokenizer.next(-12345));
			} catch (Exception e) {
				System.out.println("Error registering Weapon Slot '" + slotName + "' for Aircraft '" + aircraftClass.getName()
						+ "', StrackTrace follows:");
				e.printStackTrace();
			}
		}
		registeredWeaponsList.add(slotName);
		weaponsHashMap.put(Finger.Int(slotName), a_lweaponslot);
	}

	private static void initMethods() {
		if (aircraftWeaponsListProperty == null) {
			aircraftWeaponsListProperty = Reflection.getMethod(Aircraft.class, "weaponsListProperty",
					new Class[] { Class.class });
		}
		if (aircraftWeaponsMapProperty == null) {
			aircraftWeaponsMapProperty = Reflection.getMethod(Aircraft.class, "weaponsMapProperty",
					new Class[] { Class.class });
		}
		if (aircraftGetWeaponTriggersRegistered == null) {
			aircraftGetWeaponTriggersRegistered = Reflection.getMethod(Aircraft.class, "getWeaponTriggersRegistered",
					new Class[] { Class.class });
		}
	}

	private static ArrayList weaponsListProperty(Class class1) {
		initMethods();
		if (aircraftWeaponsListProperty == null)
			return null;
		return (ArrayList)Reflection.invokeMethod(aircraftWeaponsListProperty, new Object[] { class1 });
	}

	private static HashMapInt weaponsMapProperty(Class class1) {
		initMethods();
		if (aircraftWeaponsMapProperty == null)
			return null;
		return (HashMapInt)Reflection.invokeMethod(aircraftWeaponsMapProperty, new Object[] { class1 });
	}

	private static int[] getWeaponTriggersRegistered(Class class1) {
		initMethods();
		if (aircraftGetWeaponTriggersRegistered == null)
			return null;
		return (int[])Reflection.invokeMethod(aircraftGetWeaponTriggersRegistered, new Object[] { class1 });
	}
	
    private static String dumpName(String hookName) {
        int i = hookName.length() - 1;
        while (i >= 0 && Character.isDigit(hookName.charAt(i))) i--;
        i++;
        return hookName.substring(0, i) + "Dump" + hookName.substring(i);
    }
	  
    private static boolean doUpdateExternalWeaponHooks(Aircraft aircraft) {
        boolean retVal = true;
        String[] weaponHookArray = Aircraft.getWeaponHooksRegistered(aircraft.getClass());
        int[] weaponTriggerArray = Aircraft.getWeaponTriggersRegistered(aircraft.getClass());
        for (int i = 0; i < weaponHookArray.length; i++) {
            if (weaponTriggerArray.length > i && weaponTriggerArray[i] > 9) continue;
            try {
                BulletEmitter be = aircraft.getBulletEmitterByHookName(weaponHookArray[i]);
                if (be instanceof Pylon) {
                    Pylon py = (Pylon)be;
                    py.pos.getRel().set(0, 0, 0, 0, 0, 0);
                    py.set(aircraft, py.getHookName());
                    continue;
                }
                if (be instanceof GunGeneric) {
                    GunGeneric gg = (GunGeneric)be;
                    gg.pos.getRel().set(0, 0, 0, 0, 0, 0);
                    try {
                        Hook localHook = aircraft.findHook(weaponHookArray[i]);
                        gg.pos.setBase(aircraft, localHook, false);
                    } catch (ActorException e) {
                        continue;
                    }
                    gg.pos.changeHookToRel();
                    gg.visibilityAsBase(true);
                    gg.pos.setUpdateEnable(false);
                    gg.pos.reset();
                    Eff3DActor shells = (Eff3DActor)Reflection.getValue(gg, "shells");
                    if (shells != null) {
                        shells.pos.getRel().set(0, 0, 0, 0, 0, 0);
                        try {
                            Hook localHook = aircraft.findHook(dumpName(weaponHookArray[i]));
                            shells.pos.setBase(aircraft, localHook, false);
                        } catch (ActorException e) {
                            continue;
                        }
                        shells.pos.changeHookToRel();
                        shells.visibilityAsBase(true);
                        shells.pos.setUpdateEnable(false);
                        shells.pos.reset();
                    }
                    continue;
                }
                if (!be.haveBullets()) {
                    continue;
                }
                if (be instanceof BombGun && !be.getHookName().toLowerCase().startsWith("_external")) continue;
                if (BaseGameVersion.is410orLater() && be instanceof RocketBombGun && !be.getHookName().toLowerCase().startsWith("_external")) continue;
                int bullets = ((aircraft == World.getPlayerAircraft()) && !World.cur().diffCur.Limited_Ammo) ? -1 : be.countBullets();
                be.loadBullets(0);
                be.loadBullets(bullets);
            } catch (Exception e) {
                e.printStackTrace();
                retVal = false;
            }
        }
        return retVal;
    }
	
}
