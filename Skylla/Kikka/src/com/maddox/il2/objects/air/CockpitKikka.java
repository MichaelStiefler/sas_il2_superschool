package com.maddox.il2.objects.air;

public class CockpitKikka extends CockpitME_262 {
	
	/**
	 * @author SAS~Skylla
	 * @see Kikka.java
	 * 
	 * TODO
	 *  > [x] move Cockpit Door [DONE]
	**/
	
	public void reflectWorldToInstruments(float f) {
		super.reflectWorldToInstruments(f);
		this.resetYPRmodifier();
	      Cockpit.xyz[0] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -0.65F);
	      super.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
	}
	
}

/* not needed now, kept in case of further need.

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.CockpitPilot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

public class CockpitKikka extends CockpitPilot {

   private CockpitKikka.Variables setOld = new CockpitKikka.Variables((CockpitKikka.Variables)null);
   private CockpitKikka.Variables setNew = new CockpitKikka.Variables((CockpitKikka.Variables)null);
   private CockpitKikka.Variables setTmp;
   public Vector3f w = new Vector3f();
   private Point3d tmpP = new Point3d();
   private Vector3d tmpV = new Vector3d();
   private Gun[] gun = new Gun[4];
   private boolean bNeedSetUp = true;
   private float pictAiler = 0.0F;
   private float pictElev = 0.0F;
   private boolean bU4 = false;
   private static final float[] speedometerIndScale = new float[]{0.0F, 0.0F, 0.0F, 17.0F, 35.5F, 57.5F, 76.0F, 95.0F, 112.0F};
   private static final float[] speedometerTruScale = new float[]{0.0F, 32.75F, 65.5F, 98.25F, 131.0F, 164.0F, 200.0F, 237.0F, 270.5F, 304.0F, 336.0F};
   private static final float[] variometerScale = new float[]{0.0F, 13.5F, 27.0F, 43.5F, 90.0F, 142.5F, 157.0F, 170.5F, 184.0F, 201.5F, 214.5F, 226.0F, 239.5F, 253.0F, 266.0F};
   private static final float[] rpmScale = new float[]{0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55.0F, 77.5F, 104.0F, 133.5F, 162.5F, 192.0F, 224.0F, 254.0F, 255.5F, 260.0F};
   private static final float[] fuelScale = new float[]{0.0F, 11.0F, 31.0F, 57.0F, 84.0F, 103.5F};


   protected float waypointAzimuth() {
      WayPoint waypoint = super.fm.AP.way.curr();
      if(waypoint == null) {
         return 0.0F;
      } else {
         waypoint.getP(this.tmpP);
         this.tmpV.sub(this.tmpP, super.fm.Loc);
         return (float)(57.29577951308232D * Math.atan2(this.tmpV.y, this.tmpV.x));
      }
   }

   public CockpitKikka() {
      super("3DO/Cockpit/Me-262/hier.him", "he111");
      super.cockpitNightMats = new String[]{"2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank"};
      this.setNightMats(false);
      this.setNew.dimPosition = 1.0F;
      this.interpPut(new CockpitKikka.Interpolater(), (Object)null, Time.current(), (Message)null);
   }

   public void removeCanopy() {
      super.mesh.chunkVisible("Canopy", false);
      super.mesh.chunkVisible("Z_Holes2_D1", false);
      super.mesh.chunkVisible("Z_Holes1_D1", false);
   }

   public void reflectWorldToInstruments(float f) {
      if(this.bNeedSetUp) {
         this.reflectPlaneMats();
         this.bNeedSetUp = false;
      }

      if(this.gun[0] == null) {
         this.gun[0] = ((Aircraft)super.fm.actor).getGunByHookName("_CANNON03");
         this.gun[1] = ((Aircraft)super.fm.actor).getGunByHookName("_CANNON01");
         this.gun[2] = ((Aircraft)super.fm.actor).getGunByHookName("_CANNON02");
         this.gun[3] = ((Aircraft)super.fm.actor).getGunByHookName("_CANNON04");
      }

      if(super.fm.isTick(44, 0)) {
         super.mesh.chunkVisible("Z_GearLGreen1", super.fm.CT.getGear() == 1.0F && super.fm.Gears.lgear);
         super.mesh.chunkVisible("Z_GearRGreen1", super.fm.CT.getGear() == 1.0F && super.fm.Gears.rgear);
         super.mesh.chunkVisible("Z_GearCGreen1", super.fm.CT.getGear() == 1.0F);
         super.mesh.chunkVisible("Z_GearLRed1", super.fm.CT.getGear() == 0.0F || super.fm.Gears.isAnyDamaged());
         super.mesh.chunkVisible("Z_GearRRed1", super.fm.CT.getGear() == 0.0F || super.fm.Gears.isAnyDamaged());
         super.mesh.chunkVisible("Z_GearCRed1", super.fm.CT.getGear() == 0.0F);
         if(!this.bU4) {
            super.mesh.chunkVisible("Z_GunLamp01", !this.gun[0].haveBullets());
            super.mesh.chunkVisible("Z_GunLamp02", !this.gun[1].haveBullets());
            super.mesh.chunkVisible("Z_GunLamp03", !this.gun[2].haveBullets());
            super.mesh.chunkVisible("Z_GunLamp04", !this.gun[3].haveBullets());
         }

         super.mesh.chunkVisible("Z_MachLamp", super.fm.getSpeed() / Atmosphere.sonicSpeed((float)super.fm.Loc.z) > 0.8F);
         super.mesh.chunkVisible("Z_CabinLamp", super.fm.Loc.z > 12000.0D);
         super.mesh.chunkVisible("Z_FuelLampV", super.fm.M.fuel < 300.0F);
         super.mesh.chunkVisible("Z_FuelLampIn", super.fm.M.fuel < 300.0F);
      }

      super.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45.0F), 0.0F, 0.0F);
    //TODO skylla Cockpit Door
      this.resetYPRmodifier();
      //super.mesh.chunkSetAngles("Canopy", 0.0F, 0.0F, -100.0F * super.fm.CT.getCockpitDoor());
      Cockpit.xyz[0] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -0.65F);
      super.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
    //------------------------
      this.resetYPRmodifier();
      Cockpit.xyz[1] = super.fm.CT.GearControl == 0.0F && super.fm.CT.getGear() != 0.0F?-0.0107F:0.0F;
      super.mesh.chunkSetLocate("Z_GearEin", Cockpit.xyz, Cockpit.ypr);
      Cockpit.xyz[1] = super.fm.CT.GearControl == 1.0F && super.fm.CT.getGear() != 1.0F?-0.0107F:0.0F;
      super.mesh.chunkSetLocate("Z_GearAus", Cockpit.xyz, Cockpit.ypr);
      this.resetYPRmodifier();
      Cockpit.xyz[1] = super.fm.CT.FlapsControl < super.fm.CT.getFlap()?-0.0107F:0.0F;
      super.mesh.chunkSetLocate("Z_FlapEin", Cockpit.xyz, Cockpit.ypr);
      Cockpit.xyz[1] = super.fm.CT.FlapsControl > super.fm.CT.getFlap()?-0.0107F:0.0F;
      super.mesh.chunkSetLocate("Z_FlapAus", Cockpit.xyz, Cockpit.ypr);
      super.mesh.chunkSetAngles("Z_Column", 10.0F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * super.fm.CT.AileronControl), 0.0F, 10.0F * (this.pictElev = 0.85F * this.pictElev + 0.15F * super.fm.CT.ElevatorControl));
      this.resetYPRmodifier();
      if(super.fm.CT.saveWeaponControl[0]) {
         Cockpit.xyz[2] = -0.0025F;
      }

      super.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
      this.resetYPRmodifier();
      if(super.fm.CT.saveWeaponControl[2] || super.fm.CT.saveWeaponControl[3]) {
         Cockpit.xyz[2] = -0.00325F;
      }

      super.mesh.chunkSetLocate("Z_Columnbutton2", Cockpit.xyz, Cockpit.ypr);
      super.mesh.chunkSetAngles("Z_PedalStrut", 20.0F * super.fm.CT.getRudder(), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_LeftPedal", -20.0F * super.fm.CT.getRudder(), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_RightPedal", -20.0F * super.fm.CT.getRudder(), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_ThrottleL", 0.0F, -75.0F * this.interp(this.setNew.throttlel, this.setOld.throttlel, f), 0.0F);
      super.mesh.chunkSetAngles("Z_ThrottleR", 0.0F, -75.0F * this.interp(this.setNew.throttler, this.setOld.throttler, f), 0.0F);
      super.mesh.chunkSetAngles("Z_FuelLeverL", super.fm.EI.engines[0].getControlMagnetos() == 3?6.5F:0.0F, 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_FuelLeverR", super.fm.EI.engines[1].getControlMagnetos() == 3?6.5F:0.0F, 0.0F, 0.0F);
      this.resetYPRmodifier();
      Cockpit.xyz[1] = 0.03675F * super.fm.CT.getTrimElevatorControl();
      super.mesh.chunkSetLocate("Z_TailTrim", Cockpit.xyz, Cockpit.ypr);
      if(super.fm.CT.Weapons[3] != null && !super.fm.CT.Weapons[3][0].haveBullets()) {
         super.mesh.chunkSetAngles("Z_Bombbutton", 0.0F, 53.0F, 0.0F);
      }

      super.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt((float)this.gun[1].countBullets(), 0.0F, 100.0F, 0.0F, -7.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt((float)this.gun[2].countBullets(), 0.0F, 100.0F, 0.0F, -7.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float)super.fm.Loc.z, super.fm.getSpeedKMH()), 100.0F, 400.0F, 2.0F, 8.0F), speedometerIndScale), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(super.fm.getSpeedKMH(), 100.0F, 1000.0F, 1.0F, 10.0F), speedometerTruScale), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 16000.0F, 0.0F, 360.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000.0F, 0.0F, 7200.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24.0F, 0.0F, 720.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Second1", this.cvt(World.getTimeofDay() % 1.0F * 60.0F % 1.0F, 0.0F, 1.0F, 0.0F, 360.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_TurnBank1", super.fm.Or.getTangage(), 0.0F, super.fm.Or.getKren());
      super.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -this.cvt(this.getBall(6.0D), -6.0F, 6.0F, -7.5F, 7.5F));
      this.w.set((Tuple3d)super.fm.getW());
      super.fm.Or.transform((Tuple3f)this.w);
      super.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, -50.0F, 50.0F));
      super.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -20.0F, 50.0F, 0.0F, 14.0F), variometerScale), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_RPML", this.floatindex(this.cvt(super.fm.EI.engines[0].getRPM() * 10.0F * 0.25F, 2000.0F, 14000.0F, 2.0F, 14.0F), rpmScale), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_RPMR", this.floatindex(this.cvt(super.fm.EI.engines[1].getRPM() * 10.0F * 0.25F, 2000.0F, 14000.0F, 2.0F, 14.0F), rpmScale), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Compass1", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_Compass2", -this.interp(this.setNew.waypointAzimuth, this.setOld.waypointAzimuth, f), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_GasPressureL", this.cvt(super.fm.M.fuel > 1.0F?0.6F * super.fm.EI.engines[0].getPowerOutput():0.0F, 0.0F, 1.0F, 0.0F, 273.5F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_GasPressureR", this.cvt(super.fm.M.fuel > 1.0F?0.6F * super.fm.EI.engines[1].getPowerOutput():0.0F, 0.0F, 1.0F, 0.0F, 273.5F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_GasTempL", this.cvt(super.fm.EI.engines[0].tWaterOut, 300.0F, 1000.0F, 0.0F, 96.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_GasTempR", this.cvt(super.fm.EI.engines[1].tWaterOut, 300.0F, 1000.0F, 0.0F, 96.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_OilPressureL", this.cvt(1.0F + 0.005F * super.fm.EI.engines[0].tOilOut, 0.0F, 10.0F, 0.0F, 278.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_OilPressureR", this.cvt(1.0F + 0.005F * super.fm.EI.engines[1].tOilOut, 0.0F, 10.0F, 0.0F, 278.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_FuelPressL", this.cvt(super.fm.M.fuel > 1.0F?80.0F * super.fm.EI.engines[0].getPowerOutput() * super.fm.EI.engines[0].getReadyness():0.0F, 0.0F, 160.0F, 0.0F, 278.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_FuelPressR", this.cvt(super.fm.M.fuel > 1.0F?80.0F * super.fm.EI.engines[1].getPowerOutput() * super.fm.EI.engines[1].getReadyness():0.0F, 0.0F, 160.0F, 0.0F, 278.0F), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_FuelRemainV", this.floatindex(this.cvt(super.fm.M.fuel / 0.72F, 0.0F, 1000.0F, 0.0F, 5.0F), fuelScale), 0.0F, 0.0F);
      super.mesh.chunkSetAngles("Z_FuelRemainIn", this.floatindex(this.cvt(super.fm.M.fuel / 0.72F, 0.0F, 1000.0F, 0.0F, 5.0F), fuelScale), 0.0F, 0.0F);
   }

   public void reflectCockpitState() {
      if((super.fm.AS.astateCockpitState & 4) != 0) {
         super.mesh.chunkVisible("HullDamage2", true);
         super.mesh.chunkVisible("XGlassDamage4", true);
         super.mesh.chunkVisible("Speedometer1", false);
         super.mesh.chunkVisible("Speedometer1_D1", true);
         super.mesh.chunkVisible("Z_Speedometer1", false);
         super.mesh.chunkVisible("Z_Speedometer2", false);
         super.mesh.chunkVisible("RPML", false);
         super.mesh.chunkVisible("RPML_D1", true);
         super.mesh.chunkVisible("Z_RPML", false);
         super.mesh.chunkVisible("FuelRemainV", false);
         super.mesh.chunkVisible("FuelRemainV_D1", true);
         super.mesh.chunkVisible("Z_FuelRemainV", false);
      }

      if((super.fm.AS.astateCockpitState & 8) != 0) {
         super.mesh.chunkVisible("HullDamage4", true);
         super.mesh.chunkVisible("XGlassDamage3", true);
         super.mesh.chunkVisible("Altimeter1", false);
         super.mesh.chunkVisible("Altimeter1_D1", true);
         super.mesh.chunkVisible("Z_Altimeter1", false);
         super.mesh.chunkVisible("Z_Altimeter2", false);
         super.mesh.chunkVisible("GasPressureL", false);
         super.mesh.chunkVisible("GasPressureL_D1", true);
         super.mesh.chunkVisible("Z_GasPressureL", false);
      }

      if((super.fm.AS.astateCockpitState & 16) != 0) {
         super.mesh.chunkVisible("HullDamage1", true);
         super.mesh.chunkVisible("XGlassDamage4", true);
         super.mesh.chunkVisible("RPMR", false);
         super.mesh.chunkVisible("RPMR_D1", true);
         super.mesh.chunkVisible("Z_RPMR", false);
         super.mesh.chunkVisible("FuelPressR", false);
         super.mesh.chunkVisible("FuelPressR_D1", true);
         super.mesh.chunkVisible("Z_FuelPressR", false);
      }

      if((super.fm.AS.astateCockpitState & 32) != 0) {
         super.mesh.chunkVisible("HullDamage3", true);
         super.mesh.chunkVisible("XGlassDamage3", true);
         super.mesh.chunkVisible("GasPressureR", false);
         super.mesh.chunkVisible("GasPressureR_D1", true);
         super.mesh.chunkVisible("Z_GasPressureR", false);
      }

      if((super.fm.AS.astateCockpitState & 1) != 0) {
         super.mesh.chunkVisible("XGlassDamage1", true);
         super.mesh.chunkVisible("XGlassDamage2", true);
         super.mesh.chunkVisible("Climb", false);
         super.mesh.chunkVisible("Climb_D1", true);
         super.mesh.chunkVisible("Z_Climb1", false);
         super.mesh.chunkVisible("FuelPressR", false);
         super.mesh.chunkVisible("FuelPressR_D1", true);
         super.mesh.chunkVisible("Z_FuelPressR", false);
      }

      if((super.fm.AS.astateCockpitState & 2) != 0) {
         super.mesh.chunkVisible("XGlassDamage1", true);
         super.mesh.chunkVisible("HullDamage1", true);
         super.mesh.chunkVisible("HullDamage2", true);
         super.mesh.chunkVisible("Revi_D0", false);
         super.mesh.chunkVisible("Revi_D1", true);
         super.mesh.chunkVisible("Z_Z_RETICLE", false);
         super.mesh.chunkVisible("Z_Z_MASK", false);
         super.mesh.chunkVisible("FuelPressL", false);
         super.mesh.chunkVisible("FuelPressL_D1", true);
         super.mesh.chunkVisible("Z_FuelPressL", false);
      }

      if((super.fm.AS.astateCockpitState & 64) != 0) {
         super.mesh.chunkVisible("HullDamage1", true);
         super.mesh.chunkVisible("Altimeter1", false);
         super.mesh.chunkVisible("Altimeter1_D1", true);
         super.mesh.chunkVisible("Z_Altimeter1", false);
         super.mesh.chunkVisible("Z_Altimeter2", false);
         super.mesh.chunkVisible("Climb", false);
         super.mesh.chunkVisible("Climb_D1", true);
         super.mesh.chunkVisible("Z_Climb1", false);
         super.mesh.chunkVisible("AFN", false);
         super.mesh.chunkVisible("AFN_D1", true);
         super.mesh.chunkVisible("Z_AFN1", false);
         super.mesh.chunkVisible("Z_AFN2", false);
         super.mesh.chunkVisible("FuelPressL", false);
         super.mesh.chunkVisible("FuelPressL_D1", true);
         super.mesh.chunkVisible("Z_FuelPressL", false);
         super.mesh.chunkVisible("FuelRemainIn", false);
         super.mesh.chunkVisible("FuelRemainIn_D1", true);
         super.mesh.chunkVisible("Z_FuelRemainIn", false);
      }

      super.fm.AS.getClass();
      this.retoggleLight();
   }

   protected void reflectPlaneMats() {
      HierMesh hiermesh = this.aircraft().hierMesh();
      Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
      super.mesh.materialReplace("Gloss1D0o", mat);
      if(this.aircraft() instanceof ME_262A1AU4) {
         super.mesh.chunkVisible("Z_Ammo262U4", true);
         this.bU4 = true;
      }

   }

   public void toggleDim() {
      super.cockpitDimControl = !super.cockpitDimControl;
   }

   public void toggleLight() {
      super.cockpitLightControl = !super.cockpitLightControl;
      if(super.cockpitLightControl) {
         this.setNightMats(true);
      } else {
         this.setNightMats(false);
      }

   }

   private void retoggleLight() {
      if(super.cockpitLightControl) {
         this.setNightMats(false);
         this.setNightMats(true);
      } else {
         this.setNightMats(true);
         this.setNightMats(false);
      }

   }

   static CockpitKikka.Variables access$0(CockpitKikka var0) {
      return var0.setOld;
   }

   static void access$1(CockpitKikka var0, CockpitKikka.Variables var1) {
      var0.setTmp = var1;
   }

   static CockpitKikka.Variables access$2(CockpitKikka var0) {
      return var0.setNew;
   }

   static void access$3(CockpitKikka var0, CockpitKikka.Variables var1) {
      var0.setOld = var1;
   }

   static CockpitKikka.Variables access$4(CockpitKikka var0) {
      return var0.setTmp;
   }

   static void access$5(CockpitKikka var0, CockpitKikka.Variables var1) {
      var0.setNew = var1;
   }

   private class Variables {

      float altimeter;
      float throttlel;
      float throttler;
      float azimuth;
      float waypointAzimuth;
      float vspeed;
      float dimPosition;
      final CockpitKikka this$1;


      private Variables() {
         this.this$1 = CockpitKikka.this;
      }

      Variables(CockpitKikka.Variables var2) {
         this();
      }
   }

   static class NamelessClass952318865 {
   }

   class Interpolater extends InterpolateRef {

      final CockpitKikka this$3 = CockpitKikka.this;

      public boolean tick() {
         this.this$3.setTmp = this.this$3.setOld;
         this.this$3.setOld = this.this$3.setNew;
         this.this$3.setNew = this.this$3.setTmp;
         this.this$3.setNew.altimeter = this.this$3.fm.getAltitude();
         this.this$3.setNew.throttlel = (10.0F * this.this$3.setOld.throttlel + this.this$3.fm.EI.engines[0].getControlThrottle()) / 11.0F;
         this.this$3.setNew.throttler = (10.0F * this.this$3.setOld.throttler + this.this$3.fm.EI.engines[1].getControlThrottle()) / 11.0F;
         this.this$3.setNew.azimuth = this.this$3.fm.Or.getYaw();
         CockpitKikka.Variables var10000;
         if(this.this$3.setOld.azimuth > 270.0F && this.this$3.setNew.azimuth < 90.0F) {
            var10000 = this.this$3.setOld;
            var10000.azimuth -= 360.0F;
         }

         if(this.this$3.setOld.azimuth < 90.0F && this.this$3.setNew.azimuth > 270.0F) {
            var10000 = this.this$3.setOld;
            var10000.azimuth += 360.0F;
         }

         this.this$3.setNew.waypointAzimuth = (10.0F * this.this$3.setOld.waypointAzimuth + (this.this$3.waypointAzimuth() - this.this$3.setOld.azimuth) + World.Rnd().nextFloat(-30.0F, 30.0F)) / 11.0F;
         this.this$3.setNew.vspeed = (299.0F * this.this$3.setOld.vspeed + this.this$3.fm.getVertSpeed()) / 300.0F;
         if(this.this$3.cockpitDimControl) {
            if(this.this$3.setNew.dimPosition > 0.0F) {
               this.this$3.setNew.dimPosition = this.this$3.setOld.dimPosition - 0.05F;
            }
         } else if(this.this$3.setNew.dimPosition < 1.0F) {
            this.this$3.setNew.dimPosition = this.this$3.setOld.dimPosition + 0.05F;
         }

         return true;
      }
   }
}
*/