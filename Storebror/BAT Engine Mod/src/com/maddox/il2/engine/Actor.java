
package com.maddox.il2.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.Rocket;
import com.maddox.il2.objects.weapons.RocketBomb;
import com.maddox.rts.Finger;
import com.maddox.rts.Message;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.ObjState;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

// Referenced classes of package com.maddox.il2.engine:
//            ActorException, Interpolators, MsgDreamGlobalListener, Orient, 
//            Loc, Engine, MsgOwner, ActorNet, 
//            ActorPos, Landscape, InterpolateAdapter, ActorDraw, 
//            DreamEnv, Mat, Hook, Interpolate

public abstract class Actor extends ObjState
{

    public static boolean isValid(Actor actor)
    {
        return actor != null && !actor.isDestroyed();
    }

    public static boolean isAlive(Actor actor)
    {
        return actor != null && actor.isAlive();
    }

    public boolean isAlive()
    {
        return (flags & 0x8004) == 0;
    }

    public void setDiedFlag(boolean flag)
    {
        if(flag)
        {
            if((flags & 4) == 0)
            {
                flags |= 4;
                if(this instanceof Prey)
                {
                    int i = Engine.targets().indexOf(this);
                    if(i >= 0)
                        Engine.targets().remove(i);
                }
                if(isValid(owner))
                    MsgOwner.died(owner, this);
            }
        } else
        if((flags & 4) != 0)
        {
            flags &= -5;
            if(this instanceof Prey)
                Engine.targets().add(this);
        }
    }

    public boolean getDiedFlag()
    {
        return (flags & 4) != 0;
    }

    public boolean isTaskComplete()
    {
        return (flags & 8) != 0;
    }

    public void setTaskCompleteFlag(boolean flag)
    {
        if(flag)
        {
            if((flags & 8) == 0)
            {
                flags |= 8;
                if(isValid(owner))
                    MsgOwner.taskComplete(owner, this);
            }
        } else
        {
            flags &= -9;
        }
    }

    public int getArmy()
    {
        return flags >>> 16;
    }

    public void setArmy(int i)
    {
        flags = i << 16 | flags & 0xffff;
    }

    public boolean isRealTime()
    {
        return (flags & 0x2000) != 0 || Time.isRealOnly();
    }

    public boolean isRealTimeFlag()
    {
        return (flags & 0x2000) != 0;
    }

    public boolean isNet()
    {
        return net != null;
    }

    public boolean isNetMaster()
    {
        return net != null && net.isMaster();
    }

    public boolean isNetMirror()
    {
        return net != null && net.isMirror();
    }

    public boolean isSpawnFromMission()
    {
        return (flags & 0x1000) != 0;
    }

    public void missionStarting()
    {
    }

    public void setName(String s)
    {
        if(name != null)
            Engine.cur.name2Actor.remove(name);
        name = s;
        if(s != null)
            Engine.cur.name2Actor.put(name, this);
    }

    public String name()
    {
        return name != null ? name : "NONAME";
    }

    public boolean isNamed()
    {
        return name != null;
    }

    public static Actor getByName(String s)
    {
        return (Actor)Engine.cur.name2Actor.get(s);
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        return new NetMsgSpawn(net);
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
    }

    public Actor getOwner()
    {
        return owner;
    }

    public boolean isContainOwner(Object obj)
    {
        if(obj == null)
            return false;
        if(owner == null)
            return false;
        if(owner.equals(obj))
            return true;
        else
            return owner.isContainOwner(obj);
    }

    public Object[] getOwnerAttached()
    {
        if(ownerAttached != null)
            return ownerAttached.toArray();
        else
            return emptyArrayOwners;
    }

    public Object[] getOwnerAttached(Object aobj[])
    {
        if(ownerAttached != null)
            return ownerAttached.toArray(aobj);
        else
            return emptyArrayOwners;
    }

    public int getOwnerAttachedCount()
    {
        if(ownerAttached != null)
            return ownerAttached.size();
        else
            return 0;
    }

    public int getOwnerAttachedIndex(Object obj)
    {
        if(ownerAttached != null)
            return ownerAttached.indexOf(obj);
        else
            return -1;
    }

    public Object getOwnerAttached(int i)
    {
        return ownerAttached.get(i);
    }

