package com.maddox.il2.game.campaign;

import com.maddox.il2.ai.*;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Statics;
import com.maddox.rts.*;
import com.maddox.util.SharedTokenizer;
import java.io.PrintStream;
import java.util.StringTokenizer;

public abstract class Campaign
{
    public int originalRank()
    {
        return _originalRank;
    }

    public String originalFlightName()
    {
        return _originalFlightName;
    }

    public int originalPlayerNum()
    {
        return _originalPlayerNum;
    }

    public String originalPlaneName()
    {
        return _originalPlaneName;
    }

    public Campaign()
    {
        amountMissions = 0;
    }

    public int army()
    {
        return 0;
    }

    public String branch()
    {
        return _country;
    }

    public String country()
    {
        return Regiment.getCountryFromBranch(_country);
    }

    public String missionsDir()
    {
        return _missionsDir;
    }

    public long difficulty()
    {
        return _difficulty;
    }

    public int completeMissions()
    {
        return _completeMissions;
    }

    public int score()
    {
        return _score;
    }

    public int scoreRank()
    {
        return _scoreRank;
    }

    public int rank()
    {
        return _rank;
    }

    public int aircraftLost()
    {
        return _aircraftLost;
    }

    public float enemyAirDestroyed()
    {
        return _enemyAirDestroyed;
    }

