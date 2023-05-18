package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombJATO_J;
import com.maddox.il2.objects.weapons.MGunHo115k;
import com.maddox.il2.objects.weapons.MGunHo5s;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.AircraftTools;

public class Kikka extends ME_262B {

	/**
	 * @author SAS~Skylla
	 * @see com.maddox.il2.objects.air.CockpitKikka.java, com.maddox.objects.weapons.BombJATO_J.java
	 * 
	 * useful links:
	 *  - http://hyperscale.com/2007/features/kikkacw_1.htm
	 *  - http://www.militaryfactory.com/aircraft/detail.asp?aircraft_id=1265
	 *  - http://kanyiko.deviantart.com/art/What-if-the-Nakajima-Kikka-492969725
	 *  
	 * TODO:
	 *  > [x] Loadouts [DONE]
	 *  > [x] Boosters [DONE]
	 *  > [x] stop gears from moving when boosters are attached [DONE]
	 *  > [x] moveCockpitDoor [FIXED]
	 *  > [x] ailerons [FIXED]
	 *  > [x] gun hooks [DONE]
	 *  > [x] adjust bomb & pylon places [DONE]
	 *  > [x] CockpitClass moveDoor [DONE]
	 * 	> [-] reduce RATO burning time to 9 seconds, adjust power to equal 800kg.
	 *  > [-] build new FM
	 *  > [x] moveWingFold [DONE]
	**/
	
	
	private Bomb      booster[] = { null, null };
	protected boolean bHasBoosters;
	protected long    boosterFireOutTime;
	private boolean   gearMoved = false;
	
	private static final double boosterPower = 10000D;
	private static final long boosterRunTime = 9000L;
	   
	public Kikka() {
		this.bHasBoosters = true;
	    this.boosterFireOutTime = -1L;
	}  
	   
	public void destroy() {
		this.doCutBoosters();
	    super.destroy();
	}
	   
	public void doCutBoosters() {
		for (int i = 0; i < booster.length; i++)
			if (this.booster[i] != null) {
				this.booster[i].start();
	            this.booster[i] = null;
	        }
	   }
	   
