
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Time;
import com.maddox.sound.*;


public class Mi8_24HeliFamily extends Scheme2
    implements TypeHelicopter
{

    public Mi8_24HeliFamily()
    {
        rotorRPM = 0.0F;
        curAngleRotor = 0.0F;
        lastTimeFan = -1L;
        oMainRotor = new Orient();
        vThrust = new Vector3f();
        iMainTorque = 0.0F;
        MainRotorStatus = 1.0F;
        TailRotorStatus = 1.0F;
        isGeneratorAlive = false;
        obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
        carrierSpawn = false;
        carrierSpawnLevel = 0;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!bObserverKilled)
            if(obsLookTime == 0)
            {
                obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
                obsMove = 0.0F;
                obsAzimuthOld = obsAzimuth;
                obsElevationOld = obsElevation;
                if(World.Rnd().nextFloat() > 0.80F)
                {
                    obsAzimuth = 0.0F;
                    obsElevation = 0.0F;
                } else
                {
                    obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                    obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                }
            } else
            {
                obsLookTime--;
            }

    }

    protected void moveElevator(float f)
    {
    }

    protected void moveAileron(float f)
    {
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    protected void moveFan(float f)
    {
        float tRPM = Math.max(FM.EI.engines[0].getRPM(), FM.EI.engines[1].getRPM());
        tRPM *= 0.061F;
        float aThrottle = (FM.EI.engines[0].getControlThrottle() + FM.EI.engines[1].getControlThrottle()) / 2.0F;
        float aThrust = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput()) / 2.0F;
        carrierSpawn = rotorRPM == 0F && FM.brakeShoe && FM.Gears.isUnderDeck() && aThrottle < 0.105F;
        if(carrierSpawn)
            carrierSpawnLevel = 10;
        else if(carrierSpawnLevel > 0)
            carrierSpawnLevel--;

        if(carrierSpawn || aThrust * aThrust < 0.1F || tRPM < 0.9F)
            tRPM = 0.0F;
        if(tRPM == 0.0F)
        {
            if(rotorRPM > 30F)
                rotorRPM *= 0.984F;
            else if(rotorRPM > 0.0F)
                rotorRPM -= 0.08F;
        }
        else
            rotorRPM += (tRPM - rotorRPM) * (0.0001F + carrierSpawnLevel * 0.0005F);
        if(rotorRPM < 0.0F)
            rotorRPM = 0.0F;
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        else
        {
            if(aThrust * aThrottle > 0.25F && rotorRPM > 10.0F)
            {
                hierMesh().chunkVisible("Prop1_D0", false);
                hierMesh().chunkVisible("PropRot1_D0", true);
            } else
            {
                hierMesh().chunkVisible("Prop1_D0", true);
                hierMesh().chunkVisible("PropRot1_D0", false);
            }
        }
        if(hierMesh().isChunkVisible("Prop2_D1"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        else
        {
            if(aThrust * aThrottle > 0.25F && rotorRPM > 10.0F)
            {
                hierMesh().chunkVisible("Prop2_D0", false);
                hierMesh().chunkVisible("PropRot2_D0", true);
            } else
            {
                hierMesh().chunkVisible("Prop2_D0", true);
                hierMesh().chunkVisible("PropRot2_D0", false);
            }
        }
        if(hierMesh().isChunkVisible("Tail1_CAP") || hierMesh().isChunkVisible("Keel1_CAP"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
            hierMesh().chunkVisible("Prop2_D1", false);
        }
        diffAngleRotor = (6F * rotorRPM * (float)(Time.current() - lastTimeFan)) / 1000F;
        curAngleRotor += diffAngleRotor;
        lastTimeFan = Time.current();
        hierMesh().chunkSetAngles("Prop1_D0", -curAngleRotor % 360F, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, -(curAngleRotor * 5.86F) % 360F);
        isGeneratorAlive = (rotorRPM > 10F || (carrierSpawn && aThrust > 0.33F));
    }

    private void tiltRotor(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -oMainRotor.getTangage() / 10F);
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -oMainRotor.getKren() / 10F, 0.0F);
    }

    public void update(float f)
    {
        tiltRotor(f);
        if(obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
        {
            if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                obsMove += 0.30F * f;
            else
            if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                obsMove += 0.15F;
            else
                obsMove += 1.2F * f;
            obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
            obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
            hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
        }
        super.update(f);
        float kren = FM.CT.getAileron() + FM.CT.getTrimAileronControl();
        kren *= 35F;
        float tang = FM.CT.getElevator() + FM.CT.getTrimElevatorControl();
        tang -= 0.05F;
        tang *= tang >= 0.0F ? 35F : 50F;
        vThrust.set(0.0F, 0.0F, 1.0F);
        oMainRotor.set(0.0F, tang, kren);
        oMainRotor.transform(vThrust);
        if((vThrust.x > 0F && FM.getSpeed() > 0F) || (vThrust.x < 0F && FM.getSpeed() < 0F))
        {
            if(Math.abs(FM.getSpeed()) < FM.Vmax * 1.1F)
                vThrust.x *= cvt(Math.abs(FM.getSpeed()) / FM.Vmax, 0.85F, 1.1F, 1.0F, 0.6F);
            else
                vThrust.x *= cvt(Math.abs(FM.getSpeed()) / FM.Vmax, 1.1F, 1.3F, 0.6F, 0.0F);
        }
        if(FM.getSpeed() > 0F)
            vThrust.x -= cvt(Math.abs(FM.getSpeed()) / FM.Vmax, 0.9F, 1.2F, 0.0F, 0.45F);
        else
            vThrust.x += cvt(Math.abs(FM.getSpeed()) / FM.Vmax, 0.9F, 1.2F, 0.0F, 0.45F);
        vThrust.normalize();
        FM.EI.engines[0].setVector(vThrust);
        FM.EI.engines[1].setVector(vThrust);
        float aThrottle = (FM.EI.engines[0].getControlThrottle() + FM.EI.engines[1].getControlThrottle()) / 2.0F;
        float aThrust = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput()) / 2.0F;
        super.FM.getW().scale(0.75F * aThrottle);
        float MainTorque = FM.CT.getRudder() + FM.CT.getTrimRudderControl();
        MainTorque *= FM.Gears.nOfGearsOnGr > 1 ? 0.3F : 1.5F;
        MainTorque *= aThrottle * aThrust;
        iMainTorque += (MainTorque - iMainTorque) * 0.05F;
        FM.Or.increment(iMainTorque, 0.0F, 0.0F);
        float fStab = FM.getAltitude() <= 2300F ? FM.CT.getAirBrake() : 0.0F;
        if(FM.CT.getAirBrake() > 0.5F)
            super.FM.setGCenter(0.1F - fStab * 25F);
        if(this == World.getPlayerAircraft() && super.FM.turret.length > 0 && FM.AS.astatePilotStates[1] < 90 && super.FM.turret[0].bIsAIControlled && (FM.getOverload() > 3F || FM.getOverload() < -1.5F))
            Voice.speakRearGunShake();
        if(hierMesh().isChunkVisible("Tail1_CAP") || hierMesh().isChunkVisible("Keel1_CAP")
           || hierMesh().isChunkVisible("Prop2_D1") || FM.CT.getAirBrake() > 0.5F)
            FM.Or.increment(aThrottle * aThrust * 5F, 0.0F, 0.0F);
    }

    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    protected boolean bObserverKilled;
    protected boolean isGeneratorAlive;
    private float rotorRPM;
    private float curAngleRotor;
    private float diffAngleRotor;
    private long lastTimeFan;
    private Orient oMainRotor;
    private Vector3f vThrust;
    private float iMainTorque;
    private float MainRotorStatus;
    private float TailRotorStatus;
    private boolean carrierSpawn;
    private int carrierSpawnLevel;

}