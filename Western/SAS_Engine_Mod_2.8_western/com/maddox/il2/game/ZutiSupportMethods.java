package com.maddox.il2.game;

import com.maddox.il2.objects.vehicles.tanks.*;
import com.maddox.il2.objects.trains.*;
import com.maddox.il2.objects.vehicles.artillery.*;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.objects.ships.*;
import com.maddox.il2.net.*;
import com.maddox.il2.gui.*;
import com.maddox.util.*;
import java.util.*;
import com.maddox.il2.engine.*;
import com.maddox.gwindow.*;
import com.maddox.JGP.*;
import com.maddox.rts.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.vehicles.stationary.*;
import com.maddox.il2.ai.*;

public class ZutiSupportMethods
{
    private static String[] ZUTI_TIP;
    private static boolean ZUTI_TARGETS_LOADED;
    public static int ZUTI_KIA_COUNTER;
    public static boolean ZUTI_KIA_DELAY_CLEARED;
    public static List ZUTI_BANNED_PILOTS;
    public static List ZUTI_DEAD_TARGETS;
    public static long BASE_CAPRUTING_LAST_CHECK;
    public static int BASE_CAPTURING_INTERVAL;
    
    public static void clear() {
        if (ZutiSupportMethods.ZUTI_BANNED_PILOTS != null) {
            ZutiSupportMethods.ZUTI_BANNED_PILOTS.clear();
        }
        if (ZutiSupportMethods.ZUTI_DEAD_TARGETS != null) {
            ZutiSupportMethods.ZUTI_DEAD_TARGETS.clear();
        }
    }
    
    public static void setTargetsLoaded(final boolean zuti_TARGETS_LOADED) {
        ZutiSupportMethods.ZUTI_TARGETS_LOADED = zuti_TARGETS_LOADED;
    }
    
