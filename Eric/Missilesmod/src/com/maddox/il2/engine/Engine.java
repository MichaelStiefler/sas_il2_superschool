// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 01.06.2013 16:03:35
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Engine.java

package com.maddox.il2.engine;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.*;
import com.maddox.sound.Acoustics;
import com.maddox.util.HashMapExt;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.engine:
//            ActorDestroyListener, Actor, ActorSoundListener, LightEnvXY, 
//            DrawEnvXY, CollideEnvXY, DreamEnvXY, InterpolateAdapter, 
//            EngineProfile, Renders, Landscape, BulletGeneric, 
//            DrawEnv, CollideEnv, DreamEnv, MeshShared, 
//            GObj, LightEnv

public class Engine
{

    public static boolean isServer()
    {
        return NetEnv.isServer();
    }

    public static Landscape land()
    {
        return cur.land;
    }

    public static Actor actorLand()
    {
        return cur.actorLand;
    }

    public static LightEnv lightEnv()
    {
        return cur.lightEnv;
    }

    public static DrawEnv drawEnv()
    {
        return cur.drawEnv;
    }

    public static CollideEnv collideEnv()
    {
        return cur.collideEnv;
    }

    public static DreamEnv dreamEnv()
    {
        return cur.dreamEnv;
    }

    public static List targets()
    {
        return cur.targets;
    }
    
//TODO: For RWR
    public static List missiles()
    {
        return cur.missiles;
    }
    
    public static List countermeasures()
    {
        return cur.countermeasures;
    }

    public static void setWorldAcoustics(String s)
    {
        cur.worldAcoustics = new Acoustics(s);
    }

    public static Acoustics worldAcoustics()
    {
        return cur.worldAcoustics;
    }

    public static ActorSoundListener soundListener()
    {
        return cur.soundListener;
    }

    public static Renders rendersMain()
    {
        return cur.rendersMain;
    }

    public static InterpolateAdapter interpolateAdapter()
    {
        return cur.interpolateAdapter;
    }

    protected void actorDestroyed(Actor actor)
    {
        for(int i = 0; i < actorDestroyListeners.size(); i++)
            ((ActorDestroyListener)actorDestroyListeners.get(i)).actorDestroyed(actor);

    }

    public void addActorDestroyListener(ActorDestroyListener actordestroylistener)
    {
        if(!actorDestroyListeners.contains(actordestroylistener))
            actorDestroyListeners.add(actordestroylistener);
    }

    public void removeActorDestroyListener(ActorDestroyListener actordestroylistener)
    {
        int i = actorDestroyListeners.indexOf(actordestroylistener);
        if(i >= 0)
            actorDestroyListeners.remove(i);
    }

    public static void processPostDestroyActors()
    {
        int i = cur.queueDestroyActors.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)cur.queueDestroyActors.get(j);
            if(Actor.isValid(actor))
                actor.destroy();
        }

        cur.queueDestroyActors.clear();
    }

    public static void postDestroyActor(Actor actor)
    {
        cur.queueDestroyActors.add(actor);
    }

    public static HashMapExt name2Actor()
    {
        return cur.name2Actor;
    }

    public static void destroyListGameActors(List list)
    {
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!Actor.isValid(actor) || (actor.flags & 0x4000) != 0)
                continue;
            try
            {
                actor.destroy();
            }
            catch(Exception exception)
            {
                printDebug(exception);
            }
        }

        list.clear();
    }

    private void dectroyMsgActors(Message amessage[])
    {
        if(amessage == null)
            return;
        for(int i = 0; i < amessage.length && amessage[i] != null; i++)
        {
            Object obj = amessage[i].listener();
            if(obj instanceof Actor)
            {
                Actor actor = (Actor)obj;
                if(Actor.isValid(actor) && (actor.flags & 0x4000) == 0)
                    try
                    {
                        actor.destroy();
                    }
                    catch(Exception exception)
                    {
                        printDebug(exception);
                    }
            }
            amessage[i] = null;
        }

    }

    public void resetGameClear()
    {
        while(bulletList != null) 
        {
            BulletGeneric bulletgeneric = bulletList;
            bulletList = bulletgeneric.nextBullet;
            bulletgeneric.destroy();
            bulletgeneric.nextBullet = null;
        }
        targets.clear();
//TODO: For RWR
        missiles.clear();
        
        countermeasures.clear();
        
        ArrayList arraylist = new ArrayList();
        cur.drawEnv.resetGameClear();
        cur.collideEnv.resetGameClear();
        cur.dreamEnv.resetGameClear();
        cur.interpolateAdapter.resetGameClear();
        actorLand.destroy();
        arraylist.addAll(allEff3DActors.keySet());
        destroyListGameActors(arraylist);
        arraylist.addAll(name2Actor.values());
        destroyListGameActors(arraylist);
        MeshShared.clearAll();
        cur.world.resetGameClear();
        dectroyMsgActors(RTSConf.cur.queue.toArray());
        dectroyMsgActors(RTSConf.cur.queueNextTick.toArray());
        processPostDestroyActors();
        GObj.DeleteCppObjects();
    }

    public void resetGameCreate()
    {
        Actor.resetActorGameHashCodes();
        cur.world.resetGameCreate();
        cur.collideEnv.resetGameCreate();
        cur.drawEnv.resetGameCreate();
        cur.lightEnv.clear();
        cur.dreamEnv.resetGameCreate();
        cur.interpolateAdapter.resetGameCreate();
        actorLand = new ActorLand();
        soundListener = new ActorSoundListener();
        soundListener.initDraw();
    }

    public Engine()
    {
        world = new World();
        lightEnv = new LightEnvXY(world.sun());
        drawEnv = new DrawEnvXY();
        collideEnv = new CollideEnvXY();
        dreamEnv = new DreamEnvXY();
        targets = new ArrayList();
//TODO: For RWR
        missiles = new ArrayList();
        
        countermeasures = new ArrayList();
        
        worldAcoustics = null;
        soundListener = null;
        interpolateAdapter = new InterpolateAdapter();
        actorDestroyListeners = new ArrayList();
        profile = new EngineProfile();
        queueDestroyActors = new ArrayList();
        name2Actor = new HashMapExt();
        allEff3DActors = new HashMapExt();
        allActors = new HashMapExt();
        posChanged = new ArrayList();
        cur = this;
        rendersMain = new Renders();
        land = new Landscape();
        actorLand = new ActorLand();
        soundListener = new ActorSoundListener();
    }

    protected static void printDebug(Exception exception)
    {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    public static Engine cur;
    public static final boolean CHECK_DESTROY_ACTORS = false;
    public Landscape land;
    public World world;
    public Actor actorLand;
    public BulletGeneric bulletList;
    public LightEnv lightEnv;
    public DrawEnv drawEnv;
    public CollideEnv collideEnv;
    public DreamEnv dreamEnv;
    protected ArrayList targets;
//TODO: for RWR
    protected ArrayList missiles;
    
    protected ArrayList countermeasures;
    
    private Acoustics worldAcoustics;
    protected ActorSoundListener soundListener;
    private Renders rendersMain;
    protected InterpolateAdapter interpolateAdapter;
    private ArrayList actorDestroyListeners;
    protected EngineProfile profile;
    private ArrayList queueDestroyActors;
    protected HashMapExt name2Actor;
    protected HashMapExt allEff3DActors;
    protected HashMapExt allActors;
    protected ArrayList posChanged;
}