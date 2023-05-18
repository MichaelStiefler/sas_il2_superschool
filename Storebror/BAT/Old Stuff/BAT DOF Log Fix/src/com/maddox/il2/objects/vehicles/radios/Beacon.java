package com.maddox.il2.objects.vehicles.radios;

public abstract class Beacon {
// public static class Radio_Paris extends BeaconGeneric
// implements TypeHasBeacon, TypeHasRadioStation
// {
// public String getStationID()
// {
// return "Radio Paris";
// }
// }
// public static class Radio_Tokyo extends BeaconGeneric
// implements TypeHasBeacon, TypeHasRadioStation
// {
// public String getStationID()
// {
// return "Radio Tokyo";
// }
// }
    public static class Grossdeutscher_Rundfunk extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "Grossdeutscher Rundfunk";
        }
    }

    public static class Radio_Roma extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "Radio Roma";
        }
    }

    public static class Magyar_Radio extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "Magyar Radio";
        }
    }

    public static class Radio_Moscow extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "Radio Moscow";
        }
    }

    public static class Radio_BBC extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "BBC";
        }
    }

    public static class Radio_Suomen_Yleisradio extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "Suomen Yleisradio";
        }
    }

    public static class Radio_Honolulu extends BeaconGeneric implements TypeHasBeacon, TypeHasRadioStation {
        public String getStationID() {
            return "Radio Honolulu";
        }
    }

    public static class YGBeacon extends RotatingYGBeacon implements TypeHasBeacon, TypeHasYGBeacon {
    }

    public static class LorenzBLBeacon_AAIAS extends LorenzBLBeacon implements TypeHasAAFIAS {
    }

    public static class LorenzBLBeacon_LongRunway extends LorenzBLBeacon {
    }

    public static class LorenzBLBeacon extends LorenzBlindLandingBeacon implements TypeHasBeacon, TypeHasLorenzBlindLanding {
    }

    public static class Meacon extends BeaconGeneric implements TypeHasMeacon {
    }

    public static class RadioBeaconLowVis extends BeaconGeneric implements TypeHasBeacon {
    }

    public static class RadioBeacon extends BeaconGeneric implements TypeHasBeacon {
    }

    public static String getBeaconID(int i) {
        if (i == -1) {
            return "";
        }
        if (i < beaconIDStrings.length) {
            return beaconIDStrings[i];
        } else {
            return "XY";
        }
    }

    private static final String beaconIDStrings[] = { "AM", "AR", "BD", "BO", "CA", "CM", "CT", "FC", "GR", "GT", "HK", "JU", "LA", "LH", "MA", "MG", "MK", "MW", "OM", "PS", "RG", "RU", "ST", "SQ", "TA", "VI", "WI", "WP", "YO", "ZB", "ZO", "ZU" };
    static {
        new BeaconGeneric.SPAWN(RadioBeacon.class);
        new BeaconGeneric.SPAWN(RadioBeaconLowVis.class);
        new BeaconGeneric.SPAWN(Meacon.class);
        new BeaconGeneric.SPAWN(LorenzBLBeacon.class);
        new BeaconGeneric.SPAWN(LorenzBLBeacon_LongRunway.class);
        new BeaconGeneric.SPAWN(LorenzBLBeacon_AAIAS.class);
        new BeaconGeneric.SPAWN(YGBeacon.class);
        new BeaconGeneric.SPAWN(Radio_Honolulu.class);
        new BeaconGeneric.SPAWN(Radio_Suomen_Yleisradio.class);
        new BeaconGeneric.SPAWN(Radio_BBC.class);
        new BeaconGeneric.SPAWN(Radio_Moscow.class);
        new BeaconGeneric.SPAWN(Magyar_Radio.class);
        new BeaconGeneric.SPAWN(Radio_Roma.class);
        new BeaconGeneric.SPAWN(Grossdeutscher_Rundfunk.class);
// new BeaconGeneric.SPAWN(Radio_Tokyo.class);
// new BeaconGeneric.SPAWN(Radio_Paris.class);
    }
}
