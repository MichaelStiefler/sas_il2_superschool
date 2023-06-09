/* Here because of obfuscation reasons */
package com.maddox.il2.objects.air;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Fuze;
import com.maddox.il2.objects.weapons.Fuze_EL_AZ;
import com.maddox.rts.LDRres;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;

public abstract class AircraftLH extends Aircraft {
    public static int     hudLogCompassId     = HUD.makeIdLog();
    public static boolean printCompassHeading = false;
    public boolean        bWantBeaconKeys     = false;

    public void beaconPlus() {
        if (this.bWantBeaconKeys && (Main.cur().mission.getBeacons(this.getArmy()) == null || Main.cur().mission.getBeacons(this.getArmy()).size() != 0)) this.FM.AS.beaconPlus();
    }

    public void beaconMinus() {
        if (this.bWantBeaconKeys && (Main.cur().mission.getBeacons(this.getArmy()) == null || Main.cur().mission.getBeacons(this.getArmy()).size() != 0)) this.FM.AS.beaconMinus();
    }

    public void beaconSet(int i) {
        if (this.bWantBeaconKeys && (Main.cur().mission.getBeacons(this.getArmy()) == null || Main.cur().mission.getBeacons(this.getArmy()).size() != 0)) this.FM.AS.setBeacon(i);
    }

    public void auxPlus(int i) {
        switch (i) {
            case 1:
                this.headingBug = this.headingBug + 1.0F;
                if (this.headingBug >= 360.0F) this.headingBug = 0.0F;
                // TODO: Storebror - cross version compatibility
//				if (printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && this.bWantBeaconKeys) { HUD.log(hudLogCompassId, "CompassHeading", new Object[] { "" + (int) this.headingBug }); }
                if ((printCompassHeading || Main3D.cur3D().cockpitCur.printCompassHeading) && World.cur().diffCur.RealisticNavigationInstruments && this.bWantBeaconKeys)
                    HUD.log(hudLogCompassId, "CompassHeading", new Object[] { "" + (int) this.headingBug });
        }
    }

    public void auxMinus(int i) {
        switch (i) {
            case 1:
                this.headingBug = this.headingBug - 1.0F;
                if (this.headingBug < 0.0F) this.headingBug = 359.0F;
                // TODO: Storebror - cross version compatibility
//				if (printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && this.bWantBeaconKeys) { HUD.log(hudLogCompassId, "CompassHeading", new Object[] { "" + (int) this.headingBug }); }
                if ((printCompassHeading || Main3D.cur3D().cockpitCur.printCompassHeading) && World.cur().diffCur.RealisticNavigationInstruments && this.bWantBeaconKeys)
                    HUD.log(hudLogCompassId, "CompassHeading", new Object[] { "" + (int) this.headingBug });
        }
    }

    public void auxPressed(int i) {
        if (i == 1) this.FM.CT.dropExternalStores(true);
    }

    protected void hitFlesh(int i, Shot shot, int i_0_) {
        // TODO: Cheater Protection
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return;
        // ---
        int i_1_ = 0;
        int i_2_ = (int) (shot.power * 0.0035F * World.Rnd().nextFloat(0.5F, 1.5F));
        switch (i_0_) {
            case 0:
                if (!(World.Rnd().nextFloat() < 0.05F)) {
                    if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
                    i_2_ *= 30;
                    break;
                }
                return;
            case 1:
                if (World.Rnd().nextFloat() < 0.08F) {
                    i_2_ *= 2;
                    i_1_ = World.Rnd().nextInt(1, 15) * 8000;
                } else {
                    boolean bool = World.Rnd().nextInt(0, 100 - i_2_) <= 20;
                    if (bool) i_1_ = i_2_ / World.Rnd().nextInt(1, 10);
                }
                break;
            case 2:
                if (World.Rnd().nextFloat() < 0.015F) i_1_ = World.Rnd().nextInt(1, 15) * 1000;
                else {
                    boolean bool = World.Rnd().nextInt(0, 100 - i_2_) <= 10;
                    if (bool) i_1_ = i_2_ / World.Rnd().nextInt(1, 15);
                }
                i_2_ /= 1.5F;
                break;
        }
        this.debuggunnery("*** Pilot " + i + " hit for " + i_2_ + "% (" + (int) shot.power + " J)");
        if (i < 0 || i >= this.FM.AS.astatePilotStates.length) return; // TODO: Added by SAS~Storebror: Add array boundary checks!
        this.FM.AS.hitPilot(shot.initiator, i, i_2_);
        if (World.cur().diffCur.RealisticPilotVulnerability) {
            if (i_1_ > 0) this.FM.AS.setBleedingPilot(shot.initiator, i, i_1_);
            if (i == 0 && i_0_ > 0) this.FM.AS.woundedPilot(shot.initiator, i_0_, i_2_);
        }
        if (this.FM.AS.astatePilotStates[i] > 95 && i_0_ == 0) this.debuggunnery("*** Headshot!.");
    }

