/*Modified AutopilotAI class for the SAS Engine Mod*/
//TODO: Includes changes that allow for AI bomb bay door and missile control

package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.sounds.Voice;

public class AutopilotAI extends Autopilotage
{
  public boolean bWayPoint;
  public boolean bStabAltitude;
  public boolean bStabSpeed;
  public boolean bStabDirection;
  protected double StabAltitude;
  protected double StabSpeed;
  protected double StabDirection;
  protected Pilot FM;
  protected WayPoint WWPoint;
  protected Point3d WPoint = new Point3d();

  private static Point3d P = new Point3d();
  private static Point3d PlLoc = new Point3d();
  private static Orientation O = new Orientation();

  protected Vector3d courseV = new Vector3d();
  protected Vector3d windV = new Vector3d();
  private float Ail;
  private float Pw;
  private float Ru;
  private float Ev;
  private float SA;
  private Vector3d Ve = new Vector3d();

    private boolean overrideMissileControl = false;
    private Controls theMissileControls = null;
  
  
  public AutopilotAI(FlightModel paramFlightModel)
  {
    this.FM = ((Pilot)paramFlightModel);
  }
  public boolean getWayPoint() {
    return this.bWayPoint; } 
  public boolean getStabAltitude() { return this.bStabAltitude; } 
  public boolean getStabSpeed() { return this.bStabSpeed; } 
  public boolean getStabDirection() { return this.bStabDirection;
  }

  public void setWayPoint(boolean paramBoolean)
  {
    this.bWayPoint = paramBoolean;
    if (!paramBoolean) return;
    this.bStabSpeed = false;
    this.bStabAltitude = false;
    this.bStabDirection = false;

    if (this.WWPoint != null) {
      this.WWPoint.getP(this.WPoint);
      this.StabSpeed = this.WWPoint.Speed;
      this.StabAltitude = this.WPoint.z;
    } else {
      this.StabAltitude = 1000.0D;
      this.StabSpeed = 80.0D;
    }
    this.StabDirection = O.getAzimut();
  }

  public void setStabAltitude(boolean paramBoolean) {
    this.bStabAltitude = paramBoolean;
    if (!paramBoolean) return;
    this.bWayPoint = false;
    this.FM.getLoc(P);
    this.StabAltitude = P.z;

    if (!this.bStabSpeed) this.StabSpeed = this.FM.getSpeed();

    this.Pw = this.FM.CT.PowerControl;
  }

  public void setStabAltitude(float paramFloat)
  {
    this.bStabAltitude = true;
    this.bWayPoint = false;
    this.FM.getLoc(P);
    this.StabAltitude = paramFloat;

    if (!this.bStabSpeed) this.StabSpeed = this.FM.getSpeed();

    this.Pw = this.FM.CT.PowerControl;
  }

  public void setStabSpeed(boolean paramBoolean)
  {
    this.bStabSpeed = paramBoolean;
    if (!paramBoolean) return;
    this.bWayPoint = false;
    this.StabSpeed = this.FM.getSpeed();
  }

  public void setStabSpeed(float paramFloat)
  {
    this.bStabSpeed = true;
    this.bWayPoint = false;
    this.StabSpeed = (paramFloat / 3.6F);
  }

  public void setStabDirection(boolean paramBoolean)
  {
    this.bStabDirection = paramBoolean;
    if (!paramBoolean) return;
    this.bWayPoint = false;
    O.set(this.FM.Or);
    this.StabDirection = O.getAzimut();
    this.Ail = this.FM.CT.AileronControl;
  }

  public void setStabDirection(float paramFloat) {
    this.bStabDirection = true;
    this.bWayPoint = false;
    this.StabDirection = ((paramFloat + 3600.0F) % 360.0F);
    this.Ail = this.FM.CT.AileronControl;
  }

  public void setStabAll(boolean paramBoolean) {
    this.bWayPoint = false;
    setStabDirection(paramBoolean);
    setStabAltitude(paramBoolean);
    setStabSpeed(paramBoolean);
    setStabDirection(paramBoolean);
  }

  public float getWayPointDistance()
  {
    if (this.WPoint == null) return 1000000.0F;
    this.way.curr().getP(P); P.sub(this.FM.Loc);
    return (float)Math.sqrt(P.x * P.x + P.y * P.y);
  }

