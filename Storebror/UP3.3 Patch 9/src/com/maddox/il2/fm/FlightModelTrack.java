package com.maddox.il2.fm;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelOutStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Spawn;

public class FlightModelTrack extends NetObj
    implements NetUpdate
{
    static class SPAWN
        implements NetSpawn
    {

        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            try
            {
                NetObj netobj = netmsginput.readNetObj();
                Aircraft aircraft = (Aircraft)netobj.superObj();
                FlightModelTrack flightmodeltrack = new FlightModelTrack(aircraft, netmsginput.channel(), i);
                if(netmsginput.available() > 0)
                    flightmodeltrack.netInput(netmsginput);
            }
            catch(Exception exception)
            {
                FlightModelTrack.printDebug(exception);
            }
        }

        SPAWN()
        {
        }
    }


    public void setCockpitAzimuthSpeed(float f)
    {
        cockpitAzimuthSpeed = f;
    }

    public float getCockpitAzimuthSpeed()
    {
        return cockpitAzimuthSpeed;
    }

    public void FMupdate(FlightModel flightmodel)
    {
        flightmodel.Or.wrap();
        if(World.cur().diffCur.Wind_N_Turbulence)
            World.wind().getVector(flightmodel.Loc, flightmodel.Vwind);
        else
            flightmodel.Vwind.set(0.0D, 0.0D, 0.0D);
        flightmodel.Vair.sub(flightmodel.Vwld, flightmodel.Vwind);
        flightmodel.Or.transformInv(flightmodel.Vair, flightmodel.Vflow);
        flightmodel.AOS = FMMath.RAD2DEG((float)Math.atan2(flightmodel.Vflow.y, FMMath.hypot(flightmodel.Vflow.x, flightmodel.Vflow.z)));
    }

    private void codeByteSet(float f, float f1, float f2)
    {
        codeByteMIN = f;
        codeByteMAX = f1;
        codeByteMUL = f2;
    }

    private byte codeByte(float f)
    {
        if(f < codeByteMIN)
            f = codeByteMIN;
        if(f > codeByteMAX)
            f = codeByteMAX;
        return (byte)((int)(codeByteMUL * f) & 0xff);
    }

    private float decodeByte(byte byte0)
    {
        float f = byte0;
        return f / codeByteMUL;
    }

    private void checkByte(int i, float f, byte byte0, float af[])
    {
        if(bFirstUpdate)
        {
            flags |= i;
            return;
        }
        codeByteSet(af[0], af[1], af[2]);
        byte byte1 = codeByte(f);
        if(byte1 != byte0)
            flags |= i;
    }

    private void checkMByte(int i, int j, float f, byte byte0, float af[])
    {
        if(bFirstUpdate)
        {
            m[i] |= j;
            return;
        }
        codeByteSet(af[0], af[1], af[2]);
        byte byte1 = codeByte(f);
        if(byte1 != byte0)
        {
            flags |= 1 << 14 + i;
            m[i] |= j;
        }
    }

    private byte writeByte(int i, float f, byte byte0, float af[])
        throws IOException
    {
        return writeByte(i, f, byte0, false, af);
    }

    private byte writeByte(int i, float f, byte byte0, boolean flag, float af[])
        throws IOException
    {
        if((flags & i) == 0)
            return byte0;
        codeByteSet(af[0], af[1], af[2]);
        byte byte1 = codeByte(f);
        msgOut.writeByte(byte1);
        if(flag)
            System.out.println("writeByte " + byte1 + " " + f);
        return byte1;
    }

    private byte writeMByte(int i, int j, float f, byte byte0, float af[])
        throws IOException
    {
        return writeMByte(i, j, f, byte0, false, af);
    }

    private byte writeMByte(int i, int j, float f, byte byte0, boolean flag, float af[])
        throws IOException
    {
        if((m[i] & j) == 0)
            return byte0;
        codeByteSet(af[0], af[1], af[2]);
        byte byte1 = codeByte(f);
        msgOut.writeByte(byte1);
        if(flag)
            System.out.println("writeMByte " + byte1 + " " + f);
        return byte1;
    }

    private float readByte(int i, float f, float af[])
        throws IOException
    {
        return readByte(i, f, false, af);
    }

    private float readByte(int i, float f, boolean flag, float af[])
        throws IOException
    {
        if((flags & i) == 0)
            return f;
        codeByteSet(af[0], af[1], af[2]);
        byte byte0 = msgIn.readByte();
        float f1 = decodeByte(byte0);
        if(flag)
            System.out.println("readByte " + byte0 + " " + f + " " + f1);
        return f1;
    }

    private float readMByte(int i, int j, float f, float af[])
        throws IOException
    {
        return readMByte(i, j, f, false, af);
    }

    private float readMByte(int i, int j, float f, boolean flag, float af[])
        throws IOException
    {
        if((m[i] & j) == 0)
            return f;
        codeByteSet(af[0], af[1], af[2]);
        byte byte0 = msgIn.readByte();
        float f1 = decodeByte(byte0);
        if(flag)
            System.out.println("readMByte " + byte0 + " " + f + " " + f1);
        return f1;
    }

    private void codeShortSet(float f, float f1, float f2)
    {
        codeShortMIN = f;
        codeShortMAX = f1;
        codeShortMUL = f2;
    }

    private short codeShort(float f)
    {
        if(f < codeShortMIN)
            f = codeShortMIN;
        if(f > codeShortMAX)
            f = codeShortMAX;
        return (short)((int)(codeShortMUL * f) & 0xffff);
    }

    private float decodeShort(short word0)
    {
        float f = word0;
        return f / codeShortMUL;
    }

    private void checkShort(int i, float f, short word0, float af[])
    {
        if(bFirstUpdate)
        {
            flags |= i;
            return;
        }
        codeShortSet(af[0], af[1], af[2]);
        short word1 = codeShort(f);
        if(word1 != word0)
            flags |= i;
    }

    private void checkMShort(int i, int j, float f, short word0, float af[])
    {
        if(bFirstUpdate)
        {
            m[i] |= j;
            return;
        }
        codeShortSet(af[0], af[1], af[2]);
        short word1 = codeShort(f);
        if(word1 != word0)
        {
            flags |= 1 << 14 + i;
            m[i] |= j;
        }
    }

    private short writeShort(int i, float f, short word0, float af[])
        throws IOException
    {
        return writeShort(i, f, word0, false, af);
    }

    private short writeShort(int i, float f, short word0, boolean flag, float af[])
        throws IOException
    {
        if((flags & i) == 0)
            return word0;
        codeShortSet(af[0], af[1], af[2]);
        short word1 = codeShort(f);
        msgOut.writeShort(word1);
        if(flag)
            System.out.println("writeShort" + word1 + " " + f);
        return word1;
    }

    private short writeMShort(int i, int j, float f, short word0, float af[])
        throws IOException
    {
        if((m[i] & j) == 0)
        {
            return word0;
        } else
        {
            codeShortSet(af[0], af[1], af[2]);
            short word1 = codeShort(f);
            msgOut.writeShort(word1);
            return word1;
        }
    }

    private float readShort(int i, float f, float af[])
        throws IOException
    {
        return readShort(i, f, false, af);
    }

    private float readShort(int i, float f, boolean flag, float af[])
        throws IOException
    {
        if((flags & i) == 0)
            return f;
        codeShortSet(af[0], af[1], af[2]);
        short word0 = msgIn.readShort();
        float f1 = decodeShort(word0);
        if(flag)
            System.out.println("readShort" + word0 + " " + f + " " + f1);
        return f1;
    }

    private float readMShort(int i, int j, float f, float af[])
        throws IOException
    {
        if((m[i] & j) == 0)
        {
            return f;
        } else
        {
            codeShortSet(af[0], af[1], af[2]);
            return decodeShort(msgIn.readShort());
        }
    }

    public void netUpdate()
    {
        if(!Actor.isValid(air))
        {
            destroy();
            return;
        }
        if((isMirror() || channel == null) && !bFirstUpdate)
            return;
        FlightModel flightmodel = air.FM;
        RealFlightModel realflightmodel = null;
        if(flightmodel instanceof RealFlightModel)
            realflightmodel = (RealFlightModel)flightmodel;
        flags = 0;
        bits = 0;
        weapons = 0;
        checkByte(1, flightmodel.CT.GearControl, ct_GearControl, _ct_GearControl);
        checkByte(2, flightmodel.CT.FlapsControl, ct_FlapsControl, _ct_FlapsControl);
        checkByte(4, flightmodel.CT.BrakeControl, ct_BrakeControl, _ct_BrakeControl);
        checkByte(8, flightmodel.CT.AileronControl, ct_AileronControl, _ct_AileronControl);
        checkByte(16, flightmodel.CT.ElevatorControl, ct_ElevatorControl, _ct_ElevatorControl);
        checkByte(32, flightmodel.CT.RudderControl, ct_RudderControl, _ct_RudderControl);
        checkByte(64, flightmodel.CT.getTrimAileronControl(), ct_TrimHorControl, _ct_TrimHorControl);
        checkByte(128, flightmodel.CT.getTrimElevatorControl(), ct_TrimVerControl, _ct_TrimVerControl);
        checkByte(256, flightmodel.CT.getTrimRudderControl(), ct_TrimRudControl, _ct_TrimRudControl);
        checkByte(0x20000000, flightmodel.CT.Sensitivity, ct_Sensitivity, _ct_Sensitivity);
        checkShort(512, flightmodel.M.fuel, mass_fuel1, _mass_fuel1);
        checkByte(1024, cockpitAzimuthSpeed, cockpit_AzimuthSpeed, _cockpit_AzimuthSpeed);
        if(realflightmodel != null)
        {
            checkShort(2048, realflightmodel.saveDeep, fm_saveDeep, _fm_saveDeep);
            checkByte(4096, realflightmodel.shakeLevel, fm_shakeLevel, _fm_shakeLevel);
        }
        checkByte(8192, (float)flightmodel.AM.x, fm_AMx, _fm_AMx);
        for(int i = 0; i < flightmodel.EI.getNum(); i++)
        {
            m[i] = 0;
            checkMByte(i, 1, flightmodel.EI.engines[i].tOilIn, ei_tOilIn[i], _ei_tOilIn);
            checkMByte(i, 2, flightmodel.EI.engines[i].tOilOut, ei_tOilOut[i], _ei_tOilOut);
            checkMByte(i, 4, flightmodel.EI.engines[i].tWaterOut, ei_tWaterOut[i], _ei_tWaterOut);
            checkMShort(i, 8, flightmodel.EI.engines[i].getPropPhi(), ei_propPhi[i], _ei_propPhi);
            checkMByte(i, 16, flightmodel.EI.engines[i].getManifoldPressure(), ei_compressorManifoldPressure[i], _ei_compressorManifoldPressure);
            checkMShort(i, 32, flightmodel.EI.engines[i].getw(), ei_w1[i], _ei_w1);
            checkMByte(i, 64, flightmodel.EI.engines[i].getControlThrottle(), ei_controlThrottle[i], _ei_controlThrottle);
            checkMByte(i, 128, flightmodel.EI.engines[i].getControlProp(), ei_controlProp[i], _ei_controlProp);
            checkMByte(i, 256, flightmodel.EI.engines[i].getControlMix(), ei_controlMix[i], _ei_controlMix);
            if(ei_controlMagneto[i] != (byte)flightmodel.EI.engines[i].getControlMagnetos() || bFirstUpdate)
            {
                flags |= 1 << 14 + i;
                m[i] |= 0x200;
            }
            if(ei_controlCompressor[i] != (byte)flightmodel.EI.engines[i].getControlCompressor() || bFirstUpdate)
            {
                flags |= 1 << 14 + i;
                m[i] |= 0x400;
            }
        }

        bits |= flightmodel.CT.getRadiatorControl() <= 0.5F ? 0 : 1;
        bits |= flightmodel.Gears.lgear ? 2 : 0;
        bits |= flightmodel.Gears.rgear ? 4 : 0;
        bits |= flightmodel.Gears.bIsHydroOperable ? 8 : 0;
        bits |= flightmodel.Gears.onGround ? 0x10 : 0;
        bits |= flightmodel.CT.AirBrakeControl <= 0.5F ? 0 : 0x20;
        if(bits != bitset)
        {
            bitset = bits;
            flags |= 0x80000000;
        }
        for(int j = 0; j < 20; j++)
            weapons |= flightmodel.CT.WeaponControl[j] ? 1 << j : 0;

        if((flightmodel.CT.TWCT & 4) != 0)
            weapons |= 4;
        if((flightmodel.CT.TWCT & 8) != 0)
            weapons |= 8;
        flightmodel.CT.TWCT = 0;
        if(weapons != weaponset)
        {
            weaponset = weapons;
            flags |= 0x40000000;
        }
        if(bFirstUpdate)
        {
            bitset = ~bits;
            weaponset = ~weapons;
            flags |= 0x10000000;
        } else
        {
            int k = 0;
            for(int l = 0; l < flightmodel.CT.Weapons.length; l++)
            {
                com.maddox.il2.ai.BulletEmitter abulletemitter[] = flightmodel.CT.Weapons[l];
                if(abulletemitter == null)
                    continue;
                for(int i1 = 0; i1 < abulletemitter.length; i1++)
                    if(abulletemitter[i1] instanceof Gun)
                    {
                        Gun gun = (Gun)abulletemitter[i1];
                        k += gun.countBullets();
                    }

            }

            if(k != gunsset)
                flags |= 0x10000000;
        }
        if(flags == 0)
            return;
        if(!bFirstUpdate)
            msgOut = new NetMsgGuaranted();
        try
        {
            netWrite();
            if(!bFirstUpdate)
                postTo(channel, msgOut);
        }
        catch(Exception exception)
        {
            printDebug(exception);
        }
        if(!bFirstUpdate)
            msgOut = null;
    }

    private void netWrite()
        throws IOException
    {
        FlightModel flightmodel = air.FM;
        RealFlightModel realflightmodel = null;
        if(flightmodel instanceof RealFlightModel)
            realflightmodel = (RealFlightModel)flightmodel;
        msgOut.writeInt(flags);
        ct_GearControl = writeByte(1, flightmodel.CT.GearControl, ct_GearControl, _ct_GearControl);
        ct_FlapsControl = writeByte(2, flightmodel.CT.FlapsControl, ct_FlapsControl, _ct_FlapsControl);
        ct_BrakeControl = writeByte(4, flightmodel.CT.BrakeControl, ct_BrakeControl, _ct_BrakeControl);
        ct_AileronControl = writeByte(8, flightmodel.CT.AileronControl, ct_AileronControl, _ct_AileronControl);
        ct_ElevatorControl = writeByte(16, flightmodel.CT.ElevatorControl, ct_ElevatorControl, _ct_ElevatorControl);
        ct_RudderControl = writeByte(32, flightmodel.CT.RudderControl, ct_RudderControl, _ct_RudderControl);
        ct_TrimHorControl = writeByte(64, flightmodel.CT.getTrimAileronControl(), ct_TrimHorControl, _ct_TrimHorControl);
        ct_TrimVerControl = writeByte(128, flightmodel.CT.getTrimElevatorControl(), ct_TrimVerControl, _ct_TrimVerControl);
        ct_TrimRudControl = writeByte(256, flightmodel.CT.getTrimRudderControl(), ct_TrimRudControl, _ct_TrimRudControl);
        mass_fuel1 = writeShort(512, flightmodel.M.fuel, mass_fuel1, _mass_fuel1);
        cockpit_AzimuthSpeed = writeByte(1024, cockpitAzimuthSpeed, cockpit_AzimuthSpeed, _cockpit_AzimuthSpeed);
        if(realflightmodel != null)
        {
            fm_saveDeep = writeShort(2048, realflightmodel.saveDeep, fm_saveDeep, _fm_saveDeep);
            fm_shakeLevel = writeByte(4096, realflightmodel.shakeLevel, fm_shakeLevel, _fm_shakeLevel);
        }
        fm_AMx = writeByte(8192, (float)flightmodel.AM.x, fm_AMx, _fm_AMx);
        for(int i = 0; i < flightmodel.EI.getNum(); i++)
        {
            if((flags & 1 << 14 + i) == 0)
                continue;
            msgOut.writeShort(m[i]);
            ei_tOilIn[i] = writeMByte(i, 1, flightmodel.EI.engines[i].tOilIn, ei_tOilIn[i], _ei_tOilIn);
            ei_tOilOut[i] = writeMByte(i, 2, flightmodel.EI.engines[i].tOilOut, ei_tOilOut[i], _ei_tOilOut);
            ei_tWaterOut[i] = writeMByte(i, 4, flightmodel.EI.engines[i].tWaterOut, ei_tWaterOut[i], _ei_tWaterOut);
            ei_propPhi[i] = writeMShort(i, 8, flightmodel.EI.engines[i].getPropPhi(), ei_propPhi[i], _ei_propPhi);
            ei_compressorManifoldPressure[i] = writeMByte(i, 16, flightmodel.EI.engines[i].getManifoldPressure(), ei_compressorManifoldPressure[i], _ei_compressorManifoldPressure);
            ei_w1[i] = writeMShort(i, 32, flightmodel.EI.engines[i].getw(), ei_w1[i], _ei_w1);
            ei_controlThrottle[i] = writeMByte(i, 64, flightmodel.EI.engines[i].getControlThrottle(), ei_controlThrottle[i], _ei_controlThrottle);
            ei_controlProp[i] = writeMByte(i, 128, flightmodel.EI.engines[i].getControlProp(), ei_controlProp[i], _ei_controlProp);
            ei_controlMix[i] = writeMByte(i, 256, flightmodel.EI.engines[i].getControlMix(), ei_controlMix[i], _ei_controlMix);
            if((m[i] & 0x200) != 0)
                msgOut.writeByte(ei_controlMagneto[i] = (byte)flightmodel.EI.engines[i].getControlMagnetos());
            if((m[i] & 0x400) != 0)
                msgOut.writeByte(ei_controlCompressor[i] = (byte)flightmodel.EI.engines[i].getControlCompressor());
        }

        if((flags & 0x80000000) != 0)
            msgOut.writeByte(bits);
        if((flags & 0x40000000) != 0)
            msgOut.writeInt(weapons);
        ct_Sensitivity = writeByte(0x20000000, flightmodel.CT.Sensitivity, ct_Sensitivity, _ct_Sensitivity);
        if((flags & 0x10000000) != 0)
        {
            int j = 0;
            for(int k = 0; k < flightmodel.CT.Weapons.length; k++)
            {
                com.maddox.il2.ai.BulletEmitter abulletemitter[] = flightmodel.CT.Weapons[k];
                if(abulletemitter == null)
                    continue;
                for(int l = 0; l < abulletemitter.length; l++)
                    if(abulletemitter[l] instanceof Gun)
                    {
                        Gun gun = (Gun)abulletemitter[l];
                        j += gun.countBullets();
                        msgOut.writeShort(gun.countBullets());
                    }

            }

            gunsset = j;
        }
    }

    public boolean netInput(NetMsgInput netmsginput)
        throws IOException
    {
        if(isMaster() || channel == null || !Actor.isValid(air))
            return false;
        msgIn = netmsginput;
        FlightModel flightmodel = air.FM;
        RealFlightModel realflightmodel = null;
        if(flightmodel instanceof RealFlightModel)
            realflightmodel = (RealFlightModel)air.FM;
        flags = msgIn.readInt();
        flightmodel.CT.GearControl = readByte(1, flightmodel.CT.GearControl, _ct_GearControl);
        if(bFirstInput)
        {
            flightmodel.CT.forceGear(flightmodel.CT.GearControl);
            Aircraft.forceGear(air.getClass(), air.hierMesh(), flightmodel.CT.GearControl);
        }
        flightmodel.CT.FlapsControl = readByte(2, flightmodel.CT.FlapsControl, _ct_FlapsControl);
        flightmodel.CT.BrakeControl = readByte(4, flightmodel.CT.BrakeControl, _ct_BrakeControl);
        flightmodel.CT.AileronControl = readByte(8, flightmodel.CT.AileronControl, _ct_AileronControl);
        flightmodel.CT.ElevatorControl = readByte(16, flightmodel.CT.ElevatorControl, _ct_ElevatorControl);
        flightmodel.CT.RudderControl = readByte(32, flightmodel.CT.RudderControl, _ct_RudderControl);
        flightmodel.CT.setTrimAileronControl(readByte(64, flightmodel.CT.getTrimAileronControl(), _ct_TrimHorControl));
        flightmodel.CT.setTrimElevatorControl(readByte(128, flightmodel.CT.getTrimElevatorControl(), _ct_TrimVerControl));
        flightmodel.CT.setTrimRudderControl(readByte(256, flightmodel.CT.getTrimRudderControl(), _ct_TrimRudControl));
        if(NetMissionTrack.playingVersion() == 100)
            flightmodel.M.fuel = readByte(512, flightmodel.M.fuel, _mass_fuel);
        else
            flightmodel.M.fuel = readShort(512, flightmodel.M.fuel, _mass_fuel1);
        cockpitAzimuthSpeed = readByte(1024, cockpitAzimuthSpeed, _cockpit_AzimuthSpeed);
        if(realflightmodel != null)
        {
            realflightmodel.saveDeep = readShort(2048, realflightmodel.saveDeep, _fm_saveDeep);
            realflightmodel.shakeLevel = readByte(4096, realflightmodel.shakeLevel, _fm_shakeLevel);
        } else
        {
            if((flags & 0x800) != 0)
                msgIn.readShort();
            if((flags & 0x1000) != 0)
                msgIn.readByte();
        }
        flightmodel.AM.x = readByte(8192, (float)flightmodel.AM.x, _fm_AMx);
        for(int i = 0; i < flightmodel.EI.getNum(); i++)
        {
            if((flags & 1 << 14 + i) == 0)
                continue;
            m[i] = msgIn.readShort();
            flightmodel.EI.engines[i].tOilIn = readMByte(i, 1, flightmodel.EI.engines[i].tOilIn, _ei_tOilIn);
            flightmodel.EI.engines[i].tOilOut = readMByte(i, 2, flightmodel.EI.engines[i].tOilOut, _ei_tOilOut);
            flightmodel.EI.engines[i].tWaterOut = readMByte(i, 4, flightmodel.EI.engines[i].tWaterOut, _ei_tWaterOut);
            flightmodel.EI.engines[i].setPropPhi(readMShort(i, 8, flightmodel.EI.engines[i].getPropPhi(), _ei_propPhi));
            flightmodel.EI.engines[i].setManifoldPressure(readMByte(i, 16, flightmodel.EI.engines[i].getManifoldPressure(), _ei_compressorManifoldPressure));
            if(NetMissionTrack.playingVersion() == 100)
                flightmodel.EI.engines[i].setw(readMByte(i, 32, flightmodel.EI.engines[i].getw(), _ei_w));
            else
                flightmodel.EI.engines[i].setw(readMShort(i, 32, flightmodel.EI.engines[i].getw(), _ei_w1));
            flightmodel.EI.engines[i].setControlThrottle(readMByte(i, 64, flightmodel.EI.engines[i].getControlThrottle(), _ei_controlThrottle));
            flightmodel.EI.engines[i].setControlProp(readMByte(i, 128, flightmodel.EI.engines[i].getControlProp(), _ei_controlProp));
            flightmodel.EI.engines[i].setControlMix(readMByte(i, 256, flightmodel.EI.engines[i].getControlMix(), _ei_controlMix));
            if((m[i] & 0x200) != 0)
                flightmodel.EI.engines[i].setControlMagneto(msgIn.readByte());
            if((m[i] & 0x400) != 0)
                flightmodel.EI.engines[i].setControlCompressor(msgIn.readByte());
        }

        if((flags & 0x80000000) != 0)
        {
            bits = msgIn.readByte();
            flightmodel.CT.setRadiatorControl((bits & 1) == 0 ? 0.0F : 1.0F);
            flightmodel.Gears.lgear = (bits & 2) != 0;
            flightmodel.Gears.rgear = (bits & 4) != 0;
            flightmodel.Gears.bIsHydroOperable = (bits & 8) != 0;
            flightmodel.Gears.onGround = (bits & 0x10) != 0;
            flightmodel.CT.AirBrakeControl = (bits & 0x20) == 0 ? 0.0F : 1.0F;
        }
        if((flags & 0x40000000) != 0)
        {
            weapons = msgIn.readInt();
            for(int j = 0; j < 20; j++)
                flightmodel.CT.WeaponControl[j] = (weapons & 1 << j) != 0;

        }
        flightmodel.CT.Sensitivity = readByte(0x20000000, flightmodel.CT.Sensitivity, _ct_Sensitivity);
        if((flags & 0x10000000) != 0)
        {
            for(int k = 0; k < flightmodel.CT.Weapons.length; k++)
            {
                com.maddox.il2.ai.BulletEmitter abulletemitter[] = flightmodel.CT.Weapons[k];
                if(abulletemitter == null)
                    continue;
                for(int l = 0; l < abulletemitter.length; l++)
                {
                    if(!(abulletemitter[l] instanceof Gun))
                        continue;
                    if(msgIn.available() < 4)
                        break;
                    short word0 = msgIn.readShort();
                    Gun gun = (Gun)abulletemitter[l];
                    gun._loadBullets(word0);
                }

            }

        }
        flightmodel.CT.setPowerControl(flightmodel.EI.engines[0].getControlThrottle());
        flightmodel.CT.setStepControl(flightmodel.EI.engines[0].getControlProp());
        flightmodel.CT.setMixControl(flightmodel.EI.engines[0].getControlMix());
        flightmodel.CT.setMagnetoControl(flightmodel.EI.engines[0].getControlMagnetos());
        flightmodel.CT.setCompressorControl(flightmodel.EI.engines[0].getControlCompressor());
        msgIn = null;
        if(isMirrored())
        {
            NetMsgGuaranted netmsgguaranted = msgOut;
            msgOut = new NetMsgGuaranted();
            try
            {
                netWrite();
                post(msgOut);
            }
            catch(Exception exception)
            {
                printDebug(exception);
            }
            msgOut = netmsgguaranted;
        }
        bFirstInput = false;
        return true;
    }

    public FlightModelTrack(Aircraft aircraft)
    {
        super(null);
        bFirstUpdate = false;
        bitset = 0;
        weaponset = 0;
        gunsset = -1;
        m = new int[AircraftState.MAX_ENGINES];
        ct_GearControl = 0;
        ct_FlapsControl = 0;
        ct_BrakeControl = 0;
        ct_AileronControl = 0;
        ct_ElevatorControl = 0;
        ct_RudderControl = 0;
        ct_TrimHorControl = 0;
        ct_TrimVerControl = 0;
        ct_TrimRudControl = 0;
        ct_Sensitivity = 0;
        mass_fuel1 = 0;
        cockpit_AzimuthSpeed = 0;
        fm_saveDeep = 0;
        fm_shakeLevel = 0;
        fm_AMx = 0;
        ei_tOilIn = new byte[AircraftState.MAX_ENGINES];
        ei_tOilOut = new byte[AircraftState.MAX_ENGINES];
        ei_tWaterOut = new byte[AircraftState.MAX_ENGINES];
        ei_propPhi = new short[AircraftState.MAX_ENGINES];
        ei_compressorManifoldPressure = new byte[AircraftState.MAX_ENGINES];
        ei_w1 = new short[AircraftState.MAX_ENGINES];
        ei_controlThrottle = new byte[AircraftState.MAX_ENGINES];
        ei_controlProp = new byte[AircraftState.MAX_ENGINES];
        ei_controlMix = new byte[AircraftState.MAX_ENGINES];
        ei_controlMagneto = new byte[AircraftState.MAX_ENGINES];
        ei_controlCompressor = new byte[AircraftState.MAX_ENGINES];
        codeByteMIN = -1F;
        codeByteMAX = 1.0F;
        codeByteMUL = 32F;
        codeShortMIN = -1F;
        codeShortMAX = 1.0F;
        codeShortMUL = 32F;
        bFirstInput = true;
        air = aircraft;
        air.setFMTrack(this);
    }

    public FlightModelTrack(Aircraft aircraft, NetChannel netchannel, int i)
    {
        super(null, netchannel, i);
        bFirstUpdate = false;
        bitset = 0;
        weaponset = 0;
        gunsset = -1;
        m = new int[AircraftState.MAX_ENGINES];
        ct_GearControl = 0;
        ct_FlapsControl = 0;
        ct_BrakeControl = 0;
        ct_AileronControl = 0;
        ct_ElevatorControl = 0;
        ct_RudderControl = 0;
        ct_TrimHorControl = 0;
        ct_TrimVerControl = 0;
        ct_TrimRudControl = 0;
        ct_Sensitivity = 0;
        mass_fuel1 = 0;
        cockpit_AzimuthSpeed = 0;
        fm_saveDeep = 0;
        fm_shakeLevel = 0;
        fm_AMx = 0;
        ei_tOilIn = new byte[AircraftState.MAX_ENGINES];
        ei_tOilOut = new byte[AircraftState.MAX_ENGINES];
        ei_tWaterOut = new byte[AircraftState.MAX_ENGINES];
        ei_propPhi = new short[AircraftState.MAX_ENGINES];
        ei_compressorManifoldPressure = new byte[AircraftState.MAX_ENGINES];
        ei_w1 = new short[AircraftState.MAX_ENGINES];
        ei_controlThrottle = new byte[AircraftState.MAX_ENGINES];
        ei_controlProp = new byte[AircraftState.MAX_ENGINES];
        ei_controlMix = new byte[AircraftState.MAX_ENGINES];
        ei_controlMagneto = new byte[AircraftState.MAX_ENGINES];
        ei_controlCompressor = new byte[AircraftState.MAX_ENGINES];
        codeByteMIN = -1F;
        codeByteMAX = 1.0F;
        codeByteMUL = 32F;
        codeShortMIN = -1F;
        codeShortMAX = 1.0F;
        codeShortMUL = 32F;
        bFirstInput = true;
        air = aircraft;
        air.setFMTrack(this);
        channel = netchannel;
        ((NetUser)NetEnv.host()).setArmy(air.getArmy());
        air.FM.Gravity = air.FM.M.getFullMass() * Atmosphere.g();
    }

    public void msgNetDelChannel(NetChannel netchannel)
    {
        if(channel == netchannel)
            channel = null;
    }

    public void destroy()
    {
        super.destroy();
        air = null;
        channel = null;
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        if(!(netchannel instanceof NetChannelOutStream))
            return null;
        if(!netchannel.isMirrored(air.net))
            return null;
        if(netchannel.isMirrored(this))
        {
            return null;
        } else
        {
            NetMsgSpawn netmsgspawn = new NetMsgSpawn(this);
            netmsgspawn.writeNetObj(air.net);
            channel = netchannel;
            msgOut = netmsgspawn;
            bFirstUpdate = true;
            netUpdate();
            bFirstUpdate = false;
            msgOut = null;
            return netmsgspawn;
        }
    }

    static final boolean DEBUG = false;
    private Aircraft air;
    private NetChannel channel;
    private int flags;
    private int bits;
    private int weapons;
    private NetMsgGuaranted msgOut;
    private NetMsgInput msgIn;
    private boolean bFirstUpdate;
    public static final int CT_GearControl = 1;
    public static final int CT_FlapsControl = 2;
    public static final int CT_BrakeControl = 4;
    public static final int CT_AileronControl = 8;
    public static final int CT_ElevatorControl = 16;
    public static final int CT_RudderControl = 32;
    public static final int CT_TrimHorControl = 64;
    public static final int CT_TrimVerControl = 128;
    public static final int CT_TrimRudControl = 256;
    public static final int MASS_fuel = 512;
    public static final int COCKPIT_AzimuthSpeed = 1024;
    public static final int FM_saveDeep = 2048;
    public static final int FM_shakeLevel = 4096;
    public static final int FM_AMx = 8192;
    public static final int M_0 = 16384;
    public static final int M_0_shift = 14;
    public static final int M_1 = 32768;
    public static final int M_2 = 0x10000;
    public static final int M_3 = 0x20000;
    public static final int M_4 = 0x40000;
    public static final int M_5 = 0x80000;
    public static final int M_6 = 0x100000;
    public static final int M_7 = 0x200000;
    public static final int BITSET = 0x80000000;
    public static final int WEAPONSET = 0x40000000;
    public static final int CT_Sensitivity = 0x20000000;
    public static final int GUNS = 0x10000000;
    public static final int EI_tOilIn = 1;
    public static final int EI_tOilOut = 2;
    public static final int EI_tWaterOut = 4;
    public static final int EI_propPhi = 8;
    public static final int EI_compressorManifoldPressure = 16;
    public static final int EI_w = 32;
    public static final int EI_controlThrottle = 64;
    public static final int EI_controlProp = 128;
    public static final int EI_controlMix = 256;
    public static final int EI_controlMagneto = 512;
    public static final int EI_controlCompressor = 1024;
    public static final int CT_RadiatorControl = 1;
    public static final int GEARS_lgear = 2;
    public static final int GEARS_rgear = 4;
    public static final int GEARS_bIsHydroOperable = 8;
    public static final int GEARS_onGround = 16;
    public static final int CT_AirBrakeControl = 32;
    private int bitset;
    private int weaponset;
    private int gunsset;
    private int m[];
    private byte ct_GearControl;
    private float _ct_GearControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_FlapsControl;
    private float _ct_FlapsControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_BrakeControl;
    private float _ct_BrakeControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_AileronControl;
    private float _ct_AileronControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_ElevatorControl;
    private float _ct_ElevatorControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_RudderControl;
    private float _ct_RudderControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_TrimHorControl;
    private float _ct_TrimHorControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_TrimVerControl;
    private float _ct_TrimVerControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_TrimRudControl;
    private float _ct_TrimRudControl[] = {
        -1F, 1.0F, 32F
    };
    private byte ct_Sensitivity;
    private float _ct_Sensitivity[] = {
        0.0F, 1.0F, 100F
    };
    private float _mass_fuel[] = {
        0.0F, 65535F, 0.001953125F
    };
    private short mass_fuel1;
    private float _mass_fuel1[] = {
        0.0F, 65535F, 0.5F
    };
    private byte cockpit_AzimuthSpeed;
    private float _cockpit_AzimuthSpeed[] = {
        -50F, 50F, 2.0F
    };
    private short fm_saveDeep;
    private float _fm_saveDeep[] = {
        -3F, 4F, 200F
    };
    private byte fm_shakeLevel;
    private float _fm_shakeLevel[] = {
        0.0F, 1.0F, 128F
    };
    private byte fm_AMx;
    private float _fm_AMx[] = {
        -1000000F, 1000000F, 0.0001F
    };
    private byte ei_tOilIn[];
    private float _ei_tOilIn[] = {
        0.0F, 400F, 0.25F
    };
    private byte ei_tOilOut[];
    private float _ei_tOilOut[] = {
        0.0F, 400F, 0.25F
    };
    private byte ei_tWaterOut[];
    private float _ei_tWaterOut[] = {
        0.0F, 400F, 0.25F
    };
    private short ei_propPhi[];
    private float _ei_propPhi[] = {
        0.0F, 1.6F, 625F
    };
    private byte ei_compressorManifoldPressure[];
    private float _ei_compressorManifoldPressure[] = {
        0.0F, 2.5F, 40F
    };
    private float _ei_w[] = {
        0.0F, 400F, 0.25F
    };
    private short ei_w1[];
    private float _ei_w1[] = {
        0.0F, 640F, 51.18125F
    };
    private byte ei_controlThrottle[];
    private float _ei_controlThrottle[] = {
        0.0F, 1.1F, 100F
    };
    private byte ei_controlProp[];
    private float _ei_controlProp[] = {
        -1F, 1.0F, 64F
    };
    private byte ei_controlMix[];
    private float _ei_controlMix[] = {
        0.0F, 1.0F, 64F
    };
    private byte ei_controlMagneto[];
    private byte ei_controlCompressor[];
    private float cockpitAzimuthSpeed;
    private float codeByteMIN;
    private float codeByteMAX;
    private float codeByteMUL;
    private float codeShortMIN;
    private float codeShortMAX;
    private float codeShortMUL;
    private boolean bFirstInput;

    static 
    {
        Spawn.add(com.maddox.il2.fm.FlightModelTrack.class, new SPAWN());
    }

}
