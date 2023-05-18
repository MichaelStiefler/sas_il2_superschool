package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.BombAB1000;
import com.maddox.il2.objects.weapons.BombSC1000;
import com.maddox.il2.objects.weapons.BombSC1800;
import com.maddox.il2.objects.weapons.BombSC2000;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class JU_88A4Late extends JU_88Axx implements TypeBomber, TypeDiveBomber, TypeScout, TypeRocketBoost {

    public JU_88A4Late() {
        this.booster = new BombStarthilfe109500[2];
        this.boosterEffects = new Eff3DActor[2];
        this.boostState = AircraftState._AS_BOOST_NOBOOST;
        this.boosterFireOutTime = -1L;
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.needsToOpenBombays = false;
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.fDiveRecoveryAlt = 850F;
        this.fDiveVelocity = 150F;
        this.fDiveAngle = 70F;
        this.iRust = 1;
        this.blisterRemoved = false;
        this.topBlisterRemoved = false;
    }
    
    public void setBoostState(int state) {
        if (this.boostState == state) return; // Nothing to do here, we are in the requested state already.
        if (((state ^ this.boostState) & AircraftState._AS_BOOST_EXISTS) != 0) { // The existence of boosters has changed
            if ((state & AircraftState._AS_BOOST_EXISTS) != 0) { // Boosters exist now
                this.doAttachBoosters();
            } else { // Boosters don't exist anymore
                this.doCutBoosters();
            }
        }
        if (((state ^ this.boostState) & AircraftState._AS_BOOST_ACTIVE) != 0) { // The boosters activity state changed
            if ((state & AircraftState._AS_BOOST_ACTIVE) != 0) { // Boosters are active now
                this.doFireBoosters();
            } else { // Boosters aren't active
                this.doShutoffBoosters();
            }
        }
        this.boostState = state;
    }

    public int getBoostState() {
        return this.boostState;
    }

    public void doAttachBoosters() {
        for (int i = 0; i < 2; i++) {
            try {
                this.booster[i] = new BombStarthilfe109500();
                this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
                this.booster[i].pos.resetAsBase();
                this.booster[i].drawing(true);
            } catch (Exception exception) {
                this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
            }
        }
    }
    
    public void doCutBoosters() {
      for (int i = 0; i < 2; i++) {
          if (this.booster[i] != null) {
              this.booster[i].start();
              this.booster[i] = null;
          }
      }
      this.stopBoosterSound();
    }

    public void doFireBoosters() {
        for (int i=0; i<2; i++) {
            this.boosterEffects[i] = Eff3DActor.New(this, this.findHook("_Booster" + (i+1)), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        }
        this.startBoosterSound();
    }

    public void doShutoffBoosters() {
        for (int i=0; i<2; i++) {
            Eff3DActor.finish(this.boosterEffects[i]); // No null checks etc. required here, it's done internally already.
        }
        this.stopBoosterSound();
    }

    public void startBoosterSound() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].startSound();
            }
        }
    }

    public void stopBoosterSound() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].stopSound();
            }
        }
    }
    
    private void boostUpdate() {
        if (!(this.FM instanceof Pilot)) return;
        if ((this.boostState & AircraftState._AS_BOOST_EXISTS) == 0) return;
            // TODO: Changed Booster cutoff reasons from absolute altitude to altitude above ground
            if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 300F && this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
                this.FM.AS.setBoostState(this, AircraftState._AS_BOOST_NOBOOST);
                this.FM.AS.setGliderBoostOff();
            }
            if (this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6 && this.FM.getSpeedKMH() > 20F) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.FM.AS.setBoostState(this, this.boostState | AircraftState._AS_BOOST_ACTIVE);
                this.FM.AS.setGliderBoostOn();
            }
            if (this.boosterFireOutTime > 0L) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 20000D;
                } else { // Stop sound
                    this.FM.AS.setBoostState(this, this.boostState & ~AircraftState._AS_BOOST_ACTIVE);
                }
                if (Time.current() > this.boosterFireOutTime + 10000L) { // cut boosters 10 seconds after burnout regardless altitude if not done so before.
                    this.FM.AS.setBoostState(this, AircraftState._AS_BOOST_NOBOOST);
                    this.FM.AS.setGliderBoostOff();
                }
            }
    }
    
    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (!this.isNetMaster()) return; // FIXME: Maybe FM.AS.isMaster() works better? Idea is to deal with "setOnGround" in single player missions only, or in Dogfight missions on server side only.
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) break;
                if (aobj[i] instanceof BombSC1000 || aobj[i] instanceof BombAB1000 || aobj[i] instanceof BombSC1800 || aobj[i] instanceof BombSC2000) {
                    this.FM.AS.setBoostState(this, this.boostState | AircraftState._AS_BOOST_EXISTS);
                    break;
                }
                i++;
            } while (true);
        }
    }
    
    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        float f = 0.0F;
        float f1 = 0.0F;
        this.needsToOpenBombays = false;
        if (this.thisWeaponsName.endsWith("RustA")) {
            this.iRust = 1;
            if (this.thisWeaponsName.endsWith("2xTank900L_RustA")) f1 += 45F;
            if (this.thisWeaponsName.startsWith("28x")) this.needsToOpenBombays = true;
        } else if (this.thisWeaponsName.endsWith("RustB")) {
            this.iRust = 2;
            f1 += 20F;
            f += 900F;
            if (this.thisWeaponsName.endsWith("1xTank900L_RustB")) f1 += 45F;
            else if (this.thisWeaponsName.endsWith("2xTank900L_RustB")) f1 += 110F;
            if (this.thisWeaponsName.startsWith("10x")) this.needsToOpenBombays = true;
        } else if (this.thisWeaponsName.endsWith("RustC")) {
            this.iRust = 3;
            this.FM.CT.bHasBayDoors = false;
            f1 += 170F;
            if (this.thisWeaponsName.startsWith("1xSC1800")) {
                f += 1300F;
                f1 += 45F;
            } else if (this.thisWeaponsName.endsWith("1xTank900L_RustC")) {
                f1 += 110F;
                f += 1400F;
            } else {
                f1 += 45F;
                f += 1400F;
            }
        }
        float f2 = this.FM.M.fuel / this.FM.M.maxFuel;
        this.FM.M.fuel += f2 * f;
        this.FM.M.maxFuel += f;
        this.FM.M.massEmpty += f1;
    }

    public void doWreck(String s) {
        if (this.hierMesh().chunkFindCheck(s) != -1) {
            this.hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void blisterRemoved(int i) {
        if (i < 4) {
            if (!this.topBlisterRemoved) {
                this.doWreck("BlisterTop_D0");
                this.hierMesh().chunkVisible("Turret2B_D0", false);
                this.hierMesh().chunkVisible("Turret3B_D0", false);
                this.topBlisterRemoved = true;
            }
        } else if (!this.blisterRemoved && i == 4) {
            this.doWreck("BlisterDown_D0");
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.blisterRemoved = true;
        }
    }

    protected void moveBayDoor(float f) {
        if (!this.needsToOpenBombays && !this.FM.isPlayers()) return;
        if (this.iRust != 3) {
            this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 87F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -86F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 86F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -87F * f, 0.0F);
        }
        if (this.iRust == 1) {
            this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay7_D0", 0.0F, 85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -85F * f, 0.0F);
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 4; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f) {
        this.updateJU87D5(f);
        this.updateJU87(f);
        if (Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 70F && this.FM.CT.getFlap() > 0.01D && this.FM.CT.FlapsControl != 0.0F) {
            this.FM.CT.FlapsControl = 0.0F;
            if (this == World.getPlayerAircraft()) HUD.log("FlapsRaised");
        }
        super.update(f);
        this.boostUpdate();
    }

    public void updateJU87(float f) {
        if (this == World.getPlayerAircraft() && this.FM instanceof RealFlightModel) if (((RealFlightModel) this.FM).isRealMode()) switch (this.diveMechStage) {
            case 0:
                if (this.bNDives && this.FM.CT.AirBrakeControl == 1.0F && this.FM.Loc.z > this.fDiveRecoveryAlt) {
                    this.diveMechStage++;
                    this.bNDives = false;
                } else this.bNDives = this.FM.CT.AirBrakeControl != 1.0F;
                break;

            case 1:
                this.FM.CT.setTrimElevatorControl(-0.25F);
                this.FM.CT.trimElevator = -0.25F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.CT.saveWeaponControl[3] || this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) {
                    if (this.FM.CT.AirBrakeControl == 0.0F) this.diveMechStage++;
                    if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) this.diveMechStage++;
                }
                break;

            case 2:
                this.FM.CT.setTrimElevatorControl(0.45F);
                this.FM.CT.trimElevator = 0.45F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.Or.getTangage() > 0.0F) this.diveMechStage++;
                break;

            case 3:
                this.FM.CT.setTrimElevatorControl(0.0F);
                this.FM.CT.trimElevator = 0.0F;
                this.diveMechStage = 0;
                break;
        }
        else {
            this.FM.CT.setTrimElevatorControl(0.0F);
            this.FM.CT.trimElevator = 0.0F;
        }
        if (this.bDropsBombs && this.FM.isTick(3, 0) && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets())
            this.FM.CT.WeaponControl[3] = true;
    }

    public void updateJU87D5(float f) {
        this.fDiveAngle = -this.FM.Or.getTangage();
        if (this.fDiveAngle > 89F) this.fDiveAngle = 89F;
        if (this.fDiveAngle < 10F) this.fDiveAngle = 10F;
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -90F * f, 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                break;
        }
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = !this.bSightAutomation;
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) this.fSightCurForwardAngle = 85F;
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) this.typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) this.fSightCurForwardAngle = 0.0F;
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) this.typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3F) this.fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.05F;
        if (this.fSightCurSideslip < -3F) this.fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 850F;
        this.typeDiveBomberAdjAltitudeReset();
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 10000F) this.fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        this.typeDiveBomberAdjAltitudePlus();
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 500F) this.fSightCurAltitude = 500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        this.typeDiveBomberAdjAltitudeMinus();
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 150F;
        this.typeDiveBomberAdjVelocityReset();
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 700F) this.fSightCurSpeed = 700F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
        this.typeDiveBomberAdjVelocityPlus();
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 150F) this.fSightCurSpeed = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
        this.typeDiveBomberAdjVelocityMinus();
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) this.fSightCurReadyness = 0.0F;
        }
        if (this.fSightCurReadyness < 1.0F) this.fSightCurReadyness += 0.0333333F * f;
        else if (this.bSightAutomation) {
            this.fSightCurDistance -= this.fSightCurSpeed / 3.6F * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
            if (this.fSightCurDistance < this.fSightCurSpeed / 3.6F * Math.sqrt(this.fSightCurAltitude * (2F / 9.81F))) this.bSightBombDump = true;
            if (this.bSightBombDump) if (this.FM.isTick(3, 0)) {
                if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
                    this.FM.CT.WeaponControl[3] = true;
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                }
            } else this.FM.CT.WeaponControl[3] = false;
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
        this.fSightCurAltitude = this.fDiveRecoveryAlt = netmsginput.readFloat();
        this.fSightCurSpeed = this.fDiveVelocity = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
        this.fDiveRecoveryAlt += 10F;
        if (this.fDiveRecoveryAlt > 10000F) this.fDiveRecoveryAlt = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjAltitudeMinus() {
        this.fDiveRecoveryAlt -= 10F;
        if (this.fDiveRecoveryAlt < 500F) this.fDiveRecoveryAlt = 500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
        this.fDiveVelocity += 10F;
        if (this.fDiveVelocity > 700F) this.fDiveVelocity = 700F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjVelocityMinus() {
        this.fDiveVelocity -= 10F;
        if (this.fDiveVelocity < 150F) this.fDiveVelocity = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeBomberAdjOpbLeft() {
    }

    public void typeBomberAdjOpbRight() {
    }

    public void typeBomberAdjOpbForward() {
    }

    public void typeBomberAdjOpbBack() {
    }

    public boolean typeBomberAdjOpbTimer() {
        return false;
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
    }

    public static boolean bChangedPit = false;
    private int           iRust;
    public boolean        blisterRemoved;
    public boolean        topBlisterRemoved;
    public int            diveMechStage;
    public boolean        bNDives;
    private boolean       bDropsBombs;
    private boolean       needsToOpenBombays;
    private boolean       bSightAutomation;
    private boolean       bSightBombDump;
    private float         fSightCurDistance;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;
    public float          fDiveRecoveryAlt;
    public float          fDiveVelocity;
    public float          fDiveAngle;
    private BombStarthilfe109500          booster[];
    private Eff3DActor                    boosterEffects[];
    protected long        boosterFireOutTime;
    private int           boostState;
//    protected boolean     bHasBoosters;
//    // TODO: Added by SAS~Storebror to avoid multiple "setOnGround()" method processes for the same aircraft
//    private boolean       setOnGroundDone;

    static {
        Class class1 = JU_88A4Late.class;
        new NetAircraft.SPAWN(class1);
//        // TODO: By SAS~Storebror: Rocket Booster Net Replication +++
//        Property.set(class1, "BoosterHook", "_BoosterH");
//        Property.set(class1, "BoosterEffectHook", "_Booster");
//        Property.set(class1, "BoosterEffect", "_Booster");
//        // TODO: By SAS~Storebror: Rocket Booster Net Replication ---
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88A-4Late/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-4Late.fmd");
        Property.set(class1, "cockpitClass",
                new Class[] { CockpitJU_88A4Late.class, CockpitJU_88A4Late_Bombardier.class, CockpitJU_88A4Late_NGunner.class, CockpitJU_88A4Late_LGunner.class, CockpitJU_88A4Late_RGunner.class, CockpitJU_88A4Late_BGunner.class });
        Property.set(class1, "LOSElevation", 1.0976F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn08a", "_BombSpawn09",
                        "_BombSpawn09a", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn15a", "_BombSpawn16", "_BombSpawn16a", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20",
                        "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_ExternalBomb04", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb03", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03",
                        "_ExternalDev04" });
    }
}
