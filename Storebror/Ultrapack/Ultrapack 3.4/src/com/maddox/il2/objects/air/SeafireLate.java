// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
// www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/03/11

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class SeafireLate extends SEAFIRE3 implements TypeFighterAceMaker, TypeBNZFighter {
    public SeafireLate() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.flapps = 0.0F;
        this.arrestor = 0.0F;
        this.bWingsFolded = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            SEAFIRE3.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            SEAFIRE3.bChangedPit = true;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    protected void moveGear(float f) {
        SeafireLate.moveGear(this.hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        SeafireLate.moveGear(this.hierMesh(), f, f1, f2);
    }

    protected void moveFlap(float f) {
        if (f > this.FM.CT.FlapsControl) {
            f -= 0.005F;
        } else {
            f += 0.005F;
        }
        f = this.clamp01(f);
        this.FM.CT.forceFlaps(f);

        float fFlapAngle = -85.0F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, fFlapAngle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, fFlapAngle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, fFlapAngle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, fFlapAngle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap05_D0", 0.0F, fFlapAngle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap06_D0", 0.0F, fFlapAngle, 0.0F);
    }

    private float clamp01(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        if (!this.FM.CT.bHasWingControl) {
            return;
        }
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -112F * f, 0.0F);
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, -112F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -112F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -112F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        if (!this.FM.CT.bHasWingControl) {
            return;
        }
        this.moveWingFold(this.hierMesh(), f);
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(true);
        }
        this.bWingsFolded = (f > 0.999F);
    }

    public void moveArrestorHook(float f) {
        if (!this.FM.CT.bHasArrestorControl) {
            return;
        }
        this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -35F * f, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = 0.3385F * f;
        this.hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
        this.arrestor = f;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.CT.bHasArrestorControl && (i == 19)) {
            this.FM.CT.bHasArrestorControl = false;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        float fRadiatorPos = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - fRadiatorPos) > 0.01F) {
            this.flapps = fRadiatorPos;
            this.hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * fRadiatorPos, 0.0F);
            this.hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * fRadiatorPos, 0.0F);
        }

        if (this.FM.CT.bHasArrestorControl) {

            float fArrestorPos = this.FM.CT.getArrestor();
            float fArrestorPosPow = 81F * (float) Math.pow(fArrestorPos, 7);
            if (fArrestorPos > 0.01F) {
                if (this.FM.Gears.arrestorVAngle != 0.0F) {
                    this.arrestor = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -fArrestorPosPow, fArrestorPosPow, -fArrestorPosPow, fArrestorPosPow);
                    this.moveArrestorHook(fArrestorPos);
                } else {
                    float f3 = 58F * this.FM.Gears.arrestorVSink;
                    if ((f3 > 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                        Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                    }
                    this.arrestor += f3;
                    if (this.arrestor > fArrestorPosPow) {
                        this.arrestor = fArrestorPosPow;
                    }
                    if (this.arrestor < -fArrestorPosPow) {
                        this.arrestor = -fArrestorPosPow;
                    }
                    this.moveArrestorHook(fArrestorPos);
                }
            }

        }

        if (this.FM.CT.bHasWingControl) {
            if (this.bWingsFolded) {
                this.hierMesh().chunkVisible("WingStrutR", true);
                this.hierMesh().chunkVisible("WingStrutL", true);
            } else { //if (FM.Gears.onGround()) {
                this.hierMesh().chunkVisible("WingStrutR", false);
                this.hierMesh().chunkVisible("WingStrutL", false);
            }
        }

    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10F;
        if (this.k14Distance > 800F) {
            this.k14Distance = 800F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10F;
        if (this.k14Distance < 200F) {
            this.k14Distance = 200F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    public int      k14Mode;
    public int      k14WingspanType;
    public float    k14Distance;
    private float   flapps;
    protected float arrestor;
    private boolean bWingsFolded;
}