    public static void fillAirInterval(final GUIPad guiPad) {
        final Mission mission = Main.cur().mission;
        if (mission == null) {
            return;
        }
        try {
            final Aircraft playerAircraft = World.getPlayerAircraft();
            final boolean b = mission.zutiRadar_RefreshInterval > 0;
            final List targets = Engine.targets();
            for (int size = targets.size(), i = 0; i < size; ++i) {
                final Actor actor = (Actor)targets.get(i);
                if (!actor.equals(playerAircraft)) {
                    if (actor instanceof Aircraft && !actor.getDiedFlag() && !guiPad.zutiPadObjects.containsKey(new Integer(actor.hashCode()))) {
                        final ZutiPadObject zutiPadObject = new ZutiPadObject(actor, b);
                        zutiPadObject.type = 0;
                        guiPad.zutiPadObjects.put(new Integer(zutiPadObject.hashCode()), zutiPadObject);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void fillGroundChiefsArray(final GUIPad guiPad) {
        final Mission mission = Main.cur().mission;
        if (mission == null) {
            return;
        }
        final boolean b = mission.zutiRadar_RefreshInterval > 0;
        final HashMapExt name2Actor = Engine.name2Actor();
        for (Map.Entry entry = name2Actor.nextEntry(null); entry != null; entry = name2Actor.nextEntry(entry)) {
            final Actor actor = (Actor) entry.getValue();
            if (!GUI.pad.zutiPadObjects.containsKey(new Integer(actor.hashCode()))) {
                if (actor.isAlive()) {
                    if (actor instanceof TankGeneric) {
                        final ZutiPadObject zutiPadObject = new ZutiPadObject(actor, b);
                        zutiPadObject.type = 1;
                        guiPad.zutiPadObjects.put(new Integer(zutiPadObject.hashCode()), zutiPadObject);
                    }
                    else if (actor instanceof Train) {
                        final ZutiPadObject zutiPadObject2 = new ZutiPadObject(actor, b);
                        zutiPadObject2.type = 5;
                        guiPad.zutiPadObjects.put(new Integer(zutiPadObject2.hashCode()), zutiPadObject2);
                    }
                    else if (actor instanceof RocketryGeneric) {
                        final ZutiPadObject zutiPadObject3 = new ZutiPadObject(actor, b);
                        zutiPadObject3.type = 3;
                        guiPad.zutiPadObjects.put(new Integer(zutiPadObject3.hashCode()), zutiPadObject3);
                    }
                    else if (actor instanceof AAA) {
                        if (!actor.getDiedFlag()) {
                            final ZutiPadObject zutiPadObject4 = new ZutiPadObject(actor, b);
                            zutiPadObject4.type = 2;
                            guiPad.zutiPadObjects.put(new Integer(zutiPadObject4.hashCode()), zutiPadObject4);
                        }
                    }
                    else if (actor instanceof ArtilleryGeneric) {
                        if (!actor.getDiedFlag()) {
                            final ZutiPadObject zutiPadObject5 = new ZutiPadObject(actor, b);
                            zutiPadObject5.type = 2;
                            guiPad.zutiPadObjects.put(new Integer(zutiPadObject5.hashCode()), zutiPadObject5);
                        }
                    }
                    else if (actor instanceof ChiefGround) {
                        final ZutiPadObject zutiPadObject6 = new ZutiPadObject(actor, b);
                        zutiPadObject6.type = 5;
                        guiPad.zutiPadObjects.put(new Integer(zutiPadObject6.hashCode()), zutiPadObject6);
                    }
                    else if ((actor instanceof BigshipGeneric || actor instanceof ShipGeneric) && !actor.getDiedFlag()) {
                        final ZutiPadObject zutiPadObject7 = new ZutiPadObject(actor, b);
                        zutiPadObject7.type = 4;
                        guiPad.zutiPadObjects.put(new Integer(zutiPadObject7.hashCode()), zutiPadObject7);
                    }
                }
            }
        }
    }
    
    public static String getCountryFromNetRegiment(final String s) {
        final ResourceBundle bundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        final List all = Regiment.getAll();
        for (int size = all.size(), i = 0; i < size; ++i) {
            final Regiment regiment = (Regiment) all.get(i);
            final String string = bundle.getString(regiment.branch());
            if (regiment.name().equals(s)) {
                return string;
            }
        }
        return "NONE";
    }
    
    public static String getUserCfgRegiment(final String s) {
        final ResourceBundle bundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        final List all = Regiment.getAll();
        for (int size = all.size(), i = 0; i < size; ++i) {
            final Regiment regiment = (Regiment) all.get(i);
            if (bundle.getString(regiment.branch()).equals(s)) {
                return regiment.name();
            }
        }
        return "NoNe";
    }
    
    public static String getHomeBaseFirstCountry(final BornPlace bornPlace) {
        final ArrayList zutiHomeBaseCountries = bornPlace.zutiHomeBaseCountries;
        if (zutiHomeBaseCountries == null || zutiHomeBaseCountries.size() == 0) {
            return "None";
        }
        return (String) zutiHomeBaseCountries.get(0);
    }
    
    public static boolean isRegimentValidForSelectedHB(final String s, final BornPlace bornPlace) {
        final String countryFromNetRegiment = getCountryFromNetRegiment(s);
        if (bornPlace.zutiHomeBaseCountries == null) {
            return false;
        }
        for (int i = 0; i < bornPlace.zutiHomeBaseCountries.size(); ++i) {
            if (((String)bornPlace.zutiHomeBaseCountries.get(i)).equals(countryFromNetRegiment)) {
                return true;
            }
        }
        return false;
    }
    
    public static int getPlayerArmy() {
        if (Mission.isDogfight()) {
            return ((NetUser)NetEnv.host()).getArmy();
        }
        return World.getPlayerArmy();
    }
    
    public static void removeTarget(String s)
    {
        try
        {
            com.maddox.il2.gui.GUIBriefing.TargetPoint targetpoint = null;
            Iterator iterator = GUIBriefing.ZUTI_TARGETS.iterator();
            Object obj = null;
            do
            {
                if(!iterator.hasNext())
                    break;
                com.maddox.il2.gui.GUIBriefing.TargetPoint targetpoint1 = (com.maddox.il2.gui.GUIBriefing.TargetPoint)iterator.next();
                if(targetpoint1.nameTargetOrig == null || targetpoint1.nameTargetOrig.indexOf(s.trim()) <= -1)
                    continue;
                targetpoint = targetpoint1;
                break;
            } while(true);
            if(targetpoint != null)
                GUIBriefing.ZUTI_TARGETS.remove(targetpoint);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
    
    public static boolean removeTarget(double d, double d1)
    {
        com.maddox.il2.gui.GUIBriefing.TargetPoint targetpoint;
        targetpoint = null;
        Object obj = null;
        Iterator iterator = GUIBriefing.ZUTI_TARGETS.iterator();
        try{
            do
            {
                if(!iterator.hasNext())
                    break;
                com.maddox.il2.gui.GUIBriefing.TargetPoint targetpoint1 = (com.maddox.il2.gui.GUIBriefing.TargetPoint)iterator.next();
                if((double)targetpoint1.x != d || (double)targetpoint1.y != d1)
                    continue;
                targetpoint = targetpoint1;
                break;
            } while(true);
            if(targetpoint != null)
            {
                GUIBriefing.ZUTI_TARGETS.remove(targetpoint);
                return true;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
    
    public static void assignTargetActor(final GUIBriefing.TargetPoint targetPoint) {
        if (targetPoint == null) {
            return;
        }
        try {
            targetPoint.actor = Actor.getByName(targetPoint.nameTarget);
            if (targetPoint.actor != null && targetPoint.actor instanceof Wing) {
                final Wing wing = (Wing)targetPoint.actor;
                targetPoint.isBaseActorWing = true;
                targetPoint.wing = wing;
                for (int length = wing.airc.length, i = 0; i < length; ++i) {
                    if (wing.airc[i] != null && !wing.airc[i].getDiedFlag()) {
                        targetPoint.actor = wing.airc[i];
                        break;
                    }
                }
            }
            else if (targetPoint.isBaseActorWing && targetPoint.wing != null) {
                for (int length2 = targetPoint.wing.airc.length, j = 0; j < length2; ++j) {
                    if (targetPoint.wing.airc[j] != null && !targetPoint.wing.airc[j].getDiedFlag()) {
                        targetPoint.actor = targetPoint.wing.airc[j];
                        break;
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void fillTargets(final SectFile sectFile) {
        if (ZutiSupportMethods.ZUTI_TARGETS_LOADED) {
            return;
        }
        GUIBriefing.ZUTI_TARGETS.clear();
        final int sectionIndex = sectFile.sectionIndex("Target");
        if (sectionIndex >= 0) {
            for (int vars = sectFile.vars(sectionIndex), i = 0; i < vars; ++i) {
                final NumberTokenizer numberTokenizer = new NumberTokenizer(sectFile.line(sectionIndex, i));
                final int next = numberTokenizer.next(0, 0, 7);
                final int next2 = numberTokenizer.next(0, 0, 2);
                if (next2 != 2) {
                    final GUIBriefing.TargetPoint targetPoint = new GUIBriefing.TargetPoint();
                    targetPoint.type = next;
                    targetPoint.importance = next2;
                    numberTokenizer.next(0);
                    numberTokenizer.next(0, 0, 720);
                    numberTokenizer.next(0);
                    targetPoint.x = numberTokenizer.next(0);
                    targetPoint.y = numberTokenizer.next(0);
                    int next3 = numberTokenizer.next(0);
                    if (targetPoint.type == 3 || targetPoint.type == 6 || targetPoint.type == 1) {
                        if (next3 < 50) {
                            next3 = 50;
                        }
                        if (next3 > 3000) {
                            next3 = 3000;
                        }
                    }
                    targetPoint.r = next3;
                    numberTokenizer.next(0);
                    targetPoint.nameTarget = numberTokenizer.next(null);
                    if (targetPoint.nameTarget != null && targetPoint.nameTarget.startsWith("Bridge")) {
                        targetPoint.nameTargetOrig = targetPoint.nameTarget;
                        targetPoint.nameTarget = null;
                    }
                    final int next4 = numberTokenizer.next(0);
                    final int next5 = numberTokenizer.next(0);
                    if (next4 != 0 && next5 != 0) {
                        targetPoint.x = next4;
                        targetPoint.y = next5;
                    }
                    switch (targetPoint.type) {
                        case 0: {
                            targetPoint.icon = IconDraw.get("icons/tdestroyair.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdefence.mat");
                            if (targetPoint.nameTarget != null && sectFile.exist("Chiefs", targetPoint.nameTarget)) {
                                targetPoint.icon = IconDraw.get("icons/tdestroychief.mat");
                                targetPoint.iconOArmy = IconDraw.get("icons/tdefence.mat");
                            }
                            assignTargetActor(targetPoint);
                            break;
                        }
                        case 1: {
                            targetPoint.icon = IconDraw.get("icons/tdestroyground.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdefenceground.mat");
                            break;
                        }
                        case 2: {
                            targetPoint.icon = IconDraw.get("icons/tdestroybridge.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdefencebridge.mat");
                            targetPoint.nameTarget = null;
                            break;
                        }
                        case 3: {
                            targetPoint.icon = IconDraw.get("icons/tinspect.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdefence.mat");
                            assignTargetActor(targetPoint);
                            break;
                        }
                        case 4: {
                            targetPoint.icon = IconDraw.get("icons/tescort.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdestroychief.mat");
                            assignTargetActor(targetPoint);
                            break;
                        }
                        case 5: {
                            targetPoint.icon = IconDraw.get("icons/tdefence.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdestroychief.mat");
                            assignTargetActor(targetPoint);
                            break;
                        }
                        case 6: {
                            targetPoint.icon = IconDraw.get("icons/tdefenceground.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdestroyground.mat");
                            break;
                        }
                        case 7: {
                            targetPoint.icon = IconDraw.get("icons/tdefencebridge.mat");
                            targetPoint.iconOArmy = IconDraw.get("icons/tdestroybridge.mat");
                            targetPoint.nameTarget = null;
                            break;
                        }
                    }
                    if (targetPoint.nameTarget != null) {
                        targetPoint.nameTargetOrig = targetPoint.nameTarget;
                    }
                    else {
                        targetPoint.nameTarget = Float.toString(targetPoint.x) + Float.toString(targetPoint.y);
                        if (targetPoint.nameTargetOrig == null) {
                            targetPoint.nameTargetOrig = targetPoint.nameTarget;
                        }
                    }
                    if (targetPoint.nameTarget != null) {
                        if (sectFile.exist("Chiefs", targetPoint.nameTarget)) {
                            try {
                                final String nextToken = new StringTokenizer(sectFile.get("Chiefs", targetPoint.nameTarget, (String)null)).nextToken();
                                final int index = nextToken.indexOf(".");
                                targetPoint.nameTarget = I18N.technic(nextToken.substring(0, index)) + " " + I18N.technic(nextToken.substring(index + 1));
                            }
                            catch (Exception ex) {
                                targetPoint.nameTarget = null;
                            }
                        }
                        else if (sectFile.sectionIndex(targetPoint.nameTarget) >= 0) {
                            try {
                                targetPoint.nameTarget = Property.stringValue((Object)ObjIO.classForName(sectFile.get(targetPoint.nameTarget, "Class", (String)null)), "iconFar_shortClassName", null);
                            }
                            catch (Exception ex2) {
                                targetPoint.nameTarget = null;
                            }
                        }
                        else {
                            targetPoint.nameTarget = null;
                        }
                    }
                    GUIBriefing.ZUTI_TARGETS.add(targetPoint);
                }
            }
        }
        ZutiTargetsSupportMethods.checkForDeactivatedTargets();
        final ArrayList zutiTargetNamesToRemove = World.cur().targetsGuard.zutiTargetNamesToRemove;
        for (int size = zutiTargetNamesToRemove.size(), j = 0; j < size; ++j) {
            removeTarget((String)zutiTargetNamesToRemove.get(j));
        }
        World.cur().targetsGuard.zutiTargetNamesToRemove.clear();
        final ArrayList zutiTargetPosToRemove = World.cur().targetsGuard.zutiTargetPosToRemove;
        for (int size2 = zutiTargetPosToRemove.size(), k = 0; k < size2; ++k) {
            final Target target = (Target) zutiTargetPosToRemove.get(k);
            removeTarget(target.pos.getAbs().getX(), target.pos.getAbs().getY());
        }
        World.cur().targetsGuard.zutiTargetPosToRemove.clear();
        ZutiSupportMethods.ZUTI_TARGETS_LOADED = true;
    }
    
    private static void drawTargets(GUIRenders guirenders, TTFont ttfont, Mat mat, CameraOrtho2D cameraortho2d, Set set, int i, boolean flag)
    {
        try
        {
            if(set.size() != 0)
            {
                GPoint gpoint = guirenders.getMouseXY();
                float f = gpoint.x;
                float f1 = guirenders.win.dy - 1.0F - gpoint.y;
                float f2 = IconDraw.scrSizeX() / 2;
                float f3 = f;
                float f4 = f1;
                IconDraw.setColor(0xff00ffff);
                Object obj = null;
                com.maddox.il2.gui.GUIBriefing.TargetPoint targetpoint1 = null;
                Iterator iterator = set.iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    com.maddox.il2.gui.GUIBriefing.TargetPoint targetpoint = (com.maddox.il2.gui.GUIBriefing.TargetPoint)iterator.next();
                    if(targetpoint.icon != null)
                    {
                        if(targetpoint.isBaseActorWing && (targetpoint.actor == null || targetpoint.actor.getDiedFlag()))
                            assignTargetActor(targetpoint);
                        float f5 = (float)(((double)targetpoint.x - cameraortho2d.worldXOffset) * cameraortho2d.worldScale);
                        float f7 = (float)(((double)targetpoint.y - cameraortho2d.worldYOffset) * cameraortho2d.worldScale);
                        if(flag)
                            IconDraw.render(targetpoint.icon, f5, f7);
                        else
                            IconDraw.render(targetpoint.iconOArmy, f5, f7);
                        if(f5 >= f - f2 && f5 <= f + f2 && f7 >= f1 - f2 && f7 <= f1 + f2)
                        {
                            targetpoint1 = targetpoint;
                            f3 = f5;
                            f4 = f7;
                        }
                    }
                } while(true);
                if(targetpoint1 != null)
                {
                    for(int j = 0; j < 3; j++)
                        ZUTI_TIP[j] = null;

                    if(targetpoint1.importance == 0)
                        ZUTI_TIP[0] = I18N.gui("brief.Primary");
                    else
                        ZUTI_TIP[0] = I18N.gui("brief.Secondary");
                    if(flag)
                        switch(targetpoint1.type)
                        {
                        case 0: // '\0'
                            ZUTI_TIP[1] = I18N.gui("brief.Destroy");
                            break;

                        case 1: // '\001'
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyGround");
                            break;

                        case 2: // '\002'
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyBridge");
                            break;

                        case 3: // '\003'
                            ZUTI_TIP[1] = I18N.gui("brief.Inspect");
                            break;

                        case 4: // '\004'
                            ZUTI_TIP[1] = I18N.gui("brief.Escort");
                            break;

                        case 5: // '\005'
                            ZUTI_TIP[1] = I18N.gui("brief.Defence");
                            break;

                        case 6: // '\006'
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceGround");
                            break;

                        case 7: // '\007'
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceBridge");
                            break;
                        }
                    else
                        switch(targetpoint1.type)
                        {
                        case 0: // '\0'
                            ZUTI_TIP[1] = I18N.gui("brief.Defence");
                            break;

                        case 1: // '\001'
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceGround");
                            break;

                        case 2: // '\002'
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceBridge");
                            break;

                        case 3: // '\003'
                            ZUTI_TIP[1] = I18N.gui("brief.Defence");
                            break;

                        case 4: // '\004'
                            ZUTI_TIP[1] = I18N.gui("brief.Destroy");
                            break;

                        case 5: // '\005'
                            ZUTI_TIP[1] = I18N.gui("brief.Destroy");
                            break;

                        case 6: // '\006'
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyGround");
                            break;

                        case 7: // '\007'
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyBridge");
                            break;
                        }
                    if(targetpoint1.nameTarget != null)
                        ZUTI_TIP[2] = targetpoint1.nameTarget;
                    float f6 = ttfont.width(ZUTI_TIP[0]);
                    int k = 1;
                    for(int l = 1; l < 3 && ZUTI_TIP[l] != null; l++)
                    {
                        k = l;
                        float f9 = ttfont.width(ZUTI_TIP[l]);
                        if(f6 < f9)
                            f6 = f9;
                    }

                    float f8 = -ttfont.descender();
                    float f10 = (float)ttfont.height() + f8;
                    f6 += 2.0F * f8;
                    float f11 = f10 * (float)(k + 1) + 2.0F * f8;
                    float f12 = f3 - f6 / 2.0F;
                    float f13 = f4 + f2;
                    if(f12 + f6 > guirenders.win.dx)
                        f12 = guirenders.win.dx - f6;
                    if(f13 + f11 > guirenders.win.dy)
                        f13 = guirenders.win.dy - f11;
                    if(f12 < 0.0F)
                        f12 = 0.0F;
                    if(f13 < 0.0F)
                        f13 = 0.0F;
                    Render.drawTile(f12, f13, f6, f11, 0.0F, mat, 0xcf7fffff, 0.0F, 0.0F, 1.0F, 1.0F);
                    Render.drawEnd();
                    for(int i1 = 0; i1 <= k; i1++)
                        ttfont.output(0xff000000, f12 + f8, f13 + f8 + (float)(k - i1) * f10 + f8, 0.0F, ZUTI_TIP[i1]);

                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
    
    public static void drawTargets(final GUIRenders guiRenders, final TTFont ttFont, final Mat mat, final CameraOrtho2D cameraOrtho2D) {
        try {
            final int playerArmy = getPlayerArmy();
            if (playerArmy < 1) {
                return;
            }
            if (playerArmy == World.getMissionArmy()) {
                drawTargets(guiRenders, ttFont, mat, cameraOrtho2D, GUIBriefing.ZUTI_TARGETS, playerArmy, true);
            }
            else {
                drawTargets(guiRenders, ttFont, mat, cameraOrtho2D, GUIBriefing.ZUTI_TARGETS, playerArmy, false);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static Airport getAirport(double d, double d1)
    {
        ArrayList arraylist = new ArrayList();
        World.getAirports(arraylist);
        double d2 = 1000000D;
        Airport airport = null;
        try
        {
            int i = arraylist.size();
            for(int j = 0; j < i; j++)
            {
                Airport airport1 = (Airport)arraylist.get(j);
                Point3d point3d = airport1.pos.getAbsPoint();
                double d3 = Math.sqrt(Math.pow(d - point3d.x, 2D) + Math.pow(d1 - point3d.y, 2D));
                if(d3 < d2)
                {
                    d2 = d3;
                    airport = airport1;
                }
            }

        }
        catch(Exception exception) { }
        return airport;
    }

    public static BornPlace getPlayerBornPlace(final Point3d point3d, final int n) {
        final int bornPlace = ((NetUser)NetEnv.host()).getBornPlace();
        BornPlace nearestBornPlace = (BornPlace)World.cur().bornPlaces.get(bornPlace);
        if (point3d == null) {
            return nearestBornPlace;
        }
        if (bornPlace == -1) {
            nearestBornPlace = getNearestBornPlace(point3d.x, point3d.y, n);
        }
        return nearestBornPlace;
    }
    
    public static BornPlace getNearestBornPlace(final double n, final double n2, final int n3) {
        final ArrayList bornPlaces = World.cur().bornPlaces;
        double n4 = 1000000.0;
        BornPlace bornPlace = null;
        try {
            for (int size = bornPlaces.size(), i = 0; i < size; ++i) {
                final BornPlace bornPlace2 = (BornPlace)bornPlaces.get(i);
                if (bornPlace2.army == n3) {
                    final double sqrt = Math.sqrt(Math.pow(n - bornPlace2.place.x, 2.0) + Math.pow(n2 - bornPlace2.place.y, 2.0));
                    if (sqrt < n4) {
                        n4 = sqrt;
                        bornPlace = bornPlace2;
                    }
                }
            }
        }
        catch (Exception ex) {}
        return bornPlace;
    }
    
    public static List getUnavailableAircraftList(BornPlace bornplace)
    {
        ArrayList arraylist = new ArrayList();
        if(bornplace == null)
            return arraylist;
        ArrayList arraylist1 = bornplace.zutiGetNotAvailablePlanesList();
        StringBuffer stringbuffer = new StringBuffer();
        if(arraylist1 != null)
        {
            for(int i = 0; i < arraylist1.size(); i++)
                if(stringbuffer.toString().length() < 200)
                {
                    stringbuffer.append((String)arraylist1.get(i));
                    stringbuffer.append(" ");
                } else
                {
                    arraylist.add(stringbuffer.toString().trim());
                    stringbuffer = new StringBuffer();
                    stringbuffer.append((String)arraylist1.get(i));
                    stringbuffer.append(" ");
                }

            arraylist.add(stringbuffer.toString().trim());
        }
        return arraylist;
    }
    
    public static void setAircraftAvailabilityForHomeBase(final ArrayList list, final double n, final double n2) {
        if (World.cur() == null || Main.cur().netServerParams == null || Main.cur().netServerParams.isMaster()) {
            return;
        }
        final ArrayList bornPlaces = World.cur().bornPlaces;
        if (bornPlaces == null) {
            return;
        }
        for (int i = 0; i < bornPlaces.size(); ++i) {
            final BornPlace bornPlace = (BornPlace)bornPlaces.get(i);
            if (bornPlace.place.x == n && bornPlace.place.y == n2) {
                bornPlace.zutiActivatePlanes(list);
            }
        }
    }
    
    public static void setPlayerBanDuration(final long n) {
        final NetUser netUser = (NetUser)NetEnv.host();
        final String uniqueName = netUser.uniqueName();
        String hostAddress = "127.0.0.1";
        try {
            hostAddress = netUser.masterChannel().remoteAddress().getHostAddress();
        }
        catch (Exception ex) {}
        ZutiBannedUser zutiBannedUser = null;
        if (ZutiSupportMethods.ZUTI_BANNED_PILOTS != null) {
            for (int size = ZutiSupportMethods.ZUTI_BANNED_PILOTS.size(), i = 0; i < size; ++i) {
                final ZutiBannedUser zutiBannedUser2 = (ZutiBannedUser) ZutiSupportMethods.ZUTI_BANNED_PILOTS.get(i);
                if (zutiBannedUser2.isMatch(uniqueName, hostAddress)) {
                    zutiBannedUser = zutiBannedUser2;
                    break;
                }
            }
        }
        if (zutiBannedUser != null) {
            zutiBannedUser.setDuration(Time.current() + n * 1000L);
        }
    }
    
    public static boolean isPlayerBanned(final String name, final String ip) {
        if (Main.cur().mission == null) {
            return false;
        }
        ZutiBannedUser zutiBannedUser = null;
        if (ZutiSupportMethods.ZUTI_BANNED_PILOTS != null) {
            for (int size = ZutiSupportMethods.ZUTI_BANNED_PILOTS.size(), i = 0; i < size; ++i) {
                final ZutiBannedUser zutiBannedUser2 = (ZutiBannedUser) ZutiSupportMethods.ZUTI_BANNED_PILOTS.get(i);
                if (zutiBannedUser2.isMatch(name, ip)) {
                    zutiBannedUser = zutiBannedUser2;
                    break;
                }
            }
        }
        if (zutiBannedUser == null) {
            final ZutiBannedUser zutiBannedUser3 = new ZutiBannedUser();
            zutiBannedUser3.setName(name);
            zutiBannedUser3.setIP(ip);
            zutiBannedUser3.setDuration(0L);
            ZutiSupportMethods.ZUTI_BANNED_PILOTS.add(zutiBannedUser3);
        }
        else {
            if (Main.cur().mission.zutiMisc_EnableReflyOnlyIfBailedOrDied && zutiBannedUser.isBanned()) {
                return true;
            }
            if (Main.cur().mission.zutiMisc_DisableReflyForMissionDuration) {
                return true;
            }
        }
        return false;
    }
    
    public static void managePilotBornPlacePlaneCounter(final NetAircraft netAircraft, final boolean b) {
        if (netAircraft == null) {
            return;
        }
        if (netAircraft.net == null) {
            return;
        }
        final NetUser netUser = ((NetAircraft.AircraftNet)netAircraft.net).netUser;
        if (netUser == null) {
            return;
        }
        final String stringValue = Property.stringValue(((Aircraft)netAircraft).getClass(), "keyName");
        final BornPlace bornPlace = (BornPlace) World.cur().bornPlaces.get(netUser.getBornPlace());
        if (bornPlace != null) {
            bornPlace.zutiReleaseAircraft(netAircraft.FM, stringValue, ZutiAircraft.isPlaneUsable(netAircraft.FM), false, b);
        }
    }
    
    public static boolean isPlaneStationary(final FlightModel flightModel) {
        return flightModel.getSpeedKMH() < 1.0 && flightModel.getVertSpeed() < 1.0;
    }
    
    public static boolean isStaticActor(final Actor actor) {
        if (actor instanceof SmokeGeneric) {
            return false;
        }
        if (actor instanceof VisualLandingAidGeneric) {
            return false;
        }
        if (actor instanceof ShipGeneric) {
            return ((ShipGeneric)actor).zutiIsStatic();
        }
        if (actor instanceof BigshipGeneric) {
            return ((BigshipGeneric)actor).zutiIsStatic();
        }
        return !(actor instanceof Aircraft) && !(actor instanceof Chief) && (!Actor.isValid(actor.getOwner()) || !(actor.getOwner() instanceof Chief));
    }
    
    public static void removeBornPlace(final BornPlace bornPlace) {
        disconnectPilotsFromBornPlace(bornPlace);
        for (int size = bornPlace.zutiBpStayPoints.size(), i = 0; i < size; ++i) {
            try {
                ((ZutiStayPoint)bornPlace.zutiBpStayPoints.get(i)).pointStay.set(-1000000.0f, -1000000.0f);
            }
            catch (Exception ex) {}
        }
        World.cur().bornPlaces.remove(bornPlace);
    }
    
    public static void disconnectPilotsFromBornPlace(final BornPlace bornPlace) {
        final ArrayList bornPlaces = World.cur().bornPlaces;
        for (int size = bornPlaces.size(), i = 0; i < size; ++i) {
            try {
                if (bornPlaces.get(i) == bornPlace) {
                    final NetUser netUser = (NetUser)NetEnv.host();
                    if (netUser.getBornPlace() == i) {
                        netUser.setBornPlace(-1);
                    }
                    final List hosts = NetEnv.hosts();
                    for (int size2 = hosts.size(), j = 0; j < size2; ++j) {
                        final NetUser netUser2 = (NetUser) hosts.get(j);
                        if (netUser2.getBornPlace() == i) {
                            netUser2.setBornPlace(-1);
                        }
                    }
                    break;
                }
            }
            catch (Exception ex) {}
        }
    }
    
    static {
        ZutiSupportMethods.ZUTI_TIP = new String[3];
        ZutiSupportMethods.ZUTI_TARGETS_LOADED = false;
        ZutiSupportMethods.ZUTI_KIA_COUNTER = 0;
        ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
        ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK = 0L;
        ZutiSupportMethods.BASE_CAPTURING_INTERVAL = 2000;
    }
}
