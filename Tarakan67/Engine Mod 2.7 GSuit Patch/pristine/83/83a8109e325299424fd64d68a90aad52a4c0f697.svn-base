// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 17.01.2015 20:42:59
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RadarLiSN2.java

package com.maddox.il2.objects.electronics;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.electronics:
//            RCS

public class RadarLiSN2
{
    public class SpikeHolder
    {

        public void clear()
        {
            spikes.clear();
        }

        public int size()
        {
            return spikes.size();
        }

        public int toArray(Vector2f avector2f[])
        {
            for(int i = 0; i < spikes.size(); i++)
            {
                avector2f[i].x = ((Vector4f)spikes.get(i)).x;
                avector2f[i].y = ((Vector4f)spikes.get(i)).y;
            }

            return spikes.size();
        }

        public boolean addSpike(float f, float f1)
        {
            float f2 = f - slope * f1;
            float f3 = f + slope * f1;
            if(f1 < 0.0F)
                return false;
            for(int i = 0; i < spikes.size(); i++)
            {
                Vector4f vector4f = (Vector4f)spikes.get(i);
                if(vector4f.z < f2 && vector4f.w > f3)
                    return false;
                if(vector4f.z > f2 && vector4f.w < f3)
                {
                    spikes.remove(i);
                    i--;
                }
            }

            Vector4f vector4f1 = new Vector4f(f, f1, f2, f3);
            spikes.add(vector4f1);
            return true;
        }

        protected List spikes;
        protected int used;
        protected float slope;

        public SpikeHolder(float f)
        {
            slope = f;
            spikes = new ArrayList(maxNumOfSpikes);
        }
    }

    public class Contact
    {

        public void setInvis()
        {
            for(int i = 0; i < 4; i++)
                acc[i] = -1E-006F;

        }

        public void transform()
        {
            float f = dB2size(baseNoiseAtDist(dist) - RadarLiSN2.RECEIVER_SENSITIVITY);
            for(int i = 0; i < 4; i++)
            {
                size[i] = dB2size(size[i]);
                if(size[i] > 0.0F)
                    acc[i] += size[i] / f;
            }

            dist *= displayWidth;
        }

        public Actor actor;
        public float acc[];
        public float size[];
        public float dist;

        public Contact(Actor actor1)
        {
            actor = actor1;
            acc = new float[4];
            size = new float[4];
            setInvis();
        }
    }


    public RadarLiSN2()
    {
        AGL_SAMPLES = 5;
        MAX_DIST_FASTREJECT = 13000F;
        shortMode = false;
        gain = 1.0F;
        rcs = new RCS();
        contacts = new ArrayList(50);
        rnd = new Random();
    }

    public void init(Actor actor, float f, float f1, int i, float f2)
    {
        maxNumOfSpikes = i;
        me = actor;
        agl = (float)me.pos.getAbsPoint().z;
        spikeSize = f;
        spikeSize05 = 0.5F * spikeSize;
        displayWidthBak = f1;
        setMode(0);
        spikeHolder = new SpikeHolder[4];
        spikeHolder[0] = new SpikeHolder(f2);
        spikeHolder[1] = new SpikeHolder(f2);
        spikeHolder[2] = new SpikeHolder(f2);
        spikeHolder[3] = new SpikeHolder(f2);
        setGain(1.0F);
    }

    public void rareAction()
    {
        int i = contacts.size();
        Point3d point3d2 = me.pos.getAbsPoint();
        for(int j = 0; j < i; j++)
            if(!Actor.isValid(((Contact)contacts.get(j)).actor))
            {
                contacts.remove(j);
                i--;
                j--;
            } else
            {
                Point3d point3d = ((Contact)contacts.get(j)).actor.pos.getAbsPoint();
                if(Math.abs(point3d2.x - point3d.x) > (double)MAX_DIST_FASTREJECT || Math.abs(point3d2.y - point3d.y) > (double)MAX_DIST_FASTREJECT)
                {
                    contacts.remove(j);
                    i--;
                    j--;
                }
            }

        List list = Engine.targets();
        i = list.size();
        for(int k = 0; k < i; k++)
        {
            Actor actor = (Actor)list.get(k);
            if((actor instanceof Aircraft) && !actor.equals(me))
            {
                Point3d point3d1 = actor.pos.getAbsPoint();
                if(Math.abs(point3d2.x - point3d1.x) < (double)MAX_DIST_FASTREJECT && Math.abs(point3d2.y - point3d1.y) < (double)MAX_DIST_FASTREJECT)
                {
                    int l;
                    for(l = 0; l < contacts.size(); l++)
                        if(((Contact)contacts.get(l)).actor == actor)
                            break;

                    if(l == contacts.size())
                        contacts.add(new Contact(actor));
                }
            }
        }

    }

