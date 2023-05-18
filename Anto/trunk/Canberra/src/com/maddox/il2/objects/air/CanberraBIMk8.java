package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GunEmpty;

public class CanberraBIMk8 extends Canberra
    implements TypeStormovik
{

    public CanberraBIMk8()
    {
    }

    public void onAircraftLoaded(){
        if(!(getBulletEmitterByHookName("_CANNON01") instanceof GunEmpty))
        {
            hierMesh().chunkVisible("BombBay_Short", true);
            hierMesh().chunkVisible("BombDoorLeft_Short", true);
            hierMesh().chunkVisible("BombDoorRight_Short", true);
            hierMesh().chunkVisible("GunPack", true);
            hierMesh().chunkVisible("BombBay", false);
            hierMesh().chunkVisible("BombDoorLeft", false);
            hierMesh().chunkVisible("BombDoorRight", false);
        }
    }
    
	public void moveCockpitDoor(float f) {
		hierMesh().chunkSetAngles("Door", 0.0F, 0.0F, 90F * f);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null
					&& Main3D.cur3D().cockpits[0] != null) {
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			}
			this.setDoorSnd(f);
		}
	}
	
    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.CanberraBIMk8.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "iconFar_shortClassName", "Canberra Mk 8");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "meshName", "3DO/Plane/CanberraMk8(Multi1)/hier.him");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "PaintScheme", ((Object) (new PaintSchemeFMPar1956())));
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "yearService", 1950F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "yearExpired", 1990F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "FlightModel", "FlightModels/CanberraMk8.fmd:CANBERRA");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "cockpitClass", ((Object) (new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitCanberraMk8.class, com.maddox.il2.objects.air.CockpitCanberraBombardier.class
        })));
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "LOSElevation", 0.74615F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 9, 9, 9, 9, 3, 3, 3, 3, 9,
            9, 0, 0, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 9, 9, 2, 2, 2,
            2, 9, 2, 2, 2, 2
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06",
            "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", 
            "_BombSpawn17", "_ExternalDev01", "_ExternalDev02","_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02","_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05",
            "_ExternalDev06", "_MGUN01", "_MGUN01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
            "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17",
            "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27",
            "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalDev07", "_ExternalDev08", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38",
            "_ExternalRock38", "_ExternalDev09",  "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36"
        });
        com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "default", new java.lang.String[] {
        		null, null, null, null, null, null
        });
        com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "none", new java.lang.String[] {
            null, null, null, null, null, null
        });
    }
}