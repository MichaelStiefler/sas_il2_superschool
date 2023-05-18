package model;

/*
 * Stores pilot information
 */

public class PilotBlueprint {

    public enum ValidStates {
        CONNECTED, SORTIEBEGIN, INFLIGHT, REFLY, DISCONNECTED, KICK
    };

    // From connection (constructor variables)
    String      ipAddress        = "0.0.0.0";
    int         socket           = 0;
    long        connectTime;
    String      name;

    // Admin
    int         badLogin;

    // From 'user'
    int         ping;
    int         score            = 0;
    int         army             = 0;
    String      planeMarkings    = "Unknown";

    // From 'user STAT'
    int         eAir, fAir;
    int         fiBull, hiBull, hiABull;
    int         fiRock, hiRock;
    int         fiBomb, hiBomb;

    // From eventlog
    String      weapons;
    String      fuel;
    String      plane            = "Unknown";
    Aerodrome   aerodrome        = null;
    ValidStates state            = ValidStates.CONNECTED;
    int         eAirConfirmed;
    int         eSAir, eTank, eCar, eArt, eAaa, eWag, eShip;
    int         fSAir, fTank, fCar, fArt, fAaa, fWag, fShip;
    int         fGroundUnit      = 0;
    int         eGroundUnit      = 0;
    boolean     planeIsOKToFly   = false;
    String      lastPlaneFlown   = "Unknown";
    int         playerPosition   = 0;

    // From GeoIP
    String      country          = "N/A";
    String      countryCode      = "N/A";

    // From Stats
    int         pilotId;
    int         warningPoints    = 0;
    boolean     pilotInWatchList = false;

    // Computed
    int         deathCount       = 0;
    int         badWordCount     = 0;
    String      asciiTextName;

    public int getBadWordCount() {
        return badWordCount;
    }

    public void setBadWordCount(int badWordCount) {
        this.badWordCount = badWordCount;
    }

    public int getArmy() {
        return army;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public int getBadLogin() {
        return this.badLogin;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public String getCountry() {
        return this.country;
    }

    public int getEAaa() {
        return eAaa;
    }

    public int getEAir() {
        return eAir;
    }

    public int getEAirConfirmed() {
        return eAirConfirmed;
    }

    public int getEArt() {
        return eArt;
    }

    public int getECar() {
        return eCar;
    }

    public int getESAir() {
        return eSAir;
    }

    public int getEShip() {
        return eShip;
    }

    public int getETank() {
        return eTank;
    }

    public int getEWag() {
        return eWag;
    }

    public int getFAaa() {
        return fAaa;
    }

    public int getFAir() {
        return fAir;
    }

    public int getFArt() {
        return fArt;
    }

    public int getFCar() {
        return fCar;
    }

    public int getFSAir() {
        return fSAir;
    }

    public int getFShip() {
        return fShip;
    }

    public int getFTank() {
        return fTank;
    }

    public int getFWag() {
        return fWag;
    }

    public int getFiBomb() {
        return fiBomb;
    }

    public int getFiBull() {
        return fiBull;
    }

    public int getFiRock() {
        return fiRock;
    }

    public String getFuel() {
        return fuel;
    }

    public int getHiABull() {
        return hiABull;
    }

    public int getHiBomb() {
        return hiBomb;
    }

    public int getHiBull() {
        return hiBull;
    }

    public int getHiRock() {
        return hiRock;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    public String getLastPlaneFlown() {
        return lastPlaneFlown;
    }

    public String getName() {
        return name;
    }

    public int getPing() {
        return ping;
    }

    public String getPlane() {
        return plane;
    }

    public boolean getPlaneIsOKToFly() {
        return planeIsOKToFly;
    }

    public String getPlaneMarkings() {
        return planeMarkings;
    }

    public int getScore() {
        return score;
    }

    public int getSocket() {
        return socket;
    }

    public ValidStates getState() {
        return state;
    }

    public String getWeapons() {
        return weapons;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public void setBadLogin(int badLogin) {
        this.badLogin = badLogin;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEAaa(int eAaa) {
        this.eAaa = eAaa;
    }

    public void setEAir(int eAir) {
        this.eAir = eAir;
    }

    public void setEAirConfirmed(int eAirConfirmed) {
        this.eAirConfirmed = eAirConfirmed;
    }

    public void setEArt(int eArt) {
        this.eArt = eArt;
    }

    public void setECar(int eCar) {
        this.eCar = eCar;
    }

    public void setESAir(int eSAir) {
        this.eSAir = eSAir;
    }

    public void setEShip(int eShip) {
        this.eShip = eShip;
    }

    public void setETank(int eTank) {
        this.eTank = eTank;
    }

    public void setEWag(int eWag) {
        this.eWag = eWag;
    }

    public void setFAaa(int fAaa) {
        this.fAaa = fAaa;
    }

    public void setFAir(int fAir) {
        this.fAir = fAir;
    }

    public void setFArt(int fArt) {
        this.fArt = fArt;
    }

    public void setFCar(int fCar) {
        this.fCar = fCar;
    }

    public void setFSAir(int fSAir) {
        this.fSAir = fSAir;
    }

    public void setFShip(int fShip) {
        this.fShip = fShip;
    }

    public void setFTank(int fTank) {
        this.fTank = fTank;
    }

    public void setFWag(int fWag) {
        this.fWag = fWag;
    }

    public void setFiBomb(int fiBomb) {
        this.fiBomb = fiBomb;
    }

    public void setFiBull(int fiBull) {
        this.fiBull = fiBull;
    }

    public void setFiRock(int fiRock) {
        this.fiRock = fiRock;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setHiABull(int hiABull) {
        this.hiABull = hiABull;
    }

    public void setHiBomb(int hiBomb) {
        this.hiBomb = hiBomb;
    }

    public void setHiBull(int hiBull) {
        this.hiBull = hiBull;
    }

    public void setHiRock(int hiRock) {
        this.hiRock = hiRock;
    }

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public void setLastPlaneFlown(String plane) {
        this.lastPlaneFlown = plane;
    }

    public void setPlaneIsOKToFly(boolean okToFly) {
        this.planeIsOKToFly = okToFly;
    }

    public void setPlaneMarkings(String planeMarkings) {
        this.planeMarkings = planeMarkings;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }

    public void setState(ValidStates state) {
        this.state = state;
    }

    public void setWeapons(String weapons) {
        this.weapons = weapons;
    }

    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public Aerodrome getAerodrome() {
        return aerodrome;
    }

    public void setAerodrome(Aerodrome aerodrome) {
        this.aerodrome = aerodrome;
    }

    public int getFGroundUnit() {
        return fGroundUnit;
    }

    public void setFGroundUnit(int groundUnit) {
        fGroundUnit = groundUnit;
    }

    public int getEGroundUnit() {
        return eGroundUnit;
    }

    public void setEGroundUnit(int groundUnit) {
        eGroundUnit = groundUnit;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public String getAsciiTextName() {
        return asciiTextName;
    }

    public void setAsciiTextName(String asciiTextName) {
        this.asciiTextName = asciiTextName;
    }

}
