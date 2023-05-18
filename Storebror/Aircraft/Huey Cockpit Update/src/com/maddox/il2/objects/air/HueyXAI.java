package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.fm.FlightModelMain;

public abstract class HueyXAI extends HueyX {

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (World.Sun().ToSun.z < -0.22F) {
            super.FM.AS.setNavLightsState(true);
        } else {
            super.FM.AS.setNavLightsState(false);
        }
    }

    private void stability() {
        double d = 0.0D;
        final Vector3d vector3d = new Vector3d();
        this.getSpeed(vector3d);
        final Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        final float f = (float) (this.FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
        if ((f < 10F) && (this.FM.getSpeedKMH() < 60F) && (vector3d.z < -1D)) {
            vector3d.z *= 0.9D;
            this.setSpeed(vector3d);
        }
        if (((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr > 2) {
            vector3d.x *= 0.99D;
            vector3d.y *= 0.99D;
            this.setSpeed(vector3d);
        }
        if (this.FM.getSpeedKMH() > 180F) {
            d = (this.FM.getSpeedKMH() - this.FM.VmaxFLAPS) / 10F;
            if (d < 0.0D) {
                d = 0.0D;
            }
        }
        final Point3d point3d1 = new Point3d(0.0D, 0.0D, 0.0D);
        point3d1.x = 0.0D - (this.FM.Or.getTangage() / 10F - (this.FM.CT.getElevator() * 2.5D));
        point3d1.y = 0.0D - (this.FM.Or.getKren() / 10F - (this.FM.CT.getAileron() * 2.5D)) - d;
        point3d1.z = 2D;
        this.FM.EI.engines[0].setPropPos(point3d1);
        this.FM.producedAF.x += 7000D * (-this.FM.CT.getElevator() * this.FM.EI.engines[0].getPowerOutput());
        this.FM.producedAF.y += 6000D * (-this.FM.CT.getAileron() * this.FM.EI.engines[0].getPowerOutput());
    }

    public void update(float f) {
        this.tiltRotor(f);
        this.stability();
        if ((((Pilot) super.FM).get_maneuver() == 25) && ((FlightModelMain) (super.FM)).AP.way.isLandingOnShip() && (((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr >= 3)) {
            ((FlightModelMain) (super.FM)).CT.BrakeControl = 1.0F;
        }

        super.update(f);

        final float f2 = this.FM.EI.getPowerOutput() * Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 600F, 2.0F, 0.0F);
        if (this.FM.CT.getAirBrake() > 0.5F) {
            if (this.FM.Or.getTangage() > 5F) {
                this.FM.getW().scale(Aircraft.cvt(this.FM.Or.getTangage(), 45F, 90F, 1.0F, 0.1F));
                float f3 = this.FM.Or.getTangage();
                if (Math.abs(this.FM.Or.getKren()) > 90F) {
                    f3 = 90F + (90F - f3);
                }
                float f4 = f3 - 90F;
                this.FM.CT.trimElevator = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                f4 = this.FM.Or.getKren();
                if (Math.abs(f4) > 90F) {
                    if (f4 > 0.0F) {
                        f4 = 180F - f4;
                    } else {
                        f4 = -180F - f4;
                    }
                }
                this.FM.CT.trimAileron = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                this.FM.CT.trimRudder = Aircraft.cvt(f4, -15F, 15F, 0.04F, -0.04F);
            }
        } else {
            this.FM.CT.trimAileron = 0.0F;
            this.FM.CT.trimElevator = 0.0F;
            this.FM.CT.trimRudder = 0.0F;
        }
        this.FM.Or.increment(f2 * (this.FM.CT.getRudder() + this.FM.CT.getTrimRudderControl()), f2 * (this.FM.CT.getElevator() + this.FM.CT.getTrimElevatorControl()), f2 * (this.FM.CT.getAileron() + this.FM.CT.getTrimAileronControl()));
    }
}
