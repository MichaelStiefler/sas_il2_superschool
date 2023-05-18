package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

public abstract class F4U extends Scheme1 implements TypeFighter, TypeDiveBomber {

    public F4U() {
        this.arrestorPos = 0.0F;
        this.arrestorVel = 0.0F;
        this.prevGear = 0.0F;
        this.prevGear2 = 0.0F;
        this.prevWing = 1.0F;
        this.cGearPos = 0.0F;
        this.cGear = 0.0F;
        this.bNeedSetup = true;
        this.flapps = 0.0F;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public static void moveGear(HierMesh hiermesh, float f, FlightModel flightmodel) {
        float f6 = 10F * f;
        if (flightmodel != null) f = 10F * Math.max(f, flightmodel.CT.getAirBrake());
        else f = f6;
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        if (bGearExtending) {
            hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 60F), 0.0F);
            hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 60F), 0.0F);
            if (flightmodel == null) {
                float f1 = cvt(f6, 0.0F, 1.0F, 0.0F, 0.7071068F);
                f1 = 2.0F * f1 * f1;
                hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 41F * f1, 0.0F);
                hiermesh.chunkSetAngles("Hook1_D0", 0.0F, cvt(f1, 0.0F, 1.0F, 0.0F, -64.5F), 0.0F);
                f1 = cvt(f6, 0.0F, 0.25F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 140F * f1, 0.0F);
                hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 140F * f1, 0.0F);
            }
            float f2;
            float f5;
            if (f < 4F) f2 = f5 = cvt(f, 3F, 4F, 0.0F, 0.4F);
            else {
                f2 = cvt(f, 4F, 8F, 0.75F, 2.0F);
                f2 = (float) Math.sqrt(f2);
                f2 = cvt(f2, (float) Math.sqrt(0.75D), (float) Math.sqrt(2D), 0.4F, 1.0F);
                f5 = cvt(f, 4F, 8.5F, 0.75F, 2.0F);
                f5 = (float) Math.sqrt(f5);
                f5 = cvt(f5, (float) Math.sqrt(0.75D), (float) Math.sqrt(2D), 0.4F, 1.0F);
            }
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 81F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 84F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 83F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL33_D0", 0.0F, -104F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 40F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -168F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -90F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 81F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 84F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 83F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR33_D0", 0.0F, -104F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 40F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -168F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -90F * f5, 0.0F);
        } else {
            if (flightmodel == null) {
                float f3 = cvt(f6, 8.5F, 10F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 41F * f3, 0.0F);
                f3 = cvt(f6, 8.5F, 8.75F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 140F * f3, 0.0F);
                hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 140F * f3, 0.0F);
                hiermesh.chunkSetAngles("Hook1_D0", 0.0F, cvt(f6, 8.5F, 10F, 0.0F, -64.5F), 0.0F);
            }
            float f4;
            if (f > 7.5F) f4 = cvt(f, 7.5F, 8.5F, 0.9F, 1.0F);
            else f4 = cvt(f, 3F, 7.5F, 0.0F, 0.9F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 81F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 84F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 83F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL33_D0", 0.0F, -104F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 40F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -168F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -90F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 81F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 84F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 83F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR33_D0", 0.0F, -104F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 40F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -168F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -90F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 1.5F, 3.7F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 1.5F, 3.7F, 0.0F, 60F), 0.0F);
            hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f, 0.01F, 3.7F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f, 0.01F, 3.7F, 0.0F, 60F), 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        moveGear(hiermesh, f, null);
    }

    protected void moveGear(float f) {
        if (this.prevGear > f) bGearExtending = false;
        else bGearExtending = true;
        this.prevGear = f;
        moveGear(this.hierMesh(), f, this.FM);
        f *= 10F;
        if (bGearExtending) {
            float f1 = cvt(f, 0.0F, 1.0F, 0.0F, 0.7071068F);
            this.cGearPos = 2.0F * f1 * f1;
        } else this.cGearPos = cvt(f, 8.5F, 10F, 0.0F, 1.0F);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, f, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.231F, 0.0F, 0.231F);
        this.hierMesh().chunkSetLocate("GearL7_D0", xyz, ypr);
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.231F, 0.0F, -0.231F);
        this.hierMesh().chunkSetLocate("GearR7_D0", xyz, ypr);
    }

    protected void moveAirBrake(float f) {
        this.moveGear(this.FM.CT.getGear());
        this.moveArrestorHook(this.arrestorPos);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        f *= 18F;
        if (bGearExtending) {
            if (f < 1.5F) {
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 0.0F, 1.5F, 0.0F, 2.6F), 0.0F);
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 0.0F, 1.5F, 0.0F, 2.6F), 0.0F);
            } else if (f < 2.5F) {
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 1.5F, 2.5F, 2.6F, 5.1F), 0.0F);
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 1.5F, 2.5F, 2.6F, 5.1F), 0.0F);
            } else {
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 2.5F, 17.9F, 5.1F, 105F), 0.0F);
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 2.5F, 16F, 5.1F, 105F), 0.0F);
            }
        } else if (f < 9F) {
            if (f < 6.8F) hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 0.01F, 6.8F, 0.0F, 45F), 0.0F);
            else hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 6.8F, 9F, 45F, 50F), 0.0F);
            if (f < 7.5F) hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 0.75F, 7.5F, 0.0F, 45F), 0.0F);
            else hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 7.5F, 9F, 45F, 50F), 0.0F);
        } else if (f < 11F) {
            hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 9F, 11F, 50F, 60F), 0.0F);
            hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 9F, 11F, 50F, 60F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 11F, 15.75F, 60F, 105F), 0.0F);
            hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 11F, 15.75F, 60F, 105F), 0.0F);
        }
    }

    public void moveWingFold(float f) {
        if (this.prevWing > f) bGearExtending = false;
        else bGearExtending = true;
        this.prevWing = f;
//        if (f < 0.001F) {
//            this.setGunPodsOn(true);
//            this.hideWingWeapons(false);
//        } else {
//            this.setGunPodsOn(false);
//            this.FM.CT.WeaponControl[0] = false;
//            this.hideWingWeapons(true);
//        }
        this.moveWingFold(this.hierMesh(), f);
        AircraftTools.updateExternalWeaponHooks(this);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.625F);
        xyz[2] = cvt(f, 0.01F, 0.99F, 0.0F, 0.06845F);
        ypr[2] = cvt(f, 0.01F, 0.99F, 0.0F, 1.0F);
        this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        this.resetYPRmodifier();
        xyz[2] = cvt(f, 0.01F, 0.99F, 0.0F, 0.13F);
        ypr[2] = cvt(f, 0.01F, 0.99F, 0.0F, -8F);
        this.hierMesh().chunkSetLocate("Pilot1_D0", xyz, ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    protected void moveFlap(float f) {
        float f1 = 50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        if (this.bNeedSetup) {
            this.cGear = this.FM.CT.GearControl;
            this.bNeedSetup = false;
        }
        this.cGear = filter(f, this.FM.CT.GearControl, this.cGear, 999.9F, this.FM.CT.dvGear);
        if (this.prevGear2 > this.cGear) bGearExtending2 = false;
        else bGearExtending2 = true;
        this.prevGear2 = this.cGear;
        float f6 = 10F * this.cGear;
        if (bGearExtending2) {
            float f1 = cvt(f6, 0.0F, 1.0F, 0.0F, 0.7071068F);
            this.cGearPos = 2.0F * f1 * f1;
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 41F * this.cGearPos, 0.0F);
            f1 = cvt(f6, 0.0F, 0.25F, 0.0F, 1.0F);
            this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, 140F * f1, 0.0F);
            this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 140F * f1, 0.0F);
        } else {
            this.cGearPos = cvt(f6, 8.5F, 10F, 0.0F, 1.0F);
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 41F * this.cGearPos, 0.0F);
            float f2 = cvt(f6, 8.5F, 8.75F, 0.0F, 1.0F);
            this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, 140F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 140F * f2, 0.0F);
        }
        if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f3 = cvt(this.FM.Gears.arrestorVAngle, -43F, 21.5F, 0.0F, -64.5F);
            if (f3 < -64.5F * this.cGearPos) f3 = -64.5F * this.cGearPos;
            this.arrestorPos = 0.5F * this.arrestorPos + 0.5F * f3;
            this.arrestorVel = 0.0F;
        } else {
            float f4;
            if (this.arrestorVel >= -0.1F) {
                if (Engine.cur.land.isWater(this.FM.Loc.x, this.FM.Loc.y)) f4 = 0.0F;
                else f4 = cvt(this.FM.getSpeedKMH(), 0.0F, 250F, 0.0F, 0.75F);
                f4 = -47.5F * World.Rnd().nextFloat(1.0F - f4, 1.0F + f4 + f4) * this.FM.Gears.arrestorVSink;
            } else f4 = 0.0F;
            if (f4 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f4 < -47.2F) f4 = -47.2F;
            if (f4 < 0.0F) this.arrestorVel += f4;
            else this.arrestorVel += 0.5F;
            this.arrestorPos += this.arrestorVel;
            if (this.arrestorPos < -64.5F * this.cGearPos) {
                this.arrestorPos = -64.5F * this.cGearPos;
                this.arrestorVel = 0.0F;
            }
            if (this.arrestorPos > -64.5F * this.cGearPos * (1.0F - this.FM.CT.getArrestor())) {
                this.arrestorPos = -64.5F * this.cGearPos * (1.0F - this.FM.CT.getArrestor());
                this.arrestorVel = 0.0F;
            }
        }
        this.moveArrestorHook(this.arrestorPos);
        float f5 = Math.min(0.98F, this.FM.CT.getAirBrake());
        f5 = Math.max(f5, this.FM.CT.getGear());
        this.FM.CT.setGear(f5);
        f5 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f5) > 0.01F) {
            this.flapps = f5;
            for (int i = 1; i < 22; i++)
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -23.7F * f5, 0.0F);

            this.hierMesh().chunkSetAngles("Water19_D0", 0.0F, -16F * f5, 0.0F);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("f1")) this.getEnergyPastArmor(World.Rnd().nextFloat(8F, 12F) / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(16F, 36F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                } else if (s.endsWith("p2")) this.getEnergyPastArmor(11D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p3")) this.getEnergyPastArmor(11.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if (s.startsWith("xxcmglammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                int i = 0 + 2 * World.Rnd().nextInt(0, 2);
                this.FM.AS.setJamBullets(0, i);
            }
            if (s.startsWith("xxcmgrammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                int j = 1 + 2 * World.Rnd().nextInt(0, 2);
                this.FM.AS.setJamBullets(0, j);
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int k = s.charAt(10) - 48;
                switch (k) {
                    default:
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 7:
                    case 8:
                        if (World.Rnd().nextFloat() < 0.08F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;

                    case 9:
                        if (World.Rnd().nextFloat() < 0.95F && this.getEnergyPastArmor(1.27F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 280000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 100000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.66F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 1000000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("eqpt")) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                        this.debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int l = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + l + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, l);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                if (s.endsWith("prop") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if (s.startsWith("xxeng1typ") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                return;
            }
            if (s.startsWith("xxhyd")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxmgun0")) {
                int i1 = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + i1 + ") Disabled..");
                    this.FM.AS.setJamBullets(0, i1);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxradio")) {
                this.getEnergyPastArmor(25.532F, shot);
                return;
            }
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(13.8F * World.Rnd().nextFloat(0.99F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** Tail1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxsupc")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j1 = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[j1] == 0) {
                        this.debuggunnery("Fuel Tank (" + j1 + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j1, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j1, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.07F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F) {
                        this.FM.AS.hitTank(shot.initiator, j1, 2);
                        this.debuggunnery("Fuel Tank (" + j1 + "): Hit..");
                    }
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf") || s.startsWith("xblister")) {
            this.hitChunk("CF", shot);
            if (s.startsWith("xcf2")) {
                if (point3d.x > -2.313D && point3d.x < -1.455D && point3d.z > 0.669D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if (point3d.z > 1.125D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            } else if (point3d.x > -1.489D && point3d.x < -1.2D && point3d.z > 0.34D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if (World.Rnd().nextFloat() < 0.054F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if (World.Rnd().nextFloat() < 0.054F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            return;
        }
        if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k1;
            if (s.endsWith("a")) {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else k1 = s.charAt(5) - 49;
            this.hitFlesh(k1, shot, byte0);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    private static final float filter(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float) Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if (f6 < f1) {
            f6 += f4 * f;
            if (f6 > f1) f6 = f1;
        } else if (f6 > f1) {
            f6 -= f4 * f;
            if (f6 < f1) f6 = f1;
        }
        return f6;
    }

    private float          arrestorPos;
    private float          arrestorVel;
    private float          prevGear;
    private float          prevGear2;
    private static boolean bGearExtending  = false;
    private static boolean bGearExtending2 = false;
    private float          prevWing;
    private float          cGearPos;
    private float          cGear;
    private boolean        bNeedSetup;
    private float          flapps;

    static {
        Class class1 = F4U.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
