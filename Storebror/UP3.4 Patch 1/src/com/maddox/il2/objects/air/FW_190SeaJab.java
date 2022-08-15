package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FW_190SeaJab extends FW_190Sea
    implements TypeX4Carrier, TypeBomber, TypeRocketBoost
{

    public FW_190SeaJab()
    {
        bToFire = false;
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        kangle = 0.0F;
        this.booster = new BombStarthilfeSolfuel[2];
        this.boosterEffects = new Eff3DActor[2];
        this.boostState = AircraftState._AS_BOOST_NOBOOST;
        this.boosterFireOutTime = -1L;
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
                this.booster[i] = new BombStarthilfeSolfuel();
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
            if (this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6 && this.FM.getSpeedKMH() > 20F) {
                this.boosterFireOutTime = Time.current() + 20000L;
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
        this.FM.AS.setBoostState(this, this.boostState | AircraftState._AS_BOOST_EXISTS);
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if(aobj != null)
        {
            int i = 0;
            do
            {
                if(i >= aobj.length)
                    break;
                if(((Maneuver)this.FM).hasRockets() || ((Maneuver)this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun))
                {
                    try
                    {
                        booster[0] = new BombStarthilfeSolfuelL();
                        booster[0].pos.setBase(this, findHook("_BoosterH1"), false);
                        booster[0].pos.resetAsBase();
                        booster[0].drawing(true);
                    }
                    catch(Exception exception)
                    {
                        debugprintln("Structure corrupt - can't hang Starthilferakete..");
                    }
                    try
                    {
                        booster[1] = new BombStarthilfeSolfuelR();
                        booster[1].pos.setBase(this, findHook("_BoosterH2"), false);
                        booster[1].pos.resetAsBase();
                        booster[1].drawing(true);
                    }
                    catch(Exception exception)
                    {
                        debugprintln("Structure corrupt - can't hang Starthilferakete..");
                    }
                    break;
                }
                i++;
            } while(true);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
            this.FM.AS.setBoostState(this, AircraftState._AS_BOOST_NOBOOST);
            this.FM.AS.setGliderBoostOff();
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        super.update(f);
        this.boostUpdate();
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f1)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1)
        throws IOException
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 40F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        float f_0_ = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f_0_, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f_0_, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(this.FM.CT.getGear() >= 0.98F)
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void rareAction(float f, boolean bool)
    {
        super.rareAction(f, bool);
        if((!(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && bool && (this.FM instanceof Pilot))
        {
            Pilot pilot = (Pilot)this.FM;
            if(pilot.get_maneuver() == 63 && ((Maneuver) (pilot)).target != null)
            {
                Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
                point3d.sub(this.FM.Loc);
                this.FM.Or.transformInv(point3d);
                if((point3d.x > 4000D && point3d.x < 5500D || point3d.x > 100D && point3d.x < 5000D && World.Rnd().nextFloat() < 0.33F) && Time.current() > tX4Prev + 10000L)
                {
                    bToFire = true;
                    tX4Prev = Time.current();
                }
            }
        }
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -1F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    public float getFullMass()
    {
        return mass;
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot var__WeaponSlots[] = new Aircraft._WeaponSlot[i];
        try
        {
            var__WeaponSlots[0] = null;
            var__WeaponSlots[1] = null;
        }
        catch(Exception exception) { }
        return var__WeaponSlots;
    }

    private float kangle;
    public boolean bToFire;
    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;
    public float mass;
    private float fuelShotRange;
    private float fuelLongRange;
    private float fuelLongerRange;
    private float bombMassNorm;
    private float bombMassLongerRange;
    private float bombMass88;
    protected boolean mgffEnable;
    protected boolean frontbox;
    protected boolean boostersEnable;
    private int bombsc50;
    private int bomb250;
    private int bomb500;
    private int bomb1000;
    private int bomb2000;
    private BombStarthilfeSolfuel      booster[];
    private Eff3DActor                    boosterEffects[];
    private int           boostState;
    protected long        boosterFireOutTime;


    static 
    {
        Class var_class = FW_190SeaJab.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "FW190");
        Property.set(var_class, "meshName", "3DO/Plane/Fw-190F-9T/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(var_class, "yearService", 1944F);
        Property.set(var_class, "yearExpired", 1948F);
        Property.set(var_class, "FlightModel", "FlightModels/Fw-190F-9N.fmd");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitFW_190F8T.class });
        Property.set(var_class, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(var_class, new int[] {
            0, 0, 1, 1, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 9, 9, 9, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 9, 9, 9, 9, 2, 2, 1, 1, 
            1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 9, 9, 1, 1, 1, 1, 9, 9, 
            1, 1, 9, 9, 3, 9, 1, 3, 3, 3, 
            9
        });
        Aircraft.weaponHooksRegister(var_class, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb13", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", 
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", 
            "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalRock25", "_ExternalRock26", "_CANNON01", "_CANNON02", 
            "_CANNON03", "_CANNON04", "_ExternalDev10", "_ExternalDev11", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock31", 
            "_ExternalRock32", "_ExternalRock32", "_ExternalDev12", "_ExternalDev13", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev14", "_ExternalDev15", 
            "_CANNON09", "_CANNON10", "_ExternalDev16", "_ExternalDev17", "_ExternalBomb10", "_ExternalDev18", "_CANNON11", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb05", 
            "_ExternalDev19"
        });
    }
}
