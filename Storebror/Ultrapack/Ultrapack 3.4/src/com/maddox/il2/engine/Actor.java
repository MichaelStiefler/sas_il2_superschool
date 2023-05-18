package com.maddox.il2.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombNull;
import com.maddox.il2.objects.weapons.Rocket;
import com.maddox.il2.objects.weapons.RocketBomb;
import com.maddox.il2.objects.weapons.RocketNull;
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

public abstract class Actor extends ObjState {

    public static boolean isValid(Actor actor) {
        return actor != null && !actor.isDestroyed();
    }

    public static boolean isAlive(Actor actor) {
        return actor != null && actor.isAlive();
    }

    public boolean isAlive() {
        return (this.flags & 0x8004) == 0;
    }

    public void setDiedFlag(boolean flag) {
        if (flag) {
            if ((this.flags & 4) == 0) {
                this.flags |= 4;
                if (this instanceof Prey) {
                    int i = Engine.targets().indexOf(this);
                    if (i >= 0) Engine.targets().remove(i);
                }
                if (isValid(this.owner)) MsgOwner.died(this.owner, this);
            }
        } else if ((this.flags & 4) != 0) {
            this.flags &= -5;
            if (this instanceof Prey) Engine.targets().add(this);
        }
    }

    public boolean getDiedFlag() {
        return (this.flags & 4) != 0;
    }

    public boolean isTaskComplete() {
        return (this.flags & 8) != 0;
    }

    public void setTaskCompleteFlag(boolean flag) {
        if (flag) {
            if ((this.flags & 8) == 0) {
                this.flags |= 8;
                if (isValid(this.owner)) MsgOwner.taskComplete(this.owner, this);
            }
        } else this.flags &= -9;
    }

    public int getArmy() {
        return this.flags >>> 16;
    }

    public void setArmy(int i) {
        this.flags = i << 16 | this.flags & 0xffff;
    }

    public boolean isRealTime() {
        return (this.flags & 0x2000) != 0 || Time.isRealOnly();
    }

    public boolean isRealTimeFlag() {
        return (this.flags & 0x2000) != 0;
    }

    public boolean isNet() {
        return this.net != null;
    }

    public boolean isNetMaster() {
        return this.net != null && this.net.isMaster();
    }

    public boolean isNetMirror() {
        return this.net != null && this.net.isMirror();
    }

    public boolean isSpawnFromMission() {
        return (this.flags & 0x1000) != 0;
    }

    public void missionStarting() {
    }

    public void setName(String s) {
        if (this.name != null) Engine.cur.name2Actor.remove(this.name);
        this.name = s;
        if (s != null) Engine.cur.name2Actor.put(this.name, this);
    }

    public String name() {
        return this.name != null ? this.name : "NONAME";
    }

    public boolean isNamed() {
        return this.name != null;
    }

    public static Actor getByName(String s) {
        return (Actor) Engine.cur.name2Actor.get(s);
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
        return new NetMsgSpawn(this.net);
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
    }

    public Actor getOwner() {
        return this.owner;
    }

    public boolean isContainOwner(Object obj) {
        if (obj == null) return false;
        if (this.owner == null) return false;
        if (this.owner.equals(obj)) return true;
        else return this.owner.isContainOwner(obj);
    }

    public Object[] getOwnerAttached() {
        if (this.ownerAttached != null) return this.ownerAttached.toArray();
        else return emptyArrayOwners;
    }

    public Object[] getOwnerAttached(Object aobj[]) {
        if (this.ownerAttached != null) return this.ownerAttached.toArray(aobj);
        else return emptyArrayOwners;
    }

    public int getOwnerAttachedCount() {
        if (this.ownerAttached != null) return this.ownerAttached.size();
        else return 0;
    }

    public int getOwnerAttachedIndex(Object obj) {
        if (this.ownerAttached != null) return this.ownerAttached.indexOf(obj);
        else return -1;
    }

