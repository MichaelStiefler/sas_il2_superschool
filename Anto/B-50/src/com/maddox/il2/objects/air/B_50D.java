package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class B_50D extends B_29X
implements TypeX4Carrier, TypeGuidedBombCarrier, TypeDockable, TypeGuidedMissileCarrier
{
	private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	
	public B_50D()
	{
		bToFire = false;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
	}


	public GuidedMissileUtils getGuidedMissileUtils() {
	    return this.guidedMissileUtils;
	}

	public void onAircraftLoaded()
	{
		super.onAircraftLoaded();
	    this.guidedMissileUtils.onAircraftLoaded();
		if((getBulletEmitterByHookName("_ExternalRock01") instanceof com.maddox.il2.objects.weapons.RocketGunGAM63) || (getBulletEmitterByHookName("_ExternalBomb03") instanceof com.maddox.il2.objects.weapons.RocketGunTarzon))
		{
			hierMesh().chunkVisible("Bay01_D0", false);
			hierMesh().chunkVisible("Bay02_D0", false);
			hierMesh().chunkVisible("Bay03_D0", false);
			hierMesh().chunkVisible("Bay04_D0", false);
			hierMesh().chunkVisible("Radome", false);
			FM.CT.BayDoorControl = 1.0F;
			FM.CT.bHasBayDoors = false;
		}
	}


	public boolean typeGuidedBombCisMasterAlive()
	{
		return isMasterAlive;
	}

	public void typeGuidedBombCsetMasterAlive(boolean flag)
	{
		isMasterAlive = flag;
	}

	public boolean typeGuidedBombCgetIsGuiding()
	{
		return isGuidingBomb;
	}

	public void typeGuidedBombCsetIsGuiding(boolean flag)
	{
		isGuidingBomb = flag;
	}

	public void typeX4CAdjSidePlus()
	{
		deltaAzimuth = 0.002F;
	}

	public void typeX4CAdjSideMinus()
	{
		deltaAzimuth = -0.002F;
	}

	public void typeX4CAdjAttitudePlus()
	{
		deltaTangage = 0.002F;
	}

	public void typeX4CAdjAttitudeMinus()
	{
		deltaTangage = -0.002F;
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


	protected boolean cutFM(int i, int j, Actor actor)
	{
		switch(i)
		{
		case 19: // '\023'
		killPilot(this, 4);
		break;
		}
		return super.cutFM(i, j, actor);
	}

	public void doWoundPilot(int i, float f)
	{
		switch(i)
		{
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[2].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[3].setHealth(f);
			FM.turret[4].setHealth(f);
			break;
		}
	}

    protected void moveRadiator(float f)
    {
        for(int i = 0; i < 4; i++)
        {
            float f1 = FM.EI.engines[i].getControlRadiator();
            if(Math.abs(super.flapps[i] - f1) <= 0.01F)
                continue;
            super.flapps[i] = f1;
            if(i == 0)
            for(int j = 0; j < 9; j++)
                hierMesh().chunkSetAngles("Water" + (1 + j) + "_D0", 0.0F, -20F * f1, 0.0F);
            else if(i == 1)
            for(int j = 0; j < 9; j++)
                hierMesh().chunkSetAngles("Water" + (10 + j) + "_D0", 0.0F, -20F * f1, 0.0F);
            else if(i == 2)
            for(int j = 0; j < 9; j++)
                hierMesh().chunkSetAngles("Water" + (19 + j) + "_D0", 0.0F, -20F * f1, 0.0F);
            else if(i == 3)
            for(int j = 0; j < 9; j++)
                hierMesh().chunkSetAngles("Water" + (28 + j) + "_D0", 0.0F, -20F * f1, 0.0F);
        }
    }
    
	  public void update(float f) {
		    if (bNeedSetup) {
		      checkAsDrone();
		    }
		    int i = aircIndex();
		    if (super.FM instanceof Maneuver) {
		      if (typeDockableIsDocked()) {
		        if (!(super.FM instanceof RealFlightModel) || !((RealFlightModel) super.FM).isRealMode()) {
		          ((Maneuver) super.FM).unblock();
		          ((Maneuver) super.FM).set_maneuver(48);
		          for (int j = 0; j < i; j++) {
		            ((Maneuver) super.FM).push(48);
		          }

		          if (((FlightModelMain) (super.FM)).AP.way.curr().Action != 3) {
		            ((FlightModelMain) ((Maneuver) super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft) queen_)).FM)).AP.way.Cur());
		          }
		          ((Pilot) super.FM).setDumbTime(3000L);
		        }
		        if (((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel) {
		          ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
		        }
		      } else if (!(super.FM instanceof RealFlightModel) || !((RealFlightModel) super.FM).isRealMode()) {
		        if (FM.CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0) {
		          ((FlightModelMain) (super.FM)).EI.setEngineRunning();
		        }
		        if (dtime > 0L && ((Maneuver) super.FM).Group != null) {
		          ((Maneuver) super.FM).Group.leaderGroup = null;
		          ((Maneuver) super.FM).set_maneuver(22);
		          ((Pilot) super.FM).setDumbTime(3000L);
		          if (Time.current() > dtime + 3000L) {
		            dtime = -1L;
		            ((Maneuver) super.FM).clear_stack();
		            ((Maneuver) super.FM).set_maneuver(0);
		            ((Pilot) super.FM).setDumbTime(0L);
		          }
		        } else if (((FlightModelMain) (super.FM)).AP.way.curr().Action == 0) {
		          Maneuver maneuver = (Maneuver) super.FM;
		          if (maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null) {
		            maneuver.Group.setGroupTask(2);
		          }
		        }
		      }
		    }
		    this.guidedMissileUtils.update();
		    super.update(f);
		  }

		  public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		    super.msgCollisionRequest(actor, aflag);
		    if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L)) {
		      aflag[0] = false;
		    } else {
		      aflag[0] = true;
		    }
		  }

		  public void missionStarting() {
		    checkAsDrone();
		  }

		  private void checkAsDrone() {
		    if (target_ == null) {
		      if (((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null) {
		        ((FlightModelMain) (super.FM)).AP.way.next();
		      }
		      target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
		      if (Actor.isValid(target_) && (target_ instanceof Wing)) {
		        Wing wing = (Wing) target_;
		        int i = aircIndex();
		        if (Actor.isValid(wing.airc[i / 2])) {
		          target_ = wing.airc[i / 2];
		        } else {
		          target_ = null;
		        }
		      }
		    }
		    if (Actor.isValid(target_) && (target_ instanceof TypeTankerBoom)) {
		      queen_last = target_;
		      queen_time = Time.current();
		      if (isNetMaster()) {
		        ((TypeDockable) target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
		      }
		    }
		    bNeedSetup = false;
		    target_ = null;
		  }

		  public int typeDockableGetDockport() {
		    if (typeDockableIsDocked()) {
		      return dockport_;
		    } else {
		      return -1;
		    }
		  }

		  public Actor typeDockableGetQueen() {
		    return queen_;
		  }

		  public boolean typeDockableIsDocked() {
		    return Actor.isValid(queen_);
		  }

		  public void typeDockableAttemptAttach() {
		    if (((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked()) {
		      Aircraft aircraft = War.getNearestFriend(this);
		      if (aircraft instanceof TypeTankerBoom) {
		        ((TypeDockable) aircraft).typeDockableRequestAttach(this);
		      }
		    }
		  }

		  public void typeDockableAttemptDetach() {
		    if (((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_)) {
		      ((TypeDockable) queen_).typeDockableRequestDetach(this);
		    }
		  }

		  public void typeDockableRequestAttach(Actor actor1) {
		  }

		  public void typeDockableRequestDetach(Actor actor1) {
		  }

		  public void typeDockableRequestAttach(Actor actor1, int j, boolean flag1) {
		  }

		  public void typeDockableRequestDetach(Actor actor1, int j, boolean flag1) {
		  }

		  public void typeDockableDoAttachToDrone(Actor actor1, int j) {
		  }

		  public void typeDockableDoDetachFromDrone(int j) {
		  }

		  public void typeDockableDoAttachToQueen(Actor actor, int i) {

		    queen_ = actor;
		    dockport_ = i;
		    queen_last = queen_;
		    queen_time = 0L;
		    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
		    ((FlightModelMain) (super.FM)).CT.setGearAirborne();
		    moveGear(0.0F);
		    com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft) queen_)).FM;
		    if (aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
		      Maneuver maneuver = (Maneuver) flightmodel;
		      Maneuver maneuver1 = (Maneuver) super.FM;
		      if (maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
		        AirGroup airgroup = new AirGroup(maneuver1.Group);
		        maneuver1.Group.delAircraft(this);
		        airgroup.addAircraft(this);
		        airgroup.attachGroup(maneuver.Group);
		        airgroup.rejoinGroup = null;
		        airgroup.leaderGroup = null;
		        airgroup.clientGroup = maneuver.Group;
		      }
		    }
		  }

		  public void typeDockableDoDetachFromQueen(int i) {
		    if (dockport_ == i) {
		      queen_last = queen_;
		      queen_time = Time.current();
		      queen_ = null;
		      dockport_ = 0;
		    }
		  }

		  public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
		          throws IOException {
		    if (typeDockableIsDocked()) {
		      netmsgguaranted.writeByte(1);
		      com.maddox.il2.engine.ActorNet actornet = null;
		      if (Actor.isValid(queen_)) {
		        actornet = queen_.net;
		        if (actornet.countNoMirrors() > 0) {
		          actornet = null;
		        }
		      }
		      netmsgguaranted.writeByte(dockport_);
		      netmsgguaranted.writeNetObj(actornet);
		    } else {
		      netmsgguaranted.writeByte(0);
		    }
		  }

		  public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
		          throws IOException {
		    if (netmsginput.readByte() == 1) {
		      dockport_ = netmsginput.readByte();
		      NetObj netobj = netmsginput.readNetObj();
		      if (netobj != null) {
		        Actor actor = (Actor) netobj.superObj();
		        ((TypeDockable) actor).typeDockableDoAttachToDrone(this, dockport_);
		      }
		    }
		  }

		  public void rareAction(float f, boolean flag) {
		    super.rareAction(f, flag);
		    if ((super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot)) {
		      return;
		    }
		    if (flag && ((FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft) queen_)).FM)).Or.getKren()) < 3F) {
		      if (super.FM.isPlayers()) {
		        if ((super.FM instanceof RealFlightModel) && !((RealFlightModel) super.FM).isRealMode()) {
		          typeDockableAttemptDetach();
		          ((Maneuver) super.FM).set_maneuver(22);
		          ((Maneuver) super.FM).setCheckStrike(false);
		          ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
		          dtime = Time.current();
		        }
		      } else {
		        typeDockableAttemptDetach();
		        ((Maneuver) super.FM).set_maneuver(22);
		        ((Maneuver) super.FM).setCheckStrike(false);
		        ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
		        dtime = Time.current();
		      }
		    }
		  }

		  
	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;
	private Actor queen_last;
	  private long queen_time;
	  private boolean bNeedSetup;
	  private long dtime;
	  private Actor target_;
	  private Actor queen_;
	  private int dockport_;

	static 
	{
		Class class1 = com.maddox.il2.objects.air.B_50D.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-50D");
		Property.set(class1, "meshName", "3DO/Plane/B-50D(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar1956());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1946F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-50D.fmd:B29_50");
		Property.set(class1, "cockpitClass", new Class[] {
	            com.maddox.il2.objects.air.CockpitB29.class, com.maddox.il2.objects.air.CockpitB50_Bombardier.class, com.maddox.il2.objects.air.CockpitB29_NoseGunner.class, com.maddox.il2.objects.air.CockpitB29_T2Gunner.class, com.maddox.il2.objects.air.CockpitB29_WRGunner.class, com.maddox.il2.objects.air.CockpitB29_AGunner.class, com.maddox.il2.objects.air.CockpitB29_WLGunner.class
		});
		weaponTriggersRegister(class1, new int[] {
				10, 10, 10, 10, 11, 11, 12, 12, 13, 13, 
				14, 14, 3, 3, 3, 3, 3, 3, 3, 3,
				3, 2
		});
		weaponHooksRegister(class1, new String[] {
				"_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", 
				"_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_ExternalBomb01", "_ExternalBomb02",
				"_ExternalBomb03", "_ExternalRock01"
		});
		weaponsRegister(class1, "default", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null, 
				null, null
		});
		weaponsRegister(class1, "1x1600", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, "BombGun1600lbs 1", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "6x300", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun300lbs 3", "BombGun300lbs 3", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "20x100", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun50kg 3", "BombGun50kg 3", "BombGun50kg 7", "BombGun50kg 7", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "4x500", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun500lbs 2", "BombGun500lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "2x1000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "1x2000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, "BombGun2000lbs 1", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "4x1000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun1000lbs 2", "BombGun1000lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "2x2000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun2000lbs 1", "BombGun2000lbs 1", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "16x300", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun300lbs 8", "BombGun300lbs 8", null, null, null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "10x500", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 5", "BombGun500lbs 5", null, null, null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "20x250", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun250lbs 8", "BombGun250lbs 8", "BombGun250lbs 2", "BombGun250lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "6x1600", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1600lbs 1", "BombGun1600lbs 1", "BombGun1600lbs 2", "BombGun1600lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "20x500", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 8", "BombGun500lbs 8", "BombGun500lbs 2", "BombGun500lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "12x1000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1000lbs 1", "BombGun1000lbs 1", "BombGun1000lbs 2", "BombGun1000lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "6x2000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun2000lbs 1", "BombGun2000lbs 1", "BombGun2000lbs 2", "BombGun2000lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "12x1600", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1600lbs 1", "BombGun1600lbs 1", "BombGun1600lbs 2", "BombGun1600lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "20x1000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1000lbs 8", "BombGun1000lbs 8", "BombGun1000lbs 2", "BombGun1000lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "10x2000", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun2000lbs 3", "BombGun2000lbs 3", "BombGun2000lbs 2", "BombGun2000lbs 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "4xRazon", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "RocketGunRazon 2", "RocketGunRazon 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "10xRazon", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "RocketGunRazon 5", "RocketGunRazon 5", null, null, null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "20xRazon", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "RocketGunRazon 8", "RocketGunRazon 8", "RocketGunRazon 2", "RocketGunRazon 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "2xTallBoy", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, "BombGun12000Tallboy 1", "BombGun12000Tallboy 1",
				null, null
		});
		weaponsRegister(class1, "1xTarzon", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null,
				"RocketGunTarzon 1", null
		});
		weaponsRegister(class1, "1xMk4", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, "BombGunFatMan 1", null, null,
				null, null
		});
		weaponsRegister(class1, "1xMk7", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, "BombGunMk7", null,
				null, null
		});
		weaponsRegister(class1, "1xGAM63", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null, 
				null, "RocketGunGAM63 1"
		});
		weaponsRegister(class1, "60xMk81", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunMk81 24", "BombGunMk81 24", "BombGunMk81 6", "BombGunMk81 6", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "44xMk82", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunMk82 18", "BombGunMk82 18", "BombGunMk82 4", "BombGunMk82 4", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "20xMk83", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunMk82 8", "BombGunMk82 8", "BombGunMk82 2", "BombGunMk82 2", null, null, null, null,
				null, null
		});
		weaponsRegister(class1, "none", new String[] {
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null,
				null, null
		});
	}
}