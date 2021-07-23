package com.maddox.il2.game.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.NetUserLeft;
import com.maddox.il2.net.NetUserStat;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;

public class CmdUser extends Cmd {

    protected void INFO_HARD(String s) {
        if (this.bEventLog) EventLog.type(false, s);
        else super.INFO_HARD(s);
    }

    private boolean isDServer(NetUser netuser) {
        if (Main.cur().netServerParams == null) return false;
        if (!Main.cur().netServerParams.isDedicated()) return false;
        else return Main.cur().netServerParams.host() == netuser;
    }

    public Object exec(CmdEnv cmdenv, Map map) {
        if (Main.cur().netServerParams == null) return null;
        ArrayList arraylist = new ArrayList();
        this.fillUsers(map, arraylist, false);
        if (arraylist.size() == 0 && !exist(map, "STAT")) return null;
        this.bEventLog = exist(map, "EVENTLOG");
        if (exist(map, "STAT")) {
            this.fillUsers(map, arraylist, true);
            for (int i = 0; i < arraylist.size(); i++) {
                Object obj = arraylist.get(i);
                boolean flag = false;
                NetUserStat netuserstat;
                String s;
                if (obj instanceof NetUser) {
                    NetUser netuser1 = (NetUser) obj;
                    if (this.isDServer(netuser1)) continue;
                    netuserstat = netuser1.curstat();
                    s = netuser1.uniqueName();
                } else {
                    NetUserLeft netuserleft = (NetUserLeft) obj;
                    netuserstat = netuserleft.stat;
                    s = netuserleft.uniqueName;
                    flag = true;
                }
                this.INFO_HARD("-------------------------------------------------------");
                this.INFO_HARD("Name: \t" + s);
                this.INFO_HARD("Score: \t" + (flag ? 0 : (int) netuserstat.score));
                this.INFO_HARD("State: \t" + this.playerState(netuserstat, flag));
                this.INFO_HARD("Enemy Aircraft Kill: \t" + netuserstat.enemyKill[0]);
                this.INFO_HARD("Enemy Static Aircraft Kill: \t" + netuserstat.enemyKill[8]);
                this.INFO_HARD("Enemy Tank Kill: \t" + netuserstat.enemyKill[1]);
                this.INFO_HARD("Enemy Car Kill: \t" + netuserstat.enemyKill[2]);
                this.INFO_HARD("Enemy Artillery Kill: \t" + netuserstat.enemyKill[3]);
                this.INFO_HARD("Enemy AAA Kill: \t" + netuserstat.enemyKill[4]);
                this.INFO_HARD("Enemy Wagon Kill: \t" + netuserstat.enemyKill[6]);
                this.INFO_HARD("Enemy Ship Kill: \t" + netuserstat.enemyKill[7]);
                this.INFO_HARD("Enemy Radio Kill: \t" + netuserstat.enemyKill[9]);
                this.INFO_HARD("Friend Aircraft Kill: \t" + netuserstat.friendKill[0]);
                this.INFO_HARD("Friend Static Aircraft Kill: \t" + netuserstat.friendKill[8]);
                this.INFO_HARD("Friend Tank Kill: \t" + netuserstat.friendKill[1]);
                this.INFO_HARD("Friend Car Kill: \t" + netuserstat.friendKill[2]);
                this.INFO_HARD("Friend Artillery Kill: \t" + netuserstat.friendKill[3]);
                this.INFO_HARD("Friend AAA Kill: \t" + netuserstat.friendKill[4]);
                this.INFO_HARD("Friend Wagon Kill: \t" + netuserstat.friendKill[6]);
                this.INFO_HARD("Friend Ship Kill: \t" + netuserstat.friendKill[7]);
                this.INFO_HARD("Friend Radio Kill: \t" + netuserstat.friendKill[9]);
                this.INFO_HARD("Fire Bullets: \t\t" + netuserstat.bulletsFire);
                // TODO: Cheater Protection
                if (AircraftState.isCheater(s)) {
                    this.INFO_HARD("Hit Bullets: \t\t" + netuserstat.bulletsHit * AircraftState.getCheaterHitRate(s) / 100);
                    this.INFO_HARD("Hit Air Bullets: \t" + netuserstat.bulletsHitAir * AircraftState.getCheaterHitRate(s) / 100);
                } else {
                    this.INFO_HARD("Hit Bullets: \t\t" + netuserstat.bulletsHit);
                    this.INFO_HARD("Hit Air Bullets: \t" + netuserstat.bulletsHitAir);
                }
                // ---
                this.INFO_HARD("Fire Roskets: \t\t" + netuserstat.rocketsFire);
                this.INFO_HARD("Hit Roskets: \t\t" + netuserstat.rocketsHit);
                this.INFO_HARD("Fire Bombs: \t\t" + netuserstat.bombFire);
                this.INFO_HARD("Hit Bombs: \t\t" + netuserstat.bombHit);
                // TODO: Cheater Protection
                if (AircraftState.isCheater(s)) {
                    System.out.println("Got hit by Bullets: \t" + netuserstat.gotHitBullets);
                    System.out.println("Got hit by Bullets Mass: \t" + netuserstat.gotHitMassa);
                    System.out.println("Got hit by Bullets Power: \t" + netuserstat.gotHitPower);
                    System.out.println("Got hit by Bullets Cumulative Power: \t" + netuserstat.gotHitCumulativePower);
                    netuserstat.gotHitBullets = 0L;
                    netuserstat.gotHitMassa = netuserstat.gotHitPower = netuserstat.gotHitCumulativePower = 0F;
                }
                // ---
            }

            this.INFO_HARD("-------------------------------------------------------");
        } else {
            this.INFO_HARD(" N       Name           Ping    Score   Army        Aircraft");
            for (int j = 0; j < arraylist.size(); j++) {
                StringBuffer stringbuffer = new StringBuffer();
                NetUser netuser = (NetUser) arraylist.get(j);
                if (this.isDServer(netuser)) continue;
                int k = NetEnv.hosts().indexOf(netuser);
                stringbuffer.append(" ");
                if (k >= 0) stringbuffer.append(k + 1);
                else stringbuffer.append("0");
                for (; stringbuffer.length() < 8; stringbuffer.append(" "))
                    ;
                stringbuffer.append(netuser.uniqueName());
                for (; stringbuffer.length() < 24; stringbuffer.append(" "))
                    ;
                stringbuffer.append(" ");
                stringbuffer.append(netuser.ping);
                for (; stringbuffer.length() < 32; stringbuffer.append(" "))
                    ;
                stringbuffer.append(" ");
                stringbuffer.append((int) netuser.curstat().score);
                for (; stringbuffer.length() < 40; stringbuffer.append(" "))
                    ;
                stringbuffer.append("(");
                stringbuffer.append(netuser.getArmy());
                stringbuffer.append(")");
                stringbuffer.append(Army.name(netuser.getArmy()));
                for (; stringbuffer.length() < 52; stringbuffer.append(" "))
                    ;
                Aircraft aircraft = netuser.findAircraft();
                if (Actor.isValid(aircraft)) {
                    stringbuffer.append(aircraft.typedName());
                    stringbuffer.append(" ");
                    for (; stringbuffer.length() < 64; stringbuffer.append(" "))
                        ;
                    stringbuffer.append(Property.stringValue(aircraft.getClass(), "keyName"));
                }
                this.INFO_HARD(stringbuffer.toString());
            }

        }
        return CmdEnv.RETURN_OK;
    }

