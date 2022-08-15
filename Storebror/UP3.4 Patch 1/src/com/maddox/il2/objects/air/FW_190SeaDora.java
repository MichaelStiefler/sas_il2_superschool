package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombAB1000;
import com.maddox.il2.objects.weapons.BombAB500;
import com.maddox.il2.objects.weapons.BombGun4512;
import com.maddox.il2.objects.weapons.BombGunTorpF5Bheavy;
import com.maddox.il2.objects.weapons.BombGunTorpFiume;
import com.maddox.il2.objects.weapons.BombGunTorpLTF5Practice;
import com.maddox.il2.objects.weapons.BombGunTorpMk13;
import com.maddox.il2.objects.weapons.BombSB1000;
import com.maddox.il2.objects.weapons.BombSC1000;
import com.maddox.il2.objects.weapons.BombSC500;
import com.maddox.il2.objects.weapons.BombSD500;
import com.maddox.il2.objects.weapons.BombStarthilfeSolfuelL;
import com.maddox.il2.objects.weapons.BombStarthilfeSolfuelR;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RocketGunPC1000RS;
import com.maddox.il2.objects.weapons.RocketGunWfrGr21;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FW_190SeaDora extends FW_190Sea
    implements TypeFighter, TypeBNZFighter, TypeBomber
{

    public FW_190SeaDora()
    {
        kangle = 0.0F;
        booster = new Bomb[2];
        bHasBoosters = true;
        boosterFireOutTime = -1L;
    }

    public void destroy()
    {
        doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters()
    {
        Object aobj[] = this.pos.getBaseAttached();
        if(aobj != null)
        {
            int i = 0;
            do
            {
                if(i >= aobj.length)
                    break;
                if(((Maneuver)this.FM).hasRockets() || ((Maneuver)this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun))
                {
                    if(booster[0] != null && booster[1] != null)
                        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
                    Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
                    break;
                }
                i++;
            } while(true);
        }
    }

    public void doCutBoosters()
    {
        Object aobj[] = this.pos.getBaseAttached();
        if(aobj != null)
        {
            int i = 0;
            do
            {
                if(i >= aobj.length)
                    break;
                if(((Maneuver)this.FM).hasRockets() || ((Maneuver)this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun))
                {
                    for(int j = 0; j < 2; j++)
                        if(booster[j] != null)
                        {
                            booster[j].start();
                            booster[j] = null;
                        }

                    break;
                }
                i++;
            } while(true);
        }
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
                if(((Maneuver)this.FM).hasRockets() || ((Maneuver)this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun))
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
            doCutBoosters();
            this.FM.AS.setGliderBoostOff();
            bHasBoosters = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        super.update(f);
        if((this.FM instanceof Pilot) && bHasBoosters)
        {
            if(this.FM.getAltitude() > 300F && boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                bHasBoosters = false;
            }
            if(bHasBoosters && boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.9F && this.FM.EI.engines[0].getStage() == 6 && this.FM.getSpeedKMH() > 20F)
            {
                Object aobj[] = this.pos.getBaseAttached();
                if(aobj != null)
                {
                    int i = 0;
                    do
                    {
                        if(i >= aobj.length)
                            break;
                        if(((Maneuver)this.FM).hasRockets() || ((Maneuver)this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun))
                        {
                            boosterFireOutTime = Time.current() + 30000L;
                            doFireBoosters();
                            this.FM.AS.setGliderBoostOn();
                            break;
                        }
                        i++;
                    } while(true);
                } else
                {
                    doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
            if(bHasBoosters && boosterFireOutTime > 0L)
            {
                if(Time.current() < boosterFireOutTime)
                    this.FM.producedAF.x += 20000D;
                if(Time.current() > boosterFireOutTime + 10000L)
                {
                    doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
        }
        for(int k = 1; k < 13; k++)
            hierMesh().chunkSetAngles("Water" + k + "_D0", 0.0F, -10F * kangle, 0.0F);

        kangle = 0.95F * kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        super.update(f);
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
        hiermesh.chunkSetAngles("GearC99_D0", 20F * f, 0.0F, 0.0F);
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

    private float kangle;
    private Bomb booster[];
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;

    static 
    {
        Class var_class = FW_190SeaDora.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "FW190");
        Property.set(var_class, "meshName", "3DO/Plane/Fw-190D-13T/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(var_class, "yearService", 1943.11F);
        Property.set(var_class, "yearExpired", 1948F);
        Property.set(var_class, "FlightModel", "FlightModels/Fw-190D-13N.fmd");
        Property.set(var_class, "cockpitClass", new Class[] {
            CockpitFW_190D11Sea.class
        });
        Property.set(var_class, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(var_class, new int[] {
            1, 1, 0, 0, 9, 3, 9, 9, 9, 1, 
            1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 
            1, 9, 9, 2, 2, 9, 9, 1, 1
        });
        Aircraft.weaponHooksRegister(var_class, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_CANNON03", 
            "_CANNON04", "_ExternalDev05", "_ExternalDev06", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev07", "_ExternalDev08", "_CANNON09", 
            "_CANNON10", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12", "_CANNON11", "_CANNON12"
        });
    }
}