    public float[] enemyAirDestroyedPerCountry()
    {
        if(_enemyAirDestroyedPerCountry == null)
            return new float[0];
        float af[] = new float[19];
        int i = 0;
        for(StringTokenizer stringtokenizer = new StringTokenizer(_enemyAirDestroyedPerCountry, "/"); stringtokenizer.hasMoreTokens();)
        {
            String s = stringtokenizer.nextToken();
            try
            {
                af[i] = Float.parseFloat(s);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            i++;
        }

        return af;
    }

    public int[] enemyGroundDestroyed()
    {
        return _enemyGroundDestroyed;
    }

    public int friendDestroyed()
    {
        return _friendDestroyed;
    }

    public String lastMissionMapName()
    {
        return _lastMissionMapName;
    }

    public void setLastMissionMapName(String s)
    {
        _lastMissionMapName = s;
    }

    public String epilogueTrack()
    {
        return _epilogueTrack;
    }

    public boolean isComplete()
    {
        if(amountMissions == 0)
        {
            SectFile sectfile = missionsSectFile();
            int i = sectfile.sectionIndex("list");
            if(i >= 0)
            {
                int j = sectfile.vars(i);
                for(int k = 0; k < j; k++)
                {
                    String s = sectfile.var(i, k);
                    if(!lineIsIntroName(s))
                        amountMissions++;
                }

            }
        }
        return amountMissions == completeMissions();
    }

    public int awards(int i)
    {
        int j = i;
        if(j < _scoreAward)
            j = _scoreAward;
        return _awards.count(j);
    }

    public int[] getAwards(int i)
    {
        int j = i;
        if(j < _scoreAward)
            j = _scoreAward;
        return _awards.index(j);
    }

    public void incAircraftLost()
    {
        _aircraftLost++;
    }

    protected int rankStep()
    {
        return 1200;
    }

    public int newRank(int i)
    {
        if(i < _scoreRank + rankStep())
            return _rank;
        int j = _rank + (i - _scoreRank) / rankStep();
        if(j >= 6)
        {
            j = 6;
            newScoreRank = i;
        } else
        {
            newScoreRank = (j - _rank) * rankStep() + _scoreRank;
        }
        return j;
    }

    public void currentMissionComplete(int i, float f, int ai[], int j, float af[])
    {
        _completeMissions++;
        _score = i;
        _enemyAirDestroyed = f;
        _enemyGroundDestroyed = ai;
        _friendDestroyed = j;
        int k = newRank(i);
        if(k > _rank)
        {
            _rank = k;
            _scoreRank = newScoreRank;
        }
        if(i > _scoreAward)
            _scoreAward = i;
        String s = "";
        for(int l = 0; l < af.length; l++)
            if(l < af.length - 1)
                s = s + af[l] + "/";
            else
                s = s + af[l];

        _enemyAirDestroyedPerCountry = s;
    }

    public void clearSavedStatics(SectFile sectfile)
    {
        String s = branch() + missionsDir();
        int i = sectfile.sectionIndex(s + "Bridge");
        if(i >= 0)
            sectfile.sectionRemove(i);
        i = sectfile.sectionIndex(s + "House");
        if(i >= 0)
            sectfile.sectionRemove(i);
    }

    public void saveStatics(SectFile sectfile)
    {
        String s = branch() + missionsDir();
        clearSavedStatics(sectfile);
        if(Mission.cur() == null)
            return;
        if(Mission.cur().sectFile() == null)
        {
            return;
        } else
        {
            int i = sectfile.sectionAdd(s + "Bridge");
            World.cur().statics.saveStateBridges(sectfile, i);
            i = sectfile.sectionAdd(s + "House");
            World.cur().statics.saveStateHouses(sectfile, i);
            String s1 = Mission.cur().sectFile().get("MAIN", "MAP", (String)null);
            setLastMissionMapName(s1);
            return;
        }
    }

    public void copySavedStatics(SectFile sectfile, SectFile sectfile1)
    {
        String s = branch() + missionsDir();
        copySection(sectfile, s + "Bridge", sectfile1, "AddBridge");
        copySection(sectfile, s + "House", sectfile1, "AddHouse");
    }

    private void copySection(SectFile sectfile, String s, SectFile sectfile1, String s1)
    {
        int i = sectfile1.sectionIndex(s1);
        if(i >= 0)
            sectfile1.sectionRemove(i);
        int j = sectfile.sectionIndex(s);
        if(j < 0)
            return;
        i = sectfile1.sectionAdd(s1);
        int k = sectfile.vars(j);
        for(int l = 0; l < k; l++)
            sectfile1.lineAdd(i, sectfile.line(j, l));

    }

    private void copySavedStatics(SectFile sectfile)
    {
        String s = sectfile.get("MAIN", "MAP", (String)null);
        if(s == null)
            return;
        if(!s.equals(lastMissionMapName()))
        {
            return;
        } else
        {
            String s1 = "users/" + World.cur().userCfg.sId + "/campaigns.ini";
            SectFile sectfile1 = new SectFile(s1, 0, false, World.cur().userCfg.krypto());
            copySavedStatics(sectfile1, sectfile);
            return;
        }
    }

    public SectFile missionsSectFile()
    {
        if(missionsSectFile != null)
        {
            return missionsSectFile;
        } else
        {
            missionsSectFile = new SectFile("missions/campaign/" + branch() + "/" + missionsDir() + "/campaign.ini", 0);
            return missionsSectFile;
        }
    }

    public boolean isDGen()
    {
        SectFile sectfile = missionsSectFile();
        String s = sectfile.get("Main", "ExecGenerator", (String)null);
        if(s == null)
            return false;
        else
            return "dgen.exe".equalsIgnoreCase(s);
    }

    public void doExternalGenerator()
    {
        SectFile sectfile = missionsSectFile();
        String s = sectfile.get("Main", "ExecGenerator", (String)null);
        if(s == null)
            return;
        amountMissions = 0;
        missionsSectFile = null;
        try
        {
            RTSConf.cur.console.flush();
            EventLog.close();
        }
        catch(Throwable throwable)
        {
            System.out.println(throwable.getMessage());
            throwable.printStackTrace();
        }
        try
        {
            String s1 = "users/" + World.cur().userCfg.sId + "/";
            String s2 = "missions/campaign/" + branch() + "/" + missionsDir() + "/";
            String s3 = s1 + " " + s2 + " " + completeMissions() + " " + difficulty() + " " + score() + " " + rank();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(s + " " + s3);
            process.waitFor();
        }
        catch(Throwable throwable1)
        {
            System.out.println(throwable1.getMessage());
            throwable1.printStackTrace();
        }
    }

    public String nextIntro()
    {
        SectFile sectfile = missionsSectFile();
        int i = sectfile.sectionIndex("list");
        if(i >= 0)
        {
            int j = sectfile.vars(i);
            int k = 0;
            for(int l = 0; l < j; l++)
            {
                String s = sectfile.var(i, l);
                if(lineIsIntroName(s))
                {
                    if(k == _completeMissions)
                    {
                        SharedTokenizer.set(sectfile.line(i, l));
                        SharedTokenizer.nextToken();
                        return SharedTokenizer.nextToken();
                    }
                } else
                {
                    k++;
                }
            }

        }
        return null;
    }

    public SectFile nextMission()
    {
        if(isComplete())
            return null;
        SectFile sectfile = missionsSectFile();
        int i = sectfile.sectionIndex("list");
        String s = null;
        int j = sectfile.vars(i);
        int k = 0;
        for(int l = 0; l < j; l++)
        {
            String s1 = sectfile.var(i, l);
            if(lineIsIntroName(s1))
                continue;
            if(k == _completeMissions)
            {
                s = sectfile.line(i, l);
                break;
            }
            k++;
        }

        SharedTokenizer.set(s);
        j = SharedTokenizer.countTokens();
        int i1 = (int)Math.round((double)(j - 1) * Math.random());
        String s2;
        for(s2 = SharedTokenizer.nextToken(); i1-- > 0; s2 = SharedTokenizer.nextToken());
        String s3 = "missions/campaign/" + branch() + "/" + missionsDir() + "/" + s2;
        SectFile sectfile1 = new SectFile(s3, 0);
        if(!isDGen() && !prepareMission(sectfile1))
        {
            return null;
        } else
        {
            copySavedStatics(sectfile1);
            return sectfile1;
        }
    }

    private boolean lineIsIntroName(String s)
    {
        if(s == null)
            return false;
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if(c == '*')
                return true;
            if(c > ' ')
                return false;
        }

        return false;
    }

