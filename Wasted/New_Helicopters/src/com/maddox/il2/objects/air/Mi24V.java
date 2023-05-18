// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 05.10.2017 20:29:34
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mi24V.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;

import java.util.ArrayList;

public class Mi24V extends Mi24X  {

	public Mi24V() {
	}
	
	private void OperatorTurret() {
		Pilot pilot = (Pilot) FM;
		if ((pilot != null) && !Mission.isNet()) {
			if (isAI) {
				Actor actor = War.GetNearestEnemy(this, 1, 3000F);
				if (pilot != null && isAlive(actor) && !(actor instanceof BridgeSegment)) {
					Point3d point3d = new Point3d();
					actor.pos.getAbs(point3d);
					if (pos.getAbsPoint().distance(point3d) < 1700D) {
						point3d.sub(FM.Loc);
						FM.Or.transformInv(point3d);
						if (point3d.y < 0.0D) {
							FM.turret[1].target = actor;
							FM.turret[1].tMode = 2;
						}
					}
				} else if (actor != null) {
					if (FM.turret[1].target != null && !(FM.turret[1].target instanceof Aircraft) && !isAlive(FM.turret[1].target))
						FM.turret[1].target = null;
				}
			} 
		} else {
			Actor actor = victim;
			if (pilot != null && victim != null && isAlive(actor)) {
				Point3d point3d = new Point3d();
				actor.pos.getAbs(point3d);
				if (pos.getAbsPoint().distance(point3d) < 1700D) {
					point3d.sub(FM.Loc);
					FM.Or.transformInv(point3d);
					if (point3d.y < 0.0D) {
						FM.turret[1].target = actor;
						FM.turret[1].tMode = 2;
					}
				}
			} else if (actor != null) {
				if (FM.turret[1].target != null && !(FM.turret[1].target instanceof Aircraft) && !isAlive(FM.turret[1].target))
					FM.turret[1].target = null;
			}
		}
	}
	
	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -80F) {
				f = -80F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -60F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -80F) {
				f = -80F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -60F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void update(float f) {
		OperatorTurret();
		super.update(f);
	}
	
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.turret[0].bIsAIControlled = false;
	}
    
	static {
		Class class1 = com.maddox.il2.objects.air.Mi24V.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Mi-24V");
		Property.set(class1, "meshName", "3DO/Plane/Mi-24V/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1950F);
		Property.set(class1, "yearExpired", 1960.5F);
		Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HELIFMD");
		Property.set(class1, "cockpitClass", new Class[] {
				com.maddox.il2.objects.air.CockpitMi24.class,
				com.maddox.il2.objects.air.CockpitMi24_FLIR.class,
				com.maddox.il2.objects.air.CockpitMi24_GUNNER.class
		});
		Property.set(class1, "LOSElevation", 0.0F);
    	Aircraft.weaponTriggersRegister(class1, new int[] { 
    			11, 10, 9, 9, 9, 9, 2, 2, 2, 2, 
    			7,  7,  9, 9, 2, 2, 2, 2, 2, 2, 
    			2,  2,  9, 9, 9, 9, 3, 3, 3, 3, 
    			9,  9 
    	});
    	Aircraft.weaponHooksRegister(class1, new String[] { 
    			"_MGUN01",         "_BombSpawn01",    "_ExternalDev01", "_ExternalDev02", "_ExternalDev03",  "_ExternalDev04",  "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", 
    			"_Flare01",        "_Flare02",        "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10",
    			"_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09",  "_ExternalDev10",  "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
    			"_ExternalDev11",  "_ExternalDev12" 
    	});
        String s = "";
        ArrayList arraylist = new ArrayList();
        Property.set(class1, "weaponsList", arraylist);
        HashMapInt hashmapint = new HashMapInt();
        Property.set(class1, "weaponsMap", hashmapint);
        byte byte0 = 32;
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
		try 
		{
            s = "default";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "80xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AoutConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "80xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AoutConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "10xS-13";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB13inConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "20xS-13";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB13inConfig(a_lweaponslot);
			a_lweaponslot = InsertB13outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "10xS-13+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB13inConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "20xS-13+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB13inConfig(a_lweaponslot);
			a_lweaponslot = InsertB13outConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-250M46";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB250outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xFAB-250M46";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB250inConfig(a_lweaponslot);
			a_lweaponslot = InsertFAB250outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-250M46+40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB250outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-250M46+40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB250outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-500M46";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB500outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xFAB-500M46";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB500inConfig(a_lweaponslot);
			a_lweaponslot = InsertFAB500outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-500M46+40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = InsertFAB500outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-500M46+40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertFAB500outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-250(AO)";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK250outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xRBK-250(AO)";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK250inConfig(a_lweaponslot);
			a_lweaponslot = InsertRBK250outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-250(AO)+40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK250outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-250(AO)+40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK250outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-500(AO)";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK500outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xRBK-500(AO)";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK500inConfig(a_lweaponslot);
			a_lweaponslot = InsertRBK500outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-500(AO)+40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = InsertRBK500outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-500(AO)+40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertRBK500outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xKMGU-2(PTAB)";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertKMGU2outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xKMGU-2(PTAB)";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertKMGU2inConfig(a_lweaponslot);
			a_lweaponslot = InsertKMGU2outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xKMGU-2(PTAB)+40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertKMGU2outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xKMGU-2(PTAB)+40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertKMGU2outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xSAB-100-90";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertSAB100outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xSAB-100-90";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertSAB100inConfig(a_lweaponslot);
			a_lweaponslot = InsertSAB100outConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xSAB-100-90+40xS-8OFP2";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertSAB100outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xSAB-100-90+40xS-8OFP2+4x9M114";
			a_lweaponslot = GenerateDefaultConfig(byte0);
			a_lweaponslot = InsertSAB100outConfig(a_lweaponslot);
			a_lweaponslot = InsertB8V20AinConfig(a_lweaponslot);
			a_lweaponslot = Insert9M114Config(a_lweaponslot);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "none";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = null;
			a_lweaponslot[1] = null;
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
		} catch (Exception exception) {
		}
	}
}
