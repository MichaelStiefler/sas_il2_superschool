package com.maddox.il2.objects.air;

/*
 * @author: SAS~Skylla at www.sas1946.com
 * 
 * @see: com.maddox.il2.object.air.CockpitHurricaneMkII, com.maddox.il2.object.air.HurricaneMkIIbModFilter
 * 
 */

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Aircraft

public class CockpitHurricaneMkIIbModFilter extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = (10F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl) / 11F;
                setNew.altimeter = fm.getAltitude();
                if(Math.abs(((FlightModelMain) (fm)).Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + -((FlightModelMain) (fm)).Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
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
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables()
        {
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected float waypointAzimuth()
    {
        WayPoint waypoint = ((FlightModelMain) (super.fm)).AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, ((FlightModelMain) (super.fm)).Loc);
            return (float)(57.295779513082323D * Math.atan2(-((Tuple3d) (tmpV)).y, ((Tuple3d) (tmpV)).x));
        }
    }

    public CockpitHurricaneMkIIbModFilter()
    {
        super("3DO/Cockpit/HurricaneMkIIbModFilter/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bNeedSetUp = true;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new String[] {
            "BORT2", "BORT4", "COMPASS", "prib_five_fin", "prib_five", "prib_four", "prib_one_fin", "prib_one", "prib_six", "prib_three", 
            "prib_two", "pricel"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.2F);
    }

    public void reflectWorldToInstruments(float f)
    {
    	
    	//TODO: Move Cockpit Canopy
    	this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        this.mesh.chunkSetLocate("FONAR2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("FONAR_GLASS2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("XLampEngHeat", (this.fm.EI.engines[0].tOilOut > 115F) | (this.fm.EI.engines[0].tWaterOut > 115F));
        //---------------------------------
        
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        super.mesh.chunkVisible("XLampEngHeat", (((FlightModelMain) (super.fm)).EI.engines[0].tOilOut > 105.5F) | (((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut > 135.5F));
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        super.mesh.chunkSetLocate("FONAR2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("FONAR_GLASS2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("XGlassDamage2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("XGlassDamage3", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        super.mesh.chunkVisible("XLampGearUpL", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F && ((FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("XLampGearUpR", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F && ((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("XLampGearDownL", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F && ((FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("XLampGearDownR", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F && ((FlightModelMain) (super.fm)).Gears.rgear);
        resetYPRmodifier();
        super.mesh.chunkSetAngles("RUSBase", 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl), 0.0F);
        super.mesh.chunkSetAngles("RUS", -35F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F, 0.0F);
        super.mesh.chunkVisible("RUS_GUN", !((FlightModelMain) (super.fm)).CT.WeaponControl[0]);
        super.mesh.chunkSetAngles("RUS_TORM", 0.0F, 0.0F, -40F * ((FlightModelMain) (super.fm)).CT.getBrake());
        Cockpit.xyz[2] = 0.01625F * ((FlightModelMain) (super.fm)).CT.getAileron();
        super.mesh.chunkSetLocate("RUS_TAND_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -Cockpit.xyz[2];
        super.mesh.chunkSetLocate("RUS_TAND_R", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("RUD", 0.0F, -81.81F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.GearControl == 0.0F && ((FlightModelMain) (super.fm)).CT.getGear() != 0.0F)
        {
            Cockpit.xyz[0] = 0.05F;
            Cockpit.xyz[2] = 0.0F;
        } else
        if(((FlightModelMain) (super.fm)).CT.GearControl == 1.0F && ((FlightModelMain) (super.fm)).CT.getGear() != 1.0F)
        {
            Cockpit.xyz[0] = -0.05F;
            Cockpit.xyz[2] = 0.0F;
        } else
        if(Math.abs(((FlightModelMain) (super.fm)).CT.FlapsControl - ((FlightModelMain) (super.fm)).CT.getFlap()) > 0.02F)
            if(((FlightModelMain) (super.fm)).CT.FlapsControl - ((FlightModelMain) (super.fm)).CT.getFlap() > 0.0F)
            {
                Cockpit.xyz[0] = -0.05F;
                Cockpit.xyz[2] = 0.0345F;
            } else
            {
                Cockpit.xyz[0] = 0.05F;
                Cockpit.xyz[2] = 0.0345F;
            }
        super.mesh.chunkSetLocate("SHAS_RUCH_T", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("PROP_CONTR", (((FlightModelMain) (super.fm)).CT.getStepControl() >= 0.0F ? ((FlightModelMain) (super.fm)).CT.getStepControl() : 1.0F - interp(setNew.throttle, setOld.throttle, f)) * -60F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("PEDALY", 9F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("COMPASS_M", -interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("SHKALA_DIRECTOR", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        super.mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        super.mesh.chunkSetAngles("STREL_ALT_SHRT1", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -108F));
        super.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.5173668F, 2.72369F, -70F, 250F));
        super.mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 307.7F, 0.0F, 60F));
        super.mesh.chunkSetAngles("STRELK_FUEL_LB", 0.0F, -cvt(((FlightModelMain) (super.fm)).M.fuel > 1.0F ? 10F * ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput() : 0.0F, 0.0F, 10F, 0.0F, 10F), 0.0F);
        super.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
        super.mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 40F, 100F, 0.0F, 270F));
        super.mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), radScale));
        resetYPRmodifier();
        Cockpit.xyz[2] = 0.05865F * ((FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator();
        super.mesh.chunkSetLocate("zRadFlap", Cockpit.xyz, Cockpit.ypr);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, -48F, 48F));
        super.mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
        super.mesh.chunkVisible("STRELK_V_SHORT", false);
        if(bFAF)
            super.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeed()), 0.0F, 143.0528F, 0.0F, 32F), speedometerScaleFAF));
        else
            super.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScale));
        super.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
        super.mesh.chunkSetAngles("STRELKA_GOR_FAF", 0.0F, 0.0F, ((FlightModelMain) (super.fm)).Or.getKren());
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
        super.mesh.chunkSetLocate("STRELKA_GOR_RAF", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("STRELKA_GOS_FAF", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("STRELKA_HOUR", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("STRELKA_MINUTE", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("STRELKA_SECUND", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("HullDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
            super.mesh.materialReplace("prib_four", "prib_four_damage");
            super.mesh.materialReplace("prib_four_night", "prib_four_damage_night");
            super.mesh.materialReplace("prib_three", "prib_three_damage");
            super.mesh.materialReplace("prib_three_night", "prib_three_damage_night");
            super.mesh.chunkVisible("STRELK_TEMP_OIL", false);
            super.mesh.chunkVisible("STRELK_TEMP_RAD", false);
            super.mesh.chunkVisible("STRELKA_BOOST", false);
            super.mesh.chunkVisible("STRELKA_FUEL", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
        {
            super.mesh.chunkVisible("HullDamage4", true);
            super.mesh.chunkVisible("HullDamage2_RAF", true);
            super.mesh.chunkVisible("HullDamage2_FAF", true);
            super.mesh.materialReplace("prib_three", "prib_three_damage");
            super.mesh.materialReplace("prib_three_night", "prib_three_damage_night");
            super.mesh.chunkVisible("STRELK_TEMP_OIL", false);
            super.mesh.chunkVisible("STRELK_TEMP_RAD", false);
            super.mesh.chunkVisible("STRELKA_BOOST", false);
            super.mesh.chunkVisible("STRELKA_FUEL", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
        {
            super.mesh.chunkVisible("HullDamage3", true);
            super.mesh.chunkVisible("HullDamage6", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.materialReplace("prib_two", "prib_two_damage");
            super.mesh.materialReplace("prib_two_night", "prib_two_damage_night");
            super.mesh.chunkVisible("STREL_ALT_LONG", false);
            super.mesh.chunkVisible("STREL_ALT_SHORT", false);
            super.mesh.chunkVisible("STRELKA_RPM", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
        {
            super.mesh.chunkVisible("HullDamage5", true);
            super.mesh.chunkVisible("HullDamage6", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("HullDamage1", true);
            if(bFAF)
                super.mesh.chunkVisible("HullDamage2_FAF", true);
            else
                super.mesh.chunkVisible("HullDamage2_RAF", true);
            super.mesh.materialReplace("prib_one", "prib_one_damage");
            super.mesh.materialReplace("prib_one_fin", "prib_one_fin_damage");
            super.mesh.materialReplace("prib_one_night", "prib_one_damage_night");
            super.mesh.materialReplace("prib_one_fin_night", "prib_one_fin_damage_night");
            super.mesh.chunkVisible("STRELK_V_LONG", false);
            super.mesh.chunkVisible("STRELK_V_SHORT", false);
            super.mesh.chunkVisible("STRELKA_GOR_FAF", false);
            super.mesh.chunkVisible("STRELKA_GOR_RAF", false);
            super.mesh.chunkVisible("STRELKA_VY", false);
            super.mesh.chunkVisible("STREL_TURN_DOWN", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
        }
        retoggleLight();
    }

    protected void reflectPlaneMats()
    {
        if(aircraft().getRegiment().country().equals("fi"))
        {
            bFAF = true;
            super.mesh.chunkVisible("PRIBORY_RAF", false);
            super.mesh.chunkVisible("PRIBORY_FAF", true);
            super.mesh.chunkVisible("STRELKA_GOR_RAF", false);
            super.mesh.chunkVisible("STRELKA_GOR_FAF", true);
            super.mesh.chunkVisible("STRELKA_GOS_FAF", true);
        } else
        {
            bFAF = false;
            super.mesh.chunkVisible("PRIBORY_RAF", true);
            super.mesh.chunkVisible("PRIBORY_FAF", false);
            super.mesh.chunkVisible("STRELKA_GOR_RAF", true);
            super.mesh.chunkVisible("STRELKA_GOR_FAF", false);
            super.mesh.chunkVisible("STRELKA_GOS_FAF", false);
        }
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(super.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private boolean bFAF;
    private float pictAiler;
    private float pictElev;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 
        112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 
        297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 
        476F, 492.5F, 513F, 534.5F, 552F, 569.5F
    };
    private static final float speedometerScaleFAF[] = {
        0.0F, 0.0F, 2.0F, 6F, 21F, 40F, 56F, 72.5F, 89.5F, 114F, 
        135.5F, 157F, 182.5F, 205F, 227.5F, 246.5F, 265.5F, 286F, 306F, 326F, 
        345.5F, 363F, 385F, 401F, 414.5F, 436.5F, 457F, 479F, 496.5F, 515F, 
        539F, 559.5F, 576.5F
    };
    private static final float radScale[] = {
        0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 
        125F, 161F, 202.5F, 253F, 315.5F
    };
    private static final float rpmScale[] = {
        0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 
        311.5F
    };
    private static final float variometerScale[] = {
        -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F
    };
    private Point3d tmpP;
    private Vector3d tmpV;
}