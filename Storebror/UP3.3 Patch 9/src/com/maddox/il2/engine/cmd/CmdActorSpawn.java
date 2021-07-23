/*4.10.1 class*/
package com.maddox.il2.engine.cmd;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Orient;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.QuoteTokenizer;

public class CmdActorSpawn extends com.maddox.rts.Cmd {
    public static final String EMPTY      = "";
    public static final String NAME       = "NAME";
    public static final String OVR        = "OVR";
    public static final String ARMY       = "ARMY";
    public static final String POSP       = "POSP";
    public static final String POSO       = "POSO";
    public static final String BASED      = "BASED";
    public static final String BASE       = "BASE";
    public static final String HOOK       = "HOOK";
    public static final String OWNER      = "OWNER";
    public static final String ICON       = "ICON";
    public static final String MESH       = "MESH";
    public static final String MAT        = "MAT";
    public static final String PARAMFILE  = "PARAMFILE";
    public static final String SIZE       = "SIZE";
    public static final String TIMELEN    = "TIMELEN";
    public static final String TIMENATIVE = "TIMENATIVE";
    public static final String TYPE       = "TYPE";
    public static final String PATH       = "PATH";
    public static final String TARGET     = "TARGET";
    public static final String ACOUSTIC   = "ACOUSTIC";
    public static final String SOUND      = "SOUND";
    public static final String PRELOAD    = "PRELOAD";
    public static final String COLOR      = "COLOR";
    public static final String LIGHT      = "LIGHT";
    public static final String Z0         = "Z0";
    public static final String FM         = "FM";
    public static final String FM_Type    = "FM_Type";
    public static final String WEAPONS    = "WEAPONS";
    public static final String FUEL       = "FUEL";
    public static final String SPEED      = "SPEED";
    public static final String SKILL      = "SKILL";
    public static final String PLAYER     = "PLAYER";
    public static final String BORNPLACE  = "BORNPLACE";
    public static final String STAYPLACE  = "STAYPLACE";
    public static final String NUMBEROFF  = "NUMBEROFF";
    public static final String RAWDATA    = "RAWDATA";

    // TODO: Added by |ZUTI|
    // -----------------------------------------------------------------
    public static final String Z_MULTICREW         = "Z_MULTICREW";
    public static final String Z_MULTICREW_ANYTIME = "Z_MULTICREW_ANYTIME";
    // -----------------------------------------------------------------

    private boolean                             nameExist;
    private boolean                             ovrExist;
    private com.maddox.il2.engine.Actor         basedActor;
    private com.maddox.util.QuoteTokenizer      tokens;
    private String                              word;
    private com.maddox.il2.engine.ActorSpawnArg sarg;
    private com.maddox.JGP.Point3d              P;
    private com.maddox.il2.engine.Orient        O;
    private float                               light[];
    private com.maddox.JGP.Color3f              color3f;
    private com.maddox.JGP.Vector3d             speed3d;
    private boolean                             bExit;

    class Token {
        public void parse() {
        }

        public String getStr() {
            if (!CmdActorSpawn.this.tokens.hasMoreTokens()) {
                CmdActorSpawn.this.word = null;
                return "";
            }
            StringBuffer stringbuffer = new StringBuffer();
            int i = 0;
            CmdActorSpawn.this.word = null;
            String s;
            for (; CmdActorSpawn.this.tokens.hasMoreTokens(); stringbuffer.append(s)) {
                s = CmdActorSpawn.this.tokens.nextToken();
                if (CmdActorSpawn.this.paramContainsKey(s)) {
                    CmdActorSpawn.this.word = s;
                    break;
                }
                if (i++ > 0) stringbuffer.append(' ');
            }

            if (stringbuffer.length() > 0) return stringbuffer.toString();
            else return "";
        }

        Token() {
        }
    }

    public boolean isRawFormat() {
        return true;
    }

    protected boolean paramContainsKey(String s) {
        return this.param.containsKey(s);
    }

