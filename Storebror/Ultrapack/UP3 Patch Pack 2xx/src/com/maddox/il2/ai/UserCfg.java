/*4.10.1 class*/
package com.maddox.il2.ai;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.campaign.Campaign;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.IniFile;
import com.maddox.rts.ObjIO;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.UnicodeTo8bit;

public class UserCfg
{
	public static final String defName;
	public static final String defCallsign;
	public static final String defSurname;
	public static final String nameHotKeyEnvs[] = {"pilot", "gunner", "aircraftView", "SnapView", "PanView", "orders", "misc", "$$$misc", "timeCompression", "move"};
	public String sId;
	private int krypto[];
	public String name;
	public String callsign;
	public String surname;
	public int singleDifficulty;
	public int netDifficulty;
	public String placeBirth;
	public int yearBirth;
	public String netRegiment;
	public String netAirName;
	public String netPilot;
	public int netSquadron;
	public int netTacticalNumber;
	public boolean netNumberOn;
	public float coverMashineGun;
	public float coverCannon;
	public float coverRocket;
	public float rocketDelay;
	public float bombDelay;
	// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
    public float bombFuze;
	// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
    // TODO: Storebror: +++ Bomb/Rocket Fuze/Delay Replication
    public static final float BOMB_FUZE_MIN = 0F;
    public static final float BOMB_FUZE_MAX = 10F;
    public static final float BOMB_DELAY_MIN = 0F;
    public static final float BOMB_DELAY_MAX = 10F;
    public static final float ROCKET_DELAY_MIN = 1F;
    public static final float ROCKET_DELAY_MAX = 60F;
    // TODO: Storebror: --- Bomb/Rocket Fuze/Delay Replication
	public float fuel;
	
	//TODO: Added by |ZUTI|
	//------------------------------------
	public boolean bZutiMultiCrew;
	public boolean bZutiMultiCrewAnytime;
	//------------------------------------
	
	private HashMapExt skinMap;
	private HashMapExt weaponMap;
	private HashMapExt noseartMap;
	
	public String iniFileName()
	{
		return "users/" + sId + "/settings.ini";
	}
	
	public String getSkin(String s)
	{
		return (String)skinMap.get(s);
	}
	
	public String getWeapon(String s)
	{
		return (String)weaponMap.get(s);
	}
	
	public String getNoseart(String s)
	{
		return (String)noseartMap.get(s);
	}
	
	public void setSkin(String s, String s1)
	{
		skinMap.put(s, s1);
	}
	
	public void setWeapon(String s, String s1)
	{
		weaponMap.put(s, s1);
	}
	
	public void setNoseart(String s, String s1)
	{
		noseartMap.put(s, s1);
	}
	
	private void loadMap(IniFile inifile, HashMapExt hashmapext, String s)
	{
		hashmapext.clear();
		String as[] = inifile.getVariables(s);
		if (as == null || as.length == 0)
			return;
		for (int i = 0; i < as.length; i++)
		{
			String s1 = inifile.get(s, as[i], (String)null);
			if (s1 != null)
				hashmapext.put(as[i], s1);
		}
		
	}
	
