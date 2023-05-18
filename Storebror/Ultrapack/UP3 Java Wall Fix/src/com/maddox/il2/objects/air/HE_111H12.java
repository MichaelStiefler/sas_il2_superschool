package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class HE_111H12 extends HE_111
    implements TypeX4Carrier, TypeGuidedBombCarrier
{

    public HE_111H12()
    {
        bToFire = false;
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        isGuidingBomb = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.endsWith("X"))
            hierMesh().chunkVisible("Bombsight_D0", true);
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
            break;

        case 2:
            FM.turret[1].setHealth(f);
            FM.turret[2].setHealth(f);
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3)
            {
                if(World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitTank(this, 0, 1);
                if(World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitTank(this, 1, 1);
            }
            if(FM.AS.astateEngineStates[1] > 3)
            {
                if(World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitTank(this, 2, 1);
                if(World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitTank(this, 3, 1);
            }
            if(FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.125F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.125F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

        mydebug("========================== isGuidingBomb = " + isGuidingBomb);
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
        mydebug("Chimata typeX4CAdjSidePlus, deltaAzimuth = " + deltaAzimuth);
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.002F;
        mydebug("Chimata typeX4CAdjSideMinus, deltaAzimuth = " + deltaAzimuth);
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.002F;
        mydebug("Chimata typeX4CAdjAttitudePlus, deltaTangage = " + deltaTangage);
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.002F;
        mydebug("Chimata typeX4CAdjAttitudeMinus, deltaTangage = " + deltaTangage);
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

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 85F)
            fSightCurForwardAngle = 85F;
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        if(!isGuidingBomb)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
                new Integer((int)fSightCurForwardAngle)
            });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        if(!isGuidingBomb)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
                new Integer((int)fSightCurForwardAngle)
            });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(!isGuidingBomb)
        {
            fSightCurSideslip += 0.1F;
            if(fSightCurSideslip > 3F)
                fSightCurSideslip = 3F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
                new Integer((int)(fSightCurSideslip * 10F))
            });
        }
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(!isGuidingBomb)
        {
            fSightCurSideslip -= 0.1F;
            if(fSightCurSideslip < -3F)
                fSightCurSideslip = -3F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
                new Integer((int)(fSightCurSideslip * 10F))
            });
        }
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(!isGuidingBomb)
        {
            fSightCurAltitude += 10F;
            if(fSightCurAltitude > 10000F)
                fSightCurAltitude = 10000F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
                new Integer((int)fSightCurAltitude)
            });
            fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        }
    }

    public void typeBomberAdjAltitudeMinus()
    {
        if(!isGuidingBomb)
        {
            fSightCurAltitude -= 10F;
            if(fSightCurAltitude < 850F)
                fSightCurAltitude = 850F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
                new Integer((int)fSightCurAltitude)
            });
            fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.equals("xxarmorg1"))
            getEnergyPastArmor(5F, shot);
        else
            super.hitBone(s, shot, point3d);
    }

    protected void mydebug(String s)
    {
    }

    public boolean bToFire;
    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;

    static 
    {
        Class class1 = HE_111H12.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-12/hier_h12.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-12.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitHE_111H12.class, CockpitHE_111H12_Bombardier.class, CockpitHE_111H6_NGunner.class, CockpitHE_111H12_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