	public void doFireBoosters() {
		for(int i = 0; i < booster.length; i++) {
			//looks way better:
			Eff3DActor.New(this, this.findHook("_Booster" + (i+1)), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", (float)(boosterRunTime/1000));
			//old / 4.12:
			//Eff3DActor.New(this, this.findHook("_Booster" + (i+1)), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
		}
	}
	
	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
	    case 38: // '&'
	    	this.doCutBoosters();
	        this.FM.AS.setGliderBoostOff();
	        this.bHasBoosters = false;
	        break;
	    }
	    return super.cutFM(i, j, actor);
	}
	   
	private boolean boostersAllowed() {
		if (!this.thisWeaponsName.startsWith("rato")) {
			bHasBoosters = false;
			return false;
		}
		return true;
	 }
	
	public void moveCockpitDoor(float var1) {
		this.resetYPRmodifier();
		Aircraft.xyz[0] =  Aircraft.cvt(var1, 0.01F, 0.99F, 0.0F, -0.68F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr); 
	    if(Config.isUSE_RENDER()) {
	    	if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) {
	    		Main3D.cur3D().cockpits[0].onDoorMoved(var1);
	        }
	        this.setDoorSnd(var1);
	    }
	}
	 
	public void moveGear(float f) {
		if(bHasBoosters) {
			FM.CT.GearControl = 1.0F;
			//if(FM.isPlayers())
			//	HUD.log("Can't raise gear with boosters attached!"); 
			if(this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 5F)
				gearMoved = true;
		} else {
			super.moveGear(f);
		}
	}
	
	protected void moveWingFold(HierMesh var1, float var2) {
		float offy = 2.87F; 	//static offset y
		float offz = -3.60F; 	//static offset z
		float offym = 0.18F;	//moving offset y
		float offzm = 0.55F;	//moving offset z
		float turndeg = 150.0F; //angle of wing movement in degree

		//left wing:
		float y = (float)Math.toRadians(Aircraft.cvt(var2, 0.0F, 1.0F, -90.0F, (-90.0F + turndeg)));
		float z = (float)Math.toRadians(Aircraft.cvt(var2, 0.0F, 1.0F, 0.0F, turndeg));
		Aircraft.ypr[2] =  Aircraft.cvt(var2, 0.0F, 1.0F, 0.0F, -turndeg);
		Aircraft.xyz[1] =  offy*(float)Math.sin(y) - offym*(float)Math.cos(y) + offy;
		Aircraft.xyz[2] =  offz*(float)Math.sin(z) + offzm*(float)Math.sin(Aircraft.cvt(var2, 0.0F, 1.0F, 0.0F, (float)Math.PI));
		var1.chunkSetLocate("WingLOut_D0", Aircraft.xyz, Aircraft.ypr); 
		//right wing:
		y = (float)Math.toRadians(Aircraft.cvt(var2, 0.0F, 1.0F, -90.0F, (-90.0F + turndeg)));
		Aircraft.ypr[2] =  Aircraft.cvt(var2, 0.0F, 1.0F, 0.0F, turndeg);
		Aircraft.xyz[1] =  -offy*(float)Math.sin(y) + offym*(float)Math.cos(y) - offy;
		var1.chunkSetLocate("WingROut_D0", Aircraft.xyz, Aircraft.ypr);
	}
	
	public void moveWingFold(float var1) {
		this.moveWingFold(this.hierMesh(), var1);
	}
	
	protected void moveAileron(float var1) {
		this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, +30.0F * var1, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30.0F * var1, 0.0F);
	}
	
	public void rareAction(float var1, boolean var2) {
		super.rareAction(var1, var2);
	    if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && var2 && super.FM instanceof Pilot) {
	    	Pilot var3 = (Pilot)super.FM;
	        if(var3.get_maneuver() == 63 && var3.target != null) {
	        	Point3d var4 = new Point3d(var3.target.Loc);
	            var4.sub(super.FM.Loc);
	            super.FM.Or.transformInv((Tuple3d)var4);
	        }
	    }
	}
	
	   public void doMurderPilot(int var1) {
		   switch(var1) {
		   case 0:
			   this.hierMesh().chunkVisible("Pilot1_D0", false);
		       this.hierMesh().chunkVisible("Head1_D0", false);
		       this.hierMesh().chunkVisible("Pilot1_D1", true);
		       if(!super.FM.AS.bIsAboutToBailout) {
		    	   this.hierMesh().chunkVisible("Gore1_D0", true);
		       }
		       break;
		   case 1:
		       this.hierMesh().chunkVisible("Pilot2_D0", false);
		       this.hierMesh().chunkVisible("Head2_D0", false);
		       this.hierMesh().chunkVisible("Pilot2_D1", true);
		       if(!super.FM.AS.bIsAboutToBailout) {
		          this.hierMesh().chunkVisible("Gore3_D0", true);
		       }
		   }
	   }

	   public void update(float var1) {
		   //boosters:
		   if (this.bHasBoosters && boostersAllowed()) {
	           if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 300F && this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
	               this.doCutBoosters();
	               this.FM.AS.setGliderBoostOff();
	               this.bHasBoosters = false;
	               if(gearMoved) {
	            	   FM.CT.GearControl = 0.0F;
	               }	  
	           }
	           if (this.bHasBoosters && this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6 && this.FM.getSpeedKMH() > 20F) {
	               this.boosterFireOutTime = Time.current() + boosterRunTime;
	               this.doFireBoosters();
	               this.FM.AS.setGliderBoostOn();
	           }
	           if (this.bHasBoosters && this.boosterFireOutTime > 0L) {
	               if (Time.current() < this.boosterFireOutTime)
	            	   //booster thrust:
	            	   this.FM.producedAF.x += boosterPower * booster.length;  	   
	               if (Time.current() > this.boosterFireOutTime + 10000L) {
	                   this.doCutBoosters();
	                   this.FM.AS.setGliderBoostOff();
	                   this.bHasBoosters = false;
	                   if(gearMoved) {
		            	   FM.CT.GearControl = 0.0F;
	                   }
	               }
	           }
	       }		   
		   super.update(var1);
	   }
	   
	   public void onAircraftLoaded() {
		   super.onAircraftLoaded();
		   if((getBulletEmitterByHookName("_CANNON01") instanceof MGunHo5s && getBulletEmitterByHookName("_CANNON02") instanceof MGunHo5s) || 
				   (getBulletEmitterByHookName("_CANNON01") instanceof MGunHo115k && getBulletEmitterByHookName("_CANNON02") instanceof MGunHo115k)) {
			   this.hierMesh().chunkVisible("20mmL_D0", true);
			   this.hierMesh().chunkVisible("20mmR_D0", true);
		   } else {
			   this.hierMesh().chunkVisible("20mmL_D0", false);
			   this.hierMesh().chunkVisible("20mmR_D0", false);
		   }
		   if(boostersAllowed()) {
		    	  for (int i = 0; i < booster.length; i++) {
		          	try {
		              	this.booster[i] = new BombJATO_J(); 
		              	this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
		              	this.booster[i].pos.resetAsBase();
		              	this.booster[i].drawing(true);
		          	} catch (Exception e) {
		              	this.debugprintln("Structure corrupt - can't hang RATO-Booster:");
		              	e.printStackTrace();
		          	}
		      	}
		   }
	   }

	   static Class class$(String var0) {
		   try {
			   return Class.forName(var0);
		   } catch (ClassNotFoundException var2) {
			   throw new NoClassDefFoundError(var2.getMessage());
		   }
	   }
	
	   
	static {
		final Class clazz = Kikka.class;
        new SPAWN(clazz);
        Property.set(clazz, "iconFar_shortClassName", "Kikka");
        Property.set(clazz, "meshName", "3DO/Plane/Kikka/hier.him");
        Property.set(clazz, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(clazz, "yearService", 1946.1f);
        Property.set(clazz, "yearExpired", 1946.5f);
        Property.set(clazz, "FlightModel", "FlightModels/Kikka.fmd");
        Property.set(clazz, "cockpitClass", new Class[] { CockpitKikka.class });
        Property.set(clazz, "LOSElevation", 0.7498f);
        Aircraft.weaponTriggersRegister(clazz, new int[] { 0, 0, 9, 3 });
        Aircraft.weaponHooksRegister(clazz, new String[] { "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01" });
        /* 
         * loadouts for
         * > interceptor:
         *   - 2x MGunHo5s 70 \ 2x MGunHo115k 50
         *  
         * > fieldMod:
         *   - 2x 800kg RATO-Boosters
         *   - 1x 30mm Type 5 MK 
         * 
         * > JaBo:
         *   - 2x 800kg RATO-Boosters
         *   - 2x MGunHo5s 180 \ 2x MGunHo115k 140
         *   - 1x 250kg \ 1x 500kg [\ 1x 800kg] <- cut out the last; it is ridiculous .. 
         */
        AircraftTools.weaponsRegister(clazz, "default", new String[] { "MGunHo5s 70", "MGunHo5s 70", null, null });
        AircraftTools.weaponsRegister(clazz, "2x30", new String [] {"MGunHo115k 500", "MGunHo115k 50", null, null });
        AircraftTools.weaponsRegister(clazz, "rato+2x20+250", new String [] {"MGunHo5s 70", "MGunHo5s 70", "PylonKI43PLN1 1", "BombGun250kgJ 1" });
        AircraftTools.weaponsRegister(clazz, "rato+2x30+250", new String [] {"MGunHo115k 50", "MGunHo115k 50", "PylonKI43PLN1 1", "BombGun250kgJ 1" });
        AircraftTools.weaponsRegister(clazz, "rato+2x20+500", new String [] {"MGunHo5s 70", "MGunHo5s 70", "PylonKI43PLN1 1", "BombGun500kgJ 1" });
        AircraftTools.weaponsRegister(clazz, "rato+2x30+500", new String [] {"MGunHo115k 50", "MGunHo115k 50", "PylonKI43PLN1 1", "BombGun500kgJ 1" });
        AircraftTools.weaponsRegister(clazz, "none", new String[] { null, null, null, null });
     }
}