    protected boolean prepareMission(SectFile sectfile)
    {
        String s1;
        int l;
        int i1;
label0:
        {
            _originalRank = _rank;
            _originalFlightName = sectfile.get("Main", "player", (String)null);
            _originalPlayerNum = sectfile.get("Main", "playerNum", 0, 0, 3);
            _originalPlaneName = sectfile.get(_originalFlightName, "Class", (String)null);
            String s = sectfile.get("MAIN", "player", (String)null);
            s1 = s.substring(0, s.length() - 1);
            int ai[] = {
                0, 0, 0, 0
            };
            int i = sectfile.sectionIndex("Wing");
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                String s2 = sectfile.var(i, k);
                if(s2.startsWith(s1))
                {
                    int j1 = s2.charAt(s2.length() - 1) - 48;
                    int k3 = sectfile.get(s2, "Planes", 0, 0, 4);
                    ai[j1] = k3;
                }
            }

            l = -1;
            i1 = 0;
label1:
            switch(_rank)
            {
            default:
                break;

            case 6: // '\006'
                int k1 = 0;
                do
                {
                    if(k1 >= 4)
                        break;
                    if(ai[k1] != 0)
                    {
                        l = k1;
                        i1 = 0;
                        break;
                    }
                    k1++;
                } while(true);
                break;

            case 5: // '\005'
                int l1 = 0;
                for(int l3 = 0; l3 < 4; l3++)
                {
                    if(ai[l3] == 0)
                        continue;
                    if(l1 == 0)
                    {
                        l1++;
                        continue;
                    }
                    l = l3;
                    i1 = 0;
                    break;
                }

                if(l >= 0)
                    break;
                // fall through

            case 4: // '\004'
                int i2 = 3;
                do
                {
                    if(i2 < 0)
                        break label1;
                    if(ai[i2] != 0)
                    {
                        l = i2;
                        i1 = 0;
                        break label1;
                    }
                    i2--;
                } while(true);

            case 3: // '\003'
                int j2 = 0;
                do
                {
                    if(j2 >= 4)
                        break;
                    if(ai[j2] > 1)
                    {
                        l = j2;
                        i1 = 1;
                        break;
                    }
                    j2++;
                } while(true);
                if(l >= 0)
                    break;
                // fall through

            case 2: // '\002'
                int k2 = 3;
                do
                {
                    if(k2 < 0)
                        break;
                    if(ai[k2] > 1)
                    {
                        l = k2;
                        i1 = 1;
                        break;
                    }
                    k2--;
                } while(true);
                if(l >= 0)
                    break;
                k2 = 3;
                do
                {
                    if(k2 < 0)
                        break label1;
                    if(ai[k2] != 0)
                    {
                        l = k2;
                        i1 = 0;
                        break label1;
                    }
                    k2--;
                } while(true);

            case 1: // '\001'
                int l2 = 0;
                do
                {
                    if(l2 >= 4)
                        break;
                    if(ai[l2] > 2)
                    {
                        l = l2;
                        i1 = ai[l2] - 1;
                        break;
                    }
                    l2++;
                } while(true);
                if(l >= 0)
                    break;
                // fall through

            case 0: // '\0'
                int i3 = 3;
                do
                {
                    if(i3 < 0)
                        break label1;
                    if(ai[i3] != 0)
                    {
                        l = i3;
                        i1 = ai[i3] - 1;
                        break label1;
                    }
                    i3--;
                } while(true);
            }
            for(int j3 = 0; j3 < 4; j3++)
                if(ai[j3] > 0)
                {
                    String s3 = sectfile.get(s1 + j3, "Class", (String)null);
                    try
                    {
                        Class class1 = ObjIO.classForName(s3);
                        if(!Property.containsValue(class1, "cockpitClass"))
                            ai[j3] = 0;
                    }
                    catch(Exception exception)
                    {
                        ai[j3] = 0;
                    }
                }

            if(ai[l] > 0)
                break label0;
            l++;
            i1 = -1;
            do
            {
                if(l >= 4)
                    break;
                if(ai[l] > 0)
                {
                    i1 = 0;
                    break;
                }
                l++;
            } while(true);
            if(i1 >= 0)
                break label0;
            do
            {
                if(l <= 0)
                    break label0;
                l--;
            } while(ai[l] <= 0);
            i1 = ai[l] - 1;
        }
        if(i1 >= 0)
        {
            sectfile.set("Main", "player", s1 + l);
            sectfile.set("Main", "playerNum", i1);
            return true;
        } else
        {
            return false;
        }
    }

    public void init(Awards awards1, String s, String s1, long l, int i)
    {
        _awards = awards1;
        _nawards = -1;
        _country = s;
        _missionsDir = s1;
        _difficulty = l;
        _rank = i;
    }

    private static Class _cls;
    public Awards _awards;
    public int _nawards;
    public String _country;
    public String _missionsDir;
    public long _difficulty;
    public int _completeMissions;
    public int _score;
    public int _scoreAward;
    public int _scoreRank;
    public int _rank;
    public int _aircraftLost;
    public float _enemyAirDestroyed;
    public String _enemyAirDestroyedPerCountry;
    public int _enemyGroundDestroyed[];
    public int _friendDestroyed;
    public String _lastMissionMapName;
    public String _epilogueTrack;
    private int amountMissions;
    private int newScoreRank;
    private SectFile missionsSectFile;
    private int _originalRank;
    private String _originalFlightName;
    private String _originalPlaneName;
    private int _originalPlayerNum;

    static 
    {
        _cls = com.maddox.il2.game.campaign.Campaign.class;
        ObjIO.field(_cls, "_awards");
        ObjIO.field(_cls, "_nawards");
        ObjIO.field(_cls, "_country");
        ObjIO.field(_cls, "_missionsDir");
        ObjIO.field(_cls, "_difficulty");
        ObjIO.field(_cls, "_completeMissions");
        ObjIO.field(_cls, "_score");
        ObjIO.field(_cls, "_scoreAward");
        ObjIO.field(_cls, "_scoreRank");
        ObjIO.field(_cls, "_rank");
        ObjIO.field(_cls, "_aircraftLost");
        ObjIO.field(_cls, "_enemyAirDestroyed");
        ObjIO.field(_cls, "_enemyAirDestroyedPerCountry");
        ObjIO.field(_cls, "_enemyGroundDestroyed");
        ObjIO.field(_cls, "_friendDestroyed");
        ObjIO.field(_cls, "_lastMissionMapName");
        ObjIO.field(_cls, "_epilogueTrack");
    }
}
