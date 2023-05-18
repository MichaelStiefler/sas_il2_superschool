// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2011/09/20 13:20:00
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Torpedo.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, Ballistics

public class USSTorpedo extends Bomb
{

    public void msgCollision(Actor actor, String s, String s1)
    {
        Other = actor;
        OtherChunk = s1;
        if(actor instanceof ActorLand)
        {
            if(flow)
            {
                doExplosion(actor, s1);
                return;
            }
            surface();
            if(World.land().isWater(P.x, P.y))
                return;
        }
        doExplosion(actor, s1);
    }

    private void surface()
    {
        pos.getAbs(P, Or);
        flow = true;
        getSpeed(spd);
        if(World.land().isWater(P.x, P.y))
        {
            if(spd.z < -0.11999999731779099D)
                Explosions.RS82_Water(P, 4F, 1.0F);
            double d = spd.length();
            if(d > 0.001D)
                d = spd.z / spd.length();
            else
                d = -1D;
            if(d < -0.46999999999999997D)
                sendexplosion();
        }
        spd.z = 0.0D;
        setSpeed(spd);
        P.z = 0.0D;
        float af[] = new float[3];
        Or.getYPR(af);
        Or.setYPR(af[0], 0.0F, af[2]);
        pos.setAbs(P, Or);
        flags &= 0xffffffbf;
        drawing(false);
        Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/Line.eff", -1F);
        Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/Wave.eff", -1F);
    }

    public void interpolateTick()
    {
        float f = Time.tickLenFs();
        pos.getAbs(P);
        if(P.z <= -0.10000000149011612D)
            surface();
        if(!flow)
        {
            Ballistics.update(this, M, S);
        } else
        {
            getSpeed(spd);
            if(spd.length() > (double)velocity)
                spd.scale(0.99000000953674316D);
            else
            if(spd.length() < (double)velocity)
                spd.scale(1.0099999904632568D);
            setSpeed(spd);
            pos.getAbs(P);
            P.x += spd.x * (double)f;
            P.y += spd.y * (double)f;
            pos.setAbs(P);
            if(Time.current() > started + travelTime || !World.land().isWater(P.x, P.y))
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
        Class class1 = getClass();
        init(Property.floatValue(class1, "kalibr", 1.0F), Property.floatValue(class1, "massa", 1.0F));
        started = Time.current();
        velocity = Property.floatValue(class1, "velocity", 1.0F);
        travelTime = (long)Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
        setOwner(pos.base(), false, false, false);
        pos.setBase(null, null, true);
        pos.setAbs(pos.getCurrent());
        getSpeed(spd);
        pos.getAbs(P, Or);
        Vector3d vector3d = new Vector3d(Property.floatValue(class1, "startingspeed", 0.0F), 0.0D, 0.0D);
        Or.transform(vector3d);
        spd.add(vector3d);
        setSpeed(spd);
        collide(true);
        interpPut(new Bomb.Interpolater(), null, Time.current(), null);
        drawing(true);
        if(Property.containsValue(class1, "emitColor"))
        {
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor((Color3f)Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
            lightpointactor.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
            draw.lightMap().put("light", lightpointactor);
        }
        sound = newSound(Property.stringValue(class1, "sound", null), false);
        if(sound != null)
            sound.play();
    }

    public USSTorpedo()
    {
    }

    Actor Other;
    String OtherChunk;
    String ThisChunk;
    boolean flow;
    private float velocity;
    private long travelTime;
    private long started;
    static Vector3d spd = new Vector3d();
    static Orient Or = new Orient();
    static Point3d P = new Point3d();

}