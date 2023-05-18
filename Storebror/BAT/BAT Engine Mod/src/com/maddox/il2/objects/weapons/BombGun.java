// 26th/Feb./2020 : Merge Bomb Release mod for Dive bomber like Blackburn Sukua

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.AircraftLH;
import com.maddox.il2.objects.air.TypeHaveBombReleaseGear;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class BombGun extends Interpolate
    implements BulletEmitter
{

    public BombGun()
    {
        ready = true;
        bHide = false;
        bExternal = true;
        bCassette = false;
        bulletClass = null;
        bombDelay = 0.0F;
        isJettosoned = false;
        armingRnd = new RangeRandom(0L);
        armingTime = 2000L;
        selectedFuze = null;
        bulletMassa = 0.048F;
        numBombs = 0;
    }

    public void doDestroy()
    {
        ready = false;
        if(bomb != null)
        {
            bomb.destroy();
            bomb = null;
        }
    }

    private boolean nameEQ(HierMesh hiermesh, int i, int j)
    {
        if(hiermesh == null)
            return false;
        hiermesh.setCurChunk(i);
        String s = hiermesh.chunkName();
        hiermesh.setCurChunk(j);
        String s1 = hiermesh.chunkName();
        int l = Math.min(s.length(), s1.length());
        for(int k = 0; k < l; k++)
        {
            char c = s.charAt(k);
            if(c == '_')
                return true;
            if(c != s1.charAt(k))
                return false;
        }

        return true;
    }

    public BulletEmitter detach(HierMesh hiermesh, int i)
    {
        if(!ready)
            return GunEmpty.get();
        if(i == -1 || nameEQ(hiermesh, i, hook.chunkNum()))
        {
            ready = false;
            bExecuted = true;
            return GunEmpty.get();
        } else
        {
            return this;
        }
    }

    protected int bullets()
    {
        return actor == null ? 0 : bulletss - actor.hashCode();
    }

    protected void bullets(int i)
    {
        if(actor != null)
            bulletss = i + actor.hashCode();
        else
            bulletss = 0;
    }

    public void hide(boolean flag)
    {
        bHide = flag;
        if(bHide)
        {
            if(Actor.isValid(bomb) && bExternal)
                bomb.drawing(false);
        } else
        if(Actor.isValid(bomb) && bExternal)
            bomb.drawing(true);
    }

    public boolean isHide()
    {
        return bHide;
    }

    public boolean isEnablePause()
    {
        return false;
    }

    public boolean isPause()
    {
        return false;
    }

    public void setPause(boolean flag)
    {
    }

    public void setBombDelay(float f)
    {
        bombDelay = f;
        if(bomb != null)
            bomb.delayExplosion = bombDelay;
    }

    public void setFuzeForMirror(int i, int j, float f)
    {
        setBombDelay(f);
        if(!World.cur().diffCur.BombFuzes || (this instanceof BombGunNull) || (this instanceof TorpedoGun))
            return;
        if(selectedFuze == null)
        {
            if(bulletClass == null)
                bulletClass = (Class)Property.value(getClass(), "bulletClass", null);
            selectedFuze = Fuze.selectFuze(bulletClass, i, f, false);
        }
        if(selectedFuze instanceof Fuze_EL_AZ)
        {
            ((Fuze_EL_AZ)selectedFuze).setMirrorFuzeMode(j);
            selectedFuze.fuzeMode = Property.intValue(selectedFuze.getClass(), "fuzeSelectionMode", 0);
        }
    }

    private void enableFuzeSelectionMode()
    {
        if(selectedFuze != null)
        {
            int i = Property.intValue(selectedFuze.getClass(), "fuzeSelectionMode", 0);
            if(i != 0)
            {
                selectedFuze.fuzeMode = i;
                AircraftLH.hasFuzeModeSelector = true;
            }
        }
    }

    public void addGenericFuze()
    {
        try
        {
            selectedFuze = (Fuze)(com.maddox.il2.objects.weapons.Fuze_generic_1sec.class).newInstance();
        }
        catch(Exception exception) { }
    }

    public void selectFuzeAutomatically(boolean flag)
    {
        if(!World.cur().diffCur.BombFuzes || (this instanceof BombGunNull) || (this instanceof TorpedoGun))
            return;
        if(bulletClass == null)
            bulletClass = (Class)Property.value(getClass(), "bulletClass", null);
        UserCfg usercfg = World.cur().userCfg;
        int i = usercfg.fuzeType;
        selectedFuze = Fuze.selectFuze(bulletClass, i, bombDelay, flag);
        if(selectedFuze != null)
            setBombDelay(selectedFuze.getDetonationDelay());
        enableFuzeSelectionMode();
        if(flag && !Mission.isDogfight())
            Fuze.setStartTime();
    }

    public boolean isExternal()
    {
        return bExternal;
    }

    public boolean isCassette()
    {
        return bCassette;
    }

    public float bulletMassa()
    {
        return bulletMassa;
    }

    public int countBullets()
    {
        return bullets();
    }

    public boolean haveBullets()
    {
        return bullets() != 0;
    }

    public void loadBullets()
    {
        loadBullets(bulletsFull);
    }

    public void _loadBullets(int i)
    {
        loadBullets(i);
    }

    public void loadBullets(int i)
    {
        bullets(i);
        if(bullets() != 0)
        {
            if(!Actor.isValid(bomb))
                newBomb();
        } else
        if(Actor.isValid(bomb))
        {
            bomb.destroy();
            bomb = null;
        }
    }

    public Class bulletClass()
    {
        return bulletClass;
    }

    public void setBulletClass(Class class1)
    {
        bulletClass = class1;
        bulletMassa = Property.floatValue(bulletClass, "massa", bulletMassa);
    }

    public boolean isShots()
    {
        return bExecuted;
    }

    public void shots(int i, float f)
    {
        shots(i);
    }

    public void shots(int i)
    {
        if(isHide())
            return;
        if(isCassette() && i != 0)
            i = bullets();
        if(bullets() == -1 && i == -1)
            i = 25;
        if(!bExecuted && i != 0)
        {
            if(bullets() == 0)
                return;
            if(bomb instanceof FuelTank)
                bullets(1);
            curShotStep = 0;
            curShots = i;
            bExecuted = true;
        } else
        if(bExecuted && i != 0)
            curShots = i;
        else
        if(bExecuted && i == 0)
            bExecuted = false;
    }

    protected void interpolateStep()
    {
        bTickShot = false;
        if(curShotStep == 0)
        {
            if(bullets() == 0 || curShots == 0 || !Actor.isValid(actor))
            {
                shots(0);
                return;
            }
            if(actor instanceof Aircraft)
            {
                FlightModel flightmodel = ((Aircraft)actor).FM;
                if(flightmodel.getOverload() < 0.0F)
                    return;
            }
            if((actor instanceof TypeHaveBombReleaseGear) && !((TypeHaveBombReleaseGear)actor).waitRelease())
                return;
            bTickShot = true;
            if(bomb != null)
            {
                bomb.pos.setUpdateEnable(true);
                bomb.pos.resetAsBase();
                bomb.setSeed(armingRnd.nextInt());
                bomb.setFuse(getFuze());
                bomb.start();
                if(isJettosoned)
                    bomb.setJettisoned();
                if(sound != null)
                    sound.play();
                bomb = null;
            }
            if(curShots > 0)
                curShots--;
            if(bullets() > 0)
                bullets(bullets() - 1);
            if(bullets() != 0)
                newBomb();
            curShotStep = shotStep;
        }
        curShotStep--;
    }

    public boolean tick()
    {
        interpolateStep();
        return ready;
    }

    private Fuze getFuze()
    {
        if(selectedFuze != null && bullets() != 1)
            try
            {
                Fuze fuze = (Fuze)selectedFuze.getClass().newInstance();
                fuze.fuzeMode = selectedFuze.fuzeMode;
                fuze.setDetonationDelay(selectedFuze.getDetonationDelay());
                return fuze;
            }
            catch(Exception exception) { }
        return selectedFuze;
    }

    private void newBomb()
    {
        try
        {
            bomb = (Bomb)bulletClass.newInstance();
            bomb.index = numBombs++;
            bomb.pos.setBase(actor, hook, false);
            bomb.pos.changeHookToRel();
            bomb.pos.resetAsBase();
            if(!bExternal)
                bomb.drawing(false);
            else
                bomb.visibilityAsBase(true);
            bomb.setSeed(armingRnd.nextInt());
            bomb.pos.setUpdateEnable(false);
            bomb.delayExplosion = bombDelay;
            bomb.setFuse(getFuze());
            if(isJettosoned)
                bomb.setJettisoned();
        }
        catch(Exception exception)
        {
            System.out.println("Exception in newBomb: " + exception.toString());
        }
    }

    public String getHookName()
    {
        return hook.name();
    }

    public void set(Actor actor, String s, Loc loc1)
    {
        set(actor, s);
    }

    public void set(Actor actor, String s, String s1)
    {
        set(actor, s);
    }

    public void set(Actor actor0, String s)
    {
        actor = actor0;
        Class class1 = getClass();
        bExternal = Property.containsValue(class1, "external");
        bCassette = Property.containsValue(class1, "cassette");
        if(bulletClass == null)
            bulletClass = (Class)Property.value(class1, "bulletClass", null);
        bullets(Property.intValue(class1, "bullets", 1));
        bulletsFull = bullets();
        setBulletClass(bulletClass);
        float f = Property.floatValue(class1, "shotFreq", 0.5F);
        if(f < 0.001F)
            f = 0.001F;
        shotStep = (int)((1.0F / f + Time.tickConstLenFs() / 2.0F) / Time.tickConstLenFs());
        if(shotStep <= 0)
            shotStep = 1;
        hook = (HookNamed)actor.findHook(s);
        newBomb();
        String s1 = Property.stringValue(getClass(), "sound", null);
        if(s1 != null)
        {
            bomb.pos.getAbs(loc);
            loc.sub(actor.pos.getAbs());
            sound = actor.newSound(s1, false);
            if(sound != null)
            {
                SoundFX soundfx = actor.getRootFX();
                if(soundfx != null)
                {
                    sound.setParent(soundfx);
                    sound.setPosition(loc.getPoint());
                }
            }
        }
        actor.interpPut(this, null, -1L, null);
    }

    public void jettisonBomb()
    {
        isJettosoned = true;
        if(bomb != null)
            bomb.setJettisoned();
    }

    public void setRnd(int i)
    {
        armingRnd = new RangeRandom(i);
        if(bomb != null)
            bomb.setSeed(armingRnd.nextInt());
    }

    public void updateHook(String s)
    {
        Class class1 = getClass();
        bullets(Property.intValue(class1, "bullets", 1));
        hook = (HookNamed)actor.findHook(s);
        try
        {
            bomb.pos.destroy();
            bomb.pos = new ActorPosMove(bomb);
            bomb.pos.setBase(actor, hook, false);
            bomb.pos.changeHookToRel();
            bomb.pos.resetAsBase();
            bomb.visibilityAsBase(true);
            bomb.pos.setUpdateEnable(false);
        }
        catch(Exception exception) { }
    }

    protected boolean ready;
    protected Bomb bomb;
    protected boolean bHide;
    protected boolean bExternal;
    protected boolean bCassette;
    protected HookNamed hook;
    protected Class bulletClass;
    protected int bulletsFull;
    private int bulletss;
    protected int shotStep;
    protected float bombDelay;
    private boolean isJettosoned;
    protected SoundFX sound;
    protected long armingTime;
    protected RangeRandom armingRnd;
    private Fuze selectedFuze;
    protected float bulletMassa;
    private int curShotStep;
    private int curShots;
    protected boolean bTickShot;
    protected int numBombs;
    private static Loc loc = new Loc();

}