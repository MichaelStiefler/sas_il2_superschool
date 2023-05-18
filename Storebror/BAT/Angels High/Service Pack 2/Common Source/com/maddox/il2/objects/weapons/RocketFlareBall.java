package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.EffAnimatedSprite;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.LightPointActorLong;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.effects.MiscEffects;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;

public class RocketFlareBall extends Rocket {

    public boolean interpolateStep() {
        // Randomize Light Emission ("Flicker" Flare), take current Flare Size into account.
        float randomFactor = TrueRandom.nextFloat();
        float timeFactor = 1.0F - Math.abs(((float) (Time.current() - this.timeStart) / (float) (RADIUS_PERIOD / 2)) - 1.0F);
        if (timeFactor < 0F) {
            timeFactor = 0F;
        }
        float endFactor = MiscEffects.cvt((this.timeStart + this.timeLife) - Time.current(), 0F, 1000F, 0F, 1F);
        Eff3DActor.setIntesity(this.eff3dactor, ((randomFactor * 4.75F) + 0.25F) * endFactor);
        if (this.lightpointactor != null) {
            this.lightPointIntensity = (randomFactor + 0.5F) * timeFactor * endFactor;
            this.lightpointactor.light.setEmit(this.lightPointIntensity, this.lightPointRange);
        }
        this.getSpeed(this.v);
        if (this.v.z < -15D) {
            this.v.z *= 0.9D;
            this.setSpeed(this.v);
        }

        // Relocate Flare Sound emitter position
        if ((this.draw != null) && (this.draw.sounds() != null)) {
            SoundFX soundfx = this.draw.sounds().get();
            soundfx.setPosition(this.pos.getAbsPoint());
        }
        return super.interpolateStep();
    }

    public RocketFlareBall(Actor owner, int i, Point3d launchPoint, Orient launchOrient, float f) {
        this(owner, launchPoint, launchOrient);
    }

    public RocketFlareBall(Actor owner, Point3d launchPoint, Orient launchOrient) {
        this(owner, launchPoint, launchOrient, FLARE_COLOR_WHITE);
    }

    public RocketFlareBall(Actor owner, Point3d launchPoint, Orient launchOrient, int flareColor) {
        this.speed = new Vector3d();
        this.pos.setAbs(launchPoint, launchOrient);
        this.pos.reset();
        this.pos.setBase(owner, (Hook) null, true);
        this.collide(false);
        this.drawing(false);
        this.eff3dactor = null;
        this.lightpointactor = null;
        this.lightPointIntensity = 0F;
        this.lightPointRange = 0F;
        this.flareColor = flareColor;
    }

    public void start(float f, int i) {
        super.start(f, i);
        this.speed.normalize();
        this.speed.scale(110D);
        this.drawing(false);
        String flareEffect = flareEffects[this.flareColor];
        boolean saveEffAnimatedSpriteStaticVisibilityLong = EffAnimatedSprite.staticVisibilityLong;
        EffAnimatedSprite.staticVisibilityLong = true;
        this.eff3dactor = Eff3DActor.New(this, null, new Loc(), 1.0F, flareEffect, f);
        this.timeStart = Time.current();
        this.timeLife = (long) (f * 1000F);

        this.lightPointIntensity = 0.5F + TrueRandom.nextFloat();
        this.lightPointRange = MiscEffects.cvt(World.Sun().ToSun.z, -0.3F, 0.1F, 1000F, 100F);
//        this.lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
        this.lightpointactor = new LightPointActorLong(new LightPointWorld(), new Point3d());
        Color3f lightColor = flareLightColors[this.flareColor];
        this.lightpointactor.light.setColor(lightColor);
        this.lightpointactor.light.setEmit(this.lightPointIntensity, this.lightPointRange);
        this.eff3dactor.draw.lightMap().put("light", this.lightpointactor);
        EffAnimatedSprite.staticVisibilityLong = saveEffAnimatedSpriteStaticVisibilityLong;

        // Shoot Flare Gun, play sound
        SoundFX soundfx = this.newSound("weapon.rocketflare", true);
        if (soundfx.isInitialized()) {
            soundfx.setAcoustics(this.acoustics);
            if ((this.draw != null) && (this.draw.sounds() != null)) {
                soundfx.insert(this.draw.sounds(), true);
            }
            soundfx.play(this.pos.getAbsPoint());
        }
    }

    protected void doExplosion(Actor actor, String s) {
        this.destroy();
    }

    protected void doExplosionAir() {
    }

    public void destroy() {

        // Turn off flare illumination if there is any
        if (this.lightpointactor != null) {
            this.lightpointactor.destroy();
        }

        // Stop Flare Sound
        this.stopSounds();

        super.destroy();
    }

    private Vector3d               v                 = new Vector3d();
    private Eff3DActor             eff3dactor;
//    private LightPointActor        lightpointactor;
    private LightPointActorLong    lightpointactor;
    private float                  lightPointIntensity;
    private float                  lightPointRange;
    private long                   timeStart;
    private int                    flareColor        = FLARE_COLOR_WHITE;
    private static final long      RADIUS_PERIOD     = 25000L;
    public static final int        FLARE_COLOR_WHITE = 0;
    public static final int        FLARE_COLOR_RED   = 1;
    public static final int        FLARE_COLOR_GREEN = 2;
    private static final String[]  flareEffects      = { "3DO/Effects/Fireworks/FlareBallWhite.eff", "3DO/Effects/Fireworks/FlareBallRed.eff", "3DO/Effects/Fireworks/FlareBallGreen.eff" };
    private static final Color3f[] flareLightColors  = { new Color3f(1.0F, 0.9F, 0.6F), new Color3f(1.0F, 0.0F, 0.0F), new Color3f(0.0F, 1.0F, 0.0F) };

    static {
        Class theClass = RocketFlareBall.class;
        Property.set(theClass, "mesh", "3DO/Arms/RocketFlareBall/mono.sim");
        Property.set(theClass, "smoke", "3do/effects/rocket/RocketSmokeWhiteTile.eff");
        Property.set(theClass, "emitColor", new Color3f(0.0F, 0.0F, 0.0F));
        Property.set(theClass, "sprite", (Object) null);
        Property.set(theClass, "flame", (Object) null);
        Property.set(theClass, "emitLen", 0.0F);
        Property.set(theClass, "emitMax", 0.0F);
        Property.set(theClass, "sound", (Object) null); // Don't set the sound preset here, it won't work.
        Property.set(theClass, "radius", 0.1F);
        Property.set(theClass, "timeLife", 40F);
        Property.set(theClass, "timeFire", 10F);
        Property.set(theClass, "force", 0.0F);
        Property.set(theClass, "power", 0.01485F);
        Property.set(theClass, "powerType", 0);
        Property.set(theClass, "kalibr", 0.001F);
        Property.set(theClass, "massa", 3F);
        Property.set(theClass, "massaEnd", 0.1F);
    }
}
