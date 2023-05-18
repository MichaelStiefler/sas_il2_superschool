package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;

public class B_29T extends B_29X
implements TypeX4Carrier, TypeGuidedBombCarrier, TypeGuidedMissileCarrier
{
	private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	
	public B_29T()
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
		Class class1 = com.maddox.il2.objects.air.B_29T.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-29 Tarzon");
        Property.set(class1, "meshName", "3DO/Plane/B-29T(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar1956());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1946F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-29.fmd:B29_50");
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
		weaponsRegister(class1, "1xTarzon", new String[] {
				"MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null,
				"RocketGunTarzon 1", null
		});
		weaponsRegister(class1, "none", new String[] {
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null,
				null, null
		});
	}
}