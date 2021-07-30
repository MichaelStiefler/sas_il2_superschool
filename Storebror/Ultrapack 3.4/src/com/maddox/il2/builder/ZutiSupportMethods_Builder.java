package com.maddox.il2.builder;

import java.io.DataInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GWindowBoxSeparate;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowTabDialogClient;
import com.maddox.il2.ai.Target;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.builder.PlMisHouse.Type;
import com.maddox.il2.builder.Zuti_WResourcesManagement.RRRItem;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.ZutiAircraft;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.ships.ZutiTypeAircraftCarrier;
import com.maddox.il2.objects.vehicles.planes.Plane.SpawnplaceMarker;
import com.maddox.il2.objects.vehicles.planes.Plane.Spawnplaceholder;
import com.maddox.rts.HomePath;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.sas1946.il2.util.CommonTools;
import com.maddox.util.NumberTokenizer;

public class ZutiSupportMethods_Builder {
    private static final int SPAWN_PLACE_HOLDER_SAVED_ARMY = 65535;
    public static String     BOMB_CARGO_NAME               = "BombCargo";
    private static Map       DEFAULT_RRR_OBJECT_PROPERTIES = new HashMap();
    static {
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Blue_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("BombCargo_Red_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTank_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_fuel", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_engines", new Long(6));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_Workshop_repairKits", new Long(10));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_fuel", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_engines", new Long(6));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("3Duby_HQ_repairKits", new Long(10));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_02_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("FTankS_01_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDEW_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bullets", new Long(800));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_rockets", new Long(8));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bomb250", new Long(6));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bomb500", new Long(4));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bomb1000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bomb2000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bomb5000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_bomb9999", new Long(1));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_fuel", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46TRBoxes_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bullets", new Long(800));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_rockets", new Long(8));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bomb250", new Long(6));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bomb500", new Long(4));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bomb1000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bomb2000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bomb5000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_bomb9999", new Long(1));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_fuel", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46LWBoxes_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bullets", new Long(800));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_rockets", new Long(8));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bomb250", new Long(6));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bomb500", new Long(4));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bomb1000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bomb2000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bomb5000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_bomb9999", new Long(1));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_fuel", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46JuBoxes_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bullets", new Long(800));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_rockets", new Long(8));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bomb250", new Long(6));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bomb500", new Long(4));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bomb1000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bomb2000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bomb5000", new Long(2));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_bomb9999", new Long(1));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_fuel", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46GRBoxes_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankRU_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankDE_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("46FTankCIV_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("TrainOilTank_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeFuelTankW_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeTank1W_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock2W_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("AirdromeBarrelBlock1W_repairKits", new Long(0));
        // ==================================================
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bullets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_rockets", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bomb250", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bomb500", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bomb1000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bomb2000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bomb5000", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_bomb9999", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_fuel", new Long(2500));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_engines", new Long(0));
        DEFAULT_RRR_OBJECT_PROPERTIES.put("IndustrialFactoryTank1W_repairKits", new Long(0));
        // ==================================================
    }

    // ----------------------------------------------------------
    static class ZutiAircraft_CompareByName implements Comparator {
        public int compare(Object o1, Object o2) {
            ZutiAircraft oo1 = (ZutiAircraft) o1;
            ZutiAircraft oo2 = (ZutiAircraft) o2;

            if (oo1 == null || oo2 == null) return 0;

            return oo1.getAcName().compareTo(oo2.getAcName());
        }
    }
    // ----------------------------------------------------------

    // ----------------------------------------------------------
    static class RRRItem_CompareByName implements Comparator {
        public int compare(Object o1, Object o2) {
            RRRItem oo1 = (RRRItem) o1;
            RRRItem oo2 = (RRRItem) o2;

            if (oo1 == null || oo2 == null) return 0;

            return oo1.name.compareTo(oo2.name);
        }
    }
    // ----------------------------------------------------------

    private static ArrayList           AIRDROME_POINTS = new ArrayList();
    public static DecimalFormat        DECIMAL_FORMAT  = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    // Tab Spawn/Radar
    private GWindowTabDialogClient.Tab tabSpawn;
    private GWindowLabel               lAirSpawn;
    private GWindowLabel               lRadar;
    // Tab capturing
    private GWindowTabDialogClient.Tab tabCapturing;
    private GWindowCheckBox            wZutiCaptureIfChiefPresent;
    private GWindowEditControl         wZutiCapturing_CapturedBasePlanesRed;
    private GWindowEditControl         wZutiCapturing_CapturedBasePlanesBlue;
    private GWindowButton              bShowAircraftsRED;
    private GWindowButton              bShowAircraftsBLUE;
    private GWindowEditControl         wZutiCapturing_RequiredParatroopers;
    private GWindowButton              bModifyCapturedRedCountries;
    private GWindowButton              bModifyCapturedBlueCountries;
    // Tab RRR
    private GWindowTabDialogClient.Tab tabRRR;
    private GWindowEditControl         wZutiOneMgCannonRearmSecond;
    private GWindowEditControl         wZutiOneBombFTankTorpedoeRearmSeconds;
    private GWindowEditControl         wZutiOneRocketRearmSeconds;
    private GWindowEditControl         wZutiGallonsLitersPerSecond;
    private GWindowEditControl         wZutiOneWeaponRepairSeconds;
    private GWindowEditControl         wZutiFlapsRepairSeconds;
    private GWindowCheckBox            wZutiRefuelOnlyIfFuelTanksExist;
    private GWindowCheckBox            wZutiRearmOnlyIfAmmoBoxesExist;
    private GWindowCheckBox            wZutiRepairOnlyIfWorkshopExist;
    private GWindowEditControl         wZutiEngineRepairSeconds;
    private GWindowEditControl         wZutiCockpitRepairSeconds;
    private GWindowEditControl         wZutiOneControlCableRepairSeconds;
    private GWindowEditControl         wZutiOneFuelOilTankRepairSeconds;
    private GWindowEditControl         wZutiLoadoutChangePenaltySeconds;
    private GWindowCheckBox            wZutiOnlyHomeBaseSpecificLoadouts;
    private GWindowCheckBox            wZutiReload_EnableResourcesManagement;

    // Separators
    private GWindowBoxSeparate               bSeparate_AirSpawn;
    private GWindowBoxSeparate               bSeparate_Radar;
    private GWindowBoxSeparate               bSeparate_Rearm;
    private GWindowLabel                     lSeparate_Rearm;
    private GWindowBoxSeparate               bSeparate_Refuel;
    private GWindowLabel                     lSeparate_Refuel;
    private GWindowBoxSeparate               bSeparate_Repair;
    private GWindowLabel                     lSeparate_Repair;
    private GWindowBoxSeparate               bSeparate_ShipSpawnPoints;
    private GWindowLabel                     lSeparate_ShipSpawnPoints;
    private GWindowBoxSeparate               bSeparate_Resources;
    private GWindowLabel                     lSeparate_Resources;

    protected static Zuti_WHomeBaseCountries HOME_BASE_COUNTRIES_WINDOW;
    public static final String               ALTERNATIVE_AIRFIELDS          = "AlternativeAirfield";
    public static final int                  BORN_PLACE_RADIUS_CARRIERS     = 4000000;// 2km
    private static final int                 BORN_PLACE_RADIUS_AIRFIELD     = 25000000; // 5km
    public static final int                  FRONT_MARKER_RADIUS            = 10000; // 100m*100m
    public static final float                FRONT_MARKER_RADIUS_SHIP_MULTI = 100F;
    public static final int                  HOME_BASE_Y_SEPARATION         = 10;

    /**
     * Reset class variables
     */
    public static void resetClassVariables() {
        AIRDROME_POINTS.clear();
    }

    /**
     * Saves country lit for specified born place and thus setting restrictions for country selection.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void saveBornPlaceCountries(ActorBorn actorborn, SectFile sectfile) {
        String i18nCountryName = null;
        String basicCountryName = null;

        if (actorborn.zutiHomeBaseCountries != null && actorborn.zutiHomeBaseCountries.size() > 0) {
            java.util.Collections.sort(actorborn.zutiHomeBaseCountries);

            int sectionId = sectfile.sectionAdd("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_Countries");
            for (int i = 0; i < actorborn.zutiHomeBaseCountries.size(); i++) {
                i18nCountryName = (String) actorborn.zutiHomeBaseCountries.get(i);
                basicCountryName = I18N.getCountryKey(i18nCountryName);
                // System.out.println("Country i18n name: " + i18nCountryName + ", generic key: " + basicCountryName);
                sectfile.lineAdd(sectionId, basicCountryName);
            }
        }

        if (actorborn.zutiHomeBaseCapturedRedCountries != null && actorborn.zutiHomeBaseCapturedRedCountries.size() > 0) {
            java.util.Collections.sort(actorborn.zutiHomeBaseCapturedRedCountries);

            int sectionId = sectfile.sectionAdd("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedCountries_Red");
            for (int i = 0; i < actorborn.zutiHomeBaseCapturedRedCountries.size(); i++) {
                i18nCountryName = (String) actorborn.zutiHomeBaseCapturedRedCountries.get(i);
                basicCountryName = I18N.getCountryKey(i18nCountryName);
                sectfile.lineAdd(sectionId, basicCountryName);
            }
        }

        if (actorborn.zutiHomeBaseCapturedBlueCountries != null && actorborn.zutiHomeBaseCapturedBlueCountries.size() > 0) {
            java.util.Collections.sort(actorborn.zutiHomeBaseCapturedBlueCountries);

            int sectionId = sectfile.sectionAdd("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedCountries_Blue");
            for (int i = 0; i < actorborn.zutiHomeBaseCapturedBlueCountries.size(); i++) {
                i18nCountryName = (String) actorborn.zutiHomeBaseCapturedBlueCountries.get(i);
                basicCountryName = I18N.getCountryKey(i18nCountryName);
                sectfile.lineAdd(sectionId, basicCountryName);
            }
        }
    }

    private static List getSTDSpawnPlaceholders(Point3d inPosition, int r) {
        List results = new ArrayList();
        int maxRange = r * r;
        ArrayList plugins = Plugin.zutiGetAllActors();
        Actor actor = null;
        Object object = null;
        PlMisStatic pluginStatic = null;
        ArrayList actors = null;
        Point3d point = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            // System.out.println("PlMisBorn - " + object.getClass() + ", " + object.getClass().getSuperclass());

            if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                actors = pluginStatic.allActors;

                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);

                    // System.out.println(" SpawnPlaceHolder army: "+ actor.getArmy() + " vs " + ZutiSupportMethods_Builder.SPAWN_PLACE_HOLDER_SAVED_ARMY);
                    if (actor instanceof Spawnplaceholder && actor.getArmy() != ZutiSupportMethods_Builder.SPAWN_PLACE_HOLDER_SAVED_ARMY) {
                        point = actor.pos.getAbsPoint();
                        double tmpDistance = Math.pow(point.x - inPosition.x, 2) + Math.pow(point.y - inPosition.y, 2);
                        if (tmpDistance <= maxRange) {
                            // note that this spawn place holder was already saved.
                            actor.setArmy(ZutiSupportMethods_Builder.SPAWN_PLACE_HOLDER_SAVED_ARMY);
                            results.add(new Loc(actor.pos.getAbsPoint(), actor.pos.getCurrentOrient()));
                        }
                    }
                }
            }
        }

        return results;
    }

    /**
     * Since during saving of spawn place holders their army is set to -1. After saving, clear it back to army 0.
     */
    public static void clearSTDSpawnPlaceholdersSavedStatus() {
        ArrayList plugins = Plugin.zutiGetAllActors();
        Actor actor = null;
        Object object = null;
        PlMisStatic pluginStatic = null;
        ArrayList actors = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            // System.out.println("PlMisBorn - " + object.getClass() + ", " + object.getClass().getSuperclass());

            if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                actors = pluginStatic.allActors;

                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);

                    if (actor instanceof Spawnplaceholder) // Saved spawn place holders have their army set to SPAWN_PLACE_HOLDER_SAVED_ARMY. Reset back to 0 once they are saved.
                        actor.setArmy(0);
                }
            }
        }
    }

    /**
     * Saves list of aircraft that will be loaded if born place is captured.
     */
    public static void saveBornPlaceCapturedPlanes(ActorBorn actorborn, SectFile sectfile) {
        if (actorborn.zutiCapturedAc_Red != null) {
            int sectionId = sectfile.sectionAdd("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedAc_Red");
            List acList = actorborn.zutiCapturedAc_Red.getAircraft();
            int count = acList.size();
            for (int i = 0; i < count; i++) {
                ZutiAircraft zac = (ZutiAircraft) acList.get(i);
                sectfile.lineAdd(sectionId, zac.getMissionLine(actorborn.zutiEnablePlaneLimits));
            }
        }

        if (actorborn.zutiCapturedAc_Blue != null) {
            int sectionId = sectfile.sectionAdd("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedAc_Blue");
            List acList = actorborn.zutiCapturedAc_Blue.getAircraft();
            int count = acList.size();
            for (int i = 0; i < count; i++) {
                ZutiAircraft zac = (ZutiAircraft) acList.get(i);
                sectfile.lineAdd(sectionId, zac.getMissionLine(actorborn.zutiEnablePlaneLimits));
            }
        }
    }

    /**
     * Loads list of countries that can be selected when selecting some born place. Countries for this method are saved in non-i18n string representation. Method does not clean existing country arrays.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void loadBornPlaceCountries_newMDS(ActorBorn actorborn, SectFile sectfile) {
        ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", com.maddox.rts.RTSConf.cur.locale, com.maddox.rts.LDRres.loader());

        String i18nCountryName = null;
        String basicCountryName = null;

        int count = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_Countries");
        if (count >= 0) {
            if (actorborn.zutiHomeBaseCountries == null) actorborn.zutiHomeBaseCountries = new ArrayList();

            int i_23_ = sectfile.vars(count);
            for (int i_24_ = 0; i_24_ < i_23_; i_24_++)
                try {
                    basicCountryName = sectfile.var(count, i_24_);
                    i18nCountryName = resourcebundle.getString(basicCountryName);
                    if (i18nCountryName != null && !actorborn.zutiHomeBaseCountries.contains(i18nCountryName)) actorborn.zutiHomeBaseCountries.add(i18nCountryName);
                } catch (Exception ex) {}
            // TODO: +++ Add Default Country "None" Mod by SAS~Storebror +++
            if (Config.cur.bAddDefaultCountryNone) {
                i18nCountryName = resourcebundle.getString("nn");
//                System.out.println("zutiHomeBaseCountries add default 1, i18nCountryName=" + i18nCountryName);
                if (i18nCountryName != null && !actorborn.zutiHomeBaseCountries.contains(i18nCountryName)) // System.out.println("zutiHomeBaseCountries add default 2");
                    actorborn.zutiHomeBaseCountries.add(i18nCountryName);
            }
            // TODO: --- Add Default Country "None" Mod by SAS~Storebror ---
        }

        count = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedCountries_Red");
        if (count >= 0) {
            if (actorborn.zutiHomeBaseCapturedRedCountries == null) actorborn.zutiHomeBaseCapturedRedCountries = new ArrayList();

            int i_23_ = sectfile.vars(count);
            for (int i_24_ = 0; i_24_ < i_23_; i_24_++)
                try {
                    basicCountryName = sectfile.var(count, i_24_);
                    i18nCountryName = resourcebundle.getString(basicCountryName);
                    if (i18nCountryName != null && !actorborn.zutiHomeBaseCapturedRedCountries.contains(i18nCountryName)) actorborn.zutiHomeBaseCapturedRedCountries.add(i18nCountryName);
                } catch (Exception ex) {}
            // TODO: +++ Add Default Country "None" Mod by SAS~Storebror +++
            if (Config.cur.bAddDefaultCountryNone) {
                i18nCountryName = resourcebundle.getString("nn");
//                System.out.println("zutiHomeBaseCapturedRedCountries add default 1, i18nCountryName=" + i18nCountryName);
                if (i18nCountryName != null && !actorborn.zutiHomeBaseCapturedRedCountries.contains(i18nCountryName)) // System.out.println("zutiHomeBaseCapturedRedCountries add default 2");
                    actorborn.zutiHomeBaseCapturedRedCountries.add(i18nCountryName);
            }
            // TODO: --- Add Default Country "None" Mod by SAS~Storebror ---
        }

        count = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedCountries_Blue");
        if (count >= 0) {
            if (actorborn.zutiHomeBaseCapturedBlueCountries == null) actorborn.zutiHomeBaseCapturedBlueCountries = new ArrayList();

            int i_23_ = sectfile.vars(count);
            for (int i_24_ = 0; i_24_ < i_23_; i_24_++)
                try {
                    basicCountryName = sectfile.var(count, i_24_);
                    i18nCountryName = resourcebundle.getString(basicCountryName);
                    if (i18nCountryName != null && !actorborn.zutiHomeBaseCapturedBlueCountries.contains(i18nCountryName)) actorborn.zutiHomeBaseCapturedBlueCountries.add(i18nCountryName);
                } catch (Exception ex) {}
            // TODO: +++ Add Default Country "None" Mod by SAS~Storebror +++
            if (Config.cur.bAddDefaultCountryNone) {
                i18nCountryName = resourcebundle.getString("nn");
//                System.out.println("zutiHomeBaseCapturedBlueCountries add default 1, i18nCountryName=" + i18nCountryName);
                if (i18nCountryName != null && !actorborn.zutiHomeBaseCapturedBlueCountries.contains(i18nCountryName)) // System.out.println("zutiHomeBaseCapturedBlueCountries add default 2");
                    actorborn.zutiHomeBaseCapturedBlueCountries.add(i18nCountryName);
            }
            // TODO: --- Add Default Country "None" Mod by SAS~Storebror ---
        }
    }

    /**
     * Loads list of countries that can be selected when selecting some born place. Countries for this method are saved in i18n string representation. Method also cleans existing country arrays.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void loadBornPlaceCountries_oldMDS(ActorBorn actorborn, SectFile sectfile) {
        int count = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_Countries");
        String country = null;
        if (count >= 0) {
            if (actorborn.zutiHomeBaseCountries == null) actorborn.zutiHomeBaseCountries = new ArrayList();

            actorborn.zutiHomeBaseCountries.clear();

            int i_23_ = sectfile.vars(count);
            for (int i_24_ = 0; i_24_ < i_23_; i_24_++)
                try {
                    country = sectfile.var(count, i_24_).replace('_', ' ');
                    if (!actorborn.zutiHomeBaseCountries.contains(country)) actorborn.zutiHomeBaseCountries.add(country);
                } catch (Exception ex) {}
            // TODO: +++ Add Default Country "None" Mod by SAS~Storebror +++
            if (Config.cur.bAddDefaultCountryNone) if (!actorborn.zutiHomeBaseCountries.contains("None")) // System.out.println("zutiHomeBaseCountries old add default");
                actorborn.zutiHomeBaseCountries.add("None");
        }

        count = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedCountries_Red");
        if (count >= 0) {
            if (actorborn.zutiHomeBaseCapturedRedCountries == null) actorborn.zutiHomeBaseCapturedRedCountries = new ArrayList();

            actorborn.zutiHomeBaseCapturedRedCountries.clear();

            int i_23_ = sectfile.vars(count);
            for (int i_24_ = 0; i_24_ < i_23_; i_24_++)
                try {
                    country = sectfile.var(count, i_24_).replace('_', ' ');
                    if (!actorborn.zutiHomeBaseCapturedRedCountries.contains(country)) actorborn.zutiHomeBaseCapturedRedCountries.add(country);
                } catch (Exception ex) {}
            // TODO: +++ Add Default Country "None" Mod by SAS~Storebror +++
            if (Config.cur.bAddDefaultCountryNone) if (!actorborn.zutiHomeBaseCapturedRedCountries.contains("None")) // System.out.println("zutiHomeBaseCapturedRedCountries old add default");
                actorborn.zutiHomeBaseCapturedRedCountries.add("None");
        }

        count = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedCountries_Blue");
        if (count >= 0) {
            if (actorborn.zutiHomeBaseCapturedBlueCountries == null) actorborn.zutiHomeBaseCapturedBlueCountries = new ArrayList();

            actorborn.zutiHomeBaseCapturedBlueCountries.clear();

            int i_23_ = sectfile.vars(count);
            for (int i_24_ = 0; i_24_ < i_23_; i_24_++)
                try {
                    country = sectfile.var(count, i_24_).replace('_', ' ');
                    if (!actorborn.zutiHomeBaseCapturedBlueCountries.contains(country)) actorborn.zutiHomeBaseCapturedBlueCountries.add(country);
                } catch (Exception ex) {}
            // TODO: +++ Add Default Country "None" Mod by SAS~Storebror +++
            if (Config.cur.bAddDefaultCountryNone) if (!actorborn.zutiHomeBaseCapturedBlueCountries.contains("None")) // System.out.println("zutiHomeBaseCapturedBlueCountries old add default");
                actorborn.zutiHomeBaseCapturedBlueCountries.add("None");
        }
    }

    /**
     * Load aircraft that are available once specified born place is overrun.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void loadBornPlaceCapturedPlanes(ActorBorn actorborn, SectFile sectfile) {
        // TODO: Added by |ZUTI|
        boolean zutiMdsSectionIdExists = sectfile.sectionIndex("MDS") > -1;

        int sectionIndex = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedAc_Red");
        if (sectionIndex >= 0) {
            int variablesCount = sectfile.vars(sectionIndex);
            for (int i = 0; i < variablesCount; i++) {
                String readLine = sectfile.line(sectionIndex, i);
                StringTokenizer stringtokenizer = new StringTokenizer(readLine);

                ZutiAircraft zac = new ZutiAircraft();
                ZutiSupportMethods.fillZutiAircraft(zac, stringtokenizer, zutiMdsSectionIdExists);
                String string = zac.getAcName();
                if (string != null) {
                    string = string.intern();
                    Class var_class = (Class) Property.value(string, "airClass", null);

                    if (var_class != null && Property.containsValue(var_class, "cockpitClass")) {
                        // Add this ac to modified table for this home base
                        if (actorborn.zutiCapturedAc_Red == null) actorborn.zutiCapturedAc_Red = new Zuti_WManageAircrafts();

                        if (!actorborn.zutiEnablePlaneLimits) zac.setNoLimitations();

                        actorborn.zutiCapturedAc_Red.addAircraft(zac);
                        actorborn.zutiBaseCapturedRedPlanes += string + " ";
                    }
                }
            }
        }

        sectionIndex = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_CapturedAc_Blue");
        if (sectionIndex >= 0) {
            int variablesCount = sectfile.vars(sectionIndex);
            for (int i = 0; i < variablesCount; i++) {
                String readLine = sectfile.line(sectionIndex, i);
                StringTokenizer stringtokenizer = new StringTokenizer(readLine);

                ZutiAircraft zac = new ZutiAircraft();
                ZutiSupportMethods.fillZutiAircraft(zac, stringtokenizer, zutiMdsSectionIdExists);
                String string = zac.getAcName();
                if (string != null) {
                    string = string.intern();
                    Class var_class = (Class) Property.value(string, "airClass", null);
                    if (var_class != null && Property.containsValue(var_class, "cockpitClass")) {
                        // Add this ac to modified table for this home base
                        if (actorborn.zutiCapturedAc_Blue == null) actorborn.zutiCapturedAc_Blue = new Zuti_WManageAircrafts();

                        if (!actorborn.zutiEnablePlaneLimits) zac.setNoLimitations();

                        actorborn.zutiCapturedAc_Blue.addAircraft(zac);
                        actorborn.zutiBaseCapturedBluePlanes += string + " ";
                    }
                }
            }
        }
    }

    /**
     * Loads born place specific RRR settings.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void loadBornPlaceRRR(ActorBorn actorborn, SectFile sectfile) {
        int sectionId = sectfile.sectionIndex("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_RRR");
        if (sectionId >= 0) {
            int lines = sectfile.vars(sectionId);
            if (lines > -1) try {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(sectionId, 0));
                actorborn.zutiOverrideDefaultRRRSettings = false;
                if (numbertokenizer.next(0, 0, 1) == 1) actorborn.zutiOverrideDefaultRRRSettings = true;

                actorborn.zutiOneMgCannonRearmSecond = numbertokenizer.next(10, 0, 99999);
                actorborn.zutiOneBombFTankTorpedoeRearmSeconds = numbertokenizer.next(25, 0, 99999);
                actorborn.zutiOneRocketRearmSeconds = numbertokenizer.next(20, 0, 99999);
                actorborn.zutiLoadoutChangePenaltySeconds = numbertokenizer.next(30, 0, 99999);
                actorborn.zutiOnlyHomeBaseSpecificLoadouts = true;
                if (numbertokenizer.next(1, 0, 1) == 0) actorborn.zutiOnlyHomeBaseSpecificLoadouts = false;
                actorborn.zutiRearmOnlyIfAmmoBoxesExist = false;
                if (numbertokenizer.next(0, 0, 1) == 1) actorborn.zutiRearmOnlyIfAmmoBoxesExist = true;

                actorborn.zutiGallonsLitersPerSecond = numbertokenizer.next(3, 0, 99999);
                actorborn.zutiRefuelOnlyIfFuelTanksExist = false;
                if (numbertokenizer.next(0, 0, 1) == 1) actorborn.zutiRefuelOnlyIfFuelTanksExist = true;

                actorborn.zutiEngineRepairSeconds = numbertokenizer.next(90, 0, 99999);
                actorborn.zutiOneControlCableRepairSeconds = numbertokenizer.next(15, 0, 99999);
                actorborn.zutiFlapsRepairSeconds = numbertokenizer.next(30, 0, 99999);
                actorborn.zutiOneWeaponRepairSeconds = numbertokenizer.next(3, 0, 99999);
                actorborn.zutiCockpitRepairSeconds = numbertokenizer.next(30, 0, 99999);
                actorborn.zutiOneFuelOilTankRepairSeconds = numbertokenizer.next(20, 0, 99999);
                actorborn.zutiRepairOnlyIfWorkshopExist = false;
                if (numbertokenizer.next(0, 0, 1) == 1) actorborn.zutiRepairOnlyIfWorkshopExist = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Saves born place specific RRR settings.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void saveBornPlaceRRR(ActorBorn actorborn, SectFile sectfile) {
        int sectionId = sectfile.sectionAdd("MDS_BornPlace_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y + "_RRR");

        java.lang.StringBuffer sb = new java.lang.StringBuffer();

        sb.append(ZutiSupportMethods.boolToInt(actorborn.zutiOverrideDefaultRRRSettings));
        sb.append(" ");

        sb.append(actorborn.zutiOneMgCannonRearmSecond);
        sb.append(" ");
        sb.append(actorborn.zutiOneBombFTankTorpedoeRearmSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiOneRocketRearmSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiLoadoutChangePenaltySeconds);
        sb.append(" ");
        sb.append(ZutiSupportMethods.boolToInt(actorborn.zutiOnlyHomeBaseSpecificLoadouts));
        sb.append(" ");
        sb.append(ZutiSupportMethods.boolToInt(actorborn.zutiRearmOnlyIfAmmoBoxesExist));
        sb.append(" ");

        sb.append(actorborn.zutiGallonsLitersPerSecond);
        sb.append(" ");
        sb.append(ZutiSupportMethods.boolToInt(actorborn.zutiRefuelOnlyIfFuelTanksExist));
        sb.append(" ");

        sb.append(actorborn.zutiEngineRepairSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiOneControlCableRepairSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiFlapsRepairSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiOneWeaponRepairSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiCockpitRepairSeconds);
        sb.append(" ");
        sb.append(actorborn.zutiOneFuelOilTankRepairSeconds);
        sb.append(" ");
        sb.append(ZutiSupportMethods.boolToInt(actorborn.zutiRepairOnlyIfWorkshopExist));

        sectfile.lineAdd(sectionId, sb.toString());
    }

    /**
     * Synchronizes specified list resulting in a list without any duplicates.
     *
     * @param airNames
     * @param modifiedAircrafts
     * @return
     */
    public static ArrayList syncLists(ArrayList airNames, ArrayList modifiedAircrafts) {
        // This one can be done cleaner, I'm sure. I just have no time.
        // Sync airNames list with modified planes list.
        ArrayList defaultPlanes = new ArrayList();

        for (int i = 0; i < airNames.size(); i++) {
            String acName = (String) airNames.get(i);
            boolean found = false;
            for (int j = 0; j < modifiedAircrafts.size(); j++) {
                ZutiAircraft zac = (ZutiAircraft) modifiedAircrafts.get(j);

                // System.out.println(acName + " vs " + zac.getAcName());

                if (zac.getAcName().equals(acName)) {
                    found = true;
                    break;
                }
            }

            if (!found) // Make default zac
                defaultPlanes.add(ZutiSupportMethods_Air.createDefaultZutiAircraft(acName));
        }

        for (int i = 0; i < defaultPlanes.size(); i++)
            modifiedAircrafts.add(defaultPlanes.get(i));

        // now, remove those planes that are in modified list but were removed from airNames list
        defaultPlanes.clear();
        for (int i = 0; i < modifiedAircrafts.size(); i++) {
            ZutiAircraft zac = (ZutiAircraft) modifiedAircrafts.get(i);
            boolean exists = false;
            for (int j = 0; j < airNames.size(); j++) {
                String acName = (String) airNames.get(j);
                if (zac.getAcName().equals(acName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) defaultPlanes.add(zac);
        }

        for (int i = 0; i < defaultPlanes.size(); i++)
            modifiedAircrafts.remove(defaultPlanes.get(i));

        return modifiedAircrafts;
    }

    /**
     * Method returns integer representation for specified string value. If string can not be processed as int, 0 is returned.
     *
     * @param value
     * @return
     */
    public static int getIntValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    /**
     * Based on specified inPoint method finds closest carrier. If this carrier exists it then fills combo with available number of spawn points for found carrier designation.
     *
     * @param shipDesignation
     * @param combo
     */
    public static int getCarrierSpawnPointsBasedOnyCarrierType(Point3d inPoint3d) {
        if (inPoint3d == null) return 0;

        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();

        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        Actor actor = null;
        Object object = null;
        Object aobj[] = null;
        Point3d point3d = null;
        double closest = ZutiSupportMethods_Builder.BORN_PLACE_RADIUS_CARRIERS;
        String chiefName = null;
        String className = null;
        Actor shipActor = null;
        ZutiTypeAircraftCarrier closestCarrier = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);

            if (object instanceof PlMisChief) {
                PlMisChief plMisChief = (PlMisChief) object;
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    if (actor instanceof PathChief) {
                        pc = (PathChief) actor;
                        int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
                        switch (pcType) {
                            case 0: // Infantry
                            case 1: // Armor
                            case 2: // Vehicle
                            case 3: // Train
                                break;
                            case 4:
                            case 5: {
                                chiefName = plMisChief.type[pc._iType].item[pc._iItem].name;
                                try {
                                    className = "com.maddox.il2.objects.ships.Ship$" + chiefName;
                                    shipActor = ZutiSupportMethods_Builder.getActorFromClassName(className);
                                    if (shipActor instanceof ZutiTypeAircraftCarrier) {
                                        // Current actor is aircraft carrier
                                        point3d = pc.point(0).pos.getAbsPoint();
                                        // System.out.println(" Found carrier at position x=" + point3d.x + ", y=" + point3d.y);
                                        double d = (inPoint3d.x - point3d.x) * (inPoint3d.x - point3d.x) + (inPoint3d.y - point3d.y) * (inPoint3d.y - point3d.y);
                                        if (d < closest) {
                                            // too far away, just set the bugger at current location!
                                            closest = d;
                                            closestCarrier = (ZutiTypeAircraftCarrier) shipActor;
                                            // System.out.println(" Currently closest: "+ closestChiefName);
                                        }
                                    }
                                } catch (Exception ex) {}
                            }
                        }
                    }
                }
            } else if (object instanceof PlMisStatic) {
                PlMisStatic pluginStatic = (PlMisStatic) object;
                ArrayList actors = pluginStatic.allActors;

                for (int j = 0; j < actors.size(); j++) {
                    actor = (Actor) actors.get(j);

                    if (actor instanceof ZutiTypeAircraftCarrier) try {
                        shipActor = ZutiSupportMethods_Builder.getActorFromClassName(actor.getClass().getName());
                        // Current actor is aircraft carrier
                        point3d = actor.pos.getAbsPoint();
                        // System.out.println(" Found carrier at position x=" + point3d.x + ", y=" + point3d.y);
                        double d = (inPoint3d.x - point3d.x) * (inPoint3d.x - point3d.x) + (inPoint3d.y - point3d.y) * (inPoint3d.y - point3d.y);
                        if (d < closest) {
                            // too far away, just set the bugger at current location!
                            closest = d;
                            closestCarrier = (ZutiTypeAircraftCarrier) shipActor;
                            // System.out.println(" Currently closest: "+ closestChiefName);
                        }
                    } catch (Exception ex) {}
                }
            }
        }

        if (closestCarrier != null) // System.out.println(" Number of spawn points for selected carrier = " + closestCarrier.getSpawnPoints());
            return closestCarrier.getSpawnPoints();

        return 0;
        /*
         * String shipDesignation = ZutiSupportMethods_Builder.getClosestMovingCarrier_Name(inPoint3d, ZutiSupportMethods_Builder.BORN_PLACE_RADIUS_CARRIERS); if( shipDesignation == null ) { shipDesignation =
         * ZutiSupportMethods_Builder.getClosestStaticCarrier_Name(inPoint3d, ZutiSupportMethods_Builder.BORN_PLACE_RADIUS_CARRIERS); if( shipDesignation == null ) return 0; }
         *
         * //System.out.println("Found ship for given coordinates: "+ shipDesignation); if( shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[0]) > -1 ||
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[1]) > -1 || shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[2]) > -1 ) { return 6; } else if(
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[3]) > -1 || shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[4]) > -1 ) { return 5; } else if(
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[5]) > -1 || shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[6]) > -1 ||
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[18]) > -1 ) { return 2; } else if( shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[7]) > -1 ) { return 7; } else if(
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[8]) > -1 || shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[9]) > -1 ||
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[13]) > -1 || shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[14]) > -1 ) { return 5; } else if(
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[10]) > -1 || shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[15]) > -1 ) { return 8; } else if(
         * shipDesignation.indexOf(ZutiSupportMethods_Ships.ZUTI_CARRIER_SUBCLASS_STRING[11]) > -1 ) { return 6; }
         *
         * return 0;
         */
    }

    /**
     * Goes through all Actor Born actors and checks if any already exists at given coordinates
     *
     * @param allBornPlaces
     * @param x
     * @param y
     * @return
     */
    public static boolean isBornPlaceExistingAtCoordinates(List allBornPlaces, double x, double y) {
        ActorBorn ab = null;
        for (int i = 0; i < allBornPlaces.size(); i++) {
            ab = (ActorBorn) allBornPlaces.get(i);
            // System.out.println("x1=" + ab.pos.getAbsPoint().x + " vs " + x + ", y1=" + (ab.pos.getAbsPoint().y - ZutiSupportMethods_Builder.HOME_BASE_Y_SEPARATION) + " vs " +y);

            // TODO: Fixed by SAS~Storebror: Replace test for floating point equality by equality within a certain range, due to rounding errors in floating point calculations.
//            if (ab.pos.getAbsPoint().x == x && ab.pos.getAbsPoint().y - ZutiSupportMethods_Builder.HOME_BASE_Y_SEPARATION == y) return true;
//            if (Math.abs(ab.pos.getAbsPoint().x - x) < .0000001D && Math.abs(ab.pos.getAbsPoint().y - ZutiSupportMethods_Builder.HOME_BASE_Y_SEPARATION - y) < .0000001D) return true;
            if (CommonTools.equals(ab.pos.getAbsPoint().x, x) && CommonTools.equals(ab.pos.getAbsPoint().y - ZutiSupportMethods_Builder.HOME_BASE_Y_SEPARATION, y, 7)) return true;
        }

        return false;
    }

    public static boolean isStdBaseOnCarrier(List allActors, Point3d point3d, float radius) {
        // First, check for nearby aircraft carriers
        Point3d carrierPos = ZutiSupportMethods_Builder.getClosestMovingCarrier_Position(allActors, point3d, radius);
        if (carrierPos != null) return true;

        return false;
    }

    /**
     * Method checks for nearby airfield closes airfield to specified coordinate.
     *
     * @param allActors:
     * @param point3d
     * @return
     */
    public static boolean canPlaceBornPlaceAtLocation(List allActors, Point3d point3d, float radius, boolean isStandAlone) {
        point3d.z = 0.0D;

        // First, check for nearby aircraft carriers
        Point3d carrierPos = ZutiSupportMethods_Builder.getClosestMovingCarrier_Position(allActors, point3d, BORN_PLACE_RADIUS_CARRIERS);
        if (carrierPos != null) if (isStandAlone) {
            new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("mds.section.error"), Plugin.i18n("mds.section.NoSTDOnCarriers"), 3, 0.0F);
            return false;
        } else {
            point3d.set(carrierPos);
            return true;
        }

        com.maddox.il2.ai.Airport airport = com.maddox.il2.ai.Airport.nearest(point3d, -1, 7);

        if (airport != null) {
            // check if airport found is too far away. Say, it's more than 5km away... that's too much for us ;)
            Point3d airportPoint = airport.pos.getAbsPoint();

            double d = (airportPoint.x - point3d.x) * (airportPoint.x - point3d.x) + (airportPoint.y - point3d.y) * (airportPoint.y - point3d.y);
            if (d > ZutiSupportMethods_Builder.BORN_PLACE_RADIUS_AIRFIELD) // too far away, just set the bugger at current location!
                return true;

            point3d.set(airport.pos.getAbsPoint());

            for (int i = 0; i < allActors.size(); i++) {
                com.maddox.il2.builder.ActorBorn actorborn = (com.maddox.il2.builder.ActorBorn) allActors.get(i);
                com.maddox.JGP.Point3d point3d1 = actorborn.pos.getAbsPoint();
                d = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
                if (d < 100D) return false;
            }

            return true;
        } else // Well, no airports near by, just set the damn thing wherever user wants
            return true;
    }

    /**
     * Method returns location of closest aircraft carrier found near specified inPoint.
     */
    private static Point3d getClosestMovingCarrier_Position(List allBornPlaces, Point3d inPoint, float radious) {
        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();

        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        Actor actor = null;
        Object object = null;
        Object aobj[] = null;
        Point3d point3d = null;
        double closest = radious;
        Point3d closestPoint = null;
        String className = null;
        String chiefName = null;
        Actor shipActor = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);

            if (object instanceof PlMisChief) {
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    if (actor instanceof PathChief) {
                        pc = (PathChief) actor;
                        // System.out.println("SHIP NAME = " + actor.toString() + ", TYPE: " + pc._iType + ", INC = " + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT);
                        int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
                        switch (pcType) {
                            case 0: // Infantry
                            case 1: // Armor
                            case 2: // Vehicle
                            case 3: // Train
                                break;
                            case 4:
                            case 5: {
                                if (pc.zutiUnitsNames != null && pc.zutiUnitsNames.length > 0) chiefName = pc.zutiUnitsNames[0];
                                else chiefName = Property.stringValue(pc, "i18nName", "");

                                className = "com.maddox.il2.objects.ships.Ship$" + chiefName;
                                shipActor = ZutiSupportMethods_Builder.getActorFromClassName(className);
                                if (shipActor instanceof ZutiTypeAircraftCarrier) {
                                    // Current actor is aircraft carrier
                                    point3d = pc.point(0).pos.getAbsPoint();
                                    // System.out.println(" Found carrier at position x=" + point3d.x + ", y=" + point3d.y);
                                    double d = (inPoint.x - point3d.x) * (inPoint.x - point3d.x) + (inPoint.y - point3d.y) * (inPoint.y - point3d.y);
                                    if (d < closest && !isBornPlaceExistingAtCoordinates(allBornPlaces, point3d.x, point3d.y + ZutiSupportMethods_Builder.HOME_BASE_Y_SEPARATION)) {
                                        // too far away, just set the bugger at current location!
                                        closestPoint = point3d;
                                        closest = d;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return closestPoint;
    }

    /**
     * Method returns location of closest actor suitable to carry front marker found near specified inPoint.
     */
    public static Point3d getClosestFrontMarkerCarrier(Point3d inPoint, float maxDistance, int army) {
        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();

        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        Actor actor = null;
        Object object = null;
        Object aobj[] = null;
        Point3d point3d = null;
        double closest = maxDistance;
        Point3d closestPoint = null;
        // String chiefName = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);

            if (object instanceof PlMisChief) {
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];

                    if (actor.getArmy() != army) continue;

                    if (actor instanceof PathChief) {
                        pc = (PathChief) actor;
                        int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
                        switch (pcType) {
                            // chiefName = Property.stringValue(pc, "i18nName", "");
                            case 1: // Armor
                            {
                                // Current actor is aircraft carrier
                                point3d = pc.point(0).pos.getAbsPoint();
                                // System.out.println(" Found armor >" + chiefName + "< at position x=" + point3d.x + ", y=" + point3d.y);
                                double d = (inPoint.x - point3d.x) * (inPoint.x - point3d.x) + (inPoint.y - point3d.y) * (inPoint.y - point3d.y);
                                if (d < closest) {
                                    // too far away, just set the bugger at current location!
                                    closestPoint = point3d;
                                    closest = d;
                                }
                                break;
                            }
                            case 2: // Vehicle
                            {
                                // Current actor is aircraft carrier
                                point3d = pc.point(0).pos.getAbsPoint();
                                // System.out.println(" Found vehicle >" + chiefName + "< at position x=" + point3d.x + ", y=" + point3d.y);
                                double d = (inPoint.x - point3d.x) * (inPoint.x - point3d.x) + (inPoint.y - point3d.y) * (inPoint.y - point3d.y);
                                if (d < closest) {
                                    // too far away, just set the bugger at current location!
                                    closestPoint = point3d;
                                    closest = d;
                                }
                                break;
                            }
                            case 3: // Train
                                break;
                            case 4:
                            case 5: {
                                // Current actor is aircraft carrier
                                point3d = pc.point(0).pos.getAbsPoint();
                                // System.out.println(" Found ship >" + chiefName + "< at position x=" + point3d.x + ", y=" + point3d.y);
                                double d = (inPoint.x - point3d.x) * (inPoint.x - point3d.x) + (inPoint.y - point3d.y) * (inPoint.y - point3d.y);
                                if (d < closest) {
                                    // too far away, just set the bugger at current location!
                                    closestPoint = point3d;
                                    closest = d;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return closestPoint;
    }

    /**
     * Loads list of countries that can be selected when selecting some born place.
     *
     * @param sectfile
     * @param merge    If true loaded spawn places are merged with existing ones loaded from actors.static file.
     */
    public static List loadSpawnPointsForSTDBornPlace(SectFile sectfile, boolean merge) {
        List points = new ArrayList();
        int sectionIndex = sectfile.sectionIndex("MDS_STDBornPlace_SpawnPlaces");
        if (sectionIndex >= 0) {
            int sectionVars = sectfile.vars(sectionIndex);
            StringTokenizer st = null;
            String token = null;
            Point3d point = null;
            float azimut = 0.0F;
            float tangage = 0.0F;
            float kren = 0.0F;
            String line = null;
            for (int i = 0; i < sectionVars; i++)
                try {
                    line = sectfile.line(sectionIndex, i);
                    st = new StringTokenizer(line);
                    point = new Point3d();
                    azimut = 0.0F;
                    tangage = 0.0F;
                    kren = 0.0F;
                    // System.out.println("ZutiSupportMethods_Builder - tokenising: " + line);
                    int counter = 0;
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        switch (counter) {
                            case 0:
                                point.x = Double.parseDouble(token);
                                break;
                            case 1:
                                point.y = Double.parseDouble(token);
                                // System.out.println("zutiSupportMethods_Builder = " + point.y);
                                break;
                            case 2:
                                point.z = Double.parseDouble(token);
                                break;
                            case 3:
                                azimut = Float.parseFloat(token);
                                break;
                            case 4:
                                tangage = Float.parseFloat(token);
                                break;
                            case 5:
                                kren = Float.parseFloat(token);
                                break;
                        }
                        counter++;
                    }

                    Point_Stay ps = new Point_Stay((float) point.x, (float) point.y);
                    ps.zutiLocation = new Loc(point, new Orient(azimut, tangage, kren));
                    points.add(new Point_Stay[] { ps });
                } catch (Exception ex) {}

            if (merge) {
                // Add found points to existing ones
                printDebugMessage("ZutiSupportMethods_Builder - stay points before merge: " + World.cur().airdrome.stay.length);
                World.cur().airdrome.stay = ZutiSupportMethods.mergeArrays(World.cur().airdrome.stay, points);
                printDebugMessage("ZutiSupportMethods_Builder - stay points after merge: " + World.cur().airdrome.stay.length);
            }
        }

        return points;
    }

    /**
     * Method first executes loadSpawnPointsForSTDBornPlace method and then takes it's results, adds necessary entries so that resulting line can then be inserted into numbers tokenizer in PlMisStatic load method.
     *
     * @param sectfile
     * @return
     */
    public static List getPreparedSpawnPlacePlaceHolders(SectFile sectfile, int index) {
        List results = new ArrayList();

        List spawnPlacePlaceHolders = ZutiSupportMethods_Builder.loadSpawnPointsForSTDBornPlace(sectfile, false);
        Point_Stay[] pss = null;
        Point_Stay ps = null;
        StringBuffer sb = null;
        for (int x = 0; x < spawnPlacePlaceHolders.size(); x++)
            try {
                pss = (Point_Stay[]) spawnPlacePlaceHolders.get(x);
                ps = pss[0];

                sb = new StringBuffer();
                sb.append(index);
                sb.append("_Static");
                sb.append(" ");
                sb.append("vehicles.planes.Plane$Spawnplaceholder");
                sb.append(" 0 ");
                sb.append(ps.x);
                sb.append(" ");
                sb.append(ps.y);
                sb.append(" ");
                sb.append(ps.zutiLocation.getOrient().azimut());
                sb.append(" 0.0 null");

                // System.out.println("ZutiSupportMethods_Builder - generated spawn place place holder: >" + sb.toString() + "<");
                results.add(sb.toString());

                index++;
            } catch (Exception ex) {}

        return results;
    }

    /**
     * Method saves all spawn points defined by STD home places.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void saveSpawnPointholdersForSTDBornPlace(ActorBorn actorborn, SectFile sectfile) {
        if (actorborn.zutiIsStandAloneBornPlace) {
            DECIMAL_FORMAT.applyLocalizedPattern("#.##");
            int sectionIndex = sectfile.sectionIndex("MDS_STDBornPlace_SpawnPlaces");
            if (sectionIndex < 0) sectionIndex = sectfile.sectionAdd("MDS_STDBornPlace_SpawnPlaces");

            List spawnPoints = ZutiSupportMethods_Builder.getSTDSpawnPlaceholders(actorborn.pos.getAbsPoint(), actorborn.r);
            Loc loc = null;
            double[] data = null;
            for (int i = 0; i < spawnPoints.size(); i++) {
                loc = (Loc) spawnPoints.get(i);
                if (loc != null) {
                    data = new double[6];
                    loc.get(data);
                    if (data != null) sectfile.lineAdd(sectionIndex,
                            DECIMAL_FORMAT.format(data[0]) + " " + DECIMAL_FORMAT.format(data[1]) + " " + DECIMAL_FORMAT.format(data[2]) + " " + DECIMAL_FORMAT.format(data[3]) + " " + DECIMAL_FORMAT.format(data[4]) + " " + DECIMAL_FORMAT.format(data[5]));
                }
            }
        }
    }

    /**
     * Method creates spawn tab for home base object.
     *
     * @param plMisBorn
     */
    public static GWindowTabDialogClient.Tab getPlMisBornSpawnTab() {
        ZutiSupportMethods_Builder plMisBorn = new ZutiSupportMethods_Builder();

        GWindowDialogClient gwindowdialogclient_Spawn = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        plMisBorn.tabSpawn = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("mds.tabSpawn"), gwindowdialogclient_Spawn);

        gwindowdialogclient_Spawn.addLabel(plMisBorn.lAirSpawn = new GWindowLabel(gwindowdialogclient_Spawn, 3.0F, 0.5F, 11.0F, 1.3F, Plugin.i18n("mds.spawn"), null));
        plMisBorn.bSeparate_AirSpawn = new GWindowBoxSeparate(gwindowdialogclient_Spawn, 1.0F, 1.0F, 37.0F, 11.0F);
        plMisBorn.bSeparate_AirSpawn.exclude = plMisBorn.lAirSpawn;

        // wMaxPilots
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 2.0F, 11.0F, 1.3F, Plugin.i18n("mds.spawn.maxPilots"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 2.0F, 5.0F, 1.3F, "Max number of pilots that can spawn on this home base.") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiMaxBasePilots).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiMaxBasePilots = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wHeight
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 4.0F, 36.0F, 1.3F, Plugin.i18n("mds.spawn.height"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 4.0F, 2.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 4.0F, 5.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiSpawnHeight).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiSpawnHeight = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wSpeed
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 6.0F, 11.0F, 1.3F, Plugin.i18n("mds.spawn.speed"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 6.0F, 6.0F, 1.3F, Plugin.i18n("mds.kmh"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 6.0F, 5.0F, 1.3F, "Speed in km/h") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiSpawnSpeed).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiSpawnSpeed = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wOrient
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 8.0F, 11.0F, 1.3F, Plugin.i18n("mds.spawn.orient"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 8.0F, 2.0F, 1.3F, Plugin.i18n("mds.degrees"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 8.0F, 5.0F, 1.3F, "Orientation in degrees.") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiSpawnOrient).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiSpawnOrient = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wAlwaysAirSpawn
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 10.0F, 11.0F, 1.3F, Plugin.i18n("mds.spawn.alwaysAirSpawn"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowCheckBox(gwindowdialogclient_Spawn, 34.0F, 10.0F, null) {
            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiAirspawnOnly, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiAirspawnOnly = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        gwindowdialogclient_Spawn.addLabel(plMisBorn.lSeparate_ShipSpawnPoints = new GWindowLabel(gwindowdialogclient_Spawn, 3.0F, 13.5F, 24.0F, 1.3F, Plugin.i18n("mds.shipSpawnPoints"), null));
        plMisBorn.bSeparate_ShipSpawnPoints = new GWindowBoxSeparate(gwindowdialogclient_Spawn, 1.0F, 14.0F, 37.0F, 11.0F);
        plMisBorn.bSeparate_ShipSpawnPoints.exclude = plMisBorn.lSeparate_ShipSpawnPoints;

        // wNrOfSpawnPlaces
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 15.0F, 30.0F, 1.3F, Plugin.i18n("mds.shipSpawnPointsNr"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowComboControl(gwindowdialogclient_Spawn, 30.0F, 15.0F, 5.0F) {
            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
            }

            public void preRender() {
                if (this.size() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();

                if (actorborn.zutiCarrierSpawnPoints < 1) return;

                for (int i = 0; i < actorborn.zutiCarrierSpawnPoints; i++)
                    this.add(new Integer(i + 1).toString());

                if (actorborn.zutiCarrierSelectedSpawnPointsIndex > this.size() || actorborn.zutiCarrierSelectedSpawnPointsIndex - 1 < 0) this.setSelected(this.size() - 1, true, false);
                else this.setSelected(actorborn.zutiCarrierSelectedSpawnPointsIndex - 1, true, false);
            }

            public boolean notify(int i_48_, int i_49_) {
                if (i_48_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();

                if (actorborn.zutiCarrierSpawnPoints < 1) return false;

                actorborn.zutiCarrierSelectedSpawnPointsIndex = this.getSelected() + 1;
                PlMission.setChanged();
                return false;
            }
        });

        // wEnableQueue
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 17.0F, 30.0F, 1.3F, Plugin.i18n("mds.shipSpawnPointsQueue"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowCheckBox(gwindowdialogclient_Spawn, 34.0F, 17.0F, null) {
            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiEnableQueue, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiEnableQueue = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        // wDeckClearTimeout
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 3.0F, 19.0F, 30.0F, 1.3F, Plugin.i18n("mds.shipSpawnPointsDeckTime"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 19.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 19.0F, 5.0F, 1.3F, "Determines how fast the pilots have to clear the deck after they spawn.") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (actorborn != null) this.setEnable(actorborn.zutiEnableQueue);

                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                this.setValue(new Integer(actorborn.zutiDeckClearTimeout).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiDeckClearTimeout = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wAirspawnIfQueueFull
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 3.0F, 21.0F, 30.0F, 1.3F, Plugin.i18n("mds.shipAirspawnIfQueueFull"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowCheckBox(gwindowdialogclient_Spawn, 34.0F, 21.0F, null) {
            public void preRender() {
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (actorborn != null) this.setEnable(actorborn.zutiEnableQueue);

                super.preRender();

                // TODO: Added by SAS~Storebror to avoid null dereference
                if (actorborn != null) this.setChecked(actorborn.zutiAirspawnIfQueueFull, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiAirspawnIfQueueFull = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        // wPlayerAcInvulnerableWhileOnTheDeck
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 23.0F, 30.0F, 1.3F, Plugin.i18n("mds.shipPAcInvulnerableAir"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowCheckBox(gwindowdialogclient_Spawn, 34.0F, 23.0F, null) {
            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiPilotInVulnerableWhileOnTheDeck, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiPilotInVulnerableWhileOnTheDeck = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        // gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 1.0F, 13.0F, 37.0F, 1.3F, "-------------------------------------------------------------------------------------------------------------------------------", null));
        gwindowdialogclient_Spawn.addLabel(plMisBorn.lRadar = new GWindowLabel(gwindowdialogclient_Spawn, 3.0F, 26.5F, 13.0F, 1.3F, Plugin.i18n("mds.radar"), null));
        plMisBorn.bSeparate_Radar = new GWindowBoxSeparate(gwindowdialogclient_Spawn, 1.0F, 27.0F, 37.0F, 7.0F);
        plMisBorn.bSeparate_Radar.exclude = plMisBorn.lRadar;

        // wZutiRadar_Range
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 28.0F, 30.0F, 1.3F, Plugin.i18n("mds.radar.range"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 28.0F, 5.0F, 1.3F, Plugin.i18n("mds.kilometer"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 28.0F, 5.0F, 1.3F, "Range in km") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiRadarRange).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (this.getValue().trim().length() < 1) return false;
                actorborn.zutiRadarRange = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wZutiRadar_Min
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 30.0F, 30.0F, 1.3F, Plugin.i18n("mds.radar.minHeight"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 30.0F, 5.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 30.0F, 5.0F, 1.3F, "Height in meters.") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiRadarHeight_MIN).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (this.getValue().trim().length() < 1) return false;
                actorborn.zutiRadarHeight_MIN = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wZutiRadar_Max
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 2.0F, 32.0F, 30.0F, 1.3F, Plugin.i18n("mds.radar.maxHeight"), null));
        gwindowdialogclient_Spawn.addLabel(new GWindowLabel(gwindowdialogclient_Spawn, 35.5F, 32.0F, 5.0F, 1.3F, Plugin.i18n("mds.meter"), null));
        gwindowdialogclient_Spawn.addControl(new GWindowEditControl(gwindowdialogclient_Spawn, 30.0F, 32.0F, 5.0F, 1.3F, "Height in meters") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiRadarHeight_MAX).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (this.getValue().trim().length() < 1) return false;
                actorborn.zutiRadarHeight_MAX = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        return plMisBorn.tabSpawn;
    }

    /**
     * Method creates Capturing tab for base object.
     *
     * @param plMisBorn
     */
    public static GWindowTabDialogClient.Tab getPlMisBornCapturingTab() {
        final ZutiSupportMethods_Builder plMisBorn = new ZutiSupportMethods_Builder();

        GWindowDialogClient gwindowdialogclient_Capturing = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        plMisBorn.tabCapturing = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("mds.tabCapturing"), gwindowdialogclient_Capturing);

        new GWindowBoxSeparate(gwindowdialogclient_Capturing, 1.0F, 1.0F, 37.0F, 17.0F);

        // wZutiFront_HomeBaseCanBeCaptured
        gwindowdialogclient_Capturing.addLabel(new GWindowLabel(gwindowdialogclient_Capturing, 2.0F, 2.0F, 36.0F, 1.3F, Plugin.i18n("mds.capturing"), null));
        gwindowdialogclient_Capturing.addControl(new GWindowCheckBox(gwindowdialogclient_Capturing, 36.0F, 2.0F, null) {
            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiCanThisHomeBaseBeCaptured, false);
                if (this.isChecked()) {
                    plMisBorn.wZutiCaptureIfChiefPresent.setEnable(true);
                    plMisBorn.bShowAircraftsRED.setEnable(true);
                    plMisBorn.bModifyCapturedRedCountries.setEnable(true);
                    plMisBorn.wZutiCapturing_CapturedBasePlanesRed.setEnable(true);
                    plMisBorn.bShowAircraftsBLUE.setEnable(true);
                    plMisBorn.bModifyCapturedBlueCountries.setEnable(true);
                    plMisBorn.wZutiCapturing_CapturedBasePlanesBlue.setEnable(true);
                    plMisBorn.wZutiCapturing_RequiredParatroopers.setEnable(true);
                } else {
                    plMisBorn.wZutiCaptureIfChiefPresent.setEnable(false);
                    plMisBorn.bShowAircraftsRED.setEnable(false);
                    plMisBorn.bModifyCapturedRedCountries.setEnable(false);
                    plMisBorn.wZutiCapturing_CapturedBasePlanesRed.setEnable(false);
                    plMisBorn.bShowAircraftsBLUE.setEnable(false);
                    plMisBorn.bModifyCapturedBlueCountries.setEnable(false);
                    plMisBorn.wZutiCapturing_CapturedBasePlanesBlue.setEnable(false);
                    plMisBorn.wZutiCapturing_RequiredParatroopers.setEnable(false);
                }
            }

            public boolean notify(int i_52_, int i_53_) {

                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiCanThisHomeBaseBeCaptured = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        // wZutiFront_CaptureIfChiefPresent
        gwindowdialogclient_Capturing.addLabel(new GWindowLabel(gwindowdialogclient_Capturing, 2.0F, 4.0F, 36.0F, 1.3F, Plugin.i18n("mds.capturing.ifNoChiefPresent"), null));
        gwindowdialogclient_Capturing.addControl(plMisBorn.wZutiCaptureIfChiefPresent = new GWindowCheckBox(gwindowdialogclient_Capturing, 36.0F, 4.0F, null) {
            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiCaptureOnlyIfNoChiefPresent, false);
            }

            public boolean notify(int i_52_, int i_53_) {

                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiCaptureOnlyIfNoChiefPresent = this.isChecked();
                PlMission.setChanged();
                return false;
            }
        });

        // bShowAircraftsRED
        gwindowdialogclient_Capturing.addControl(plMisBorn.bShowAircraftsRED = new GWindowButton(gwindowdialogclient_Capturing, 2.0F, 6.0F, 9.0F, 1.3F, Plugin.i18n("mds.capturing.redPlanes"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (actorborn == null) return false;

                if (actorborn.zutiCapturedAc_Red == null) actorborn.zutiCapturedAc_Red = new Zuti_WManageAircrafts();

                if (actorborn.zutiCapturedAc_Red.isVisible()) {
                    actorborn.zutiCapturedAc_Red.hideWindow();
                    actorborn.zutiCapturedAc_Red.clearAirNames();
                    // zuti_manageAircrafts.setAircraftLoadout(null);
                } else {
                    actorborn.zutiCapturedAc_Red.setTitle(Plugin.i18n("mds.capturing.redTitle"));
                    actorborn.zutiCapturedAc_Red.setParentEditControl(plMisBorn.wZutiCapturing_CapturedBasePlanesRed, true);
                    actorborn.zutiCapturedAc_Red.enableAcModifications(actorborn.zutiEnablePlaneLimits);
                    actorborn.zutiCapturedAc_Red.showWindow();
                }
                return true;
            }
        });
        // wZutiFront_CapturedBasePlanesRed
        gwindowdialogclient_Capturing.addControl(plMisBorn.wZutiCapturing_CapturedBasePlanesRed = new GWindowEditControl(gwindowdialogclient_Capturing, 12.0F, 6.0F, 25.0F, 1.3F, null) {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bCanEdit = false;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(actorborn.zutiBaseCapturedRedPlanes, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiBaseCapturedRedPlanes = this.getValue();

                PlMission.setChanged();
                return false;
            }
        });

        gwindowdialogclient_Capturing.addControl(plMisBorn.bModifyCapturedRedCountries = new GWindowButton(gwindowdialogclient_Capturing, 2.0F, 8.0F, 35.0F, 2.0F, Plugin.i18n("mds.capturing.redCountries"), null) {
            public boolean notify(int i_83_, int i_84_) {
                if (i_83_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                ZutiSupportMethods_Builder.countriesWindowShow(actorborn, 1);

                return true;
            }
        });

        // bShowAircraftsBLUE
        gwindowdialogclient_Capturing.addControl(plMisBorn.bShowAircraftsBLUE = new GWindowButton(gwindowdialogclient_Capturing, 2.0F, 11.0F, 9.0F, 1.3F, Plugin.i18n("mds.capturing.bluePlanes"), null) {
            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                if (actorborn == null) return false;

                if (actorborn.zutiCapturedAc_Blue == null) actorborn.zutiCapturedAc_Blue = new Zuti_WManageAircrafts();

                if (actorborn.zutiCapturedAc_Blue.isVisible()) {
                    actorborn.zutiCapturedAc_Blue.hideWindow();
                    actorborn.zutiCapturedAc_Blue.clearAirNames();
                    // zuti_manageAircrafts.setAircraftLoadout(null);
                } else {
                    if (actorborn.zutiCapturedAc_Blue == null) actorborn.zutiCapturedAc_Blue = new Zuti_WManageAircrafts();

                    actorborn.zutiCapturedAc_Blue.setTitle(Plugin.i18n("mds.capturing.blueTitle"));
                    actorborn.zutiCapturedAc_Blue.setParentEditControl(plMisBorn.wZutiCapturing_CapturedBasePlanesBlue, true);
                    actorborn.zutiCapturedAc_Blue.enableAcModifications(actorborn.zutiEnablePlaneLimits);
                    actorborn.zutiCapturedAc_Blue.showWindow();
                }

                return true;
            }
        });

        // wZutiFront_CapturedBasePlanesBlue
        gwindowdialogclient_Capturing.addControl(plMisBorn.wZutiCapturing_CapturedBasePlanesBlue = new GWindowEditControl(gwindowdialogclient_Capturing, 12.0F, 11.0F, 25.0F, 1.3F, null) {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bCanEdit = false;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(actorborn.zutiBaseCapturedBluePlanes, false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiBaseCapturedBluePlanes = this.getValue();

                PlMission.setChanged();
                return false;
            }
        });

        gwindowdialogclient_Capturing.addControl(plMisBorn.bModifyCapturedBlueCountries = new GWindowButton(gwindowdialogclient_Capturing, 2.0F, 13.0F, 35.0F, 2.0F, Plugin.i18n("mds.capturing.blueCountries"), null) {
            public boolean notify(int i_83_, int i_84_) {
                if (i_83_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                ZutiSupportMethods_Builder.countriesWindowShow(actorborn, 2);

                return true;
            }
        });

        // wZutiCapturing_RequiredParatroopers
        gwindowdialogclient_Capturing.addLabel(new GWindowLabel(gwindowdialogclient_Capturing, 2.0F, 16.0F, 34.0F, 1.3F, Plugin.i18n("mds.capturing.paratroopers"), null));
        gwindowdialogclient_Capturing.addControl(plMisBorn.wZutiCapturing_RequiredParatroopers = new GWindowEditControl(gwindowdialogclient_Capturing, 33.0F, 16.0F, 4.0F, 1.3F, "Number of paratroopers that need to land inside home base circle.") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                // System.out.println(" ...............................REQUIRED IS " + actorborn.zutiCapturingRequiredParatroopers);
                this.setValue(new Integer(actorborn.zutiCapturingRequiredParatroopers).toString(), false);
            }

            public boolean notify(int i_52_, int i_53_) {
                if (i_52_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiCapturingRequiredParatroopers = Integer.parseInt(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        return plMisBorn.tabCapturing;
    }

    /**
     * Method creates RRR tab for home base object.
     *
     * @param plMisBorn
     */
    public static GWindowTabDialogClient.Tab getPlMisBornRRRTab() {
        final ZutiSupportMethods_Builder plMisBorn = new ZutiSupportMethods_Builder();

        GWindowDialogClient gwindowdialogclient_RRR = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        plMisBorn.tabRRR = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("mds.tabRRR"), gwindowdialogclient_RRR);

        // wZutiOverrideDefaultRRRSettings
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 1.0F, 1.0F, 36.0F, 1.3F, Plugin.i18n("mds.RRR"), null));
        gwindowdialogclient_RRR.addControl(new GWindowCheckBox(gwindowdialogclient_RRR, 36.0F, 1.0F, null) {
            public void preRender() {
                super.preRender();

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiOverrideDefaultRRRSettings, false);

                plMisBorn.wZutiOneMgCannonRearmSecond.setEnable(this.isChecked());
                plMisBorn.wZutiOneBombFTankTorpedoeRearmSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiOneRocketRearmSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiGallonsLitersPerSecond.setEnable(this.isChecked());
                plMisBorn.wZutiOneWeaponRepairSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiFlapsRepairSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiRefuelOnlyIfFuelTanksExist.setEnable(this.isChecked());
                plMisBorn.wZutiRearmOnlyIfAmmoBoxesExist.setEnable(this.isChecked());
                plMisBorn.wZutiRepairOnlyIfWorkshopExist.setEnable(this.isChecked());
                plMisBorn.wZutiEngineRepairSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiCockpitRepairSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiOneControlCableRepairSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiOneFuelOilTankRepairSeconds.setEnable(this.isChecked());
                plMisBorn.wZutiLoadoutChangePenaltySeconds.setEnable(this.isChecked());
                plMisBorn.wZutiOnlyHomeBaseSpecificLoadouts.setEnable(this.isChecked());
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOverrideDefaultRRRSettings = this.isChecked();

                PlMission.setChanged();

                return false;
            }
        });

        gwindowdialogclient_RRR.addLabel(plMisBorn.lSeparate_Rearm = new GWindowLabel(gwindowdialogclient_RRR, 3.0F, 3.5F, 4.0F, 1.6F, Plugin.i18n("mds.rearm"), null));
        plMisBorn.bSeparate_Rearm = new GWindowBoxSeparate(gwindowdialogclient_RRR, 1.0F, 4.0F, 37.0F, 9.0F);
        plMisBorn.bSeparate_Rearm.exclude = plMisBorn.lSeparate_Rearm;

        // wZutiOneMgCannonRearmSecond
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 5.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.mg"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 14.5F, 5.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOneMgCannonRearmSecond = new GWindowEditControl(gwindowdialogclient_RRR, 11.0F, 5.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiOneMgCannonRearmSecond).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOneMgCannonRearmSecond = ZutiSupportMethods_Builder.getIntValue(this.getValue());
                PlMission.setChanged();
                return false;
            }
        });

        // wZutiOneRocketRearmSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 7.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.rocket"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 14.5F, 7.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOneRocketRearmSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 11.0F, 7.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiOneRocketRearmSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOneRocketRearmSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiOneBombFTankTorpedoeRearmSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 24.0F, 5.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.bomb"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 36.5F, 5.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOneBombFTankTorpedoeRearmSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 33.0F, 5.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiOneBombFTankTorpedoeRearmSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOneBombFTankTorpedoeRearmSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiLoadoutChangePenaltySeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 24.0F, 7.0F, 10.0F, 1.3F, Plugin.i18n("mds.rearm.loadout"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 36.5F, 7.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiLoadoutChangePenaltySeconds = new GWindowEditControl(gwindowdialogclient_RRR, 33.0F, 7.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiLoadoutChangePenaltySeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiLoadoutChangePenaltySeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiOnlyHomeBaseSpecificLoadouts
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 9.0F, 36.0F, 1.3F, Plugin.i18n("mds.rearm.onlyHbLoadouts"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOnlyHomeBaseSpecificLoadouts = new GWindowCheckBox(gwindowdialogclient_RRR, 35.0F, 9.0F, null) {
            public void afterCreated() {
                this.setEnable(false);
            }

            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiOnlyHomeBaseSpecificLoadouts, false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOnlyHomeBaseSpecificLoadouts = this.isChecked();

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiRearmOnlyIfAmmoBoxesExist
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 11.0F, 36.0F, 1.3F, Plugin.i18n("mds.rearm.ammo"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiRearmOnlyIfAmmoBoxesExist = new GWindowCheckBox(gwindowdialogclient_RRR, 35.0F, 11.0F, null) {
            public void afterCreated() {
                this.setEnable(false);
            }

            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiRearmOnlyIfAmmoBoxesExist, false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiRearmOnlyIfAmmoBoxesExist = this.isChecked();

                PlMission.setChanged();

                return false;
            }
        });

        gwindowdialogclient_RRR.addLabel(plMisBorn.lSeparate_Refuel = new GWindowLabel(gwindowdialogclient_RRR, 3.0F, 14.5F, 4.0F, 1.6F, Plugin.i18n("mds.refuel"), null));
        plMisBorn.bSeparate_Refuel = new GWindowBoxSeparate(gwindowdialogclient_RRR, 1.0F, 15.0F, 37.0F, 5.0F);
        plMisBorn.bSeparate_Refuel.exclude = plMisBorn.lSeparate_Refuel;

        // wZutiGallonsLitersPerSecond
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 16.0F, 10.0F, 1.3F, Plugin.i18n("mds.refuel.rate1"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 35.0F, 16.0F, 12.0F, 1.3F, Plugin.i18n("mds.refuel.rate2"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiGallonsLitersPerSecond = new GWindowEditControl(gwindowdialogclient_RRR, 31.5F, 16.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiGallonsLitersPerSecond).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiGallonsLitersPerSecond = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiRefuelOnlyIfFuelTanksExist
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 18.0F, 36.0F, 1.3F, Plugin.i18n("mds.refuel.fuelTanks"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiRefuelOnlyIfFuelTanksExist = new GWindowCheckBox(gwindowdialogclient_RRR, 35.0F, 18.0F, null) {
            public void afterCreated() {
                this.setEnable(false);
            }

            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiRefuelOnlyIfFuelTanksExist, false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiRefuelOnlyIfFuelTanksExist = this.isChecked();

                PlMission.setChanged();

                return false;
            }
        });

        gwindowdialogclient_RRR.addLabel(plMisBorn.lSeparate_Repair = new GWindowLabel(gwindowdialogclient_RRR, 3.0F, 21.5F, 4.0F, 1.6F, Plugin.i18n("mds.repair"), null));
        plMisBorn.bSeparate_Repair = new GWindowBoxSeparate(gwindowdialogclient_RRR, 1.0F, 22.0F, 37.0F, 9.0F);
        plMisBorn.bSeparate_Repair.exclude = plMisBorn.lSeparate_Repair;

        // wZutiEngineRepairSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 23.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.engine"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 14.5F, 23.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiEngineRepairSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 11.0F, 23.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiEngineRepairSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiEngineRepairSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiOneControlCableRepairSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 24.0F, 23.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.controlCable"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 36.5F, 23.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOneControlCableRepairSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 33.0F, 23.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiOneControlCableRepairSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOneControlCableRepairSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiFlapsRepairSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 25.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.flaps"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 14.5F, 25.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiFlapsRepairSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 11.0F, 25.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiFlapsRepairSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiFlapsRepairSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiOneWeaponRepairSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 24.0F, 25.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.cockpit"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 36.5F, 25.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOneWeaponRepairSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 33.0F, 25.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiOneWeaponRepairSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOneWeaponRepairSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiCockpitRepairSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 27.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.cockpit"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 14.5F, 27.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiCockpitRepairSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 11.0F, 27.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiCockpitRepairSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiCockpitRepairSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();

                return false;
            }
        });

        // wZutiOneFuelOilTankRepairSeconds
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 24.0F, 27.0F, 10.0F, 1.3F, Plugin.i18n("mds.repair.tank"), null));
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 36.5F, 27.0F, 2.0F, 1.3F, Plugin.i18n("mds.second"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiOneFuelOilTankRepairSeconds = new GWindowEditControl(gwindowdialogclient_RRR, 33.0F, 27.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
                this.setEnable(false);
            }

            public void preRender() {
                if (this.getValue().trim().length() > 0) return;

                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setValue(new Integer(actorborn.zutiOneFuelOilTankRepairSeconds).toString(), false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiOneFuelOilTankRepairSeconds = ZutiSupportMethods_Builder.getIntValue(this.getValue());

                PlMission.setChanged();
                return false;
            }
        });

        // wZutiRepairOnlyIfWorkshopExist
        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 29.0F, 36.0F, 1.3F, Plugin.i18n("mds.repair.workshop"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiRepairOnlyIfWorkshopExist = new GWindowCheckBox(gwindowdialogclient_RRR, 35.0F, 29.0F, null) {
            public void afterCreated() {
                this.setEnable(false);
            }

            public void preRender() {
                super.preRender();
                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                this.setChecked(actorborn.zutiRepairOnlyIfWorkshopExist, false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();
                actorborn.zutiRepairOnlyIfWorkshopExist = this.isChecked();

                PlMission.setChanged();

                return false;
            }
        });

        gwindowdialogclient_RRR.addLabel(plMisBorn.lSeparate_Resources = new GWindowLabel(gwindowdialogclient_RRR, 3.0F, 32.5F, 14.0F, 1.6F, Plugin.i18n("mds.RRR.resourcesManagement"), null));
        plMisBorn.bSeparate_Resources = new GWindowBoxSeparate(gwindowdialogclient_RRR, 1.0F, 33.0F, 37.0F, 6.0F);
        plMisBorn.bSeparate_Resources.exclude = plMisBorn.lSeparate_Resources;

        gwindowdialogclient_RRR.addLabel(new GWindowLabel(gwindowdialogclient_RRR, 2.0F, 34.0F, 35.0F, 1.3F, Plugin.i18n("mds.RRR.enableResourcesMng"), null));
        gwindowdialogclient_RRR.addControl(plMisBorn.wZutiReload_EnableResourcesManagement = new GWindowCheckBox(gwindowdialogclient_RRR, 35.0F, 34.0F, null) {
            public void preRender() {
                super.preRender();

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();

                if (actorborn != null) this.setChecked(actorborn.zutiEnableResourcesManagement, false);
            }

            public boolean notify(int i, int i_18_) {
                if (i != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();

                if (actorborn != null) {
                    actorborn.zutiEnableResourcesManagement = this.isChecked();

                    if (!actorborn.zutiEnableResourcesManagement) actorborn.resourcesManagement = null;
                }

                return false;
            }
        });
        // HB resources
        gwindowdialogclient_RRR.addControl(new GWindowButton(gwindowdialogclient_RRR, 2.0F, 36.0F, 35.0F, 2.0F, Plugin.i18n("mds.RRR.setResourcesHB"), null) {
            public void preRender() {
                // TODO: Resources
                super.preRender();

                this.setEnable(plMisBorn.wZutiReload_EnableResourcesManagement.isChecked());

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();

                if (actorborn != null && actorborn.resourcesManagement != null && !this.isEnable()) {
                    if (actorborn.resourcesManagement.isVisible()) actorborn.resourcesManagement.hideWindow();

                    actorborn.resourcesManagement = null;
                }
            }

            public boolean notify(int i_61_, int i_62_) {
                if (i_61_ != 2) return false;

                ActorBorn actorborn = (ActorBorn) Plugin.builder.selectedActor();

                if (actorborn.resourcesManagement == null) actorborn.resourcesManagement = new Zuti_WResourcesManagement(1);

                if (actorborn.resourcesManagement.isVisible()) actorborn.resourcesManagement.hideWindow();
                else {
                    actorborn.resourcesManagement.setTitle(Plugin.i18n("mds.RRR.setResourcesHB"));
                    actorborn.resourcesManagement.setActorBorn(actorborn);
                    actorborn.resourcesManagement.countRRRObjects();
                    actorborn.resourcesManagement.showWindow();
                }

                PlMission.setChanged();
                return true;
            }
        });

        return plMisBorn.tabRRR;
    }

    /**
     * Method clears all PlMisBorn variables that were created by me.
     *
     * @param plMisBorn
     */
    public static void resetPlMisBornVariableValues(PlMisBorn plMisBorn) {
        if (plMisBorn.wFriction != null) plMisBorn.wFriction.setValue("");
        /*
         * System.out.println("Resetting values..."); if( plMisBorn.wHeight != null ) plMisBorn.wHeight.setValue(""); if( plMisBorn.wSpeed != null ) plMisBorn.wSpeed.setValue(""); if( plMisBorn.wOrient != null ) plMisBorn.wOrient.setValue(""); if(
         * plMisBorn.wMaxPilots != null ) plMisBorn.wMaxPilots.setValue(""); if( plMisBorn.wFriction != null ) plMisBorn.wFriction.setValue("");
         *
         * if( plMisBorn.wZutiRadar_Min != null ) plMisBorn.wZutiRadar_Min.setValue(""); if( plMisBorn.wZutiRadar_Max != null ) plMisBorn.wZutiRadar_Max.setValue(""); if( plMisBorn.wZutiRadar_Range != null ) plMisBorn.wZutiRadar_Range.setValue(""); if(
         * plMisBorn.wZutiCapturing_CapturedBasePlanesRed != null ) plMisBorn.wZutiCapturing_CapturedBasePlanesRed.setValue(""); if( plMisBorn.wZutiCapturing_CapturedBasePlanesBlue != null ) plMisBorn.wZutiCapturing_CapturedBasePlanesBlue.setValue("");
         * if( plMisBorn.wZutiCapturing_RequiredParatroopers != null ) plMisBorn.wZutiCapturing_RequiredParatroopers.setValue("");
         *
         * plMisBorn.wZutiCarrierSpawnPoints.clear(); plMisBorn.wZutiOneMgCannonRearmSecond.setValue(""); plMisBorn.wZutiOneBombFTankTorpedoeRearmSeconds.setValue(""); plMisBorn.wZutiOneRocketRearmSeconds.setValue("");
         * plMisBorn.wZutiGallonsLitersPerSecond.setValue(""); plMisBorn.wZutiOneWeaponRepairSeconds.setValue(""); plMisBorn.wZutiFlapsRepairSeconds.setValue(""); plMisBorn.wZutiEngineRepairSeconds.setValue("");
         * plMisBorn.wZutiCockpitRepairSeconds.setValue(""); plMisBorn.wZutiOneControlCableRepairSeconds.setValue(""); plMisBorn.wZutiOneFuelOilTankRepairSeconds.setValue(""); plMisBorn.wZutiLoadoutChangePenaltySeconds.setValue("");
         */
    }

    /**
     * This method sets labels for targets that show how many objects are covered by target and how many of them need to be destroyed in order to complete target.
     *
     * @param plMisTarget
     * @param actortarget
     * @param zutiCountObjects
     */
    public static void setPlMisTargetLabels(PlMisTarget plMisTarget, ActorTarget actortarget, boolean zutiCountObjects) {
        plMisTarget.lZutiRadius2.cap = new com.maddox.gwindow.GCaption(" [" + actortarget.r + "m]");

        if (actortarget != plMisTarget.zutiTempActorTarget || zutiCountObjects) {
            /*
             * System.out.println("Static actors: " + Target.countStaticActors(actortarget.pos.getAbsPoint(), actortarget.r)); System.out.println("Houses actors: " + Target.zutiCountHousesInArea(actortarget.pos.getAbsPoint(), actortarget.r));
             * System.out.println("Position: " + actortarget.pos.getAbsPoint().toString() + ", r=" + actortarget.r);
             */

            if (actortarget.zutiAllowHousesTargeting) plMisTarget.zutiObjectsCoveredByTargetArea = Target.countStaticActors(actortarget.pos.getAbsPoint(), actortarget.r) + ZutiSupportMethods.countHousesInArea(actortarget.pos.getAbsPoint(), actortarget.r);
            else plMisTarget.zutiObjectsCoveredByTargetArea = Target.countStaticActors(actortarget.pos.getAbsPoint(), actortarget.r);

            plMisTarget.lZutiDescription.cap = new GCaption(Plugin.i18n("mds.objectives.objectsNr") + " " + new Integer(plMisTarget.zutiObjectsCoveredByTargetArea));

            plMisTarget.zutiTempActorTarget = actortarget;
        }

        try {
            // TODO: Fixed by SAS~Storebror: Messy code and wround rounding with integers fixed!
            if (actortarget.type == 1 || actortarget.type == 6)
                plMisTarget.lZutiConditionsSummary.cap = new GCaption(Plugin.i18n("mds.objectives.destroyCond") + " " + Math.round((float) plMisTarget.zutiObjectsCoveredByTargetArea * (float) actortarget.destructLevel / 100F));
//            if (actortarget.type == 1) plMisTarget.lZutiConditionsSummary.cap = new GCaption(Plugin.i18n("mds.objectives.destroyCond") + " " + Math.round(plMisTarget.zutiObjectsCoveredByTargetArea * actortarget.destructLevel / 100));
//            else if (actortarget.type == 6) plMisTarget.lZutiConditionsSummary.cap = new GCaption(Plugin.i18n("mds.objectives.defenceCond") + " " + Math.round(plMisTarget.zutiObjectsCoveredByTargetArea * actortarget.destructLevel / 100));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Call this window to modify actor born countries.
     *
     * @param actorborn
     * @param mode
     */
    protected static void countriesWindowShow(ActorBorn actorborn, int mode) {
        if (actorborn == null) return;

        try {
            if (HOME_BASE_COUNTRIES_WINDOW == null) HOME_BASE_COUNTRIES_WINDOW = new Zuti_WHomeBaseCountries();

            HOME_BASE_COUNTRIES_WINDOW.setMode(mode);

            if (HOME_BASE_COUNTRIES_WINDOW.isVisible()) {
                // Close it and thus save changes for prev. actor. Load new Actor settings
                HOME_BASE_COUNTRIES_WINDOW.close(true);
                // Open it with new settings
                HOME_BASE_COUNTRIES_WINDOW.setSelectedCountries(actorborn);
                HOME_BASE_COUNTRIES_WINDOW.showWindow();
            } else {
                HOME_BASE_COUNTRIES_WINDOW.setSelectedCountries(actorborn);
                HOME_BASE_COUNTRIES_WINDOW.showWindow();
            }
        } catch (Exception ex) {}
    }

    /**
     * Call this method to update countries window with countries from specified actor born.
     *
     * @param actorborn
     */
    protected static void updateCountriesWindow(ActorBorn actorborn) {
        if (actorborn == null) return;

        try {
            if (HOME_BASE_COUNTRIES_WINDOW == null) return;

            if (HOME_BASE_COUNTRIES_WINDOW.isVisible()) {
                // Close it and thus save changes for prev. actor. Load new Actor settings
                HOME_BASE_COUNTRIES_WINDOW.close(true);
                // Open it with new settings
                HOME_BASE_COUNTRIES_WINDOW.setSelectedCountries(actorborn);
                HOME_BASE_COUNTRIES_WINDOW.showWindow();
            }
        } catch (Exception ex) {}
    }

    /**
     * This method loads MDS plugin.
     */
    protected static void createMDSPlugin() {
        String line = "(builder.ZutiPlMDS)";

        Object object = ObjIO.fromString(line);

        if (object != null && object instanceof Plugin) {
            Plugin plugin = (Plugin) object;
            String name = plugin.name();
            if (name != null && !Plugin.zutiGetMapNames().containsKey(name)) {
                Plugin.zutiGetAllActors().add(object);
                Plugin.zutiGetMapNames().put(name, plugin);
            }
        }

        line = "(builder.PlMisMods)";
        object = ObjIO.fromString(line);

        if (object != null && object instanceof Plugin) {
            Plugin plugin = (Plugin) object;
            String name = plugin.name();
            if (name != null && !Plugin.zutiGetMapNames().containsKey(name)) {
                Plugin.zutiGetAllActors().add(object);
                Plugin.zutiGetMapNames().put(name, plugin);
            }
        }
    }

    /**
     * Get army that object on specified coordinates is at.
     *
     * @param x
     * @param y
     * @return
     */
    public static int getCoordinatesLocationArmy(double x, double y) {
        ArrayList plugins = Plugin.zutiGetAllActors();
        ArrayList actors = null;
        Object object = null;
        PlMisFront plFront = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisFront) {
                plFront = (PlMisFront) object;
                break;
            }
        }

        ActorFrontMarker frontMarker = null;

        // TODO: Added by SAS~Storebror to avoid null dereference
        if (plFront != null) actors = plFront.allActors;
        if (actors == null) return -1;

        int army = -1;
        double min = 90000000000D;
        for (int j = 0; j < actors.size(); j++) {
            frontMarker = (ActorFrontMarker) actors.get(j);
            Point3d point = frontMarker.pos.getAbsPoint();
            double distance = (x - point.x) * (x - point.x) + (y - point.y) * (y - point.y);
            if (distance < min) {
                army = frontMarker.getArmy();
                min = distance;
            }
        }

        return army;
    }

    /**
     * Method searches for PlMisBorn class and returns its actors list.
     *
     * @return
     */
    public static List getPlMisBornActorsList() {
        ArrayList plugins = Plugin.zutiGetAllActors();
        Object object = null;
        PlMisBorn plBorn = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisBorn) {
                plBorn = (PlMisBorn) object;
                return plBorn.allActors;
            }
        }

        return new ArrayList();
    }

    /**
     * Method checks if given coordinates are on any actor born area.
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean isOnActorBornArea(double x, double y) {
        List actorBorns = getPlMisBornActorsList();
        Point3d pos = null;

        for (int i = 0; i < actorBorns.size(); i++) {
            ActorBorn ab = (ActorBorn) actorBorns.get(i);
            long radius = ab.r * ab.r;
            pos = ab.pos.getAbsPoint();
            double tmpDistance = Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2);
            if (tmpDistance < radius) return true;
        }

        return false;
    }

    /**
     * Get list of RRR items for selected army.
     *
     * @param objectsMap
     * @param amry
     * @return
     */
    public static Map listRRRObjects(Map objectsMap, int army) {
        Map rrrObjects = new HashMap();
        ArrayList plugins = Plugin.zutiGetAllActors();
        ArrayList actors = null;
        Object object = null;
        PlMisHouse plHouse = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisHouse) {
                plHouse = (PlMisHouse) object;
                break;
            }
        }

        // TODO: Added by SAS~Storebror to avoid null dereference
        if (plHouse == null) return rrrObjects;

        actors = plHouse.allActors;
        Point3d pos = null;
        for (int j = 0; j < actors.size(); j++) {
            Actor actor = (Actor) actors.get(j);
            pos = actor.pos.getAbsPoint();

            if (isOnActorBornArea(pos.x, pos.y)) continue;

            int outArmy = getCoordinatesLocationArmy(pos.x, pos.y);
            if (outArmy != army) continue;

            if (Actor.isValid(actor)) {
                Type type = (Type) Property.value(actor, "builderType", null);
                String name = type.shortClassName;
                name = name.substring(name.indexOf("$") + 1, name.length());

                // System.out.println("Checking object: " + name);
                if (ZutiSupportMethods.isAmmoBoxObject(name) || ZutiSupportMethods.isFuelTankObject(name) || ZutiSupportMethods.isWorkshopObject(name)) {
                    RRRItem rrrItem = (RRRItem) rrrObjects.get(name);
                    if (rrrItem != null) // Inc counter
                        rrrItem.count += 1;
                    else {
                        // Insert new Item
                        rrrItem = new RRRItem();
                        rrrItem.name = name;
                        rrrItem.count += 1;
                        rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
                        rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
                        rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
                        rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
                        rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
                        rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
                        rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
                        rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
                        rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
                        rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
                        rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

                        rrrObjects.put(rrrItem.name, rrrItem);
                    }
                }
            }
        }

        // Insert BombCargo object
        String name = BOMB_CARGO_NAME;
        if (army == 1) name = name + "_Red";
        else if (army == 2) name = name + "_Blue";

        RRRItem rrrItem = new RRRItem();
        rrrItem.name = name;
        rrrItem.count += 1;
        rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
        rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
        rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
        rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
        rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
        rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
        rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
        rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
        rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
        rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
        rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

        rrrObjects.put(rrrItem.name, rrrItem);

        return rrrObjects;
    }

    /**
     * Find all moving objects on the map that might be connected to RRR operations
     *
     * @param objectsMap
     * @param army
     * @return
     */
    public static Map listRRRMovingObjects(Map objectsMap, int army) {
        Map rrrObjects = new HashMap();

        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();

        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        Actor actor = null;
        Object object = null;
        Object aobj[] = null;
        String chiefName = null;
        String className = null;
        boolean isMovingRRRunit = false;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);

            if (object instanceof PlMisChief) {
                PlMisChief plMisChief = (PlMisChief) object;
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    System.out.println("--------------------ACTOR: " + actor.toString());
                    if (actor instanceof PathChief) {
                        if (actor.getArmy() != army) continue;

                        chiefName = null;
                        pc = (PathChief) actor;
                        chiefName = plMisChief.type[pc._iType].item[pc._iItem].name;

                        isMovingRRRunit = false;
                        if (pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT > 3) {
                            // Ship...
                            className = "com.maddox.il2.objects.ships.Ship$" + chiefName;
                            isMovingRRRunit = ZutiSupportMethods.isMovingRRRObject(className, null);
                        } else isMovingRRRunit = ZutiSupportMethods.isMovingRRRObject(chiefName, null);

                        if (isMovingRRRunit) {
                            RRRItem rrrItem = (RRRItem) rrrObjects.get(chiefName);
                            if (rrrItem != null) // Inc counter
                                rrrItem.count += 1;
                            else {
                                // Insert new Item
                                rrrItem = new RRRItem();
                                rrrItem.count = 1;
                                rrrItem.name = chiefName;
                                rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
                                rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
                                rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
                                rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
                                rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
                                rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
                                rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
                                rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
                                rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
                                rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
                                rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

                                rrrObjects.put(rrrItem.name, rrrItem);
                            }
                        }
                    }
                }
            }
        }

        return rrrObjects;
    }

    /**
     * Get list of RRR items that are inside provided coordinates.
     *
     * @param amry
     * @return
     */
    public static Map listRRRObjects_ActorBorn(Map resourcesData, double x, double y, long r, int army) {
        long radius = r * r;

        Map rrrObjects = new HashMap();
        ArrayList plugins = Plugin.zutiGetAllActors();
        ArrayList actors = null;
        Object object = null;
        PlMisHouse plHouse = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisHouse) {
                plHouse = (PlMisHouse) object;
                break;
            }
        }

        // TODO: Added by SAS~Storebror to avoid null dereference
        if (plHouse == null) return rrrObjects;

        actors = plHouse.allActors;
        for (int j = 0; j < actors.size(); j++) {
            Actor actor = (Actor) actors.get(j);
            Point3d pos = actor.pos.getAbsPoint();
            if (Actor.isValid(actor)) {
                double tmpDistance = Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2);
                if (tmpDistance > radius) continue;

                Type type = (Type) Property.value(actor, "builderType", null);
                String name = type.shortClassName;
                if (name.indexOf("$") > -1) name = name.substring(name.indexOf("$") + 1, name.length());

                // System.out.println("Checking object: " + name);
                if (ZutiSupportMethods.isAmmoBoxObject(name) || ZutiSupportMethods.isFuelTankObject(name) || ZutiSupportMethods.isWorkshopObject(name)) {
                    RRRItem rrrItem = (RRRItem) rrrObjects.get(name);
                    if (rrrItem != null) // Inc counter
                        rrrItem.count += 1;
                    /*
                     * rrrItem.bullets = rrrItem.bullets + getRRRObjectResources(rrrItem.name, "bullets"); rrrItem.rockets = rrrItem.rockets + getRRRObjectResources(rrrItem.name, "rockets"); rrrItem.bomb250 = rrrItem.bomb250 +
                     * getRRRObjectResources(rrrItem.name, "bomb250"); rrrItem.bomb500 = rrrItem.bomb500 + getRRRObjectResources(rrrItem.name, "bomb500"); rrrItem.bomb1000 = rrrItem.bomb1000 + getRRRObjectResources(rrrItem.name, "bomb1000");
                     * rrrItem.bomb2000 = rrrItem.bomb2000 + getRRRObjectResources(rrrItem.name, "bomb2000"); rrrItem.bomb5000 = rrrItem.bomb5000 + getRRRObjectResources(rrrItem.name, "bomb5000"); rrrItem.bomb9999 = rrrItem.bomb9999 +
                     * getRRRObjectResources(rrrItem.name, "bomb9999"); rrrItem.fuel = rrrItem.fuel + getRRRObjectResources(rrrItem.name, "fuel"); rrrItem.engines = rrrItem.engines + getRRRObjectResources(rrrItem.name, "engines"); rrrItem.repairKits =
                     * rrrItem.repairKits + getRRRObjectResources(rrrItem.name, "repairKits");
                     */
                    else {
                        // Insert new Item
                        rrrItem = new RRRItem();
                        rrrItem.name = name;
                        rrrItem.count += 1;
                        rrrItem.bullets = getRRRObjectResources(resourcesData, rrrItem.name, "bullets");
                        rrrItem.rockets = getRRRObjectResources(resourcesData, rrrItem.name, "rockets");
                        rrrItem.bomb250 = getRRRObjectResources(resourcesData, rrrItem.name, "bomb250");
                        rrrItem.bomb500 = getRRRObjectResources(resourcesData, rrrItem.name, "bomb500");
                        rrrItem.bomb1000 = getRRRObjectResources(resourcesData, rrrItem.name, "bomb1000");
                        rrrItem.bomb2000 = getRRRObjectResources(resourcesData, rrrItem.name, "bomb2000");
                        rrrItem.bomb5000 = getRRRObjectResources(resourcesData, rrrItem.name, "bomb5000");
                        rrrItem.bomb9999 = getRRRObjectResources(resourcesData, rrrItem.name, "bomb9999");
                        rrrItem.fuel = getRRRObjectResources(resourcesData, rrrItem.name, "fuel");
                        rrrItem.engines = getRRRObjectResources(resourcesData, rrrItem.name, "engines");
                        rrrItem.repairKits = getRRRObjectResources(resourcesData, rrrItem.name, "repairKits");

                        rrrObjects.put(rrrItem.name, rrrItem);
                    }
                }
            }
        }

        return rrrObjects;
    }

    /**
     * Get list of RRR items that are inside provided coordinates. ONLY SEARCHES FOR AIRCRAFT CARRIERS!!!
     *
     * @param objectsMap
     * @param army
     * @return
     */
    public static Map listAircraftCarriers_ActorBorn(Map objectsMap, double x, double y, long r) {
        Map rrrObjects = new HashMap();

        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();

        double closest = r * r;
        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        Actor actor = null;
        Object object = null;
        Object aobj[] = null;
        String chiefName = null;
        String className = null;
        Point3d pos = null;
        Actor closestActor = null;
        String closestChiefName = null;
        Actor shipActor = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);

            if (object instanceof PlMisChief) {
                PlMisChief plMisChief = (PlMisChief) object;
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    if (actor instanceof PathChief) {
                        pc = (PathChief) actor;
                        int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
                        switch (pcType) {
                            case 0: // Infantry
                            case 1: // Armor
                            case 2: // Vehicle
                            case 3: // Train
                                break;
                            case 4:
                            case 5: {
                                chiefName = plMisChief.type[pc._iType].item[pc._iItem].name;
                                className = "com.maddox.il2.objects.ships.Ship$" + chiefName;
                                shipActor = ZutiSupportMethods_Builder.getActorFromClassName(className);
                                if (shipActor instanceof ZutiTypeAircraftCarrier) {
                                    // Current actor is aircraft carrier
                                    pos = pc.point(0).pos.getAbsPoint();
                                    double d = (x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y);
                                    if (d < closest) {
                                        closest = d;
                                        closestActor = actor;
                                        closestChiefName = chiefName;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (closestActor == null) return rrrObjects;

        RRRItem rrrItem = (RRRItem) rrrObjects.get(closestChiefName);
        rrrItem = new RRRItem();
        rrrItem.name = closestChiefName;
        rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
        rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
        rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
        rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
        rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
        rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
        rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
        rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
        rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
        rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
        rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

        rrrObjects.put(rrrItem.name, rrrItem);

        return rrrObjects;
    }

    /**
     * Call this method to save information about actor born RRR objects assigned resources.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void saveRRRObjectsResources(ActorBorn actorborn, SectFile sectfile) {
        if (sectfile == null || !actorborn.zutiEnableResourcesManagement || actorborn.resourcesManagement == null || actorborn.resourcesManagement.wTable == null || actorborn.resourcesManagement.wTable.items == null
                || actorborn.resourcesManagement.wTable.items.size() < 3)
            return;

        int sectionIdRRR = sectfile.sectionAdd("BornPlace_Resources_" + (int) actorborn.pos.getAbsPoint().x + "_" + (int) actorborn.pos.getAbsPoint().y);
        List list = actorborn.resourcesManagement.wTable.items;

        Collections.sort(list, new RRRItem_CompareByName());

        for (int i = 0; i < list.size(); i++) {
            RRRItem item = (RRRItem) list.get(i);

            if (item == null || item.name.equals(I18N.gui("mds.RRR.sum"))) continue;

            sectfile.lineAdd(sectionIdRRR, item.getFileLine());
        }
    }

    /**
     * Call this method to save information about RRR objects assigned resources for each side.
     *
     * @param actorborn
     * @param sectfile
     */
    public static void saveRRRObjectsResourcesForSide(List list, SectFile sectfile, int side) {
        // List size < 3 means we have one empty line and sum line. So, no real data!
        if (sectfile == null || list == null || list.size() < 3) return;

        Collections.sort(list, new RRRItem_CompareByName());

        String sideStr = "None";
        if (side == 1) sideStr = "Red";
        else if (side == 2) sideStr = "Blue";

        int sectionIdRRR = sectfile.sectionAdd("RRR_Resources_" + sideStr);
        for (int i = 0; i < list.size(); i++) {
            RRRItem item = (RRRItem) list.get(i);

            if (item == null || item.name.equals(I18N.gui("mds.RRR.sum"))) continue;

            sectfile.lineAdd(sectionIdRRR, item.getFileLine());
        }
    }

    /**
     * Method outputs entries that can then be used to fill hash map.
     *
     * @param rrrItem
     */
    public static void listRRRItemsForHashing(RRRItem rrrItem) {
        if (rrrItem == null) return;

        if (ZutiSupportMethods.isAmmoBoxObject(rrrItem.name)) {
            System.out.println("map.put(\"" + rrrItem.name + "_bullets\"" + ", new Integer(" + 800 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_rockets\"" + ", new Integer(" + 8 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb250\"" + ", new Integer(" + 6 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb500\"" + ", new Integer(" + 4 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb1000\"" + ", new Integer(" + 2 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb2000\"" + ", new Integer(" + 2 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb5000\"" + ", new Integer(" + 2 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb9999\"" + ", new Integer(" + 1 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_fuel\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_engines\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_repairKits\"" + ", new Integer(" + 0 + "));");
            System.out.println("==================================================");
        } else if (ZutiSupportMethods.isFuelTankObject(rrrItem.name)) {
            System.out.println("map.put(\"" + rrrItem.name + "_bullets\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_rockets\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb250\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb500\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb1000\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb2000\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb5000\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb9999\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_fuel\"" + ", new Integer(" + 2500 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_engines\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_repairKits\"" + ", new Integer(" + 0 + "));");
            System.out.println("==================================================");
        } else if (ZutiSupportMethods.isWorkshopObject(rrrItem.name)) {
            System.out.println("map.put(\"" + rrrItem.name + "_bullets\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_rockets\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb250\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb500\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb1000\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb2000\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb5000\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_bomb9999\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_fuel\"" + ", new Integer(" + 0 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_engines\"" + ", new Integer(" + 6 + "));");
            System.out.println("map.put(\"" + rrrItem.name + "_repairKits\"" + ", new Integer(" + 10 + "));");
            System.out.println("==================================================");
        }
    }

    /**
     * Get value for specified object and ordinance. Value is first checked in specified map. If no value for specified object is found, default value is assigned.
     *
     * @param objectsMap
     * @param objectName
     * @param ordinanceName
     * @return
     */
    public static long getRRRObjectResources(Map objectsMap, String objectName, String ordinanceName) {
        Long value = null;
        if (objectsMap != null) value = (Long) objectsMap.get(objectName + "_" + ordinanceName);

        if (value == null) value = (Long) DEFAULT_RRR_OBJECT_PROPERTIES.get(objectName + "_" + ordinanceName);

        if (value == null) value = new Long(0);

        return value.longValue();
    }

    /**
     * Method checks if resources management is enabled for home bases. If so, we will ignore global side resources management.
     *
     * @return
     */
    public static boolean isResourcesEnabledForActorsBorn() {
        List list = getPlMisBornActorsList();
        for (int i = 0; i < list.size(); i++)
            if (((ActorBorn) list.get(i)).zutiEnableResourcesManagement) return true;
        return false;
    }

    /**
     * Call this method upon load up process in FMB.
     *
     * @param section
     * @param fileName
     */
    public static void loadResources(ZutiMDSSection_RRR section, SectFile sectfile) {
        System.out.println("Loading RRR objects resources data...");

        // Try loading resources for RED side
        int sectionId = sectfile.sectionIndex("RRR_Resources_Red");
        if (sectionId >= 0) {
            section.wZutiReload_EnableResourcesManagement.setChecked(true, false);
            section.resourcesManagement_Red = new Zuti_WResourcesManagement(1);
            section.resourcesManagement_Red.objectsMap = new HashMap();

            int vars = sectfile.vars(sectionId);
            for (int i = 0; i < vars; i++) {
                String line = sectfile.line(sectionId, i).trim();
                String name = line.substring(0, line.indexOf(" "));
                String remaining = line.substring(line.indexOf(" ") + 1, line.length());

                // Load remaining values
                NumberTokenizer numbertokenizer = new NumberTokenizer(remaining);
                numbertokenizer.next(0, 0, 100000);// count, no need to save it form FMB
                section.resourcesManagement_Red.objectsMap.put(name + "_bullets", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_rockets", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_bomb250", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_bomb500", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_bomb1000", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_bomb2000", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_bomb5000", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_bomb9999", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_fuel", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_engines", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Red.objectsMap.put(name + "_repairKits", new Long(numbertokenizer.next(0, 0, 100000)));
            }

            section.resourcesManagement_Red.countRRRObjects();
        }

        // Try loading resources for BLUE side
        sectionId = sectfile.sectionIndex("RRR_Resources_Blue");
        if (sectionId >= 0) {
            section.wZutiReload_EnableResourcesManagement.setChecked(true, false);
            section.resourcesManagement_Blue = new Zuti_WResourcesManagement(2);
            section.resourcesManagement_Blue.objectsMap = new HashMap();

            int vars = sectfile.vars(sectionId);
            for (int i = 0; i < vars; i++) {
                String line = sectfile.line(sectionId, i).trim();
                String name = line.substring(0, line.indexOf(" "));
                String remaining = line.substring(line.indexOf(" ") + 1, line.length()).trim();

                // Load remaining values
                NumberTokenizer numbertokenizer = new NumberTokenizer(remaining);
                numbertokenizer.next(0, 0, 100000);// count, no need to save it form FMB
                section.resourcesManagement_Blue.objectsMap.put(name + "_bullets", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_rockets", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_bomb250", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_bomb500", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_bomb1000", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_bomb2000", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_bomb5000", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_bomb9999", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_fuel", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_engines", new Long(numbertokenizer.next(0, 0, 100000)));
                section.resourcesManagement_Blue.objectsMap.put(name + "_repairKits", new Long(numbertokenizer.next(0, 0, 100000)));
            }

            section.resourcesManagement_Blue.countRRRObjects();
        }

        // Now, try loading born place related settings
        List list = getPlMisBornActorsList();
        if (list != null) for (int i = 0; i < list.size(); i++) {
            ActorBorn ab = (ActorBorn) list.get(i);
            sectionId = sectfile.sectionIndex("BornPlace_Resources_" + (int) ab.pos.getAbsPoint().x + "_" + (int) ab.pos.getAbsPoint().y);
            if (sectionId >= 0) {
                ab.zutiEnableResourcesManagement = true;
                ab.resourcesManagement = new Zuti_WResourcesManagement(ab.getArmy());
                ab.resourcesManagement.setActorBorn(ab);
                ab.resourcesManagement.objectsMap = new HashMap();

                int vars = sectfile.vars(sectionId);
                for (int j = 0; j < vars; j++) {
                    String line = sectfile.line(sectionId, j).trim();
                    String name = line.substring(0, line.indexOf(" "));
                    String remaining = line.substring(line.indexOf(" ") + 1, line.length()).trim();

                    // Load remaining values
                    NumberTokenizer numbertokenizer = new NumberTokenizer(remaining);
                    numbertokenizer.next(0, 0, 100000);// count, no need to save it form FMB
                    ab.resourcesManagement.objectsMap.put(name + "_bullets", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_rockets", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_bomb250", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_bomb500", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_bomb1000", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_bomb2000", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_bomb5000", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_bomb9999", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_fuel", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_engines", new Long(numbertokenizer.next(0, 0, 100000)));
                    ab.resourcesManagement.objectsMap.put(name + "_repairKits", new Long(numbertokenizer.next(0, 0, 100000)));
                }

                ab.resourcesManagement.countRRRObjects();
            }
        }
        System.out.println("Done.");
    }

    public static Map listRRRMovingObjects_SeparateCarConwoy(Map objectsMap, int army) {
        Map rrrObjects = new HashMap();

        if (ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT < 0) ZutiMDSSection_Objectives.setChiefIdIncrement();

        ArrayList plugins = Plugin.zutiGetAllActors();
        PathChief pc = null;
        Actor actor = null;
        Object object = null;
        Object aobj[] = null;
        String chiefName = null;
        Point3d pos = null;
        boolean isMovingRRRunit = false;
        String className = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);

            if (object instanceof PlMisChief) {
                PlMisChief plMisChief = (PlMisChief) object;
                aobj = Plugin.builder.pathes.getOwnerAttached();
                for (int j = 0; j < aobj.length; j++) {
                    actor = (Actor) aobj[j];
                    if (actor instanceof PathChief) {
                        if (actor.getArmy() != army) continue;

                        chiefName = null;
                        pc = (PathChief) actor;

                        if (actor.pos != null) {
                            pos = actor.pos.getAbsPoint();
                            chiefName = plMisChief.type[pc._iType].item[pc._iItem].name;

                            if (isOnActorBornArea(pos.x, pos.y)) continue;

                            isMovingRRRunit = false;
                            if (pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT > 3) {
                                // Ship...
                                className = "com.maddox.il2.objects.ships.Ship$" + chiefName;
                                isMovingRRRunit = ZutiSupportMethods.isMovingRRRObject(className, null);
                            } else isMovingRRRunit = ZutiSupportMethods.isMovingRRRObject(chiefName, null);

                            if (isMovingRRRunit) {
                                RRRItem rrrItem = (RRRItem) rrrObjects.get(chiefName);
                                if (rrrItem != null) // Inc counter
                                    rrrItem.count += 1;
                                else {
                                    // Insert new Item
                                    rrrItem = new RRRItem();
                                    rrrItem.name = chiefName;
                                    rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
                                    rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
                                    rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
                                    rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
                                    rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
                                    rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
                                    rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
                                    rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
                                    rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
                                    rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
                                    rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

                                    rrrObjects.put(rrrItem.name, rrrItem);
                                }
                            }
                        } else {
                            // Get convoy name... based on it we determine if it is RRR or not
                            chiefName = plMisChief.type[pc._iType].item[pc._iItem].name;
                            isMovingRRRunit = false;
                            if (pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT > 3) {
                                // Ship...
                                className = "com.maddox.il2.objects.ships.Ship$" + chiefName;
                                isMovingRRRunit = ZutiSupportMethods.isMovingRRRObject(className, null);
                            } else isMovingRRRunit = ZutiSupportMethods.isMovingRRRObject(chiefName, null);

                            if (!isMovingRRRunit) continue;

                            // We are certainly doing with convoy.
                            if (pc.units != null && pc.units.length > 0) pos = pc.units[0].pos.getAbsPoint();

                            if (isOnActorBornArea(pos.x, pos.y)) continue;

                            // If we are processing train convoy, just add it, else further process it
                            int pcType = pc._iType + ZutiMDSSection_Objectives.CHIELF_TYPE_INCREMENT;
                            if (pcType == 3) {
                                // Train convoy
                                RRRItem rrrItem = (RRRItem) rrrObjects.get(chiefName);
                                if (rrrItem != null) // Inc counter
                                    rrrItem.count += 1;
                                else {
                                    // Insert new Item
                                    rrrItem = new RRRItem();
                                    rrrItem.name = chiefName;
                                    rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
                                    rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
                                    rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
                                    rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
                                    rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
                                    rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
                                    rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
                                    rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
                                    rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
                                    rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
                                    rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

                                    rrrObjects.put(rrrItem.name, rrrItem);
                                }
                            } else if (pcType == 2) // Car convoy
                                for (int x = 0; x < pc.zutiUnitsNames.length; x++) {
                                    chiefName = pc.zutiUnitsNames[x];
                                    if (chiefName == null) continue;

                                    RRRItem rrrItem = (RRRItem) rrrObjects.get(chiefName);
                                    if (rrrItem != null) // Inc counter
                                        rrrItem.count += 1;
                                    else {
                                        // Insert new Item
                                        rrrItem = new RRRItem();
                                        rrrItem.name = chiefName;
                                        rrrItem.bullets = getRRRObjectResources(objectsMap, rrrItem.name, "bullets");
                                        rrrItem.rockets = getRRRObjectResources(objectsMap, rrrItem.name, "rockets");
                                        rrrItem.bomb250 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb250");
                                        rrrItem.bomb500 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb500");
                                        rrrItem.bomb1000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb1000");
                                        rrrItem.bomb2000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb2000");
                                        rrrItem.bomb5000 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb5000");
                                        rrrItem.bomb9999 = getRRRObjectResources(objectsMap, rrrItem.name, "bomb9999");
                                        rrrItem.fuel = getRRRObjectResources(objectsMap, rrrItem.name, "fuel");
                                        rrrItem.engines = getRRRObjectResources(objectsMap, rrrItem.name, "engines");
                                        rrrItem.repairKits = getRRRObjectResources(objectsMap, rrrItem.name, "repairKits");

                                        rrrObjects.put(rrrItem.name, rrrItem);
                                    }
                                }
                        }
                    }
                }
            }
        }

        return rrrObjects;
    }

    /**
     * Call this method to change bomb cargo properties. Global changes! Based on player side.
     *
     * @param name
     * @param paramId
     * @param value
     * @return
     */
    public static boolean changeBombCargoParameters(RRRItem item, int paramId, long value) {
        if (item.name.indexOf(BOMB_CARGO_NAME) < 0) return false;

        switch (paramId) {
            case 2: {
                item.bullets = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bullets", new Long(value));
                break;
            }
            case 3: {
                item.rockets = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_rockets", new Long(value));
                break;
            }
            case 4: {
                item.bomb250 = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bomb250", new Long(value));
                break;
            }
            case 5: {
                item.bomb500 = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bomb500", new Long(value));
                break;
            }
            case 6: {
                item.bomb1000 = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bomb1000", new Long(value));
                break;
            }
            case 7: {
                item.bomb2000 = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bomb2000", new Long(value));
                break;
            }
            case 8: {
                item.bomb5000 = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bomb5000", new Long(value));
                break;
            }
            case 9: {
                item.bomb9999 = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_bomb9999", new Long(value));
                break;
            }
            case 10: {
                item.fuel = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_fuel", new Long(value));
                break;
            }
            case 11: {
                item.engines = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_engines", new Long(value));
                break;
            }
            case 12: {
                item.repairKits = value;
                DEFAULT_RRR_OBJECT_PROPERTIES.put(item.name + "_repairKits", new Long(value));
                break;
            }
        }

        return true;
    }

    /**
     * This method indicates if any resource management is enabled in the mission or not.
     *
     * @return
     */
    public static boolean resourcesManagementEnabled(ZutiMDSSection_RRR mdsSection_RRR) {
        if (mdsSection_RRR != null) {
            if (mdsSection_RRR.resourcesManagement_Red != null && mdsSection_RRR.resourcesManagement_Red.wTable != null && mdsSection_RRR.resourcesManagement_Red.wTable.items.size() > 2) return true;

            if (mdsSection_RRR.resourcesManagement_Blue != null && mdsSection_RRR.resourcesManagement_Blue.wTable != null && mdsSection_RRR.resourcesManagement_Blue.wTable.items.size() > 2) return true;
        }

        List bornPlaces = ZutiSupportMethods_Builder.getPlMisBornActorsList();
        if (bornPlaces != null) for (int i = 0; i < bornPlaces.size(); i++) {
            ActorBorn actorborn = (ActorBorn) bornPlaces.get(i);

            if (actorborn == null) continue;

            if (actorborn.zutiEnableResourcesManagement && actorborn.resourcesManagement != null && actorborn.resourcesManagement.wTable != null && actorborn.resourcesManagement.wTable.items != null
                    && actorborn.resourcesManagement.wTable.items.size() > 2)
                return true;
        }

        return false;
    }

    /**
     * Check if given coordinates are inside stand alone born place.
     *
     * @param x
     * @param y
     * @return
     */
    private static ActorBorn LAST_STD_ACTOR_BORN = null;

    public static boolean isOnStandAloneBornPlace(double x, double y) {
        if (LAST_STD_ACTOR_BORN != null && LAST_STD_ACTOR_BORN.zutiIsStandAloneBornPlace) {
            double r = LAST_STD_ACTOR_BORN.r * LAST_STD_ACTOR_BORN.r;
            Point3d pos = LAST_STD_ACTOR_BORN.pos.getAbsPoint();
            double tmpDistance = Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2);
            if (tmpDistance <= r) return true;
        }

        ArrayList plugins = Plugin.zutiGetAllActors();
        for (int i = 0; i < plugins.size(); i++) {
            Object object = plugins.get(i);

            if (object instanceof PlMisBorn) {
                PlMisBorn pluginBorn = (PlMisBorn) object;

                for (int j = 0; j < pluginBorn.allActors.size(); j++) {
                    ActorBorn ab = (ActorBorn) pluginBorn.allActors.get(j);
                    if (ab.zutiIsStandAloneBornPlace) {
                        double r = ab.r * ab.r;
                        Point3d pos = ab.pos.getAbsPoint();
                        double tmpDistance = Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2);
                        if (tmpDistance <= r) {
                            LAST_STD_ACTOR_BORN = ab;
                            return true;
                        }
                    }
                }

                break;
            }
        }

        return false;
    }

    /**
     * Puts spawn place indicators on the spawn points on the map.
     *
     * @param stayPlaces
     * @return
     */
    public static boolean loadSpawnPlaceMarkers(Point_Stay[][] stayPlaces) {
        if (stayPlaces == null || stayPlaces.length == 0) return false;

        removeSpawnPlaceMarkers();

        ArrayList plugins = Plugin.zutiGetAllActors();
        Object object = null;
        PlMisStatic pluginStatic = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                break;
            }
        }
        NumberTokenizer numbertokenizer = null;
//		int counter = 0;
        for (int i = 0; i < stayPlaces.length; i++) {
            Point_Stay[] ps = stayPlaces[i];

            numbertokenizer = new NumberTokenizer(getPreparedSpawnPlacePlaceMarkerString(ps));
            pluginStatic.insert(numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String) null),
                    numbertokenizer.next((String) null), numbertokenizer.next((String) null));
//			counter++;
        }
        setToggleSpawnPlaceIndicatorsSwitch(true);

        return true;
    }

    private static String getPreparedSpawnPlacePlaceMarkerString(Point_Stay[] pss) {
        Point_Stay ps1 = null;
        Point_Stay ps2 = null;
        StringBuffer sb = new StringBuffer();
        try {
            double azimut = 0;

            ps1 = pss[pss.length - 1];
            if (pss.length > 1) {
                ps2 = pss[pss.length - 2];
                azimut = getAzimut(ps1.x, ps1.y, ps2.x, ps2.y);
            }
            // 0_Static vehicles.planes.Plane$L2D 2 11787.62 19122.15 360.00 0.0 null
//            sb = new StringBuffer();
            sb.append("NONAME");
            sb.append(" ");
            sb.append("vehicles.planes.Plane$SpawnplaceMarker");// TODO:
            sb.append(" 0 ");
            sb.append(ps1.x);
            sb.append(" ");
            sb.append(ps1.y);
            sb.append(" ");
            sb.append(azimut);
            // sb.append("0.0");
            sb.append(" 0.0 null");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }

    private static void setToggleSpawnPlaceIndicatorsSwitch(boolean value) {
        ArrayList plugins = Plugin.zutiGetAllActors();
        Object object = null;
        PlMisBorn pluginBp = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisBorn) {
                pluginBp = (PlMisBorn) object;
                break;
            }
        }

        if (pluginBp == null) return;

        ActorBorn ab = null;
        for (int i = 0; i < pluginBp.allActors.size(); i++) {
            ab = (ActorBorn) pluginBp.allActors.get(i);
            ab.zutiToggleSpawnPlaceIndicatorsStatus = value;
        }
    }

    /**
     * Load spawn place indicators for selected home base only.
     *
     * @param x
     * @param y
     * @param r
     * @return
     */
    public static boolean loadSpawnPlaceMarkers_BornPlace(Point_Stay[][] stayPlaces, double x, double y, double r) {
        if (stayPlaces == null || stayPlaces.length == 0) return false;

        ArrayList plugins = Plugin.zutiGetAllActors();
        Object object = null;
        PlMisStatic pluginStatic = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                break;
            }
        }

        r = r * r;
        Point_Stay[] pss = null;
        Point_Stay ps = null;
        NumberTokenizer numbertokenizer = null;
        int counter = 0;
        for (int i = 0; i < stayPlaces.length; i++) {
            pss = stayPlaces[i];
            ps = pss[pss.length - 1];
            double tmpDistance = Math.pow(ps.x - x, 2) + Math.pow(ps.y - y, 2);
            if (tmpDistance <= r) {
                numbertokenizer = new NumberTokenizer(getPreparedSpawnPlacePlaceMarkerString(pss));
                pluginStatic.insert(numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F),
                        numbertokenizer.next((String) null), numbertokenizer.next((String) null), numbertokenizer.next((String) null));

                counter++;
            }
        }

        if (counter > 0) // new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("mds.section.info"), (Plugin.i18n("mds.section.spawnPlaceIndicatorsLoaded")), 4, 0.0F);
            return true;
        else new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("mds.section.info"), Plugin.i18n("mds.section.noSpawnPlacesHB"), 4, 0.0F);

        return false;
    }

    /**
     * Remove all spawn place indicators from the map. Only those are left that are on STD home bases.
     *
     * @return
     */
    public static boolean removeSpawnPlaceMarkers() {
        ArrayList plugins = Plugin.zutiGetAllActors();
        Object object = null;
        PlMisStatic pluginStatic = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                break;
            }
        }

        // TODO: Added by SAS~Storebror to avoid null dereference
        if (pluginStatic == null) return true;

        ArrayList actors = pluginStatic.allActors;
        if (actors == null || actors.size() == 0) return true;

        Class spawnPlaceIndicator = generateSpawnPlaceMarkerClass();
        if (spawnPlaceIndicator == null) return false;

        ArrayList newList = new ArrayList();
        Actor actor = null;
        for (int i = 0; i < actors.size(); i++) {
            actor = (Actor) actors.get(i);
            if (actor instanceof SpawnplaceMarker) actor.destroy();
            else newList.add(actor);
        }

        pluginStatic.allActors = newList;
        // new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("mds.section.info"), (Plugin.i18n("mds.section.spawnPlacesIndicatorsCleared")), 4, 0.0F);
        setToggleSpawnPlaceIndicatorsSwitch(false);
        return true;
    }

    /**
     * Remove spawn place indicators that are inside given coordinates.
     *
     * @param x
     * @param y
     * @param r
     * @return
     */
    public static void removeSpawnPlaceMarkers_BornPlace(double x, double y, double r) {
        ArrayList plugins = Plugin.zutiGetAllActors();
        Object object = null;
        PlMisStatic pluginStatic = null;

        for (int i = 0; i < plugins.size(); i++) {
            object = plugins.get(i);
            if (object instanceof PlMisStatic) {
                pluginStatic = (PlMisStatic) object;
                break;
            }
        }

        // TODO: Added by SAS~Storebror to avoid null dereference
        if (pluginStatic == null) return;

        ArrayList actors = pluginStatic.allActors;
        if (actors == null || actors.size() == 0) return;

        Class spawnPlaceIndicator = generateSpawnPlaceMarkerClass();
        if (spawnPlaceIndicator == null) return;

        r = r * r;
        ArrayList newList = new ArrayList();
        Actor actor = null;
        Point3d actorPos = null;
        for (int i = 0; i < actors.size(); i++) {
            actor = (Actor) actors.get(i);
            actorPos = actor.pos.getAbsPoint();
            if (actor instanceof SpawnplaceMarker) {
                double tmpDistance = Math.pow(actorPos.x - x, 2) + Math.pow(actorPos.y - y, 2);
                if (tmpDistance <= r) actor.destroy();
                else newList.add(actor);
            } else newList.add(actor);
        }

        pluginStatic.allActors = newList;
        // new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("mds.section.info"), (Plugin.i18n("mds.section.spawnPlacesIndicatorsClearedHB")), 4, 0.0F);
    }

    /**
     * Generates class that represents actor that is used for spawn place marking - original map spawn places.
     *
     * @return
     */
    public static Class generateSpawnPlaceMarkerClass() {
        Class spawnPlaceIndicator = null;
        try {
            spawnPlaceIndicator = ObjIO.classForName("vehicles.planes.Plane$SpawnplaceMarker");
            return spawnPlaceIndicator;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Generates class that represents actor that is used for spawn place holding - user inserted aircraft.
     *
     * @return
     */
    public static Class generateSpawnPlaceHolderClass() {
        Class spawnPlaceIndicator = null;
        try {
            spawnPlaceIndicator = ObjIO.classForName("vehicles.planes.Plane$Spawnplaceholder");
            return spawnPlaceIndicator;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Get resulting angle based on input coordinates. Point 2 is deducted from point 1. Final result is decreased for 90 degrees because of rotated coordinate system in IL2.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getAzimut(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;

        double azimut = Math.atan2(y, x);
        azimut = Geom.RAD2DEG((float) azimut);

        Orient orient = new Orient();
        orient.setYPR((float) azimut, 0.0F, 0.0F);
        return orient.getAzimut();
    }

    private static Class SPAWN_PLACE_MARKER_CLASS = null;
    private static Class SPAWN_PLACE_HOLDER_CLASS = null;

    /**
     * Call this method to determine of actor over which mouse is hovering can be selected in FMB or not.
     *
     * @param actor
     * @return
     */
    public static boolean canSelectActor(Actor actor) {
        if (actor != null) {
            if (actor instanceof PAirdrome && Plugin.getPlugin("MapAirdrome") == null) return false;

            if (SPAWN_PLACE_MARKER_CLASS == null) SPAWN_PLACE_MARKER_CLASS = ZutiSupportMethods_Builder.generateSpawnPlaceMarkerClass();

            if (actor.getClass().equals(SPAWN_PLACE_MARKER_CLASS)) return false;

            if (SPAWN_PLACE_HOLDER_CLASS == null) SPAWN_PLACE_HOLDER_CLASS = ZutiSupportMethods_Builder.generateSpawnPlaceHolderClass();

            if (actor.getClass().equals(SPAWN_PLACE_HOLDER_CLASS)) {
                Point3d point3d = actor.pos.getAbsPoint();
                if (!ZutiSupportMethods_Builder.isOnStandAloneBornPlace(point3d.x, point3d.y)) return false;
            }
        }

        return true;
    }

    /**
     * This method draws complete airport schematics for all airports on the loaded map in FMB.
     *
     * @param value
     */
    public static void showAirportPoints(boolean value) {
        if (!value) {
            for (int i = 0; i < ZutiSupportMethods_Builder.AIRDROME_POINTS.size(); i++) {
                PathAirdrome pathairdrome = (PathAirdrome) ZutiSupportMethods_Builder.AIRDROME_POINTS.get(i);
                pathairdrome.drawing(false);
            }
            ZutiSupportMethods_Builder.AIRDROME_POINTS.clear();
            return;
        }

        String string = ZutiSupportMethods_Builder.getMapsActorsStaticName();
        if (string == null) {
            System.out.println("Current map has no actors static file!");
            return;
        }

        try {
            DataInputStream datainputstream = new DataInputStream(new SFSInputStream(string));
            boolean bool_6_ = true;
            int i_7_ = datainputstream.readInt();
            if (i_7_ == -65535) {
                bool_6_ = false;
                i_7_ = datainputstream.readInt();
            }
            for (int i_9_ = 0; i_9_ < i_7_; i_9_++) {
                datainputstream.readInt();
                datainputstream.readInt();
                datainputstream.readInt();
                datainputstream.readInt();
                datainputstream.readInt();
                datainputstream.readFloat();
            }
            if (bool_6_) {
                i_7_ = datainputstream.readInt();
                if (i_7_ > 0) {
                    while (i_7_-- > 0)
                        datainputstream.readUTF();
                    i_7_ = datainputstream.readInt();
                    while (i_7_-- > 0) {
                        datainputstream.readInt();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                    }
                }
            } else {
                i_7_ = datainputstream.readInt();
                if (i_7_ > 0) {
                    for (int i_23_ = 0; i_23_ < i_7_; i_23_++)
                        datainputstream.readUTF();
                    i_7_ = datainputstream.readInt();
                    while (i_7_-- > 0) {
                        datainputstream.readInt();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                        datainputstream.readFloat();
                    }
                }
                i_7_ = datainputstream.readInt();
                if (i_7_ > 0) {
                    for (int i_29_ = 0; i_29_ < i_7_; i_29_++)
                        datainputstream.readUTF();
                    int i_31_ = datainputstream.readInt();
                    while (i_31_-- > 0) {
                        datainputstream.readInt();
                        int i_34_ = datainputstream.readInt();
                        while (i_34_-- > 0) {
                            datainputstream.readInt();
                            datainputstream.readInt();
                        }
                    }
                }
            }
            if (datainputstream.available() > 0) {
                Point3d point3d = new Point3d();
                for (int i_45_ = 0; i_45_ < 3; i_45_++) {
                    i_7_ = datainputstream.readInt();
                    while (i_7_-- > 0) {
                        PathAirdrome pathairdrome = new PathAirdrome(Plugin.builder.pathes, i_45_);
                        ZutiSupportMethods_Builder.AIRDROME_POINTS.add(pathairdrome);
                        PAirdrome pairdrome = null;
                        int i_46_ = datainputstream.readInt();
                        for (int i_47_ = 0; i_47_ < i_46_; i_47_++) {
                            point3d.set(datainputstream.readFloat(), datainputstream.readFloat(), 0.0);
                            point3d.z = Engine.land().HQ(point3d.x, point3d.y) + 0.2;
                            pairdrome = new PAirdrome(pathairdrome, pairdrome, point3d, i_45_);
                        }
                    }
                }
            }
            datainputstream.close();

            for (int i = 0; i < ZutiSupportMethods_Builder.AIRDROME_POINTS.size(); i++) {
                PathAirdrome pathairdrome = (PathAirdrome) ZutiSupportMethods_Builder.AIRDROME_POINTS.get(i);
                pathairdrome.drawing(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method draws complete airport schematics for selected home base in FMB.
     *
     * @param points
     * @param value
     * @param x
     * @param y
     * @param r
     */
    public static void showAirportPoints(ArrayList points, boolean value, double x, double y, int r) {
        if (!value && points != null) {
            for (int i = 0; i < points.size(); i++) {
                PathAirdrome pathairdrome = (PathAirdrome) points.get(i);
                pathairdrome.drawing(false);
            }
            return;
        }

        if (value && (points == null || points.size() == 0)) {
            r = r * r;
            String string = ZutiSupportMethods_Builder.getMapsActorsStaticName();
            if (string == null) {
                System.out.println("Current map has no actors static file!");
                return;
            }

            try {
                DataInputStream datainputstream = new DataInputStream(new SFSInputStream(string));
                boolean bool_6_ = true;
                int i_7_ = datainputstream.readInt();
                if (i_7_ == -65535) {
                    bool_6_ = false;
                    i_7_ = datainputstream.readInt();
                }
                for (int i_9_ = 0; i_9_ < i_7_; i_9_++) {
                    datainputstream.readInt();
                    datainputstream.readInt();
                    datainputstream.readInt();
                    datainputstream.readInt();
                    datainputstream.readInt();
                    datainputstream.readFloat();
                }
                if (bool_6_) {
                    i_7_ = datainputstream.readInt();
                    if (i_7_ > 0) {
                        while (i_7_-- > 0)
                            datainputstream.readUTF();
                        i_7_ = datainputstream.readInt();
                        while (i_7_-- > 0) {
                            datainputstream.readInt();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                        }
                    }
                } else {
                    i_7_ = datainputstream.readInt();
                    if (i_7_ > 0) {
                        for (int i_23_ = 0; i_23_ < i_7_; i_23_++)
                            datainputstream.readUTF();
                        i_7_ = datainputstream.readInt();
                        while (i_7_-- > 0) {
                            datainputstream.readInt();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                            datainputstream.readFloat();
                        }
                    }
                    i_7_ = datainputstream.readInt();
                    if (i_7_ > 0) {
                        for (int i_29_ = 0; i_29_ < i_7_; i_29_++)
                            datainputstream.readUTF();
                        int i_31_ = datainputstream.readInt();
                        while (i_31_-- > 0) {
                            datainputstream.readInt();
                            int i_34_ = datainputstream.readInt();
                            while (i_34_-- > 0) {
                                datainputstream.readInt();
                                datainputstream.readInt();
                            }
                        }
                    }
                }
                if (datainputstream.available() > 0) {
                    Point3d point3d = new Point3d();
                    for (int i_45_ = 0; i_45_ < 3; i_45_++) {
                        i_7_ = datainputstream.readInt();
                        while (i_7_-- > 0) {
                            PathAirdrome pathairdrome = new PathAirdrome(Plugin.builder.pathes, i_45_);
                            pathairdrome.drawing(false);
                            PAirdrome pairdrome = null;
                            int i_46_ = datainputstream.readInt();
                            for (int i_47_ = 0; i_47_ < i_46_; i_47_++) {
                                point3d.set(datainputstream.readFloat(), datainputstream.readFloat(), 0.0);
                                point3d.z = Engine.land().HQ(point3d.x, point3d.y) + 0.2;
                                pairdrome = new PAirdrome(pathairdrome, pairdrome, point3d, i_45_);

                                double tmpDistance = Math.pow(point3d.x - x, 2) + Math.pow(point3d.y - y, 2);
                                if (tmpDistance <= r) points.add(pathairdrome);
                            }
                        }
                    }
                }
                datainputstream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (value && points != null && points.size() > 0) {
            for (int i = 0; i < points.size(); i++) {
                PathAirdrome pathairdrome = (PathAirdrome) points.get(i);
                pathairdrome.drawing(true);
            }
            return;
        }
    }

    /**
     * Method returns current maps actors static name. For FMB only.
     *
     * @return
     */
    public static String getMapsActorsStaticName() {
        String s = "maps/" + PlMapLoad.mapFileName();
        SectFile sectfile = new SectFile(s);
        int i = sectfile.sectionIndex("static");
        if (i >= 0 && sectfile.vars(i) > 0) {
            String s1 = sectfile.var(i, 0);
            return HomePath.concatNames(s, s1);
        } else return null;
    }

    /**
     * Create actor object from class full name. Class name example: com.maddox.il2.objects.ships.Ship$SomeShip
     *
     * @param className
     * @return
     */
    public static Actor getActorFromClassName(String className) {
        try {
            Class actorClass = ObjIO.classForName(className);
            ActorSpawnArg spawnArg = new ActorSpawnArg();
            spawnArg.orient = new Orientation(0.0F, 0.0F, 0.0F);
            spawnArg.point = new Point3d(0.0D, 0.0D, 0.0D);
            spawnArg.army = 0;
            spawnArg.armyExist = true;
            Actor actor = ((ActorSpawn) Spawn.get(actorClass.getName(), false)).actorSpawn(spawnArg);

            return actor;
        } catch (Exception ex) {}

        return null;
    }

    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_ZSM_BUILDER", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println(theMessage);
    }

}