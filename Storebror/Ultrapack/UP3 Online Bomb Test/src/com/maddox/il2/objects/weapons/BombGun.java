package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;

public class BombGun extends Interpolate implements BulletEmitter {

    public BombGun() {
        ready = true;
        bHide = false;
        bExternal = true;
        bCassette = false;
        bulletClass = null;
        bombDelay = 0.0F;
        // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//        armingTime = 2000L;
        armingTime = 0L;
        // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
        // FIXME: Storebror: +++ TEST Bomb/Rocket Fuze/Delay Replication
        if (!Config.isUSE_RENDER()) bombDelay = 0.5F;
        // FIXME: Storebror: --- TEST Bomb/Rocket Fuze/Delay Replication
        bulletMassa = 0.048F;
        numBombs = 0;
    }

    public void doDestroy() {
        ready = false;
        if (bomb != null) {
            bomb.destroy();
            bomb = null;
        }
    }

    private boolean nameEQ(HierMesh hiermesh, int i, int j) {
        if (hiermesh == null)
            return false;
        hiermesh.setCurChunk(i);
        String s = hiermesh.chunkName();
        hiermesh.setCurChunk(j);
        String s1 = hiermesh.chunkName();
        int l = Math.min(s.length(), s1.length());
        for (int k = 0; k < l; k++) {
            char c = s.charAt(k);
            if (c == '_')
                return true;
            if (c != s1.charAt(k))
                return false;
        }

        return true;
    }

    public BulletEmitter detach(HierMesh hiermesh, int i) {
        if (!ready)
            return GunEmpty.get();
        if (i == -1 || nameEQ(hiermesh, i, hook.chunkNum())) {
            ready = false;
            bExecuted = true;
            return GunEmpty.get();
        } else {
            return this;
        }
    }

    protected int bullets() {
        return actor == null ? 0 : bulletss - actor.hashCode();
    }

    protected void bullets(int i) {
        if (actor != null)
            bulletss = i + actor.hashCode();
        else
            bulletss = 0;
    }

    public void hide(boolean flag) {
        bHide = flag;
        if (bHide) {
            if (Actor.isValid(bomb) && bExternal)
                bomb.drawing(false);
        } else if (Actor.isValid(bomb) && bExternal)
            bomb.drawing(true);
    }

    public boolean isHide() {
        return bHide;
    }

    public boolean isEnablePause() {
        return false;
    }

    public boolean isPause() {
        return false;
    }

    public void setPause(boolean flag) {}

    public void setBombDelay(float f) {
        bombDelay = f;
        if (bomb != null)
            bomb.delayExplosion = bombDelay;
    }

    public boolean isExternal() {
        return bExternal;
    }

    public boolean isCassette() {
        return bCassette;
    }

    public float bulletMassa() {
        return bulletMassa;
    }

    public int countBullets() {
        return bullets();
    }

    public boolean haveBullets() {
        return bullets() != 0;
    }

    public void loadBullets() {
        loadBullets(bulletsFull);
    }

    public void _loadBullets(int i) {
        loadBullets(i);
    }

    public void loadBullets(int i) {
        bullets(i);
        if (bullets() != 0) {
            if (!Actor.isValid(bomb))
                newBomb();
        } else if (Actor.isValid(bomb)) {
            bomb.destroy();
            bomb = null;
        }
    }

    public Class bulletClass() {
        return bulletClass;
    }

    public void setBulletClass(Class class1) {
        bulletClass = class1;
        bulletMassa = Property.floatValue(bulletClass, "massa", bulletMassa);
    }

    public boolean isShots() {
        return bExecuted;
    }

    public void shots(int i, float f) {
        shots(i);
    }

    public void shots(int i) {
        if (isHide())
            return;
        if (isCassette() && i != 0)
            i = bullets();
        if (bullets() == -1 && i == -1)
            i = 25;
        if (!bExecuted && i != 0) {
            if (bullets() == 0)
                return;
            if (bomb instanceof FuelTank)
                bullets(1);
            curShotStep = 0;
            curShots = i;
            bExecuted = true;
        } else if (bExecuted && i != 0)
            curShots = i;
        else if (bExecuted && i == 0)
            bExecuted = false;
    }