    protected void updateAGL()
    {
        Point3d point3d = me.pos.getAbsPoint();
        float f = (float)point3d.z;
        agl += f - lastAlt;
        lastAlt = f;
        float f4 = f * f;
        float f5 = 1E+010F;
        for(int i = 0; i < AGL_SAMPLES; i++)
        {
            float f1 = (float)rnd.nextGaussian() / 5F;
            float f2 = (float)rnd.nextGaussian() / 5F;
            if(f1 > 1.0F)
                f1 = 1.0F;
            else
            if(f1 < -1F)
                f1 = -1F;
            if(f2 > 1.0F)
                f2 = 1.0F;
            else
            if(f2 < -1F)
                f2 = -1F;
            float f3 = f - (float)Engine.land().HQ_Air(point3d.x + (double)(f1 * f), point3d.y + (double)(f2 * f));
            f3 *= f3;
            f3 += f4 * (f1 * f1 + f2 * f2);
            if(f3 < f5)
                f5 = f3;
        }

        f5 = (float)Math.sqrt(f5);
        agl = agl * AGL_FILTER + (1.0F - AGL_FILTER) * f5;
    }

    public void setMode(int i)
    {
        if(i == 1)
        {
            displayWidth = displayWidthBak / 4000F;
            transmitEcho = 200F;
            shortMode = true;
        } else
        {
            displayWidth = displayWidthBak / 10000F;
            transmitEcho = 300F;
            shortMode = false;
        }
    }

    public float getGroundClutterDist()
    {
        float f = agl;
        if(shortMode)
            return f * 2.5F;
        else
            return f;
    }

    protected float dB2size(float f)
    {
        float f1 = 0.1F + ((float)Math.pow(1.1000000000000001D, f) - 1.0F);
        if(f1 < 0.0F)
            return 0.0F;
        f1 *= gain_spikeSize;
        if(f1 > spikeSize05)
            f1 = spikeSize05 + (spikeSize05 * (f1 - spikeSize05)) / f1;
        return f1;
    }

    public void setGain(float f)
    {
        gain = 1.0F / (8F / (0.0008F + f));
        gain_spikeSize = gain * spikeSize;
    }

    protected float baseNoiseAtDist(float f)
    {
        float f1;
        if(f < agl)
        {
            f1 = NOISE_MIN;
        } else
        {
            float f2 = agl + GROUND_SHIFT;
            float f3 = f2 * f2 + f * f;
            float f4 = (float)Math.sqrt(f3);
            f1 = NOISE_MIN + GROUND_REFL + 23F * (float)Math.log(f2 / (f4 * f3));
            float f5 = rnd.nextFloat();
            f5 *= f5 * f5;
            f1 -= f5 * NOISE_CLUTTER_HOLES;
            if(f1 < NOISE_MIN)
                f1 = NOISE_MIN;
        }
        return f1;
    }

    protected float noiseAtDist(float f)
    {
        float f1 = -1F + 2.0F * rnd.nextFloat();
        f1 *= f1 * f1;
        f1 *= f1 * f1;
        f1 *= NOISE_GRASS_SIZE * f1 * f1;
        return f1 + baseNoiseAtDist(f);
    }