    // TODO: Storebror: Implement Aircraft Control Surfaces and Pilot View
    // Replication
    private float         headPos[] = { 0, 0, 0 };
    private float         headOr[]  = { 0, 0, 0 };
    private static Orient tmpOrLH   = new Orient();
    private float         headYp;
    private float         headTp;
    private float         headYm;
    private float         headTm;

    public void update(float f) {
        super.update(f);
//        DecimalFormat df = new DecimalFormat("0.##");
//        HUD.training("R:" + df.format(this.FM.EI.engines[0].getRPM()) +
//                " F:" + df.format(this.FM.EI.engines[0].getEngineForce().length()));
        if (World.getPlayerAircraft() == this /* && !NetMissionTrack.isPlaying() */ && !World.isPlayerGunner() && this.FM.AS.astatePilotStates[0] <= 95) // HookPilot hookpilot = HookPilot.current;
//            if(hookpilot != null) {
//                Orient o = (Orient)Reflection.getValue(hookpilot, "o");
//                setHeadAngles(-o.getAzimut(), o.getTangage());
//            }
            this.setHeadAngles(-HookPilot.current.o.getAzimut(), HookPilot.current.o.getTangage());
        else if (this.FM instanceof Maneuver) this.headTurn(f, (Maneuver) this.FM);
        this.movePilotsHead(this.viewAzimut, this.viewTangage);
    }

    public float[] getHeadPos() {
        return headPos;
    }

    public float[] getHeadOr() {
        return headOr;
    }

    protected void headTurn(float f, Maneuver maneuver) {
        boolean flag = false;
//            Vector3d Ve = (Vector3d)Reflection.getValue(maneuver, "Ve");
//            float pilotHeadY = Reflection.getFloat(maneuver, "pilotHeadY");
//            float pilotHeadT = Reflection.getFloat(maneuver, "pilotHeadT");
        switch (maneuver.get_task()) {
            case Maneuver.STAY_FORMATION:
                if (maneuver.Leader != null) {
                    Maneuver.Ve.set(maneuver.Leader.Loc);
                    flag = true;
                }
                break;
            case Maneuver.DEFENCE:
                if (maneuver.danger != null) {
                    Maneuver.Ve.set(maneuver.danger.Loc);
                    flag = true;
                }
                break;
            case Maneuver.DEFENDING:
                if (maneuver.airClient != null) {
                    Maneuver.Ve.set(maneuver.airClient.Loc);
                    flag = true;
                }
                break;
            case Maneuver.ATTACK_AIR:
                if (maneuver.target != null) {
                    Maneuver.Ve.set(maneuver.target.Loc);
                    flag = true;
                }
                break;
            case Maneuver.ATTACK_GROUND:
                if (maneuver.target_ground != null) {
                    Maneuver.Ve.set(maneuver.target_ground.pos.getAbsPoint());
                    flag = true;
                }
                break;
        }
        float f1;
        float f2;
        if (flag) {
            Maneuver.Ve.sub(maneuver.Loc);
            maneuver.Or.transformInv(Maneuver.Ve);
            tmpOr.setAT0(Maneuver.Ve);
            f1 = tmpOr.getTangage();
            f2 = tmpOr.getYaw();
            if (f2 > 75.0F) f2 = 75.0F;
            if (f2 < -75.0F) f2 = -75.0F;
            if (f1 < -15.0F) f1 = -15.0F;
            if (f1 > 40.0F) f1 = 40.0F;
        } else if (maneuver.get_maneuver() != Maneuver.PILOT_DEAD) {
            f1 = 0.0F;
            f2 = 0.0F;
        } else {
            f2 = -15.0F;
            f1 = -15.0F;
        }
        if (Math.abs(maneuver.pilotHeadT - f1) > 3.0F) maneuver.pilotHeadT = maneuver.pilotHeadT + 90.0F * (maneuver.pilotHeadT <= f1 ? 1.0F : -1.0F) * f;
        else maneuver.pilotHeadT = f1;
        if (Math.abs(maneuver.pilotHeadY - f2) > 2.0F) maneuver.pilotHeadY = maneuver.pilotHeadY + 60.0F * (maneuver.pilotHeadY <= f2 ? 1.0F : -1.0F) * f;
        else maneuver.pilotHeadY = f2;
//            Reflection.setFloat(maneuver, "pilotHeadY", pilotHeadY);
//            Reflection.setFloat(maneuver, "pilotHeadT", pilotHeadT);
        this.setHeadAngles(maneuver.pilotHeadY, maneuver.pilotHeadT);
    }

