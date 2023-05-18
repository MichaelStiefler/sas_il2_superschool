
package com.maddox.il2.objects.air;

/*
 * @author: SAS~Skylla at www.sas1946.com
 * 
 * @see: com.maddox.il2.object.air.HurricaneMkIIbMod, com.maddox.il2.object.air.CockpitHurricaneMkIIbModFilter
 * 
 * IMPORTANT: Needs also the classes 	com.maddox.il2.objects.weapons.FuelTank_Tank44gal
 * 								&		com.maddox.il2.objects.weapons.FuelTankGun_Tank44gal
 * from SAS Hurricane Pack to work!
 * If these are not included you will not have the loadouts after the first 44gal Tank option!
 */


import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class HurricaneMkIIbModFilter extends Hurricane implements TypeFighter, TypeStormovik
{
	
	public HurricaneMkIIbModFilter() {
		
	}
	
	public void moveCockpitDoor(float paramFloat) {
		resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.99F, 0.0F, 0.68F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
				Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
			}
			setDoorSnd(paramFloat);
		}
	}
  	
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.CT.bHasCockpitDoorControl = true;
		this.FM.CT.dvCockpitDoor = 0.75F;
	}
  
	static {
		Class localClass = HurricaneMkIIbModFilter.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "Hurri");
		Property.set(localClass, "meshName", "3DO/Plane/HurricaneMkIIbModFilter/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(localClass, "yearService", 1942.0F);
		Property.set(localClass, "yearExpired", 1945.5F);
		Property.set(localClass, "FlightModel", "FlightModels/HurricaneMkIIMod.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitHurricaneMkIIbModFilter.class });
    
		Property.set(localClass, "LOSElevation", 0.965F);
		Aircraft.weaponTriggersRegister(localClass, new int[] { 
				0, 0, 1, 1,
				9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 
				9, 3, 9, 3
    	});
		//IMPORTANT: Loadouts are defined in cod! Changing anything here will have no effect! 
		Aircraft.weaponHooksRegister(localClass, new String[] { 
				"_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", 
				"_ExternalDev01", "_ExternalRock01", "_ExternalDev02", "_ExternalRock02", "_ExternalDev03", "_ExternalRock03", "_ExternalDev04", "_ExternalRock04", "_ExternalDev05", "_ExternalRock05", "_ExternalDev06", "_ExternalRock06", 
				"_ExternalDev07", "_ExternalBomb01", "_ExternalDev08", "_ExternalBomb02"
		});
    
		Aircraft.weaponsRegister(localClass, "default", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120", 
				null, null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "4xubtest", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunUBk 100", "MGunUBk 100",
				null, null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "4xrs82", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120", 
				"PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", null, null, null, null, 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "6xrs82", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120", 
				"PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "2fab100", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120",
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "BombGunFAB100 1", "PylonF4FPLN2", "BombGunFAB100 1"
				});
		Aircraft.weaponsRegister(localClass, "2xDrop44", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120",
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "FuelTankGun_Tank44gal 1", "PylonF4FPLN2", "FuelTankGun_Tank44gal 1"
		});
		Aircraft.weaponsRegister(localClass, "2xDrop80", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120", 
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "FuelTankGun_Tank80 1", "PylonF4FPLN2", "FuelTankGun_Tank80 1"
		});
		Aircraft.weaponsRegister(localClass, "4x20", new String[] {
	            "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", 
	            null, null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "4x204r", new String[] {
				"MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120",
				"PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", null, null, null, null, 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "4x206r", new String[] {
				"MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120",
				"PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", "PylonRO_82_1", "RocketGunRS82 1", 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "4x20b100", new String[] {
				"MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", 
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "BombGunFAB100 1", "PylonF4FPLN2", "BombGunFAB100 1"
		});
		Aircraft.weaponsRegister(localClass, "4x202xDrop44", new String[] {
				"MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", null, null, 
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "FuelTankGun_Tank44gal 1", "PylonF4FPLN2", "FuelTankGun_Tank44gal 1"
		});
		Aircraft.weaponsRegister(localClass, "4x202xDrop80", new String[] {
				"MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", "MGunShVAKk 120", 
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "FuelTankGun_Tank80 1", "PylonF4FPLN2", "FuelTankGun_Tank80 1"
		});		
		Aircraft.weaponsRegister(localClass, "none", new String[] {
				null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null
		});
		Aircraft.weaponsRegister(localClass, "test", new String[] {
				"MGunUBk 100", "MGunUBk 100", "MGunShVAKk 120", "MGunShVAKk 120",
				null, null, null, null, null, null, null, null, null, null, null, null, 
				"PylonF4FPLN2", "BombGunFAB100 1", "PylonF4FPLN2", "BombGunFAB100 1"
		});
	}
}