    public int[] getContacts(Vector2f avector2f[][])
    {
        Vector3d vector3d = new Vector3d();
        Orient orient = new Orient();
        boolean flag = false;
        int ai[] = new int[4];
        updateAGL();
        for(int i = 0; i < contacts.size(); i++)
        {
            Contact contact = (Contact)contacts.get(i);
            if(Actor.isValid(contact.actor))
            {
                vector3d.set(contact.actor.pos.getAbsPoint().x - me.pos.getAbsPoint().x, contact.actor.pos.getAbsPoint().y - me.pos.getAbsPoint().y, contact.actor.pos.getAbsPoint().z - me.pos.getAbsPoint().z);
                float f = (float)vector3d.length();
                if(f > 10500F)
                    contact.setInvis();
                else
                if(shortMode && f > 4200F)
                {
                    contact.setInvis();
                } else
                {
                    orient.setAT0(vector3d);
                    orient.sub(contact.actor.pos.getAbsOrient());
                    float f1 = rcs.getRCS(contact.actor, orient);
                    f1 += MAXANTGAIN + MAXANTGAIN + 63.54F;
                    f1 = (float)((double)f1 - 2D * ((20D / Math.log(10D)) * Math.log(90F * f) - 27.549999237060547D));
                    f1 -= RECEIVER_SENSITIVITY;
                    if(f1 < 0.0F)
                    {
                        contact.setInvis();
                    } else
                    {
                        orient.setAT0(vector3d);
                        orient.sub(me.pos.getAbsOrient());
                        contact.dist = f;
                        contact.size[0] = f1 + lobe(orient.getTangage(), orient.getAzimut());
                        contact.size[1] = f1 + lobe(-orient.getTangage(), orient.getAzimut());
                        contact.size[2] = f1 + lobe(-orient.getAzimut(), orient.getTangage());
                        contact.size[3] = f1 + lobe(orient.getAzimut(), orient.getTangage());
                        contact.transform();
                    }
                }
            }
        }

        for(int j = 0; j < 4; j++)
        {
            ai[j] = 0;
            spikeHolder[j].clear();
            for(int k = 0; k < MAX_CONTACTS; k++)
            {
                float f2 = 0.0F;
                int l = -1;
                for(int i1 = 0; i1 < contacts.size(); i1++)
                    if(((Contact)contacts.get(i1)).acc[j] > f2)
                    {
                        l = i1;
                        f2 = ((Contact)contacts.get(i1)).acc[j];
                    }

                if(l == -1)
                    break;
                Contact contact1 = (Contact)contacts.get(l);
                contact1.acc[j] = 0.0F;
                if(contact1.size[j] > 0.0F && !spikeHolder[j].addSpike(contact1.dist, contact1.size[j]))
                    k--;
            }

            float f3;
            if(shortMode)
                f3 = 4000F;
            else
                f3 = 10800F;
            float f8 = maxNumOfSpikes - spikeHolder[j].size();
            f3 = (f3 - transmitEcho) / (f8 + 1.0F);
            float f9 = transmitEcho - rnd.nextFloat() * f3;
            for(int j1 = 0; (float)j1 < f8; j1++)
            {
                float f4 = f9 + rnd.nextFloat() * f3;
                float f6 = dB2size(noiseAtDist(f4) - RECEIVER_SENSITIVITY);
                f4 *= displayWidth;
                spikeHolder[j].addSpike(f4, f6);
                f9 += f3;
            }

            int k1 = 0;
            f3 *= f8;
            float f5;
            float f7;
            for(; k1++ < 2 * maxNumOfSpikes && spikeHolder[j].size() < maxNumOfSpikes; spikeHolder[j].addSpike(f5, f7))
            {
                f5 = rnd.nextFloat() * f3 + transmitEcho;
                f7 = dB2size(noiseAtDist(f5) - RECEIVER_SENSITIVITY);
                f5 *= displayWidth;
            }

            ai[j] = spikeHolder[j].toArray(avector2f[j]);
        }

        return ai;
    }

    private float lobe(float f, float f1)
    {
        for(; f1 > 180F; f1 -= 360F);
        if(f1 < 0.0F)
            f1 = -f1;
        if(f1 > 90F)
        {
            f1 = 180F - f1;
            f += 180F;
        }
        for(; f > 360F; f -= 360F);
        for(; f < 0.0F; f += 360F);
        int i = (int)(f1 / 15F);
        for(; f < 0.0F; f += 360F);
        int j = (int)(f / 10F);
        float f2 = (f - (float)j * 10F) / 10F;
        float f3 = aGain[j][i] * (1.0F - f2) + f2 * aGain[j][i + 1];
        float f4 = aGain[j + 1][i] * (1.0F - f2) + f2 * aGain[j + 1][i + 1];
        f2 = (f1 - (float)i * 15F) / 15F;
        f3 = f3 * (1.0F - f2) + f2 * f4;
        return 2.0F * (f3 - MAXANTGAIN);
    }

