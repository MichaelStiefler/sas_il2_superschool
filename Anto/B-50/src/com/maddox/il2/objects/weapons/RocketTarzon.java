package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.air.*;
import com.maddox.rts.*;
import java.io.IOException;

public class RocketTarzon extends RocketBomb
{
    class Master extends ActorNet
        implements NetUpdate
    {

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(!Actor.isValid(actor()))
                return;
            if(netchannel.isMirrored(this))
                return;
            try
            {
                if(netchannel.userState == 0)
                {
                    NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
                    if(netmsgspawn != null)
                    {
                        postTo(netchannel, netmsgspawn);
                        actor().netFirstUpdate(netchannel);
                    }
                }
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return false;
        }

        public void netUpdate()
        {
            try
            {
                out.unLockAndClear();
                getSpeed(RocketTarzon.v);
                out.writeFloat((float)RocketTarzon.v.x);
                out.writeFloat((float)RocketTarzon.v.y);
                out.writeFloat((float)RocketTarzon.v.z);
                mydebug("WRITE DATA: " + RocketTarzon.v);
                post(Time.current(), out);
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;

        public Master(Actor actor)
        {
            super(actor);
            out = new NetMsgFiltered();
        }
    }

    class Mirror extends ActorNet
    {

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(!Actor.isValid(actor()))
                return;
            if(netchannel.isMirrored(this))
                return;
            try
            {
                if(netchannel.userState == 0)
                {
                    NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
                    if(netmsgspawn != null)
                    {
                        postTo(netchannel, netmsgspawn);
                        actor().netFirstUpdate(netchannel);
                    }
                }
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return false;
            if(isMirrored())
            {
                out.unLockAndSet(netmsginput, 0);
                postReal(Message.currentTime(true), out);
            }
            RocketTarzon.v.x = netmsginput.readFloat();
            RocketTarzon.v.y = netmsginput.readFloat();
            RocketTarzon.v.z = netmsginput.readFloat();
            mydebug("READ DATA: " + RocketTarzon.v);
            setSpeed(RocketTarzon.v);
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            out = new NetMsgFiltered();
        }
    }

    static class SPAWN
        implements NetSpawn
    {

        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            NetObj netobj = netmsginput.readNetObj();
            if(netobj == null)
                return;
            try
            {
                Actor actor = (Actor)netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
                float f = netmsginput.readFloat();
                RocketTarzon rockettarzon = new RocketTarzon(actor, netmsginput.channel(), i, point3d, orient, f);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        SPAWN()
        {
        }
    }


    void endMaster()
    {
        if(isMaster() && getOwner() != null && (getOwner() instanceof TypeGuidedBombCarrier))
        {
            TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier)(TypeGuidedBombCarrier)getOwner();
            typeguidedbombcarrier.typeGuidedBombCsetMasterAlive(false);
        }
    }

    boolean isMaster()
    {
        return isMaster;
    }

    public boolean interpolateStep()
    {
        float f = Time.tickLenFs();
        pos.getAbs(p, or);
        if(World.Rnd().nextFloat() > 0.8F && fl1 != null)
        {
            mydebug("setIntesity() fl1");
            Eff3DActor.setIntesity(fl1, World.Rnd().nextFloat(0.1F, 2.5F));
        }
        mydebug("****************** BOMB NAME |" + name() + "|");
        if(first)
        {
            mydebug("INTERPOLATE ");
            mydebug("V = " + speed);
            mydebug("POS = " + p);
            mydebug("TICKLEN = " + f);
            first = false;
        }
        mydebug(p.x + "| " + p.y + "| " + p.z);
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Maneuver) && target != null)
            {
                mydebug("Target =  " + target.name());
                pT = target.pos.getAbsPoint();
                Point3d point3d = new Point3d();
                point3d.x = pT.x;
                point3d.y = pT.y;
                point3d.z = pT.z + 2.5D;
                mydebug("Target pos =  " + point3d);
                mydebug("Bomb pos =  " + p);
                point3d.sub(p);
                or.transformInv(point3d);
                mydebug("Target error =  " + point3d);
                Aircraft aircraft = (Aircraft)getOwner();
                boolean flag = aircraft.FM.isCapableOfACM() && !aircraft.FM.isReadyToDie() && !aircraft.FM.isTakenMortalDamage() && !aircraft.FM.AS.bIsAboutToBailout && !aircraft.FM.AS.isPilotDead(1);
                if(point3d.x > -10D && flag)
                {
                    double d;
                    if(isMaster())
                        d = Aircraft.cvt((float)fm.Skill * aircraft.FM.AS.getPilotHealth(1), 0.0F, 3F, 25F, 0.0F);
                    else
                        d = Aircraft.cvt((float)fm.Skill * aircraft.FM.AS.getPilotHealth(1), 0.0F, 3F, 100F, 20F);
                    ((TypeX4Carrier)fm.actor).typeX4CResetControls();
                    if(point3d.y > d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjSideMinus();
                    if(point3d.y < -d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjSidePlus();
                    if(point3d.z < -d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjAttitudeMinus();
                    if(point3d.z > d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjAttitudePlus();
                }
            }
            getSpeed(spd);
            float f1 = (float)spd.length();
            Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
            vector3d.y = -azimuthControlScaleFact * (double)f1 * (double)((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth();
            vector3d.z = tangageControlScaleFact * (double)f1 * (double)((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage();
            if(getOwner() != null && (getOwner() instanceof TypeGuidedBombCarrier))
            {
                TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier)(TypeGuidedBombCarrier)getOwner();
                mydebug("isMasterAlive " + ((Actor)typeguidedbombcarrier).name() + " " + typeguidedbombcarrier.typeGuidedBombCisMasterAlive());
                mydebug("isMaster() = " + isMaster());
                mydebug("last bomb alive = " + lastBombAlive);
            }
            if(lastBombAlive.equals(name()))
                ((TypeX4Carrier)fm.actor).typeX4CResetControls();
            mydebug("correction: " + vector3d);
            if(vector3d.y != 0.0D || vector3d.z != 0.0D)
            {
                pos.getAbs(Or);
                Or.transform(vector3d);
                spd.add(vector3d);
                float f2 = (float)spd.length();
                float f3 = f1 / f2;
                spd.scale(f3);
                setSpeed(spd);
            }
        }
        if(!Actor.isValid(getOwner()) || !(getOwner() instanceof Aircraft))
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return true;
        } else
        {
            return true;
        }
    }

    public RocketTarzon()
    {
        first = true;
        isMaster = false;
        fm = null;
    }

    public RocketTarzon(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        first = true;
        isMaster = false;
        fm = null;
        net = new Mirror(this, netchannel, i);
        pos.setAbs(point3d, orient);
        pos.reset();
        pos.setBase(actor, null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f)
    {
        Actor actor = pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            net = new Master(this);
        }
        doStart(f);
    }

    private void doStart(float f)
    {
        super.start(-1F);
        fm = ((Aircraft)getOwner()).FM;
        if(fm instanceof Maneuver)
        {
            maneuver = (Maneuver)fm;
            target = maneuver.target_ground;
        }
        if(Config.isUSE_RENDER())
        {
            fl1 = Eff3DActor.New(this, findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite1.eff", -1F);
            if(fl1 != null)
            {
                mydebug("setIntesity()");
                Eff3DActor.setIntesity(fl1, 0.1F);
            }
        }
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
        if(getOwner() != null && (getOwner() instanceof TypeGuidedBombCarrier))
        {
            TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier)(TypeGuidedBombCarrier)getOwner();
            if(!typeguidedbombcarrier.typeGuidedBombCisMasterAlive())
            {
                isMaster = true;
                typeguidedbombcarrier.typeGuidedBombCsetMasterAlive(true);
            }
        }
        if(getOwner() == World.getPlayerAircraft())
            lastBombAlive = name();
        getSpeed(spd);
        pos.getAbs(p, or);
        Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
        mydebug("BombDispersion!");
        mydebug("Speed BEFORE = " + spd.x + " " + spd.y + " " + spd.z + " ");
        vector3d.x += World.Rnd().nextFloat_Dome(-5F, 5F);
        vector3d.y += World.Rnd().nextFloat_Dome(-2.2F, 2.2F);
        mydebug("error: " + vector3d);
        or.transform(vector3d);
        spd.add(vector3d);
        setSpeed(spd);
        getSpeed(spd);
        mydebug("Speed AFTER  = " + spd.x + " " + spd.y + " " + spd.z + " ");
    }

    public void destroy()
    {
        if(isNet() && isNetMirror())
            doExplosionAir();
        if(Config.isUSE_RENDER())
            Eff3DActor.finish(fl1);
        endMaster();
        super.destroy();
    }

    protected void doExplosionAir()
    {
        pos.getTime(Time.current(), p);
        MsgExplosion.send(null, null, p, getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosionAir();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(getOwner().net);
        Point3d point3d = pos.getAbsPoint();
        netmsgspawn.writeFloat((float)point3d.x);
        netmsgspawn.writeFloat((float)point3d.y);
        netmsgspawn.writeFloat((float)point3d.z);
        Orient orient = pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        float f = (float)getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    protected void mydebug(String s)
    {
    }

    private FlightModel fm;
    private Maneuver maneuver;
    private Actor target;
    private Eff3DActor fl1;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private static double azimuthControlScaleFact = 0.90000000000000002D;
    private static double tangageControlScaleFact = 0.90000000000000002D;
    private boolean first;
    public static String lastBombAlive = "NONE";
    public boolean isMaster;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketTarzon.class;
        Property.set(class1, "mesh", "3do/arms/Tarzon/Tarzon.sim");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.5F);
        Property.set(class1, "timeLife", 1000F);
        Property.set(class1, "timeFire", 12F);
        Property.set(class1, "force", 2.0F);
        Property.set(class1, "massaEnd", 5900F);
        Property.set(class1, "radius", 1500F);
        Property.set(class1, "power", 4500F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 5900F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Spawn.add(class1, new SPAWN());
    }

}