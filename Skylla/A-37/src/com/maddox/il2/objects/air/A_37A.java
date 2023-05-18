package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

public class A_37A extends A_37 {
	
	public static boolean bChangedPit = false;
	
    public A_37A() {
    	super();
    }
    
  //for testing ---------------------------------------------------------------------------------------------------
    
    public void onAircraftLoaded() {
    	super.onAircraftLoaded();
    	
    }
    
    public void update(float f) {
    	super.update(f);

    }
    
  //-------------------------------------------------------------------------------------------------------------
    
    public void doSetSootState(final int i, final int j) {
        for (int k = 0; k < 2; ++k) {
            if (super.FM.AS.astateSootEffects[i][k] != null) {
                Eff3DActor.finish(super.FM.AS.astateSootEffects[i][k]);
            }
            super.FM.AS.astateSootEffects[i][k] = null;
        }
        if(i == 1) {
        	System.out.println("SKYLLA A-37 DEBUG: doSetSootState("+i+", "+j+")");
        }
        switch (j) {
            case 1: {
                //super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0f, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0f);
                //super.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0f, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0f);
            	super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine1ES_01"), null, 1.0f, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0f);
            	super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0f, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0f);
                break;
            }
            case 3: {
            	//super.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0f, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0f);
            	super.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine1EF_0"+(i+1)), null, 1.0f, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0f);
            }
            case 2: {
                //super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0f, "3DO/Effects/Aircraft/TurboZippo.eff", -1.0f);
            	super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine1EF_0"+(i+1)), null, 1.0f, "3DO/Effects/Aircraft/TurboZippo.eff", -1.0f);
            	break;
            }
            case 5: {
                //super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 3.0f, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1.0f);
            	super.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine1EF_0"+(i+1)), null, 3.0f, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1.0f);
            }
            case 4: {
                //super.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0f, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0f);
            	super.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_EngineEF_0"+(i+1)), null, 1.0f, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0f);
            	break;
            }
        }
    }
    
    
    //TODO
    static {
        Class class1 = A_37A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-37");
        Property.set(class1, "meshName", "3DO/Plane/A-37/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/A-37A.fmd:A_37A_FM");
        Property.set(class1, "cockpitClass", new Class[] {CockpitA_37.class}); 
        
        String gun        = "MGunMiniGun3000 ";
        String suu11      = "PylonSUU11A 1";
        String tank       = "FuelTankGun_Tank100gal_A37 1";
        String rocket     = "RocketGunFFARMk4_gn16 1";
        String mk82_500   = "BombGun500lb_Mk82_Mod1 1";
        String mk8215_500 = "BombGun500lb_Mk82_Mod1_Mk15";
        String mk81_250   = "BombGun250lb_Mk81_Mod1 1";
        String snakeeye   = "BombGunMk82SnakeEye_gn16 1";
        String daisy      = "BombGun500lbGP_AN_M64A1_M128Rod 1";
        String cbu24      = "BombGunCBU24_gn16 1"; //BombGunCBU24
        String m117       = "BombGun750lbGP_M117A1 1";
        String blu1       = "BombGun750lbInc_BLU1 1";
        String blu32      = "BombGun500lbInc_BLU32B 1";
        String blu23      = "BombGun500lbInc_BLU23B 1";
        String blu27      = "BombGun750lbInc_BLU27B";
        String lau3       = "Pylon_LAU3 1";
        String lau61      = "Pylon_LAU61 1";
        
        //and now, welcome to hook hell... brace yourselves, this will be in cod soon ..
        Aircraft.weaponTriggersRegister(class1, new int[] {
                0, 1, 1, 9, 9,  							//Guns & PylonSUU11A
                3, 3, 3, 3, 3, 3, 3, 3,						//DreamK weapon packs:  BombGun500lb_Mk82_Mod1, BombGun750lbGP_M117A1, BombGun500lbGP_AN_M64A1_M128Rod, 
                											//						BombGun500lb_Mk82_Mod1_Mk15, BombGun250lb_Mk81_Mod1, BombGun750lbInc_BLU27B, 
                											//						BombGun750lbInc_BLU1, BombGun500lbInc_BLU32B, BombGun500lbInc_BLU23B,
				//3, 3, 3, 3,	3, 3, 3, 3,						//BombGunCBU24 .. use BombGunCBU24_gn16 instead
				3, 3, 3, 3,	3, 3, 3, 3,						//_gn16 Weapons
                9, 9, 9, 9,	9, 9, 9, 9, 					//Pylon_LAU3, Pylon_LAU61, Tanks
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,	//RocketGunFFARMk4 (LAU3)
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,	//RocketGunFFARMk4 (LAU3)
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,	//RocketGunFFARMk4 (LAU3)
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,	//RocketGunFFARMk4 (LAU3)
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //RocketGunFFARMk4 (LAU61)
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2  //RocketGunFFARMk4 (LAU61)
            });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", 
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08",
            //"_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16",
            "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24",
            "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10",
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14",
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28",
            "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42",
            "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56",
            "_ExternalRock57", "_ExternalRock58", "_ExternalRock59", "_ExternalRock60", "_ExternalRock61", "_ExternalRock62", "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68", "_ExternalRock69", "_ExternalRock70", "_ExternalRock71", "_ExternalRock72", "_ExternalRock73", "_ExternalRock74", "_ExternalRock75", 
            "_ExternalRock76", "_ExternalRock77", "_ExternalRock78", "_ExternalRock79", "_ExternalRock80", "_ExternalRock81", "_ExternalRock82", "_ExternalRock83", "_ExternalRock84", "_ExternalRock85", "_ExternalRock86", "_ExternalRock87", "_ExternalRock88", "_ExternalRock89", "_ExternalRock90", "_ExternalRock91", "_ExternalRock92", "_ExternalRock93", "_ExternalRock94", 

        });        
        AircraftTools.weaponsRegister(class1, "default", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null        		
        });
        /* too heavy!
        AircraftTools.weaponsRegister(class1, "8xdt", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		tank, tank, tank, tank, tank, tank, tank, tank,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });*/
        AircraftTools.weaponsRegister(class1, "6xdt", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, tank, tank, tank, tank, tank, tank,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });
        AircraftTools.weaponsRegister(class1, "4xdt+2xsuu11", new String [] {
        		gun + "1800", gun + "1200", gun + "1200", suu11, suu11,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, tank, tank, tank, tank, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        }); 
        AircraftTools.weaponsRegister(class1, "4xdt+2xlau61", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, tank, tank, tank, tank, lau61, lau61,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket,
        		rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket, rocket
        });
        AircraftTools.weaponsRegister(class1, "2xdt+4x500", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, mk82_500, mk82_500, mk82_500, mk82_500, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, tank, tank,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });
        AircraftTools.weaponsRegister(class1, "2xdt+4xsnakeeye", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, snakeeye, snakeeye, snakeeye, snakeeye, null, null, 
        		null, null, null, null, null, null, tank, tank,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null        		
        });
        AircraftTools.weaponsRegister(class1, "2xdt+6x250", new String [] {
        		gun + "1800", null, null, null, null,
        		mk81_250, mk81_250, mk81_250, mk81_250, mk81_250, mk81_250, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, tank, tank,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });
        AircraftTools.weaponsRegister(class1, "8x500", new String [] {
        		gun + "1800", null, null, null, null,
        		mk82_500, mk82_500, mk82_500, mk82_500, mk82_500, mk82_500, mk82_500, mk82_500,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });
        AircraftTools.weaponsRegister(class1, "8xsnakeeye", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		snakeeye, snakeeye, snakeeye, snakeeye, snakeeye, snakeeye, snakeeye, snakeeye,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });
        AircraftTools.weaponsRegister(class1, "8x250", new String [] {
        		gun + "1800", null, null, null, null,
        		mk81_250, mk81_250, mk81_250, mk81_250, mk81_250, mk81_250, mk81_250, mk81_250,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });
        AircraftTools.weaponsRegister(class1, "4xcbu24", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, cbu24, cbu24, cbu24, cbu24,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null        		
        });
        AircraftTools.weaponsRegister(class1, "4xm117", new String [] {
        		gun + "1800", null, null, null, null,
        		null, null, null, null, m117, m117, m117, m117,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null        		
        });
        AircraftTools.weaponsRegister(class1, "none", new String [] {
        		null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        		null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        });      
    }
}

