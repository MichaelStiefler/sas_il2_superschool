package com.maddox.il2.objects.air;

import java.io.IOException;
import java.security.SecureRandom;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.HUD;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.weapons.MGunAdjustableMiniGun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

/**
 * This class was modified by SAS~Skylla in the course of the AC-47 rework.
 *
 * @see MGunAdjustableMiniGun, MGunMiniGun3000, MGunMiniGun6000
 *
 *      Modifications: - Imported new 3D by SAS~GJE52 from here: http://www.sas1946.com/main/index.php/topic,32003.0.html - Reduced Recoil on the miniguns - Added the option to select the rpm-values on the miniguns: + MGunMiniGun3000 can be set to 2000rpm
 *      / 4000rpm + MGunMiniGun6000 can be set to 3000rpm / 6000rpm - Reworked the Minigun belt: After 2 belts normal .50cal there is one 0.50cal APIT ammo - Added the opion to select either 1, 2 or all three guns when firing - Moved to flare drop
 *      position to the open cargo door. - Added 4.12 compatible gear code & randomized the gear animation - Added net replication for gun rpm and active guns
 *
 **/

public class AC_47 extends Scheme2 implements TypeTransport, TypeBomber {

    private float[]        rndgear     = { 0.0F, 0.0F, 0.0F };
    private static float[] rndgearnull = { 0.0F, 0.0F, 0.0F };

    private int            gunsActive  = 0;
    private int            gunsRPM     = 0;

    public AC_47() {
        SecureRandom secRandom = new SecureRandom();
        secRandom.setSeed(System.currentTimeMillis());
        RangeRandom rr = new RangeRandom(secRandom.nextLong());
        for (int i = 0; i < this.rndgear.length; i++)
            this.rndgear[i] = rr.nextFloat(0.0F, 0.25F);
    }

    // Gear Code:
    // ---------------------------------------------------------------------------