    public void setOwner(Actor actor, boolean flag, boolean flag1, boolean flag2)
    {
        if(actor != owner)
        {
            if(isValid(owner) && owner.ownerAttached != null)
            {
                int i = owner.ownerAttached.indexOf(this);
                if(i >= 0)
                {
                    owner.ownerAttached.remove(i);
                    if(flag1)
                        MsgOwner.detach(owner, this);
                }
            }
            Actor actor1 = owner;
            if(isValid(actor))
            {
                owner = actor;
                if(flag)
                {
                    if(owner.ownerAttached == null)
                        owner.ownerAttached = new ArrayList();
                    owner.ownerAttached.add(this);
                    if(flag1)
                        MsgOwner.attach(owner, this);
                    if(flag2)
                        MsgOwner.change(this, actor, actor1);
                }
            } else
            {
                owner = null;
                if(actor != null)
                    throw new ActorException("new owner is destroyed");
                if(flag2)
                    MsgOwner.change(this, actor, actor1);
            }
        }
    }

    public void setOwnerAfter(Actor actor, Actor actor1, boolean flag, boolean flag1, boolean flag2)
    {
        if(actor != owner)
        {
            if(isValid(owner) && owner.ownerAttached != null)
            {
                int i = owner.ownerAttached.indexOf(this);
                if(i >= 0)
                {
                    owner.ownerAttached.remove(i);
                    if(flag1)
                        MsgOwner.detach(owner, this);
                }
            }
            Actor actor2 = owner;
            if(isValid(actor))
            {
                owner = actor;
                if(flag)
                {
                    if(owner.ownerAttached == null)
                        owner.ownerAttached = new ArrayList();
                    if(actor1 == null)
                    {
                        owner.ownerAttached.add(0, this);
                    } else
                    {
                        int j = owner.ownerAttached.indexOf(actor1);
                        if(j < 0)
                            throw new ActorException("beforeChildren not found");
                        owner.ownerAttached.add(j + 1, this);
                    }
                    if(flag1)
                        MsgOwner.attach(owner, this);
                    if(flag2)
                        MsgOwner.change(this, actor, actor2);
                }
            } else
            {
                owner = null;
                if(actor != null)
                    throw new ActorException("new owner is destroyed");
                if(flag2)
                    MsgOwner.change(this, actor, actor2);
            }
        }
    }

    public void setOwner(Actor actor)
    {
        setOwner(actor, true, true, false);
    }

    public void changeOwner(Actor actor)
    {
        setOwner(actor, true, true, true);
    }

    public Hook findHook(Object obj)
    {
        return null;
    }

    public float futurePosition(float f, Point3d point3d)
    {
        if(pos == null)
        {
            return 0.0F;
        } else
        {
            long l = (long)(f * 1000F + 0.5F);
            pos.getTime(Time.current() + l, point3d);
            return f;
        }
    }

    public float futurePosition(float f, Loc loc)
    {
        if(pos == null)
        {
            return 0.0F;
        } else
        {
            long l = (long)(f * 1000F + 0.5F);
            pos.getTime(Time.current() + l, loc);
            return f;
        }
    }

    public void alignPosToLand(double d, boolean flag)
    {
        if(pos == null)
            return;
        if(Engine.land() == null)
            return;
        pos.getAbs(_tmpPoint);
        _tmpPoint.z = Engine.land().HQ(_tmpPoint.x, _tmpPoint.y) + d;
        pos.setAbs(_tmpPoint);
        if(flag)
            pos.reset();
    }

