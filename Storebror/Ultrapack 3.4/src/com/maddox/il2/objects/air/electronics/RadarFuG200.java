package com.maddox.il2.objects.air.electronics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector4f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.Time;

public class RadarFuG200 {
    public class SpikeHolder {

        public void clear() {
            this.spikes.clear();
        }

        public int size() {
            return this.spikes.size();
        }

        public int toArray(Vector2f avector2f[]) {
            for (int i = 0; i < this.spikes.size(); i++) {
                avector2f[i].x = ((Vector4f) this.spikes.get(i)).x;
                avector2f[i].y = ((Vector4f) this.spikes.get(i)).y;
            }

            return this.spikes.size();
        }

        public boolean addSpike(float f, float f1) {
            float f2 = f - (this.slope * f1);
            float f3 = f + (this.slope * f1);
            if (f1 < 0.0F) {
                return false;
            }
            for (int i = 0; i < this.spikes.size(); i++) {
                Vector4f vector4f = (Vector4f) this.spikes.get(i);
                if ((vector4f.z < f2) && (vector4f.w > f3)) {
                    return false;
                }
                if ((vector4f.z > f2) && (vector4f.w < f3)) {
                    this.spikes.remove(i);
                    i--;
                }
            }

            Vector4f vector4f1 = new Vector4f(f, f1, f2, f3);
            this.spikes.add(vector4f1);
            return true;
        }

        protected List  spikes;
        protected int   used;
        protected float slope;

        public SpikeHolder(float f) {
            this.slope = f;
            this.spikes = new ArrayList(RadarFuG200.this.maxNumOfSpikes);
        }
    }

    public class Contact {

        public void setInvis() {
            for (int i = 0; i < 4; i++) {
                this.acc[i] = -1E-006F;
            }

        }

        public void transform() {
            float f = RadarFuG200.this.dB2size(RadarFuG200.this.baseNoiseAtDist(this.dist) - RadarFuG200.RECEIVER_SENSITIVITY);
            for (int i = 0; i < 4; i++) {
                this.size[i] = RadarFuG200.this.dB2size(this.size[i]);
                if (this.size[i] > 0.0F) {
                    this.acc[i] += this.size[i] / f;
                }
            }

            this.dist *= RadarFuG200.this.displayWidth;
        }

        public Actor actor;
        public float acc[];
        public float size[];
        public float dist;

        public Contact(Actor actor1) {
            this.actor = actor1;
            this.acc = new float[4];
            this.size = new float[4];
            this.setInvis();
        }
    }

    public RadarFuG200() {
        this.AGL_SAMPLES = 5;
        this.MAX_DIST_FASTREJECT = 13000F;
        this.shortMode = false;
        this.gain = 1.0F;
        this.rcs = new RCS_ship();
        this.contacts = new ArrayList(50);
        this.rnd = new Random();
    }

    public void init(Actor actor, float f, float f1, int i, float f2) {
        this.maxNumOfSpikes = i;
        this.me = actor;
        this.agl = (float) this.me.pos.getAbsPoint().z;
        this.spikeSize = f;
        this.spikeSize05 = 0.5F * this.spikeSize;
        this.displayWidthBak = f1;
        this.setMode(0);
        this.spikeHolder = new SpikeHolder[4];
        this.spikeHolder[0] = new SpikeHolder(f2);
        this.spikeHolder[1] = new SpikeHolder(f2);
        this.spikeHolder[2] = new SpikeHolder(f2);
        this.spikeHolder[3] = new SpikeHolder(f2);
        this.setGain(1.0F);
    }

    public boolean isTick(int i, int j)
    {
        if(me == null)
            return false;
        else
            return (Time.tickCounter() + me.hashCode()) % i == j;
    }
    
