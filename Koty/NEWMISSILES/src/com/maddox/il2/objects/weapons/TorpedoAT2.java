// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 30.03.2018 23:16:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TorpedoAT2.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.AudioStream;
import com.maddox.util.HashMapExt;
import java.text.DecimalFormat;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, TorpedoAcusticUtils, Ballistics

public class TorpedoAT2 extends Bomb
{

    public TorpedoAT2()
    {
        victim = null;
        torpedoUtils = null;
        pT = null;
        deltaAzimuth = 0.0F;
        v = null;
        torpedoUtils = new TorpedoAcusticUtils(this);
        pT = new Point3d();
        v = new Vector3d();
        MagneticFuse = false;
    }

    private void getTorpedoTarget()
    {
        Actor theTarget = torpedoUtils.lookTorpedo(this, 45F, 12000D);
        if((theTarget instanceof ShipGeneric) || (theTarget instanceof BigshipGeneric))
            victim = theTarget;
        else
            victim = null;
    }

    public void msgCollision(Actor actor, String string, String string_0_)
    {
        double randf = Math.random() * 100D;
        impact = Time.current() - started;
        Other = actor;
        OtherChunk = string_0_;
        if(actor instanceof ActorLand)
        {
            if(flow)
            {
                doExplosion(actor, string_0_);
                return;
            }
            surface();
            if(World.land().isWater(((Tuple3d) (P)).x, ((Tuple3d) (P)).y))
                return;
        }
        if(impact < 6200L)
        {
            collide(false);
            return;
        }
        if(randf >= 53.200000000000003D && randf < 55.600000000000001D)
        {
            collide(false);
            return;
        }
        if(randf >= 12.5D && randf < 19.800000000000001D)
            destroy();
        else
            doExplosion(actor, string_0_);
    }

    private void surface()
    {
        if(victim == null)
            getTorpedoTarget();
        Class var_class = getClass();
        double randi = Math.random() * 100D;
        travelTime = (long)Property.floatValue(var_class, "traveltime", 1.0F) * 1000L;
        super.pos.getAbs(P, Or);
        flow = true;
        getSpeed(spd);
        if(World.land().isWater(((Tuple3d) (P)).x, ((Tuple3d) (P)).y))
        {
            if(((Tuple3d) (spd)).z < -0.11999999731779099D)
                Explosions.RS82_Water(P, 4F, 1.0F);
            double d = spd.length();
            if(d > 0.001D)
                d = ((Tuple3d) (spd)).z / spd.length();
            else
                d = -1D;
            if(d > -0.56999999999999995D && randi >= 73D && randi <= 75.700000000000003D)
                travelTime = 18400L;
            if(d > -0.56999999999999995D && randi <= 1.7D)
            {
                velocity = 0.02F;
                destroy();
            }
            if(d > -0.56999999999999995D && randi >= 38.100000000000001D && randi <= 42.299999999999997D)
                destroy();
            if(d < -0.56999999999999995D)
            {
                velocity = 0.02F;
                destroy();
            }
        }
        spd.z = 0.0D;
        setSpeed(spd);
        P.z = 0.0D;
        float fs[] = new float[3];
        Or.getYPR(fs);
        Or.setYPR(fs[0], 0.0F, fs[2]);
        super.pos.setAbs(P, Or);
        super.flags &= 0xffffffbf;
        drawing(false);
        Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/Line.eff", -1F);
        Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/wave.eff", -1F);
    }

