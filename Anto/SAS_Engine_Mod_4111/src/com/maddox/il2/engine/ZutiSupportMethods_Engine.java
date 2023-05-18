package com.maddox.il2.engine;

import java.util.ArrayList;
import java.util.List;
import com.maddox.rts.IniFile;

public class ZutiSupportMethods_Engine
{
	/**
	 * Reset class variables
	 */
	public static void resetClassVariables()
	{
		ZutiSupportMethods_Engine.AIRFIELDS = new ArrayList();
		ZutiSupportMethods_Engine.WATER_STATE = null;
	}
	
	public static List AIRFIELDS = null;
	public static String WATER_STATE = null;
	
    /**
     * Method adds new mission defined airfield to AIRFIELDS list.
     * @param line
     */
	public static void addAirfieldPoint_MisIni(String line)
	{
		//This method is called first, before map.ini file is processed
		//System.out.println("----------------------------processing mission file...");
		String originalLine = line;
		try
		{
			double x1 = 0.0D;
			double y1 = 0.0D;
			double x2 = 0.0D;
			double y2 = 0.0D;
			double friction = 3.8D;
			
			int index = 0;
		
			while(true)
			{
				if(index > 3)
					break;
				
				String lineFirstPoint = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" "), line.length()).trim();
				
				switch(index)
				{
					case 0:
						x1 = Double.parseDouble(lineFirstPoint);
						break;
					case 1:
						y1 = Double.parseDouble(lineFirstPoint);
						break;
					case 2:
						x2 = Double.parseDouble(lineFirstPoint);
						break;
					case 3:
						y2 = Double.parseDouble(lineFirstPoint);
						friction = Double.parseDouble(line);
						break;
				}
				index++;
			}
			if( ZutiSupportMethods_Engine.AIRFIELDS != null )
				ZutiSupportMethods_Engine.AIRFIELDS.add(new com.maddox.il2.game.ZutiAirfieldPoint(x1, y1, x2, y2, friction));
			else
				System.out.println("ZutiAirports table is null!");
				
			//System.out.println("Mis file: T1 <" + new Double(x1).toString() + ", " + new Double(y1).toString() + ">, T2 <" + new Double(x2).toString() + ", " + new Double(y2).toString() + ">, Friction: " + new Double(friction).toString());
			System.out.println("Mis file: line <" + originalLine + "> is valid!");
		}
		catch(Exception ex)
		{
			System.out.println("Mis file: line <" + originalLine + "> is INVALID!");
		}
	}
	
	/**
     * Method adds new mission defined airfield to AIRFIELDS list.
     * @param line
     */
	public static void addAirfieldPoint_MapIni(String line)
	{
		//This method is called second in the row, after mission file was read
		//System.out.println("----------------------------processing map ini file...");
		String originalLine = line;
		try
		{
			double x1 = 0.0D;
			double y1 = 0.0D;
			double x2 = 0.0D;
			double y2 = 0.0D;
			double friction = 3.8D;
			
			int index = 0;
		
			while(true)
			{
				if(index > 3)
					break;
				
				String lineFirstPoint = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" "), line.length()).trim();
				
				switch(index)
				{
					case 0:
						x1 = Double.parseDouble(lineFirstPoint);
						break;
					case 1:
						y1 = Double.parseDouble(lineFirstPoint);
						break;
					case 2:
						x2 = Double.parseDouble(lineFirstPoint);
						break;
					case 3:
						y2 = Double.parseDouble(lineFirstPoint);
						friction = Double.parseDouble(line);
						break;
				}
				index++;
			}
			
			if( ZutiSupportMethods_Engine.AIRFIELDS != null )
				ZutiSupportMethods_Engine.AIRFIELDS.add(new com.maddox.il2.game.ZutiAirfieldPoint(x1, y1, x2, y2, friction));
			else
				System.out.println("ZutiAirports table is null!");
			
			System.out.println("Map file: line <" + originalLine + "> is valid!");
		}
		catch(Exception ex)
		{
			System.out.println("Map file: line <" + originalLine + "> is INVALID!");
		}
	}
	
	public static void loadMapDefinedAirfields(IniFile inifile)
	{
		//TODO: Created by |ZUTI|: load airfields/rearm places defined by map maker
		//--------------------------------------------------------------------------
		System.out.println("Loading map.ini defined airfields:");
		if( ZutiSupportMethods_Engine.AIRFIELDS == null )
			ZutiSupportMethods_Engine.AIRFIELDS = new ArrayList();
		
		for( int id=0; id<999; id++ )
		{
			String index = "";
			if( id<10 )
				index = "00" + new Integer(id).toString();
			else if( id>9 && id<100 )
				index = "0" + new Integer(id).toString();
			else if( id>99 )
				index = new Integer(id).toString();
			
			String airport = inifile.getValue("AIRFIELDS", "Airfield_" + index);
			if( airport.trim().length() > 0 )
			{
				ZutiSupportMethods_Engine.addAirfieldPoint_MapIni(airport);
			}
		}
		//--------------------------------------------------------------------------
	}
	
	public static void alterWaterState(IniFile inifile, String camouflage)
	{
		//TODO: Added by |ZUTI|: be compatible with old/original IL2 maps that don't have this WATER_STATE variable
		ZutiSupportMethods_Engine.WATER_STATE = inifile.get("WORLDPOS", "WATER_STATE", "");
		if( ZutiSupportMethods_Engine.WATER_STATE == null || ZutiSupportMethods_Engine.WATER_STATE.trim().length() == 0 )
		{
			if( "WINTER".equals(camouflage) )
				ZutiSupportMethods_Engine.WATER_STATE = "ICE";
			else
				ZutiSupportMethods_Engine.WATER_STATE = "LIQUID";
		}
	}
}