    public Object getOwnerAttached(int i) {
        return this.ownerAttached.get(i);
    }

    public void setOwner(Actor actor, boolean flag, boolean flag1, boolean flag2) {
        if (actor != this.owner) {
            // TODO: +++ Enhanced Ordnance View by SAS~Storebror +++
            if ((this instanceof Bomb || this instanceof Rocket || this instanceof RocketBomb) && !(this instanceof BombNull) && !(this instanceof RocketNull)) {
//                System.out.println("old owner=" + (owner==null?"null":owner.getClass().getName()) + ", new owner=" + (actor==null?"null":actor.getClass().getName()));
                if (this.owner instanceof Aircraft) ((Aircraft) this.owner).removeOrdnance(this); // just in case... this ordnance had another owner before, so let's remove it from that owner's ordnance list first.
                if (actor instanceof Aircraft) ((Aircraft) actor).addOrdnance(this);
            }
            // TODO: --- Enhanced Ordnance View by SAS~Storebror ---
            if (isValid(this.owner) && this.owner.ownerAttached != null) {
                int i = this.owner.ownerAttached.indexOf(this);
                if (i >= 0) {
                    this.owner.ownerAttached.remove(i);
                    if (flag1) MsgOwner.detach(this.owner, this);
                }
            }
            Actor actor1 = this.owner;
            if (isValid(actor)) {
                this.owner = actor;
                if (flag) {
                    if (this.owner.ownerAttached == null) this.owner.ownerAttached = new ArrayList();
                    this.owner.ownerAttached.add(this);
                    if (flag1) MsgOwner.attach(this.owner, this);
                    if (flag2) MsgOwner.change(this, actor, actor1);
                }
            } else {
                this.owner = null;
                if (actor != null) throw new ActorException("new owner is destroyed");
                if (flag2) MsgOwner.change(this, actor, actor1);
            }
        }
    }

    public void setOwnerAfter(Actor actor, Actor actor1, boolean flag, boolean flag1, boolean flag2) {
        if (actor != this.owner) {
            if (isValid(this.owner) && this.owner.ownerAttached != null) {
                int i = this.owner.ownerAttached.indexOf(this);
                if (i >= 0) {
                    this.owner.ownerAttached.remove(i);
                    if (flag1) MsgOwner.detach(this.owner, this);
                }
            }
            Actor actor2 = this.owner;
            if (isValid(actor)) {
                this.owner = actor;
                if (flag) {
                    if (this.owner.ownerAttached == null) this.owner.ownerAttached = new ArrayList();
                    if (actor1 == null) this.owner.ownerAttached.add(0, this);
                    else {
                        int j = this.owner.ownerAttached.indexOf(actor1);
                        if (j < 0) throw new ActorException("beforeChildren not found");
                        this.owner.ownerAttached.add(j + 1, this);
                    }
                    if (flag1) MsgOwner.attach(this.owner, this);
                    if (flag2) MsgOwner.change(this, actor, actor2);
                }
            } else {
                this.owner = null;
                if (actor != null) throw new ActorException("new owner is destroyed");
                if (flag2) MsgOwner.change(this, actor, actor2);
            }
        }
    }

    public void setOwner(Actor actor) {
        this.setOwner(actor, true, true, false);
    }

    public void changeOwner(Actor actor) {
        this.setOwner(actor, true, true, true);
    }

    public Hook findHook(Object obj) {
        return null;
    }

    public float futurePosition(float f, Point3d point3d) {
        if (this.pos == null) return 0.0F;
        else {
            long l = (long) (f * 1000F + 0.5F);
            this.pos.getTime(Time.current() + l, point3d);
            return f;
        }
    }

    public float futurePosition(float f, Loc loc) {
        if (this.pos == null) return 0.0F;
        else {
            long l = (long) (f * 1000F + 0.5F);
            this.pos.getTime(Time.current() + l, loc);
            return f;
        }
    }