  private void voiceCommand(Point3d paramPoint3d1, Point3d paramPoint3d2) {
    this.Ve.sub(paramPoint3d2, paramPoint3d1);
    float f = 57.324841F * (float)Math.atan2(this.Ve.x, this.Ve.y);
    int i = (int)f;
    i = (i + 180) % 360;
    Voice.speakHeading((Aircraft)this.FM.actor, i);
    Voice.speakAltitude((Aircraft)this.FM.actor, (int)paramPoint3d1.z);
  }

  public void update(float paramFloat) {
      if (this.overrideMissileControl) {
        this.theMissileControls.WeaponControl[2] = true;
      }
    this.FM.getLoc(PlLoc);
    this.SA = (float)Math.max(this.StabAltitude, Engine.land().HQ_Air(PlLoc.x, PlLoc.y) + 5.0D);

    if (this.bWayPoint) {
      if ((this.WWPoint != this.way.auto(PlLoc)) || (this.way.isReached(PlLoc))) {
        this.WWPoint = this.way.auto(PlLoc);
        this.WWPoint.getP(this.WPoint);
        if ((((Aircraft)this.FM.actor).aircIndex() == 0) && (!this.way.isLanding())) voiceCommand(this.WPoint, PlLoc);

        this.StabSpeed = (this.WWPoint.Speed - 2.0F * ((Aircraft)this.FM.actor).aircIndex());
        this.StabAltitude = this.WPoint.z;
        Object localObject;
        if (this.WWPoint.Action == 3) {
          localObject = this.WWPoint.getTarget();
          if (localObject != null) {
            this.FM.target_ground = null;
          }
          else if ((((Aircraft)this.FM.actor instanceof TypeBomber)) && (this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][0] != null) && (this.FM.CT.Weapons[3][(this.FM.CT.Weapons[3].length - 1)].haveBullets()))
          {
            this.FM.CT.BayDoorControl = 1.0F;
            Pilot localPilot = this.FM;
            while (localPilot.Wingman != null) {
              localPilot = (Pilot)localPilot.Wingman;
              localPilot.CT.BayDoorControl = 1.0F;
            }
          }
        }
        else {
          if (((Aircraft)this.FM.actor instanceof TypeBomber)) {
            this.FM.CT.BayDoorControl = 0.0F;
            localObject = this.FM;
            while (((Pilot)localObject).Wingman != null) {
              localObject = (Pilot)((Pilot)localObject).Wingman;
              ((Pilot)localObject).CT.BayDoorControl = 0.0F;
            }
          }
          localObject = this.WWPoint.getTarget();
          if ((localObject instanceof Aircraft)) {
            if (((Actor)localObject).getArmy() == this.FM.actor.getArmy()) this.FM.airClient = ((Maneuver)((Aircraft)localObject).FM); else {
              this.FM.target = ((Aircraft)localObject).FM;
            }
          }
        }

        if (this.way.isLanding()) {
          this.FM.getLoc(P);
          if ((this.way.Cur() > 3) && (P.z > this.WPoint.z + 500.0D)) this.way.setCur(1);
          if ((this.way.Cur() == 5) && (
            (!Mission.isDogfight()) || (!Main.cur().mission.zutiMisc_DisableAIRadioChatter))) {
            Voice.speakLanding((Aircraft)this.FM.actor);
          }
          if ((this.way.Cur() == 6) || (this.way.Cur() == 7)) {
            int i = 0;
            if (Actor.isAlive(this.way.landingAirport)) {
              i = this.way.landingAirport.landingFeedback(this.WPoint, (Aircraft)this.FM.actor);
            }
            if ((i == 0) && (
              (!Mission.isDogfight()) || (!Main.cur().mission.zutiMisc_DisableAIRadioChatter))) {
              Voice.speakLandingPermited((Aircraft)this.FM.actor);
            }
            if (i == 1) {
              if ((!Mission.isDogfight()) || (!Main.cur().mission.zutiMisc_DisableAIRadioChatter))
                Voice.speakLandingDenied((Aircraft)this.FM.actor);
              this.way.first();
              this.FM.push(2);
              this.FM.push(2);
              this.FM.push(2);
              this.FM.push(2);
              this.FM.pop();
              if ((!Mission.isDogfight()) || (!Main.cur().mission.zutiMisc_DisableAIRadioChatter))
                Voice.speakGoAround((Aircraft)this.FM.actor);
              this.FM.CT.FlapsControl = 0.4F;
              this.FM.CT.GearControl = 0.0F;
              return;
            }
            if (i == 2) {
              if ((!Mission.isDogfight()) || (!Main.cur().mission.zutiMisc_DisableAIRadioChatter))
                Voice.speakWaveOff((Aircraft)this.FM.actor);
              if (this.FM.isReadyToReturn()) {
                if ((!Mission.isDogfight()) || (!Main.cur().mission.zutiMisc_DisableAIRadioChatter))
                  Voice.speakGoingIn((Aircraft)this.FM.actor);
                this.FM.AS.setCockpitDoor(this.FM.actor, 1);
                this.FM.CT.GearControl = 1.0F;
                return;
              }
              this.way.first();
              this.FM.push(2);
              this.FM.push(2);
              this.FM.push(2);
              this.FM.push(2);
              this.FM.pop();
              this.FM.CT.FlapsControl = 0.4F;
              this.FM.CT.GearControl = 0.0F;
              Aircraft.debugprintln(this.FM.actor, "Going around!.");
              return;
            }
            this.FM.CT.GearControl = 1.0F;
          }
        }
      }

      if ((this.way.isLanding()) && (this.way.Cur() < 6) && (this.way.getCurDist() < 800.0D)) this.way.next();

      if (((this.way.Cur() == this.way.size() - 1) && (getWayPointDistance() < 2000.0F) && (this.way.curr().getTarget() == null) && (this.FM.M.fuel < 0.2F * this.FM.M.maxFuel)) || ((this.way.curr().Action == 2) && (!this.way.isLanding())))
      {
        Airport localAirport = Airport.makeLandWay(this.FM);
        if (localAirport != null) {
          this.WWPoint = null;
          this.way.first();
          update(paramFloat);
          return;
        }
        this.FM.set_task(3);
        this.FM.set_maneuver(49);
        this.FM.setBusy(true);
      }
      boolean bStabDirectionSet = false;

      if (World.cur().diffCur.Wind_N_Turbulence) {
        World.cur();
        if ((!World.wind().noWind) && (this.FM.Skill > 0))
        {
          World.cur(); World.wind().getVectorAI(this.WPoint, this.windV);
          this.windV.scale(-1.0D);

          if (this.FM.Skill == 1) {
            this.windV.scale(0.75D);
          }
          this.courseV.set(this.WPoint.x - PlLoc.x, this.WPoint.y - PlLoc.y, 0.0D);
          this.courseV.normalize();

          this.courseV.scale(this.FM.getSpeed());
          this.courseV.add(this.windV);
          this.StabDirection = (-FMMath.RAD2DEG((float)Math.atan2(this.courseV.y, this.courseV.x)));
          bStabDirectionSet = true;
        }
      }
      if (!bStabDirectionSet) this.StabDirection = (-FMMath.RAD2DEG((float)Math.atan2(this.WPoint.y - PlLoc.y, this.WPoint.x - PlLoc.x)));
    }

