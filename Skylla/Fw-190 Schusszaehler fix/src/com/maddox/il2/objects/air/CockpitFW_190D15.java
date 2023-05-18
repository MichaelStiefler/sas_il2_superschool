// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.air.CockpitPilot.Interpolater;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Message;
import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Time;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.ai.WayPoint;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.objects.weapons.Gun;

/*
 * FIXME: 
 * compiler issues:
 * - Revi sun cover keeps coming up and down without order => error NOT located and NOT solved!
 * - variometer does not work anymore. See line 250 => error IS located but NOT solved!
 * @see: CockpitFW-190D11
 */

public class CockpitFW_190D15 extends CockpitPilot
{
    boolean bNeedSetUp;
    private float tmp;
    private Gun[] gun;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private LightPointActor light2;
    private BulletEmitter[] bomb;
    private long t1;
    private float pictAiler;
    private float pictElev;
    private static final float[] speedometerScale;
    private static final float[] rpmScale;
    private static final float[] fuelScale;
    private static final float[] manPrsScale;
    private static final float[] oilfuelNeedleScale;
    private static final float[] vsiNeedleScale;
    private static final float[] oilTempScale;
    private Point3d tmpP;
    private Vector3d tmpV;
    
    static {
        speedometerScale = new float[] { 0.0f, 18.5f, 67.0f, 117.0f, 164.0f, 215.0f, 267.0f, 320.0f, 379.0f, 427.0f, 428.0f };
        rpmScale = new float[] { 0.0f, 11.25f, 53.0f, 108.0f, 170.0f, 229.0f, 282.0f, 334.0f, 342.5f, 342.5f };
        fuelScale = new float[] { 0.0f, 16.0f, 35.0f, 52.5f, 72.0f, 72.0f };
        manPrsScale = new float[] { 0.0f, 0.0f, 0.0f, 15.5f, 71.0f, 125.0f, 180.0f, 235.0f, 290.0f, 245.0f, 247.0f, 247.0f };
        oilfuelNeedleScale = new float[] { 0.0f, 38.0f, 84.0f, 135.5f, 135.0f };
        vsiNeedleScale = new float[] { 0.0f, 48.0f, 82.0f, 96.5f, 111.0f, 120.5f, 130.0f, 130.0f };
        oilTempScale = new float[] { 0.0f, 23.0f, 52.0f, 81.0f, 81.0f };
    }
    
    protected float waypointAzimuth() {
        final WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0f;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        return (float)(57.29577951308232 * Math.atan2(this.tmpV.y, this.tmpV.x));
    }
    
