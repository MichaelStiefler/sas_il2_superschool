package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;

public class B_50A extends B_29X
implements TypeX4Carrier, TypeGuidedBombCarrier, TypeGuidedMissileCarrier
{
	private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	
	public B_50A()
	{
		bToFire = false;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
	}


	public GuidedMissileUtils getGuidedMissileUtils() {
	    return this.guidedMissileUtils;
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
		    this.guidedMissileUtils.update();
		    super.update(f);
		  }
		  
	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;

	static 
	{
		Class class1 = com.maddox.il2.objects.air.B_50A.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-50A");
		Property.set(class1, "meshName", "3DO/Plane/B-50A(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar1956());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1946F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-50A.fmd:B29_50");
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