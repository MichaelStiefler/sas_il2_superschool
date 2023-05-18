package com.maddox.il2.objects.vehicles.radars;

import java.io.IOException;
import java.util.List;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.TableFunction2;

public abstract class RotatingRadarGeneric extends ActorHMesh implements MsgExplosionListener, MsgShotListener, Obstacle, Prey, ActorAlign {
    public static class RotatingRadarProperties {

        public String         meshName;
        public String         meshName1;
        public String         meshSummer;
        public String         meshDesert;
        public String         meshWinter;
        public String         meshSummer1;
        public String         meshDesert1;
        public String         meshWinter1;
        public TableFunction2 fnShotPanzer;
        public TableFunction2 fnExplodePanzer;
        public float          PANZER_BODY_FRONT;
        public float          PANZER_BODY_BACK;
        public float          PANZER_BODY_SIDE;
        public float          PANZER_BODY_TOP;
        public float          PANZER_HEAD;
        public float          PANZER_HEAD_TOP;
        public float          PANZER_TNT_TYPE;
        public int            HITBY_MASK;
        public String         explodeName;
        public float          MaxRange;
        public float          RangeUnit;
        public float          RangeAccuracy;
        public float          AltitudeUnit;
        public float          AltitudeAccuracy;
        public float          AzimuthAccuracy;
        public String         RadarID;
        public float          MaxAltitude;
        public float          AntennaHeight;

        public RotatingRadarProperties() {
            this.meshName = null;
            this.meshName1 = null;
            this.meshSummer = null;
            this.meshDesert = null;
            this.meshWinter = null;
            this.meshSummer1 = null;
            this.meshDesert1 = null;
            this.meshWinter1 = null;
            this.fnShotPanzer = null;
            this.fnExplodePanzer = null;
            this.PANZER_BODY_FRONT = 0.001F;
            this.PANZER_BODY_BACK = 0.001F;
            this.PANZER_BODY_SIDE = 0.001F;
            this.PANZER_BODY_TOP = 0.001F;
            this.PANZER_HEAD = 0.001F;
            this.PANZER_HEAD_TOP = 0.001F;
            this.PANZER_TNT_TYPE = 1.0F;
            this.HITBY_MASK = -2;
            this.explodeName = null;
            this.MaxRange = 200F;
            this.RangeUnit = 1.0F;
            this.RangeAccuracy = 1.0F;
            this.AltitudeUnit = 1.0F;
            this.AltitudeAccuracy = 1.0F;
            this.AzimuthAccuracy = 1.0F;
            this.RadarID = null;
            this.MaxAltitude = 10000F;
            this.AntennaHeight = 10F;
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                if (netmsginput.readByte() != 100) {
                    return false;
                }
                if (RotatingRadarGeneric.this.dying == 1) {
                    return true;
                } else {
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                    RotatingRadarGeneric.this.Die(actor, (short) 0, true);
                    return true;
                }
            } else {
                return false;
            }
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                switch (netmsginput.readByte()) {
                    case 68:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
                            this.post(netmsgguaranted);
                        }
                        if (RotatingRadarGeneric.this.dying != 1) {
                            com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                            Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                            RotatingRadarGeneric.this.Die(actor, (short) 1, true);
                        }
                        return true;