    public void movePilotsHead(float f, float f1) {
        if (Config.isUSE_RENDER() && (this.headTp < f1 || this.headTm > f1 || this.headYp < f || this.headYm > f)) {
            this.headTp = f1 + 0.0005F;
            this.headTm = f1 - 0.0005F;
            this.headYp = f + 0.0005F;
            this.headYm = f - 0.0005F;
//            f *= 0.7F;
//            f1 *= 0.7F;
            tmpOrLH.setYPR(0.0F, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, f, 0.0F);
            tmpOrLH.increment(f1, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, 0.0F, -0.2F * f1 + 0.05F * f1);

            this.headOr[0] = tmpOrLH.getYaw();
            this.headOr[1] = tmpOrLH.getPitch();
            this.headOr[2] = tmpOrLH.getRoll();

            this.headPos[0] = 0.0005F * Math.abs(f);
            this.headPos[1] = -0.0001F * Math.abs(f);
            this.headPos[2] = 0.0F;
            this.hierMesh().chunkSetLocate("Head1_D0", this.headPos, this.headOr);
            // TODO: Debugging Head Movement...
//            if (this == World.getPlayerAircraft()) {
//                java.text.DecimalFormat twoDigits = new java.text.DecimalFormat("#.##");
//                HUD.training("f:" + twoDigits.format(f) + "f1:" + twoDigits.format(f1) + " Y:" + twoDigits.format(headOr[0]) + " P:" + twoDigits.format(headOr[1]) + " R:" + twoDigits.format(headOr[2]));
//            }
        }
    }
    // ---

    // TODO: Cheater Protection
    protected boolean cutFM(int i, int j, Actor actor) {
        if (AircraftState.isCheaterHitInactive(actor)) return true;
        return super.cutFM(i, j, actor);
    }

    // TODO: Cheater Protection
    protected void nextDMGLevel(String s, int i, Actor actor) {
        if (AircraftState.isCheaterHitInactive(actor)) return;
        super.nextDMGLevel(s, i, actor);
        return;
    }

    // TODO: Cheater Protection
    protected void nextDMGLevels(int i, int j, String s, Actor actor) {
        if (AircraftState.isCheaterHitInactive(actor)) return;
        super.nextDMGLevels(i, j, s, actor);
        return;
    }

    // TODO: Cheater Protection
    protected void nextCUTLevel(String s, int i, Actor actor) {
        if (AircraftState.isCheaterHitInactive(actor)) return;
        super.nextCUTLevel(s, i, actor);
        return;
    }

    // TODO: Cheater Protection
    public void msgCollision(Actor actor, String s, String s1) {
        if (AircraftState.isCheaterHitInactive(actor)) return;
        super.msgCollision(actor, s, s1);
        return;
    }

    // TODO: Cheater Protection
    public void msgExplosion(Explosion explosion) {
        if (AircraftState.isCheaterHitInactive(explosion.initiator)) return;
        super.msgExplosion(explosion);
        return;
    }

    // TODO: Cheater Protection
    protected void setShot(Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return;
        super.setShot(shot);
        return;
    }

    // TODO: Cheater Protection
    protected void setExplosion(Explosion explosion) {
        if (AircraftState.isCheaterHitInactive(explosion.initiator)) return;
        super.setExplosion(explosion);
        return;
    }