    protected void interpolateTick()
    {
        if(interp != null && interp.size() > 0)
        {
            try
            {
                interp.tick((flags & 0x2000) == 0 ? Time.current() : Time.currentReal());
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            return;
        } else
        {
            InterpolateAdapter.adapter().removeListener(this);
            return;
        }
    }

    public boolean interpIsSleep()
    {
        if(interp != null)
            return interp.isSleep();
        else
            return false;
    }

    public boolean interpSleep()
    {
        if(interp != null)
            return interp.sleep();
        else
            return false;
    }

    public boolean interpWakeup()
    {
        if(interp != null)
            return interp.wakeup();
        else
            return false;
    }

    public int interpSize()
    {
        if(interp != null)
            return interp.size();
        else
            return 0;
    }

    public Interpolate interpGet(Object obj)
    {
        if(interp != null)
            return interp.get(obj);
        else
            return null;
    }

    public void interpPut(Interpolate interpolate, Object obj, long l, Message message)
    {
        if(interp == null)
            interp = new Interpolators();
        interp.put(interpolate, obj, l, message, this);
        if(interp.size() == 1)
            InterpolateAdapter.adapter().addListener(this);
    }

    public boolean interpEnd(Object obj)
    {
        if(interp != null)
            return interp.end(obj);
        else
            return false;
    }

    public void interpEndAll()
    {
        if(interp != null)
            interp.endAll();
    }

    public boolean interpCancel(Object obj)
    {
        if(interp != null)
            return interp.cancel(obj);
        else
            return false;
    }

    public void interpCancelAll()
    {
        if(interp != null)
            interp.cancelAll();
    }

    public boolean isDrawing()
    {
        return (flags & 1) != 0 && (draw != null || icon != null);
    }

    public boolean isIconDrawing()
    {
        return (flags & 1) != 0 && icon != null;
    }

    public void drawing(boolean flag)
    {
        if(flag != ((flags & 1) != 0))
        {
            if(flag)
                flags |= 1;
            else
                flags &= -2;
            if(pos != null && pos.actor() == this)
                pos.drawingChange(flag);
        }
    }

    public boolean isVisibilityAsBase()
    {
        return (flags & 2) != 0;
    }

    public void visibilityAsBase(boolean flag)
    {
        if(((flags & 2) != 0) == flag)
            return;
        if(flag)
            flags |= 2;
        else
            flags &= -3;
        if(pos != null && (flags & 1) != 0 && pos.actor() == this)
            pos.drawingChange(true);
    }

    public boolean isCollide()
    {
        return (flags & 0x10) != 0;
    }

    public boolean isCollideAsPoint()
    {
        return (flags & 0x20) != 0;
    }

    public boolean isCollideAndNotAsPoint()
    {
        return (flags & 0x30) == 16;
    }

    public boolean isCollideOnLand()
    {
        return (flags & 0x40) != 0;
    }

    public void collide(boolean flag)
    {
        if(flag != ((flags & 0x10) != 0))
        {
            if(flag)
                flags |= 0x10;
            else
                flags &= 0xffffffef;
            if(pos != null && (flags & 0x20) == 0 && pos.actor() == this)
                pos.collideChange(flag);
        }
    }

    public boolean isDreamListener()
    {
        return (flags & 0x200) != 0;
    }

    public boolean isDreamFire()
    {
        return (flags & 0x100) != 0;
    }

    public void dreamFire(boolean flag)
    {
        if(flag != ((flags & 0x100) != 0))
        {
            if(flag)
                flags |= 0x100;
            else
                flags &= 0xfffffeff;
            if(pos != null && pos.actor() == this)
                pos.dreamFireChange(flag);
        }
    }

    public float collisionR()
    {
        return 10F;
    }

    public Acoustics acoustics()
    {
        Actor actor;
        for(actor = this; actor != null && actor.acoustics == null;)
            if(actor.pos != null)
                actor = actor.pos.base();
            else
                actor = null;

        if(actor != null)
            return actor.acoustics;
        else
            return Engine.worldAcoustics();
    }

    public Actor actorAcoustics()
    {
        Actor actor;
        for(actor = this; actor != null && actor.acoustics == null;)
            if(actor.pos != null)
                actor = actor.pos.base();
            else
                actor = null;

        return actor;
    }

    public Acoustics findParentAcoustics()
    {
        Actor actor = this;
        do
        {
            if(actor == null)
                break;
            if(actor.acoustics != null)
                return actor.acoustics;
            if(actor.pos == null)
                break;
            actor = actor.pos.base();
        } while(true);
        return null;
    }

    public void setAcoustics(Acoustics acoustics1)
    {
        if(acoustics1 == null)
            acoustics1 = Engine.worldAcoustics();
        acoustics = acoustics1;
        if(draw != null && draw.sounds != null)
        {
            for(SoundFX soundfx = draw.sounds.get(); soundfx != null; soundfx = soundfx.next())
                soundfx.setAcoustics(acoustics);

        }
        if(ownerAttached != null)
        {
            for(int i = 0; i < ownerAttached.size(); i++)
            {
                Actor actor = (Actor)ownerAttached.get(i);
                actor.setAcoustics(acoustics1);
            }

        }
    }

    public SoundFX newSound(String s, boolean flag)
    {
        if(draw == null || s == null)
            return null;
        if(s.equals(""))
        {
            System.out.println("Empty sound in " + toString());
            return null;
        }
        SoundFX soundfx = new SoundFX(s);
        if(soundfx.isInitialized())
        {
            soundfx.setAcoustics(acoustics);
            soundfx.insert(draw.sounds(), false);
            if(flag)
                soundfx.play();
        } else
        {
            soundfx = null;
        }
        return soundfx;
    }

    public SoundFX newSound(SoundPreset soundpreset, boolean flag, boolean flag1)
    {
        if(draw == null || soundpreset == null)
            return null;
        SoundFX soundfx = new SoundFX(soundpreset);
        if(soundfx.isInitialized())
        {
            soundfx.setAcoustics(acoustics);
            soundfx.insert(draw.sounds(), flag1);
            if(flag)
                soundfx.play();
        } else
        {
            soundfx = null;
        }
        return soundfx;
    }

    public void playSound(String s, boolean flag)
    {
        if(draw == null || s == null)
            return;
        if(s.equals(""))
        {
            System.out.println("Empty sound in " + toString());
            return;
        }
        SoundFX soundfx = new SoundFX(s);
        if(flag && soundfx.isInitialized())
        {
            soundfx.setAcoustics(acoustics);
            soundfx.insert(draw.sounds(), true);
            soundfx.play();
        } else
        {
            soundfx.play(pos.getAbsPoint());
        }
    }

    public void playSound(SoundPreset soundpreset, boolean flag)
    {
        if(draw == null || soundpreset == null)
            return;
        SoundFX soundfx = new SoundFX(soundpreset);
        if(flag && soundfx.isInitialized())
        {
            soundfx.setAcoustics(acoustics);
            soundfx.insert(draw.sounds(), true);
            soundfx.play();
        }
        soundfx.play(pos.getAbsPoint());
    }

    public void stopSounds()
    {
        if(draw != null && draw.sounds != null)
        {
            for(SoundFX soundfx = draw.sounds.get(); soundfx != null; soundfx = soundfx.next())
                soundfx.stop();

        }
    }

    public void breakSounds()
    {
        if(draw != null && draw.sounds != null)
        {
            for(SoundFX soundfx = draw.sounds.get(); soundfx != null; soundfx = soundfx.next())
                soundfx.cancel();

        }
    }

    public SoundFX getRootFX()
    {
        return null;
    }

    public boolean hasInternalSounds()
    {
        return false;
    }

    public boolean isDestroyed()
    {
        return (flags & 0x8000) != 0;
    }

    public void destroy()
    {
        if(isDestroyed())
            return;
        breakSounds();
        if(pos != null)
            if(pos.actor() == this)
            {
                pos.reset();
                pos.destroy();
            } else
            if(isValid(pos.base()))
                pos.base().pos.removeChildren(this);
        if(this instanceof MsgDreamGlobalListener)
            Engine.dreamEnv().removeGlobalListener(this);
        if((this instanceof Bomb) || (this instanceof Rocket) || (this instanceof RocketBomb))
            Engine.ordinances().remove(this);
        if(ownerAttached != null)
        {
            Actor actor;
            for(; ownerAttached.size() > 0; actor.changeOwner(null))
                actor = (Actor)ownerAttached.get(0);

        }
        setOwner(null);
        destroy(((com.maddox.rts.Destroy) (net)));
        if(interp != null)
        {
            interp.destroy();
            interp = null;
            InterpolateAdapter.adapter().removeListener(this);
        }
        destroy(((com.maddox.rts.Destroy) (draw)));
        if(name != null)
            Engine.cur.name2Actor.remove(name);
        if(this instanceof Prey)
        {
            int i = Engine.targets().indexOf(this);
            if(i >= 0)
                Engine.targets().remove(i);
        }
        flags |= 0x8000;
        super.destroy();
        _countActors--;
        if(Engine.cur != null)
            Engine.cur.actorDestroyed(this);
    }

    public void postDestroy()
    {
        Engine.postDestroyActor(this);
    }

    public void postDestroy(long l)
    {
        MsgDestroy.Post(l, this);
    }

    public double distance(Actor actor)
    {
        return pos.getAbsPoint().distance(actor.pos.getAbsPoint());
    }

    public int target_O_Clock(Actor actor)
    {
        _V1.sub(actor.pos.getAbsPoint(), pos.getAbsPoint());
        pos.getAbsOrient().transformInv(_V1);
        float f = 57.32484F * (float)Math.atan2(_V1.y, -_V1.x);
        int i = (int)f;
        i = ((i + 180) % 360 + 15) / 30;
        if(i == 0)
            i = 12;
        float f1 = (float)_V1.length() + 0.1F;
        float f2 = (float)(actor.pos.getAbsPoint().z - pos.getAbsPoint().z) / f1;
        if(f2 > 0.4F)
            i += 12;
        else
        if(f2 < -0.4F)
            i += 24;
        return i;
    }

    public double getSpeed(Vector3d vector3d)
    {
        return pos.speed(vector3d);
    }

    public void setSpeed(Vector3d vector3d)
    {
    }

    public static void setSpawnFromMission(boolean flag)
    {
        bSpawnFromMission = flag;
    }

    public static int countAll()
    {
        return _countActors;
    }

    public boolean isGameActor()
    {
        return _hash > 0;
    }

    protected Actor()
    {
        flags = 0;
        acoustics = null;
        createActorHashCode();
        if(bSpawnFromMission)
            flags |= 0x1000;
        _countActors++;
        if(this instanceof MsgDreamGlobalListener)
            Engine.dreamEnv().addGlobalListener(this);
        if(this instanceof Prey)
            Engine.targets().add(this);
        if((this instanceof Bomb) || (this instanceof Rocket) || (this instanceof RocketBomb))
            Engine.ordinances().add(this);
    }

    protected void createActorHashCode()
    {
        makeActorGameHashCode();
    }

    protected void makeActorRealHashCode()
    {
        _hash = -Math.abs(super.hashCode());
    }

    protected void makeActorGameHashCode()
    {
        _hash = _hashNext++;
    }

    protected static void resetActorGameHashCodes()
    {
        _hashNext = 1;
    }

    public static int _getCurHashNextCode()
    {
        return _hashNext;
    }

    public int hashCode()
    {
        return _hash;
    }

    public long getCRC(long l)
    {
        if(pos == null)
        {
            return l;
        } else
        {
            pos.getAbs(_tmpPoint, _tmpOrient);
            _tmpPoint.get(_d3);
            long l1 = Finger.incLong(l, _d3);
            _tmpOrient.get(_f3);
            l1 = Finger.incLong(l1, _f3);
            return l1;
        }
    }

    public int getCRC(int i)
    {
        if(pos == null)
        {
            return i;
        } else
        {
            pos.getAbs(_tmpPoint, _tmpOrient);
            _tmpPoint.get(_d3);
            int j = Finger.incInt(i, _d3);
            _tmpOrient.get(_f3);
            j = Finger.incInt(j, _f3);
            return j;
        }
    }

    public static final int DRAW = 1;
    public static final int VISIBILITY_AS_BASE = 2;
    public static final int COLLIDE = 16;
    public static final int COLLIDE_AS_POINT = 32;
    public static final int COLLIDE_ON_LAND = 64;
    public static final int COLLIDE_ONLY_THIS = 128;
    public static final int DREAM_FIRE = 256;
    public static final int DREAM_LISTENER = 512;
    public static final int MISSION_SPAWN = 4096;
    public static final int REAL_TIME = 8192;
    public static final int SERVICE = 16384;
    public static final int DESTROYED = 32768;
    public static final int _DEAD = 4;
    public static final int _TASK_COMPLETE = 8;
    protected int flags;
    private String name;
    public ActorNet net;
    private Actor owner;
    protected List ownerAttached;
    private static Object emptyArrayOwners[] = new Object[0];
    public ActorPos pos;
    public Interpolators interp;
    public Mat icon;
    public ActorDraw draw;
    public Acoustics acoustics;
    private static boolean bSpawnFromMission = false;
    private static Vector3d _V1 = new Vector3d();
    public static Point3d _tmpPoint = new Point3d();
    public static Orient _tmpOrient = new Orient();
    public static Loc _tmpLoc = new Loc();
    public static double _d3[] = new double[3];
    public static float _f3[] = new float[3];
    private int _hash;
    private static int _hashNext = 1;
    private static int _countActors = 0;

}