    private String playerState(NetUserStat netuserstat, boolean flag) {
        if (flag) return "Left the Game";
        if ((netuserstat.curPlayerState & 1) != 0) return "KIA";
        if ((netuserstat.curPlayerState & 0x10) != 0) return "Captured";
        if ((netuserstat.curPlayerState & 8) != 0) return "Emergency Landed";
        if ((netuserstat.curPlayerState & 4) != 0) return "Landed at Airfield";
        if ((netuserstat.curPlayerState & 2) != 0) return "Hit the Silk";
        if ((netuserstat.curPlayerState & 0x20) != 0) return "Selects Aircraft";
        else return "In Flight";
    }

    private void fillUsers(Map map, List list, boolean flag) {
        boolean flag1 = false;
        if (!map.containsKey("_$$") && !map.containsKey("#") && !map.containsKey("ARMY")) flag1 = true;
        if (map.containsKey("_$$") && nargs(map, "_$$") == 1 && "*".equals(arg(map, "_$$", 0))) flag1 = true;
        if (flag1) {
            if (flag) list.addAll(NetUserLeft.all);
            else {
                list.add(NetEnv.host());
                for (int i = 0; i < NetEnv.hosts().size(); i++)
                    list.add(NetEnv.hosts().get(i));

            }
            return;
        }
        ArrayList arraylist = new ArrayList();
        HashMap hashmap = new HashMap();
        if (map.containsKey("_$$")) {
            int j = nargs(map, "_$$");
            label0: for (int i1 = 0; i1 < j; i1++) {
                String s = arg(map, "_$$", i1);
                if (flag) {
                    int i2 = 0;
                    do {
                        if (i2 >= NetUserLeft.all.size()) continue label0;
                        NetUserLeft netuserleft = (NetUserLeft) NetUserLeft.all.get(i2);
                        if (s.equals(netuserleft.uniqueName) && !hashmap.containsKey(netuserleft)) {
                            hashmap.put(netuserleft, null);
                            arraylist.add(netuserleft);
                        }
                        i2++;
                    } while (true);
                }
                NetUser netuser = (NetUser) NetEnv.host();
                if (s.equals(netuser.uniqueName())) {
                    if (!hashmap.containsKey(netuser)) {
                        hashmap.put(netuser, null);
                        arraylist.add(netuser);
                    }
                    continue;
                }
                int k2 = 0;
                do {
                    if (k2 >= NetEnv.hosts().size()) continue label0;
                    NetUser netuser1 = (NetUser) NetEnv.hosts().get(k2);
                    String s3 = netuser1.uniqueName();
                    if (s.equals(s3)) {
                        if (!hashmap.containsKey(netuser1)) {
                            hashmap.put(netuser1, null);
                            arraylist.add(netuser1);
                        }
                        continue label0;
                    }
                    k2++;
                } while (true);
            }

        }
        if (map.containsKey("#")) {
            int k = nargs(map, "#");
            for (int j1 = 0; j1 < k; j1++) {
                String s1 = arg(map, "#", j1);
                if (s1.charAt(0) < '0' || s1.charAt(0) > '9') continue;
                int j2 = arg(map, "#", j1, 1000, 0, 1000);
                if (flag) {
                    int l2 = j2 - 1 - NetEnv.hosts().size();
                    if (l2 < 0 || l2 >= NetUserLeft.all.size()) continue;
                    NetUserLeft netuserleft1 = (NetUserLeft) NetUserLeft.all.get(l2);
                    if (!hashmap.containsKey(netuserleft1)) {
                        hashmap.put(netuserleft1, null);
                        arraylist.add(netuserleft1);
                    }
                    continue;
                }
                if (j2 > 0 && j2 <= NetEnv.hosts().size()) {
                    NetUser netuser2 = (NetUser) NetEnv.hosts().get(j2 - 1);
                    if (!hashmap.containsKey(netuser2)) {
                        hashmap.put(netuser2, null);
                        arraylist.add(netuser2);
                    }
                    continue;
                }
                if (j2 != 0) continue;
                NetUser netuser3 = (NetUser) NetEnv.host();
                if (!hashmap.containsKey(netuser3)) {
                    hashmap.put(netuser3, null);
                    arraylist.add(netuser3);
                }
            }

        }
        if (map.containsKey("ARMY")) {
            int l = nargs(map, "ARMY");
            label1: for (int k1 = 0; k1 < l; k1++) {
                int l1 = -1;
                String s2 = arg(map, "ARMY", k1);
                if (s2.charAt(0) >= '0' && s2.charAt(0) <= '9') {
                    l1 = arg(map, "ARMY", k1, 1000, 0, 1000);
                    if (l1 >= Army.amountNet()) continue;
                } else {
                    for (l1 = 0; l1 < Army.amountNet() && !Army.name(l1).equals(s2); l1++)
                        ;
                    if (l1 == Army.amountNet()) continue;
                }
                if (flag) {
                    int i3 = 0;
                    do {
                        if (i3 >= NetUserLeft.all.size()) continue label1;
                        NetUserLeft netuserleft2 = (NetUserLeft) NetUserLeft.all.get(i3);
                        if (l1 == netuserleft2.army && !hashmap.containsKey(netuserleft2)) {
                            hashmap.put(netuserleft2, null);
                            arraylist.add(netuserleft2);
                        }
                        i3++;
                    } while (true);
                }
                NetUser netuser4 = (NetUser) NetEnv.host();
                if (l1 == netuser4.getArmy()) {
                    if (!hashmap.containsKey(netuser4)) {
                        hashmap.put(netuser4, null);
                        arraylist.add(netuser4);
                    }
                    continue;
                }
                int j3 = 0;
                do {
                    if (j3 >= NetEnv.hosts().size()) continue label1;
                    NetUser netuser5 = (NetUser) NetEnv.hosts().get(j3);
                    if (l1 == netuser5.getArmy()) {
                        if (!hashmap.containsKey(netuser5)) {
                            hashmap.put(netuser5, null);
                            arraylist.add(netuser5);
                        }
                        continue label1;
                    }
                    j3++;
                } while (true);
            }

        }
        list.addAll(arraylist);
    }

    public CmdUser() {
        this.bEventLog = false;
        this.param.put("#", null);
        this.param.put("ARMY", null);
        this.param.put("STAT", null);
        this.param.put("EVENTLOG", null);
        this._properties.put("NAME", "user");
        this._levelAccess = 1;
    }

    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_USER", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println(theMessage);
    }

    // private static final boolean DEBUG = false;
    public static final String NN       = "#";
    public static final String ARMY     = "ARMY";
    public static final String STAT     = "STAT";
    public static final String EVENTLOG = "EVENTLOG";
    private boolean            bEventLog;
}