    protected void ERR_HARD(String s) {
        super.ERR_HARD(s);
    }

    public Object exec(com.maddox.rts.CmdEnv cmdenv, String s) {
        com.maddox.il2.engine.ActorSpawn actorspawn = null;
        this.nameExist = false;
        this.ovrExist = false;
        this.basedActor = null;
        this.bExit = false;
        this.sarg.clear();
        this.tokens = new QuoteTokenizer(s);
        for (this.word = null; this.tokens.hasMoreTokens() || this.word != null;) {
            if (this.word == null) this.word = this.tokens.nextToken();
            Token token = (Token) this.param.get(this.word);
            if (token != null) {
                if (actorspawn == null) {
                    this.ERR_HARD("class of actor NOT present");
                    this.basedActor = null;
                    return null;
                }
                token.parse();
                if (this.bExit) {
                    this.basedActor = null;
                    return null;
                }
            } else {
                Object obj = com.maddox.rts.Spawn.get_WithSoftClass(this.word, false);
                if (obj == null) obj = com.maddox.rts.Spawn.get_WithSoftClass("com.maddox.il2." + this.word, false);
                if (obj == null) obj = com.maddox.rts.Spawn.get_WithSoftClass("com.maddox.il2.objects." + this.word, false);
                if (actorspawn == null) {
                    if (obj == null) {
                        this.ERR_HARD("class " + this.word + " NOT found or NOT registered in Spawn database");
                        this.basedActor = null;
                        return null;
                    }
                    if (!(obj instanceof com.maddox.il2.engine.ActorSpawn)) {
                        this.ERR_HARD("class " + this.word + " NOT contains ActorSpawn interface");
                        this.basedActor = null;
                        return null;
                    }
                    actorspawn = (com.maddox.il2.engine.ActorSpawn) obj;
                }
                this.word = null;
            }
        }

        if (this.nameExist) {
            com.maddox.il2.engine.Actor actor = com.maddox.il2.engine.Actor.getByName(this.sarg.name);
            if (actor != null) if (this.ovrExist) actor.destroy();
            else {
                this.ERR_HARD("actor: " + this.sarg.name + " alredy exist");
                this.basedActor = null;
                return null;
            }
        }
        if (this.basedActor != null) {
            com.maddox.JGP.Point3d point3d;
            com.maddox.il2.engine.Orient orient;
            if (this.sarg.baseActor != null) {
                point3d = this.basedActor.pos.getRelPoint();
                orient = this.basedActor.pos.getRelOrient();
            } else {
                point3d = this.basedActor.pos.getAbsPoint();
                orient = this.basedActor.pos.getAbsOrient();
            }
            if (this.sarg.point != null) this.sarg.point.add(point3d);
            else {
                this.P.set(point3d);
                this.sarg.point = this.P;
            }
            if (this.sarg.orient != null) this.sarg.orient.add(orient);
            else {
                this.O.set(orient);
                this.sarg.orient = this.O;
            }
        }
        this.basedActor = null;
        com.maddox.il2.engine.Actor actor1 = actorspawn.actorSpawn(this.sarg);
        if (actor1 != null && com.maddox.il2.engine.Config.isAppEditor()) {
            com.maddox.rts.Property.set(actor1, "spawn", s);
            com.maddox.rts.Property.set(actor1, "spawn arg", new ActorSpawnArg(this.sarg));
        }
        return actor1;
    }

