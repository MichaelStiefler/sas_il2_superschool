
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import java.io.IOException;
import java.io.PrintStream;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RemoteControlRocket extends Rocket
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
                pos.getAbs(RemoteControlRocket.p, RemoteControlRocket.or);
                out.writeFloat((float)((Tuple3d) (RemoteControlRocket.p)).x);
                out.writeFloat((float)((Tuple3d) (RemoteControlRocket.p)).y);
                out.writeFloat((float)((Tuple3d) (RemoteControlRocket.p)).z);
                RemoteControlRocket.or.wrap();
                int i = (int)((RemoteControlRocket.or.getYaw() * 32000F) / 180F);
                int j = (int)((RemoteControlRocket.or.tangage() * 32000F) / 90F);
                out.writeShort(i);
                out.writeShort(j);
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
            RemoteControlRocket.p.x = netmsginput.readFloat();
            RemoteControlRocket.p.y = netmsginput.readFloat();
            RemoteControlRocket.p.z = netmsginput.readFloat();
            int i = netmsginput.readShort();
            int j = netmsginput.readShort();
            float f = -(((float)i * 180F) / 32000F);
            float f1 = ((float)j * 90F) / 32000F;
            RemoteControlRocket.or.set(f, f1, 0.0F);
            pos.setAbs(RemoteControlRocket.p, RemoteControlRocket.or);
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
                RemoteControlRocket rocketagm = new RemoteControlRocket(actor, netmsginput.channel(), i, point3d, orient, f);
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


    public boolean interpolateStep()
    {
        float f = Time.tickLenFs();
        float f1 = (float)getSpeed(null);
        f1 += (320F - f1) * 0.1F * f;
        super.pos.getAbs(p, or);
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(f1);
        setSpeed(v);
        p.x += ((Tuple3d) (v)).x * (double)f;
        p.y += ((Tuple3d) (v)).y * (double)f;
        p.z += ((Tuple3d) (v)).z * (double)f;
        if(isNet() && isNetMirror())
        {
            super.pos.setAbs(p, or);
            return false;
        }
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
            {
                Pilot pilot = (Pilot)fm;
                if(((Maneuver) (pilot)).target != null)
                {
                    ((FlightModelMain) (((Maneuver) (pilot)).target)).Loc.get(pT);
                    pT.sub(p);
                    or.transformInv(pT);
                    if(((Tuple3d) (pT)).x > -10D)
                    {
                        double d = Aircraft.cvt(((FlightModelMain) (fm)).Skill, 0.0F, 3F, 15F, 0.0F);
                        if(((Tuple3d) (pT)).y > d)
                            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjSideMinus();
                        if(((Tuple3d) (pT)).y < -d)
                            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjSidePlus();
                        if(((Tuple3d) (pT)).z < -d)
                            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjAttitudeMinus();
                        if(((Tuple3d) (pT)).z > d)
                            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjAttitudePlus();
                    }
                }
            }
            or.increment(50F * f * ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CgetdeltaAzimuth(), 50F * f * ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CgetdeltaTangage(), 0.0F);
            or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CResetControls();
        }
        super.pos.setAbs(p, or);
        if(Time.current() > tStart + 500L)
        {
            hunted = NearestTargets.getEnemy(0, -1, p, 800D, 0);
            if(Actor.isValid(hunted))
            {
                float f2 = (float)p.distance(hunted.pos.getAbsPoint());
                if((hunted instanceof Aircraft) && (f2 < 20F || f2 < 40F && f2 > prevd && prevd != 1000F))
                {
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                }
                prevd = f2;
            } else
            {
                prevd = 1000F;
            }
        }
        if(!Actor.isValid(getOwner()) || !(getOwner() instanceof Aircraft))
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return false;
        } else
        {
            return false;
        }
    }

    public RemoteControlRocket()
    {
        fm = null;
        tStart = 0L;
        prevd = 1000F;
    }

    public RemoteControlRocket(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        fm = null;
        tStart = 0L;
        prevd = 1000F;
        super.net = new Mirror(this, netchannel, i);
        super.pos.setAbs(point3d, orient);
        super.pos.reset();
        super.pos.setBase(actor, null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f, int i)
    {
        Actor actor = super.pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            super.net = new Master(this);
        }
        doStart(f);
    }

    protected void doStart(float f)
    {
        super.start(-1F, 0);
        fm = ((SndAircraft) ((Aircraft)getOwner())).FM;
        tStart = Time.current();
        super.pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        super.pos.setAbs(p, or);
    }

    public void destroy()
    {
        if(isNet() && isNetMirror())
            doExplosionAir();
        super.destroy();
    }

    protected void doExplosion(Actor actor, String s)
    {
        super.pos.getTime(Time.current(), p);
        MsgExplosion.send(actor, s, p, getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir()
    {
        super.pos.getTime(Time.current(), p);
        MsgExplosion.send(null, null, p, getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosionAir();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(getOwner().net);
        Point3d point3d = super.pos.getAbsPoint();
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).x);
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).y);
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).z);
        Orient orient = super.pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        float f = (float)getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    private FlightModel fm;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private static Actor hunted = null;
    private long tStart;
    private float prevd;


}