    public void rareAction() {
        if(!isTick(44, 0)) return;
        int i = this.contacts.size();
        Point3d point3d2 = this.me.pos.getAbsPoint();
        for (int j = 0; j < i; j++) {
            if (!Actor.isValid(((Contact) this.contacts.get(j)).actor)) {
                this.contacts.remove(j);
                i--;
                j--;
            } else {
                Point3d point3d = ((Contact) this.contacts.get(j)).actor.pos.getAbsPoint();
                if ((Math.abs(point3d2.x - point3d.x) / 10F > this.MAX_DIST_FASTREJECT) || (Math.abs(point3d2.y - point3d.y) / 10F > this.MAX_DIST_FASTREJECT)) {
                    this.contacts.remove(j);
                    i--;
                    j--;
                }
            }
        }

        List list = Engine.targets();
        i = list.size();
        for (int k = 0; k < i; k++) {
            Actor actor = (Actor) list.get(k);
            if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric) {
                Point3d point3d1 = actor.pos.getAbsPoint();
                if ((Math.abs(point3d2.x - point3d1.x) / 10F < this.MAX_DIST_FASTREJECT) && (Math.abs(point3d2.y - point3d1.y) / 10F < this.MAX_DIST_FASTREJECT)) {
                    int l;
                    for (l = 0; l < this.contacts.size(); l++) {
                        if (((Contact) this.contacts.get(l)).actor == actor) {
                            break;
                        }
                    }

                    if (l == this.contacts.size()) {
                        this.contacts.add(new Contact(actor));
                    }
                }
            }
        }

    }

    protected void updateAGL() {
        Point3d mePoint = this.me.pos.getAbsPoint();
        Vector3d vRay = new Vector3d();
        Orient orMe = new Orient();
        orMe.set(this.me.pos.getAbsOrient());
        Point3d rayPoint = new Point3d();
        Point3d hitPoint = new Point3d();
        float startYaw = orMe.getYaw();
        float startPitch = 360F - orMe.getPitch();
        float startRoll = 360F - orMe.getRoll();
        double distMin = this.shortMode ? 12000D:120000D;
        
        double airAgl = (float)mePoint.z - (float)Engine.land().HQ_Air(mePoint.x, mePoint.y);
        if (airAgl < distMin) {
            float pitchMin = (float)Math.asin(airAgl / distMin);
            for (int pitch = -30; pitch < 30; pitch+=1) {
                if (startPitch + (float)pitch < pitchMin) continue;
                for (int yaw = -30; yaw < 30; yaw+=10) {
                    orMe.setYPR(startYaw + (float)yaw, -(startPitch + (float)pitch), -startRoll);
                    vRay.set(1D, 0D, 0D);
                    orMe.transform(vRay);
                    vRay.scale(distMin);
                    rayPoint.set(mePoint);
                    rayPoint.add(vRay);
                    if (Landscape.rayHitHQ(mePoint, rayPoint, hitPoint)) {
                        if (!Engine.land().isWater(hitPoint.x, hitPoint.y)) {
                            double hitDist = mePoint.distance(hitPoint);
                            if (hitDist < distMin) distMin = hitDist;
                        }
                    }
                }
            }
        }
        
        float f = (float) distMin;
        this.agl += f - this.lastAlt;
        this.lastAlt = f;
        float f4 = f * f;
        float f5 = 1E+010F;
        for (int i = 0; i < this.AGL_SAMPLES; i++) {
            float f1 = (float) this.rnd.nextGaussian() / 5F;
            float f2 = (float) this.rnd.nextGaussian() / 5F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else if (f1 < -1F) {
                f1 = -1F;
            }
            if (f2 > 1.0F) {
                f2 = 1.0F;
            } else if (f2 < -1F) {
                f2 = -1F;
            }
            float f3 = f - (float) Engine.land().HQ_Air(mePoint.x + (f1 * f), mePoint.y + (f2 * f));
            f3 *= f3;
            f3 += f4 * ((f1 * f1) + (f2 * f2));
            if (f3 < f5) {
                f5 = f3;
            }
        }

        f5 = (float) Math.sqrt(f5);
        this.agl = (this.agl * AGL_FILTER) + ((1.0F - AGL_FILTER) * f5);
    }

    public void setMode(int i) {
        if (i == 1) {
            this.displayWidth = this.displayWidthBak / 1000F;
            this.transmitEcho = 200F;
            this.shortMode = true;
        } else {
            this.displayWidth = this.displayWidthBak / 10000F;
            this.transmitEcho = 300F;
            this.shortMode = false;
        }
    }

    public float getRCSDivisionFactor() {
        return this.rcs.RCS_DIVISION_FACTOR;
    }

    public void setRCSDivisionFactor(float newRCSDivisionFactor) {
        this.rcs.RCS_DIVISION_FACTOR = newRCSDivisionFactor;
    }

    public float getGroundClutterDist() {
        float f = this.agl;
        if (this.shortMode) {
            return f * 10F;
        } else {
            return f;
        }
    }

    protected float dB2size(float f) {
        float f1 = 0.1F + ((float) Math.pow(1.1D, f) - 1.0F);
        if (f1 < 0.0F) {
            return 0.0F;
        }
        f1 *= this.gain_spikeSize;
        if (f1 > this.spikeSize05) {
            f1 = this.spikeSize05 + ((this.spikeSize05 * (f1 - this.spikeSize05)) / f1);
        }
        return f1;
    }

    public void setGain(float f) {
        this.gain = 1.0F / (8F / (0.0008F + f));
        this.gain_spikeSize = this.gain * this.spikeSize;
    }

    protected float baseNoiseAtDist(float f) {
        float f1;
        if (f < this.agl) {
            f1 = NOISE_MIN;
        } else {
            float f2 = this.agl + GROUND_SHIFT;
            float f3 = (f2 * f2) + (f * f / 50F);
            float f4 = (float) Math.sqrt(f3);
            f1 = NOISE_MIN + GROUND_REFL + (23F * (float) Math.log(f2 / (f4 * f3)));
            float f5 = this.rnd.nextFloat();
            f5 *= f5 * f5;
            f1 -= f5 * NOISE_CLUTTER_HOLES;
            if (f1 < NOISE_MIN) {
                f1 = NOISE_MIN;
            }
        }
        return f1;
    }

    protected float noiseAtDist(float f) {
        float f1 = -1F + (2.0F * this.rnd.nextFloat());
        f1 *= f1 * f1;
        f1 *= f1 * f1;
        f1 *= NOISE_GRASS_SIZE * f1 * f1;
        return f1 + this.baseNoiseAtDist(f);
    }

    public int[] getContacts(Vector2f avector2f[][]) {
        Vector3d vector3d = new Vector3d();
        Orient orient = new Orient();
        int ai[] = new int[4];
        this.updateAGL();
        for (int i = 0; i < this.contacts.size(); i++) {
            Contact contact = (Contact) this.contacts.get(i);
            if (!Actor.isValid(contact.actor)) continue;
            vector3d.set((contact.actor.pos.getAbsPoint().x - this.me.pos.getAbsPoint().x), (contact.actor.pos.getAbsPoint().y - this.me.pos.getAbsPoint().y), (contact.actor.pos.getAbsPoint().z - this.me.pos.getAbsPoint().z));
            vector3d.scale(0.1D);
            float f = (float) vector3d.length();
            if (f > 10500F || (this.shortMode && (f > 1050F))) {
                contact.setInvis();
                continue;
            }
            orient.setAT0(vector3d);
            orient.sub(this.me.pos.getAbsOrient());
            float f1 = this.rcs.getRCS(contact.actor, orient);
            f1 += MAXANTGAIN + MAXANTGAIN + 63.54F;
            f1 = (float) (f1 - (2D * (((20D / Math.log(10D)) * Math.log(90F * f)) - 27.55D)));
            f1 -= RECEIVER_SENSITIVITY;
            if (f1 < 0.0F) {
                contact.setInvis();
                continue;
            }
            orient.setAT0(vector3d);
            orient.sub(this.me.pos.getAbsOrient());
            
            double dist = 1D + f / (this.shortMode?100F:1000F);
            dist = (Math.log(dist) / Math.log(11D));
            
            dist *= this.shortMode?1000F:10000F;
            
            contact.dist = (float)dist;
            contact.size[0] = f1 + this.lobe(orient.getTangage(), orient.getAzimut());
            contact.size[1] = f1 + this.lobe(-orient.getTangage(), orient.getAzimut());
            contact.size[2] = f1 + this.lobe(-orient.getAzimut(), orient.getTangage());
            contact.size[3] = f1 + this.lobe(orient.getAzimut(), orient.getTangage());
            contact.transform();
        }

        for (int j = 0; j < 4; j++) {
            ai[j] = 0;
            this.spikeHolder[j].clear();
            for (int k = 0; k < MAX_CONTACTS; k++) {
                float f2 = 0.0F;
                int l = -1;
                for (int i1 = 0; i1 < this.contacts.size(); i1++) {
                    if (((Contact) this.contacts.get(i1)).acc[j] > f2) {
                        l = i1;
                        f2 = ((Contact) this.contacts.get(i1)).acc[j];
                    }
                }

                if (l == -1) {
                    break;
                }
                Contact contact1 = (Contact) this.contacts.get(l);
                contact1.acc[j] = 0.0F;
                if ((contact1.size[j] > 0.0F) && !this.spikeHolder[j].addSpike(contact1.dist, contact1.size[j])) {
                    k--;
                }
            }

            float f3;
            if (this.shortMode) {
                f3 = 10000F;
            } else {
                f3 = 100000F;
            }
            float f8 = this.maxNumOfSpikes - this.spikeHolder[j].size();
            f3 = (f3 - this.transmitEcho) / (f8 + 1.0F);
            float f9 = this.transmitEcho - (this.rnd.nextFloat() * f3);
            for (int j1 = 0; j1 < f8; j1++) {
                float f4 = f9 + (this.rnd.nextFloat() * f3);
                float f6 = this.dB2size(this.noiseAtDist(f4) - RECEIVER_SENSITIVITY);
                double dist = 1D + f4 / (this.shortMode?1000F:10000F);
                dist = (Math.log(dist) / Math.log(11D));
                dist *= this.shortMode?1000F:10000F;
                f4 = (float)dist * this.displayWidth;
                this.spikeHolder[j].addSpike(f4, f6);
                f9 += f3;
            }

            int k1 = 0;
            f3 *= f8;
            float f5;
            float f7;
            while (k1++ < (2 * this.maxNumOfSpikes) && this.spikeHolder[j].size() < this.maxNumOfSpikes) {
                f5 = (this.rnd.nextFloat() * f3) + this.transmitEcho;
                f7 = this.dB2size(this.noiseAtDist(f5) - RECEIVER_SENSITIVITY);
                double dist = 1D + f5 / (this.shortMode?1000F:10000F);
                dist = (Math.log(dist) / Math.log(11D));
                dist *= this.shortMode?1000F:10000F;
                f5 = (float)dist * this.displayWidth;
                this.spikeHolder[j].addSpike(f5, f7);
            }

            ai[j] = this.spikeHolder[j].toArray(avector2f[j]);
        }

        return ai;
    }

    private float lobe(float f, float f1) {
        while (f1 < -180.0F)
            f1 += 360F;
        while (f1 > 180F)
            f1 -= 360F;
        if (f1 < 0.0F) f1 = -f1;
        if (f1 > 90F) {
            f1 = 180F - f1;
            f += 180F;
        }
        while (f > 360F)
            f -= 360F;
        while (f < 0.0F)
            f += 360F;
        int i = (int) (f1 / this.BEAM_WIDTH_NORMAL);
        int j = (int) (f / this.DETECTION_WIDTH_NORMAL);
        float f2 = (f - (j * this.getRes1())) / this.getRes1();
        if (i < 0) i = 0;
        if (j < 0) j = 0;
        if (j > this.aGain.length - 2) j = this.aGain.length - 2;
        if (i > this.aGain[j].length - 2) i = this.aGain[j].length - 2;
        if (i > this.aGain[j + 1].length - 2) i = this.aGain[j + 1].length - 2;
        float f3 = (this.aGain[j][i] * (1.0F - f2)) + (f2 * this.aGain[j][i + 1]);
        float f4 = (this.aGain[j + 1][i] * (1.0F - f2)) + (f2 * this.aGain[j + 1][i + 1]);
        f2 = (f1 - (i * this.getRes2())) / this.getRes2();
        f3 = (f3 * (1.0F - f2)) + (f2 * f4);
        return 2.0F * (f3 - MAXANTGAIN);
    }

    private float getRes1() {
        return this.shortMode ? this.DETECTION_WIDTH_SHORT : this.DETECTION_WIDTH_NORMAL;
    }

    private float getRes2() {
        return this.shortMode ? this.BEAM_WIDTH_SHORT : this.BEAM_WIDTH_NORMAL;
    }

    private float           BEAM_WIDTH_NORMAL      = 15F;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           // Was:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    // 15F
    private float           BEAM_WIDTH_SHORT       = 5F;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             // Was:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // 5F
    private float           DETECTION_WIDTH_NORMAL = 10F;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           // Was:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    // 10F
    private float           DETECTION_WIDTH_SHORT  = 3.3F;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         // Was:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   // 3.3F
    public static final int RADAR_MODE_NORMAL      = 0;
    public static final int RADAR_MODE_SHORT       = 1;

    protected int           AGL_SAMPLES;
    protected static float  AGL_FILTER             = 0.8F;
    private float           agl;
    private float           lastAlt;
    protected static float  GROUND_REFL            = 500F;
    protected static float  NOISE_MIN              = -90F;
    protected static float  GROUND_SHIFT           = 500F;
    protected static float  NOISE_CLUTTER_HOLES    = 90F;
    protected static float  NOISE_GRASS_SIZE       = 20F;
    private Actor           me;
    private float           displayWidth;
    private float           displayWidthBak;
    private ArrayList       contacts;
    RCS_ship                rcs;
    int                     maxNumOfSpikes;
    SpikeHolder             spikeHolder[];
    Random                  rnd;
    float                   transmitEcho;
    float                   gain;
    private float           spikeSize;
    private float           spikeSize05;
    float                   gain_spikeSize;
    private boolean         shortMode;
    private static int      MAX_CONTACTS           = 8;
    private float           MAX_DIST_FASTREJECT;
    protected static float  RECEIVER_SENSITIVITY   = -90F;
    private static float    MAXANTGAIN             = 4.9F;
    private float           aGain[][]              = { { 2.9F, 2.75F, 2.28F, 1.39F, -0.11F, -2.97F, -10F, -2.97F }, { 3.6F, 3.45F, 2.98F, 2.09F, 0.59F, -2.27F, -10F, -2.27F }, { 4.2F, 4.05F, 3.58F, 2.69F, 1.19F, -1.67F, -10F, -1.67F },
            { 4.7F, 4.55F, 4.08F, 3.19F, 1.69F, -1.17F, -10F, -1.17F }, { 4.9F, 4.75F, 4.28F, 3.39F, 1.89F, -0.97F, -10F, -0.97F }, { 4.8F, 4.65F, 4.18F, 3.29F, 1.79F, -1.07F, -10F, -1.07F }, { 4.6F, 4.45F, 3.98F, 3.09F, 1.59F, -1.27F, -10F, -1.27F },
            { 4.2F, 4.05F, 3.58F, 2.69F, 1.19F, -1.67F, -10F, -1.67F }, { 3.7F, 3.55F, 3.08F, 2.19F, 0.69F, -2.17F, -10F, -2.17F }, { 3.3F, 3.15F, 2.68F, 1.79F, 0.29F, -2.57F, -10F, -2.57F }, { 2.4F, 2.25F, 1.78F, 0.89F, -0.61F, -3.47F, -10F, -3.47F },
            { 1.6F, 1.45F, 0.98F, 0.09F, -1.41F, -4.27F, -10F, -4.27F }, { 1.8F, 1.65F, 1.18F, 0.29F, -1.21F, -4.07F, -10F, -4.07F }, { 1.9F, 1.75F, 1.28F, 0.39F, -1.11F, -3.97F, -10F, -3.97F }, { 1.7F, 1.55F, 1.08F, 0.19F, -1.31F, -4.17F, -10F, -4.17F },
            { 1.2F, 1.05F, 0.58F, -0.31F, -1.81F, -4.67F, -10F, -4.67F }, { 0.6F, 0.45F, -0.02F, -0.91F, -2.41F, -5.27F, -10F, -5.27F }, { 0.0F, -0.15F, -0.62F, -1.51F, -3.01F, -5.87F, -10F, -5.87F },
            { 0.0F, -0.15F, -0.62F, -1.51F, -3.01F, -5.87F, -10F, -5.87F }, { 0.0F, -0.15F, -0.62F, -1.51F, -3.01F, -5.87F, -10F, -5.87F }, { 0.6F, 0.45F, -0.02F, -0.91F, -2.41F, -5.27F, -10F, -5.27F },
            { 0.8F, 0.65F, 0.18F, -0.71F, -2.21F, -5.07F, -10F, -5.07F }, { 1.4F, 1.25F, 0.78F, -0.11F, -1.61F, -4.47F, -10F, -4.47F }, { 1.7F, 1.55F, 1.08F, 0.19F, -1.31F, -4.17F, -10F, -4.17F },
            { 2.0F, 1.85F, 1.38F, 0.49F, -1.01F, -3.87F, -10F, -3.87F }, { 2.2F, 2.05F, 1.58F, 0.69F, -0.81F, -3.67F, -10F, -3.67F }, { 2.4F, 2.25F, 1.78F, 0.89F, -0.61F, -3.47F, -10F, -3.47F }, { 2.7F, 2.55F, 2.08F, 1.19F, -0.31F, -3.17F, -10F, -3.17F },
            { 3F, 2.85F, 2.38F, 1.49F, -0.01F, -2.87F, -10F, -2.87F }, { 3.3F, 3.15F, 2.68F, 1.79F, 0.29F, -2.57F, -10F, -2.57F }, { 3.4F, 3.25F, 2.78F, 1.89F, 0.39F, -2.47F, -10F, -2.47F }, { 2.5F, 2.35F, 1.88F, 0.99F, -0.51F, -3.37F, -10F, -3.37F },
            { 1.8F, 1.65F, 1.18F, 0.29F, -1.21F, -4.07F, -10F, -4.07F }, { 0.6F, 0.45F, -0.02F, -0.91F, -2.41F, -5.27F, -10F, -5.27F }, { 1.3F, 1.15F, 0.68F, -0.21F, -1.71F, -4.57F, -10F, -4.57F },
            { 2.2F, 2.05F, 1.58F, 0.69F, -0.81F, -3.67F, -10F, -3.67F }, { 2.9F, 2.75F, 2.28F, 1.39F, -0.11F, -2.97F, -10F, -2.97F } };

}
