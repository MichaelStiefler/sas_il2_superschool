package com.maddox.il2.ai;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.DServer;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.buildings.House;
import com.maddox.rts.HomePath;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;

public class EventLog
{
    public static class Action
    {

        public int event;
        public float time;
        public String arg0;
        public int scoreItem0;
        public int army0;
        public String arg1;
        public String arg2;
        public int scoreItem1;
        public int argi;
        public float x;
        public float y;

        public Action()
        {
        }

        public Action(int i, String s, int j, String s1, int k, int l, float f, 
                float f1, String s2)
        {
            event = i;
            time = World.getTimeofDay();
            arg0 = s;
            scoreItem0 = j;
            army0 = 0;
            if(s != null)
            {
                Actor actor = Actor.getByName(s);
                if(actor != null)
                    army0 = actor.getArmy();
            }
            arg1 = s1;
            arg2 = s2;
            scoreItem1 = k;
            argi = l;
            x = f;
            y = f1;
            if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isDedicated())
            {
                return;
            } else
            {
                EventLog.actions.add(this);
                return;
            }
        }

        public Action(int i, String s, int j, String s1, int k, int l, float f, 
                float f1)
        {
            event = i;
            time = World.getTimeofDay();
            arg0 = s;
            scoreItem0 = j;
            army0 = 0;
            if(s != null)
            {
                Actor actor = Actor.getByName(s);
                if(actor != null)
                    army0 = actor.getArmy();
            }
            arg1 = s1;
            scoreItem1 = k;
            argi = l;
            x = f;
            y = f1;
            if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isDedicated())
            {
                return;
            } else
            {
                EventLog.actions.add(this);
                return;
            }
        }
    }


    public EventLog()
    {
    }

    public static void flyPlayer(Point3d point3d)
    {
        if(Mission.isDogfight())
            return;
        if(lastFly != null)
        {
            double d = ((double)lastFly.x - point3d.x) * ((double)lastFly.x - point3d.x) + ((double)lastFly.y - point3d.y) * ((double)lastFly.y - point3d.y);
            if(d < 250000D)
                return;
        }
        lastFly = new Action(11, null, -1, null, -1, 0, (float)point3d.x, (float)point3d.y);
    }

    public static void resetGameClear()
    {
        actions.clear();
        lastFly = null;
    }

    public static void resetGameCreate()
    {
    }

    private static void checkInited()
    {
        if(!bInited)
        {
            logKeep = Config.cur.ini.get("game", "eventlogkeep", 1, 0, 1) == 1;
            fileName = Config.cur.ini.get("game", "eventlog", (String)null);
            bInited = true;
        }
        if(longDate == null)
        {
            longDate = DateFormat.getDateTimeInstance(2, 2);
            shortDate = DateFormat.getTimeInstance(2);
        }
    }

    public static void checkState()
    {
        checkInited();
        if(fileName == null && Main.cur().campaign != null && Main.cur().campaign.isDGen())
        {
            fileName = "eventlog.lst";
            Config.cur.ini.set("game", "eventlog", "eventlog.lst");
            Config.cur.ini.saveFile();
        }
        if(logKeep)
        {
            checkBuffering();
        } else
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(Exception exception) { }
                file = null;
            }
            if(fileName != null)
                try
                {
                    File file1 = new File(HomePath.toFileSystemName(fileName, 0));
                    file1.delete();
                }
                catch(Exception exception1) { }
        }
    }

    private static void checkBuffering()
    {
        if(file == null)
            return;
        boolean flag = true;
        if((Main.cur() instanceof DServer) || Main.cur().netServerParams != null && !Main.cur().netServerParams.isSingle() && Main.cur().netServerParams.isMaster())
            flag = false;
        if(flag != bBuffering)
        {
            close();
            open();
        }
    }

    public static boolean open()
    {
        if(file != null)
            return true;
        checkInited();
        if(fileName == null)
            return false;
        bBuffering = !(Main.cur() instanceof DServer) && (Main.cur().netServerParams == null || Main.cur().netServerParams.isSingle() || !Main.cur().netServerParams.isMaster());
        try
        {
            file = new PrintStream(new FileOutputStream(new File(PIPE_URL)));
            file.print(HomePath.toFileSystemName(fileName, 0) + (bBuffering?PIPE_FLUSH_TIMEOUT_1000:PIPE_FLUSH_TIMEOUT_0));
            file.flush();
        }
        catch(FileNotFoundException fnfe)
        {
            try
            {
                if (bBuffering) {
                    file = new PrintStream(new BufferedOutputStream(new FileOutputStream(HomePath.toFileSystemName(fileName, 0), true), 2048));
                } else {
                    file = new PrintStream(new FileOutputStream(HomePath.toFileSystemName(fileName, 0), true));
                }
            }
            catch(Exception e1)
            {
                return false;
            }
        }
        catch(Exception e2)
        {
            return false;
        }
        return true;
    }

    public static void close()
    {
        if(file != null)
        {
            file.flush();
            file.close();
            file = null;
        }
    }

    public static StringBuffer logOnTime(float f)
    {
        if(Mission.isDogfight())
        {
            Calendar calendar = Calendar.getInstance();
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("[");
            // TODO: +++ Instant Log by SAS~Storebror +++
            if (shortDate == null) {
                shortDate = DateFormat.getTimeInstance(DateFormat.MEDIUM);
            }
            // TODO: --- Instant Log by SAS~Storebror ---
            stringbuffer.append(shortDate.format(calendar.getTime()));
            stringbuffer.append("] ");
            return stringbuffer;
        }
        int i = (int)f;
        int j = (int)(((f - (float)i) * 60F) % 60F);
        int k = (int)(((f - (float)i - (float)(j / 60)) * 3600F) % 60F);
        StringBuffer stringbuffer1 = new StringBuffer();
        if(i < 10)
            stringbuffer1.append('0');
        stringbuffer1.append(i);
        stringbuffer1.append(':');
        if(j < 10)
            stringbuffer1.append('0');
        stringbuffer1.append(j);
        stringbuffer1.append(':');
        if(k < 10)
            stringbuffer1.append('0');
        stringbuffer1.append(k);
        stringbuffer1.append(' ');
        return stringbuffer1;
    }

    public static String name(Actor actor)
    {
        if(!Actor.isValid(actor))
            return "";
        if(Mission.isNet() && Mission.isDogfight() && (actor instanceof Aircraft))
        {
            Aircraft aircraft = (Aircraft)actor;
            NetUser netuser = aircraft.netUser();
            if(netuser != null)
                return netuser.shortName() + ":" + Property.stringValue(aircraft.getClass(), "keyName", "");
            else
                return Property.stringValue(aircraft.getClass(), "keyName", "");
        } else
        {
            return actor.name();
        }
    }

    private static boolean isTyping(int i)
    {
        if(Main.cur().netServerParams == null)
            return true;
        if(Main.cur().netServerParams.isMaster())
            return true;
        else
            return (Main.cur().netServerParams.eventlogClient() & 1 << i) != 0;
    }

    public static void type(int i, float f, String s, String s1, int j, float f1, float f2, boolean flag)
    {
        type(i, f, s, s1, null, j, f1, f2, flag);
    }

    public static void type(int i, float f, String s, String s1, String s2, int j, float f1, float f2, 
            boolean flag)
    {
        StringBuffer stringbuffer = logOnTime(f);
        switch(i)
        {
        case 0:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") seat occupied by ");
            stringbuffer.append(s1);
            stringbuffer.append(" at ");
            break;

        case 1:
            stringbuffer.append(s1);
            stringbuffer.append(" is trying to occupy seat ");
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(")");
            break;

        case 2:
            stringbuffer.append(s);
            stringbuffer.append(" crashed at ");
            break;

        case 3:
            stringbuffer.append(s);
            stringbuffer.append(" shot down by ");
            stringbuffer.append(s1);
            if(s2 != null && !s2.equals(""))
            {
                stringbuffer.append(" and ");
                stringbuffer.append(s2);
            }
            stringbuffer.append(" at ");
            break;

        case 4:
            stringbuffer.append(s);
            stringbuffer.append(" destroyed by ");
            stringbuffer.append(s1);
            stringbuffer.append(" at ");
            break;

        case 5:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") bailed out at ");
            break;

        case 6:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            if(s1 == null || "".equals(s1))
            {
                stringbuffer.append(") was killed at ");
            } else
            {
                stringbuffer.append(") was killed by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");
            }
            break;

        case 7:
            stringbuffer.append(s);
            stringbuffer.append(" landed at ");
            break;

        case 24:
            stringbuffer.append(s);
            stringbuffer.append(" in flight at ");
            break;

        case 8:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was captured at ");
            break;

        case 9:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was wounded at ");
            break;

        case 10:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was heavily wounded at ");
            break;

        case 12:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was killed in his chute by ");
            stringbuffer.append(s1);
            stringbuffer.append(" at ");
            break;

        case 13:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") has chute destroyed by ");
            stringbuffer.append(s1);
            stringbuffer.append(" at ");
            break;

        case 14:
            stringbuffer.append(s);
            stringbuffer.append(" turned wingtip smokes on at ");
            break;

        case 15:
            stringbuffer.append(s);
            stringbuffer.append(" turned wingtip smokes off at ");
            break;

        case 16:
            stringbuffer.append(s);
            stringbuffer.append(" turned landing lights on at ");
            break;

        case 17:
            stringbuffer.append(s);
            stringbuffer.append(" turned landing lights off at ");
            break;

        case 18:
            stringbuffer.append(s);
            stringbuffer.append(" loaded weapons '");
            stringbuffer.append(s1);
            stringbuffer.append("' fuel ");
            stringbuffer.append(j);
            stringbuffer.append("%");
            break;

        case 19:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") successfully bailed out at ");
            break;

        case 20:
            stringbuffer.append(s);
            stringbuffer.append(" damaged on the ground at ");
            break;

        case 21:
            stringbuffer.append(s);
            stringbuffer.append(" damaged by ");
            stringbuffer.append(s1);
            stringbuffer.append(" at ");
            break;

        case 22:
            stringbuffer.append(s);
            stringbuffer.append(" has disconnected");
            break;

        case 23:
            stringbuffer.append(s);
            stringbuffer.append(" has connected");
            break;

        case 25:
            stringbuffer.append(s);
            stringbuffer.append(" entered refly menu");
            break;

        case 26:
            stringbuffer.append(s);
            stringbuffer.append(" removed at ");
            break;

        case 27:
            stringbuffer.append(s);
            stringbuffer.append(" selected army ");
            stringbuffer.append(Army.name(j));
            stringbuffer.append(" at ");
            break;

        case 11:
        default:
            return;
        }
        if(i != 1 && i != 18 && i != 22 && i != 23 && i != 25)
        {
            stringbuffer.append(f1);
            stringbuffer.append(" ");
            stringbuffer.append(f2);
        }
        if(open() && isTyping(i)) {
            file.println(stringbuffer);
            //close();
        }
        if(flag)
            ((NetUser)NetEnv.host()).replicateEventLog(i, f, s, s1, j, f1, f2);
    }

    public static void type(boolean flag, String s)
    {
        if(open())
        {
            if(flag)
            {
                Calendar calendar = Calendar.getInstance();
                if(longDate == null) longDate = DateFormat.getDateTimeInstance(2, 2);
                file.println("[" + longDate.format(calendar.getTime()) + "] " + s);
            } else
            {
                file.println(s);
            }
            //close();
        }
    }

    public static void type(String s)
    {
        StringBuffer stringbuffer = logOnTime(World.getTimeofDay());
        stringbuffer.append(s);
        if(open())
        {
            file.println(stringbuffer);
            //close();
        }
    }

    public static void onOccupied(Aircraft aircraft, NetUser netuser, int i)
    {
        if(!Actor.isValid(aircraft))
            return;
        String s = null;
        if(netuser != null)
            s = netuser.uniqueName();
        else
        if(Mission.isSingle())
            s = "Player";
        if(s == null)
            return;
        type(0, World.getTimeofDay(), name(aircraft), s, i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
        if(!Mission.isDogfight())
            new Action(0, name(aircraft), 0, s, -1, i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
    }

    public static void onTryOccupied(String s, NetUser netuser, int i)
    {
        String s1 = null;
        if(netuser != null)
            s1 = netuser.uniqueName();
        else
        if(Mission.isSingle())
            s1 = "Player";
        if(s1 == null)
        {
            return;
        } else
        {
            type(1, World.getTimeofDay(), s, s1, i, 0.0F, 0.0F, true);
            return;
        }
    }

    public static void onActorDied(Actor actor, Actor actor1, Actor actor2)
    {
        if(!Mission.isPlaying())
            return;
        if(!Actor.isValid(actor))
            return;
        if((actor instanceof House) && Main.cur().netServerParams != null && Main.cur().netServerParams.eventlogHouse())
        {
            House house = (House)actor;
            type(4, World.getTimeofDay(), house.getMeshLiveName(), name(actor1), 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, false);
            return;
        }
        if(!actor.isNamed())
            return;
        if(actor.isNetMirror())
            return;
        if(World.cur().scoreCounter.getRegisteredType(actor) < 0)
            return;
        if(actor1 == World.remover)
            type(26, World.getTimeofDay(), name(actor), "", 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
        else
        if(actor == actor1 || !Actor.isValid(actor1) || !actor1.isNamed())
        {
            type(2, World.getTimeofDay(), name(actor), "", 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            if(!Mission.isDogfight())
                new Action(2, name(actor), World.cur().scoreCounter.getRegisteredType(actor), null, -1, 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y);
        } else
        if(actor instanceof Aircraft)
        {
            type(3, World.getTimeofDay(), name(actor), name(actor1), name(actor2), 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            if(!Mission.isDogfight())
                new Action(3, name(actor), 0, name(actor1), World.cur().scoreCounter.getRegisteredType(actor1), 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, name(actor2));
        } else
        {
            type(4, World.getTimeofDay(), name(actor), name(actor1), 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            if(!Mission.isDogfight())
                new Action(4, name(actor), World.cur().scoreCounter.getRegisteredType(actor), name(actor1), World.cur().scoreCounter.getRegisteredType(actor1), 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y);
        }
    }

    public static void onBailedOut(Aircraft aircraft, int i)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
            return;
        type(5, World.getTimeofDay(), name(aircraft), "", i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
        if(!Mission.isDogfight())
            new Action(5, name(aircraft), 0, null, -1, i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
    }

    public static void onPilotKilled(Aircraft aircraft, int i, Actor actor)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
            return;
        String s = null;
        if(Actor.isValid(actor))
            s = name(actor);
        type(6, World.getTimeofDay(), name(aircraft), s != null ? s : "", i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
        if(!Mission.isDogfight())
            new Action(6, name(aircraft), 0, s, -1, i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
    }

    public static void onPilotKilled(Actor actor, String s, int i)
    {
        if(!Mission.isPlaying())
            return;
        type(6, World.getTimeofDay(), s, "", i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
        if(!Mission.isDogfight())
            new Action(6, s, 0, null, -1, i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y);
    }

    public static void onAirLanded(Aircraft aircraft)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
            return;
        type(7, World.getTimeofDay(), name(aircraft), "", 0, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
        if(!Mission.isDogfight())
            new Action(7, name(aircraft), 0, null, -1, 0, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
    }

    public static void onAirInflight(Aircraft aircraft)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
        {
            return;
        } else
        {
            type(24, World.getTimeofDay(), name(aircraft), "", 0, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onCaptured(Actor actor, String s, int i)
    {
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(8, World.getTimeofDay(), s, "", i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onCaptured(Aircraft aircraft, int i)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
        {
            return;
        } else
        {
            type(8, World.getTimeofDay(), name(aircraft), "", i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onPilotWounded(Aircraft aircraft, int i)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
        {
            return;
        } else
        {
            type(9, World.getTimeofDay(), name(aircraft), "", i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onPilotHeavilyWounded(Aircraft aircraft, int i)
    {
        if(!Mission.isPlaying())
            return;
        if(aircraft.isNetMirror())
        {
            return;
        } else
        {
            type(10, World.getTimeofDay(), name(aircraft), "", i, (float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onParaKilled(Actor actor, String s, int i, Actor actor1)
    {
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
            return;
        type(12, World.getTimeofDay(), s, name(actor1), i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
        if(!Mission.isDogfight())
            new Action(6, s, 0, null, -1, i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y);
    }

    public static void onChuteKilled(Actor actor, String s, int i, Actor actor1)
    {
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(13, World.getTimeofDay(), s, name(actor1), i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onToggleSmoke(Actor actor, boolean flag)
    {
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(flag ? 14 : 15, World.getTimeofDay(), name(actor), "", 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onToggleLandingLight(Actor actor, boolean flag)
    {
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(flag ? 16 : 17, World.getTimeofDay(), name(actor), "", 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onWeaponsLoad(Actor actor, String s, int i)
    {
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(18, World.getTimeofDay(), name(actor), s, i, 0.0F, 0.0F, true);
            return;
        }
    }

    public static void onParaLanded(Actor actor, String s, int i)
    {
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(19, World.getTimeofDay(), s, "", i, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onDamagedGround(Actor actor)
    {
        if(!Actor.isValid(actor))
            return;
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(20, World.getTimeofDay(), name(actor), "", 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onDamaged(Actor actor, Actor actor1)
    {
        if(!Actor.isValid(actor))
            return;
        if(!Mission.isPlaying())
            return;
        if(actor.isNetMirror())
        {
            return;
        } else
        {
            type(21, World.getTimeofDay(), name(actor), name(actor1), 0, (float)actor.pos.getAbsPoint().x, (float)actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onDisconnected(String s)
    {
        if(!Mission.isPlaying())
        {
            return;
        } else
        {
            type(22, World.getTimeofDay(), s, "", 0, 0.0F, 0.0F, false);
            return;
        }
    }

    public static void onConnected(String s)
    {
        if(!Mission.isPlaying())
        {
            return;
        } else
        {
            type(23, World.getTimeofDay(), s, "", 0, 0.0F, 0.0F, false);
            return;
        }
    }

    public static void onRefly(String s)
    {
        if(!Mission.isPlaying())
        {
            return;
        } else
        {
            type(25, World.getTimeofDay(), s, "", 0, 0.0F, 0.0F, true);
            return;
        }
    }

    public static void onBaseSelect(String s, BornPlace bornplace)
    {
        if(!Mission.isPlaying())
        {
            return;
        } else
        {
            type(27, World.getTimeofDay(), s, "", bornplace.army, (float)bornplace.place.x, (float)bornplace.place.y, false);
            return;
        }
    }

    public static ArrayList actions = new ArrayList();
    public static Action lastFly = null;
    public static final int OCCUPIED = 0;
    public static final int TRYOCCUPIED = 1;
    public static final int CRASHED = 2;
    public static final int SHOTDOWN = 3;
    public static final int DESTROYED = 4;
    public static final int BAILEDOUT = 5;
    public static final int KILLED = 6;
    public static final int LANDED = 7;
    public static final int CAPTURED = 8;
    public static final int WOUNDED = 9;
    public static final int HEAVILYWOUNDED = 10;
    public static final int FLY = 11;
    public static final int PARAKILLED = 12;
    public static final int CHUTEKILLED = 13;
    public static final int SMOKEON = 14;
    public static final int SMOKEOFF = 15;
    public static final int LANDINGLIGHTON = 16;
    public static final int LANDINGLIGHTOFF = 17;
    public static final int WEAPONSLOAD = 18;
    public static final int PARALANDED = 19;
    public static final int DAMAGEDGROUND = 20;
    public static final int DAMAGED = 21;
    public static final int DISCONNECTED = 22;
    public static final int CONNECTED = 23;
    public static final int INFLIGHT = 24;
    public static final int REFLY = 25;
    public static final int REMOVED = 26;
    public static final int BASESELECT = 27;
    private static PrintStream file = null;
    private static boolean bBuffering = true;
    private static String fileName = null;
    private static boolean logKeep = false;
    private static DateFormat longDate = null;
    private static DateFormat shortDate = null;
    private static boolean bInited = false;

    // TODO: +++ Instant Log Enhanced by SAS~Storebror +++
    private static final String PIPE_URL = "\\\\.\\pipe\\SAS_PIPE_LOGGER";
    private static final String PIPE_FLUSH_TIMEOUT_1000 = "\u00001000"; // \u0000 = Separator, Flush Timeout = 1000ms
    private static final String PIPE_FLUSH_TIMEOUT_0 = "\u00000"; // \u0000 = Separator, Flush Timeout = 0ms
    // TODO: --- Instant Log Enhanced by SAS~Storebror ---
}