    protected int AGL_SAMPLES;
    protected static float AGL_FILTER = 0.8F;
    private float agl;
    private float lastAlt;
    protected static float GROUND_REFL = 500F;
    protected static float NOISE_MIN = -90F;
    protected static float GROUND_SHIFT = 500F;
    protected static float NOISE_CLUTTER_HOLES = 90F;
    protected static float NOISE_GRASS_SIZE = 20F;
    private Actor me;
    private float displayWidth;
    private float displayWidthBak;
    private ArrayList contacts;
    RCS rcs;
    int maxNumOfSpikes;
    SpikeHolder spikeHolder[];
    Random rnd;
    float transmitEcho;
    float gain;
    private float spikeSize;
    private float spikeSize05;
    float gain_spikeSize;
    private boolean shortMode;
    private static int MAX_CONTACTS = 8;
    private float MAX_DIST_FASTREJECT;
    protected static float RECEIVER_SENSITIVITY = -90F;
    private static float MAXANTGAIN = 4.9F;
    private float aGain[][] = {
        {
            2.9F, 2.75F, 2.28F, 1.39F, -0.11F, -2.97F, -10F, -2.97F
        }, {
            3.6F, 3.45F, 2.98F, 2.09F, 0.59F, -2.27F, -10F, -2.27F
        }, {
            4.2F, 4.05F, 3.58F, 2.69F, 1.19F, -1.67F, -10F, -1.67F
        }, {
            4.7F, 4.55F, 4.08F, 3.19F, 1.69F, -1.17F, -10F, -1.17F
        }, {
            4.9F, 4.75F, 4.28F, 3.39F, 1.89F, -0.97F, -10F, -0.97F
        }, {
            4.8F, 4.65F, 4.18F, 3.29F, 1.79F, -1.07F, -10F, -1.07F
        }, {
            4.6F, 4.45F, 3.98F, 3.09F, 1.59F, -1.27F, -10F, -1.27F
        }, {
            4.2F, 4.05F, 3.58F, 2.69F, 1.19F, -1.67F, -10F, -1.67F
        }, {
            3.7F, 3.55F, 3.08F, 2.19F, 0.69F, -2.17F, -10F, -2.17F
        }, {
            3.3F, 3.15F, 2.68F, 1.79F, 0.29F, -2.57F, -10F, -2.57F
        }, {
            2.4F, 2.25F, 1.78F, 0.89F, -0.61F, -3.47F, -10F, -3.47F
        }, {
            1.6F, 1.45F, 0.98F, 0.09F, -1.41F, -4.27F, -10F, -4.27F
        }, {
            1.8F, 1.65F, 1.18F, 0.29F, -1.21F, -4.07F, -10F, -4.07F
        }, {
            1.9F, 1.75F, 1.28F, 0.39F, -1.11F, -3.97F, -10F, -3.97F
        }, {
            1.7F, 1.55F, 1.08F, 0.19F, -1.31F, -4.17F, -10F, -4.17F
        }, {
            1.2F, 1.05F, 0.58F, -0.31F, -1.81F, -4.67F, -10F, -4.67F
        }, {
            0.6F, 0.45F, -0.02F, -0.91F, -2.41F, -5.27F, -10F, -5.27F
        }, {
            0.0F, -0.15F, -0.62F, -1.51F, -3.01F, -5.87F, -10F, -5.87F
        }, {
            0.0F, -0.15F, -0.62F, -1.51F, -3.01F, -5.87F, -10F, -5.87F
        }, {
            0.0F, -0.15F, -0.62F, -1.51F, -3.01F, -5.87F, -10F, -5.87F
        }, {
            0.6F, 0.45F, -0.02F, -0.91F, -2.41F, -5.27F, -10F, -5.27F
        }, {
            0.8F, 0.65F, 0.18F, -0.71F, -2.21F, -5.07F, -10F, -5.07F
        }, {
            1.4F, 1.25F, 0.78F, -0.11F, -1.61F, -4.47F, -10F, -4.47F
        }, {
            1.7F, 1.55F, 1.08F, 0.19F, -1.31F, -4.17F, -10F, -4.17F
        }, {
            2.0F, 1.85F, 1.38F, 0.49F, -1.01F, -3.87F, -10F, -3.87F
        }, {
            2.2F, 2.05F, 1.58F, 0.69F, -0.81F, -3.67F, -10F, -3.67F
        }, {
            2.4F, 2.25F, 1.78F, 0.89F, -0.61F, -3.47F, -10F, -3.47F
        }, {
            2.7F, 2.55F, 2.08F, 1.19F, -0.31F, -3.17F, -10F, -3.17F
        }, {
            3F, 2.85F, 2.38F, 1.49F, -0.01F, -2.87F, -10F, -2.87F
        }, {
            3.3F, 3.15F, 2.68F, 1.79F, 0.29F, -2.57F, -10F, -2.57F
        }, {
            3.4F, 3.25F, 2.78F, 1.89F, 0.39F, -2.47F, -10F, -2.47F
        }, {
            2.5F, 2.35F, 1.88F, 0.99F, -0.51F, -3.37F, -10F, -3.37F
        }, {
            1.8F, 1.65F, 1.18F, 0.29F, -1.21F, -4.07F, -10F, -4.07F
        }, {
            0.6F, 0.45F, -0.02F, -0.91F, -2.41F, -5.27F, -10F, -5.27F
        }, {
            1.3F, 1.15F, 0.68F, -0.21F, -1.71F, -4.57F, -10F, -4.57F
        }, {
            2.2F, 2.05F, 1.58F, 0.69F, -0.81F, -3.67F, -10F, -3.67F
        }, {
            2.9F, 2.75F, 2.28F, 1.39F, -0.11F, -2.97F, -10F, -2.97F
        }
    };


}
