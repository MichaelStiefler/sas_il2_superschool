// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   I18N.java

package com.maddox.il2.game;

import com.maddox.rts.LDRres;
import com.maddox.rts.RTSConf;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18N
{
    static class Res
    {

        String get(String s)
        {
            if ( (res == null) && (res_us == null) && (res_ru == null) )
                return s;
            try {
            	return res.getString(s);
	        } catch (Exception e1) {
	            try {
	            	return res_us.getString(s);
		        } catch (Exception e2) {
		            try {
		            	return res_ru.getString(s);
			        } catch (Exception e3) {
			        }
		        }
	        }
            return s;
        }

        boolean isExist(String s)
        {
            if ( (res == null) && (res_us == null) && (res_ru == null) )
                return false;
            try {
	            res.getString(s);
	            return true;
            } catch (Exception e1) {
                try {
    	            res_us.getString(s);
    	            return true;
                } catch (Exception e2) {
                    try {
        	            res_ru.getString(s);
        	            return true;
                    } catch (Exception e3) {
                    	return false;
                    }
                }
            }
        }

//        private String myName;
        ResourceBundle res;
        ResourceBundle res_us;
        ResourceBundle res_ru;

        Res(String s)
        {
            try
            {
                res = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
                res_us = ResourceBundle.getBundle(s, Locale.US, LDRres.loader());
                res_ru = ResourceBundle.getBundle(s, new Locale("ru", "RU"), LDRres.loader());
//                myName = s;
            }
            catch(Exception exception) { }
        }
    }


    public static String army(String s)
    {
        if(army == null)
            army = new Res("i18n/army");
        return army.get(s);
    }

    public static String color(String s)
    {
        if(color == null)
            color = new Res("i18n/color");
        return color.get(s);
    }

    public static String map(String s)
    {
        if(map == null)
            map = new Res("i18n/maps");
        return map.get(s);
    }

    public static String gui(String s)
    {
        if(gui == null)
            gui = new Res("i18n/gui");
        return gui.get(s);
    }

    public static boolean isGuiExist(String s)
    {
        if(gui == null)
            gui = new Res("i18n/gui");
        return gui.isExist(s);
    }

    public static String bld(String s)
    {
        if(bld == null)
            bld = new Res("i18n/bld");
        return bld.get(s);
    }

    public static String plane(String s)
    {
        if(plane == null)
            plane = new Res("i18n/plane");
        return plane.get(s);
    }

    public static String weapons(String s, String s1)
    {
        if(weapons == null)
            weapons = new Res("i18n/weapons");
        String s2 = s + "." + s1;
        if(weapons.isExist(s2))
            return weapons.get(s2);
        else
            return s1;
    }

    public static String technic(String s)
    {
        if(technic == null)
            technic = new Res("i18n/technics");
        return technic.get(s);
    }

    public static String regimentShort(String s)
    {
        if(regimentShort == null)
            regimentShort = new Res("i18n/regShort");
        return regimentShort.get(s);
    }

    public static String regimentInfo(String s)
    {
        if(regimentInfo == null)
            regimentInfo = new Res("i18n/regInfo");
        return regimentInfo.get(s);
    }

    public static String gwindow(String s)
    {
        if(s == null)
            return s;
        if(gwindow == null)
            gwindow = new Res("i18n/gwindow");
        if(gwindow.isExist(s))
            return gwindow.get(s);
        if(s.indexOf('_') < 0)
            return s;
        else
            return s.replace('_', ' ');
    }

    public static String hud_log(String s)
    {
        if(hud_log == null)
            hud_log = new Res("i18n/hud_log");
        return hud_log.get(s);
    }

    public static String credits(String s)
    {
        if(credits == null)
            credits = new Res("i18n/credits");
        return credits.get(s);
    }

    private I18N()
    {
    }

    static Res army;
    static Res color;
    static Res map;
    static Res gui;
    static Res bld;
    static Res plane;
    static Res weapons;
    static Res technic;
    static Res regimentShort;
    static Res regimentInfo;
    static Res gwindow;
    static Res hud_log;
    static Res credits;
}