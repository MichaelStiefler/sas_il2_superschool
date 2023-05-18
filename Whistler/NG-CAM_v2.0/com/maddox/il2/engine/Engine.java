// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 21/11/2018 10:01:57
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
//            ActorDestroyListener, Actor, BulletGeneric, DrawEnv, 
//            CollideEnv, DreamEnv, InterpolateAdapter, MeshShared, 
//            GObj, LightEnv, ActorSoundListener, LightEnvXY, 
//            DrawEnvXY, CollideEnvXY, DreamEnvXY, EngineProfile, 
//            Renders, Landscape

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

    public static List ordinances()
    {
        return cur.ordinances;
    }

    public static List targets()
    {
        return cur.targets;
    }

    public static List missiles()
    {
        return cur.missiles;
    }

    public static List countermeasures()
    {
        return cur.countermeasures;
    }

    public static void setWorldAcoustics(String namePresets)
    {
        cur.worldAcoustics = new Acoustics(namePresets);
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

    protected void actorDestroyed(Actor a)
    {
        for(int i = 0; i < actorDestroyListeners.size(); i++)
            ((ActorDestroyListener)actorDestroyListeners.get(i)).actorDestroyed(a);

    }

    public void addActorDestroyListener(ActorDestroyListener listener)
    {
        if(!actorDestroyListeners.contains(listener))
            actorDestroyListeners.add(listener);
    }

    public void removeActorDestroyListener(ActorDestroyListener listener)
    {
        int indx = actorDestroyListeners.indexOf(listener);
        if(indx >= 0)
            actorDestroyListeners.remove(indx);
    }

    public static void processPostDestroyActors()
    {
        int n = cur.queueDestroyActors.size();
        for(int i = 0; i < n; i++)
        {
            Actor a = (Actor)cur.queueDestroyActors.get(i);
            if(Actor.isValid(a))
                a.destroy();
        }

        cur.queueDestroyActors.clear();
    }

    public static void postDestroyActor(Actor a)
    {
        cur.queueDestroyActors.add(a);
    }

    public static HashMapExt name2Actor()
    {
        return cur.name2Actor;
    }

    public static void destroyListGameActors(List lst)
    {
        int n = lst.size();
        for(int i = 0; i < n; i++)
        {
            Actor a = (Actor)lst.get(i);
            if(Actor.isValid(a) && (a.flags & 0x4000) == 0)
                try
                {
                    a.destroy();
                }
                catch(Exception e)
                {
                    printDebug(e);
                }
        }

        lst.clear();
    }

    private void dectroyMsgActors(Message m[])
    {
        if(m == null)
            return;
        for(int i = 0; i < m.length; i++)
        {
            if(m[i] == null)
                break;
            Object listener = m[i].listener();
            if(listener instanceof Actor)
            {
                Actor a = (Actor)listener;
                if(Actor.isValid(a) && (a.flags & 0x4000) == 0)
                    try
                    {
                        a.destroy();
                    }
                    catch(Exception e)
                    {
                        printDebug(e);
                    }
            }
            m[i] = null;
        }

    }

    public void resetGameClear()
    {
        while(bulletList != null) 
        {
            BulletGeneric bullet = bulletList;
            bulletList = bullet.nextBullet;
            bullet.destroy();
            bullet.nextBullet = null;
        }
        aimerBullets.clear();
        targets.clear();
        missiles.clear();
        countermeasures.clear();
        ArrayList lst = new ArrayList();
        cur.drawEnv.resetGameClear();
        cur.collideEnv.resetGameClear();
        cur.dreamEnv.resetGameClear();
        cur.interpolateAdapter.resetGameClear();
        actorLand.destroy();
        lst.addAll(allEff3DActors.keySet());
        destroyListGameActors(lst);
        lst.addAll(name2Actor.values());
        destroyListGameActors(lst);
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
        missiles = new ArrayList();
        countermeasures = new ArrayList();
        world = new World();
        aimerBullets = new ArrayList();
        lightEnv = new LightEnvXY(world.sun());
        drawEnv = new DrawEnvXY();
        collideEnv = new CollideEnvXY();
        dreamEnv = new DreamEnvXY();
        ordinances = new ArrayList();
        targets = new ArrayList();
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

    protected static void printDebug(Exception e)
    {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    public static Engine cur;
    public static final boolean CHECK_DESTROY_ACTORS = false;
    protected ArrayList missiles;
    protected ArrayList countermeasures;
    public Landscape land;
    public World world;
    public Actor actorLand;
    public ArrayList aimerBullets;
    public BulletGeneric bulletList;
    public LightEnv lightEnv;
    public DrawEnv drawEnv;
    public CollideEnv collideEnv;
    public DreamEnv dreamEnv;
    protected ArrayList ordinances;
    protected ArrayList targets;
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