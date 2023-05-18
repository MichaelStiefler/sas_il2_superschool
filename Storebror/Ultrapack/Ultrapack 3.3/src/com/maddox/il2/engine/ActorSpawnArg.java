/*4.10.1 class*/
package com.maddox.il2.engine;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Cmd;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class ActorSpawnArg {
    public String                   name;
    public boolean                  armyExist;
    public int                      army;
    public Point3d                  point;
    public Orient                   orient;
    public Actor                    baseActor;
    public String                   hookName;
    public Actor                    ownerActor;
    public String                   iconName;
    public String                   meshName;
    public String                   matName;
    public String                   paramFileName;
    public boolean                  sizeExist;
    public float                    size;
    public boolean                  timeLenExist;
    public float                    timeLen;
    public boolean                  timeNativeExist;
    public boolean                  timeNative;
    public boolean                  typeExist;
    public int                      type;
    public String                   path;
    public String                   target;
    public String                   acoustic;
    public String                   sound;
    public String                   preload;
    public Color3f                  color3f;
    public float[]                  light;
    public boolean                  Z0Exist;
    public float                    Z0;
    public String                   FM;
    public int                      FM_Type;
    public NetChannel               netChannel;
    public int                      netIdRemote;
    public String                   weapons;
    public float                    fuel;
    public Vector3d                 speed;
    public int                      skill;
    public boolean                  bPlayer;
    public boolean                  bornPlaceExist;
    public int                      bornPlace;
    public boolean                  stayPlaceExist;
    public int                      stayPlace;
    public boolean                  bNumberOn = true;
    public String                   rawData;
    public String                   country;
    private static Loc              lempty    = new Loc();
    private static ActorPosMoveInit apos      = new ActorPosMoveInit();

    // TODO: Added by |ZUTI|
    // -------------------------------------------
    public boolean bZutiMultiCrew        = false;
    public boolean bZutiMultiCrewAnytime = false;
    // -------------------------------------------

    public ActorSpawnArg() {
    }

    public ActorSpawnArg(ActorSpawnArg actorspawnarg_0_) {
        this.name = actorspawnarg_0_.name;
        this.armyExist = actorspawnarg_0_.armyExist;
        this.army = actorspawnarg_0_.army;
        this.point = actorspawnarg_0_.point;
        this.orient = actorspawnarg_0_.orient;
        this.baseActor = actorspawnarg_0_.baseActor;
        this.hookName = actorspawnarg_0_.hookName;
        this.ownerActor = actorspawnarg_0_.ownerActor;
        this.iconName = actorspawnarg_0_.iconName;
        this.meshName = actorspawnarg_0_.meshName;
        this.matName = actorspawnarg_0_.matName;
        this.paramFileName = actorspawnarg_0_.paramFileName;
        this.sizeExist = actorspawnarg_0_.sizeExist;
        this.size = actorspawnarg_0_.size;
        this.timeLenExist = actorspawnarg_0_.timeLenExist;
        this.timeLen = actorspawnarg_0_.timeLen;
        this.timeNativeExist = actorspawnarg_0_.timeNativeExist;
        this.timeNative = actorspawnarg_0_.timeNative;
        this.typeExist = actorspawnarg_0_.typeExist;
        this.type = actorspawnarg_0_.type;
        this.path = actorspawnarg_0_.path;
        this.target = actorspawnarg_0_.target;
        this.acoustic = actorspawnarg_0_.acoustic;
        this.sound = actorspawnarg_0_.sound;
        this.preload = actorspawnarg_0_.preload;
        this.color3f = actorspawnarg_0_.color3f;
        this.light = actorspawnarg_0_.light;
        this.Z0 = actorspawnarg_0_.Z0;
        this.Z0Exist = actorspawnarg_0_.Z0Exist;
        this.FM = actorspawnarg_0_.FM;
        this.FM_Type = actorspawnarg_0_.FM_Type;
        this.netChannel = actorspawnarg_0_.netChannel;
        this.netIdRemote = actorspawnarg_0_.netIdRemote;
        this.weapons = actorspawnarg_0_.weapons;
        this.fuel = actorspawnarg_0_.fuel;
        this.speed = actorspawnarg_0_.speed;
        this.skill = actorspawnarg_0_.skill;
        this.bPlayer = actorspawnarg_0_.bPlayer;
        this.bornPlaceExist = actorspawnarg_0_.bornPlaceExist;
        this.bornPlace = actorspawnarg_0_.bornPlace;
        this.stayPlaceExist = actorspawnarg_0_.stayPlaceExist;
        this.stayPlace = actorspawnarg_0_.stayPlace;
        this.bNumberOn = actorspawnarg_0_.bNumberOn;
        this.rawData = actorspawnarg_0_.rawData;
        this.country = actorspawnarg_0_.country;

        // TODO: Added by |ZUTI|
        // ------------------------------------------------------------
        this.bZutiMultiCrew = actorspawnarg_0_.bZutiMultiCrew;
        this.bZutiMultiCrewAnytime = actorspawnarg_0_.bZutiMultiCrewAnytime;
        // ------------------------------------------------------------
    }

    public void clear() {
        this.name = null;
        this.armyExist = false;
        this.army = 0;
        this.point = null;
        this.orient = null;
        this.baseActor = null;
        this.hookName = null;
        this.ownerActor = null;
        this.iconName = null;
        this.meshName = null;
        this.matName = null;
        this.paramFileName = null;
        this.sizeExist = false;
        this.timeLenExist = false;
        this.timeNativeExist = false;
        this.typeExist = false;
        this.path = null;
        this.target = null;
        this.acoustic = null;
        this.sound = null;
        this.preload = null;
        this.color3f = null;
        this.light = null;
        this.Z0 = 0.0F;
        this.Z0Exist = false;
        this.FM = null;
        this.FM_Type = 0;
        this.netChannel = null;
        this.netIdRemote = 0;
        this.weapons = null;
        this.fuel = 100.0F;
        this.speed = null;
        this.skill = 1;
        this.bPlayer = false;
        this.bornPlaceExist = false;
        this.bornPlace = -1;
        this.stayPlaceExist = false;
        this.stayPlace = -1;
        this.bNumberOn = true;
        this.rawData = null;
        this.country = null;

        // TODO: Added by |ZUTI|
        // ------------------------------------------------------------
        this.bZutiMultiCrew = false;
        this.bZutiMultiCrewAnytime = false;
        // ------------------------------------------------------------
    }

    public Actor set(Actor actor) {
        if (this.name != null) actor.setName(this.name);
        if (this.armyExist) actor.setArmy(this.army);
        if (this.baseActor != null) {
            Hook hook = null;
            if (this.hookName != null) hook = this.baseActor.findHook(this.hookName);
            actor.pos.setBase(this.baseActor, hook, false);
            if (this.point != null && this.orient != null) actor.pos.setRel(this.point, this.orient);
            else if (this.point != null) actor.pos.setRel(this.point);
            else if (this.orient != null) actor.pos.setRel(this.orient);
            else actor.pos.setRel(lempty);
            actor.pos.reset();
        } else {
            if (this.point != null && this.orient != null) actor.pos.setAbs(this.point, this.orient);
            else if (this.point != null) actor.pos.setAbs(this.point);
            else if (this.orient != null) actor.pos.setAbs(this.orient);
            else actor.pos.setAbs(lempty);
            actor.pos.reset();
        }
        if (this.ownerActor != null) actor.setOwner(this.ownerActor);
        if (this.speed != null) {
            actor.setSpeed(this.speed);
            if (actor.pos != null) actor.pos.reset();
        }
        if (this.iconName != null) {
            try {
                actor.icon = Mat.New(this.iconName);
            } catch (Exception exception) {
                /* empty */
            }
            if (actor.icon == null) this.ERR_SOFT("Icon : " + this.iconName + " not loaded");
        } else IconDraw.create(actor);
        if (this.bPlayer && actor instanceof Aircraft) World.setPlayerAircraft((Aircraft) actor);
        return actor;
    }

    public Actor setStationary(Actor actor) {
        if (this.name != null) actor.setName(this.name);
        if (!this.armyExist) throw new ActorException(actor.getClass().getName() + ": missing army");
        actor.setArmy(this.army);
        if (this.point == null || this.orient == null) throw new ActorException(actor.getClass().getName() + ": missing pos or orient");
        actor.pos.setAbs(this.point, this.orient);
        actor.pos.reset();
        if (this.iconName != null) {
            try {
                actor.icon = Mat.New(this.iconName);
            } catch (Exception exception) {
                /* empty */
            }
            if (actor.icon == null) this.ERR_SOFT(actor.getClass().getName() + ": icon '" + this.iconName + "' not loaded");
        } else IconDraw.create(actor);
        return actor;
    }

    public Actor setStationaryNoIcon(Actor actor) {
        if (this.name != null) actor.setName(this.name);
        if (!this.armyExist) throw new ActorException(actor.getClass().getName() + ": missing army");
        actor.setArmy(this.army);
        if (this.point == null || this.orient == null) throw new ActorException(actor.getClass().getName() + ": missing pos or orient");
        actor.pos.setAbs(this.point, this.orient);
        actor.pos.reset();
        return actor;
    }

    public Actor setNameOwnerIcon(Actor actor) {
        if (this.name != null) actor.setName(this.name);
        if (this.armyExist) actor.setArmy(this.army);
        if (this.ownerActor != null) actor.setOwner(this.ownerActor);
        if (this.iconName != null) {
            try {
                actor.icon = Mat.New(this.iconName);
            } catch (Exception exception) {
                /* empty */
            }
            if (actor.icon == null) this.ERR_SOFT("Icon : " + this.iconName + " not loaded");
        } else IconDraw.create(actor);
        return actor;
    }

    public Loc getAbsLoc() {
        if (this.baseActor != null) {
            Hook hook = null;
            if (this.hookName != null) hook = this.baseActor.findHook(this.hookName);
            apos.setBase(this.baseActor, hook, false);
            if (this.point != null && this.orient != null) apos.setRel(this.point, this.orient);
            else if (this.point != null) apos.setRel(this.point);
            else if (this.orient != null) apos.setRel(this.orient);
            else apos.setRel(lempty);
        } else if (this.point != null && this.orient != null) apos.setAbs(this.point, this.orient);
        else if (this.point != null) apos.setAbs(this.point);
        else if (this.orient != null) apos.setAbs(this.orient);
        else apos.setAbs(lempty);
        Loc loc = new Loc();
        apos.getAbs(loc);
        apos.setBase(null, null, false);
        return loc;
    }

    public void ERR_HARD(String string) {
        if (Cmd.ERR_HARD) System.err.println("spawn " + Spawn.getLastClassName() + ": " + string);
    }

    public void ERR_SOFT(String string) {
        if (Cmd.ERR_SOFT) System.err.println("spawn " + Spawn.getLastClassName() + ": " + string);
    }

    public boolean isNoExistHARD(Object object, String string) {
        if (object == null) {
            this.ERR_HARD(string);
            return true;
        }
        return false;
    }

    public void INFO_HARD(String string) {
        if (Cmd.INFO_HARD) System.out.println(string);
    }

    public void INFO_SOFT(String string) {
        if (Cmd.INFO_SOFT) System.out.println(string);
    }
}