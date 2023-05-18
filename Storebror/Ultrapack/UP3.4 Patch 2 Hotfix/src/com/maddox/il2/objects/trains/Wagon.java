package com.maddox.il2.objects.trains;

import java.io.IOException;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Time;

public abstract class Wagon extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey, TgtTrain {
    public class MyMsgAction extends MsgAction {

        public void doAction(Object obj) {
        }

        Object obj2;

        public MyMsgAction(double d, Object obj, Object obj1) {
            super(d, obj);
            this.obj2 = obj1;
        }
    }

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                byte byte2 = netmsginput.readByte();
                switch (byte2) {
                    case 83:
                    case 115:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted);
                        }
                        float f = netmsginput.readFloat();
                        if (f <= 0.0F) {
                            f = -1F;
                        }
                        Train.TrainState trainstate = new Train.TrainState();
                        trainstate._headSeg = netmsginput.readInt();
                        trainstate._headAlong = netmsginput.readDouble();
                        trainstate._curSpeed = netmsginput.readFloat();
                        trainstate._milestoneDist = netmsginput.readDouble();
                        trainstate._requiredSpeed = netmsginput.readFloat();
                        trainstate._maxAcceler = netmsginput.readFloat();
                        Wagon.this.LifeChanged(false, f, null, true);
                        if (Wagon.this.getOwner() != null) {
                            boolean flag = byte2 == 115;
                            ((Train) Wagon.this.getOwner()).setStateDataMirror(trainstate, flag);
                        }
                        Wagon.this.forgetAllAiming();
                        return true;

                    case 73:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted1);
                        }
                        float f1 = netmsginput.readFloat();
                        if (f1 <= 0.0F) {
                            f1 = -1F;
                        }
                        Wagon.this.LifeChanged(false, f1, null, true);
                        Wagon.this.forgetAllAiming();
                        return true;

                    case 68:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                            this.post(netmsgguaranted2);
                        }
                        if (Wagon.this.life > 0.0F) {
                            NetObj netobj2 = netmsginput.readNetObj();
                            Actor actor2 = netobj2 != null ? ((ActorNet) netobj2).actor() : null;
                            Wagon.this.LifeChanged(false, 0.0F, actor2, false);
                        }
                        return true;
                }
                return false;
            }
            switch (netmsginput.readByte()) {
                default:
                    break;

                case 84:
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    byte byte0 = netmsginput.readByte();
                    NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                    int i = netmsginput.readUnsignedByte();
                    Wagon.this.Track_Mirror(byte0, actor, i);
                    break;

                case 70:
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    byte byte1 = netmsginput.readByte();
                    NetObj netobj1 = netmsginput.readNetObj();
                    Actor actor1 = netobj1 != null ? ((ActorNet) netobj1).actor() : null;
                    float f2 = netmsginput.readFloat();
                    float f3 = (0.001F * (Message.currentGameTime() - Time.current())) + f2;
                    int j = netmsginput.readUnsignedByte();
                    Wagon.this.Fire_Mirror(byte1, actor1, j, f3);
                    break;

                case 68:
                    this.out.unLockAndSet(netmsginput, 1);
                    this.out.setIncludeTime(false);
                    this.postRealTo(Message.currentRealTime(), this.masterChannel(), this.out);
                    break;
            }
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                return true;
            }
            if (netmsginput.readByte() != 68) {
                return false;
            }
            if (Wagon.this.life <= 0.0F) {
                return true;
            } else {
                NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                Wagon.this.LifeChanged(false, 0.0F, actor, false);
                return true;
            }
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    public class Pair {

        Wagon victim;
        Actor initiator;

        Pair(Wagon wagon1, Actor actor) {
            this.victim = wagon1;
            this.initiator = actor;
        }
    }

    protected void forgetAllAiming() {
    }

    public int HitbyMask() {
        return -1;
    }

    public int chooseBulletType(BulletProperties abulletproperties[]) {
        if (this.IsDamaged()) {
            return -1;
        }
        if (abulletproperties.length == 1) {
            return 0;
        }
        if (abulletproperties.length <= 0) {
            return -1;
        }
        if (abulletproperties[0].power <= 0.0F) {
            return 0;
        }
        if (abulletproperties[1].power <= 0.0F) {
            return 1;
        }
        if (abulletproperties[0].cumulativePower > 0.0F) {
            return 0;
        }
        if (abulletproperties[1].cumulativePower > 0.0F) {
            return 1;
        }
        if (abulletproperties[0].powerType == 1) {
            return 0;
        }
        if (abulletproperties[1].powerType == 1) {
            return 1;
        }
        return abulletproperties[0].powerType != 0 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        return !this.IsDamaged() ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.IsDamaged() || (i != 0)) {
            return false;
        }
        if (point3d != null) {
            point3d.set(0.0D, 0.0D, 0.0D);
        }
        return true;
    }

    public final boolean isStaticPos() {
        return false;
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private final boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    final boolean IsDamaged() {
        return this.life <= 0.0F;
    }

    final boolean IsDead() {
        return this.life < 0.0F;
    }

    final boolean IsDeadOrDying() {
        return this.life <= 0.0F;
    }

    final float getLength() {
        return this.hook1 - this.hook2;
    }

    private void changePoseAsCrushed(Point3d point3d, Orient orient) {
        Wagon.tmp_rnd.setSeed(211 * this.crushSeed);
        orient.get(Wagon.tmp_atk);
        Wagon.tmp_atk[0] += Wagon.tmp_rnd.nextFloat(-13F, 13F);
        Wagon.tmp_atk[1] += Wagon.tmp_rnd.nextFloat(-2F, 2.0F);
        Wagon.tmp_atk[2] += Wagon.tmp_rnd.nextFloat(-8F, 8F);
        orient.set(Wagon.tmp_atk[0], Wagon.tmp_atk[1], Wagon.tmp_atk[2]);
        point3d.x += Wagon.tmp_rnd.nextDouble(-0.80000000000000004D, 0.80000000000000004D);
        point3d.y += Wagon.tmp_rnd.nextDouble(-0.90000000000000002D, 0.90000000000000002D);
        point3d.z += Wagon.tmp_rnd.nextDouble(-0.25D, 0.0D);
    }

    void place(Point3d point3d, Point3d point3d1, boolean flag, boolean flag1) {
        if (flag1) {
            return;
        }
        Orient orient = new Orient();
        Point3d point3d2 = new Point3d();
        point3d2.interpolate(point3d, point3d1, this.hook1 / (this.hook1 - this.hook2));
        point3d2.z += this.heightAboveLandSurface;
        Vector3d vector3d = new Vector3d();
        vector3d.sub(point3d, point3d1);
        orient.setAT0(vector3d);
        if (this.life < 0.0F) {
            this.changePoseAsCrushed(point3d2, orient);
        }
        this.pos.setAbs(point3d2, orient);
        if (flag) {
            this.pos.reset();
        }
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        if (actor instanceof Wagon) {
            Actor actor1 = this.getOwner();
            Actor actor2 = actor.getOwner();
            if (actor1 == actor2) {
                aflag[0] = false;
                return;
            }
            if (((Train) actor1).stoppedForever() && ((Train) actor2).stoppedForever()) {
                aflag[0] = false;
                return;
            } else {
                return;
            }
        }
        if (((Train) this.getOwner()).stoppedForever() && (actor instanceof ActorMesh) && ((ActorMesh) actor).isStaticPos()) {
            aflag[0] = false;
            return;
        }
        if (actor instanceof BridgeSegment) {
            aflag[0] = false;
            return;
        } else {
            return;
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((this.life < 0.0F) || this.isNetMirror()) {
            return;
        }
        if (actor instanceof Wagon) {
            this.LifeChanged(false, 0.0F, actor, false);
        }
    }

    protected float killProbab(float f) {
        float f1 = this.life;
        float f2 = 3.9E+009F * (f1 * f1 * f1);
        float f3 = f / f2;
        float f4;
        if (f3 <= 1.0F) {
            f3 = (f3 * 2.0F) - 1.0F;
            if (f3 <= 0.0F) {
                return 0.0F;
            }
            f4 = f3 * Wagon.PROBAB_DEATH_WHEN_SHOT;
        } else {
            if (f3 >= 10F) {
                return 1.0F;
            }
            f3 = (f3 - 1.0F) / 9F;
            f4 = Wagon.PROBAB_DEATH_WHEN_SHOT + (0.96F * f3);
        }
        return f4;
    }

    public void msgShot(Shot shot) {
        if (shot.chunkName.startsWith("Armor") && (shot.power <= 20450F)) {
            return;
        }
        shot.bodyMaterial = this.bodyMaterial;
        if (this.IsDamaged() || (shot.power <= 0.0F) || (this.isNetMirror() && shot.isMirage())) {
            return;
        }
        if (shot.powerType == 1) {
            if (this.RndB(0.125F)) {
                return;
            } else {
                this.LifeChanged(this.isNetMirror(), 0.0F, shot.initiator, false);
                return;
            }
        }
        if (!this.RndB(this.killProbab(shot.power))) {
            return;
        } else {
            this.LifeChanged(this.isNetMirror(), 0.0F, shot.initiator, false);
            return;
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (this.IsDamaged() || (this.isNetMirror() && explosion.isMirage())) {
            return;
        }
        if (Explosion.killable(this, explosion.receivedTNT_1meter(this), this.ignoreTNT, this.killTNT, 0.0F)) {
            this.LifeChanged(this.isNetMirror(), 0.0F, explosion.initiator, false);
        }
    }

    protected void hiddenexplode() {
    }

    protected void explode(Actor actor) {
        new MsgAction(0.0D, this) {

            public void doAction(Object obj) {
                Wagon wagon = (Wagon) obj;
                try {
                    Eff3DActor.New(wagon, new HookNamed(wagon, "Damage"), null, 1.0F, "Effects/Smokes/WagonFC.eff", 60F);
                } catch (ActorException actorexception) {
                    Eff3DActor.New(wagon, new HookNamed(wagon, "Select1"), null, 1.0F, "Effects/Smokes/WagonFC.eff", 60F);
                }
            }

        };
        new MsgAction(2.5D) {

            public void doAction() {
                Point3d point3d = new Point3d();
                Wagon.this.pos.getAbs(point3d);
                Explosions.ExplodeVagonArmor(point3d, point3d, 2.0F);
            }

        };
        new MsgAction(4.5D, new Pair(this, actor)) {

            public void doAction(Object obj) {
                Actor actor1 = Wagon.this.getOwner();
                if (actor1 != null) {
                    ((Train) actor1).wagonDied(((Pair) obj).victim, ((Pair) obj).initiator);
                }
                Wagon.this.life = -1F;
                Wagon.this.ActivateMesh();
            }

        };
    }

    private final void LifeChanged(boolean flag, float f, Actor actor, boolean flag1) {
        if (flag1) {
            if (f > 0.0F) {
                this.life = f;
            } else {
                this.life = -1F;
            }
            if (this.life < 0.0F) {
                this.crushSeed = (byte) World.Rnd().nextInt(1, 127);
                World.onActorDied(this, actor);
                this.hiddenexplode();
            }
            this.ActivateMesh();
            return;
        }
        if (f <= 0.0F) {
            if (this.life <= 0.0F) {
                return;
            }
            if (flag) {
                this.life = 0.001F;
                this.send_DeathRequest(actor);
                return;
            }
            this.life = 0.0F;
        } else {
            this.life = f;
            return;
        }
        this.crushSeed = (byte) World.Rnd().nextInt(1, 127);
        this.explode(actor);
        World.onActorDied(this, actor);
        if (!this.isNetMirror()) {
            this.send_DeathCommand(actor);
        }
    }

    public void absoluteDeath(Actor actor) {
        if (!this.getDiedFlag()) {
            World.onActorDied(this, actor);
        }
        this.destroy();
    }

    public void destroy() {
        super.destroy();
    }

    protected void ActivateMesh() {
        boolean flag = this.IsDead();
        this.setMesh(flag ? this.meshDead : this.meshLive);
        if (!flag) {
            this.heightAboveLandSurface = 0.0F;
            int i = this.hierMesh().hookFind("Ground_Level");
            if (i != -1) {
                Matrix4d matrix4d = new Matrix4d();
                this.hierMesh().hookMatrix(i, matrix4d);
                this.heightAboveLandSurface = (float) (-matrix4d.m23);
            }
            i = this.hierMesh().hookFind("Select1");
            if (i != -1) {
                Matrix4d matrix4d1 = new Matrix4d();
                this.hierMesh().hookMatrix(i, matrix4d1);
                this.hook1 = (float) matrix4d1.m03;
            } else {
                throw new ActorException("Wagon: hook Select1 not found in " + this.meshLive);
            }
            i = this.hierMesh().hookFind("Select2");
            if (i != -1) {
                Matrix4d matrix4d2 = new Matrix4d();
                this.hierMesh().hookMatrix(i, matrix4d2);
                this.hook2 = (float) matrix4d2.m03;
            } else {
                throw new ActorException("Wagon: hook Select2 not found in " + this.meshLive);
            }
            if (this.hook1 <= this.hook2) {
                throw new ActorException("Wagon: hooks SelectX placed incorrectly in " + this.meshLive);
            }
        }
    }

    public Wagon(Train train, String s, String s1) {
        super(s);
        this.hook1 = 1.0F;
        this.hook2 = -1F;
        this.heightAboveLandSurface = 0.0F;
        this.life = 0.017F;
        this.ignoreTNT = 0.35F;
        this.killTNT = 1.9F;
        this.bodyMaterial = 2;
        this.outCommand = new NetMsgFiltered();
        this.collide(true);
        this.drawing(true);
        this.setOwner(train);
        this.setArmy(train.getArmy());
        this.meshLive = new String(s);
        this.meshDead = new String(s1);
        this.life = 1E-005F;
        this.ActivateMesh();
        int i = Mission.cur().getUnitNetIdRemote(this);
        NetChannel netchannel = Mission.cur().getNetMasterChannel();
        if (netchannel == null) {
            this.net = new Master(this);
        } else if (i != 0) {
            this.net = new Mirror(this, netchannel, i);
        }
    }

    private void send_DeathCommand(Actor actor) {
        if (!this.isNetMaster()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeNetObj(actor.net);
            if (this.net != null) this.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void send_FireCommand(int i, Actor actor, int j, float f) {
        if (!this.isNetMaster() || !this.net.isMirrored() || !Actor.isValid(actor) || !actor.isNet()) {
            return;
        }
        j &= 0xff;
        if (f < 0.0F) {
            try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(84);
                this.outCommand.writeByte(i);
                this.outCommand.writeNetObj(actor.net);
                this.outCommand.writeByte(j);
                this.outCommand.setIncludeTime(false);
                if (this.net != null) this.net.post(Time.current(), this.outCommand);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        } else {
            try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(70);
                this.outCommand.writeByte(i);
                this.outCommand.writeFloat(f);
                this.outCommand.writeNetObj(actor.net);
                this.outCommand.writeByte(j);
                this.outCommand.setIncludeTime(true);
                if (this.net != null) this.net.post(Time.current(), this.outCommand);
            } catch (Exception exception1) {
                System.out.println(exception1.getMessage());
                exception1.printStackTrace();
            }
        }
    }

    private void send_DeathRequest(Actor actor) {
        if (!this.isNetMirror() || (this.net.masterChannel() instanceof NetChannelInStream)) {
            return;
        }
        try {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(68);
            netmsgfiltered.writeNetObj(actor.net);
            netmsgfiltered.setIncludeTime(false);
            if (this.net != null) this.net.postTo(Time.current(), this.net.masterChannel(), netmsgfiltered);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void Track_Mirror(int i, Actor actor, int j) {
        if (this.IsDamaged()) {
            return;
        } else {
            return;
        }
    }

    protected void Fire_Mirror(int i, Actor actor, int j, float f) {
        if (this.IsDamaged()) {
            return;
        } else {
            return;
        }
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        if (this instanceof LocomotiveVerm) {
            Train train = (Train) this.getOwner();
            netmsgguaranted.writeByte(train.isAnybodyDead() ? 115 : 83);
            netmsgguaranted.writeFloat(this.life);
            Train.TrainState trainstate = new Train.TrainState();
            train.getStateData(trainstate);
            netmsgguaranted.writeInt(trainstate._headSeg);
            netmsgguaranted.writeDouble(trainstate._headAlong);
            netmsgguaranted.writeFloat(trainstate._curSpeed);
            netmsgguaranted.writeDouble(trainstate._milestoneDist);
            netmsgguaranted.writeFloat(trainstate._requiredSpeed);
            netmsgguaranted.writeFloat(trainstate._maxAcceler);
        } else {
            netmsgguaranted.writeByte(73);
            netmsgguaranted.writeFloat(this.life);
        }
        if (this.net != null) this.net.postTo(netchannel, netmsgguaranted);
    }

    private static final float PROBAB_DEATH_WHEN_SHOT = 0.04F;
    private float              hook1;
    private float              hook2;
    private float              heightAboveLandSurface;
    private String             meshLive;
    private String             meshDead;
    protected byte             crushSeed;
    protected float            life;
    protected float            ignoreTNT;
    protected float            killTNT;
    protected int              bodyMaterial;
    private static float       tmp_atk[]              = new float[3];
    private static RangeRandom tmp_rnd                = new RangeRandom(0L);
    private NetMsgFiltered     outCommand;

}
