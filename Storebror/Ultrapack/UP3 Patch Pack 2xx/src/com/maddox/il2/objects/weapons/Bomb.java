/* 4.101 class used from Storebor. UP Compatible. */
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.Chat;
import com.maddox.il2.objects.ActorCrater;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;
import java.util.AbstractCollection;

public class Bomb extends ActorMesh implements MsgCollisionRequestListener, MsgCollisionListener {

    class Interpolater extends Interpolate {

        public boolean tick() {
            interpolateTick();
            return true;
        }

        Interpolater() {}
    }

    static class DelayParam {

        Actor   other;
        String  otherChunk;
        Point3d p;
        Loc     loc;

        DelayParam(Actor actor, String s, Loc loc1) {
            p = new Point3d();
            other = actor;
            otherChunk = s;
            loc1.get(p);
            if (Actor.isValid(actor)) {
                loc = new Loc();
                other.pos.getTime(Time.current(), Bomb.__loc);
                loc.sub(loc1, Bomb.__loc);
            }
        }
    }

    private static class PlateFilter implements ActorFilter {

        public boolean isUse(Actor actor, double d) {
            if (!(actor instanceof Plate)) {
                return true;
            }
            Mesh mesh = ((ActorMesh) actor).mesh();
            mesh.getBoundBox(Bomb.plateBox);
            Bomb.corn1.set(Bomb.corn);
            Loc loc1 = actor.pos.getAbs();
            loc1.transformInv(Bomb.corn1);
            if ((double) (Bomb.plateBox[0] - 2.5F) < Bomb.corn1.x && Bomb.corn1.x < (double) (Bomb.plateBox[3] + 2.5F) && (double) (Bomb.plateBox[1] - 2.5F) < Bomb.corn1.y && Bomb.corn1.y < (double) (Bomb.plateBox[4] + 2.5F)) {
                Bomb.bPlateExist = true;
            }
            return true;
        }

        private PlateFilter() {}
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        if (actor == getOwner()) {
            aflag[0] = false;
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        pos.getTime(Time.current(), p);
        impact = Time.current() - started;
        if (impact < armingTime && isArmed) {
//            System.out.println("Bomb NOT armed!");
//            System.out.println("================================");
            isArmed = false;
        }
        // TODO: Added by |ZUTI|
        // -----------------------------
        else if (impact >= armingTime && !isArmed) {
            if (!(actor instanceof ActorLand)) {
//                System.out.println("Bomb impacted >" + actor.name() + "< in more than 2s...");
//                System.out.println("================================");
                isArmed = true;
            }
        }
        // -----------------------------

        if (actor != null && (actor instanceof ActorLand) && isPointApplicableForJump()) {
            if (speed.z >= 0.0D) {
                return;
            }
            float f = (float) speed.length();
            if (f >= 30F) {
                dir.set(speed);
                dir.scale(1.0F / f);
                if (-dir.z < 0.31F) {
                    pos.getAbs(or);
                    dirN.set(1.0F, 0.0F, 0.0F);
                    or.transform(dirN);
                    if (dir.dot(dirN) >= 0.91F) {
                        float f1 = -dir.z;
                        f1 *= 3.225806F;
                        f1 = 0.85F - 0.35F * f1;
                        // TODO: Storebror: Bomb Spread Replication
                        // ------------------------------------
                        f1 *= this.getRnd().nextFloat(0.85F, 1.0F);
                        // ------------------------------------
                        speed.scale(f1);
                        speed.z *= f1;
                        if (speed.z < 0.0D) {
                            speed.z = -speed.z;
                        }
                        p.z = Engine.land().HQ(p.x, p.y);
                        pos.setAbs(p);
                        if (M >= 200F) {
                            f1 = 1.0F;
                        } else if (M <= 5F) {
                            f1 = 0.0F;
                        } else {
                            f1 = (M - 5F) / 195F;
                        }
                        float f2 = 3.5F + f1 * 12F;
                        if (Engine.land().isWater(p.x, p.y)) {
                            Explosions.SomethingDrop_Water(p, f2);
                        } else {
                            Explosions.Explode10Kg_Land(p, 2.0F, 2.0F);
                        }
                        return;
                    }
                }
            }
        }
        if (getOwner() == World.getPlayerAircraft() && !(actor instanceof ActorLand)) {
            World.cur().scoreCounter.bombHit++;
            if (Mission.isNet() && (actor instanceof Aircraft) && ((Aircraft) actor).isNetPlayer()) {
                Chat.sendLogRnd(3, "gore_bombed", (Aircraft) getOwner(), (Aircraft) actor);
            }
        }
        if (delayExplosion > 0.0F) {
            // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
            Bomb.printDebug(this.getOwner(), "Bomb " + Bomb.simpleClassName(this) + " msgCollision, delayExplosion=" + delayExplosion);
            // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
            pos.getTime(Time.current(), loc);
            collide(false);
            drawing(false);
            // TODO: Storebror: +++ Make Bomb stop moving after impact when delayed explosion is set
            this.speed.set(0,0,0);
            // TODO: Storebror: --- Make Bomb stop moving after impact when delayed explosion is set
            DelayParam delayparam = new DelayParam(actor, s1, loc);
            if (p.z < Engine.land().HQ(p.x, p.y) + 5D) {
                if (Engine.land().isWater(p.x, p.y)) {
                    Explosions.Explode10Kg_Water(p, 2.0F, 2.0F);
                } else {
                    Explosions.Explode10Kg_Land(p, 2.0F, 2.0F);
                }
            }
            interpEndAll();
            (new MsgInvokeMethod_Object("doDelayExplosion", delayparam)).post(this, delayExplosion);
            if (sound != null) {
                sound.cancel();
            }
        } else {
            doExplosion(actor, s1);
        }
    }

