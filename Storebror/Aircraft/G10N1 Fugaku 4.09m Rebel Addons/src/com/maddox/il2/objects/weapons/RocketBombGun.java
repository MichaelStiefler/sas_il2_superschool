package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class RocketBombGun extends RocketGun //Interpolate
    implements BulletEmitter
{

    public RocketBombGun()
    {
        ready = true;
        bHide = false;
        bRocketPosRel = true;
        bulletClass = null;
        bombDelay = 0.0F;
        bExternal = true;
        bCassette = false;
        timeLife = -1F;
        plusPitch = 0.0F;
        plusYaw = 0.0F;
        bulletMassa = 0.048F;
    }

    public void doDestroy()
    {
        ready = false;
        if(rocket != null)
        {
            rocket.destroy();
            rocket = null;
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
        int k = Math.min(s.length(), s1.length());
        for(int l = 0; l < k; l++)
        {
            char c = s.charAt(l);
            if(c == '_')
                return true;
            if(c != s1.charAt(l))
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
            bExecuted = true;
            ready = false;
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
            if(Actor.isValid(rocket))
                rocket.drawing(false);
        } else
        if(Actor.isValid(rocket))
            rocket.drawing(true);
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

    public boolean isExternal()
    {
        return bExternal;
    }

    public boolean isCassette()
    {
        return bCassette;
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = f;
    }

    public float getRocketTimeLife()
    {
        return timeLife;
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
            if(!Actor.isValid(rocket))
                newRocket();
        } else
        if(Actor.isValid(rocket))
        {
            rocket.destroy();
            rocket = null;
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
        if(!isHide())
        {
            if(isCassette() && i != 0)
                i = bullets();
            if(bullets() == -1 && i == -1)
                i = 25;
            if(!bExecuted && i != 0)
            {
                if(bullets() != 0)
                {
                    curShotStep = 0;
                    curShots = i;
                    bExecuted = true;
                }
            } else
            if(bExecuted && i != 0)
                curShots = i;
            else
            if(bExecuted && i == 0)
                bExecuted = false;
        }
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
            bTickShot = true;
            if(rocket != null)
            {
                rocket.pos.setUpdateEnable(true);
                if(plusPitch != 0.0F || plusYaw != 0.0F)
                {
                    rocket.pos.getAbs(_tmpOr0);
                    _tmpOr1.setYPR(plusYaw, plusPitch, 0.0F);
                    _tmpOr1.add(_tmpOr0);
                    rocket.pos.setAbs(_tmpOr1);
                }
                rocket.pos.resetAsBase();
                rocket.start(timeLife);
                if(Actor.isValid(rocket))
                {
                    String s = Property.stringValue(getClass(), "sound", null);
                    if(s != null)
                        rocket.newSound(s, true);
                }
                rocket = null;
            }
            if(curShots > 0)
                curShots--;
            if(bullets() > 0)
                bullets(bullets() - 1);
            if(bullets() != 0)
                newRocket();
            curShotStep = shotStep;
        }
        curShotStep--;
    }

    public boolean tick()
    {
        interpolateStep();
        return ready;
    }

    private void newRocket()
    {
        try
        {
            rocket = (RocketBomb)bulletClass.newInstance();
            rocket.pos.setBase(actor, hook, false);
            if(bRocketPosRel)
                rocket.pos.changeHookToRel();
            rocket.pos.resetAsBase();
            rocket.visibilityAsBase(true);
            if(bRocketPosRel)
                rocket.pos.setUpdateEnable(false);
            setRocketName();
        }
        catch(Exception exception) { }
    }

    void setRocketName()
    {
        rocket.setName(getHookName() + "|" + counter);
        counter++;
    }

    public void setHookToRel(boolean flag)
    {
        if(bRocketPosRel != flag)
        {
            if(Actor.isValid(rocket))
                if(flag)
                {
                    rocket.pos.changeHookToRel();
                    rocket.pos.setUpdateEnable(false);
                } else
                {
                    rocket.pos.setRel(nullLoc);
                    rocket.pos.setBase(rocket.pos.base(), hook, false);
                    rocket.pos.setUpdateEnable(true);
                }
            bRocketPosRel = flag;
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

    public void set(Actor actor, String s)
    {
        this.actor = actor;
        Class class1 = getClass();
        bCassette = Property.containsValue(class1, "cassette");
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
        newRocket();
        String s1 = Property.stringValue(getClass(), "sound", null);
        if(s1 != null)
        {
            rocket.pos.getAbs(loc);
            loc.sub(this.actor.pos.getAbs());
            sound = this.actor.newSound(s1, false);
            if(sound != null)
            {
                SoundFX soundfx = this.actor.getRootFX();
                if(soundfx != null)
                {
                    sound.setParent(soundfx);
                    sound.setPosition(loc.getPoint());
                }
            }
        }
        this.actor.interpPut(this, null, -1L, null);
    }

    protected boolean ready;
    protected RocketBomb rocket;
    protected HookNamed hook;
    protected boolean bHide;
    protected boolean bRocketPosRel;
    protected Class bulletClass;
    protected int bulletsFull;
    protected int bulletss;
    protected int shotStep;
    protected float bombDelay;
    protected SoundFX sound;
    protected boolean bExternal;
    private boolean bCassette;
    protected float timeLife;
    private float plusPitch;
    private float plusYaw;
    protected float bulletMassa;
    private int curShotStep;
    private int curShots;
    protected boolean bTickShot;
    private static Orient _tmpOr0 = new Orient();
    private static Orient _tmpOr1 = new Orient();
    private static Loc nullLoc = new Loc();
    private static Loc loc = new Loc();
    private int counter;

}