    public void interpolateTick()
    {
        float f = Time.tickLenFs();
        super.pos.getAbs(P, or);
        if(((Tuple3d) (P)).z <= -0.10000000149011612D)
            surface();
        if(!flow)
        {
            Ballistics.update(this, super.M, super.S);
        } else
        {
            float f1 = (float)getSpeed(null);
            getSpeed(spd);
            if(spd.length() > (double)velocity)
                f1 = (float)((double)f1 * 0.99000000953674316D);
            else
            if(spd.length() < (double)velocity)
                f1 = (float)((double)f1 * 1.0099999904632568D);
            super.pos.getAbs(P, or);
            v.set(1.0D, 0.0D, 0.0D);
            or.transform(v);
            v.scale(f1);
            setSpeed(v);
            P.x += ((Tuple3d) (v)).x * (double)f;
            P.y += ((Tuple3d) (v)).y * (double)f;
            if(victim != null)
            {
                pT = victim.pos.getAbsPoint();
                pT.sub(P);
                or.transformInv(pT);
                double angleAzimuth = Math.toDegrees(Math.atan(((Tuple3d) (pT)).y / ((Tuple3d) (pT)).x));
                DecimalFormat twoPlaces = new DecimalFormat("+000.00;-000.00");
                deltaAzimuth = 0.0F;
                if(angleAzimuth > 2D)
                    deltaAzimuth = -1F;
                if(angleAzimuth < -2D)
                    deltaAzimuth = 1.0F;
                deltaAzimuth *= 5F * f;
                or.increment(deltaAzimuth, 0.0F, 0.0F);
                or.setYPR(or.getYaw(), 0.0F, 0.0F);
                
                Point3d me = new Point3d();
                Point3d tgt = new Point3d();
                super.pos.getAbs(me);   	me.z  = 0;
                victim.pos.getAbs(tgt); 	tgt.z = 0;
                double distance = me.distance(tgt);
                
                /*if(!MagneticFuse)
                {
                	if(distance < 60D)MagneticFuse=true;
                }
                else*/
                {
                	if(Math.abs(distance) < 0.2D)
                	{
                		doMidAirExplosion();
                        postDestroy();
                        collide(false);
                        drawing(false);
                	}
                }
            }
            super.pos.setAbs(P, or);
            if(Time.current() > started + travelTime || !World.land().isWater(((Tuple3d) (P)).x, ((Tuple3d) (P)).y))
                sendexplosion();
            
            
        }
        updateSound();
    }

    private void sendexplosion()
    {
        MsgCollision.post(Time.current(), this, Other, null, OtherChunk);
    }

    public void start()
    {
        Class var_class = getClass();
        init(Property.floatValue(var_class, "kalibr", 1.0F), Property.floatValue(var_class, "massa", 1.0F));
        started = Time.current();
        velocity = Property.floatValue(var_class, "velocity", 1.0F);
        travelTime = (long)Property.floatValue(var_class, "traveltime", 1.0F) * 1000L;
        setOwner(super.pos.base(), false, false, false);
        super.pos.setBase(null, null, true);
        super.pos.setAbs(super.pos.getCurrent());
        getSpeed(spd);
        super.pos.getAbs(P, Or);
        Vector3d vector3d = new Vector3d(Property.floatValue(var_class, "startingspeed", 0.0F), 0.0D, 0.0D);
        Or.transform(vector3d);
        spd.add(vector3d);
        setSpeed(spd);
        collide(true);
        interpPut(new Bomb.Interpolater(), null, Time.current(), null);
        drawing(true);
        if(Property.containsValue(var_class, "emitColor"))
        {
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor((Color3f)Property.value(var_class, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
            lightpointactor.light.setEmit(Property.floatValue(var_class, "emitMax", 1.0F), Property.floatValue(var_class, "emitLen", 50F));
            super.draw.lightMap().put("light", lightpointactor);
        }
        super.sound = newSound(Property.stringValue(var_class, "sound", null), false);
        if(super.sound != null)
            super.sound.play();
    }

    Actor Other;
    String OtherChunk;
    String ThisChunk;
    boolean flow;
    private float velocity;
    private long travelTime;
    private long started;
    private long impact;
    static Vector3d spd = new Vector3d();
    static Orient Or = new Orient();
    static Point3d P = new Point3d();
    private Actor victim;
    private TorpedoAcusticUtils torpedoUtils;
    private Point3d pT;
    private static Orient or = new Orient();
    private float deltaAzimuth;
    private Vector3d v;
    private boolean MagneticFuse;

}