    public CockpitFW_190D15() {
        super("3DO/Cockpit/FW-190D-15/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.gun = new Gun[12];
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.bomb = new BulletEmitter[4];
        this.pictAiler = 0.0f;
        this.pictElev = 0.0f;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.setNew.dimPosition = 1.0f;
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
        Loc loc = new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
        hooknamed.computePos(this, new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126.0f, 232.0f, 245.0f);
        this.light1.light.setEmit(0.0f, 0.0f);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_R");
        loc = new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
        hooknamed.computePos(this, new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126.0f, 232.0f, 245.0f);
        this.light2.light.setEmit(0.0f, 0.0f);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = new String[] { "D9GP1", "A8GP2", "D9GP3", "A8GP4", "A8GP5", "A4GP6", "A5GP3Km", "DA8GP1", "DA8GP2", "DA8GP3", "DA8GP4", "D9GP5", "D9Trans2" };
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }
    
    public void reflectWorldToInstruments(final float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1f, 0.99f, 0.0f, 0.35f);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("CanopyHandle", this.cvt(this.fm.CT.getCockpitDoor(), 0.1f, 0.99f, 0.0f, 3600.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("Handle", -this.cvt(this.fm.CT.getCockpitDoor(), 0.1f, 0.99f, 0.0f, 3600.0f), 0.0f, 0.0f);
        this.resetYPRmodifier();
        switch (this.aircraft().chunkDamageVisible("WingLIn")) {
            case 0: {
                this.mesh.chunkVisible("WingLIn_D0", true);
                this.mesh.chunkVisible("WingLIn_D1", false);
                this.mesh.chunkVisible("WingLIn_D2", false);
                this.mesh.chunkVisible("WingLIn_D3", false);
                break;
            }
            case 1: {
                this.mesh.chunkVisible("WingLIn_D0", false);
                this.mesh.chunkVisible("WingLIn_D1", true);
                this.mesh.chunkVisible("WingLIn_D2", false);
                this.mesh.chunkVisible("WingLIn_D3", false);
                break;
            }
            case 2: {
                this.mesh.chunkVisible("WingLIn_D0", false);
                this.mesh.chunkVisible("WingLIn_D1", false);
                this.mesh.chunkVisible("WingLIn_D2", true);
                this.mesh.chunkVisible("WingLIn_D3", false);
                break;
            }
            case 3: {
                this.mesh.chunkVisible("WingLIn_D0", false);
                this.mesh.chunkVisible("WingLIn_D1", false);
                this.mesh.chunkVisible("WingLIn_D2", false);
                this.mesh.chunkVisible("WingLIn_D3", true);
                break;
            }
            default: {
                this.mesh.chunkVisible("WingLIn_D0", false);
                this.mesh.chunkVisible("WingLIn_D1", false);
                this.mesh.chunkVisible("WingLIn_D2", false);
                this.mesh.chunkVisible("WingLIn_D3", false);
                break;
            }
        }
        switch (this.aircraft().chunkDamageVisible("WingRIn")) {
            case 0: {
                this.mesh.chunkVisible("WingRIn_D0", true);
                this.mesh.chunkVisible("WingRIn_D1", false);
                this.mesh.chunkVisible("WingRIn_D2", false);
                this.mesh.chunkVisible("WingRIn_D3", false);
                break;
            }
            case 1: {
                this.mesh.chunkVisible("WingRIn_D0", false);
                this.mesh.chunkVisible("WingRIn_D1", true);
                this.mesh.chunkVisible("WingRIn_D2", false);
                this.mesh.chunkVisible("WingRIn_D3", false);
                break;
            }
            case 2: {
                this.mesh.chunkVisible("WingRIn_D0", false);
                this.mesh.chunkVisible("WingRIn_D1", false);
                this.mesh.chunkVisible("WingRIn_D2", true);
                this.mesh.chunkVisible("WingRIn_D3", false);
                break;
            }
            case 3: {
                this.mesh.chunkVisible("WingRIn_D0", false);
                this.mesh.chunkVisible("WingRIn_D1", false);
                this.mesh.chunkVisible("WingRIn_D2", false);
                this.mesh.chunkVisible("WingRIn_D3", true);
                break;
            }
            default: {
                this.mesh.chunkVisible("WingRIn_D0", false);
                this.mesh.chunkVisible("WingRIn_D1", false);
                this.mesh.chunkVisible("WingRIn_D2", false);
                this.mesh.chunkVisible("WingRIn_D3", false);
                break;
            }
        }
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[1] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON02");
            this.gun[2] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[3] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON04");
            this.gun[4] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON05");
            this.gun[5] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON06");
            this.gun[6] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON07");
            this.gun[7] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON08");
            this.gun[8] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON11");
            this.gun[9] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON12");
            this.gun[10] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON13");
            this.gun[11] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON14");
        }
        if (this.bomb[0] == null) {
            for (int i = 0; i < this.bomb.length; ++i) {
                this.bomb[i] = GunEmpty.get();
            }
            if (((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01") != GunEmpty.get()) {
                this.bomb[1] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
                this.bomb[2] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            }
            if (((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalRock01") != GunEmpty.get()) {
                this.bomb[0] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalRock01");
                this.bomb[3] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalRock02");
            }
            else if (((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03") != GunEmpty.get()) {
                this.bomb[0] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
                this.bomb[3] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
            }
            else if (((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev05") != GunEmpty.get()) {
                this.bomb[0] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev05");
                this.bomb[3] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev06");
            }
            this.t1 = Time.current();
        }
        this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0f, 10000.0f, 0.0f, 3600.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleALTKm", 0.0f, 0.0f, this.cvt(this.setNew.altimeter, 0.0f, 10000.0f, 0.0f, -180.0f));
        this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6f, 1.8f, 0.0f, 336.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0f, 900.0f, 0.0f, 9.0f), CockpitFW_190D15.speedometerScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0f, 4000.0f, 0.0f, 8.0f), CockpitFW_190D15.rpmScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.72f, 0.0f, 400.0f, 0.0f, 4.0f), CockpitFW_190D15.fuelScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleWaterTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0f, 120.0f, 0.0f, 60.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleOilTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0f, 120.0f, 0.0f, 60.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt((this.fm.M.fuel > 1.0f) ? 0.26f : 0.0f, 0.0f, 3.0f, 0.0f, 135.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0f + 0.05f * this.fm.EI.engines[0].tOilOut, 0.0f, 15.0f, 0.0f, 135.0f), 0.0f, 0.0f);
        float f_0_;
        if (this.aircraft().isFMTrackMirror()) {
            f_0_ = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        }
        else {
        	// FIXME: compiler issue:
        	// original code:
            // f_0_ = this.cvt((this.setNew.azimuth - this.setOld.azimuth) / Time.tickLenFs(), -6.0f, 6.0f, 20.0f, -20.0f);
        	f_0_ = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -6.0f, 6.0f, 20.0f, -20.0f);
        	// solved? - NO!
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f_0_);
            }
        }
        this.mesh.chunkSetAngles("NeedleAHTurn", 0.0f, f_0_, 0.0f);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0x0) {
            this.mesh.chunkSetAngles("NeedleAHBank", 0.0f, this.cvt(this.getBall(7.0), -7.0f, 7.0f, -11.0f, 11.0f), 0.0f);
        }
        this.mesh.chunkSetAngles("NeedleAHCyl", 0.0f, 0.0f, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("NeedleAHBar", 0.0f, 0.0f, this.cvt(this.fm.Or.getTangage(), -45.0f, 45.0f, 12.0f, -12.0f));
        this.mesh.chunkSetAngles("NeedleCD", (this.setNew.vspeed >= 0.0f) ? (-this.floatindex(this.cvt(this.setNew.vspeed, 0.0f, 30.0f, 0.0f, 6.0f), CockpitFW_190D15.vsiNeedleScale)) : this.floatindex(this.cvt(-this.setNew.vspeed, 0.0f, 30.0f, 0.0f, 6.0f), CockpitFW_190D15.vsiNeedleScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.azimuth.getDeg(f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.waypointAzimuth.getDeg(f * 0.1f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleHBSmall", -105.0f + (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5.0f, 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleHBLarge", -270.0f + (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60.0f, 0.0f, 0.0f);
        if ((this.fm.AS.astateCockpitState & 0x2) == 0x0 && (this.fm.AS.astateCockpitState & 0x1) == 0x0 && (this.fm.AS.astateCockpitState & 0x4) == 0x0 && (this.fm.AS.astateCockpitState & 0x10) == 0x0) {
            this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25.0f, 0.0f, 0.0f);
        }
        if ((this.fm.AS.astateCockpitState & 0x8) == 0x0 && (this.fm.AS.astateCockpitState & 0x20) == 0x0) {
            this.mesh.chunkSetAngles("NeedleHClock", -this.cvt(World.getTimeofDay(), 0.0f, 24.0f, 0.0f, 720.0f), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleMClock", -this.cvt(World.getTimeofDay() % 1.0f, 0.0f, 1.0f, 0.0f, 360.0f), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleSClock", -this.cvt(World.getTimeofDay() % 1.0f * 60.0f % 1.0f, 0.0f, 1.0f, 0.0f, 360.0f), 0.0f, 0.0f);
        }
        this.resetYPRmodifier();
        if (this.gun[0] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0f, 250.0f, -0.044f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[1] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[1].countBullets(), 0.0f, 250.0f, -0.044f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[2] != null && this.gun[2].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[2].countBullets(), 0.0f, 125.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[3] != null && this.gun[3].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[3].countBullets(), 0.0f, 125.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[4] != null && this.gun[4].countBullets() > 0.0f && this.gun[5] != null && this.gun[5].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[4].countBullets() + this.gun[5].countBullets(), 0.0f, 250.0f, -0.018f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[6] != null && this.gun[6].countBullets() > 0.0f && this.gun[7] != null && this.gun[7].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[6].countBullets() + this.gun[7].countBullets(), 0.0f, 250.0f, -0.018f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[8] != null && this.gun[8].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[8].countBullets(), 0.0f, 35.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[9] != null && this.gun[9].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[9].countBullets(), 0.0f, 35.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[10] != null && this.gun[10].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[10].countBullets(), 0.0f, 55.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[11] != null && this.gun[11].countBullets() > 0.0f) {
            Cockpit.xyz[0] = this.cvt(this.gun[11].countBullets(), 0.0f, 55.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.t1 < Time.current()) {
            this.t1 = Time.current() + 500L;
            this.mesh.chunkVisible("XLampBombCL", this.bomb[1].haveBullets());
            this.mesh.chunkVisible("XLampBombCR", this.bomb[2].haveBullets());
        }
        this.mesh.chunkSetAngles("IgnitionSwitch", 24.0f * this.fm.EI.engines[0].getControlMagnetos(), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("Revi16Tinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0f, 1.0f, 0.0f, -45.0f), 0.0f, 0.0f);
        final HierMesh mesh = this.mesh;
        final String s = "Stick";
        final float n = 0.0f;
        final float pictAiler = 0.85f * this.pictAiler + 0.15f * this.fm.CT.AileronControl;
        this.pictAiler = pictAiler;
        mesh.chunkSetAngles(s, n, pictAiler * 20.0f, (this.pictElev = 0.85f * this.pictElev + 0.15f * this.fm.CT.ElevatorControl) * 20.0f);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = (this.fm.CT.WeaponControl[1] ? -0.004f : 0.0f);
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f) * 34.0f * 0.91f;
        Cockpit.xyz[2] = ((Cockpit.ypr[0] > 7.0f) ? -0.006f : 0.0f);
        this.mesh.chunkSetLocate("ThrottleQuad", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RPedalBase", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("RPedalStrut", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("RPedal", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f - this.fm.CT.getBrake() * 15.0f);
        this.mesh.chunkSetAngles("LPedalBase", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("LPedalStrut", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("LPedal", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f - this.fm.CT.getBrake() * 15.0f);
        this.mesh.chunkVisible("XLampTankSwitch", this.fm.M.fuel > 144.0f);
        this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 43.2f);
        this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05f);
        this.mesh.chunkVisible("XLampGearL_2", this.fm.CT.getGear() > 0.95f && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05f);
        this.mesh.chunkVisible("XLampGearR_2", this.fm.CT.getGear() > 0.95f && this.fm.Gears.rgear);
    }
    
    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }
    
    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0012f, 0.75f);
            this.light2.light.setEmit(0.0012f, 0.75f);
            this.setNightMats(true);
        }
        else {
            this.light1.light.setEmit(0.0f, 0.0f);
            this.light2.light.setEmit(0.0f, 0.0f);
            this.setNightMats(false);
        }
    }
    
    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        }
        else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }
    
    private class Variables
    {
        float altimeter;
        float throttle;
        float dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float turn;
        float beaconDirection;
        float beaconRange;
        float vspeed;
        
        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }
    
    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x2) != 0x0 || (this.fm.AS.astateCockpitState & 0x1) != 0x0 || (this.fm.AS.astateCockpitState & 0x4) != 0x0 || (this.fm.AS.astateCockpitState & 0x10) != 0x0) {
            this.mesh.materialReplace("D9GP1", "DA8GP1");
            this.mesh.materialReplace("D9GP1_night", "DA8GP1_night");
            this.mesh.materialReplace("A8GP4", "DA8GP4");
            this.mesh.chunkVisible("NeedleManPress", false);
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("RepeaterOuter", false);
            this.mesh.chunkVisible("RepeaterPlane", false);
            this.mesh.chunkVisible("NeedleHBLarge", false);
            this.mesh.chunkVisible("NeedleHBSmall", false);
            this.mesh.chunkVisible("NeedleFuel", false);
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0x0) {
            this.mesh.materialReplace("A8GP2", "DA8GP2");
            this.mesh.materialReplace("A8GP2_night", "DA8GP2_night");
            this.resetYPRmodifier();
            Cockpit.xyz[0] = 0.0f;
            Cockpit.xyz[1] = 0.003f;
            Cockpit.xyz[2] = 0.012f;
            Cockpit.ypr[0] = -3.0f;
            Cockpit.ypr[1] = -3.0f;
            Cockpit.ypr[2] = 9.0f;
            this.mesh.chunkSetLocate("IPCentral", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("NeedleAHCyl", false);
            this.mesh.chunkVisible("NeedleAHBar", false);
            this.mesh.chunkVisible("NeedleAHTurn", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.chunkVisible("NeedleOilTemp", false);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x8) != 0x0 || (this.fm.AS.astateCockpitState & 0x20) != 0x0) {
            this.mesh.materialReplace("D9GP3", "DA8GP3");
            this.mesh.materialReplace("D9GP3_night", "DA8GP3_night");
            this.mesh.chunkVisible("NeedleKMH", false);
            this.mesh.chunkVisible("NeedleCD", false);
            this.mesh.chunkVisible("NeedleAlt", false);
            this.mesh.chunkVisible("NeedleAltKM Kill", false);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        this.retoggleLight();
    }
    
    protected void reflectPlaneMats() {
        final HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }
}