    private boolean isPointApplicableForJump() {
        if (Engine.land().isWater(p.x, p.y)) {
            return true;
        }
        float f = 200F;
        bPlateExist = false;
        p.get(corn);
        Engine.drawEnv().getFiltered((AbstractCollection) null, corn.x - (double) f, corn.y - (double) f, corn.x + (double) f, corn.y + (double) f, 1, plateFilter);
        if (bPlateExist) {
            return true;
        }
        int i = Engine.cur.land.HQ_RoadTypeHere(p.x, p.y);
        switch (i) {
            case 1: // '\001'
                return true;

            case 2: // '\002'
                return true;

            case 3: // '\003'
                return false;
        }
        return false;
    }

    public void doDelayExplosion(Object obj) {
        DelayParam delayparam = (DelayParam) obj;
        // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
        Bomb.printDebug(this.getOwner(), "Bomb " + Bomb.simpleClassName(this) + " doDelayExplosion");
        // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        if (Actor.isValid(delayparam.other)) {
            delayparam.other.pos.getTime(Time.current(), __loc);
            delayparam.loc.add(__loc);
            doExplosion(delayparam.other, delayparam.otherChunk, delayparam.loc.getPoint());
        } else {
            doExplosion(Engine.actorLand(), "Body", delayparam.p);
        }
    }

    protected void doExplosion(Actor actor, String s) {
        // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
        Bomb.printDebug(this.getOwner(), "Bomb " + Bomb.simpleClassName(this) + " doExplosion w/o Point3d");
        // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        pos.getTime(Time.current(), p);
        if (isArmed()) {
            doExplosion(actor, s, p);
        } else {
            if (p.z < Engine.land().HQ(p.x, p.y) + 5D) {
                if (Engine.land().isWater(p.x, p.y)) {
                    Explosions.Explode10Kg_Water(p, 2.0F, 2.0F);
                } else {
                    Explosions.Explode10Kg_Land(p, 2.0F, 2.0F);
                }
            }
            destroy();
        }
    }

    protected void doExplosion(Actor actor, String s, Point3d point3d) {
        // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
        Bomb.printDebug(this.getOwner(), "Bomb " + Bomb.simpleClassName(this) + " doExplosion w/ Point3d");
        // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 1000F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 150F);
        
        // TODO: Storebror: Random Bomb Explosion Radius
        // ------------------------------------
        f1 *= getRnd().nextFloat(0.85F, 1.0F);
        // ------------------------------------
        
        int j = Property.intValue(class1, "newEffect", 0);
        int k = Property.intValue(class1, "nuke", 0);
        if (isArmed()) {
            MsgExplosion.send(actor, s, point3d, getOwner(), M, f, i, f1, k);
            ActorCrater.initOwner = getOwner();
           // Explosions.generate(actor, point3d, f, i, f1, !Mission.isNet(), j);
            Explosions.generate(actor, point3d, f, i, f1, false, j);
            ActorCrater.initOwner = null;
            destroy();
        } else {
            destroy();
        }
    }