    if ((this.bStabSpeed) || (this.bWayPoint)) {
      this.Pw = (0.3F - 0.04F * (this.FM.getSpeed() - (float)this.StabSpeed));
      if (this.Pw > 1.0F) this.Pw = 1.0F; else if (this.Pw < 0.0F) this.Pw = 0.0F;

    }

    if ((this.bStabAltitude) || (this.bWayPoint)) {
      this.Ev = this.FM.CT.ElevatorControl;

      double d1 = this.SA - this.FM.getAltitude();
      double d2 = 0.0D;
      double d3 = 0.0D;
      float f4;
      if (d1 > -50.0D)
      {
        f4 = 5.0F + 0.00025F * this.FM.getAltitude();
        f4 = (float)(f4 + 0.02D * (250.0D - this.FM.Vmax));
        if (f4 > 14.0F) f4 = 14.0F;
        d2 = Math.min(this.FM.getAOA() - f4, this.FM.Or.getTangage() - 1.0F) * 1.0F * paramFloat + 0.5F * this.FM.getForwAccel();
      }

      if (d1 < 50.0D)
      {
        f4 = -15.0F + this.FM.M.mass * 0.00033F;
        if (f4 < -4.0F) f4 = -4.0F;
        d3 = (this.FM.Or.getTangage() - f4) * 0.8F * paramFloat;
      }
      double d4 = 0.01D * (d1 + 50.0D);
      if (d4 > 1.0D) d4 = 1.0D;
      if (d4 < 0.0D) d4 = 0.0D;
      this.Ev = (float)(this.Ev - (d4 * d2 + (1.0D - d4) * d3));

      this.Ev = (float)(this.Ev + (1.0D * this.FM.getW().y + 0.5D * this.FM.getAW().y));
      if (this.FM.getSpeed() < 1.3F * this.FM.VminFLAPS) this.Ev -= 0.004F * paramFloat;

      float f5 = 9.0F * this.FM.getSpeed() / this.FM.VminFLAPS;
      if (this.FM.VminFLAPS < 28.0F) f5 = 10.0F;
      if (f5 > 25.0F) f5 = 25.0F;
      float f6 = (f5 - this.FM.Or.getTangage()) * 0.1F;
      float f7 = -15.0F + this.FM.M.mass * 0.00033F;
      if (f7 < -4.0F) f7 = -4.0F;
      float f8 = (f7 - this.FM.Or.getTangage()) * 0.2F;

      if (this.Ev > f6) this.Ev = f6;
      if (this.Ev < f8) this.Ev = f8;

      this.FM.CT.ElevatorControl = (0.8F * this.FM.CT.ElevatorControl + 0.2F * this.Ev);
    }