    public CmdActorSpawn() {
        this.sarg = new ActorSpawnArg();
        this.P = new Point3d();
        this.O = new Orient();
        this.light = new float[2];
        this.color3f = new Color3f();
        this.speed3d = new Vector3d();
        this.param.put("NAME", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.name = this.getStr();
                CmdActorSpawn.this.nameExist = true;
            }

        });
        this.param.put("OVR", new Token() {

            public void parse() {
                this.getStr();
                CmdActorSpawn.this.ovrExist = true;
            }

        });
        this.param.put("ARMY", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.armyExist = true;
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.army = Integer.parseInt(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format army: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("POSP", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.point = CmdActorSpawn.this.P;
                CmdActorSpawn.this.sarg.point.set(0.0D, 0.0D, 0.0D);
                if (s.length() > 0) {
                    com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
                    try {
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.sarg.point.x = numbertokenizer.nextDouble();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.sarg.point.y = numbertokenizer.nextDouble();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.sarg.point.z = numbertokenizer.nextDouble();
                    } catch (Exception exception) {
                        CmdActorSpawn.this.ERR_HARD("bad format position: " + s);
                        CmdActorSpawn.this.bExit = true;
                    }
                }
            }

        });
        this.param.put("POSO", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.orient = CmdActorSpawn.this.O;
                CmdActorSpawn.this.sarg.orient.set(0.0F, 0.0F, 0.0F);
                if (s.length() > 0) {
                    com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
                    try {
                        float f = 0.0F;
                        float f1 = 0.0F;
                        float f2 = 0.0F;
                        if (numbertokenizer.hasMoreTokens()) f = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) f1 = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) f2 = numbertokenizer.nextFloat();
                        CmdActorSpawn.this.sarg.orient.set(f, f1, f2);
                    } catch (Exception exception) {
                        CmdActorSpawn.this.ERR_HARD("bad format orientation: " + s);
                        CmdActorSpawn.this.bExit = true;
                    }
                }
            }

        });
        this.param.put("BASED", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.basedActor = com.maddox.il2.engine.Actor.getByName(s);
                if (CmdActorSpawn.this.basedActor == null) {
                    CmdActorSpawn.this.ERR_HARD("based actor: " + s + " not found");
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("BASE", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.baseActor = com.maddox.il2.engine.Actor.getByName(s);
                if (CmdActorSpawn.this.sarg.baseActor == null) {
                    CmdActorSpawn.this.ERR_HARD("base actor: " + s + " not found");
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("HOOK", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.hookName = this.getStr();
            }

        });
        this.param.put("OWNER", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.ownerActor = com.maddox.il2.engine.Actor.getByName(s);
                if (CmdActorSpawn.this.sarg.ownerActor == null) {
                    CmdActorSpawn.this.ERR_HARD("owner actor: " + s + " not found");
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("ICON", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.iconName = this.getStr();
            }

        });
        this.param.put("MESH", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.meshName = this.getStr();
            }

        });
        this.param.put("MAT", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.matName = this.getStr();
            }

        });
        this.param.put("PARAMFILE", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.paramFileName = this.getStr();
            }

        });
        this.param.put("SIZE", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.sizeExist = true;
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.size = Float.parseFloat(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format size: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("TIMELEN", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.timeLenExist = true;
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.timeLen = Float.parseFloat(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format timeLen: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("TIMENATIVE", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.timeNativeExist = true;
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.timeNative = Integer.parseInt(s) != 0;
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format timeNative: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("TYPE", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.typeExist = true;
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.type = Integer.parseInt(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format type: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("PATH", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.path = this.getStr();
            }

        });
        this.param.put("TARGET", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.target = this.getStr();
            }

        });
        this.param.put("ACOUSTIC", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.acoustic = this.getStr();
            }

        });
        this.param.put("SOUND", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.sound = this.getStr();
            }

        });
        this.param.put("PRELOAD", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.preload = this.getStr();
            }

        });
        this.param.put("COLOR", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.color3f = CmdActorSpawn.this.color3f;
                CmdActorSpawn.this.color3f.set(1.0F, 1.0F, 1.0F);
                if (s.length() > 0) {
                    com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
                    try {
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.color3f.x = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.color3f.y = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.color3f.z = numbertokenizer.nextFloat();
                    } catch (Exception exception) {
                        CmdActorSpawn.this.ERR_HARD("bad format color3f: " + s);
                        CmdActorSpawn.this.bExit = true;
                    }
                }
            }

        });
        this.param.put("LIGHT", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.light = CmdActorSpawn.this.light;
                CmdActorSpawn.this.sarg.light[0] = 1.0F;
                CmdActorSpawn.this.sarg.light[1] = 10F;
                if (s.length() > 0) {
                    com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
                    try {
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.sarg.light[0] = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.sarg.light[1] = numbertokenizer.nextFloat();
                    } catch (Exception exception) {
                        CmdActorSpawn.this.ERR_HARD("bad format light: " + s);
                        CmdActorSpawn.this.bExit = true;
                    }
                }
            }

        });
        this.param.put("Z0", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.Z0Exist = true;
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.Z0 = Float.parseFloat(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format Z0: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("FM", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.FM = this.getStr();
            }

        });
        this.param.put("FM_Type", new Token() {

            public void parse() {
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.FM_Type = Integer.parseInt(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format FM_Type: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("WEAPONS", new Token() {

            public void parse() {
                CmdActorSpawn.this.sarg.weapons = this.getStr();
            }

        });
        this.param.put("FUEL", new Token() {

            public void parse() {
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.fuel = Float.parseFloat(s) / 100F;
                    if (CmdActorSpawn.this.sarg.fuel > 1.0F) CmdActorSpawn.this.sarg.fuel = 1.0F;
                    if (CmdActorSpawn.this.sarg.fuel < 0.0F) CmdActorSpawn.this.sarg.fuel = 0.0F;
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format fuel: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("SPEED", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.speed = CmdActorSpawn.this.speed3d;
                CmdActorSpawn.this.speed3d.set(0.0D, 0.0D, -1D);
                if (s.length() > 0) {
                    com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
                    try {
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.speed3d.x = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.speed3d.y = numbertokenizer.nextFloat();
                        if (numbertokenizer.hasMoreTokens()) CmdActorSpawn.this.speed3d.z = numbertokenizer.nextFloat();
                    } catch (Exception exception) {
                        CmdActorSpawn.this.ERR_HARD("bad format speed: " + s);
                        CmdActorSpawn.this.bExit = true;
                    }
                }
            }

        });
        this.param.put("SKILL", new Token() {

            public void parse() {
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.skill = Integer.parseInt(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format skill: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
            }

        });
        this.param.put("PLAYER", new Token() {

            public void parse() {
                this.getStr();
                CmdActorSpawn.this.sarg.bPlayer = true;
            }

        });
        this.param.put("NUMBEROFF", new Token() {

            public void parse() {
                this.getStr();
                CmdActorSpawn.this.sarg.bNumberOn = false;
            }

        });
        this.param.put("BORNPLACE", new Token() {

            public void parse() {
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.bornPlace = Integer.parseInt(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format born place: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
                CmdActorSpawn.this.sarg.bornPlaceExist = true;
            }

        });
        this.param.put("STAYPLACE", new Token() {

            public void parse() {
                String s = this.getStr();
                try {
                    CmdActorSpawn.this.sarg.stayPlace = Integer.parseInt(s);
                } catch (Exception exception) {
                    CmdActorSpawn.this.ERR_HARD("bad format stay place: " + s);
                    CmdActorSpawn.this.bExit = true;
                }
                CmdActorSpawn.this.sarg.stayPlaceExist = true;
            }

        });
        this.param.put("RAWDATA", new Token() {

            public void parse() {
                String s = this.getStr();
                CmdActorSpawn.this.sarg.rawData = s;
            }

        });
        // TODO: Added by |ZUTI|
        // --------------------------------------------
        this.param.put(Z_MULTICREW, new Token() {
            public void parse() {
                this.getStr();
                CmdActorSpawn.this.sarg.bZutiMultiCrew = true;
            }
        });
        this.param.put(Z_MULTICREW_ANYTIME, new Token() {
            public void parse() {
                this.getStr();
                CmdActorSpawn.this.sarg.bZutiMultiCrewAnytime = true;
            }
        });
        // --------------------------------------------
        this._properties.put("NAME", "spawn");
        this._levelAccess = 0;
    }
}