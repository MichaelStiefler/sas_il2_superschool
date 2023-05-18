package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class JU_87 extends Scheme1 implements TypeDiveBomber {

    public JU_87() {
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.fDiveRecoveryAlt = 750F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("AroneRodL_D0", 0.0F, -26.5F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneRodR_D0", 0.0F, -27.5F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("AroneRodL_D0", 0.0F, -27.5F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneRodR_D0", 0.0F, -26.5F * f, 0.0F);
        }
    }

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapRod01_D0", 0.0F, 0.75F * f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapRod02_D0", 0.0F, 0.95F * f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapRod03_D0", 0.0F, 0.95F * f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapRod04_D0", 0.0F, 0.75F * f1, 0.0F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        xyz[2] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.35F, 0.0F, 0.3F);
        this.hierMesh().chunkSetLocate("GearL33_D0", xyz, ypr);
        xyz[2] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.35F, 0.0F, 0.3F);
        this.hierMesh().chunkSetLocate("GearR33_D0", xyz, ypr);
    }

    public void update(float f) {
        if (this == World.getPlayerAircraft() && this.FM instanceof RealFlightModel) if (((RealFlightModel) this.FM).isRealMode()) switch (this.diveMechStage) {
            case 0:
                if (this.bNDives && this.FM.CT.AirBrakeControl == 1.0F && this.FM.Loc.z > this.fDiveRecoveryAlt) {
                    this.diveMechStage++;
                    this.bNDives = false;
                } else this.bNDives = this.FM.CT.AirBrakeControl != 1.0F;
                break;

            case 1:
                this.FM.CT.setTrimElevatorControl(-0.65F);
                this.FM.CT.trimElevator = -0.65F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.CT.saveWeaponControl[3] || this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0
                        || this.FM.Loc.z < this.fDiveRecoveryAlt && !(this instanceof JU_87D5)) {
                            if (this.FM.CT.AirBrakeControl == 0.0F) this.diveMechStage++;
                            if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) this.diveMechStage++;
                            if (this.FM.Loc.z < this.fDiveRecoveryAlt) {
                                this.diveMechStage++;
                                if (World.cur().diffCur.Limited_Ammo) this.bDropsBombs = true;
                            }
                        }
                break;

            case 2:
                if (this.FM.isTick(41, 0)) {
                    this.FM.CT.setTrimElevatorControl(0.85F);
                    this.FM.CT.trimElevator = 0.85F;
                }
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
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            default:
                break;

            case 1:
                if (this.FM.turret.length > 0) this.FM.turret[0].setHealth(f);
                break;
        }
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
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -25F) {
            af[0] = -25F;
            flag = false;
        } else if (af[0] > 25F) {
            af[0] = 25F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (af[1] < -10F) {
            af[1] = -10F;
            flag = false;
        }
        if (af[1] > 45F) {
            af[1] = 45F;
            flag = false;
        }
        if (!flag) return false;
        float f1 = af[1];
        if (f < 1.2F && f1 < 13.3F) return false;
        return f1 >= -3.1F || f1 <= -4.6F;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.FM.AS.hitTank(this, 0, 6);
                return super.cutFM(34, j, actor);

            case 36:
                this.FM.AS.hitTank(this, 1, 6);
                return super.cutFM(37, j, actor);

            case 34:
                this.FM.cut(9, j, actor);
                break;

            case 37:
                this.FM.cut(10, j, actor);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null) {
            if (explosion.chunkName.startsWith("CF")) {
                if (World.Rnd().nextFloat() < 0.005F) this.FM.AS.setControlsDamage(explosion.initiator, 0);
                if (World.Rnd().nextFloat() < 0.005F) this.FM.AS.setControlsDamage(explosion.initiator, 1);
                if (World.Rnd().nextFloat() < 0.005F) this.FM.AS.setControlsDamage(explosion.initiator, 2);
            }
            if (explosion.chunkName.startsWith("Engine") && explosion.power > 0.011F) this.FM.AS.hitOil(explosion.initiator, 0);
            if (explosion.chunkName.startsWith("Tail")) {
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.setControlsDamage(explosion.initiator, 1);
                if (World.Rnd().nextFloat() < 0.11F) this.FM.AS.setControlsDamage(explosion.initiator, 2);
            }
        }
        super.msgExplosion(explosion);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLIn") && this.getEnergyPastArmor(1.0F, shot) > 0.0F && shot.powerType == 3 && World.Rnd().nextFloat() < 0.61F) this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, 2));
        if (shot.chunkName.startsWith("WingRIn") && this.getEnergyPastArmor(1.0F, shot) > 0.0F && shot.powerType == 3 && World.Rnd().nextFloat() < 0.61F) this.FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, 2));
        if (shot.chunkName.startsWith("Tail")) {
            if (World.Rnd().nextFloat() < 0.005F) this.FM.AS.setControlsDamage(shot.initiator, 1);
            if (World.Rnd().nextFloat() < 0.005F) this.FM.AS.setControlsDamage(shot.initiator, 2);
        }
        if (shot.chunkName.startsWith("Wing") && this.getEnergyPastArmor(4.35F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) this.FM.AS.setControlsDamage(shot.initiator, 0);
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat() < 9.999F * shot.mass) this.FM.AS.hitEngine(shot.initiator, 0, 1);
            if (shot.chunkName.endsWith("_D2") && World.Rnd().nextFloat() < 0.75F) return;
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            if (Pd.z > 0.76D && v1.x < 0.0D) {
                this.killPilot(shot.initiator, 0);
                if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
                return;
            }
            if (shot.power * Math.abs(v1.x) > 10370D) this.FM.AS.hitPilot(shot.initiator, 1, (int) (shot.mass * 1000F * 0.5F));
            shot.chunkName = "CF_D0";
        }
        if (shot.chunkName.startsWith("Pilot2")) {
            if (Pd.z > 0.76D && v1.x > 0.0D) {
                this.killPilot(shot.initiator, 1);
                if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
                return;
            }
            if (shot.power * Math.abs(v1.x) > 10370D) this.FM.AS.hitPilot(shot.initiator, 1, (int) (shot.mass * 1000F * 0.5F));
            shot.chunkName = "CF_D0";
        }
        super.msgShot(shot);
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
        this.fDiveRecoveryAlt += 25F;
        if (this.fDiveRecoveryAlt > 6000F) this.fDiveRecoveryAlt = 6000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjAltitudeMinus() {
        this.fDiveRecoveryAlt -= 25F;
        if (this.fDiveRecoveryAlt < 0.0F) this.fDiveRecoveryAlt = 0.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
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

    public int            diveMechStage;
    public boolean        bNDives;
    private boolean       bDropsBombs;
    public static boolean bChangedPit = false;
    public float          fDiveRecoveryAlt;

    static {
        Class class1 = JU_87.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