    // TODO: Cheater Protection
    public void msgShot(Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return;
        super.msgShot(shot);
        return;
    }

    // TODO: Cheater Protection
    protected void hitChunk(String s, Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return;
        super.hitChunk(s, shot);
        return;
    }

    // TODO: Cheater Protection
    protected float getEnergyPastArmor(float f, float f1, Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return 0.0F;
        return super.getEnergyPastArmor(f, f1, shot);
    }

    // TODO: Cheater Protection
    protected float getEnergyPastArmor(float f, Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return 0.0F;
        return super.getEnergyPastArmor(f, shot);
    }

    // TODO: Cheater Protection
    protected float getEnergyPastArmor(double d, float f, Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return 0.0F;
        return super.getEnergyPastArmor(d, f, shot);
    }

    // TODO: Cheater Protection
    protected float getEnergyPastArmor(double d, Shot shot) {
        if (AircraftState.isCheaterHitInactive(shot.initiator)) return 0.0F;
        return super.getEnergyPastArmor(d, shot);
    }

    // TODO: Cheater Protection
    protected void netHits(int i, int j, int k, Actor actor) {
        if (AircraftState.isCheaterHitInactive(actor)) return;
        super.netHits(i, j, k, actor);
        return;
    }
    // ---
    
    // TODO: Backport from 4.12 Fuzes
    private static final float[] armingTravelToAltScale = { 0.0F, 8.0F, 13.0F, 17.0F, 29.0F, 43.0F, 55.0F, 66.0F, 78.0F, 92.0F, 116.0F, 152.0F, 186.0F, 223.0F, 260.0F, 300.0F };
    private static Map infoMap = new HashMap();
    public static boolean hasFuzeModeSelector = false;
    
    public static void setInfo(Fuze fuze, String s1, String s2, float paramFloat)
    {
      ResourceBundle localResourceBundle = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
      String str1 = s1;
      String[] arrayOfString = new String[5];
      try
      {
        str1 = localResourceBundle.getString(s1);
      }
      catch (MissingResourceException localMissingResourceException1)
      {
        System.out.println(localMissingResourceException1);
      }
      arrayOfString[0] = (localResourceBundle.getString("Bomb") + " " + str1);
      String str2 = "";
      String str3 = null;
      String str4 = "";
      int j;
      if ((fuze instanceof Fuze_EL_AZ))
      {
        try
        {
          str2 = localResourceBundle.getString(s2);
          
          int i = 1;
          j = 0;
          for (int k = 0; k < str2.length(); k++) {
            if (str2.charAt(k) == '\n')
            {
              arrayOfString[i] = ("  " + str2.substring(j, k));
              j = k + 1;
              i++;
            }
          }
        }
        catch (MissingResourceException localMissingResourceException2)
        {
          System.out.println(localMissingResourceException2);
        }
      }
      else
      {
        float f = Property.floatValue(fuze.getClass(), "airTravelToArm", -1.0F);
        if (f == -1.0F)
        {
          j = Property.intValue(fuze.getClass(), "armingTime", 2000);
          String str5 = "" + j / 1000.0F;
          str5 = str5.substring(0, str5.indexOf(".") + 2);
          str2 = "  " + localResourceBundle.getString("Arming") + " " + str5 + " " + localResourceBundle.getString("ArmingTimeSeconds");
        }
        else
        {
          j = Math.round(floatindex(cvt(f, 0.0F, 750.0F, 0.0F, 15.0F), armingTravelToAltScale));
          str2 = "  " + localResourceBundle.getString("Arming") + " ~" + j + " " + localResourceBundle.getString("DropAltM");
          str3 = "  " + localResourceBundle.getString("Arming") + " ~" + (int)(j * 3.28084F) + " " + localResourceBundle.getString("DropAltFt");
        }
        str4 = "  " + localResourceBundle.getString("Delay") + " " + paramFloat + " " + localResourceBundle.getString("ArmingTimeSeconds");
        arrayOfString[1] = str2;
        arrayOfString[2] = str4;
        arrayOfString[4] = str3;
      }
      if (!infoMap.containsKey(s1)) {
        infoMap.put(s1, arrayOfString);
      }
    }
    // ---
   
}
