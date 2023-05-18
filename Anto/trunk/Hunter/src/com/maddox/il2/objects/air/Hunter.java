 package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;

import java.io.IOException;

public class Hunter extends Scheme1
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit
{
	private float oldthrl;
	private float curthrl;
	private float SonicBoom = 0.0F;
	private Eff3DActor shockwave;
	private boolean isSonic;
	private float engineSurgeDamage;
	static Actor hunted = null;
    
    public Hunter()
    {
        oldctl = -1F;
        curctl = -1F;
        AirBrakeControl = 0.0F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
    }

    /**
	 * G-Force Resistance, Tolerance and Recovery parmeters. See
	 * TypeGSuit.GFactors Private fields implementation for further details.
	 */
	private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
	private static final float NEG_G_TIME_FACTOR = 1.5F;
	private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
	private static final float POS_G_TOLERANCE_FACTOR = 2.0F;
	private static final float POS_G_TIME_FACTOR = 2.0F;
	private static final float POS_G_RECOVERY_FACTOR = 2.0F;

	public void getGFactors(GFactors theGFactors) {
		theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR,
				NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR,
				POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
	}
	
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
	}
	
    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(super.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if(((FlightModelMain) (super.FM)).AP.way.isLanding() && (double)super.FM.getSpeed() > (double)((FlightModelMain) (super.FM)).VmaxFLAPS * 2D)
            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
        else
        if(((FlightModelMain) (super.FM)).AP.way.isLanding() && (double)super.FM.getSpeed() < (double)((FlightModelMain) (super.FM)).VmaxFLAPS * 1.5D)
            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public void moveCockpitDoor(float paramFloat)
	{
 	resetYPRmodifier();
	Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.95F, 0.0F, 0.9F);
	hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
	float f1 = (float) Math.sin((double) Aircraft.cvt(paramFloat, 0.4F, 0.99F, 0.0F, 3.141593F));
	this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9.0F * f1);
	this.hierMesh().chunkSetAngles("Head1_D0", 14.0F * f1, 0.0F, 0.0F);
	if(com.maddox.il2.engine.Config.isUSE_RENDER())
		{
		 if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
		com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
		setDoorSnd(paramFloat);
		}
	}
        
    
    public static void moveGear(HierMesh paramHierMesh, float paramFloat)
    {
        paramHierMesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
        if(Math.abs(paramFloat) < 0.27F)
        {
            paramHierMesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.15F, 0.26F, 0.0F, -90F), 0.0F);
            paramHierMesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.09F, 0.22F, 0.0F, -90F), 0.0F);
        } else
        {
            paramHierMesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.65F, 0.74F, -90F, 0.0F), 0.0F);
            paramHierMesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.67F, 0.78F, -90F, 0.0F), 0.0F);
        }
        paramHierMesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(paramFloat, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(paramFloat, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(paramFloat, 0.69F, 0.74F, 0.0F, -90F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(paramFloat, 0.63F, 0.99F, 0.0F, -105F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(paramFloat, 0.63F, 0.99F, 0.0F, -125F), 0.0F);
        paramHierMesh.chunkSetAngles("Gear5e_D0", 0.0F, Aircraft.cvt(paramFloat, 0.63F, 0.99F, 0.0F, -90F), 0.0F);
    }

    protected void moveGear(float paramFloat)
    {
        moveGear(hierMesh(), paramFloat);
    }

    public void moveWheelSink()
    {
        if(curctl == -1F)
        {
            curctl = oldctl = ((FlightModelMain) (super.FM)).CT.getBrake();
            H1 = 0.17F;
            ((FlightModelMain) (super.FM)).Gears.tailStiffness = 0.4F;
        } else
        {
            curctl = ((FlightModelMain) (super.FM)).CT.getBrake();
        }
        if(!super.FM.brakeShoe && ((FlightModelMain) (super.FM)).Gears.cgear)
        {
            if(curctl - oldctl < -0.02F)
                curctl = oldctl - 0.02F;
            if(curctl < 0.0F)
                curctl = 0.0F;
            float tr = 0.25F * curctl * Math.max(Aircraft.cvt(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput(), 0.5F, 0.8F, 0.0F, 1.0F), Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 80F, 0.0F, 1.0F));
            super.FM.setGC_Gear_Shift(H1 - tr);
            resetYPRmodifier();
            Aircraft.xyz[0] = -0.4F * tr;
            float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2] - Aircraft.xyz[0], 0.0F, 0.45F, 0.0F, 1.0F);
            hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearC8_D0", 0.0F, -37.5F * f, 0.0F);
            hierMesh().chunkSetAngles("GearC9_D0", 0.0F, -75F * f, 0.0F);
        }
        oldctl = curctl;
    }

    protected void moveRudder(float paramFloat)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * paramFloat, 0.0F);
        if(((FlightModelMain) (super.FM)).CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -50F * paramFloat, 0.0F);
    }
    
    protected void moveAileron(float f) {
    	this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30.0F * f, 0.0F);
    	this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30.0F * f, 0.0F);
        }

    protected void moveFlap(float paramFloat)
    {
        float f = -45F * paramFloat;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -10F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -10F * f, 0.0F);
    }

    protected void moveFan(float f1)
    {
    }

    protected void moveAirBrake(float paramFloat)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -45F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("BrakeB01_D0", 0.0F, -25F * paramFloat, 0.0F);
        if ((double) paramFloat < 0.2)
            Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.0F, 0.50F, 0.0F);
    	else
            Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.0F, -0.10F, 0.0F);
        hierMesh().chunkSetLocate("BrakeB01e_D0", Aircraft.xyz, Aircraft.ypr);
    }
    
    protected void hitBone(String string, Shot shot, Point3d point3d) {
    	if (string.startsWith("xx")) {
    	    if (string.startsWith("xxarmor")) {
    		this.debuggunnery("Armor: Hit..");
    		if (string.endsWith("p1")) {
    		    this.getEnergyPastArmor((13.350000381469727
    					     / (Math.abs(Aircraft.v1.x)
    						+ 9.999999747378752E-5)),
    					    shot);
    		    if (shot.power <= 0.0F)
    			this.doRicochetBack(shot);
    		} else if (string.endsWith("p2"))
    		    this.getEnergyPastArmor(8.77F, shot);
    		else if (string.endsWith("g1")) {
    		    this.getEnergyPastArmor
    			(((double) World.Rnd().nextFloat(40.0F, 60.0F)
    			  / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5)),
    			 shot);
    		    FM.AS.setCockpitState(shot.initiator,
    					  FM.AS.astateCockpitState | 0x2);
    		    if (shot.power <= 0.0F)
    			this.doRicochetBack(shot);
    		}
    	    } else if (string.startsWith("xxcontrols")) {
    		this.debuggunnery("Controls: Hit..");
    		int i = string.charAt(10) - 48;
    		switch (i) {
    		default:
    		    break;
    		case 1:
    		case 2:
    		    if (World.Rnd().nextFloat() < 0.5F
    			&& this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
    			this.debuggunnery
    			    ("Controls: Ailerones Controls: Out..");
    			FM.AS.setControlsDamage(shot.initiator, 0);
    		    }
    		    break;
    		case 3:
    		case 4:
    		    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F,
    								      2.93F),
    						shot) > 0.0F
    			&& World.Rnd().nextFloat() < 0.25F) {
    			this.debuggunnery
    			    ("Controls: Elevator Controls: Disabled / Strings Broken..");
    			FM.AS.setControlsDamage(shot.initiator, 1);
    		    }
    		    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F,
    								      2.93F),
    						shot) > 0.0F
    			&& World.Rnd().nextFloat() < 0.25F) {
    			this.debuggunnery
    			    ("Controls: Rudder Controls: Disabled / Strings Broken..");
    			FM.AS.setControlsDamage(shot.initiator, 2);
    		    }
    		}
    	    } else if (string.startsWith("xxeng1")) {
    		this.debuggunnery("Engine Module: Hit..");
    		if (string.endsWith("bloc"))
    		    this.getEnergyPastArmor
    			(((double) World.Rnd().nextFloat(0.0F, 60.0F)
    			  / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5)),
    			 shot);
    		if (string.endsWith("cams")
    		    && this.getEnergyPastArmor(0.45F, shot) > 0.0F
    		    && (World.Rnd().nextFloat()
    			< FM.EI.engines[0].getCylindersRatio() * 20.0F)) {
    		    FM.EI.engines[0].setCyliderKnockOut
    			(shot.initiator,
    			 World.Rnd().nextInt(1, (int) (shot.power / 4800.0F)));
    		    this.debuggunnery("Engine Module: Engine Cams Hit, "
    				      + FM.EI.engines[0].getCylindersOperable()
    				      + "/" + FM.EI.engines[0].getCylinders()
    				      + " Left..");
    		    if (World.Rnd().nextFloat() < shot.power / 24000.0F) {
    			FM.AS.hitEngine(shot.initiator, 0, 2);
    			this.debuggunnery
    			    ("Engine Module: Engine Cams Hit - Engine Fires..");
    		    }
    		    if (shot.powerType == 3
    			&& World.Rnd().nextFloat() < 0.75F) {
    			FM.AS.hitEngine(shot.initiator, 0, 1);
    			this.debuggunnery
    			    ("Engine Module: Engine Cams Hit (2) - Engine Fires..");
    		    }
    		}
    		if (string.endsWith("eqpt")
    		    && World.Rnd().nextFloat() < shot.power / 24000.0F) {
    		    FM.AS.hitEngine(shot.initiator, 0, 3);
    		    this.debuggunnery("Engine Module: Hit - Engine Fires..");
    		}
    		if (string.endsWith("exht")) {
    		    /* empty */
    		}
    	    } else if (string.startsWith("xxmgun0")) {
    		int i = string.charAt(7) - 49;
    		if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
    		    this.debuggunnery("Armament: Machine Gun (" + i
    				      + ") Disabled..");
    		    FM.AS.setJamBullets(0, i);
    		    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F,
    								  23.325F),
    					    shot);
    		}
    	    } else if (string.startsWith("xxtank")) {
    		int i = string.charAt(6) - 49;
    		if (this.getEnergyPastArmor(0.1F, shot) > 0.0F
    		    && World.Rnd().nextFloat() < 0.25F) {
    		    if (FM.AS.astateTankStates[i] == 0) {
    			this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
    			FM.AS.hitTank(shot.initiator, i, 1);
    			FM.AS.doSetTankState(shot.initiator, i, 1);
    		    }
    		    if (shot.powerType == 3
    			&& World.Rnd().nextFloat() < 0.075F) {
    			FM.AS.hitTank(shot.initiator, i, 2);
    			this.debuggunnery("Fuel Tank (" + i + "): Hit..");
    		    }
    		}
    	    } else if (string.startsWith("xxspar")) {
    		this.debuggunnery("Spar Construction: Hit..");
    		if (string.startsWith("xxsparlm")
    		    && this.chunkDamageVisible("WingLMid") > 2
    		    && this.getEnergyPastArmor((16.5F
    						* World.Rnd().nextFloat(1.0F,
    									1.5F)),
    					       shot) > 0.0F) {
    		    this.debuggunnery
    			("Spar Construction: WingLMid Spars Damaged..");
    		    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
    		}
    		if (string.startsWith("xxsparrm")
    		    && this.chunkDamageVisible("WingRMid") > 2
    		    && this.getEnergyPastArmor((16.5F
    						* World.Rnd().nextFloat(1.0F,
    									1.5F)),
    					       shot) > 0.0F) {
    		    this.debuggunnery
    			("Spar Construction: WingRMid Spars Damaged..");
    		    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
    		}
    		if (string.startsWith("xxsparlo")
    		    && this.chunkDamageVisible("WingLOut") > 2
    		    && this.getEnergyPastArmor((16.5F
    						* World.Rnd().nextFloat(1.0F,
    									1.5F)),
    					       shot) > 0.0F) {
    		    this.debuggunnery
    			("Spar Construction: WingLOut Spars Damaged..");
    		    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
    		}
    		if (string.startsWith("xxsparro")
    		    && this.chunkDamageVisible("WingROut") > 2
    		    && this.getEnergyPastArmor((16.5F
    						* World.Rnd().nextFloat(1.0F,
    									1.5F)),
    					       shot) > 0.0F) {
    		    this.debuggunnery
    			("Spar Construction: WingROut Spars Damaged..");
    		    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
    		}
    	    } else if (string.startsWith("xxhyd"))
    		FM.AS.setInternalDamage(shot.initiator, 3);
    	    else if (string.startsWith("xxpnm"))
    		FM.AS.setInternalDamage(shot.initiator, 1);
    	} else {
    	    if (string.startsWith("xcockpit")) {
    		FM.AS.setCockpitState(shot.initiator,
    				      FM.AS.astateCockpitState | 0x1);
    		this.getEnergyPastArmor(0.05F, shot);
    	    }
    	    if (string.startsWith("xcf"))
    		this.hitChunk("CF", shot);
    	    else if (string.startsWith("xnose"))
    		this.hitChunk("Nose", shot);
    	    else if (string.startsWith("xtail")) {
    		if (this.chunkDamageVisible("Tail1") < 3)
    		    this.hitChunk("Tail1", shot);
    	    } else if (string.startsWith("xkeel")) {
    		if (this.chunkDamageVisible("Keel1") < 2)
    		    this.hitChunk("Keel1", shot);
    	    } else if (string.startsWith("xrudder"))
    		this.hitChunk("Rudder1", shot);
    	    else if (string.startsWith("xstab")) {
    		if (string.startsWith("xstabl")
    		    && this.chunkDamageVisible("StabL") < 2)
    		    this.hitChunk("StabL", shot);
    		if (string.startsWith("xstabr")
    		    && this.chunkDamageVisible("StabR") < 1)
    		    this.hitChunk("StabR", shot);
    	    } else if (string.startsWith("xvator")) {
    		if (string.startsWith("xvatorl"))
    		    this.hitChunk("VatorL", shot);
    		if (string.startsWith("xvatorr"))
    		    this.hitChunk("VatorR", shot);
    	    } else if (string.startsWith("xwing")) {
    		if (string.startsWith("xwinglin")
    		    && this.chunkDamageVisible("WingLIn") < 3)
    		    this.hitChunk("WingLIn", shot);
    		if (string.startsWith("xwingrin")
    		    && this.chunkDamageVisible("WingRIn") < 3)
    		    this.hitChunk("WingRIn", shot);
    		if (string.startsWith("xwinglmid")
    		    && this.chunkDamageVisible("WingLMid") < 3)
    		    this.hitChunk("WingLMid", shot);
    		if (string.startsWith("xwingrmid")
    		    && this.chunkDamageVisible("WingRMid") < 3)
    		    this.hitChunk("WingRMid", shot);
    		if (string.startsWith("xwinglout")
    		    && this.chunkDamageVisible("WingLOut") < 3)
    		    this.hitChunk("WingLOut", shot);
    		if (string.startsWith("xwingrout")
    		    && this.chunkDamageVisible("WingROut") < 3)
    		    this.hitChunk("WingROut", shot);
    	    } else if (string.startsWith("xarone")) {
    		if (string.startsWith("xaronel"))
    		    this.hitChunk("AroneL", shot);
    		if (string.startsWith("xaroner"))
    		    this.hitChunk("AroneR", shot);
    	    } else if (string.startsWith("xgear")) {
    		if (string.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
    		    this.debuggunnery("Hydro System: Disabled..");
    		    FM.AS.setInternalDamage(shot.initiator, 0);
    		}
    		if (string.endsWith("2") && World.Rnd().nextFloat() < 0.1F
    		    && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F,
    								     3.435F),
    					       shot) > 0.0F) {
    		    this.debuggunnery("Undercarriage: Stuck..");
    		    FM.AS.setInternalDamage(shot.initiator, 3);
    		}
    	    } else if (string.startsWith("xpilot")
    		       || string.startsWith("xhead")) {
    		int i = 0;
    		int i_3_;
    		if (string.endsWith("a")) {
    		    i = 1;
    		    i_3_ = string.charAt(6) - 49;
    		} else if (string.endsWith("b")) {
    		    i = 2;
    		    i_3_ = string.charAt(6) - 49;
    		} else
    		    i_3_ = string.charAt(5) - 49;
    		this.hitFlesh(i_3_, shot, i);
    	    }
    	}
        }
        
    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }
            
	public void typeFighterAceMakerRangeFinder() {
		if (k14Mode == 2)
			return;
		hunted = Main3D.cur3D().getViewPadlockEnemy();
		if (hunted == null) {
			k14Distance = 200F;
			hunted = War.GetNearestEnemyAircraft(FM.actor, 2700F, 9);
		}
		if (hunted != null) {
			k14Distance = (float) FM.actor.pos.getAbsPoint().distance(
					hunted.pos.getAbsPoint());
			if (k14Distance > 800F)
				k14Distance = 800F;
			else if (k14Distance < 200F)
				k14Distance = 200F;
		}
	}

	public float getAirPressure(float theAltitude) {
		float fBase = 1F - (L * theAltitude / T0);
		float fExponent = (G_CONST * M) / (R * L);
		return p0 * (float) Math.pow(fBase, fExponent);
	}

	public float getAirPressureFactor(float theAltitude) {
		return getAirPressure(theAltitude) / p0;
	}

	public float getAirDensity(float theAltitude) {
		return (getAirPressure(theAltitude) * M)
				/ (R * (T0 - (L * theAltitude)));
	}

	public float getAirDensityFactor(float theAltitude) {
		return getAirDensity(theAltitude) / Rho0;
	}

	public float getMachForAlt(float theAltValue) {
		theAltValue /= 1000F; // get altitude in km
		int i = 0;
		for (i = 0; i < fMachAltX.length; i++) {
			if (fMachAltX[i] > theAltValue)
				break;
		}
		if (i == 0)
			return fMachAltY[0];
		float baseMach = fMachAltY[i - 1];
		float spanMach = fMachAltY[i] - baseMach;
		float baseAlt = fMachAltX[i - 1];
		float spanAlt = fMachAltX[i] - baseAlt;
		float spanMult = (theAltValue - baseAlt) / spanAlt;
		return baseMach + (spanMach * spanMult);
	}

	public float getMpsFromKmh(float kilometersPerHour) {
		return kilometersPerHour / 3.6F;
	}

	public float calculateMach() {
		return (FM.getSpeedKMH() / getMachForAlt(FM.getAltitude()));

	}

	public void soundbarier() {

		float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
		if (f < 0.5F)
			f = 0.5F;
		float f_0_ = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
		if (f_0_ < 0.5F)
			f_0_ = 0.5F;
		if (calculateMach() <= 1.0) {
			FM.VmaxAllowed = FM.getSpeedKMH() + f;
			SonicBoom = 0.0F;
			isSonic = false;
		}
		if (calculateMach() >= 1.0) {
			FM.VmaxAllowed = FM.getSpeedKMH() + f_0_;
			isSonic = true;
		}
		if (FM.VmaxAllowed > 1500.0F)
			FM.VmaxAllowed = 1500.0F;

		if (isSonic && SonicBoom < 1) {
			super.playSound("aircraft.SonicBoom", true);
			super.playSound("aircraft.SonicBoomInternal", true);
			if (FM.actor == World.getPlayerAircraft())
				HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
			if (Config.isUSE_RENDER()
					&& World.Rnd().nextFloat() < getAirDensityFactor(FM
							.getAltitude())) {
				shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null,
						1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
			}
			SonicBoom = 1;
		}
		if (calculateMach() > 1.01 || calculateMach() < 1.0)
			Eff3DActor.finish(shockwave);
	}

	public void engineSurge(float f) {
		if (((FlightModelMain) (super.FM)).AS.isMaster())
			if (curthrl == -1F) {
				curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0]
						.getControlThrottle();
			} else {
				curthrl = ((FlightModelMain) (super.FM)).EI.engines[0]
						.getControlThrottle();
				if (curthrl < 1.05F) {
					if ((curthrl - oldthrl) / f > 20.0F
							&& FM.EI.engines[0].getRPM() < 3200.0F
							&& FM.EI.engines[0].getStage() == 6
							&& World.Rnd().nextFloat() < 0.40F) {
						if (FM.actor == World.getPlayerAircraft())
							HUD.log(AircraftHotKeys.hudLogWeaponId,
									"Compressor Stall!");
						super.playSound("weapon.MGunMk108s", true);
						engineSurgeDamage += 1.0000000000000001E-002D * (double) (((FlightModelMain) (super.FM)).EI.engines[0]
								.getRPM() / 1000F);
						((FlightModelMain) (super.FM)).EI.engines[0]
								.doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0]
										.getReadyness()
										- engineSurgeDamage);
						if (World.Rnd().nextFloat() < 0.05F
								&& FM instanceof RealFlightModel
								&& ((RealFlightModel) FM).isRealMode()) {
							FM.AS.hitEngine(this, 0, 100);
						}
						if (World.Rnd().nextFloat() < 0.05F
								&& FM instanceof RealFlightModel
								&& ((RealFlightModel) FM).isRealMode()) {
							FM.EI.engines[0].setEngineDies(this);
						}
					}
					if ((curthrl - oldthrl) / f < -20.0F
							&& (curthrl - oldthrl) / f > -100.0F
							&& FM.EI.engines[0].getRPM() < 3200.0F
							&& FM.EI.engines[0].getStage() == 6) {
						super.playSound("weapon.MGunMk108s", true);
						engineSurgeDamage += 1.0000000000000001E-003D * (double) (((FlightModelMain) (super.FM)).EI.engines[0]
								.getRPM() / 1000F);
						((FlightModelMain) (super.FM)).EI.engines[0]
								.doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0]
										.getReadyness()
										- engineSurgeDamage);
						if (World.Rnd().nextFloat() < 0.40F
								&& FM instanceof RealFlightModel
								&& ((RealFlightModel) FM).isRealMode()) {
							if (FM.actor == World.getPlayerAircraft())
								HUD.log(AircraftHotKeys.hudLogWeaponId,
										"Engine Flameout!");
							FM.EI.engines[0].setEngineStops(this);
						} else {
							if (FM.actor == World.getPlayerAircraft())
								HUD.log(AircraftHotKeys.hudLogWeaponId,
										"Compressor Stall!");
						}
					}
				}
				oldthrl = curthrl;
			}
	}
    	
        public void update(float f) {
        	if (FM.AS.isMaster() && Config.isUSE_RENDER()) {
        	    if (FM.EI.engines[0].getPowerOutput() > 0.45F
        		&& FM.EI.engines[0].getStage() == 6) {
        		if (FM.EI.engines[0].getPowerOutput() > 0.65F)
        		    FM.AS.setSootState(this, 0, 3);
        		else
        		    FM.AS.setSootState(this, 0, 2);
        	    } else
        		FM.AS.setSootState(this, 0, 0);
        	}
        	super.update(f);
    		engineSurge(f);
    		typeFighterAceMakerRangeFinder();
    		soundbarier();
        	if (FM.AS.isMaster()) {
        	    if (curctl == -1.0F)
        		curctl = oldctl = FM.EI.engines[0].getControlThrottle();
        	    else {
        		curctl = FM.EI.engines[0].getControlThrottle();
        		oldctl = curctl;
        	    }
        	}
           	 if(FM.CT.getGear() > 0.01F){
           	 		FM.CT.AirBrakeControl = 0.0F;
           	 		World.cur();
           	 	}
        	if (FM.getSpeedKMH() > 500.0F && FM.CT.bHasFlapsControl) {
        	    FM.CT.FlapsControl = 0.0F;
        	    FM.CT.bHasFlapsControl = false;
        	} else
        	    FM.CT.bHasFlapsControl = true;
        }
        
        public void doEjectCatapult() {
        	new MsgAction(false, this) {
        	    public void doAction(Object paramObject) {
        		Aircraft localAircraft = (Aircraft) paramObject;
        		if (Actor.isValid(localAircraft)) {
        		    Loc localLoc1 = new Loc();
        		    Loc localLoc2 = new Loc();
        		    Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
        		    HookNamed localHookNamed
        			= new HookNamed(localAircraft, "_ExternalSeat01");
        		    localAircraft.pos.getAbs(localLoc2);
        		    localHookNamed.computePos(localAircraft, localLoc2,
        					      localLoc1);
        		    localLoc1.transform(localVector3d);
        		    localVector3d.x += localAircraft.FM.Vwld.x;
        		    localVector3d.y += localAircraft.FM.Vwld.y;
        		    localVector3d.z += localAircraft.FM.Vwld.z;
        		    new EjectionSeat(1, localLoc1, localVector3d,
        				     localAircraft);
        		}
        	    }
        	};
        	this.hierMesh().chunkVisible("Seat_D0", false);
            }

    private float H1;
    private float oldctl;
    private float curctl;
    public float AirBrakeControl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Hunter.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}