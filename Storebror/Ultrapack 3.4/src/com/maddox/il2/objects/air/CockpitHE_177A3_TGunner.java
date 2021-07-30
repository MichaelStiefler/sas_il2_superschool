package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A3_TGunner extends CockpitGunner
//    implements TypeTurretK4Sight
{
    private class Variables {

        float      dimPosition;
        float      throttle1;
        float      throttle2;
        float      pictAiler;
        float      pictElev;
        float      ailTrim;
        float      prop1;
        float      _prop1;
        float      prop2;
        float      _prop2;
        float      turn;
        float      altimeter;
        float      vspeed;
        float      AFN101;
        float      beaconDirection;
        float      beaconRange;
        float      pictManf1;
        float      pictManf2;
        float      pictManf3;
        float      pictManf4;
        float      prevFuel;
        float      cons;
        float      consumptionL;
        float      consumptionR;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHE_177A3_TGunner.this.fm != null) {
                CockpitHE_177A3_TGunner.this.setTmp = CockpitHE_177A3_TGunner.this.setOld;
                CockpitHE_177A3_TGunner.this.setOld = CockpitHE_177A3_TGunner.this.setNew;
                CockpitHE_177A3_TGunner.this.setNew = CockpitHE_177A3_TGunner.this.setTmp;
                float f = CockpitHE_177A3_TGunner.this.waypointAzimuth();
                if (CockpitHE_177A3_TGunner.this.useRealisticNavigationInstruments()) {
                    CockpitHE_177A3_TGunner.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitHE_177A3_TGunner.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitHE_177A3_TGunner.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_177A3_TGunner.this.setOld.radioCompassAzimuth.getDeg(0.02F),
                            CockpitHE_177A3_TGunner.this.radioCompassAzimuthInvertMinus() - CockpitHE_177A3_TGunner.this.setOld.azimuth.getDeg(1.0F) - 90F);
                } else CockpitHE_177A3_TGunner.this.setNew.waypointAzimuth.setDeg(CockpitHE_177A3_TGunner.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHE_177A3_TGunner.this.setOld.azimuth.getDeg(1.0F));
                CockpitHE_177A3_TGunner.this.setNew.azimuth.setDeg(CockpitHE_177A3_TGunner.this.setOld.azimuth.getDeg(1.0F), CockpitHE_177A3_TGunner.this.fm.Or.azimut());
                CockpitHE_177A3_TGunner.this.setNew.throttle1 = 0.85F * CockpitHE_177A3_TGunner.this.setOld.throttle1 + CockpitHE_177A3_TGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitHE_177A3_TGunner.this.setNew.throttle2 = 0.85F * CockpitHE_177A3_TGunner.this.setOld.throttle2 + CockpitHE_177A3_TGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitHE_177A3_TGunner.this.setNew.pictAiler = 0.85F * CockpitHE_177A3_TGunner.this.setOld.pictAiler + 0.15F * CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
                CockpitHE_177A3_TGunner.this.setNew.pictElev = 0.85F * CockpitHE_177A3_TGunner.this.setOld.pictElev + 0.15F * CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
                CockpitHE_177A3_TGunner.this.setNew.ailTrim = 0.85F * CockpitHE_177A3_TGunner.this.setOld.ailTrim + 0.15F * CockpitHE_177A3_TGunner.this.fm.CT.trimAileron;
                if (Math.toDegrees(CockpitHE_177A3_TGunner.this.fm.EI.engines[0].getPropPhi()) < 36D) {
                    CockpitHE_177A3_TGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_TGunner.this.setOld.prop1 + ((FlightModelMain) CockpitHE_177A3_TGunner.this.fm).EI.engines[0].getControlProp() * 0.15F;
                    CockpitHE_177A3_TGunner.this.setNew._prop1 = CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.setNew.prop1, 0.0F, 1.0F, -22F, -63F);
                } else {
                    CockpitHE_177A3_TGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_TGunner.this.setOld.prop1;
                    CockpitHE_177A3_TGunner.this.setNew._prop1 = CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.setNew.prop1, 0.0F, 1.0F, 0.0F, -63F);
                }
                if (Math.toDegrees(CockpitHE_177A3_TGunner.this.fm.EI.engines[1].getPropPhi()) < 36D) {
                    CockpitHE_177A3_TGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_TGunner.this.setOld.prop2 + ((FlightModelMain) CockpitHE_177A3_TGunner.this.fm).EI.engines[1].getControlProp() * 0.15F;
                    CockpitHE_177A3_TGunner.this.setNew._prop2 = CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.setNew.prop2, 0.0F, 1.0F, -22F, -63F);
                } else {
                    CockpitHE_177A3_TGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_TGunner.this.setOld.prop2;
                    CockpitHE_177A3_TGunner.this.setNew._prop2 = CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.setNew.prop2, 0.0F, 1.0F, 0.0F, -63F);
                }
                CockpitHE_177A3_TGunner.this.w.set(CockpitHE_177A3_TGunner.this.fm.getW());
                CockpitHE_177A3_TGunner.this.fm.Or.transform(CockpitHE_177A3_TGunner.this.w);
                CockpitHE_177A3_TGunner.this.setNew.turn = (12F * CockpitHE_177A3_TGunner.this.setOld.turn + CockpitHE_177A3_TGunner.this.w.z) / 13F;
                CockpitHE_177A3_TGunner.this.setNew.altimeter = 0.85F * CockpitHE_177A3_TGunner.this.setOld.altimeter + CockpitHE_177A3_TGunner.this.fm.getAltitude() * 0.15F;
                CockpitHE_177A3_TGunner.this.setNew.vspeed = (99F * CockpitHE_177A3_TGunner.this.setOld.vspeed + CockpitHE_177A3_TGunner.this.fm.getVertSpeed()) / 100F;
                float f1 = CockpitHE_177A3_TGunner.this.fm.Or.getKren();
                float f2 = CockpitHE_177A3_TGunner.this.fm.Or.getTangage();
                if (f1 > 55F || f1 < -55F || f2 < -55F || f2 > 55F) CockpitHE_177A3_TGunner.this.Pn.z = 250D;
                else {
                    CockpitHE_177A3_TGunner.this.Pn.set(CockpitHE_177A3_TGunner.this.fm.Loc);
                    CockpitHE_177A3_TGunner.this.Pn.z = CockpitHE_177A3_TGunner.this.fm.getAltitude() - Engine.cur.land.HQ(((Tuple3d) CockpitHE_177A3_TGunner.this.Pn).x, ((Tuple3d) CockpitHE_177A3_TGunner.this.Pn).y);
                    double d = CockpitHE_177A3_TGunner.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f1));
                    double d1 = CockpitHE_177A3_TGunner.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f2));
                    CockpitHE_177A3_TGunner.this.Pn.z = (float) Math.sqrt(d * d + d1 * d1 + CockpitHE_177A3_TGunner.this.Pn.z * CockpitHE_177A3_TGunner.this.Pn.z);
                    if (CockpitHE_177A3_TGunner.this.fm.CT.getGear() > 0.5F) CockpitHE_177A3_TGunner.this.Pn.z = CockpitHE_177A3_TGunner.this.cvt((float) CockpitHE_177A3_TGunner.this.Pn.z, 0.0F, 150F, 0.0F, 250F);
                    else CockpitHE_177A3_TGunner.this.Pn.z = CockpitHE_177A3_TGunner.this.cvt((float) CockpitHE_177A3_TGunner.this.Pn.z, 0.0F, 750F, 0.0F, 250F);
                }
                CockpitHE_177A3_TGunner.this.setNew.AFN101 = 0.9F * CockpitHE_177A3_TGunner.this.setOld.AFN101 + 0.1F * (float) CockpitHE_177A3_TGunner.this.Pn.z;
                CockpitHE_177A3_TGunner.this.setNew.beaconDirection = (10F * CockpitHE_177A3_TGunner.this.setOld.beaconDirection + CockpitHE_177A3_TGunner.this.getBeaconDirection()) / 11F;
                CockpitHE_177A3_TGunner.this.setNew.beaconRange = (10F * CockpitHE_177A3_TGunner.this.setOld.beaconRange + CockpitHE_177A3_TGunner.this.getBeaconRange()) / 11F;
                CockpitHE_177A3_TGunner.this.setNew.pictManf1 = 0.9F * CockpitHE_177A3_TGunner.this.setOld.pictManf1 + 0.1F * CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_TGunner.this.setNew.pictManf2 = 0.8F * CockpitHE_177A3_TGunner.this.setOld.pictManf2 + 0.2F * CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_TGunner.this.setNew.pictManf3 = 0.9F * CockpitHE_177A3_TGunner.this.setOld.pictManf3 + 0.1F * CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_TGunner.this.setNew.pictManf4 = 0.8F * CockpitHE_177A3_TGunner.this.setOld.pictManf4 + 0.2F * CockpitHE_177A3_TGunner.this.cvt(CockpitHE_177A3_TGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                float f3 = CockpitHE_177A3_TGunner.this.setNew.prevFuel - CockpitHE_177A3_TGunner.this.fm.M.fuel;
                CockpitHE_177A3_TGunner.this.setNew.prevFuel = CockpitHE_177A3_TGunner.this.fm.M.fuel;
                f3 /= 0.72F;
                f3 /= Time.tickLenFs();
                f3 *= 3600F;
                CockpitHE_177A3_TGunner.this.setNew.cons = 0.9F * CockpitHE_177A3_TGunner.this.setOld.cons + 0.1F * f3;
                float f4 = CockpitHE_177A3_TGunner.this.fm.EI.engines[0].getEngineForce().x;
                float f5 = CockpitHE_177A3_TGunner.this.fm.EI.engines[1].getEngineForce().x;
                float f6 = CockpitHE_177A3_TGunner.this.setNew.cons;
                CockpitHE_177A3_TGunner.this.setNew.consumptionL = 0.9F * CockpitHE_177A3_TGunner.this.setOld.consumptionL + 0.1F * (f6 * f4) / (f4 + f5 + 1.0F);
                CockpitHE_177A3_TGunner.this.setNew.consumptionR = 0.9F * CockpitHE_177A3_TGunner.this.setOld.consumptionR + 0.1F * (f6 * f5) / (f4 + f5 + 1.0F);
                if (CockpitHE_177A3_TGunner.this.cockpitDimControl) {
                    if (CockpitHE_177A3_TGunner.this.setNew.dimPosition > 0.0F) CockpitHE_177A3_TGunner.this.setNew.dimPosition = CockpitHE_177A3_TGunner.this.setOld.dimPosition - 0.05F;
                } else if (CockpitHE_177A3_TGunner.this.setNew.dimPosition < 1.0F) CockpitHE_177A3_TGunner.this.setNew.dimPosition = CockpitHE_177A3_TGunner.this.setOld.dimPosition + 0.05F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            if (this.aircraft().thisWeaponsName.endsWith("Schlong")) this.mesh.chunkVisible("Schlong", true);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Head1_D0", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
        this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
        super.doFocusLeave();
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Z_TurretA", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -orient.getTangage());
        this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F, this.floatindex(this.cvt(orient.getTangage(), 0.0F, 90F, 0.0F, 18F), Turret_Z1));
        this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F, -this.floatindex(this.cvt(orient.getTangage(), 0.0F, 90F, 0.0F, 18F), Turret_Z2));
        this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -orient.getYaw() * 5F, 0.0F);
        this.CalculateRevi(orient);
        this.LastOrient = orient;
    }

    public void CalculateRevi(Orient orient) {
        ((HE_177A3) this.fm.actor).CalculateRevi(-orient.getYaw(), orient.getTangage());
        float f = ((HE_177A3) this.fm.actor).getB1_HeadTangage();
        float f1 = ((HE_177A3) this.fm.actor).getB1_HeadYaw();
        float f2 = ((HE_177A3) this.fm.actor).getB1_HeadTangage1();
        float f3 = ((HE_177A3) this.fm.actor).getB1_ReviYaw();
        float f4 = ((HE_177A3) this.fm.actor).getB1_ReviX();
        float f5 = ((HE_177A3) this.fm.actor).getB1_ReviY();
        this.mesh.chunkSetAngles("Z_TurretB2", 0.0F, 0.0F, f);
        this.mesh.chunkSetAngles("Z_Turret_Revi", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi1", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi2", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi4", orient.getYaw(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -f5;
        this.mesh.chunkSetLocate("Z_tRevi5", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = f4;
        this.mesh.chunkSetLocate("Z_tRevi3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_tRevi6", 0.0F, 0.0F, f2);
        this.mesh.chunkSetAngles("Z_tRevi7", f3, 0.0F, 0.0F);
    }

    private void calculateSight() {
        this.CalculateRevi(this.LastOrient);
    }

    public void typeTurretK4IronSights() {
        this.sightMode++;
        if (this.sightMode > 2) this.sightMode = 0;
    }

    public void typeTurretK4AdjDistancePlus() {
        ((HE_177A3) this.fm.actor).B1_Distance += 10F;
        if (((HE_177A3) this.fm.actor).B1_Distance > 500F) ((HE_177A3) this.fm.actor).B1_Distance = 500F;
        this.mesh.chunkSetAngles("Z_ReviDist", 0.0F, 0.0F, (500F - ((HE_177A3) this.fm.actor).B1_Distance) / 2.0F);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K4DistanceM", new Object[] { new Integer((int) ((HE_177A3) this.fm.actor).B1_Distance) });
        this.calculateSight();
    }

    public void typeTurretK4AdjDistanceMinus() {
        ((HE_177A3) this.fm.actor).B1_Distance -= 10F;
        if (((HE_177A3) this.fm.actor).B1_Distance < 100F) ((HE_177A3) this.fm.actor).B1_Distance = 100F;
        this.mesh.chunkSetAngles("Z_ReviDist", 0.0F, 0.0F, (500F - ((HE_177A3) this.fm.actor).B1_Distance) / 2.0F);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K4DistanceM", new Object[] { new Integer((int) ((HE_177A3) this.fm.actor).B1_Distance) });
        this.calculateSight();
    }

    public void typeTurretK4AdjDistance(float f) {
        ((HE_177A3) this.fm.actor).B1_Distance = this.cvt(f, 0.0F, 1.0F, 100F, 500F);
        this.mesh.chunkSetAngles("Z_ReviDist", 0.0F, 0.0F, (500F - ((HE_177A3) this.fm.actor).B1_Distance) / 2.0F);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K4DistanceM", new Object[] { new Integer((int) ((HE_177A3) this.fm.actor).B1_Distance) });
        this.calculateSight();
    }

    public void typeTurretK4AdjSpanPlus() {
        ((HE_177A3) this.fm.actor).B1_Speed += 25F;
        if (((HE_177A3) this.fm.actor).B1_Speed > 600F) ((HE_177A3) this.fm.actor).B1_Speed = 600F;
        this.mesh.chunkSetAngles("Z_ReviSpeed", 0.0F, 0.0F, ((HE_177A3) this.fm.actor).B1_Speed / 2.0F);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K4SpanM", new Object[] { new Integer((int) ((HE_177A3) this.fm.actor).B1_Speed) });
        this.calculateSight();
    }

    public void typeTurretK4AdjSpanMinus() {
        ((HE_177A3) this.fm.actor).B1_Speed -= 25F;
        if (((HE_177A3) this.fm.actor).B1_Speed < 0.0F) ((HE_177A3) this.fm.actor).B1_Speed = 0.0F;
        this.mesh.chunkSetAngles("Z_ReviSpeed", 0.0F, 0.0F, ((HE_177A3) this.fm.actor).B1_Speed / 2.0F);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K4SpanM", new Object[] { new Integer((int) ((HE_177A3) this.fm.actor).B1_Speed) });
        this.calculateSight();
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for (; f < -180F; f += 360F)
            ;
        for (; f > 180F; f -= 360F)
            ;
        for (; this.prevA0 < -180F; this.prevA0 += 360F)
            ;
        for (; this.prevA0 > 180F; this.prevA0 -= 360F)
            ;
        if (!this.isRealMode()) {
            this.prevA0 = f;
            return;
        }
        if (this.bNeedSetUp) {
            this.prevTime = Time.current() - 1L;
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
        if (f < -120F && this.prevA0 > 120F) f += 360F;
        else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
        float f3 = f - this.prevA0;
        float f12 = 0.001F * (Time.current() - this.prevTime);
        float f13 = Math.abs(f3 / f12);
        if (f13 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f12;
        else if (f < this.prevA0) f = this.prevA0 - 120F * f12;
        this.prevTime = Time.current();
        f3 = 0.0F;
        f12 = 0.0F;
        if (f >= -5F && f <= 5D) {
            if (f > -1F && f < 1.5F && f1 < 11F) this.bDontShot = true;
            else this.bDontShot = false;
            if (f1 < 3F) f12 = 3F;
        } else if (f > 5F && f <= 8F) {
            float f4 = this.cvt(f, 5F, 8F, 3F, 0.0F);
            if (f1 < f4) f12 = f4;
        } else if (f >= -8F && f < -5F) {
            float f5 = this.cvt(f, -8F, -5F, 0.0F, 3F);
            if (f1 < f5) f12 = f5;
        } else if (f <= -169F || f >= 172D) {
            if (f1 < 4F) f12 = 4F;
        } else if (f >= 169F && f < 172F) {
            float f6 = this.cvt(f, 169F, 172F, 0.0F, 4F);
            if (f1 < f6) f12 = f6;
        } else if (f > -169F && f <= -166F) {
            float f7 = this.cvt(f, -169F, -166F, 4F, 0.0F);
            if (f1 < f7) f12 = f7;
        } else if (f >= 77F && f <= 85F) {
            if (f1 < 1.5F) f12 = 1.5F;
        } else if (f >= 74F && f < 77F) {
            float f8 = this.cvt(f, 74F, 77F, 0.0F, 1.5F);
            if (f1 < f8) f12 = f8;
        } else if (f > 85F && f <= 88F) {
            float f9 = this.cvt(f, 85F, 88F, 1.5F, 0.0F);
            if (f1 < f9) f12 = f9;
        } else if (f >= -85F && f <= -77F) {
            if (f1 < 1.5F) f12 = 1.5F;
        } else if (f >= -88F && f < -85F) {
            float f10 = this.cvt(f, -88F, -85F, 0.0F, 1.5F);
            if (f1 < f10) f12 = f10;
        } else if (f > -77F && f <= -74F) {
            float f11 = this.cvt(f, -77F, -74F, 1.5F, 0.0F);
            if (f1 < f11) f12 = f11;
        }
        if (f1 > 87F) f1 = 87F;
        else if (f1 < f12) f1 = f12;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        this.prevA0 = f;
    }

    protected void interpTick() {
        if (this.bDontShot) this.bGunFire = false;
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) this.mesh.chunkSetAngles("Z_tGunFire", 0.0F, 0.0F, 1.2F);
        else this.mesh.chunkSetAngles("Z_tGunFire", 0.0F, 0.0F, 0.0F);
        if (((HE_177A3) this.aircraft()).bElectroSound && ((HE_177A3) this.aircraft()).bSightAutomation) {
            this.sfxStart(16);
            ((HE_177A3) this.aircraft()).bElectroSound = false;
        } else if (((HE_177A3) this.aircraft()).bElectroSound && !((HE_177A3) this.aircraft()).bSightAutomation) {
            this.sfxStop(16);
            ((HE_177A3) this.aircraft()).bElectroSound = false;
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitHE_177A3_TGunner() {
        super("3DO/Cockpit/He-177A-3-TGun/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.bDontShot = false;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.sightMode = 0;
        this.w = new Vector3f();
        this.w = new Vector3f();
        this.Pn = new Point3d();
        this.cockpitNightMats = new String[] { "CompGrad", "Fl20274", "Fl20342na", "Fl20342", "Fl20516", "Fl20516_1", "Fl20556", "Fl20570", "Fl20572", "Fl20723_1000", "Fl20723_1185", "Fl20723_1850", "Fl20723_200na", "Fl20723_200", "Fl20723_640",
                "Fl20841", "Fl22231", "Fl22316", "Fl22320", "Fl22334b", "Fl22334c", "Fl22382", "Fl22412", "Fl22413", "Fl22561", "Fl23885na", "Fl23885", "Fl30489", "Fl30532", "Fl32336", "Gauge19", "Gauge20", "Ln27002", "Ln28330b", "Ln28330",
                "NeedlesnLights", "Nr92182B1na", "Voltmeters" };
//		this.hidePilot = true;
        this.setNightMats(false);
//        gunLeverMoveAxis = -1;
//        magazines = -2;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.setNew.dimPosition = this.setOld.dimPosition = 1.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.prevTime = Time.current() - 1L;
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
        this.mesh.chunkSetAngles("Z_ReVi25Tint", this.cvt(this.setNew.dimPosition, 0.0F, 1.0F, -40F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Lotfe7", ((HE_177A3) this.fm.actor).fSightCurSideslip * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle3", 41.8F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle4", 41.8F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot1", 70F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot2", 70F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot3", 70F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot4", 70F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng1Starter", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng2Starter", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng3Starter", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng4Starter", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkVisible("F_LGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("F_RGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("F_AllGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear && this.fm.Gears.rgear);
        this.mesh.chunkVisible("F_TailGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.cgear);
        this.mesh.chunkVisible("F_LGearsUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("F_RGearsUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("F_AllGearsUp", this.fm.CT.getGear() == 0.0F && this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("F_TailGearsUp", this.fm.CT.getGear() == 0.0F);
        float f1 = this.fm.CT.getFlap();
        this.mesh.chunkVisible("F_FlapsUp", f1 < 0.1F);
        this.mesh.chunkVisible("F_FlapsUnf", f1 > 0.1F && f1 < 0.5F);
        this.mesh.chunkVisible("F_FlapsExt", f1 > 0.5F);
        boolean flag = false;
        if (!this.fm.CT.bHasFlapsControl) flag = true;
        else {
            float f2 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
            if (f1 > 0.21F && f2 > 270F && (f2 - 270F) * f1 > 8F) flag = true;
        }
        this.mesh.chunkVisible("F_Fl32558", flag);
        if (this.setNew.pictElev < 0.0F) this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
        else this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
        this.mesh.chunkSetAngles("Z_Handle", 93F * this.setNew.pictAiler, 0.0F, 0.0F);
        float f3 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Z_RichagL", -35F * f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Tiaga4L", 35F * f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RichagR", 35F * f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Koromislo1L", 0.0F, 0.0F, 22F * f3);
        this.mesh.chunkSetAngles("Z_Koromislo2L", 0.0F, 0.0F, 22F * f3);
        this.mesh.chunkSetAngles("Z_PedalBomL", 0.0F, 0.0F, -22F * f3);
        this.mesh.chunkSetAngles("Z_Koromislo1R", 0.0F, 0.0F, -22F * f3);
        this.mesh.chunkSetAngles("Z_Koromislo2R", 0.0F, 0.0F, -22F * f3);
        this.mesh.chunkSetAngles("Z_PedalBomR", 0.0F, 0.0F, 22F * f3);
        float f4 = this.fm.CT.getBrake();
        float f5 = this.fm.CT.getBrake();
        float f6 = 20F * f4;
        float f7 = 106.3657F - f6;
        double d = Math.cos(Geom.DEG2RAD(f7));
        float f8 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d) / Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d)));
        float f9 = 180F - f7 - f8;
        float f10 = 62.8136F - f8;
        float f11 = f9 - 10.82074F;
        this.mesh.chunkSetAngles("Z_Kachalka2L", f6, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Tiaga3L", f10, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CilindrL", 0.0F, f11, 0.0F);
        float f12 = 20F * f5;
        double d1 = Math.cos(Geom.DEG2RAD(142.0612F + f6));
        double d2 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d1);
        float f13 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d1) / d2));
        float f14 = 21.38197F - f13;
        float f15 = 89.43449F + f14 + 35F * f3;
        double d3 = Math.sqrt(0.035435102880001068D + d2 * d2 - 0.37648427486419678D * d2 * (float) Math.cos(Geom.DEG2RAD(f15)));
        float f16 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d3 * d3) / 0.031839244067668915D));
        float f17 = 89.43554F - f16;
        this.mesh.chunkSetAngles("Z_Tiaga2L", f17, 0.0F, 0.0F);
        float f18 = (float) Math.acos((0.035435102880001068D + d3 * d3 - d2 * d2) / (0.37648427486419678D * d3));
        float f19 = (float) Math.acos((0.0071521135978400707D + d3 * d3 - 0.035434890538454056D) / (0.16914033889770508D * d3));
        float f20 = Geom.RAD2DEG(f18 + f19) - 90.56514F;
        this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f20);
        double d4 = Math.cos(Geom.DEG2RAD(142.0612F + f12));
        double d5 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d4);
        float f21 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d4) / d5));
        float f22 = 21.38197F - f21;
        float f23 = 89.43449F + f22 - 35F * f3;
        double d6 = Math.sqrt(0.035435102880001068D + d5 * d5 - 0.37648427486419678D * d5 * (float) Math.cos(Geom.DEG2RAD(f23)));
        float f24 = (float) Math.acos((0.035435102880001068D + d6 * d6 - d5 * d5) / (0.37648427486419678D * d6));
        float f25 = (float) Math.acos((0.0071521135978400707D + d6 * d6 - 0.035434890538454056D) / (0.16914033889770508D * d6));
        float f26 = Geom.RAD2DEG(f24 + f25) - 90.56514F;
        this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f26);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -this.cvt(this.setNew.ailTrim, -0.5F, 0.5F, -0.0722F, 0.0722F);
        this.mesh.chunkSetLocate("Z_AilronTrim2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_PropPitch1", -this.setNew._prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch2", -this.setNew._prop2, 0.0F, 0.0F);
        float f27 = (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f27 = (int) (f27 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("Z_N_Clock1_M", f27 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock1_H", f27 * 5F, 0.0F, 0.0F);
        float f28 = (float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin());
        f28 = (int) (f28 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("Z_N_Clock2_M", f28 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock2_H", f28 * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock3_H", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock3_M", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock3_S", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress1", -200F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress2", -200F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress3", -200F, 0.0F, 0.0F);
        float f29 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        this.mesh.chunkSetAngles("Z_N_Turn1", f29, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Turn2", f29, 0.0F, 0.0F);
        f29 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 17F, -17F);
        this.mesh.chunkSetAngles("Z_N_Turn3", f29, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirSpeed1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirSpeed2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AH1", 0.0F, 0.0F, -this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_N_AH2", -this.cvt(this.fm.Or.getTangage(), -45F, 45F, -8F, 8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt_km", -this.cvt(this.setNew.altimeter, 0.0F, 11000F, 0.0F, 330F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Alti", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Alti2", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Climb1", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Climb2", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_RadioAltim", -this.setNew.AFN101, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ln28330b", this.cvt(this.fm.CT.getGear(), 0.4F, 0.6F, 40.2F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Nav1", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Nav2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("F_N_AFN2_RED", this.isOnBlindLandingMarker());
        this.mesh.chunkSetAngles("Z_N_Nav1C", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Nav2C", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("F_N_AFN2C_RED", this.isOnBlindLandingMarker());
        this.mesh.chunkSetAngles("Z_N_AiFuePress1", -this.setNew.pictManf1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AiFuePress2", -this.setNew.pictManf2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AiFuePress3", -this.setNew.pictManf3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AiFuePress4", -this.setNew.pictManf4, 0.0F, 0.0F);
        float f30 = ((FlightModelMain) this.fm).EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_N_RPM1", -this.floatindex(this.cvt(f30, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_RPM2", -this.floatindex(this.cvt(f30, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        float f31 = ((FlightModelMain) this.fm).EI.engines[1].getRPM();
        this.mesh.chunkSetAngles("Z_N_RPM3", -this.floatindex(this.cvt(f31, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_RPM4", -this.floatindex(this.cvt(f31, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_FuelConsom", -this.cvt(0.5F * this.setNew.consumptionL, 0.0F, 500F, 0.0F, 255.5F), 0.0F, 0.0F);
//		int i = ((HE_177A3) this.aircraft()).iRust;
        float f32 = this.fm.M.fuel;
//		if (i == 2) {
//			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 6825.781F, 9382.67F, 0.0F, 62.93919F), 0.0F, 0.0F);
//			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
//		} else if (i == 1) {
//			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 5395.4F, 7952.296F, 0.0F, 62.93919F), 0.0F, 0.0F);
//			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
//		} else {
//			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 5395.4F, 6521.92F, 0.0F, 55.45946F), 0.0F, 0.0F);
//			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
//		}
        switch (((HE_177A3) this.aircraft()).iRust) {
            case 3:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 6825.781F, 10813.045F, 0.0F, 62.93919F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
                break;
            case 2:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 6825.781F, 9382.67F, 0.0F, 62.93919F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
                break;
            case 1:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 5395.4F, 7952.295F, 0.0F, 62.93919F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
                break;
            default:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f32, 5395.4F, 6521.92F, 0.0F, 55.45946F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f32, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
                break;
        }
        this.mesh.chunkSetAngles("Z_N_Fuel6", -this.cvt(f32, 3424.015F, 4268.9F, 0.0F, 72.63291F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel2", -this.cvt(f32, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel3", -this.cvt(f32, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel7", -this.cvt(f32, 844.89F, 2505.015F, 0.0F, 81.76F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel8", -this.cvt(f32, 844.89F, 2505.015F, 0.0F, 81.76F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel1", -this.cvt(f32, 0.0F, 844.89F, 0.0F, 72.63291F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_FuelQuant", -this.cvt(f32, 0.0F, this.fm.M.maxFuel, 37F, 84F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress1", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress2", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress3", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress4", this.fm.M.fuel <= 1.0F ? 0.0F : -83F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Fl22334b", -(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22334c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22338b", -(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22338c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Fl22334b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22334c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22338b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22338c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Course2a", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("Z_Course2b", ((HE_177A3)aircraft()).getCourse(), 0.0F, 0.0F);
        float f33 = -this.fm.turret[1].tu[0];
        float f34 = this.fm.turret[1].tu[1];
        float f35 = Geom.DEG2RAD(f33);
        float f36 = Geom.DEG2RAD(f34);
        float f37 = (float) Math.asin(Math.tan(f35) * Math.cos(f36) * Math.cos(f35));
        float f38 = (float) Math.atan(Math.tan(f36) / Math.cos(f35));
        float f39 = Geom.RAD2DEG(f37);
        float f40 = Geom.RAD2DEG(f38);
        this.mesh.chunkSetAngles("Z_Turret151A", 0.0F, f39, 0.0F);
        this.mesh.chunkSetAngles("Z_Turret151B", 0.0F, 0.0F, f40);
        f33 = this.fm.turret[2].tu[0];
        f34 = -this.fm.turret[2].tu[1];
        f35 = Geom.DEG2RAD(f33);
        f36 = Geom.DEG2RAD(f34);
        f37 = (float) Math.asin(Math.tan(f35) * Math.cos(f36) * Math.cos(f35));
        f38 = (float) Math.atan(Math.tan(f36) / Math.cos(f35));
        f39 = Geom.RAD2DEG(f37);
        f40 = Geom.RAD2DEG(f38);
        this.mesh.chunkSetAngles("Z_Turret131B", f39, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Turret131A", 0.0F, 0.0F, f40);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("xGlassDm1", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm1", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("xGlass1", false);
            this.mesh.chunkVisible("xGlass1_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("xGlass2", false);
            this.mesh.chunkVisible("xGlass2_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("xGlassDm2", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("xGlass3", false);
            this.mesh.chunkVisible("xGlass3_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("xGlass4", false);
            this.mesh.chunkVisible("xGlass4_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlass5", false);
            this.mesh.chunkVisible("xGlass5_dmg", true);
        }
        if (!this.cockpitLightControl) {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private boolean   bNeedSetUp;
    private boolean   bDontShot;
    private long      prevTime;
    private float     prevA0;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private Orient    LastOrient;
    private int       sightMode;
//    private static final int SIGHT_BOTH = 0;
//    private static final int SIGHT_IRON_ONLY = 1;
//    private static final int SIGHT_RETICLE_ONLY = 2;
    public Vector3f            w;
    private Point3d            Pn;
    private static final float Turret_Z1[]     = { 0.0F, 4.235F, 8.64F, 13.21F, 17.92F, 22.79F, 27.82F, 32.99F, 38.29F, 43.75F, 49.36F, 55.12F, 61.05F, 67.13F, 73.4F, 79.87F, 86.55F, 93.45F, 100.61F };
    private static final float Turret_Z2[]     = { 0.0F, 2.125F, 4.32F, 6.58F, 8.87F, 11.188F, 13.53F, 15.88F, 18.21F, 20.52F, 22.78F, 24.98F, 27.105F, 29.13F, 31.03F, 32.76F, 34.33F, 35.68F, 36.78F };
    private static final float IAS_Scale[]     = { 0.0F, 8F, 18.4F, 41.26F, 67.26F, 94.36F, 119.58F, 141F, 166.1F, 190.43F, 216.05F, 241.18F, 267.5F, 293.8F, 318.19F, 341.7F };
    private static final float Fl20342_Scale[] = { 0.0F, 5.5F, 11F, 17.5F, 25F, 33F, 41.5F, 50.5F, 59.5F, 67F, 73.5F, 80.25F, 85.5F, 90F };
    private static final float Fl20274_Scale[] = { 0.0F, 4.25F, 8F, 12F, 17F, 23F, 28.5F, 34.5F, 42F, 51F, 58.5F, 69F, 79F, 91F, 103F, 114.5F, 128F, 140F, 152.5F, 164.5F, 175F, 186.5F, 195.5F, 205F, 214F, 222.5F, 230.5F, 239F, 246.5F, 253.5F, 260.5F,
            265F, 269.5F };
    final float                constAB         = 0.03866613F;
    final float                constAC         = 0.1832059F;
    final float                constOA         = 0.0391989F;
    final float                constOB         = 0.05015091F;
    final float                constAC2        = 0.1882421F;
    final float                constBD2        = 0.1882416F;
    final float                constCD2        = 0.08457017F;

    static {
        Class class1 = CockpitHE_177A3_TGunner.class;
        Property.set(class1, "aiTuretNum", 3);
        Property.set(class1, "weaponControlNum", 13);
        Property.set(class1, "astatePilotIndx", 2);
        Property.set(class1, "normZNs", new float[] { 1.8F, 1.7F, 2.4F, 1.7F });
    }
}
