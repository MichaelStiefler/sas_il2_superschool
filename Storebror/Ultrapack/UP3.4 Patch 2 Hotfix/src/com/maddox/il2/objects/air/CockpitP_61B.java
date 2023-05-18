package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_61B extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_61B.this.fm != null) {
                CockpitP_61B.this.setTmp = CockpitP_61B.this.setOld;
                CockpitP_61B.this.setOld = CockpitP_61B.this.setNew;
                CockpitP_61B.this.setNew = CockpitP_61B.this.setTmp;
                CockpitP_61B.this.setNew.trim = (0.92F * CockpitP_61B.this.setOld.trim) + (0.08F * CockpitP_61B.this.fm.CT.getTrimElevatorControl());
                CockpitP_61B.this.setNew.throttle1 = (0.85F * CockpitP_61B.this.setOld.throttle1) + (CockpitP_61B.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitP_61B.this.setNew.throttle2 = (0.85F * CockpitP_61B.this.setOld.throttle2) + (CockpitP_61B.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitP_61B.this.setNew.prop1 = (0.85F * CockpitP_61B.this.setOld.prop1) + (CockpitP_61B.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitP_61B.this.setNew.prop2 = (0.85F * CockpitP_61B.this.setOld.prop2) + (CockpitP_61B.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitP_61B.this.setNew.mix1 = (0.85F * CockpitP_61B.this.setOld.mix1) + (CockpitP_61B.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitP_61B.this.setNew.mix2 = (0.85F * CockpitP_61B.this.setOld.mix2) + (CockpitP_61B.this.fm.EI.engines[1].getControlMix() * 0.15F);
                CockpitP_61B.this.setNew.man1 = 0.92F * CockpitP_61B.this.setOld.man1 + 0.08F * CockpitP_61B.this.fm.EI.engines[0].getManifoldPressure();
                CockpitP_61B.this.setNew.man2 = 0.92F * CockpitP_61B.this.setOld.man2 + 0.08F * CockpitP_61B.this.fm.EI.engines[1].getManifoldPressure();
                CockpitP_61B.this.setNew.altimeter = CockpitP_61B.this.fm.getAltitude();
                float f = CockpitP_61B.this.waypointAzimuth();
                CockpitP_61B.this.setNew.azimuth.setDeg(CockpitP_61B.this.setOld.azimuth.getDeg(1.0F), CockpitP_61B.this.fm.Or.azimut());
                CockpitP_61B.this.setNew.waypointAzimuth.setDeg(CockpitP_61B.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                CockpitP_61B.this.setNew.radioCompassAzimuth.setDeg(CockpitP_61B.this.setOld.radioCompassAzimuth.getDeg(0.1F), f - CockpitP_61B.this.setOld.azimuth.getDeg(0.1F) - 90F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      man1;
        float      man2;
        float      mix1;
        float      mix2;
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      trim;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }
    }

    public CockpitP_61B() {
        super("3DO/Cockpit/P-61B/hier.him", "bf109");
//        radarPlane = new ArrayList();
//        rRange = 4000F;
//        rClose = 50F;
//        nTgts = 5;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "256-10_Gauges6_DMG", "256-10_Gauges6", "256-16_Gauges7_DMG", "256-16_Gauges7", "256-5_Gauges1_DMG", "256-5_Gauges1", "256-6_Gauges2_DMG", "256-6_Gauges2", "256-7_Gauges3_DMG", "256-7_Gauges3", "256-8_Gauges4_DMG", "256-8_Gauges4", "256-9_Gauges5_DMG", "256-9_Gauges5", "256-18", "256-21", "256-22", "128_Gauge_DF", "128_Gauge_DF_DMG" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float paramFloat) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        TypeRadarAPSCarrier apsc = (TypeRadarAPSCarrier)aircraft();
        if(apsc.getRadarAPS().hasMessages())
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 60F);
        else
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 0.0F);
        if(apsc.getRadarAPS().hasPower())
        {
            this.mesh.chunkVisible("RadarScreen", true);
            this.mesh.chunkVisible("RadarScreenOFF", false);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 30F, 0.0F);
            apsc.getRadarAPS().radarScanCockpit(this.mesh, 10, 4);
        } else
        {
            this.mesh.chunkVisible("RadarScreen", false);
            this.mesh.chunkVisible("RadarScreenOFF", true);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 0.0F, 0.0F);
            apsc.getRadarAPS().clearRadarMarks(this.mesh, 10, 4);
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Canopy", 0.0F, this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -120F), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, -70F * this.interp(this.setNew.throttle1, this.setOld.throttle1, paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, -70F * this.interp(this.setNew.throttle2, this.setOld.throttle2, paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, -70F * this.interp(this.setNew.prop1, this.setOld.prop1, paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, -70F * this.interp(this.setNew.prop2, this.setOld.prop2, paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, -47F * this.interp(this.setNew.mix1, this.setOld.mix1, paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 0.0F, -47F * this.interp(this.setNew.mix2, this.setOld.mix2, paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, 73F - (73F * this.fm.EI.engines[0].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger2", 0.0F, 73F - (73F * this.fm.EI.engines[1].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_BombBay1", 0.0F, 40F * this.fm.CT.BayDoorControl, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Column1", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8.8F);
        this.mesh.chunkSetAngles("Z_Column2", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 72.5F, 0.0F);
        if (this.fm.Gears.lgear) {
            this.mesh.chunkSetAngles("Z_GearLInd", 0.0F, 86F * this.fm.CT.getGear(), 0.0F);
        }
        if (this.fm.Gears.rgear) {
            this.mesh.chunkSetAngles("Z_GearRInd", 0.0F, -86F * this.fm.CT.getGear(), 0.0F);
        }
        if (this.fm.Gears.cgear) {
            this.mesh.chunkSetAngles("Z_GearCInd", 0.0F, 86F * this.fm.CT.getGear(), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FlapInd", 0.0F, -67.5F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, paramFloat), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, paramFloat), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 804.6721F, 0.0F, 10F), CockpitP_61B.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -16F, 16F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.cvt(this.setNew.vspeed, -10.159F, 10.159F, -180F, 180F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(paramFloat), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, -this.setNew.azimuth.getDeg(paramFloat), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1344F, 0.0F, 70.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man1, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man1, 0.3386378F, World.Rnd().nextFloat(1.0F, 2.0F), 0.0F, 285.5F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Pres2", 0.0F, this.cvt(this.setNew.man2, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_CarbIn1", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_CarbIn2", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hydro1", 0.0F, this.cvt(this.fm.Gears.isHydroOperable() ? 0.8F : 0.0F, 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyPres1", 0.0F, 228F, 0.0F);
        this.mesh.chunkSetAngles("Z_OxyQ1", 0.0F, 279F, 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.025F, 0.025F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
    }

//    public void radarscan()
//    {
//        try
//        {
//            Aircraft aircraft = World.getPlayerAircraft();
//            if(Time.current() + 50L > to)
//                radarPlane.clear();
//            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
//            {
//                if(Time.current() > to)
//                {
//                    to = Time.current() + 500L;
//                    Point3d point3d = aircraft.pos.getAbsPoint();
//                    Orient orient = aircraft.pos.getAbsOrient();
//                    List list = Engine.targets();
//                    int i1 = list.size();
//                    for(int k1 = 0; k1 < i1; k1++)
//                    {
//                        Actor actor = (Actor)list.get(k1);
//                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
//                        {
//                            Vector3d vector3d = new Vector3d();
//                            vector3d.set(point3d);
//                            Point3d point3d1 = new Point3d();
//                            point3d1.set(actor.pos.getAbsPoint());
//                            point3d1.sub(point3d);
//                            orient.transformInv(point3d1);
//                            if(point3d1.x > (double)rClose && point3d1.x < (double)rRange && point3d1.y < point3d1.x * 0.46630765815D && point3d1.y > -point3d1.x * 0.46630765815D && point3d1.z < point3d1.x * 0.1763269807D && point3d1.z > -point3d1.x * 0.1763269807D)
//                                radarPlane.add(point3d1);
//                        }
//                    }
//
//                }
//                int i = radarPlane.size();
//                if(i == 0)
//                {
//                    for(int i1 = 0; i1 <= 9; i1++)
//                    {
//                        String s1 = "Radarmark10" + i1;
//                        String s2 = "Radarmark20" + i1;
//                        String s3 = "Radarmark30" + i1;
//                        String s4 = "Radarmark40" + i1;
//                        if(this.mesh.isChunkVisible(s1))
//                            this.mesh.chunkVisible(s1, false);
//                        if(this.mesh.isChunkVisible(s2))
//                            this.mesh.chunkVisible(s2, false);
//                        if(this.mesh.isChunkVisible(s3))
//                            this.mesh.chunkVisible(s3, false);
//                        if(this.mesh.isChunkVisible(s4))
//                            this.mesh.chunkVisible(s4, false);
//                    }
//
//                }
//                int j = 0;
//                for(int k = 0; k < i; k++)
//                {
//                    double d = ((Point3d)radarPlane.get(k)).x;
//                    if(d > (double)rClose && j <= nTgts)
//                    {
//                        double d1 = ((Point3d)radarPlane.get(k)).y * 60D / d;
//                        double d2 = ((Point3d)radarPlane.get(k)).z * 60D / d;
//                        float f1 = cvt((float)d1, -10F, 10F, -0.04F, 0.04F);
//                        float f2 = cvt((float)d2, -10F, 10F, -0.04F, 0.04F);
//                        j++;
//                        String s1 = "Radarmark10" + j;
//                        String s2 = "Radarmark20" + j;
//                        String s3 = "Radarmark30" + j;
//                        String s4 = "Radarmark40" + j;
//                        if(this.mesh.isChunkVisible(s1))
//                            this.mesh.chunkVisible(s1, false);
//                        if(this.mesh.isChunkVisible(s2))
//                            this.mesh.chunkVisible(s2, false);
//                        if(this.mesh.isChunkVisible(s3))
//                            this.mesh.chunkVisible(s3, false);
//                        if(this.mesh.isChunkVisible(s4))
//                            this.mesh.chunkVisible(s4, false);
//                        String mark = "";
//                        int range = 0;
//                        if(d <= 300D)
//                            range = 4;
//                        if(d < 450D && d >= 300D)
//                            range = 3;
//                        if(d < 600D && d >= 450D)
//                            range = 2;
//                        if(d >= 600D)
//                            range = 1;
//                        mark = "Radarmark" + range + "0" + j;
//                        this.mesh.setCurChunk(mark);
//                        resetYPRmodifier();
//                        Cockpit.xyz[0] = f1;
//                        Cockpit.xyz[2] = f2;
//                        this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
//                        this.mesh.render();
//                        if(!this.mesh.isChunkVisible(mark))
//                            this.mesh.chunkVisible(mark, true);
//                    }
//                }
//
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Z_Pres2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_FuelPres1", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_DF", false);
            this.mesh.chunkVisible("DF_gauge_D0", false);
            this.mesh.chunkVisible("DF_gauge_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        this.retoggleLight();
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh localHierMesh = this.aircraft().hierMesh();
        Mat localMat = localHierMesh.material(localHierMesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", localMat);
        localMat = localHierMesh.material(localHierMesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", localMat);
        localMat = localHierMesh.material(localHierMesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", localMat);
    }

    protected void reflectPlaneToModel() {
        HierMesh localHierMesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", localHierMesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", localHierMesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", localHierMesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", localHierMesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", localHierMesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", localHierMesh.isChunkVisible("WingRIn_D2"));
    }

//    private long to;
//    private ArrayList radarPlane;
//    private float rRange;
//    private float rClose;
//    private int nTgts;

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 18.5F, 62F, 107F, 152.5F, 198.5F, 238.5F, 252F, 265F, 278.5F, 292F, 305.5F, 319F, 331.5F, 343F };

}
