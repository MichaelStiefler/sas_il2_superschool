package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class NetMistel {
    public static final int          NETMSG_MISTEL                 = 92;
    public static final int          NETMSG_MISTEL_EXPLODED        = 0;
    public static final int          NETMSG_MISTEL_POWERCONTROL    = 1;
    public static final int          NETMSG_MISTEL_TAILWHEELLOCKED = 2;
    public static final int          NETMSG_MISTEL_BRAKES          = 3;
    public static final int          NETMSG_MISTEL_BRAKESHOE       = 4;
    public static final int          NETMSG_MISTEL_MAX             = 4;

    private static ArrayList         netMistelAircraftList;
    private static NetMistelAircraft netMistelAircraftOther        = new NetMistelAircraft();
    private static int               DEBUG                         = 0;
    private static boolean           DEBUG_METHODS                 = false;

    private static boolean Debug_Methods(Aircraft aircraft) {
        if (!DEBUG_METHODS) return false;
        boolean retVal = false;
        if (aircraft instanceof JU_88MSTL) if (((JU_88MSTL) aircraft).wasInCutFM) retVal = true;
        if (aircraft instanceof FW_190A8MSTL) if (((FW_190A8MSTL) aircraft).getQueen() != null) if (((JU_88MSTL) ((FW_190A8MSTL) aircraft).getQueen()).wasInCutFM) retVal = true;
        if (aircraft instanceof BF_109F4MSTL) if (((BF_109F4MSTL) aircraft).getQueen() != null) if (((JU_88MSTL) ((BF_109F4MSTL) aircraft).getQueen()).wasInCutFM) retVal = true;
        return retVal;
    }

    static class NetMistelAircraft {
        private Aircraft aircraft;
        private float    lastPowerControl;
        private boolean  lastTailWheelLocked;
        private float    lastBrake;
        private float    lastBrakeLeft;
        private float    lastBrakeRight;
        private boolean  lastBrakeShoe;

        public NetMistelAircraft() {
//            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft()");
            this.aircraft = null;
            this.lastPowerControl = 0F;
            this.lastTailWheelLocked = false;
            this.lastBrake = 0F;
            this.lastBrakeLeft = 0F;
            this.lastBrakeRight = 0F;
            this.lastBrakeShoe = false;
        }

        public NetMistelAircraft(Aircraft aircraft) {
            if (Debug_Methods(aircraft)) System.out.println("NetMistelAircraft NetMistelAircraft(" + aircraft.hashCode() + ")");
            this.aircraft = aircraft;
            this.lastPowerControl = 0F;
            this.lastTailWheelLocked = false;
            this.lastBrake = 0F;
            this.lastBrakeLeft = 0F;
            this.lastBrakeRight = 0F;
            this.lastBrakeShoe = false;
        }

        public int hashCode() {
// if (Debug_Methods(aircraft))
// System.out.println("NetMistelAircraft hashCode()");
            final int prime = 31;
            int result = 1;
            result = prime * result + (this.aircraft == null ? 0 : this.aircraft.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
// if (Debug_Methods(aircraft))
// System.out.println("NetMistelAircraft equals(" + obj.hashCode() + ")");
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            NetMistelAircraft other = (NetMistelAircraft) obj;
            if (this.aircraft == null) {
                if (other.aircraft != null) return false;
            } else if (!this.aircraft.equals(other.aircraft)) return false;
            return true;
        }

        public Aircraft getAircraft() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft getAircraft()");
            return this.aircraft;
        }

        public void setAircraft(Aircraft aircraft) {
            if (Debug_Methods(aircraft)) System.out.println("NetMistelAircraft setAircraft(" + aircraft.hashCode() + ")");
            this.aircraft = aircraft;
        }

        public float getLastPowerControl() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft getLastPowerControl()");
            return this.lastPowerControl;
        }

        public void setLastPowerControl(float lastPowerControl) {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft setLastPowerControl(" + lastPowerControl + ")");
            this.lastPowerControl = lastPowerControl;
        }

        public boolean isLastTailWheelLocked() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft isLastTailWheelLocked()");
            return this.lastTailWheelLocked;
        }

        public void setLastTailWheelLocked(boolean lastTailWheelLocked) {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft setLastTailWheelLocked(" + lastTailWheelLocked + ")");
            this.lastTailWheelLocked = lastTailWheelLocked;
        }

        public float getLastBrake() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft getLastBrake()");
            return this.lastBrake;
        }

        public void setLastBrake(float lastBrake) {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft setLastBrake(" + lastBrake + ")");
            this.lastBrake = lastBrake;
        }

        public float getLastBrakeLeft() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft getLastBrakeLeft()");
            return this.lastBrakeLeft;
        }

        public void setLastBrakeLeft(float lastBrakeLeft) {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft setLastBrakeLeft(" + lastBrakeLeft + ")");
            this.lastBrakeLeft = lastBrakeLeft;
        }

        public float getLastBrakeRight() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft getLastBrakeRight()");
            return this.lastBrakeRight;
        }

        public void setLastBrakeRight(float lastBrakeRight) {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft setLastBrakeRight(" + lastBrakeRight + ")");
            this.lastBrakeRight = lastBrakeRight;
        }

        public boolean isLastBrakeShoe() {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft isLastBrakeShoe()");
            return this.lastBrakeShoe;
        }

        public void setLastBrakeShoe(boolean lastBrakeShoe) {
            if (Debug_Methods(this.aircraft)) System.out.println("NetMistelAircraft setLastBrakeShoe(" + lastBrakeShoe + ")");
            this.lastBrakeShoe = lastBrakeShoe;
        }
    }

    public static void netSendMsg(Aircraft aircraft, boolean bool, NetMsgGuaranted netmsgguaranted) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netSendMsg(" + aircraft.hashCode() + ", " + bool + ", " + netmsgguaranted.hashCode() + ")");
        if (bool) aircraft.net.postTo(aircraft.net.masterChannel(), netmsgguaranted);
        else aircraft.net.post(netmsgguaranted);
    }

    public static boolean netGetGMsg(Aircraft aircraft, NetMsgInput netmsginput, boolean bool) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netGetGMsg(" + aircraft.hashCode() + ", " + netmsginput.hashCode() + ", " + bool + ")");
        netmsginput.mark(netmsginput.available());
        int i = netmsginput.readUnsignedByte();
        if (i != NETMSG_MISTEL) {
            netmsginput.reset();
            return false;
        }
        int j = netmsginput.readUnsignedByte();
        if (j > NETMSG_MISTEL_MAX) {
            netmsginput.reset();
            return false;
        }
        boolean retVal = false;
        switch (j) {
            case NETMSG_MISTEL_EXPLODED:
                retVal = receiveNetExploded(aircraft, netmsginput);
                break;
            case NETMSG_MISTEL_POWERCONTROL:
                retVal = receivePowerControl(aircraft, netmsginput);
                break;
            case NETMSG_MISTEL_TAILWHEELLOCKED:
                retVal = receiveTailWheelLocked(aircraft, netmsginput);
                break;
            case NETMSG_MISTEL_BRAKES:
                retVal = receiveBrakes(aircraft, netmsginput);
                break;
            case NETMSG_MISTEL_BRAKESHOE:
                retVal = receiveBrakeShoe(aircraft, netmsginput);
                break;
            default:
                break;
        }
        if (!retVal) netmsginput.reset();
        return retVal;
    }

    public static boolean netSendExplosionToDroneMaster(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ")");
        if (!aircraft.isNet()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel)) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
        Aircraft drone = ((Mistel) aircraft).getDrone();
        if (drone == null) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 5");
        if (!drone.isNetMirror()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 6");

        try {
            if (DEBUG >= 2) System.out.println("NetMistel > netSendExplosionToDroneMaster < to drone master!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(NETMSG_MISTEL);
            netMsgGuaranted.writeByte(NETMSG_MISTEL_EXPLODED);
            netMsgGuaranted.writeNetObj(drone.net);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netSendMsg(drone, true, netMsgGuaranted);
            if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 7");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if (DEBUG >= 2) System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

    public static boolean netSendPowerControlToMirrors(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") getPowerControl()=" + aircraft.FM.CT.getPowerControl());
        if (!aircraft.isNet()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel)) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            if (DEBUG >= 2) System.out.println("NetMistel > netSendPowerControl < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(NETMSG_MISTEL);
            netMsgGuaranted.writeByte(NETMSG_MISTEL_POWERCONTROL);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getPowerControl());
            netSendMsg(aircraft, false, netMsgGuaranted);
            if (DEBUG >= 2) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 5");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if (DEBUG >= 2) System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

    public static boolean netSendTailwheelLockToMirrors(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") bTailwheelLocked=" + aircraft.FM.Gears.bTailwheelLocked);
        if (!aircraft.isNet()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel)) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            if (DEBUG >= 2) System.out.println("NetMistel > netSendTailwheelLock < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(NETMSG_MISTEL);
            netMsgGuaranted.writeByte(NETMSG_MISTEL_TAILWHEELLOCKED);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeBoolean(aircraft.FM.Gears.bTailwheelLocked);
            netSendMsg(aircraft, false, netMsgGuaranted);
            if (DEBUG >= 2) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if (DEBUG >= 2) System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

    public static boolean netSendBrakesToMirrors(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") getBrake()=" + aircraft.FM.CT.getBrake() + ", getBrakeLeft()=" + aircraft.FM.CT.getBrakeLeft()
                + ", getBrakeRight()=" + aircraft.FM.CT.getBrakeRight());
        if (!aircraft.isNet()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel)) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            if (DEBUG >= 2) System.out.println("NetMistel > netSendBrakes < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(NETMSG_MISTEL);
            netMsgGuaranted.writeByte(NETMSG_MISTEL_BRAKES);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getBrake());
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getBrakeLeft());
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getBrakeRight());
            netSendMsg(aircraft, false, netMsgGuaranted);
            if (DEBUG >= 2) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

    public static boolean netSendBrakeShoeToMirrors(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") brakeShoe=" + aircraft.FM.brakeShoe);
        if (!aircraft.isNet()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel)) return false;
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            if (DEBUG >= 2) System.out.println("NetMistel > netSendBrakeShoe < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(NETMSG_MISTEL);
            netMsgGuaranted.writeByte(NETMSG_MISTEL_BRAKESHOE);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeBoolean(aircraft.FM.brakeShoe);
            netSendMsg(aircraft, false, netMsgGuaranted);
            if (DEBUG >= 2) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if (DEBUG >= 2) System.out.println("NetMistel netSendBrakeShoeToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

    private static boolean receiveNetExploded(Aircraft aircraft, NetMsgInput netmsginput) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel receiveNetExploded(" + aircraft.hashCode() + ", " + netmsginput.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel receiveNetExploded(" + aircraft.getClass().getName() + " - " + aircraft.name() + ")");
        if (!(aircraft instanceof Mistel)) return false;
        if (aircraft.net != netmsginput.readNetObj()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receiveNetExploded(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (((Mistel) aircraft).getQueen() == null) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receiveNetExploded(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (((Mistel) aircraft).getQueen().net != netmsginput.readNetObj()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receiveNetExploded(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
        ((Mistel) aircraft).mistelExplosion();
        if (DEBUG >= 1) System.out.println("NetMistel receiveNetExploded(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") end");
        return true;
    }

    private static boolean receivePowerControl(Aircraft aircraft, NetMsgInput netmsginput) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel receivePowerControl(" + aircraft.hashCode() + ", " + netmsginput.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel receivePowerControl(" + aircraft.getClass().getName() + " - " + aircraft.name() + ")");
        if (aircraft.net != netmsginput.readNetObj()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receivePowerControl(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        float fThrottle = netmsginput.readFloat();
        if (DEBUG >= 2) System.out.println("NetMistel receivePowerControl(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        aircraft.FM.CT.setPowerControl(fThrottle);
        if (DEBUG >= 1) System.out.println("NetMistel receivePowerControl(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") end, PowerControl=" + fThrottle);
        return true;
    }

    private static boolean receiveTailWheelLocked(Aircraft aircraft, NetMsgInput netmsginput) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel receiveTailWheelLocked(" + aircraft.hashCode() + ", " + netmsginput.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel receiveTailWheelLocked(" + aircraft.getClass().getName() + " - " + aircraft.name() + ")");
        if (aircraft.net != netmsginput.readNetObj()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receiveTailWheelLocked(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        boolean bTailwheelLocked = netmsginput.readBoolean();
        if (DEBUG >= 2) System.out.println("NetMistel receiveTailWheelLocked(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        aircraft.FM.CT.bHasLockGearControl = true;
        aircraft.FM.Gears.bTailwheelLocked = bTailwheelLocked;
        aircraft.FM.CT.bHasLockGearControl = false;
        if (DEBUG >= 1) System.out.println("NetMistel receiveTailWheelLocked(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") end, bTailwheelLocked=" + bTailwheelLocked);
        return true;
    }

    private static boolean receiveBrakes(Aircraft aircraft, NetMsgInput netmsginput) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel receiveBrakes(" + aircraft.hashCode() + ", " + netmsginput.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel receiveBrakes(" + aircraft.getClass().getName() + " - " + aircraft.name() + ")");
        if (aircraft.net != netmsginput.readNetObj()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receiveBrakes(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        aircraft.FM.CT.BrakeControl = netmsginput.readFloat();
        aircraft.FM.CT.BrakeLeftControl = netmsginput.readFloat();
        aircraft.FM.CT.BrakeRightControl = netmsginput.readFloat();
        if (DEBUG >= 1) System.out.println("NetMistel receiveBrakes(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") end, BrakeControl=" + aircraft.FM.CT.BrakeControl + ", BrakeLeftControl=" + aircraft.FM.CT.BrakeLeftControl
                + ", BrakeRightControl=" + aircraft.FM.CT.BrakeRightControl);
        return true;
    }

    private static boolean receiveBrakeShoe(Aircraft aircraft, NetMsgInput netmsginput) throws IOException {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel receiveBrakeShoe(" + aircraft.hashCode() + ", " + netmsginput.hashCode() + ")");
        if (DEBUG >= 1) System.out.println("NetMistel receiveBrakeShoe(" + aircraft.getClass().getName() + " - " + aircraft.name() + ")");
        if (aircraft.net != netmsginput.readNetObj()) return false;
        if (DEBUG >= 2) System.out.println("NetMistel receiveBrakeShoe(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        boolean brakeShoe = netmsginput.readBoolean();
        if (DEBUG >= 2) System.out.println("NetMistel receiveBrakeShoe(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        aircraft.FM.brakeShoe = brakeShoe;
        if (DEBUG >= 1) System.out.println("NetMistel receiveBrakeShoe(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") end, brakeShoe=" + brakeShoe);
        return true;
    }

    public static void mistelToMirrors(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel mistelToMirrors(" + aircraft.hashCode() + ")");
        NetMistelAircraft netMistelAircraft = null;
        netMistelAircraftOther.setAircraft(aircraft);
        if (!netMistelAircraftList.contains(netMistelAircraftOther)) {
            netMistelAircraft = new NetMistelAircraft(aircraft);
            netMistelAircraftList.add(netMistelAircraft);
            if (DEBUG >= 2) System.out.println("Added NetMistelAircraft " + netMistelAircraft.aircraft.getClass().getName() + " - " + netMistelAircraft.aircraft.name() + " to List!");
        } else netMistelAircraft = (NetMistelAircraft) netMistelAircraftList.get(netMistelAircraftList.indexOf(netMistelAircraftOther));

        if (netMistelAircraft.aircraft.isNetMaster()) {
            if (netMistelAircraft.getLastPowerControl() != netMistelAircraft.aircraft.FM.CT.getPowerControl()) {
                netMistelAircraft.setLastPowerControl(netMistelAircraft.aircraft.FM.CT.getPowerControl());
                if (DEBUG >= 2) System.out.println("NetMistel PowerControl(" + netMistelAircraft.aircraft.getClass().getName() + " - " + netMistelAircraft.aircraft.name() + ") = " + netMistelAircraft.aircraft.FM.CT.getPowerControl());
                netSendPowerControlToMirrors(netMistelAircraft.aircraft);
            }
            if (netMistelAircraft.isLastTailWheelLocked() != netMistelAircraft.aircraft.FM.Gears.bTailwheelLocked) {
                netMistelAircraft.setLastTailWheelLocked(netMistelAircraft.aircraft.FM.Gears.bTailwheelLocked);
                if (DEBUG >= 2) System.out.println("NetMistel bTailwheelLocked(" + netMistelAircraft.aircraft.getClass().getName() + " - " + netMistelAircraft.aircraft.name() + ") = " + netMistelAircraft.aircraft.FM.Gears.bTailwheelLocked);
                netSendTailwheelLockToMirrors(netMistelAircraft.aircraft);
            }
            if (netMistelAircraft.getLastBrake() != netMistelAircraft.aircraft.FM.CT.BrakeControl || netMistelAircraft.getLastBrakeLeft() != netMistelAircraft.aircraft.FM.CT.BrakeLeftControl
                    || netMistelAircraft.getLastBrakeRight() != netMistelAircraft.aircraft.FM.CT.BrakeRightControl) {
                netMistelAircraft.setLastBrake(netMistelAircraft.aircraft.FM.CT.BrakeControl);
                netMistelAircraft.setLastBrakeLeft(netMistelAircraft.aircraft.FM.CT.BrakeLeftControl);
                netMistelAircraft.setLastBrakeRight(netMistelAircraft.aircraft.FM.CT.BrakeRightControl);
                if (DEBUG >= 2) System.out.println("NetMistel BrakeControl(" + netMistelAircraft.aircraft.getClass().getName() + " - " + netMistelAircraft.aircraft.name() + ") =" + netMistelAircraft.aircraft.FM.CT.BrakeControl + ", BrakeLeftControl="
                        + netMistelAircraft.aircraft.FM.CT.BrakeLeftControl + ", BrakeRightControl=" + netMistelAircraft.aircraft.FM.CT.BrakeRightControl);
                netSendBrakesToMirrors(netMistelAircraft.aircraft);
            }
            if (netMistelAircraft.isLastBrakeShoe() != netMistelAircraft.aircraft.FM.brakeShoe) {
                netMistelAircraft.setLastBrakeShoe(netMistelAircraft.aircraft.FM.brakeShoe);
                if (DEBUG >= 2) System.out.println("NetMistel brakeShoe(" + netMistelAircraft.aircraft.getClass().getName() + " - " + netMistelAircraft.aircraft.name() + ") = " + netMistelAircraft.aircraft.FM.brakeShoe);
                netSendBrakeShoeToMirrors(netMistelAircraft.aircraft);
            }
        }
    }

    public static void removeNetMistelFromList(Aircraft aircraft) {
        if (Debug_Methods(aircraft)) System.out.println("NetMistel removeNetMistelFromList(" + aircraft.hashCode() + ")");
        if (netMistelAircraftList.contains(aircraft)) netMistelAircraftList.remove(aircraft);
    }

    static {
        netMistelAircraftList = new ArrayList();
//
//        Thread thread = new Thread() {
//            private long lastCurrentTimeReal = 0L;
//
//            public void run() {
//                System.out.println("Thread Running");
//                do {
//                    try {
//                        Thread.sleep(10000);
//                        if (lastCurrentTimeReal == Time.currentReal()) {
//                            System.exit(0);
//                        }
//                        lastCurrentTimeReal = Time.currentReal();
////                        System.out.println("Current Time: " + lastCurrentTimeReal);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } while (true);
//            }
//        };
//
//        thread.start();
    }
}
