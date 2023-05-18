package com.maddox.il2.engine;

import java.util.ArrayList;
import java.util.List;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.Message;
import com.maddox.rts.NetEnv;
import com.maddox.rts.RTSConf;
import com.maddox.sound.Acoustics;
import com.maddox.util.HashMapExt;

public class Engine {

    public static boolean isServer() {
        return NetEnv.isServer();
    }

    public static Landscape land() {
        return cur.land;
    }

    public static Actor actorLand() {
        return cur.actorLand;
    }

    public static LightEnv lightEnv() {
        return cur.lightEnv;
    }

    public static DrawEnv drawEnv() {
        return cur.drawEnv;
    }

    public static CollideEnv collideEnv() {
        return cur.collideEnv;
    }

    public static DreamEnv dreamEnv() {
        return cur.dreamEnv;
    }

    public static List targets() {
        return cur.targets;
    }

    // TODO: Guided Missiles Update
    public static List missiles() {
        return cur.missiles;
    }

    public static List countermeasures() {
        return cur.countermeasures;
    }

    public static void setWorldAcoustics(String s) {
        cur.worldAcoustics = new Acoustics(s);
    }

    public static Acoustics worldAcoustics() {
        return cur.worldAcoustics;
    }

    public static ActorSoundListener soundListener() {
        return cur.soundListener;
    }

    public static Renders rendersMain() {
        return cur.rendersMain;
    }

    public static InterpolateAdapter interpolateAdapter() {
        return cur.interpolateAdapter;
    }

    protected void actorDestroyed(Actor actor) {
        for (int i = 0; i < this.actorDestroyListeners.size(); i++)
            ((ActorDestroyListener) this.actorDestroyListeners.get(i)).actorDestroyed(actor);

    }

    public void addActorDestroyListener(ActorDestroyListener actordestroylistener) {
        if (!this.actorDestroyListeners.contains(actordestroylistener)) this.actorDestroyListeners.add(actordestroylistener);
    }

    public void removeActorDestroyListener(ActorDestroyListener actordestroylistener) {
        int i = this.actorDestroyListeners.indexOf(actordestroylistener);
        if (i >= 0) this.actorDestroyListeners.remove(i);
    }

    public static void processPostDestroyActors() {
        int i = cur.queueDestroyActors.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) cur.queueDestroyActors.get(j);
            if (Actor.isValid(actor)) actor.destroy();
        }

        cur.queueDestroyActors.clear();
    }

    public static void postDestroyActor(Actor actor) {
        cur.queueDestroyActors.add(actor);
    }

    public static HashMapExt name2Actor() {
        return cur.name2Actor;
    }

    public static void destroyListGameActors(List list) {
        int i = list.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) list.get(j);
            if (!Actor.isValid(actor) || (actor.flags & 0x4000) != 0) continue;
            try {
                actor.destroy();
            } catch (Exception exception) {
                printDebug(exception);
            }
        }

        list.clear();
    }

    private void dectroyMsgActors(Message amessage[]) {
        if (amessage == null) return;
        for (int i = 0; i < amessage.length && amessage[i] != null; i++) {
            Object obj = amessage[i].listener();
            if (obj instanceof Actor) {
                Actor actor = (Actor) obj;
                if (Actor.isValid(actor) && (actor.flags & 0x4000) == 0) try {
                    actor.destroy();
                } catch (Exception exception) {
                    printDebug(exception);
                }
            }
            amessage[i] = null;
        }

    }

    public void resetGameClear() {
        while (this.bulletList != null) {
            BulletGeneric bulletgeneric = this.bulletList;
            this.bulletList = bulletgeneric.nextBullet;
            bulletgeneric.destroy();
            bulletgeneric.nextBullet = null;
        }
        this.targets.clear();
        ArrayList arraylist = new ArrayList();
        cur.drawEnv.resetGameClear();
        cur.collideEnv.resetGameClear();
        cur.dreamEnv.resetGameClear();
        cur.interpolateAdapter.resetGameClear();
        this.actorLand.destroy();
        arraylist.addAll(this.allEff3DActors.keySet());
        destroyListGameActors(arraylist);
        arraylist.addAll(this.name2Actor.values());
        destroyListGameActors(arraylist);
        MeshShared.clearAll();
        cur.world.resetGameClear();
        this.dectroyMsgActors(RTSConf.cur.queue.toArray());
        this.dectroyMsgActors(RTSConf.cur.queueNextTick.toArray());
        processPostDestroyActors();
        GObj.DeleteCppObjects();
    }

    public void resetGameCreate() {
        Actor.resetActorGameHashCodes();
        cur.world.resetGameCreate();
        cur.collideEnv.resetGameCreate();
        cur.drawEnv.resetGameCreate();
        cur.lightEnv.clear();
        cur.dreamEnv.resetGameCreate();
        cur.interpolateAdapter.resetGameCreate();
        this.actorLand = new ActorLand();
        this.soundListener = new ActorSoundListener();
        this.soundListener.initDraw();
    }

//    // TODO: +++ Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror +++
//    public static List ordinances()
//    {
//        return cur.ordinances;
//    }
//    // TODO: --- Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror ---

    public Engine() {
        this.world = new World();
        this.lightEnv = new LightEnvXY(this.world.sun());
        this.drawEnv = new DrawEnvXY();
        this.collideEnv = new CollideEnvXY();
        this.dreamEnv = new DreamEnvXY();
        this.targets = new ArrayList();
        this.worldAcoustics = null;
        this.soundListener = null;
        this.interpolateAdapter = new InterpolateAdapter();
        this.actorDestroyListeners = new ArrayList();
        this.profile = new EngineProfile();
        this.queueDestroyActors = new ArrayList();
        this.name2Actor = new HashMapExt();
        this.allEff3DActors = new HashMapExt();
        this.allActors = new HashMapExt();
        this.posChanged = new ArrayList();
        cur = this;
        this.rendersMain = new Renders();
        this.land = new Landscape();
        this.actorLand = new ActorLand();
        this.soundListener = new ActorSoundListener();
//        // TODO: +++ Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror +++
//        ordinances = new ArrayList();
//        // TODO: --- Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror ---
    }

    protected static void printDebug(Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    public static Engine        cur;
    public static final boolean CHECK_DESTROY_ACTORS = false;
    public Landscape            land;
    public World                world;
    public Actor                actorLand;
    public BulletGeneric        bulletList;
    public LightEnv             lightEnv;
    public DrawEnv              drawEnv;
    public CollideEnv           collideEnv;
    public DreamEnv             dreamEnv;
    protected ArrayList         targets;

    // TODO: Guided Missiles Update
    protected ArrayList          missiles;
    protected ArrayList          countermeasures;

    private Acoustics            worldAcoustics;
    protected ActorSoundListener soundListener;
    private Renders              rendersMain;
    protected InterpolateAdapter interpolateAdapter;
    private ArrayList            actorDestroyListeners;
    protected EngineProfile      profile;
    private ArrayList            queueDestroyActors;
    protected HashMapExt         name2Actor;
    protected HashMapExt         allEff3DActors;
    protected HashMapExt         allActors;
    protected ArrayList          posChanged;
//    // TODO: +++ Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror +++
//    protected ArrayList ordinances;
//    // TODO: --- Stationary Camera and Ordnance View Backport from 4.13.4 by SAS~Storebror ---
}