	private void saveMap(IniFile inifile, HashMapExt hashmapext, String s)
	{
		inifile.deleteSubject(s);
		if (hashmapext.size() == 0)
			return;
		for (Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
			if (entry.getValue() != null)
				inifile.set(s, (String)entry.getKey(), (String)entry.getValue());
		
	}
	
	public void loadConf()
	{
		IniFile inifile = new IniFile(iniFileName(), 0);
		for (int i = 0; i < nameHotKeyEnvs.length; i++)
		{
			HotKeyEnv.setCurrentEnv(nameHotKeyEnvs[i]);
			HotKeyEnv.currentEnv().all().clear();
			HotKeyEnv.fromIni(nameHotKeyEnvs[i], inifile, "HotKey " + nameHotKeyEnvs[i]);
		}
		
		singleDifficulty = inifile.get("difficulty", "single", 0);
		netDifficulty = inifile.get("difficulty", "net", 0);
		netRegiment = inifile.get("net", "regiment", (String)null);
		netAirName = inifile.get("net", "airclass", (String)null);
		netPilot = inifile.get("net", "pilot", (String)null);
		netSquadron = inifile.get("net", "squadron", 0, 0, 3);
		netTacticalNumber = inifile.get("net", "tacticalnumber", 1, 1, 99);
		netNumberOn = inifile.get("net", "numberOn", 1, 0, 1) == 1;
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		bZutiMultiCrew = inifile.get("net", "zutiMultiCrew", 0, 0, 1) == 1;
		bZutiMultiCrewAnytime = inifile.get("net", "zutiMultiCrewAnytime", 0, 0, 1) == 1;
		//------------------------------------
		
		coverMashineGun = inifile.get("cover", "mashinegun", 500F, 100F, 1000F);
		coverCannon = inifile.get("cover", "cannon", 500F, 100F, 1000F);
		coverRocket = inifile.get("cover", "rocket", 500F, 100F, 1000F);
		rocketDelay = inifile.get("cover", "rocketdelay", 10F, 1.0F, 60F);
		bombDelay = inifile.get("cover", "bombdelay", 0.0F, 0.0F, 10F);
		// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
        bombFuze = inifile.get("cover", "bombfuze", 0.0F, 0.0F, 10F);
		// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
		fuel = inifile.get("cover", "fuel", 100F, 0.0F, 100F);
		loadMap(inifile, skinMap, "skin");
		loadMap(inifile, weaponMap, "weapon");
		loadMap(inifile, noseartMap, "noseart");
		placeBirth = inifile.get("dgen", "placeBirth", (String)null);
		yearBirth = inifile.get("dgen", "yearBirth", 1910, 1850, 2050);
		redirectKeysPause();
		if (Config.cur.newCloudsRender)
		{
			singleDifficulty |= 0x1000000;
			netDifficulty |= 0x1000000;
		}
		else
		{
			singleDifficulty &= 0xfeffffff;
			netDifficulty &= 0xfeffffff;
		}
	}
	
	public void saveConf()
	{
		IniFile inifile = new IniFile(iniFileName(), 1);
		for (int i = 0; i < nameHotKeyEnvs.length; i++)
		{
			inifile.deleteSubject("HotKey " + nameHotKeyEnvs[i]);
			HotKeyEnv.toIni(nameHotKeyEnvs[i], inifile, "HotKey " + nameHotKeyEnvs[i]);
		}
		
		inifile.deleteSubject("difficulty");
		inifile.set("difficulty", "single", singleDifficulty);
		inifile.set("difficulty", "net", netDifficulty);
		inifile.deleteSubject("net");
		if (netRegiment != null)
			inifile.set("net", "regiment", netRegiment);
		if (netAirName != null)
			inifile.set("net", "airclass", netAirName);
		if (netPilot != null)
			inifile.set("net", "pilot", netPilot);
		inifile.set("net", "squadron", netSquadron);
		inifile.set("net", "tacticalnumber", netTacticalNumber);
		inifile.set("net", "numberOn", netNumberOn ? "1" : "0");
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		inifile.set("net", "zutiMultiCrew", bZutiMultiCrew ? "1" : "0");
		inifile.set("net", "zutiMultiCrewAnytime", bZutiMultiCrewAnytime ? "1" : "0");
		//------------------------------------
		
		inifile.deleteSubject("cover");
		inifile.set("cover", "mashinegun", coverMashineGun);
		inifile.set("cover", "cannon", coverCannon);
		inifile.set("cover", "rocket", coverRocket);
		inifile.set("cover", "rocketdelay", rocketDelay);
		inifile.set("cover", "bombdelay", bombDelay);
		// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
        inifile.set("cover", "bombfuze", bombFuze);
		// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
		inifile.set("cover", "fuel", fuel);
		saveMap(inifile, skinMap, "skin");
		saveMap(inifile, weaponMap, "weapon");
		saveMap(inifile, noseartMap, "noseart");
		if (placeBirth != null)
			inifile.set("dgen", "placeBirth", placeBirth);
		inifile.set("dgen", "yearBirth", yearBirth);
		inifile.saveFile();
		redirectKeysPause();
	}
	
	private void redirectKeysPause()
	{
		ArrayList arraylist = new ArrayList();
		HotKeyEnv hotkeyenv = HotKeyEnv.env("hotkeys");
		HashMapInt hashmapint = hotkeyenv.all();
		for (HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint.nextEntry(hashmapintentry))
		{
			int i = hashmapintentry.getKey();
			String s = (String)hashmapintentry.getValue();
			if ("pause".equals(s))
				arraylist.add(new Integer(i));
		}
		
		for (int j = 0; j < arraylist.size(); j++)
		{
			Integer integer = (Integer)arraylist.get(j);
			hashmapint.remove(integer.intValue());
		}
		
		arraylist.clear();
		hotkeyenv = HotKeyEnv.env("timeCompression");
		hashmapint = hotkeyenv.all();
		for (HashMapIntEntry hashmapintentry1 = hashmapint.nextEntry(null); hashmapintentry1 != null; hashmapintentry1 = hashmapint.nextEntry(hashmapintentry1))
		{
			int k = hashmapintentry1.getKey();
			String s1 = (String)hashmapintentry1.getValue();
			if ("timeSpeedPause".equals(s1))
				arraylist.add(new Integer(k));
		}
		
		for (int l = 0; l < arraylist.size(); l++)
		{
			Integer integer1 = (Integer)arraylist.get(l);
			int i1 = integer1.intValue();
			HotKeyEnv.env("hotkeys").all().put(i1, "pause");
		}
		
	}
	
	public int[] krypto()
	{
		if (krypto == null)
		{
			long l = Finger.Long("users/" + sId);
			krypto = new int[17];
			for (int i = 0; i < 17; i++)
			{
				int j = (int)(l >> 8 * (i % 8) & 255L);
				for (int k = i / 8; k > 0; k--)
				{
					j <<= 2;
					j = (j & 3 | j) & 0xff;
				}
				
				if (j == 0)
					j = 255;
				krypto[i] = j;
			}
			
		}
		return krypto;
	}
	
	public boolean existUserDir()
	{
		return existUserDir(sId);
	}
	
	private boolean existUserDir(String s)
	{
		File file = new File(HomePath.toFileSystemName("users/" + s, 0));
		return file.isDirectory();
	}
	
	public boolean existUserConf()
	{
		File file = new File(HomePath.toFileSystemName("users/" + sId + "/settings.ini", 0));
		return file.exists();
	}
	
	private void removeDGens()
	{
		String s = "users/" + sId + "/campaigns.ini";
		SectFile sectfile = new SectFile(s, 0, false, krypto());
		int i = sectfile.sectionIndex("list");
		if (i < 0)
			return;
		int j = sectfile.vars(i);
		for (int k = 0; k < j; k++)
			try
			{
				Campaign campaign = (Campaign)ObjIO.fromString(sectfile.value(i, k));
				if (campaign.isDGen())
				{
					String s1 = "missions/campaign/" + campaign.branch() + "/" + campaign.missionsDir();
					File file = new File(HomePath.toFileSystemName(s1, 0));
					File afile[] = file.listFiles();
					if (afile != null)
					{
						for (int l = 0; l < afile.length; l++)
						{
							File file1 = afile[l];
							String s2 = file1.getName();
							if (!".".equals(s2) && !"..".equals(s2))
								file1.delete();
						}
						
					}
					file.delete();
				}
				campaign.clearSavedStatics(sectfile);
			}
			catch (Exception exception)
			{}
		
	}
	
	public void removeUserDir()
	{
		removeDGens();
		File file = new File(HomePath.toFileSystemName("users/" + sId, 0));
		UserCfg.removeTree(file);
	}
	
	public static void removeTree(File file)
	{
		if (file.isDirectory())
		{
			File afile[] = file.listFiles();
			if (afile != null)
			{
				for (int i = 0; i < afile.length; i++)
					if (afile[i].isDirectory())
						UserCfg.removeTree(afile[i]);
					else
						afile[i].delete();
				
			}
		}
		file.delete();
	}
	
	public void createUserDir()
	{
		File file = new File(HomePath.toFileSystemName("users/" + sId, 0));
		if (file.exists())
			UserCfg.removeTree(file);
		file.mkdirs();
	}
	
	public void createUserConf()
	{
		String s = "users/" + sId + "/settings.ini";
		File file = new File(HomePath.toFileSystemName(s, 0));
		if (file.exists())
			UserCfg.removeTree(file);
		String s1 = "users/default.ini";
		IniFile inifile = new IniFile(s1, 0);
		inifile.saveFile(s);
	}
	
	public void makeId()
	{
		sId = null;
		if (surname.length() > 0)
		{
			sId = surname.toLowerCase();
			if (!Character.isDigit(sId.charAt(0)))
			{
				for (int i = 0; i < sId.length(); i++)
				{
					char c = sId.charAt(i);
					if (Character.isLetterOrDigit(c))
						continue;
					sId = null;
					break;
				}
				
			}
		}
		if (sId == null)
		{
			sId = "";
		}
		else
		{
			for (int j = 0; j < sId.length(); j++)
			{
				if (sId.charAt(j) < '\200')
					continue;
				sId = "";
				break;
			}
			
		}
		int k = 0;
		do
		{
			String s1;
			if (k == 0 && sId.length() > 0)
				s1 = sId;
			else
				s1 = sId + k;
			if (!existUserDir(s1))
			{
				sId = s1;
				return;
			}
			k++;
		}
		while (true);
	}
	
	public UserCfg()
	{
		netSquadron = 0;
		netTacticalNumber = 1;
		netNumberOn = true;
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		bZutiMultiCrew = false;
		bZutiMultiCrewAnytime = false;
		//------------------------------------
		
		coverMashineGun = 500F;
		coverCannon = 500F;
		coverRocket = 500F;
		rocketDelay = 10F;
		bombDelay = 0.0F;
		// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
        bombFuze = 0.0F;
		// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
		fuel = 100F;
		skinMap = new HashMapExt();
		weaponMap = new HashMapExt();
		noseartMap = new HashMapExt();
	}
	
	public UserCfg(String s, String s1, String s2)
	{
		netSquadron = 0;
		netTacticalNumber = 1;
		netNumberOn = true;
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		bZutiMultiCrew = false;
		bZutiMultiCrewAnytime = false;
		//------------------------------------
		
		coverMashineGun = 500F;
		coverCannon = 500F;
		coverRocket = 500F;
		rocketDelay = 10F;
		bombDelay = 0.0F;
        // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
        bombFuze = 0.0F;
        // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
		fuel = 100F;
		skinMap = new HashMapExt();
		weaponMap = new HashMapExt();
		noseartMap = new HashMapExt();
		if (s == null || s.length() == 0)
			s = " ";
		if (s1 == null || s1.length() == 0)
			s1 = " ";
		if (s2 == null || s2.length() == 0)
			s2 = " ";
		name = s;
		callsign = s1;
		surname = s2;
		sId = null;
	}
	
	public static UserCfg loadCurrent()
	{
		File file = new File(HomePath.toFileSystemName("users/all.ini", 0));
		if (!file.exists())
			return UserCfg.createDefault();
		SectFile sectfile = new SectFile("users/all.ini", 0);
		int i = sectfile.sectionIndex("list");
		int j = sectfile.sectionIndex("current");
		if (i < 0 || j < 0)
			return UserCfg.createDefault();
		int k = sectfile.vars(i);
		if (k == 0)
			return UserCfg.createDefault();
		String s = sectfile.var(j, 0);
		int l = 0;
		try
		{
			l = Integer.parseInt(s);
		}
		catch (Exception exception)
		{}
		if (l >= k)
			l = k - 1;
		String s1 = sectfile.var(i, l);
		NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, l));
		UserCfg usercfg = new UserCfg(UnicodeTo8bit.load(numbertokenizer.next(defName)), UnicodeTo8bit.load(numbertokenizer.next(defCallsign)), UnicodeTo8bit.load(numbertokenizer
				.next(defSurname)));
		usercfg.sId = s1;
		if (!usercfg.existUserDir())
			usercfg.createUserDir();
		if (!usercfg.existUserConf())
			usercfg.createUserConf();
		usercfg.loadConf();
		return usercfg;
	}
	
	public static UserCfg createDefault()
	{
		SectFile sectfile = new SectFile();
		sectfile.clear();
		UserCfg usercfg = new UserCfg(defName, defCallsign, defSurname);
		usercfg.makeId();
		int i = sectfile.sectionAdd("list");
		sectfile.lineAdd(i, usercfg.sId, UnicodeTo8bit.save(usercfg.name, true) + " " + UnicodeTo8bit.save(usercfg.callsign, true) + " " + UnicodeTo8bit.save(usercfg.surname, true));
		int j = sectfile.sectionAdd("current");
		sectfile.lineAdd(j, "0");
		sectfile.saveFile("users/all.ini");
		usercfg.createUserDir();
		usercfg.createUserConf();
		usercfg.loadConf();
		return usercfg;
	}
		
	static
	{
		if (Config.LOCALE.equals("RU"))
		{
			defName = "\u0418\u0432\u0430\u043D";
			defCallsign = "\u0412\u0430\u043D\u044F";
			defSurname = "\u0418\u0432\u0430\u043D\u043E\u0432";
		}
		else
		{
			defName = "John";
			defCallsign = "Mad";
			defSurname = "Doe";
		}
	}
}