                    case 73:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted1);
                        }
                        short word0 = netmsginput.readShort();
                        if ((word0 > 0) && (RotatingRadarGeneric.this.dying != 1)) {
                            RotatingRadarGeneric.this.Die((Actor) null, (short) 1, false);
                        }
                        return true;

                    case 100:
                        NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                        this.postTo(this.masterChannel(), netmsgguaranted2);
                        return true;
                }
                return false;
            } else {
                return true;
            }
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    public static class SPAWN implements ActorSpawn {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1) {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if ((f2 != -9865.345F) && (f2 >= f) && (f2 <= f1)) {
                return f2;
            }
            if (f2 == -9865.345F) {
                System.out.println("Stationary: Parameter [" + s + "]:<" + s1 + "> not found");
            } else {
                System.out.println("Stationary: Value of [" + s + "]:<" + s1 + "> (" + f2 + ") is out of range (" + f + ";" + f1 + ")");
            }
            throw new RuntimeException("Can't set property");
        }

        private static String getS(SectFile sectfile, String s, String s1) {
            String s2 = sectfile.get(s, s1);
            if ((s2 != null) && (s2.length() > 0)) {
                return s2;
            } else {
                System.out.print("Stationary: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            }
        }

        private static String getS(SectFile sectfile, String s, String s1, String s2) {
            String s3 = sectfile.get(s, s1);
            return (s3 == null) || (s3.length() <= 0) ? s2 : s3;
        }

        public static RotatingRadarProperties LoadStationaryProperties(SectFile sectfile, String s, Class class1) {
            RotatingRadarProperties rotatingradarproperties = new RotatingRadarProperties();
            String s1 = SPAWN.getS(sectfile, s, "PanzerType", (String) null);
            if (s1 == null) {
                s1 = "Tank";
            }
            rotatingradarproperties.fnShotPanzer = TableFunctions.GetFunc2(s1 + "ShotPanzer");
            rotatingradarproperties.fnExplodePanzer = TableFunctions.GetFunc2(s1 + "ExplodePanzer");
            rotatingradarproperties.PANZER_TNT_TYPE = SPAWN.getF(sectfile, s, "PanzerSubtype", 0.0F, 100F);
            rotatingradarproperties.meshSummer = SPAWN.getS(sectfile, s, "MeshSummer");
            rotatingradarproperties.meshDesert = SPAWN.getS(sectfile, s, "MeshDesert", rotatingradarproperties.meshSummer);
            rotatingradarproperties.meshWinter = SPAWN.getS(sectfile, s, "MeshWinter", rotatingradarproperties.meshSummer);
            rotatingradarproperties.meshSummer1 = SPAWN.getS(sectfile, s, "MeshSummerDamage", (String) null);
            rotatingradarproperties.meshDesert1 = SPAWN.getS(sectfile, s, "MeshDesertDamage", rotatingradarproperties.meshSummer1);
            rotatingradarproperties.meshWinter1 = SPAWN.getS(sectfile, s, "MeshWinterDamage", rotatingradarproperties.meshSummer1);
            int i = (rotatingradarproperties.meshSummer1 != null ? 0 : 1) + (rotatingradarproperties.meshDesert1 != null ? 0 : 1) + (rotatingradarproperties.meshWinter1 != null ? 0 : 1);
            if ((i != 0) && (i != 3)) {
                System.out.println("Stationary: Uncomplete set of damage meshes for '" + s + "'");
                throw new RuntimeException("Can't register RotatingRadar object");
            }
            rotatingradarproperties.explodeName = SPAWN.getS(sectfile, s, "Explode", "Stationary");
            rotatingradarproperties.PANZER_BODY_FRONT = SPAWN.getF(sectfile, s, "PanzerBodyFront", 0.001F, 9.999F);
            if (sectfile.get(s, "PanzerBodyBack", -9865.345F) == -9865.345F) {
                rotatingradarproperties.PANZER_BODY_BACK = rotatingradarproperties.PANZER_BODY_FRONT;
                rotatingradarproperties.PANZER_BODY_SIDE = rotatingradarproperties.PANZER_BODY_FRONT;
                rotatingradarproperties.PANZER_BODY_TOP = rotatingradarproperties.PANZER_BODY_FRONT;
            } else {
                rotatingradarproperties.PANZER_BODY_BACK = SPAWN.getF(sectfile, s, "PanzerBodyBack", 0.001F, 9.999F);
                rotatingradarproperties.PANZER_BODY_SIDE = SPAWN.getF(sectfile, s, "PanzerBodySide", 0.001F, 9.999F);
                rotatingradarproperties.PANZER_BODY_TOP = SPAWN.getF(sectfile, s, "PanzerBodyTop", 0.001F, 9.999F);
            }
            if (sectfile.get(s, "PanzerHead", -9865.345F) == -9865.345F) {
                rotatingradarproperties.PANZER_HEAD = rotatingradarproperties.PANZER_BODY_FRONT;
            } else {
                rotatingradarproperties.PANZER_HEAD = SPAWN.getF(sectfile, s, "PanzerHead", 0.001F, 9.999F);
            }
            if (sectfile.get(s, "PanzerHeadTop", -9865.345F) == -9865.345F) {
                rotatingradarproperties.PANZER_HEAD_TOP = rotatingradarproperties.PANZER_BODY_TOP;
            } else {
                rotatingradarproperties.PANZER_HEAD_TOP = SPAWN.getF(sectfile, s, "PanzerHeadTop", 0.001F, 9.999F);
            }
            float f = Math.min(Math.min(rotatingradarproperties.PANZER_BODY_BACK, rotatingradarproperties.PANZER_BODY_TOP), Math.min(rotatingradarproperties.PANZER_BODY_SIDE, rotatingradarproperties.PANZER_HEAD_TOP));
            rotatingradarproperties.HITBY_MASK = f <= 0.015F ? -1 : -2;
            rotatingradarproperties.MaxRange = SPAWN.getF(sectfile, s, "MaxRange", 1.0F, 400F);
            rotatingradarproperties.RangeUnit = SPAWN.getF(sectfile, s, "RangeUnit", 1.0F, 3F);
            rotatingradarproperties.RangeAccuracy = SPAWN.getF(sectfile, s, "RangeAccuracy", 0.001F, 10F);
            rotatingradarproperties.AltitudeUnit = SPAWN.getF(sectfile, s, "AltitudeUnit", 1.0F, 2.0F);
            rotatingradarproperties.AltitudeAccuracy = SPAWN.getF(sectfile, s, "AltitudeAccuracy", 0.0F, 10000F);
            rotatingradarproperties.AzimuthAccuracy = SPAWN.getF(sectfile, s, "AzimuthAccuracy", 0.1F, 90F);
            rotatingradarproperties.RadarID = SPAWN.getS(sectfile, s, "RadarID");
            rotatingradarproperties.MaxAltitude = SPAWN.getF(sectfile, s, "MaxAltitude", 1.0F, 30000F);
            rotatingradarproperties.AntennaHeight = SPAWN.getF(sectfile, s, "AntennaHeight", 1.0F, 100F);
            Property.set(class1, "iconName", "icons/" + SPAWN.getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", rotatingradarproperties.meshSummer);
            return rotatingradarproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            switch (World.cur().camouflage) {
                case 1:
                    this.proper.meshName = this.proper.meshWinter;
                    this.proper.meshName1 = this.proper.meshWinter1;
                    break;

                case 2:
                    this.proper.meshName = this.proper.meshDesert;
                    this.proper.meshName1 = this.proper.meshDesert1;
                    break;

                default:
                    this.proper.meshName = this.proper.meshSummer;
                    this.proper.meshName1 = this.proper.meshSummer1;
                    break;
            }
            try {
                RotatingRadarGeneric.constr_arg1 = this.proper;
                RotatingRadarGeneric.constr_arg2 = actorspawnarg;
                RotatingRadarGeneric rotatingRadarGeneric = (RotatingRadarGeneric)this.cls.newInstance();
                RotatingRadarGeneric.constr_arg1 = null;
                RotatingRadarGeneric.constr_arg2 = null;
                return rotatingRadarGeneric;
            } catch (Exception exception) {
                RotatingRadarGeneric.constr_arg1 = null;
                RotatingRadarGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Stationary object [class:" + this.cls.getName() + "]");
            }
            return null;
        }

        public Class                   cls;
        public RotatingRadarProperties proper;

        public SPAWN(Class class1) {
            try {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if (i < j) {
                    i = j;
                }
                String s1 = s.substring(i + 1);
                this.proper = SPAWN.LoadStationaryProperties(Statics.getTechnicsFile(), s1, class1);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + class1.getName());
            }
            this.cls = class1;
            Spawn.add(this.cls, this);
        }
    }

    public static float getConeOfSilenceMultiplier(double d, double d1) {
        float f = 57.32484F * (float) Math.atan2(d - d1, d1);
        return RotatingRadarGeneric.cvt(f, 20F, 40F, 0.0F, 1.0F);
    }

    public static float getTerrainAndNightError(Aircraft aircraft) {
        float f = aircraft.FM.Or.getYaw();
        float f1 = -45F;
        float f2 = RotatingRadarGeneric.mntNE;
        if (RotatingRadarGeneric.mntSE > f2) {
            f2 = RotatingRadarGeneric.mntSE;
            f1 = 45F;
        }
        if (RotatingRadarGeneric.mntSW > f2) {
            f2 = RotatingRadarGeneric.mntSW;
            f1 = 135F;
        }
        if (RotatingRadarGeneric.mntNW > f2) {
            f2 = RotatingRadarGeneric.mntNW;
            f1 = -135F;
        }
        float f3 = f - f1;
        f2 = RotatingRadarGeneric.cvt(f2, 15F, 1400F, 0.0F, 1.0F);
        float f4 = RotatingRadarGeneric.cvt((float) aircraft.FM.Loc.z, 3000F, 15000F, 1.0F, 0.0F);
        f2 *= f4;
        float f5 = RotatingRadarGeneric.rnd.nextFloat();
        if (f3 > 0.0F) {
            RotatingRadarGeneric.terErrDirection = -1;
        } else {
            RotatingRadarGeneric.terErrDirection = 1;
        }
        if (f5 < 0.01D) {
            RotatingRadarGeneric.terErrDirection = 0;
        }
        if (f5 < 0.007D) {
            RotatingRadarGeneric.ngtErrDirection *= -1;
        } else if (f5 < 0.13D) {
            RotatingRadarGeneric.ngtErrDirection = 1;
        } else if (f5 > 0.97D) {
            RotatingRadarGeneric.ngtErrDirection = 0;
        }
        float f6 = RotatingRadarGeneric.terErrDirection * ((f2 * 30F) + RotatingRadarGeneric.rnd.nextFloat(-f2 * 8F, f2 * 8F));
        float f7 = RotatingRadarGeneric.ngtErrDirection * ((RotatingRadarGeneric.nightError * 30F) + RotatingRadarGeneric.rnd.nextFloat(-RotatingRadarGeneric.nightError * 5F, RotatingRadarGeneric.nightError * 5F));
        return f6 + f7;
    }

    private static void sampleMountains(Aircraft aircraft) {
        float f = Math.abs(RotatingRadarGeneric.currentMntSampleCol - 25);
        for (int i = 0; i < RotatingRadarGeneric.mountainSamplesPerCycle; i++) {
            float f1 = -RotatingRadarGeneric.mntSampleRadius + (RotatingRadarGeneric.currentMntSampleRow * RotatingRadarGeneric.mntSingleSampleLen);
            float f2 = -RotatingRadarGeneric.mntSampleRadius + (RotatingRadarGeneric.currentMntSampleCol * RotatingRadarGeneric.mntSingleSampleLen);
            float f3 = Landscape.HQ_Air((float) aircraft.FM.Loc.x + f1, (float) aircraft.FM.Loc.y + f2);
            float f4 = Math.abs(RotatingRadarGeneric.currentMntSampleRow - 25);
            float f5 = Math.max(f4, f);
            f3 *= RotatingRadarGeneric.cvt(f5, 0.0F, 25F, 1.0F, 0.5F);
            RotatingRadarGeneric.mountainErrorSamples[RotatingRadarGeneric.currentMntSampleRow][RotatingRadarGeneric.currentMntSampleCol] = f3;
            RotatingRadarGeneric.currentMntSampleRow++;
            if (RotatingRadarGeneric.currentMntSampleRow != RotatingRadarGeneric.mountainSamplesPerRow) {
                continue;
            }
            RotatingRadarGeneric.currentMntSampleRow = 0;
            RotatingRadarGeneric.currentMntSampleCol++;
            f = Math.abs(RotatingRadarGeneric.currentMntSampleCol - 25);
            if (RotatingRadarGeneric.currentMntSampleCol == RotatingRadarGeneric.mountainSamplesPerRow) {
                RotatingRadarGeneric.currentMntSampleCol = 0;
            }
        }

        int j = 625;
        for (int i1 = 0; i1 < 25; i1++) {
            for (int k = 0; k < 25; k++) {
                RotatingRadarGeneric.mntSW += RotatingRadarGeneric.mountainErrorSamples[i1][k];
            }

        }

        RotatingRadarGeneric.mntSW /= j;
        for (int l = 0; l < 25; l++) {
            for (int j1 = 25; j1 < RotatingRadarGeneric.mountainSamplesPerRow; j1++) {
                RotatingRadarGeneric.mntNW += RotatingRadarGeneric.mountainErrorSamples[l][j1];
            }

        }

        RotatingRadarGeneric.mntNW /= j;
        for (int k1 = 25; k1 < RotatingRadarGeneric.mountainSamplesPerRow; k1++) {
            for (int l1 = 25; l1 < RotatingRadarGeneric.mountainSamplesPerRow; l1++) {
                RotatingRadarGeneric.mntNE += RotatingRadarGeneric.mountainErrorSamples[k1][l1];
            }

        }

        RotatingRadarGeneric.mntNE /= j;
        for (int i2 = 25; i2 < RotatingRadarGeneric.mountainSamplesPerRow; i2++) {
            for (int j2 = 0; j2 < 25; j2++) {
                RotatingRadarGeneric.mntSE += RotatingRadarGeneric.mountainErrorSamples[i2][j2];
            }

        }

        RotatingRadarGeneric.mntSE /= j;
    }

    public static float getSignalAttenuation(Point3d point3d, Aircraft aircraft, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        RotatingRadarGeneric.V.sub(aircraft.FM.Loc, point3d);
        double d = RotatingRadarGeneric.V.length();
        double d1 = 0.0D;
        double d2 = d / 500D;
        for (int i = 0; i < RotatingRadarGeneric.attSamplesPerCycle; i++) {
            double d3 = d2 * RotatingRadarGeneric.currentAttSampleSlot;
            RotatingRadarGeneric.V.normalize();
            RotatingRadarGeneric.V.scale(d3);
            float f = Landscape.HQ_Air((float) (point3d.x + RotatingRadarGeneric.V.x), (float) (point3d.y + RotatingRadarGeneric.V.y));
            double d5 = RotatingRadarGeneric.getCurvatureCorrectedHeight((float) (d3 / d), d, point3d.z, aircraft.FM.getAltitude());
            float f3 = (float) (d5 - f);
            if (f3 < 0.0F) {
                RotatingRadarGeneric.attenuationSamples[RotatingRadarGeneric.currentAttSampleSlot - 1] = -f3;
            } else {
                RotatingRadarGeneric.attenuationSamples[RotatingRadarGeneric.currentAttSampleSlot - 1] = 0.0F;
            }
            RotatingRadarGeneric.currentAttSampleSlot++;
            if (RotatingRadarGeneric.currentAttSampleSlot > RotatingRadarGeneric.numberOfSamplePoints) {
                RotatingRadarGeneric.currentAttSampleSlot = 1;
            }
        }

        float f6 = 0.0F;
        for (int j = 0; j < RotatingRadarGeneric.numberOfSamplePoints; j++) {
            if ((RotatingRadarGeneric.attenuationSamples[j] > f6) && (f6 > 0.0F)) {
                float f1 = RotatingRadarGeneric.attenuationSamples[j] - f6;
                d1 += (RotatingRadarGeneric.attenuationSamples[j] * d2) + (d2 * f1);
            }
            f6 = RotatingRadarGeneric.attenuationSamples[j];
        }

        d1 *= 0.166666D;
        if (flag3) {
            return 0.0F;
        }
        if (!flag2) {
            RotatingRadarGeneric.sampleMountains(aircraft);
        }
        float f2 = 0.0F;
        double d4 = RotatingRadarGeneric.lineOfSightDelta(point3d.z, aircraft.FM.getAltitude(), d);
        if (flag) {
            float f10 = 0.0F;
            if (d4 < 0.0D) {
                f10 = (float) (-2D * d4);
            }
            double d6 = aircraft.FM.getAltitude() - point3d.z;
            float f8 = RotatingRadarGeneric.cvt(World.Sun().ToSun.z, -0.2F, 0.1F, 0.75F, 1.0F);
            if ((World.Sun().ToSun.z > -0.1F) && (World.Sun().ToSun.z < 0.1F) && (Math.random() < 0.1D)) {
                f8 += (float) Math.random() * 0.2F;
            }
            float f4 = 1.0F - RotatingRadarGeneric.getConeOfSilenceMultiplier(d, d6);
            f2 = RotatingRadarGeneric.cvt((float) (d1 + f10) * f8, 0.0F, 7000000F, 0.0F, 1.0F);
            f2 = f2 + f4 + RotatingRadarGeneric.floatindex(RotatingRadarGeneric.cvt((float) d * f8, 0.0F, 270000F, 0.0F, 6F), RotatingRadarGeneric.signalPropagationScale);
            if (f8 < 1.0F) {
                RotatingRadarGeneric.nightError = RotatingRadarGeneric.cvt(f2, 0.65F, 1.0F, 0.0F, 1.0F);
            } else {
                RotatingRadarGeneric.nightError = 0.0F;
            }
        } else if (flag2) {
            f2 = RotatingRadarGeneric.cvt((float) d1, 0.0F, 500000F, 0.0F, 1.0F);
            float f11 = RotatingRadarGeneric.cvt((float) d4, -10000F, 0.0F, 1.0F, 0.0F);
            f2 = f2 + f11 + RotatingRadarGeneric.floatindex(RotatingRadarGeneric.cvt((float) d, 0.0F, 190000F, 0.0F, 6F), RotatingRadarGeneric.signalPropagationScale);
        } else if (flag1) {
            float f12 = 0.0F;
            if (d4 < 0.0D) {
                f12 = (float) (-3D * d4);
            }
            double d7 = aircraft.FM.getAltitude() - point3d.z;
            float f9 = RotatingRadarGeneric.cvt(World.Sun().ToSun.z, -0.2F, 0.2F, 0.4F, 1.0F);
            if ((World.Sun().ToSun.z > -0.1F) && (World.Sun().ToSun.z < 0.1F) && (Math.random() < 0.2D)) {
                f9 += (float) Math.random() * 0.3F;
            }
            float f5 = 1.0F - RotatingRadarGeneric.getConeOfSilenceMultiplier(d, d7);
            f2 = RotatingRadarGeneric.cvt((float) (d1 + f12) * f9, 0.0F, 6000000F, 0.0F, 1.0F);
            f2 = f2 + f5 + RotatingRadarGeneric.floatindex(RotatingRadarGeneric.cvt((float) d * f9, 0.0F, 300000F, 0.0F, 6F), RotatingRadarGeneric.signalPropagationScale);
            if (f9 < 1.0F) {
                RotatingRadarGeneric.nightError = RotatingRadarGeneric.cvt(f2, 0.65F, 1.0F, 0.0F, 1.0F);
            } else {
                RotatingRadarGeneric.nightError = 0.0F;
            }
        }
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }
        return f2;
    }

    private static double getCurvatureCorrectedHeight(float f, double d, double d1, float f1) {
        double d2 = f * Math.sin(d / RotatingRadarGeneric.EARTH_RADIUS) * (6371000F + f1);
        double d3 = d1 + RotatingRadarGeneric.EARTH_RADIUS + (f * ((Math.cos(d / RotatingRadarGeneric.EARTH_RADIUS) * (6371000F + f1)) - d1 - RotatingRadarGeneric.EARTH_RADIUS));
        double d4 = Math.sqrt((d2 * d2) + (d3 * d3)) - RotatingRadarGeneric.EARTH_RADIUS;
        return d4 <= 0.0D ? 0.0D : d4;
    }

    public static double lineOfSightDelta(double d, double d1, double d2) {
        return ((Math.sqrt(12.476D * d) + Math.sqrt(12.476D * d1)) * 1000D) - d2;
    }

    protected static float floatindex(float f, float af[]) {
        int i = (int) f;
        if (i >= (af.length - 1)) {
            return af[af.length - 1];
        }
        if (i < 0) {
            return af[0];
        }
        if (i == 0) {
            return f <= 0.0F ? af[0] : af[0] + (f * (af[1] - af[0]));
        } else {
            return af[i] + ((f % i) * (af[i + 1] - af[i]));
        }
    }

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    private boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public boolean isStaticPos() {
        return true;
    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if ((this.dying == 0) && (shot.power > 0.0F) && (!this.isNetMirror() || !shot.isMirage())) {
            if (shot.powerType == 1) {
                if (!this.RndB(0.15F)) {
                    this.Die(shot.initiator, (short) 0, true);
                }
            } else {
                float f = Shot.panzerThickness(this.pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), this.prop.PANZER_BODY_FRONT, this.prop.PANZER_BODY_SIDE, this.prop.PANZER_BODY_BACK, this.prop.PANZER_BODY_TOP, this.prop.PANZER_HEAD, this.prop.PANZER_HEAD_TOP);
                f *= RotatingRadarGeneric.Rnd(0.93F, 1.07F);
                float f1 = this.prop.fnShotPanzer.Value(shot.power, f);
                if ((f1 < 1000F) && ((f1 <= 1.0F) || this.RndB(1.0F / f1))) {
                    this.Die(shot.initiator, (short) 0, true);
                }
            }
        }
    }

    public void msgExplosion(Explosion explosion) {
        if ((this.dying == 0) && (!this.isNetMirror() || !explosion.isMirage()) && (explosion.power > 0.0F)) {
            int i = explosion.powerType;
            if (i == 1) {
                if (TankGeneric.splintersKill(explosion, this.prop.fnShotPanzer, RotatingRadarGeneric.Rnd(0.0F, 1.0F), RotatingRadarGeneric.Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, this.prop.PANZER_BODY_FRONT, this.prop.PANZER_BODY_SIDE, this.prop.PANZER_BODY_BACK, this.prop.PANZER_BODY_TOP, this.prop.PANZER_HEAD, this.prop.PANZER_HEAD_TOP)) {
                    this.Die(explosion.initiator, (short) 0, true);
                }
            } else {
                int j = explosion.powerType;
                if ((j == 2) && (explosion.chunkName != null)) {
                    this.Die(explosion.initiator, (short) 0, true);
                } else {
                    float f;
                    if (explosion.chunkName != null) {
                        f = 0.5F * explosion.power;
                    } else {
                        f = explosion.receivedTNTpower(this);
                    }
                    f *= RotatingRadarGeneric.Rnd(0.95F, 1.05F);
                    float f1 = this.prop.fnExplodePanzer.Value(f, this.prop.PANZER_TNT_TYPE);
                    if ((f1 < 1000F) && ((f1 <= 1.0F) || this.RndB(1.0F / f1))) {
                        this.Die(explosion.initiator, (short) 0, true);
                    }
                }
            }
        }
    }

    private void ShowExplode(float f, Actor actor) {
        if (f > 0.0F) {
            f = RotatingRadarGeneric.Rnd(f, f * 1.6F);
        }
        Explosions.runByName(this.prop.explodeName, this, "Smoke", "SmokeHead", f, actor);
    }

    private void Die(Actor actor, short word0, boolean flag) {
        if (this.dying == 0) {
            if (word0 <= 0) {
                if (this.isNetMirror()) {
                    this.send_DeathRequest(actor);
                    return;
                }
            }
            this.dying = 1;
            World.onActorDied(this, actor);
            if (this.prop.meshName1 == null) {
                this.mesh().makeAllMaterialsDarker(0.22F, 0.35F);
            } else {
                this.setMesh(this.prop.meshName1);
            }
            int i = this.mesh().hookFind("Ground_Level");
            if (i != -1) {
                Matrix4d matrix4d = new Matrix4d();
                this.mesh().hookMatrix(i, matrix4d);
                this.heightAboveLandSurface = (float) (-matrix4d.m23);
            }
            this.Align();
            if (flag) {
                this.ShowExplode(15F, actor);
            }
            if (flag) {
                this.send_DeathCommand(actor);
            }
        }
    }

    public void destroy() {
        if (!this.isDestroyed()) {
            super.destroy();
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    protected RotatingRadarGeneric() {
        this(RotatingRadarGeneric.constr_arg1, RotatingRadarGeneric.constr_arg2);
    }

    private RotatingRadarGeneric(RotatingRadarProperties rotatingradarproperties, ActorSpawnArg actorspawnarg) {
        super(rotatingradarproperties.meshName);
        this.prop = null;
        this.dying = 0;
        this.prop = rotatingradarproperties;
        actorspawnarg.setStationary(this);
        this.collide(true);
        this.drawing(true);
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.heightAboveLandSurface = 0.0F;
        int i = this.mesh().hookFind("Ground_Level");
        if (i != -1) {
            Matrix4d matrix4d = new Matrix4d();
            this.mesh().hookMatrix(i, matrix4d);
            this.heightAboveLandSurface = (float) (-matrix4d.m23);
        } else {
            System.out.println("Stationary " + this.getClass().getName() + ": hook Ground_Level not found");
        }
        this.Align();
    }

    private void Align() {
        this.pos.getAbs(RotatingRadarGeneric.p);
        RotatingRadarGeneric.p.z = Engine.land().HQ(RotatingRadarGeneric.p.x, RotatingRadarGeneric.p.y) + this.heightAboveLandSurface;
        RotatingRadarGeneric.o.setYPR(this.pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        this.pos.setAbs(RotatingRadarGeneric.p, RotatingRadarGeneric.o);
    }

    public void align() {
        this.Align();
    }

    public boolean unmovableInFuture() {
        return true;
    }

    public void collisionDeath() {
        if (!this.isNet()) {
            this.ShowExplode(-1F, (Actor) null);
            this.destroy();
        }
    }

    public float futurePosition(float f, Point3d point3d) {
        this.pos.getAbs(point3d);
        return f > 0.0F ? f : 0.0F;
    }

    private void send_DeathCommand(Actor actor) {
        if (this.isNetMaster()) {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try {
                netmsgguaranted.writeByte(68);
                netmsgguaranted.writeNetObj(actor != null ? actor.net : null);
                this.net.post(netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void send_DeathRequest(Actor actor) {
        if (this.isNetMirror() && !(this.net.masterChannel() instanceof NetChannelInStream)) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(100);
                netmsgguaranted.writeNetObj(actor != null ? actor.net : null);
                this.net.postTo(this.net.masterChannel(), netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    public void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) {
            this.net = new Master(this);
        } else {
            this.net = new Mirror(this, netchannel, i);
        }
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        if (this.dying == 0) {
            netmsgguaranted.writeShort(0);
        } else {
            netmsgguaranted.writeShort(1);
        }
        this.net.postTo(netchannel, netmsgguaranted);
    }

    protected static float cvt(float f, float f1, float f2, float f3, float f4) {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + (((f4 - f3) * (f - f1)) / (f2 - f1));
    }

    public int HitbyMask() {
        return this.prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties abulletproperties[]) {
        if (this.dying != 0) {
            return -1;
        }
        if (abulletproperties.length == 1) {
            return 0;
        }
        if (abulletproperties.length <= 0) {
            return -1;
        }
        if (abulletproperties[0].power <= 0.0F) {
            return 0;
        }
        if (abulletproperties[1].power <= 0.0F) {
            return 1;
        }
        if (abulletproperties[0].cumulativePower > 0.0F) {
            return 0;
        }
        if (abulletproperties[1].cumulativePower > 0.0F) {
            return 1;
        }
        if (abulletproperties[0].powerType == 1) {
            return 0;
        }
        if (abulletproperties[1].powerType == 1) {
            return 1;
        } else {
            return abulletproperties[0].powerType != 0 ? 0 : 1;
        }
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        return this.dying == 0 ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if ((this.dying != 0) || (i != 0)) {
            return false;
        }
        if (point3d != null) {
            point3d.set(0.0D, 0.0D, 0.0D);
        }
        return true;
    }

    public boolean PlayerAircraftSearch() {
        float f = this.prop.MaxRange * 1000F;
        if (Mission.curCloudsType() == 6) {
            f /= 2.0F;
        }
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().z;
        double d4 = this.prop.MaxAltitude + d3;
        int i = 3570;
        if (World.Sun().ToSun.z <= 0.0F) {
            i = 4120;
        }
        double d5 = this.prop.AntennaHeight;
        if ((this.getArmy() == World.getPlayerArmy()) && (aircraft.pos.getAbsPoint().distance(point3d) <= (f * i) / 3570F) && Actor.isAlive(aircraft) && (d2 >= d3) && (aircraft.pos.getAbsPoint().distance(point3d) <= (i * (Math.sqrt(d2) + Math.sqrt(d5)))) && (aircraft.pos.getAbsPoint().distance(point3d) >= ((d2 - d3) * 2D)) && (d2 <= d4)) {
            this.EnemyAircraftSearch();
        }
        if ((this.getArmy() == World.getPlayerArmy()) && (aircraft.pos.getAbsPoint().distance(point3d) <= (f * i) / 3570F) && Actor.isAlive(aircraft) && ((d2 < d3) || (aircraft.pos.getAbsPoint().distance(point3d) > (i * (Math.sqrt(d2) + Math.sqrt(d5)))) || (aircraft.pos.getAbsPoint().distance(point3d) < ((d2 - d3) * 2D)) || (d2 > d4))) {
            HUD.log(this.prop.RadarID + ": We lost your signal!");
        }
        this.RefreshInterval = Time.current();
        return true;
    }

    private boolean EnemyAircraftSearch() {
        float f = this.prop.MaxRange * 1000F;
        if (Mission.curCloudsType() == 6) {
            f /= 2.0F;
        }
        this.Interception = false;
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + this.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().z;
        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        int i = -((int) aircraft.pos.getAbsOrient().getYaw() - 90);
        if (i < 0) {
            i += 360;
        }
        double d5 = this.prop.MaxAltitude + d2;
        int j = 3570;
        if (World.Sun().ToSun.z <= 0.0F) {
            j = 4120;
        }
        List list = Engine.targets();
        int k = list.size();
        int l = 0;
        double d6 = f;
        for (int i1 = 0; i1 < k; i1++) {
            Actor actor = (Actor) list.get(i1);
            if (!(actor instanceof Aircraft) || (actor.getArmy() == World.getPlayerArmy()) || (actor == World.getPlayerAircraft()) || (actor.getSpeed(vector3d) < 30D)) {
                continue;
            }
            double d7 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
            double d9 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
            double d16 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
            double d11 = d - d7;
            double d13 = d1 - d9;
            double d17 = this.prop.AntennaHeight;
            int l1 = (int) Math.round(Math.sqrt((d13 * d13) + (d11 * d11)));
            if ((l1 <= ((f * j) / 3570F)) && (d16 >= d2) && (l1 <= (j * (Math.sqrt(d16) + Math.sqrt(d17)))) && (l1 >= ((d16 - d2) * 2D)) && (d16 <= d5)) {
                this.Interception = true;
            }
            if ((l1 <= ((f * j) / 3570F)) && (d16 >= d2) && ((l1 > (j * (Math.sqrt(d16) + Math.sqrt(d17)))) || (l1 < ((d16 - d2) * 2D)))) {
                this.Interception = false;
                HUD.log(this.prop.RadarID + ": Target lost!");
            }
            if (l1 < d6) {
                l = i1;
                d6 = l1;
            }
        }

        if (this.Interception) {
            Actor actor1 = (Actor) list.get(l);
            double d8 = Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x;
            double d10 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y;
            double d12 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().z;
            if (d12 <= 0.0D) {
                d12 = 0.0D;
            }
            double d14 = d8 - d3;
            double d15 = d10 - d4;
            float f1 = 57.32484F * (float) Math.atan2(d15, -d14);
            int j1 = Math.round(f1) - 90;
            if (j1 < 0) {
                j1 += 360;
            }
            int k1 = j1 - i;
            double d18 = d3 - d8;
            double d19 = d4 - d10;
            int j2 = (int) Math.round(Math.sqrt((d19 * d19) + (d18 * d18)));
            if (k1 < 0) {
                k1 += 360;
            }
            k1 = (int) (Math.round(k1 / this.prop.AzimuthAccuracy) * this.prop.AzimuthAccuracy);
            String s2 = "  ";
            if ((k1 <= 0) || (k1 >= 360)) {
                s2 = "dead ahead ";
            }
            if ((k1 > 0) && (k1 < 180)) {
                s2 = "right ";
            }
            if (k1 == 180) {
                s2 = "behind ";
            }
            if ((k1 > 180) && (k1 < 360)) {
                s2 = "left ";
            }
            if (k1 > 180) {
                k1 = 360 - k1;
            }
            float f2 = 1000F;
            String s3 = "   ";
            if (this.prop.RangeUnit == 1.0F) {
                f2 = 1000F;
                s3 = " km";
            }
            if (this.prop.RangeUnit == 2.0F) {
                f2 = 1609.344F;
                s3 = " mi";
            }
            if (this.prop.RangeUnit == 3F) {
                f2 = 1852F;
                s3 = " nm";
            }
            float f3 = 1.0F;
            String s4 = "   ";
            if (this.prop.AltitudeUnit == 1.0F) {
                f3 = 1.0F;
                s4 = " m";
            }
            if (this.prop.AltitudeUnit == 2.0F) {
                f3 = 0.3048F;
                s4 = " ft";
            }
            int l2 = (int) (Math.round(d12 / f3 / this.prop.AltitudeAccuracy) * this.prop.AltitudeAccuracy);
            float f4 = Math.round(j2 / f2 / this.prop.RangeAccuracy) * this.prop.RangeAccuracy;
            float f5 = (float) (Math.round(f4 * 1000D) / 1000D);
            if ((l2 > 0) && (j2 <= (2.0F * ((f * j) / 3570F))) && (j2 > 10000)) {
                HUD.log(this.prop.RadarID + ": Target " + s2 + k1 + "\260, range " + f5 + s3 + ", altitude " + l2 + s4);
            }
            if ((World.Sun().ToSun.z > 0.0F) && (l2 > 0) && (j2 <= 10000) && (j2 > 0)) {
                HUD.logCenter(this.prop.RadarID + ": Target " + s2 + k1 + "\260, range " + f5 + s3 + ", altitude " + l2 + s4);
            }
            if ((World.Sun().ToSun.z <= 0.0F) && (l2 > 0) && (j2 <= 10000) && (j2 > 0)) {
                HUD.log(this.prop.RadarID + ": Target " + s2 + k1 + "\260, range " + f5 + s3 + ", altitude " + l2 + s4);
            }
            if ((l2 <= 0) && (j2 <= (2.0F * ((f * j) / 3570F))) && (j2 > 10000)) {
                HUD.log(this.prop.RadarID + ": Target " + s2 + k1 + "\260, range " + f5 + s3);
            }
            if ((World.Sun().ToSun.z > 0.0F) && (l2 <= 0) && (j2 <= 10000) && (j2 > 0)) {
                HUD.logCenter(this.prop.RadarID + ": Target " + s2 + k1 + "\260, range " + f5 + s3);
            }
            if ((World.Sun().ToSun.z <= 0.0F) && (l2 <= 0) && (j2 <= 10000) && (j2 > 0)) {
                HUD.log(this.prop.RadarID + ": Target " + s2 + k1 + "\260, range " + f5 + s3);
            }
        }
        return true;
    }

    private RotatingRadarProperties       prop;
    private float                         heightAboveLandSurface;
    public int                            dying;
    static final int                      DYING_NONE               = 0;
    static final int                      DYING_DEAD               = 1;
    public static RotatingRadarProperties constr_arg1              = null;
    private static ActorSpawnArg          constr_arg2              = null;
    private static Point3d                p                        = new Point3d();
    private static Orient                 o                        = new Orient();
    protected static Vector3d             V                        = new Vector3d();
    private static final int              numberOfSamplePoints     = 500;
    private static final int              attSamplesPerCycle       = 20;
    private static float                  attenuationSamples[]     = new float[RotatingRadarGeneric.numberOfSamplePoints];
    private static int                    currentAttSampleSlot     = 1;
    private static final int              mountainSamplesPerRow    = 50;
    private static final int              mountainSamplesPerCycle  = 10;
    private static int                    currentMntSampleCol      = 0;
    private static int                    currentMntSampleRow      = 0;
    private static float                  mountainErrorSamples[][] = new float[RotatingRadarGeneric.mountainSamplesPerRow][RotatingRadarGeneric.mountainSamplesPerRow];
    private static final float            mntSampleRadius          = 20000F;
    private static final float            mntSingleSampleLen       = 800F;
    private static final int              EARTH_RADIUS             = 6371000;
    private static float                  nightError               = 0.0F;
    private static int                    terErrDirection          = 1;
    private static int                    ngtErrDirection          = -1;
    private static float                  mntNE                    = 0.0F;
    private static float                  mntSE                    = 0.0F;
    private static float                  mntSW                    = 0.0F;
    private static float                  mntNW                    = 0.0F;
    private static RangeRandom            rnd                      = new RangeRandom();
    private static final float            signalPropagationScale[] = { 0.0F, 0.4F, 0.6F, 0.77F, 0.89F, 0.97F, 1.0F };
    public long                           RefreshInterval;
    private boolean                       Interception;

}
