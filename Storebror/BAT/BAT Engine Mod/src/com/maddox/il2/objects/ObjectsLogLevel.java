package com.maddox.il2.objects;

import com.maddox.rts.IniFile;

public final class ObjectsLogLevel {
    private ObjectsLogLevel() {}
    
    public static int getObjectsLogLevel() {
        if (logLevel != OBJECTS_LOGLEVEL_NOT_INITIALIZED)
            return logLevel;

        IniFile iniFile = new IniFile("conf.ini", 0);
        logLevel = iniFile.get(OBJECTS_LOGLEVEL_SECTION, OBJECTS_LOGLEVEL_KEY, OBJECTS_LOGLEVEL_NONE);
        return logLevel;
    }
    
    static 
    {
        getObjectsLogLevel();
    }

    private static final String OBJECTS_LOGLEVEL_SECTION         = "Mods";
    private static final String OBJECTS_LOGLEVEL_KEY             = "ObjectsLogLevel";
    public static final int     OBJECTS_LOGLEVEL_NONE            = 0;
    public static final int     OBJECTS_LOGLEVEL_SHORT           = 1;
    public static final int     OBJECTS_LOGLEVEL_FULL            = 2;
    private static final int    OBJECTS_LOGLEVEL_NOT_INITIALIZED = -1;
    private static int          logLevel                         = OBJECTS_LOGLEVEL_NOT_INITIALIZED;
}
