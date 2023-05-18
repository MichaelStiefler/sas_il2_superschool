package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Huey_GunshipAI extends HueyX implements TypeScout, TypeTransport, TypeStormovik {

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

    static {
        final Class class1 = Huey_GunshipAI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "HueyAI");
        Property.set(class1, "meshName", "3DO/Plane/AIHuey/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1988.5F);
        Property.set(class1, "FlightModel", "FlightModels/OCXHueyAI.fmd:OCXHueyAI_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 9, 9, 1, 1, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev10", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalDev12" });
        try {
            final ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            final HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            final byte byte0 = 38;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x30Cal_2xDoorGunners";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "GunPod", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "MGGuns", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGun_2xM158_2xDoorGunners";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "GunPod", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "MGGuns", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonM158", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonM158", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGun_1xM158_1xSUU11_2xDoorGunners";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "GunPod", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunH19", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonSUU11", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "MGGuns", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonM158", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGun_2xLAU3_2xDoorGunners";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "GunPod", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "MGGuns", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunBrowning303t", 300);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "stretcher_with_wounded_GI";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 500);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "stretcher", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (final Exception exception) {
        }
    }
}