    private boolean isArmed() {
        return isArmed || (this instanceof BombSD2A) || (this instanceof BombSD4HL) || (this instanceof BombB22EZ);
    }

    public boolean isFuseArmed() {
        return isArmed;
    }

    public void interpolateTick() {
        curTm += Time.tickLenFs();
        Ballistics.updateBomb(this, M, S, J, DistFromCMtoStab);
        updateSound();
    }

    // TODO: Storebror: Bomb Spread Replication
    // ------------------------------------
    float RndFloatSign(float f, float f1) {
        f = this.getRnd().nextFloat(f, f1);
        return this.getRnd().nextFloat(0.0F, 1.0F) <= 0.5F ? -f : f;
    }

    private void randomizeStart(Orient orient, Vector3d vector3d, float f, int i) {
        // ------------------------------------
        if (i != 0) {
            dir.set(RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F));
            dir.normalize();
        } else {
            dir.set(1.0F, 0.0F, 0.0F);
            orient.transform(dir);
            float f1 = 0.04F;
            // TODO: Storebror: Bomb Spread Replication
            // ------------------------------------
            dir.add(this.getRnd().nextFloat(-f1, f1), this.getRnd().nextFloat(-f1, f1), this.getRnd().nextFloat(-f1, f1));
            // ------------------------------------
            dir.normalize();
        }
        orient.setAT0(dir);
        vector3d.set(RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F));
        vector3d.normalize();
        float f2 = Geom.DEG2RAD(RndFloatSign(2.0F, 35F));
        if (f > 60F) {
            float f3 = 0.05F;
            if (f < 350F) {
                f3 = 1.0F - (f - 60F) / 290F;
                f3 = f3 * 0.95F + 0.05F;
            }
            f2 *= f3;
        }
        if (i != 0) {
            f2 *= 0.2F;
        }
        vector3d.scale(f2);
    }

    public double getSpeed(Vector3d vector3d) {
        if (vector3d != null) {
            vector3d.set(speed);
        }
        return speed.length();
    }

    public void setSpeed(Vector3d vector3d) {
        speed.set(vector3d);
    }

    protected void init(float f, float f1) {
        if (Actor.isValid(getOwner()) && World.getPlayerAircraft() == getOwner()) {
            setName("_bomb_");
        }
        super.getSpeed(speed);
        if (f1 > 35F && World.cur().diffCur.Wind_N_Turbulence) {
            Point3d point3d = new Point3d();
            Vector3d vector3d = new Vector3d();
            pos.getAbs(point3d);
            World.wind().getVectorWeapon(point3d, vector3d);
            speed.add(-vector3d.x, -vector3d.y, 0.0D);
        }
        S = (float) ((Math.PI * (double) f * (double) f) / 4D);
        M = f1;
        // TODO: Storebror: Bomb Spread Replication
        // ------------------------------------
        M *= this.getRnd().nextFloat(1.0F, 1.06F);
        // ------------------------------------
        float f2 = f * 0.5F;
        float f3 = f * 4F;
        float f4 = f2;
        float f5 = f3 * 0.5F;
        J = M * 0.1F * (f4 * f4 * f5 * f5);
        DistFromCMtoStab = f3 * 0.05F;
    }

    protected void updateSound() {
        if (sound != null) {
            sound.setControl(200, (float) getSpeed(null));
            if (curTm < 5F) {
                sound.setControl(201, curTm);
            } else if (curTm < 5F + (float) (2 * Time.tickConstLen())) {
                sound.setControl(201, 5F);
            }
        }
    }

    protected boolean haveSound() {
        return true;
    }

    public void start() {
        Class class1 = getClass();
        init(Property.floatValue(class1, "kalibr", 0.082F), Property.floatValue(class1, "massa", 6.8F));
        started = Time.current();
        curTm = 0.0F;
        setOwner(pos.base(), false, false, false);
        pos.setBase(null, null, true);
        pos.setAbs(pos.getCurrent());
        pos.getAbs(or);
        randomizeStart(or, rotAxis, M, Property.intValue(class1, "randomOrient", 0));
        pos.setAbs(or);
        getSpeed(spd);
        pos.getAbs(P, Or);
        Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
        vector3d.x += this.getRnd().nextFloat_Dome(-2F, 2.0F);
        vector3d.y += this.getRnd().nextFloat_Dome(-1.2F, 1.2F);
        Or.transform(vector3d);
        spd.add(vector3d);
        setSpeed(spd);
        getSpeed(spd);
        collide(true);
        interpPut(new Interpolater(), null, Time.current(), null);
        drawing(true);
        if (Actor.isAlive(World.getPlayerAircraft()) && getOwner() == World.getPlayerAircraft()) {
            World.cur().scoreCounter.bombFire++;
            World.cur();
            FlightModel flightmodel = World.getPlayerFM();
            flightmodel.M.computeParasiteMass(flightmodel.CT.Weapons);
            flightmodel.getW().y -= 0.0004F * Math.min(M, 50F);
        }
        if (Property.containsValue(class1, "emitColor")) {
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor((Color3f) Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
            lightpointactor.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
            draw.lightMap().put("light", lightpointactor);
        }
        if (haveSound()) {
            String s = Property.stringValue(class1, "sound", null);
            if (s != null) {
                sound = newSound(s, true);
            }
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    // TODO: Storebror: Bomb Spread Replication
    // ------------------------------------
    public void setSeed(int seedValue) {
        this.seed = seedValue;
        this.rnd = new RangeRandom(this.seed);
//        System.out.println("Bomb setSeed(" + seedValue + ")");
    }

    public RangeRandom getRnd()
    {
      return this.rnd;
    }
    // ------------------------------------

    public Bomb() {
        setMesh(MeshShared.get(Property.stringValue(getClass(), "mesh", null)));
        isArmed = true;
        armingTime = 2000L;
        delayExplosion = 0.0F;
        speed = new Vector3d();
        rotAxis = new Vector3d(0.0D, 1.0D, 0.0D);
        sound = null;
        String s = Property.stringValue(getClass(), "mesh", null);
        setMesh(MeshShared.get(s));
        flags |= 0xe0;
        collide(false);
        drawing(true);
        // TODO: Storebror: Bomb Spread Replication
        // ------------------------------------
        this.rnd = new RangeRandom(TrueRandom.nextInt());
        // ------------------------------------
    }

    private long                 started;
    private long                 impact;
    private boolean              isArmed;
    public long                  armingTime;
    static Vector3d              spd            = new Vector3d();
    static Orient                Or             = new Orient();
    static Point3d               P              = new Point3d();
    protected float              delayExplosion;
    float                        curTm;
    protected Vector3d           speed;
    protected float              S;
    protected float              M;
    protected float              J;
    protected float              DistFromCMtoStab;
    Vector3d                     rotAxis;
    protected int                index;
    private static Point3d       p              = new Point3d();
    private static Vector3f      dir            = new Vector3f();
    private static Vector3f      dirN           = new Vector3f();
    private static Orient        or             = new Orient();
    private static Loc           loc            = new Loc();
    private static PlateFilter   plateFilter    = new PlateFilter();
    private static Point3d       corn           = new Point3d();
    private static Point3d       corn1          = new Point3d();
    private static float         plateBox[]     = new float[6];
    private static boolean       bPlateExist    = false;
    private static Loc           __loc          = new Loc();
    protected SoundFX            sound;
    protected static final float SND_TIME_BOUND = 5F;
    // TODO: Storebror: Bomb Spread Replication
    // ------------------------------------
    RangeRandom                  rnd;
    public int                   seed;
    // ------------------------------------
    
    // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
    private static int       debugLevel                 = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT              = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE)
            debugLevel = Config.cur.ini.get("Mods", "DEBUG_BOMB", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebug(Actor actor, String theMessage) {
        if (curDebugLevel() == 0)
            return;
        if (actor == null) {
            System.out.println("[DEBUG_BOMB] (null) " + theMessage);
            return;
        }
        if (!(actor instanceof NetAircraft)) {
            System.out.println("[DEBUG_BOMB] " + actor.name() + " (" + simpleClassName(actor) + ") " + theMessage);
            return;
        }
        NetAircraft netAircraft = (NetAircraft) actor;
        if (netAircraft.netUser() != null) {
            System.out.println("[DEBUG_BOMB] " + netAircraft.netUser().uniqueName() + " (" + simpleClassName(actor) + ") " + theMessage);
        } else {
            System.out.println("[DEBUG_BOMB] " + netAircraft.name() + " (" + simpleClassName(actor) + ") " + theMessage);
        }

    }

    public static void printDebug(String theMessage) {
        if (curDebugLevel() == 0)
            return;
        System.out.println("[DEBUG_BOMB] " + theMessage);
    }
    
    public static String simpleClassName(Object o) {
        String fullClassName = o.getClass().getName();
        return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    }
    // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
}