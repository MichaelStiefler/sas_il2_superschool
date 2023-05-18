// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CockpitMIG_21PF.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, MIG_21, Aircraft

public class CockpitMIG_21PF extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.9F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.1F;
                setNew.starter = 0.94F * setOld.starter + 0.06F * (((FlightModelMain) (fm)).EI.engines[0].getStage() > 0 && ((FlightModelMain) (fm)).EI.engines[0].getStage() < 6 ? 1.0F : 0.0F);
                setNew.altimeter = fm.getAltitude();
                float a = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(a - 90F);
                    setOld.waypointAzimuth.setDeg(a - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - ((FlightModelMain) (fm)).Or.azimut());
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), a - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                float f = ((MIG_21)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitMIG_21PF.k14TargetWingspanScale[((MIG_21)aircraft()).k14WingspanType]) / f;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitMIG_21PF.k14TargetMarkScale[((MIG_21)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((MIG_21)aircraft()).k14Mode;
                Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double d = 0.00125D * (double)f;
                float f_0_ = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
                float f_1_ = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
                float f_2_ = floatindex((f - 200F) * 0.04F, CockpitMIG_21PF.k14BulletDrop) - CockpitMIG_21PF.k14BulletDrop[0];
                f_1_ += (float)Math.toDegrees(Math.atan(f_2_ / f));
                setNew.k14x = 0.92F * setOld.k14x + 0.08F * f_0_;
                setNew.k14y = 0.92F * setOld.k14y + 0.08F * f_1_;
                if(setNew.k14x > 7F)
                    setNew.k14x = 7F;
                if(setNew.k14x < -7F)
                    setNew.k14x = -7F;
                if(setNew.k14y > 7F)
                    setNew.k14y = 7F;
                if(setNew.k14y < -7F)
                    setNew.k14y = -7F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle;
        float vspeed;
        float starter;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;

        private Variables()
        {
            throttle = 0.0F;
            starter = 0.0F;
            altimeter = 0.0F;
            vspeed = 0.0F;
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private float machNumber()
    {
        return ((MIG_21)super.aircraft()).calculateMach();
    }

    public CockpitMIG_21PF()
    {
        super("3DO/Cockpit/MiG-21PF/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(super.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(300F, 0.0F, 0.0F);
        light1.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(super.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(300F, 0.0F, 0.0F);
        light2.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        hooknamed = new HookNamed(super.mesh, "LAMPHOOK3");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        light3.light.setColor(300F, 0.0F, 0.0F);
        light3.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK3", light3);
        super.cockpitNightMats = (new String[] {
            "gauges1", "gauges2", "gauges3", "gauges4", "gauges5", "instrument"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
        FOV = 1.0D;
        ScX = 0.0019999999776482581D;
        ScY = 3.8999999999999999E-006D;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 20000F;
        RClose = 5F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        radarPlanefriendly = new ArrayList();
        radarTracking = new ArrayList();
        trackzone = false;
    }

    public void reflectWorldToInstruments(float f)
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {
            int i = ((MIG_21)aircraft()).k14Mode;
            boolean bool = i < 1;
            super.mesh.chunkVisible("Z_Z_RETICLE", bool);
        }
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Canopy", 0.0F, -50F * ((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.0F);
        super.mesh.chunkSetAngles("stick", 0.0F, (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 10F);
        super.mesh.chunkSetAngles("leftrudder", 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("rightrudder", 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("throttle", 0.0F, -40.909F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Acceleration", cvt(super.fm.getOverload(), -4.5F, 10F, -110F, 220F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 340F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -20F, 20F, -72F, 72F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("z_Enginespeed", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 20F), rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_ExTemp", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 175F, 0.0F, 265F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_fuel1", cvt(((FlightModelMain) (super.fm)).M.fuel / 2.0F, 0.0F, 2214F, 0.0F, 234F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Gear1", 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * ((FlightModelMain) (super.fm)).CT.GearControl), 0.0F);
        super.mesh.chunkSetAngles("Z_Horizontal2", ((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Horizontal1", 1.2F * ((FlightModelMain) (super.fm)).Or.getTangage(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(super.fm.getSpeedKMH(), 200F, 2500F, 0.0F, 24F), speedometerScale), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_Turn1", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Mach", cvt(machNumber(), 0.5F, 3F, 0.0F, 349F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass3", 90F + setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", 90F + setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f) + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        resetYPRmodifier();
        if(((MIG_21)aircraft()).k14Mode >= 1)
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
        else
            super.mesh.chunkVisible("Z_Z_RETICLE", true);
        radarclutter();
    }

    public void radarclutter()
    {
        long t = Time.current() + World.Rnd().nextLong(-250L, 250L);
        if(super.fm.getAltitude() > 500F)
            if(((FlightModelMain) (super.fm)).Or.getTangage() < -20F + 8000F / super.fm.getAltitude() && super.fm.getAltitude() < 5000F)
            {
                clutter = true;
                if(t > t1 + 400L && t < t1 + 800L)
                {
                    super.mesh.chunkVisible("Radarclutter1", true);
                    super.mesh.chunkVisible("Radarclutter2", false);
                    super.mesh.chunkVisible("Radarclutter3", false);
                } else
                if(t > t1 + 800L && t < t1 + 1200L)
                {
                    super.mesh.chunkVisible("Radarclutter1", false);
                    super.mesh.chunkVisible("Radarclutter2", true);
                    super.mesh.chunkVisible("Radarclutter3", false);
                } else
                if(t > t1 + 1200L)
                {
                    t1 = t;
                    super.mesh.chunkVisible("Radarclutter1", false);
                    super.mesh.chunkVisible("Radarclutter2", false);
                    super.mesh.chunkVisible("Radarclutter3", true);
                }
                for(int j = 0; j <= nTgts; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    String p = "RadarIFF0" + j;
                    if(super.mesh.isChunkVisible(p))
                        super.mesh.chunkVisible(p, false);
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

                String m = "Radarlock";
                String n = "RadarLL";
                String o = "RadarLR";
                String s1 = "RadarLA";
                String s2 = "RadarLB";
                if(super.mesh.isChunkVisible(m))
                    super.mesh.chunkVisible(m, false);
                if(super.mesh.isChunkVisible(n))
                    super.mesh.chunkVisible(n, false);
                if(super.mesh.isChunkVisible(o))
                    super.mesh.chunkVisible(o, false);
                if(super.mesh.isChunkVisible(s1))
                    super.mesh.chunkVisible(s1, false);
                if(super.mesh.isChunkVisible(s2))
                    super.mesh.chunkVisible(s2, false);
            } else
            {
                if(((MIG_21)aircraft()).radarmode == 0)
                {
                    clutter = false;
                    tracking = true;
                    azimult = 0.0D;
                    tangage = 0.0D;
                    radarscan();
                    String m = "Radarlock";
                    String n = "RadarLL";
                    String o = "RadarLR";
                    String s1 = "RadarLA";
                    String s2 = "RadarLB";
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                    if(super.mesh.isChunkVisible(s1))
                        super.mesh.chunkVisible(s1, false);
                    if(super.mesh.isChunkVisible(s2))
                        super.mesh.chunkVisible(s2, false);
                    super.mesh.chunkVisible("Radarlockrange", true);
                    resetYPRmodifier();
                    Cockpit.xyz[0] = ((MIG_21)aircraft()).lockrange;
                    super.mesh.chunkSetLocate("Radarlockrange", Cockpit.xyz, Cockpit.ypr);
                }
                if(((MIG_21)aircraft()).radarmode == 1 && !clutter)
                {
                    for(int j = 0; j <= nTgts + 1; j++)
                    {
                        String m = "Radarmark0" + j;
                        String n = "RadarA0" + j;
                        String o = "RadarB0" + j;
                        String p = "RadarIFF0" + j;
                        if(super.mesh.isChunkVisible(p))
                            super.mesh.chunkVisible(p, false);
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                        if(super.mesh.isChunkVisible(n))
                            super.mesh.chunkVisible(n, false);
                        if(super.mesh.isChunkVisible(o))
                            super.mesh.chunkVisible(o, false);
                    }

                    super.mesh.chunkVisible("Radarlockrange", false);
                    radartracking();
                }
                super.mesh.chunkVisible("Radarclutter1", false);
                super.mesh.chunkVisible("Radarclutter2", false);
                super.mesh.chunkVisible("Radarclutter3", false);
            }
    }

    public void radarscan()
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if(ti > to + 1450L)
                radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                if(ti > to + 2000L)
                {
                    to = ti;
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                    List list = Engine.targets();
                    int i = list.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                        {
                            Vector3d vector3d = new Vector3d();
                            vector3d.set(pointAC);
                            Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.46630765815000003D && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.46630765815000003D && ((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.1763269807D && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.1763269807D)
                                radarPlane.add(pointOrtho);
                        }
                    }

                }
                int i = radarPlane.size();
                int nt = 0;
                for(int j = 0; j < i; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                    if(x > (double)RClose && nt <= nTgts)
                    {
                        FOV = 60D / x;
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV;
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                        float f = FOrigX + (float)(NewX * ScX);
                        if(f > 0.07F)
                            f = 0.07F;
                        if(f < -0.07F)
                            f = -0.07F;
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 > 0.07F)
                            f1 = 0.07F;
                        if(f1 < -0.07F)
                            f1 = -0.07F;
                        nt++;
                        String m = "Radarmark0" + nt;
                        super.mesh.setCurChunk(m);
                        super.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[0] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        String n = "RadarA0" + nt;
                        String o = "RadarB0" + nt;
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z > ((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.017268677900000001D)
                        {
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                            if(super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, false);
                        }
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z < -((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.017268677900000001D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, false);
                        }
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z >= -((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.017268677900000001D && ((Tuple3d) ((Point3d)radarPlane.get(j))).z <= ((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.017268677900000001D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                        }
                    }
                }

                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

            }
            if(radarPlane.size() == 0 && super.mesh.isChunkVisible("Radarmark00"))
                super.mesh.chunkVisible("Radarmark00", false);
            if(super.mesh.isChunkVisible("RadarA00"))
                super.mesh.chunkVisible("RadarA00", false);
            if(super.mesh.isChunkVisible("RadarB00"))
                super.mesh.chunkVisible("RadarB00", false);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarscanIFF()
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if(ti > to + 1450L)
                radarPlanefriendly.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                if(ti > to + 2000L)
                {
                    to = ti;
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                    List list = Engine.targets();
                    int i = list.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                        {
                            Vector3d vector3d = new Vector3d();
                            vector3d.set(pointAC);
                            Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.46630765815000003D && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.46630765815000003D && ((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.1763269807D && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.1763269807D)
                                radarPlanefriendly.add(pointOrtho);
                        }
                    }

                }
                int i = radarPlanefriendly.size();
                int nt = 0;
                for(int j = 0; j < i; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlanefriendly.get(j))).x;
                    if(x > (double)RClose && nt <= nTgts)
                    {
                        FOV = 60D / x;
                        double NewX = -((Tuple3d) ((Point3d)radarPlanefriendly.get(j))).y * FOV;
                        double NewY = ((Tuple3d) ((Point3d)radarPlanefriendly.get(j))).x;
                        float f = FOrigX + (float)(NewX * ScX);
                        if(f > 0.07F)
                            f = 0.07F;
                        if(f < -0.07F)
                            f = -0.07F;
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 > 0.07F)
                            f1 = 0.07F;
                        if(f1 < -0.07F)
                            f1 = -0.07F;
                        nt++;
                        String m = "RadarIFF0" + nt;
                        super.mesh.setCurChunk(m);
                        super.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[0] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                    }
                }

                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "RadarIFF0" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                }

            }
            if(radarPlanefriendly.size() == 0 && super.mesh.isChunkVisible("RadarIFF00"))
                super.mesh.chunkVisible("RadarIFF00", false);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radartracking()
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-15L, 15L);
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft) && trefresh + 30L < ti)
            {
                trefresh = ti;
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                radarTracking.clear();
                List list = Engine.targets();
                int j1 = list.size();
                for(int k1 = 0; k1 < j1; k1++)
                {
                    Actor actor = (Actor)list.get(k1);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy() && tracking)
                    {
                        double D1 = 0.0D;
                        if(azimult == 0.0D && tangage == 0.0D)
                            D1 = 0.16397023425999999D;
                        else
                            D1 = 0.027488663520000001D;
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                        double d2 = 222222.22222222222D;
                        if(!trackzone)
                        {
                            if(((Tuple3d) (point3d1)).x < (double)(((MIG_21)aircraft()).lockrange + 0.01F) * d2 - (double)(200F * f) && ((Tuple3d) (point3d1)).x > (double)(((MIG_21)aircraft()).lockrange - 0.01F) * d2 - (double)(200F * f) && ((Tuple3d) (point3d1)).y < tangage + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).y > tangage - ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z < azimult + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z > azimult - ((Tuple3d) (point3d1)).x * D1)
                            {
                                radarTracking.add(point3d1);
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Target acquired");
                                trackzone = true;
                            }
                        } else
                        if(((Tuple3d) (point3d1)).x < (double)(((MIG_21)aircraft()).lockrange + 0.01F) * d2 - (double)(200F * f) && ((Tuple3d) (point3d1)).y < tangage + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).y > tangage - ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z < azimult + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z > azimult - ((Tuple3d) (point3d1)).x * D1)
                            radarTracking.add(point3d1);
                    }
                }

                int i = radarTracking.size();
                int j = 0;
                for(int k = 0; k < i && k < 1; k++)
                {
                    double x = ((Tuple3d) ((Point3d)radarTracking.get(k))).x;
                    tangage = ((Tuple3d) ((Point3d)radarTracking.get(k))).y;
                    azimult = ((Tuple3d) ((Point3d)radarTracking.get(k))).z;
                    if(x < 18000D && j <= nTgts)
                    {
                        FOV = 1680D / x;
                        double d3 = -((Tuple3d) ((Point3d)radarTracking.get(k))).y * FOV;
                        double d4 = ((Tuple3d) ((Point3d)radarTracking.get(k))).z * FOV;
                        double d5 = ((Tuple3d) ((Point3d)radarTracking.get(k))).x;
                        float f = (float)(d3 * 0.00059999999776482577D);
                        if(f > 0.075F)
                            f = 0.07F;
                        if(f < -0.07F)
                            f = -0.07F;
                        float f1 = (float)(d4 * 0.00039999999776482584D);
                        if(f1 > 0.07F)
                            f1 = 0.07F;
                        if(f1 < -0.07F)
                            f1 = -0.07F;
                        float f2 = (float)(d5 * 2.9000000000000002E-006D);
                        if(f2 > 0.07F)
                            f2 = 0.07F;
                        if(f2 < -0.07F)
                            f2 = -0.07F;
                        float fx = (float)(d3 * 0.00049999999776482583D);
                        float fy = (float)(d4 * 0.0002999999977648258D);
                        if((double)fx > 0.070000000000000007D || (double)fx < -0.070000000000000007D || (double)fy > 0.035000000000000003D || (double)fy < -0.035000000000000003D)
                        {
                            tracking = false;
                            trackzone = false;
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target lost");
                        }
                        j++;
                        String s1 = "Radarlock";
                        super.mesh.setCurChunk(s1);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[0] = f1;
                        super.mesh.chunkSetLocate(s1, Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        String s2 = "RadarLL";
                        String s3 = "RadarLR";
                        resetYPRmodifier();
                        Cockpit.xyz[0] = f2;
                        super.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                        resetYPRmodifier();
                        Cockpit.xyz[0] = -f2;
                        super.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
                        if(!super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, true);
                        if(!super.mesh.isChunkVisible(s2))
                            super.mesh.chunkVisible(s2, true);
                        if(!super.mesh.isChunkVisible(s3))
                            super.mesh.chunkVisible(s3, true);
                        String n = "RadarLA";
                        String o = "RadarLB";
                        if((double)f1 > 0.0050000000000000001D)
                        {
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                            if(super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, false);
                        }
                        if((double)f1 < -0.0050000000000000001D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, false);
                        }
                        if((double)f1 < 0.0050000000000000001D && (double)f1 > -0.0050000000000000001D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                        }
                    } else
                    {
                        String s1 = "Radarlock";
                        String s2 = "RadarLL";
                        String s3 = "RadarLR";
                        String n = "RadarLA";
                        String o = "RadarLB";
                        if(super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, false);
                        if(super.mesh.isChunkVisible(s2))
                            super.mesh.chunkVisible(s2, false);
                        if(super.mesh.isChunkVisible(s3))
                            super.mesh.chunkVisible(s3, false);
                        if(super.mesh.isChunkVisible(n))
                            super.mesh.chunkVisible(n, false);
                        if(super.mesh.isChunkVisible(o))
                            super.mesh.chunkVisible(o, false);
                    }
                }

                if(i == 0)
                {
                    String s1 = "Radarlock";
                    String s2 = "RadarLL";
                    String s3 = "RadarLR";
                    String n = "RadarLA";
                    String o = "RadarLB";
                    if(super.mesh.isChunkVisible(s1))
                        super.mesh.chunkVisible(s1, false);
                    if(super.mesh.isChunkVisible(s2))
                        super.mesh.chunkVisible(s2, false);
                    if(super.mesh.isChunkVisible(s3))
                        super.mesh.chunkVisible(s3, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                    tracking = false;
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void reflectCockpitState()
    {
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            light1.light.setEmit(0.006F, 0.4F);
            light2.light.setEmit(0.006F, 0.4F);
            light3.light.setEmit(0.006F, 0.4F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            light3.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    public void toggleDim()
    {
        super.cockpitDimControl = !super.cockpitDimControl;
        if(super.cockpitDimControl)
        {
            super.mesh.chunkVisible("Z_Z_MASKl", true);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ZSh-3 Helmet: Visor Down");
        } else
        {
            super.mesh.chunkVisible("Z_Z_MASKl", false);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ZSh-3 Helmet: Visor Up");
        }
    }

    private long t1;
    private boolean clutter;
    private boolean tracking;
    private long trefresh;
    private double azimult;
    private double tangage;
    private boolean trackzone;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    public Vector3f w;
    private long to;
    double FOV;
    double ScX;
    double ScY;
    double ScZ;
    float FOrigX;
    float FOrigY;
    int nTgts;
    float RRange;
    float RClose;
    float BRange;
    int BRefresh;
    int BSteps;
    float BDiv;
    long tBOld;
    private ArrayList radarPlane;
    private ArrayList radarPlanefriendly;
    private ArrayList radarTracking;
    protected float offset;
    private static final float speedometerScale[] = {
        19F, 55F, 90F, 105F, 118.8F, 131F, 144.2F, 157.8F, 171.4F, 185.2F, 
        198.5F, 212.1F, 226.3F, 239.8F, 252.1F, 265.7F, 277F, 291.1F, 302.2F, 314.4F, 
        324F, 335.8F, 346.8F, 359.5F
    };
    private static final float rpmScale[] = {
        -5F, 29F, 58F, 87F, 116F, 155F, 188F, 196.71F, 205.42F, 214.13F, 
        222.84F, 231.55F, 240.26F, 248.97F, 257.68F, 266.39F, 275.1F, 283.81F, 292.52F, 301.23F, 
        310F
    };
    private static final float k14TargetMarkScale[] = {
        -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };










}