    protected void interpolateStep() {
        bTickShot = false;
        if (curShotStep == 0) {
            if (bullets() == 0 || curShots == 0 || !Actor.isValid(actor)) {
                shots(0);
                return;
            }
            if (actor instanceof Aircraft) {
                FlightModel flightmodel = ((Aircraft) actor).FM;
                if (flightmodel.getOverload() < 0.0F)
                    return;
            }
            bTickShot = true;
            if (bomb != null) {
                bomb.pos.setUpdateEnable(true);
                bomb.pos.resetAsBase();
                // TODO: Storebror: Bomb Spread Replication
                // ------------------------------------
                bomb.setSeed(this.armingRnd.nextInt());
                // ------------------------------------
                bomb.start();
                if (sound != null)
                    sound.play();
                bomb = null;
            }
            if (curShots > 0)
                curShots--;
            if (bullets() > 0)
                bullets(bullets() - 1);
            if (bullets() != 0)
                newBomb();
            curShotStep = shotStep;
        }
        curShotStep--;
    }

    public boolean tick() {
        interpolateStep();
        return ready;
    }

    private void newBomb() {
        try {
            bomb = (Bomb) bulletClass.newInstance();
            bomb.index = numBombs++;
            bomb.pos.setBase(actor, hook, false);
            bomb.pos.changeHookToRel();
            bomb.pos.resetAsBase();
            if (!bExternal)
                bomb.drawing(false);
            else
                bomb.visibilityAsBase(true);
            // TODO: Storebror: Bomb Spread Replication
            // ------------------------------------
            bomb.setSeed(this.armingRnd.nextInt());
            // ------------------------------------
            bomb.pos.setUpdateEnable(false);
            bomb.delayExplosion = bombDelay;
            bomb.armingTime = armingTime;
            // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
            Bomb.printDebug(this.actor, "BombGun " + Bomb.simpleClassName(this) + " newBomb, bombDelay=" + bombDelay + ", armingTime=" + armingTime);
            // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        } catch (Exception exception) {}
    }

    public String getHookName() {
        return hook.name();
    }

    public void set(Actor actor, String s, Loc loc1) {
        set(actor, s);
    }

    public void set(Actor actor, String s, String s1) {
        set(actor, s);
    }

    public void set(Actor actor, String s) {
        this.actor = actor;
        Class class1 = getClass();
        bExternal = Property.containsValue(class1, "external");
        bCassette = Property.containsValue(class1, "cassette");
        bulletClass = (Class) Property.value(class1, "bulletClass", null);
        bullets(Property.intValue(class1, "bullets", 1));
        bulletsFull = bullets();
        setBulletClass(bulletClass);
        float f = Property.floatValue(class1, "shotFreq", 0.5F);
        if (f < 0.001F)
            f = 0.001F;
        shotStep = (int) ((1.0F / f + Time.tickConstLenFs() / 2.0F) / Time.tickConstLenFs());
        if (shotStep <= 0)
            shotStep = 1;
        hook = (HookNamed) actor.findHook(s);
        newBomb();
        String s1 = Property.stringValue(getClass(), "sound", null);
        if (s1 != null) {
            bomb.pos.getAbs(loc);
            loc.sub(this.actor.pos.getAbs());
            sound = this.actor.newSound(s1, false);
            if (sound != null) {
                SoundFX soundfx = this.actor.getRootFX();
                if (soundfx != null) {
                    sound.setParent(soundfx);
                    sound.setPosition(loc.getPoint());
                }
            }
        }
        this.actor.interpPut(this, null, -1L, null);
    }

    public void setArmingTime(long l) {
        // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
        Bomb.printDebug(this.actor, "BombGun " + Bomb.simpleClassName(this) + " setArmingTime(" + l + ")");
        // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        armingTime = l;
        if (bomb != null)
            bomb.armingTime = l;
    }
    
    // TODO: Storebror: Bomb Spread Replication
    // ------------------------------------
    public void setRnd(int paramInt)
    {
      this.armingRnd = new RangeRandom(paramInt);
      if (this.bomb != null)
        this.bomb.setSeed(this.armingRnd.nextInt());
    }

    protected RangeRandom armingRnd = new RangeRandom(TrueRandom.nextLong());
    // ------------------------------------

    protected boolean   ready;
    protected Bomb      bomb;
    protected boolean   bHide;
    protected boolean   bExternal;
    protected boolean   bCassette;
    protected HookNamed hook;
    protected Class     bulletClass;
    protected int       bulletsFull;
    private int         bulletss;
    protected int       shotStep;
    protected float     bombDelay;
    protected SoundFX   sound;
    protected long      armingTime;
    protected float     bulletMassa;
    private int         curShotStep;
    private int         curShots;
    protected boolean   bTickShot;
    protected int       numBombs;
    private static Loc  loc = new Loc();

}
