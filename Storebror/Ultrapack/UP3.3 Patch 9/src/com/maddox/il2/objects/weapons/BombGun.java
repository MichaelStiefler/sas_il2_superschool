package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
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
        this.ready = true;
        this.bHide = false;
        this.bExternal = true;
        this.bCassette = false;
        this.bulletClass = null;
        this.bombDelay = 0.0F;
        // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//        armingTime = 2000L;
        this.armingTime = 0L;
        // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
        // FIXME: Storebror: +++ TEST Bomb/Rocket Fuze/Delay Replication
        if (!Config.isUSE_RENDER()) {
            this.bombDelay = 0.0F;
        }
        // FIXME: Storebror: --- TEST Bomb/Rocket Fuze/Delay Replication
        this.bulletMassa = 0.048F;
        this.numBombs = 0;
    }

    public void doDestroy() {
        this.ready = false;
        if (this.bomb != null) {
            this.bomb.destroy();
            this.bomb = null;
        }
    }

    private boolean nameEQ(HierMesh hiermesh, int i, int j) {
        if (hiermesh == null) {
            return false;
        }
        hiermesh.setCurChunk(i);
        String s = hiermesh.chunkName();
        hiermesh.setCurChunk(j);
        String s1 = hiermesh.chunkName();
        int l = Math.min(s.length(), s1.length());
        for (int k = 0; k < l; k++) {
            char c = s.charAt(k);
            if (c == '_') {
                return true;
            }
            if (c != s1.charAt(k)) {
                return false;
            }
        }

        return true;
    }

    public BulletEmitter detach(HierMesh hiermesh, int i) {
        if (!this.ready) {
            return GunEmpty.get();
        }
        if ((i == -1) || this.nameEQ(hiermesh, i, this.hook.chunkNum())) {
            this.ready = false;
            this.bExecuted = true;
            return GunEmpty.get();
        } else {
            return this;
        }
    }

    protected int bullets() {
        return this.actor == null ? 0 : this.bulletss - this.actor.hashCode();
    }

    protected void bullets(int i) {
        if (this.actor != null) {
            this.bulletss = i + this.actor.hashCode();
        } else {
            this.bulletss = 0;
        }
    }

    public void hide(boolean flag) {
        this.bHide = flag;
        if (this.bHide) {
            if (Actor.isValid(this.bomb) && this.bExternal) {
                this.bomb.drawing(false);
            }
        } else if (Actor.isValid(this.bomb) && this.bExternal) {
            this.bomb.drawing(true);
        }
    }

    public boolean isHide() {
        return this.bHide;
    }

    public boolean isEnablePause() {
        return false;
    }

    public boolean isPause() {
        return false;
    }

    public void setPause(boolean flag) {
    }

    public void setBombDelay(float f) {
        this.bombDelay = f;
        if (this.bomb != null) {
            this.bomb.delayExplosion = this.bombDelay;
        }
    }

    // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
    public void setBombDelay(float bombDelay, float bombFuze) {
        this.bombDelay = bombDelay;
        this.armingTime = (long) (bombFuze * 1000F);
        if (this.bomb != null) {
            this.bomb.delayExplosion = bombDelay;
            this.bomb.armingTime = this.armingTime;
        }
    }
    // TODO: --- Bomb Fuze Setting by SAS~Storebror ---

    public boolean isExternal() {
        return this.bExternal;
    }

    public boolean isCassette() {
        return this.bCassette;
    }

    public float bulletMassa() {
        return this.bulletMassa;
    }

    public int countBullets() {
        return this.bullets();
    }

    public boolean haveBullets() {
        return this.bullets() != 0;
    }

    public void loadBullets() {
        this.loadBullets(this.bulletsFull);
    }

    public void _loadBullets(int i) {
        this.loadBullets(i);
    }

    public void loadBullets(int i) {
        this.bullets(i);
        if (this.bullets() != 0) {
            if (!Actor.isValid(this.bomb)) {
                this.newBomb();
            }
        } else if (Actor.isValid(this.bomb)) {
            this.bomb.destroy();
            this.bomb = null;
        }
    }

    public Class bulletClass() {
        return this.bulletClass;
    }

    public void setBulletClass(Class class1) {
        this.bulletClass = class1;
        this.bulletMassa = Property.floatValue(this.bulletClass, "massa", this.bulletMassa);
    }

    public boolean isShots() {
        return this.bExecuted;
    }

    public void shots(int i, float f) {
        this.shots(i);
    }

    public void shots(int i) {
        if (this.isHide()) {
            return;
        }
        if (this.isCassette() && (i != 0)) {
            i = this.bullets();
        }
        if ((this.bullets() == -1) && (i == -1)) {
            i = 25;
        }
        if (!this.bExecuted && (i != 0)) {
            if (this.bullets() == 0) {
                return;
            }
            if (this.bomb instanceof FuelTank) {
                this.bullets(1);
            }
            this.curShotStep = 0;
            this.curShots = i;
            this.bExecuted = true;
        } else if (this.bExecuted && (i != 0)) {
            this.curShots = i;
        } else if (this.bExecuted && (i == 0)) {
            this.bExecuted = false;
        }
    }

    protected void interpolateStep() {
        this.bTickShot = false;
        if (this.curShotStep == 0) {
            if ((this.bullets() == 0) || (this.curShots == 0) || !Actor.isValid(this.actor)) {
                this.shots(0);
                return;
            }
            if (this.actor instanceof Aircraft) {
                FlightModel flightmodel = ((Aircraft) this.actor).FM;
                if (flightmodel.getOverload() < 0.0F) {
                    return;
                }
            }
            this.bTickShot = true;
            if (this.bomb != null) {
                this.bomb.pos.setUpdateEnable(true);
                this.bomb.pos.resetAsBase();
                // TODO: Storebror: Bomb Spread Replication
                // ------------------------------------
                this.bomb.setSeed(this.armingRnd.nextInt());
                // ------------------------------------
                this.bomb.start();
                if (this.sound != null) {
                    this.sound.play();
                }
                this.bomb = null;
            }
            if (this.curShots > 0) {
                this.curShots--;
            }
            if (this.bullets() > 0) {
                this.bullets(this.bullets() - 1);
            }
            if (this.bullets() != 0) {
                this.newBomb();
            }
            this.curShotStep = this.shotStep;
        }
        this.curShotStep--;
    }

    public boolean tick() {
        this.interpolateStep();
        return this.ready;
    }

    private static String getCleanACName(Class class1) {
        String s = "";
        try {
            s = class1.toString().substring(class1.toString().lastIndexOf(".") + 1, class1.toString().length());
        } catch (Exception exception) {
        }
        return s;
    }

    public static float getBombVShift(Class class1, Class class2) {
        String s = BombGun.getCleanACName(class1);
        float f = Property.floatValue(class2, "verticalShift", 0.0F);
        return Property.floatValue(class2, "verticalShift_" + s, f);
    }

    public static float getBombHShift(Class class1, Class class2) {
        String s = BombGun.getCleanACName(class1);
        float f = Property.floatValue(class2, "horizontalShift", 0.0F);
        return Property.floatValue(class2, "horizontalShift_" + s, f);
    }

    private void newBomb() {
        try {
            this.bomb = (Bomb) this.bulletClass.newInstance();
            this.bomb.index = this.numBombs++;
            this.bomb.pos.setBase(this.actor, this.hook, false);
            float f = BombGun.getBombVShift(this.actor.getClass(), this.getClass());
            if (f != 0.0F) {
                Point3d point3d = this.bomb.pos.getRelPoint();
                point3d.z += f;
                this.bomb.pos.setRel(point3d);
            }
            f = BombGun.getBombHShift(this.actor.getClass(), this.getClass());
            if (f != 0.0F) {
                Point3d point3d1 = this.bomb.pos.getRelPoint();
                point3d1.x += f;
                this.bomb.pos.setRel(point3d1);
            }
            this.bomb.pos.changeHookToRel();
            this.bomb.pos.resetAsBase();
            if (!this.bExternal) {
                this.bomb.drawing(false);
            } else {
                this.bomb.visibilityAsBase(true);
            }
            // TODO: Storebror: Bomb Spread Replication
            // ------------------------------------
            this.bomb.setSeed(this.armingRnd.nextInt());
            // ------------------------------------
            this.bomb.pos.setUpdateEnable(false);
            this.bomb.delayExplosion = this.bombDelay;
            this.bomb.armingTime = this.armingTime;
            // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
            Bomb.printDebug(this.actor, "BombGun " + Bomb.simpleClassName(this) + " newBomb, bombDelay=" + this.bombDelay + ", armingTime=" + this.armingTime);
            // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        } catch (Exception e) {
        }
    }

    public String getHookName() {
        return this.hook.name();
    }

    public void set(Actor actor, String s, Loc loc1) {
        this.set(actor, s);
    }

    public void set(Actor actor, String s, String s1) {
        this.set(actor, s);
    }

    public void set(Actor actor, String s) {
        this.actor = actor;
        Class class1 = this.getClass();
        this.bExternal = Property.containsValue(class1, "external");
        this.bCassette = Property.containsValue(class1, "cassette");
        this.bulletClass = (Class) Property.value(class1, "bulletClass", null);
        this.bullets(Property.intValue(class1, "bullets", 1));
        this.bulletsFull = this.bullets();
        this.setBulletClass(this.bulletClass);
        float f = Property.floatValue(class1, "shotFreq", 0.5F);
        if (f < 0.001F) {
            f = 0.001F;
        }
        this.shotStep = (int) (((1.0F / f) + (Time.tickConstLenFs() / 2.0F)) / Time.tickConstLenFs());
        if (this.shotStep <= 0) {
            this.shotStep = 1;
        }
        this.hook = (HookNamed) actor.findHook(s);
        this.newBomb();
        String s1 = Property.stringValue(this.getClass(), "sound", null);
        if (s1 != null) {
            this.bomb.pos.getAbs(BombGun.loc);
            BombGun.loc.sub(this.actor.pos.getAbs());
            this.sound = this.actor.newSound(s1, false);
            if (this.sound != null) {
                SoundFX soundfx = this.actor.getRootFX();
                if (soundfx != null) {
                    this.sound.setParent(soundfx);
                    this.sound.setPosition(BombGun.loc.getPoint());
                }
            }
        }
        this.actor.interpPut(this, null, -1L, null);
    }

    public void setArmingTime(long l) {
        // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
        Bomb.printDebug(this.actor, "BombGun " + Bomb.simpleClassName(this) + " setArmingTime(" + l + ")");
        // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
        this.armingTime = l;
        if (this.bomb != null) {
            this.bomb.armingTime = l;
        }
    }

    // TODO: Storebror: Bomb Spread Replication
    // ------------------------------------
    public void setRnd(int paramInt) {
        this.armingRnd = new RangeRandom(paramInt);
        if (this.bomb != null) {
            this.bomb.setSeed(this.armingRnd.nextInt());
        }
    }

    protected RangeRandom armingRnd = new RangeRandom(TrueRandom.nextLong());
    // ------------------------------------

    protected boolean     ready;
    protected Bomb        bomb;
    protected boolean     bHide;
    protected boolean     bExternal;
    protected boolean     bCassette;
    protected HookNamed   hook;
    protected Class       bulletClass;
    protected int         bulletsFull;
    private int           bulletss;
    protected int         shotStep;
    protected float       bombDelay;
    protected SoundFX     sound;
    protected long        armingTime;
    protected float       bulletMassa;
    private int           curShotStep;
    private int           curShots;
    protected boolean     bTickShot;
    protected int         numBombs;
    private static Loc    loc       = new Loc();

}