    float f1 = 0.0F;

    if ((this.bStabDirection) || (this.bWayPoint))
    {
      f1 = this.FM.Or.getAzimut();
      float f2 = this.FM.Or.getKren();
      f1 = (float)(f1 - this.StabDirection);

      f1 = (f1 + 3600.0F) % 360.0F;
      f2 = (f2 + 3600.0F) % 360.0F;
      if (f1 > 180.0F) f1 -= 360.0F;
      if (f2 > 180.0F) f2 -= 360.0F;

      float f3 = ((this.FM.getSpeed() - this.FM.VminFLAPS) * 3.6F + this.FM.getVertSpeed() * 40.0F) * 0.25F;
      if (this.way.isLanding()) f3 = 65.0F;
      if (f3 < 15.0F) f3 = 15.0F; else if (f3 > 65.0F) f3 = 65.0F;
      if (f1 < -f3) f1 = -f3; else if (f1 > f3) f1 = f3;

      this.Ail = (-0.01F * (f1 + f2 + 3.0F * (float)this.FM.getW().x + 0.5F * (float)this.FM.getAW().x));
      if (this.Ail > 1.0F) this.Ail = 1.0F; else if (this.Ail < -1.0F) this.Ail = -1.0F;
      this.WPoint.get(this.Ve);
      this.Ve.sub(this.FM.Loc);
      this.FM.Or.transformInv(this.Ve);
      if ((Math.abs(this.Ve.y) < 25.0D) && (Math.abs(this.Ve.x) < 150.0D))
        this.FM.CT.AileronControl = (-0.01F * this.FM.Or.getKren());
      else
        this.FM.CT.AileronControl = this.Ail;
      this.FM.CT.ElevatorControl += Math.abs(f2) * 0.004F * paramFloat;

      this.FM.CT.RudderControl -= this.FM.getAOS() * 0.04F * paramFloat;
    }

    if ((this.bWayPoint) && (this.way.isLanding())) {
      if (World.Rnd().nextFloat() < 0.01F) this.FM.doDumpBombsPassively();
      if (this.way.Cur() > 5) this.FM.set_maneuver(25);
      this.FM.CT.RudderControl -= f1 * 0.04F * paramFloat;
      landUpdate(paramFloat);
    }
  }

  private void landUpdate(float paramFloat)
  {
    if (this.FM.getAltitude() - 10.0F + this.FM.getVertSpeed() * 5.0F - this.SA > 0.0F) {
      if (this.FM.Vwld.z > -10.0D) this.FM.Vwld.z -= 1.0F * paramFloat;
    }
    else if (this.FM.Vwld.z < 10.0D) this.FM.Vwld.z += 1.0F * paramFloat;

    if ((this.FM.getAOA() > 11.0F) && (this.FM.CT.ElevatorControl > -0.3F))
      this.FM.CT.ElevatorControl -= 0.3F * paramFloat;
  }
  
    public void setOverrideMissileControl(Controls theControls, boolean overrideMissile) {
      this.theMissileControls = theControls;
      this.overrideMissileControl = overrideMissile;
    }
  
}