    public void alignPosToLand(double d, boolean flag) {
        if (this.pos == null) return;
        if (Engine.land() == null) return;
        this.pos.getAbs(_tmpPoint);
        _tmpPoint.z = Engine.land().HQ(_tmpPoint.x, _tmpPoint.y) + d;
        this.pos.setAbs(_tmpPoint);
        if (flag) this.pos.reset();
    }

    protected void interpolateTick() {
        if (this.interp != null && this.interp.size() > 0) {
            try {
                this.interp.tick((this.flags & 0x2000) == 0 ? Time.current() : Time.currentReal());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return;
        } else {
            InterpolateAdapter.adapter().removeListener(this);
            return;
        }
    }

    public boolean interpIsSleep() {
        if (this.interp != null) return this.interp.isSleep();
        else return false;
    }

    public boolean interpSleep() {
        if (this.interp != null) return this.interp.sleep();
        else return false;
    }

    public boolean interpWakeup() {
        if (this.interp != null) return this.interp.wakeup();
        else return false;
    }

    public int interpSize() {
        if (this.interp != null) return this.interp.size();
        else return 0;
    }

    public Interpolate interpGet(Object obj) {
        if (this.interp != null) return this.interp.get(obj);
        else return null;
    }

    public void interpPut(Interpolate interpolate, Object obj, long l, Message message) {
        if (this.interp == null) this.interp = new Interpolators();
        this.interp.put(interpolate, obj, l, message, this);
        if (this.interp.size() == 1) InterpolateAdapter.adapter().addListener(this);
    }

    public boolean interpEnd(Object obj) {
        if (this.interp != null) return this.interp.end(obj);
        else return false;
    }

    public void interpEndAll() {
        if (this.interp != null) this.interp.endAll();
    }

    public boolean interpCancel(Object obj) {
        if (this.interp != null) return this.interp.cancel(obj);
        else return false;
    }

    public void interpCancelAll() {
        if (this.interp != null) this.interp.cancelAll();
    }

    public boolean isDrawing() {
        return (this.flags & 1) != 0 && (this.draw != null || this.icon != null);
    }

    public boolean isIconDrawing() {
        return (this.flags & 1) != 0 && this.icon != null;
    }

    public void drawing(boolean flag) {
        if (flag != ((this.flags & 1) != 0)) {
            if (flag) this.flags |= 1;
            else this.flags &= -2;
            if (this.pos != null && this.pos.actor() == this) this.pos.drawingChange(flag);
        }
    }

    public boolean isVisibilityAsBase() {
        return (this.flags & 2) != 0;
    }

    public void visibilityAsBase(boolean flag) {
        if ((this.flags & 2) != 0 == flag) return;
        if (flag) this.flags |= 2;
        else this.flags &= -3;
        if (this.pos != null && (this.flags & 1) != 0 && this.pos.actor() == this) this.pos.drawingChange(true);
    }

    public boolean isCollide() {
        return (this.flags & 0x10) != 0;
    }

    public boolean isCollideAsPoint() {
        return (this.flags & 0x20) != 0;
    }

    public boolean isCollideAndNotAsPoint() {
        return (this.flags & 0x30) == 16;
    }

    public boolean isCollideOnLand() {
        return (this.flags & 0x40) != 0;
    }

    public void collide(boolean flag) {
        if (flag != ((this.flags & 0x10) != 0)) {
            if (flag) this.flags |= 0x10;
            else this.flags &= 0xffffffef;
            if (this.pos != null && (this.flags & 0x20) == 0 && this.pos.actor() == this) this.pos.collideChange(flag);
        }
    }

    public boolean isDreamListener() {
        return (this.flags & 0x200) != 0;
    }

    public boolean isDreamFire() {
        return (this.flags & 0x100) != 0;
    }

    public void dreamFire(boolean flag) {
        if (flag != ((this.flags & 0x100) != 0)) {
            if (flag) this.flags |= 0x100;
            else this.flags &= 0xfffffeff;
            if (this.pos != null && this.pos.actor() == this) this.pos.dreamFireChange(flag);
        }
    }

    public float collisionR() {
        return 10F;
    }

    public Acoustics acoustics() {
        Actor actor;
        for (actor = this; actor != null && actor.acoustics == null;)
            if (actor.pos != null) actor = actor.pos.base();
            else actor = null;

        if (actor != null) return actor.acoustics;
        else return Engine.worldAcoustics();
    }

    public Actor actorAcoustics() {
        Actor actor;
        for (actor = this; actor != null && actor.acoustics == null;)
            if (actor.pos != null) actor = actor.pos.base();
            else actor = null;

        return actor;
    }

    public Acoustics findParentAcoustics() {
        Actor actor = this;
        do {
            if (actor == null) break;
            if (actor.acoustics != null) return actor.acoustics;
            if (actor.pos == null) break;
            actor = actor.pos.base();
        } while (true);
        return null;
    }

    public void setAcoustics(Acoustics acoustics1) {
        if (acoustics1 == null) acoustics1 = Engine.worldAcoustics();
        this.acoustics = acoustics1;
        if (this.draw != null && this.draw.sounds != null) for (SoundFX soundfx = this.draw.sounds.get(); soundfx != null; soundfx = soundfx.next())
            soundfx.setAcoustics(this.acoustics);
        if (this.ownerAttached != null) for (int i = 0; i < this.ownerAttached.size(); i++) {
            Actor actor = (Actor) this.ownerAttached.get(i);
            actor.setAcoustics(acoustics1);
        }
    }

    public SoundFX newSound(String s, boolean flag) {
        if (this.draw == null || s == null) return null;
        if (s.equals("")) {
            System.out.println("Empty sound in " + this.toString());
            return null;
        }
        SoundFX soundfx = new SoundFX(s);
        if (soundfx.isInitialized()) {
            soundfx.setAcoustics(this.acoustics);
            soundfx.insert(this.draw.sounds(), false);
            if (flag) soundfx.play();
        } else soundfx = null;
        return soundfx;
    }

    public SoundFX newSound(SoundPreset soundpreset, boolean flag, boolean flag1) {
        if (this.draw == null || soundpreset == null) return null;
        SoundFX soundfx = new SoundFX(soundpreset);
        if (soundfx.isInitialized()) {
            soundfx.setAcoustics(this.acoustics);
            soundfx.insert(this.draw.sounds(), flag1);
            if (flag) soundfx.play();
        } else soundfx = null;
        return soundfx;
    }

    public void playSound(String s, boolean flag) {
        if (this.draw == null || s == null) return;
        if (s.equals("")) {
            System.out.println("Empty sound in " + this.toString());
            return;
        }
        SoundFX soundfx = new SoundFX(s);
        if (flag && soundfx.isInitialized()) {
            soundfx.setAcoustics(this.acoustics);
            soundfx.insert(this.draw.sounds(), true);
            soundfx.play();
        } else soundfx.play(this.pos.getAbsPoint());
    }

    public void playSound(SoundPreset soundpreset, boolean flag) {
        if (this.draw == null || soundpreset == null) return;
        SoundFX soundfx = new SoundFX(soundpreset);
        if (flag && soundfx.isInitialized()) {
            soundfx.setAcoustics(this.acoustics);
            soundfx.insert(this.draw.sounds(), true);
            soundfx.play();
        }
        soundfx.play(this.pos.getAbsPoint());
    }

    public void stopSounds() {
        if (this.draw != null && this.draw.sounds != null) for (SoundFX soundfx = this.draw.sounds.get(); soundfx != null; soundfx = soundfx.next())
            soundfx.stop();
    }

    public void breakSounds() {
        if (this.draw != null && this.draw.sounds != null) for (SoundFX soundfx = this.draw.sounds.get(); soundfx != null; soundfx = soundfx.next())
            soundfx.cancel();
    }

    public SoundFX getRootFX() {
        return null;
    }

    public boolean hasInternalSounds() {
        return false;
    }

    public boolean isDestroyed() {
        return (this.flags & 0x8000) != 0;
    }

    public void destroy() {
        if (this.isDestroyed()) return;
        this.breakSounds();
        if (this.pos != null) if (this.pos.actor() == this) {
            this.pos.reset();
            this.pos.destroy();
        } else if (isValid(this.pos.base())) this.pos.base().pos.removeChildren(this);
        if (this instanceof MsgDreamGlobalListener) Engine.dreamEnv().removeGlobalListener(this);
//        // TODO: +++ Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror +++
//        if(((this instanceof Bomb) || (this instanceof Rocket) || (this instanceof RocketBomb)) && !(this instanceof BombNull) && !(this instanceof RocketNull))
//            Engine.ordinances().remove(this);
//        // TODO: --- Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror ---
        // TODO: +++ Enhanced Ordnance View by SAS~Storebror +++
        if ((this instanceof Bomb || this instanceof Rocket || this instanceof RocketBomb) && !(this instanceof BombNull) && !(this instanceof RocketNull)) {
//                ActorPos ap = this.pos;
//                Actor owner = ap==null?null:this.pos.base();
            Actor owner = this.getOwner();
//                System.out.println("owner=" + (owner==null?"null":owner.getClass().getName()));
            if (owner instanceof Aircraft) ((Aircraft) owner).removeOrdnance(this);
        }
        // TODO: --- Enhanced Ordnance View by SAS~Storebror ---
        if (this.ownerAttached != null) {
            Actor actor;
            for (; this.ownerAttached.size() > 0; actor.changeOwner(null))
                actor = (Actor) this.ownerAttached.get(0);

        }
        this.setOwner(null);
        destroy(this.net);
        if (this.interp != null) {
            this.interp.destroy();
            this.interp = null;
            InterpolateAdapter.adapter().removeListener(this);
        }
        destroy(this.draw);
        if (this.name != null) Engine.cur.name2Actor.remove(this.name);
        if (this instanceof Prey) {
            int i = Engine.targets().indexOf(this);
            if (i >= 0) Engine.targets().remove(i);
        }
        this.flags |= 0x8000;
        super.destroy();
        _countActors--;
        if (Engine.cur != null) Engine.cur.actorDestroyed(this);
    }

    public void postDestroy() {
        Engine.postDestroyActor(this);
    }

    public void postDestroy(long l) {
        MsgDestroy.Post(l, this);
    }

    public double distance(Actor actor) {
        return this.pos.getAbsPoint().distance(actor.pos.getAbsPoint());
    }

    public int target_O_Clock(Actor actor) {
        if (actor == null || actor.pos == null || _V1 == null) return 12;
        _V1.sub(actor.pos.getAbsPoint(), this.pos.getAbsPoint());
        this.pos.getAbsOrient().transformInv(_V1);
        float f = 57.32484F * (float) Math.atan2(_V1.y, -_V1.x);
        int i = (int) f;
        i = ((i + 180) % 360 + 15) / 30;
        if (i == 0) i = 12;
        float f1 = (float) _V1.length() + 0.1F;
        float f2 = (float) (actor.pos.getAbsPoint().z - this.pos.getAbsPoint().z) / f1;
        if (f2 > 0.4F) i += 12;
        else if (f2 < -0.4F) i += 24;
        return i;
    }

    public double getSpeed(Vector3d vector3d) {
        return this.pos.speed(vector3d);
    }

    public void setSpeed(Vector3d vector3d) {
    }

    public static void setSpawnFromMission(boolean flag) {
        bSpawnFromMission = flag;
    }

    public static int countAll() {
        return _countActors;
    }

    public boolean isGameActor() {
        return this._hash > 0;
    }

    protected Actor() {
        this.flags = 0;
        this.acoustics = null;
        this.createActorHashCode();
        if (bSpawnFromMission) this.flags |= 0x1000;
        _countActors++;
        if (this instanceof MsgDreamGlobalListener) Engine.dreamEnv().addGlobalListener(this);
        if (this instanceof Prey) Engine.targets().add(this);

//        // TODO: +++ Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror +++
//        if(((this instanceof Bomb) || (this instanceof Rocket) || (this instanceof RocketBomb)) && !(this instanceof BombNull) && !(this instanceof RocketNull))
//            Engine.ordinances().add(this);
//        // TODO: --- Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror ---
//        // TODO: +++ Enhanced Ordnance View by SAS~Storebror +++
//        if(((this instanceof Bomb) || (this instanceof Rocket) || (this instanceof RocketBomb)) && !(this instanceof BombNull) && !(this instanceof RocketNull)) {
//            ActorPos ap = this.pos;
//            Actor owner = ap==null?null:this.pos.base();
//            System.out.println("owner=" + (owner==null?"null":owner.getClass().getName()));
//            if (owner instanceof Aircraft) {
//                ((Aircraft) owner).removeOrdnance(this);
//            }
//        }
//        // TODO: --- Enhanced Ordnance View by SAS~Storebror ---
    }

    protected void createActorHashCode() {
        this.makeActorGameHashCode();
    }

    protected void makeActorRealHashCode() {
        this._hash = -Math.abs(super.hashCode());
    }

    protected void makeActorGameHashCode() {
        this._hash = _hashNext++;
    }

    protected static void resetActorGameHashCodes() {
        _hashNext = 1;
    }

    public static int _getCurHashNextCode() {
        return _hashNext;
    }

    public int hashCode() {
        return this._hash;
    }

    public long getCRC(long l) {
        if (this.pos == null) return l;
        else {
            this.pos.getAbs(_tmpPoint, _tmpOrient);
            _tmpPoint.get(_d3);
            long l1 = Finger.incLong(l, _d3);
            _tmpOrient.get(_f3);
            l1 = Finger.incLong(l1, _f3);
            return l1;
        }
    }

    public int getCRC(int i) {
        if (this.pos == null) return i;
        else {
            this.pos.getAbs(_tmpPoint, _tmpOrient);
            _tmpPoint.get(_d3);
            int j = Finger.incInt(i, _d3);
            _tmpOrient.get(_f3);
            j = Finger.incInt(j, _f3);
            return j;
        }
    }

    public static final int DRAW               = 1;
    public static final int VISIBILITY_AS_BASE = 2;
    public static final int COLLIDE            = 16;
    public static final int COLLIDE_AS_POINT   = 32;
    public static final int COLLIDE_ON_LAND    = 64;
    public static final int COLLIDE_ONLY_THIS  = 128;
    public static final int DREAM_FIRE         = 256;
    public static final int DREAM_LISTENER     = 512;
    public static final int MISSION_SPAWN      = 4096;
    public static final int REAL_TIME          = 8192;
    public static final int SERVICE            = 16384;
    public static final int DESTROYED          = 32768;
    public static final int _DEAD              = 4;
    public static final int _TASK_COMPLETE     = 8;
    protected int           flags;
    private String          name;
    public ActorNet         net;
    private Actor           owner;
    protected List          ownerAttached;
    private static Object   emptyArrayOwners[] = new Object[0];
    public ActorPos         pos;
    public Interpolators    interp;
    public Mat              icon;
    public ActorDraw        draw;
    public Acoustics        acoustics;
    private static boolean  bSpawnFromMission  = false;
    private static Vector3d _V1                = new Vector3d();
    public static Point3d   _tmpPoint          = new Point3d();
    public static Orient    _tmpOrient         = new Orient();
    public static Loc       _tmpLoc            = new Loc();
    public static double    _d3[]              = new double[3];
    public static float     _f3[]              = new float[3];
    private int             _hash;
    private static int      _hashNext          = 1;
    private static int      _countActors       = 0;
}