    public static void moveGear(HierMesh h, float l, float r, float t, float[] rnd) {
        h.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(l, 0.01F + rnd[0], 0.74F + rnd[0], 0.0F, -45.0F), 0.0F);
        h.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(r, 0.01F + rnd[1], 0.74F + rnd[1], 0.0F, -45.0F), 0.0F);
        h.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(l, 0.01F + rnd[0], 0.74F + rnd[0], 0.0F, 20.0F), 0.0F);
        h.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(r, 0.01F + rnd[1], 0.74F + rnd[1], 0.0F, 20.0F), 0.0F);
        h.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(l, 0.01F + rnd[0], 0.74F + rnd[0], 0.0F, -120.0F), 0.0F);
        h.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(r, 0.01F + rnd[1], 0.74F + rnd[1], 0.0F, -120.0F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, rndgearnull);
    }

    public static void moveGear(final HierMesh h, final float n, float[] rnd) {
        moveGear(h, n, n, n, rnd);
    }

    public static void moveGear(final HierMesh h, final float n) {
        moveGear(h, n, rndgearnull);
    }

    protected void moveGear(float l, float r, float t) {
        moveGear(this.hierMesh(), l, r, t, this.rndgear);
    }

    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n, this.rndgear);
    }

    // --------------------------------------------------------------------------------------

    public void msgShot(final Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLOut") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 6.0) this.FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingROut") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 6.0) this.FM.AS.hitTank(shot.initiator, 3, 1);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 1.94) this.FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 1.94) this.FM.AS.hitTank(shot.initiator, 2, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f) this.FM.AS.hitEngine(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("Nose") && Aircraft.Pd.x > 4.9 && Aircraft.Pd.z > -0.09 && World.Rnd().nextFloat() < 0.1f) if (Aircraft.Pd.y > 0.0) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
        } else this.killPilot(shot.initiator, 1);
        if (this.FM.AS.astateEngineStates[0] > 2 && this.FM.AS.astateEngineStates[1] > 2 && World.Rnd().nextInt(0, 99) < 33) this.FM.setCapableOfBMP(false, shot.initiator);
        super.msgShot(shot);
    }

    public void doMurderPilot(final int n) {
        switch (n) {
            case 0: {
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
            }
            case 1: {
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
            }
        }
    }

    public void rareAction(final float f, final boolean bool) {
        super.rareAction(f, bool);
        for (int i = 1; i < 3; ++i)
            if (this.FM.getAltitude() < 3000.0f) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
        // Net Replication:
        // ---------------------------------------------------------------------
        this.replicateGunStateToNet();
        // --------------------------------------------------------------------------------------
    }

    protected boolean cutFM(final int n, final int n2, final Actor actor) {
        switch (n) {
            case 13: {
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                break;
            }
            case 35: {
                if (World.Rnd().nextFloat() < 0.25f) {
                    this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
                    break;
                }
                break;
            }
            case 38: {
                if (World.Rnd().nextFloat() < 0.25f) {
                    this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
                    break;
                }
                break;
            }
        }
        return super.cutFM(n, n2, actor);
    }

    static {
        Class clazz = AC_47.class;
        new SPAWN(clazz);
        Property.set(clazz, "iconFar_shortClassName", "Douglas");
        Property.set(clazz, "meshNameDemo", "3DO/Plane/AC-47/hier.him");
        Property.set(clazz, "meshName", "3DO/Plane/AC-47/hier.him");
        Property.set(clazz, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(clazz, "noseart", 1);
        Property.set(clazz, "yearService", 1939.0f);
        Property.set(clazz, "yearExpired", 2999.9f);
        Property.set(clazz, "FlightModel", "FlightModels/C-47B.fmd");
        Property.set(clazz, "cockpitClass", new Class[] { CockpitAC47.class });
        Property.set(clazz, "LOSElevation", 0.725f);
        Aircraft.weaponTriggersRegister(clazz, new int[] { 3, 0, 1, 1 });
        Aircraft.weaponHooksRegister(clazz, new String[] { "_BombSpawn01", "_MGUN01", "_MGUN02", "_MGUN03" });
    }

    // MinigunRPM
    // ---------------------------------------------------------------------------

    private void setGunsRPM(int dir) {
        float f = -1.0F;
        for (int i = 0; i < 3; i++) {
            BulletEmitter e = this.getBulletEmitterByHookName("_MGUN0" + (i + 1));
            if (e instanceof MGunAdjustableMiniGun) {
                MGunAdjustableMiniGun m = (MGunAdjustableMiniGun) e;
                switch (dir) {
                    case -1:
                        f = m.decRPM();
                        break;
                    case 0:
                        m.resRPM();
                        break;
                    case 1:
                        f = m.incRPM();
                        break;
                    default:
                        return;
                }
            } else return;
        }
        if (f > 0) {
            if (this == World.getPlayerAircraft()) HUD.log("Current Rate of Fire: " + (int) f + "rpm");
            this.gunsRPM = (int) f;
        }
    }

    public void typeBomberAdjSpeedMinus() {
        this.setGunsRPM(-1);
        this.replicateGunStateToNet();
    }

    public void typeBomberAdjSpeedPlus() {
        this.setGunsRPM(1);
        this.replicateGunStateToNet();
    }

    public void typeBomberAdjSpeedReset() {
        this.setGunsRPM(0);
        this.replicateGunStateToNet();
    }

    // Selectable Miniguns:
    // -----------------------------------------------------------------

    private void setGunsActive(int dir) {
        BulletEmitter[] e = new BulletEmitter[3];
        int active = 0;
        for (int i = 0; i < e.length; i++) {
            e[i] = this.getBulletEmitterByHookName("_MGUN0" + (i + 1));
            if (e[i] != null) {
                if (!e[i].isPause()) active++;
            } else return;
        }
        if (active + dir > 0 && active + dir < 4) active += dir;
        switch (active) {
            case 1:
                e[0].setPause(false);
                e[1].setPause(true);
                e[2].setPause(true);
                break;
            case 2:
                e[0].setPause(false);
                e[1].setPause(false);
                e[2].setPause(true);
                break;
            default:
                e[0].setPause(false);
                e[1].setPause(false);
                e[2].setPause(false);
        }
        if (this == World.getPlayerAircraft()) HUD.log("Active Guns: " + active);
        this.gunsActive = active;
    }

    public void typeBomberAdjAltitudeMinus() {
        this.setGunsActive(-1);
        this.replicateGunStateToNet();
    }

    public void typeBomberAdjAltitudePlus() {
        this.setGunsActive(+1);
        this.replicateGunStateToNet();
    }

    // Net Replication:
    // ---------------------------------------------------------------------

    public void update(float f) {
        super.update(f);
        this.replicateGunStateFromNet();
    }

    private void replicateGunStateFromNet() {
        if (this == World.getPlayerAircraft() && !NetMissionTrack.isPlaying()) return;
        int fromNet = this.FM.AS.torpedoGyroAngle;
        int tmpRPM = fromNet / 10 * 10;
        fromNet -= tmpRPM;
        int tmpActive = fromNet;

        for (int i = 0; this.gunsRPM != tmpRPM && i < 100; i++)
            if (this.gunsRPM < tmpRPM) this.setGunsRPM(+1);
            else if (this.gunsRPM > tmpRPM) this.setGunsRPM(-1);
        for (int i = 0; this.gunsActive != tmpActive && i < 4; i++)
            if (this.gunsActive < tmpActive) this.setGunsActive(+1);
            else if (this.gunsActive > tmpActive) this.setGunsActive(-1);
    }

    private void replicateGunStateToNet() {
        if (!this.FM.AS.isMaster() || NetMissionTrack.isPlaying()) return;
        int toSend = 0;
        if (this.gunsActive < 4 && this.gunsActive > 1) toSend += this.gunsActive;
        if (this.gunsRPM > 10 && this.gunsRPM < 60000) toSend += this.gunsRPM;
        int byte1 = (toSend & 0xFF00) >> 8;
        int byte2 = toSend & 0xFF;
        this.FM.AS.netToMirrors(AircraftState._AS_GYROANGLE, byte1, byte2, null);
    }

    /*
     * alternative coarse Version only using 1 byte. private void replicateGunStateToNet() { if (!this.FM.AS.isMaster() || NetMissionTrack.isPlaying()) { return; } byte toSend = (byte)(gunsActive & 0x03); toSend |= ((gunsRPM / 1000) & 0x0F) << 2;
     * this.FM.AS.netToMirrors(AircraftState._AS_GYROANGLE, toSend, 0, null); }
     *
     * private void replicateGunStateFromNet() { if(this == World.getPlayerAircraft() && !NetMissionTrack.isPlaying()) { return; } byte data = (byte) this.FM.AS.torpedoGyroAngle; int tmpActive = (int)(data & 0x03); int tmpRPM = (((int)data >> 2) &
     * 0x0F)*1000;
     *
     *
     * for(int i = 0; (this.gunsRPM != tmpRPM) && i < 100; i++) { if(gunsRPM < tmpRPM) { setGunsRPM(+1); } else if(gunsRPM > tmpRPM){ setGunsRPM(-1); } } for(int i = 0; (this.gunsActive != tmpActive) && i < 4; i++) { if(gunsActive < tmpActive) {
     * setGunsActive(+1); } else if(gunsActive > tmpActive) { setGunsActive(-1); } } }
     */

    // --------------------------------------------------------------------------------------

    public void typeBomberReplicateToNet(final NetMsgGuaranted msg) throws IOException {

    }

    public void typeBomberReplicateFromNet(final NetMsgInput msg) throws IOException {

    }

    public void typeBomberAdjAltitudeReset() {

    }

    public void typeBomberAdjDistanceMinus() {

    }

    public void typeBomberAdjDistancePlus() {

    }

    public void typeBomberAdjDistanceReset() {

    }

    public void typeBomberAdjSideslipMinus() {

    }

    public void typeBomberAdjSideslipPlus() {

    }

    public void typeBomberAdjSideslipReset() {

    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f) {

    }

}
