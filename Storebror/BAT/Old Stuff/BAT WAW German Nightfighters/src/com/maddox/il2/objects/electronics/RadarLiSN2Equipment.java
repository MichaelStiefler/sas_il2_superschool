package com.maddox.il2.objects.electronics;

import com.maddox.JGP.Vector2f;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.TypeRadarLiSN2Carrier;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;

public class RadarLiSN2Equipment {

    private void initEquipment(Cockpit cockpit, int maxNumberOfContacts, float rcsDivisionFactor, float contactRotationAngle, float spikeSlope, float contactMaxMovement, float rangeMinMovement, float rangeMaxMovement) {
        this.cockpit = cockpit;
        this.rangeMaxMovement = rangeMaxMovement;
        this.rangeMinMovement = rangeMinMovement;
        this.contactMaxMovement = contactMaxMovement;
        this.spikeSlope = spikeSlope;
        this.contactRotationAngle = contactRotationAngle;
        this.maxNumberOfContacts = maxNumberOfContacts;
        this.updateRadar = 0;
        this.contacts = new Vector2f[4][this.maxNumberOfContacts];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.maxNumberOfContacts; j++) {
                this.contacts[i][j] = new Vector2f();
            }
        }
        this.radar = new RadarLiSN2();
        this.radar.init(this.cockpit.aircraft(), this.contactMaxMovement, this.rangeMaxMovement, this.maxNumberOfContacts, this.spikeSlope);
        this.oldGain = ((TypeRadarLiSN2Carrier) this.cockpit.aircraft()).getRadarGain();
        this.oldMode = ((TypeRadarLiSN2Carrier) this.cockpit.aircraft()).getRadarMode();
        this.radar.setRCSDivisionFactor(rcsDivisionFactor);
        this.oldTime = Time.current();

        String userLanguage = RTSConf.cur.locale.getLanguage().toLowerCase();
        if (userLanguage == null) {
            userLanguage = "us";
        } else {
            userLanguage = userLanguage.substring(0, 2);
        }
        if (userLanguage == "en") {
            userLanguage = "us";
        }
        if ((!"de".equals(userLanguage)) && (!"fr".equals(userLanguage)) && (!"cs".equals(userLanguage)) && (!"pl".equals(userLanguage)) && (!"hu".equals(userLanguage)) && (!"lt".equals(userLanguage)) && (!"us".equals(userLanguage)) && (!"ru".equals(userLanguage))) {
            userLanguage = "us";
        }
        if ("ru".equals(userLanguage)) {
            languageId = 1;
        } else if ("de".equals(userLanguage)) {
            languageId = 2;
        } else if ("fr".equals(userLanguage)) {
            languageId = 3;
        } else if ("cs".equals(userLanguage)) {
            languageId = 4;
        } else if ("pl".equals(userLanguage)) {
            languageId = 5;
        } else if ("hu".equals(userLanguage)) {
            languageId = 6;
        } else if ("lt".equals(userLanguage)) {
            languageId = 7;
        } else {
            languageId = 0;
        }
    }

    public RadarLiSN2Equipment(Cockpit cockpit) {
        this.initEquipment(cockpit, 28, 155F, 5F, 0.2F, 0.012F, 0.011637F, 0.073296F);
    }

    public RadarLiSN2Equipment(Cockpit cockpit, int maxNumberOfContacts, float rcsDivisionFactor, float contactRotationAngle, float spikeSlope, float contactMaxMovement, float rangeMinMovement, float rangeMaxMovement) {
        this.initEquipment(cockpit, maxNumberOfContacts, rcsDivisionFactor, contactRotationAngle, spikeSlope, contactMaxMovement, rangeMinMovement, rangeMaxMovement);
    }

    public void updateRadar() {
        this.doUpdateRadar();
    }

    private void doUpdateRadar() {
        int newGain = ((TypeRadarLiSN2Carrier) this.cockpit.aircraft()).getRadarGain();
        if (this.oldGain != newGain) {
            if (newGain <= 50) {
                this.radar.setGain(newGain / 50F);
            } else {
                this.radar.setGain(Aircraft.cvt(newGain / 50F, 1.01F, 1.99F, 1F, 8F));
            }
            myHUDLog(hudGain[languageId] + " = " + newGain + "%");
            this.oldGain = newGain;
        }
        int newMode = ((TypeRadarLiSN2Carrier) this.cockpit.aircraft()).getRadarMode();
        if (this.oldMode != newMode) {
            this.radar.setMode(newMode);
            switch (newMode) {
                case RadarLiSN2.RADAR_MODE_NORMAL:
                    this.cockpit.mesh.chunkSetAngles("ZRadarRange", 0F, 0F, 0F);
                    this.cockpit.mesh.materialReplace("RadarDots", "RadarDots");
                    myHUDLog(hudMode[languageId] + " = " + hudNormal[languageId]);
                    break;
                case RadarLiSN2.RADAR_MODE_SHORT:
                    this.cockpit.mesh.chunkSetAngles("ZRadarRange", 0F, -80F, 0F);
                    this.cockpit.mesh.materialReplace("RadarDots", "RadarDotsNear");
                    myHUDLog(hudMode[languageId] + " = " + hudShort[languageId]);
                    break;
                default:
                    myHUDLog(hudMode[languageId] + " = " + hudUnknown[languageId]);
                    break;
            }
            this.oldMode = newMode;
        }

        long timeDiff = Time.current() - this.oldTime;
        int cycleFactor = Math.round(40F / timeDiff);
        if (cycleFactor < 1) {
            cycleFactor = 1;
        }
        int cycleModulo = 2 * cycleFactor;
        int cycleECU = 0;
        int cycleECD = cycleFactor;
        int cycleACL = (int) Math.floor(cycleFactor / 2D);
        int cycleACR = cycleACL + cycleFactor;

        this.oldTime = Time.current();

// System.out.println("Time:" + Time.current() + ", Diff:" + timeDiff + ", cycleFactor:" + cycleFactor + ", cycleECU:" + cycleECU + ", cycleECD:" + cycleECD + ", cycleACL:" + cycleACL + ", cycleACR:" + cycleACR);

        this.updateRadar++;
        this.radar.rareAction();
        this.radar.getContacts(this.contacts);
        float f = World.Rnd().nextFloat();
        int i = World.Rnd().nextInt(5);
        for (int j = 0; j < 6; j++) {
            String sBgN = "BackgroundNoise" + j;
            String sETP = "ETransferPulse" + j;
            String sATP = "ATransferPulse" + j;
            if (j == i) {
                this.cockpit.mesh.chunkVisible(sBgN, true);
                this.cockpit.mesh.chunkVisible(sETP, true);
                this.cockpit.mesh.chunkVisible(sATP, true);
                resetYPRmodifier();
                xyz[1] = -this.rangeMinMovement + (f * 0.0005F);
                this.cockpit.mesh.chunkSetLocate(sETP, xyz, ypr);
                this.cockpit.mesh.chunkSetLocate(sATP, xyz, ypr);
            } else {
                this.cockpit.mesh.chunkVisible(sBgN, false);
                this.cockpit.mesh.chunkVisible(sETP, false);
                this.cockpit.mesh.chunkVisible(sATP, false);
            }
        }

        for (int k = 0; k < this.maxNumberOfContacts; k++) {
            String sECU = "EContact_" + k + "_U";
            String sECD = "EContact_" + k + "_D";
            String sACL = "AContact_" + k + "_L";
            String sACR = "AContact_" + k + "_R";
            this.cockpit.mesh.chunkVisible(sECU, (this.oldMode == RadarLiSN2.RADAR_MODE_NORMAL) || ((this.updateRadar % cycleModulo) == cycleECU));
            this.cockpit.mesh.chunkVisible(sECD, (this.oldMode == RadarLiSN2.RADAR_MODE_NORMAL) || ((this.updateRadar % cycleModulo) == cycleECD));
            this.cockpit.mesh.chunkVisible(sACL, (this.oldMode == RadarLiSN2.RADAR_MODE_NORMAL) || ((this.updateRadar % cycleModulo) == cycleACL));
            this.cockpit.mesh.chunkVisible(sACR, (this.oldMode == RadarLiSN2.RADAR_MODE_NORMAL) || ((this.updateRadar % cycleModulo) == cycleACR));
            xyz[1] = -this.contacts[1][k].x;
            xyz[0] = this.contacts[1][k].y - this.contactMaxMovement;
            ypr[1] = Aircraft.cvt(xyz[0], -this.contactMaxMovement, 0.0F, 0.0F, this.contactRotationAngle);
            this.cockpit.mesh.chunkSetLocate(sECD, xyz, ypr);
            xyz[1] = -this.contacts[0][k].x;
            xyz[0] = this.contacts[0][k].y - this.contactMaxMovement;
            ypr[1] = -Aircraft.cvt(xyz[0], -this.contactMaxMovement, 0.0F, 0.0F, this.contactRotationAngle);
            this.cockpit.mesh.chunkSetLocate(sECU, xyz, ypr);
            xyz[1] = -this.contacts[2][k].x;
            xyz[0] = this.contacts[2][k].y - this.contactMaxMovement;
            ypr[1] = -Aircraft.cvt(xyz[0], -this.contactMaxMovement, 0.0F, 0.0F, this.contactRotationAngle);
            this.cockpit.mesh.chunkSetLocate(sACL, xyz, ypr);
            xyz[1] = -this.contacts[3][k].x;
            xyz[0] = this.contacts[3][k].y - this.contactMaxMovement;
            ypr[1] = Aircraft.cvt(xyz[0], -this.contactMaxMovement, 0.0F, 0.0F, this.contactRotationAngle);
            this.cockpit.mesh.chunkSetLocate(sACR, xyz, ypr);
        }

    }

    private static void myHUDLog(String logLine) {
        if (myHUDLogId == -1) {
            myHUDLogId = HUD.makeIdLog();
        }
        HUD.log(myHUDLogId, logLine);
    }

    private static int      myHUDLogId = -1;
    private static int      languageId = 0;
    private static String[] hudGain    = { "Radar gain", "\u0420\u0430\u0434\u0430\u0440\u043D\u043E\u0435 \u0443\u0441\u0438\u043B\u0435\u043D\u0438\u0435", "Radar Verst\u00E4rkung", "Gain Radar", "Zisk Radar", "Wzmocnienie Radaru", "Nyeres\u00E9g Radar", "Radarai Pelnas" };
    private static String[] hudMode    = { "Radar mode", "\u0420\u0435\u0436\u0438\u043C \u0440\u0430\u0434\u0430\u0440\u0430", "Radar Betriebsart", "Mode Radar", "Re\u017Eim Radar", "Tryb Radarowy", "M\u00F3dusz Radar", "Radarai Re\u017Eimas" };
    private static String[] hudShort   = { "Short Range", "\u0431\u043B\u0438\u0437\u043A\u043E\u0434\u0435\u0439\u0441\u0442\u0432\u0443\u044E\u0449\u0438\u0439", "Nahbereich", "Courte port\u00E9e", "Kr\u00E1tk\u00E9ho Doletu", "Kr\u00F3tki Zasi\u0119g", "R\u00F6vid Hat\u00F3t\u00E1vols\u00E1g\u00FA", "Trumpas Atstumas" };
    private static String[] hudNormal  = { "Default", "\u041F\u043E \u0443\u043C\u043E\u043B\u0447\u0430\u043D\u0438\u044E", "Standard", "D\u00E9faut", "Standardn\u00ED", "Warto\u015B\u0107", "Szabv\u00E1nyos", "Numatytas" };
    private static String[] hudUnknown = { "Unknown", "\u041D\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043D\u044B\u0439", "Unbekannt", "Inconnu", "Nezn\u00E1m\u00FD", "Nieznany", "Ismeretlen", "Nežinomas" };
    // en/us, ru, de, fr, cs, pl, hu, lt

    protected static void resetYPRmodifier() {
        ypr[0] = ypr[1] = ypr[2] = xyz[0] = xyz[1] = xyz[2] = 0.0F;
    }

    private static float[] ypr = { 0.0F, 0.0F, 0.0F };
    private static float[] xyz = { 0.0F, 0.0F, 0.0F };
    private float          rangeMaxMovement;
    private float          rangeMinMovement;
    private float          contactMaxMovement;
    float                  spikeSlope;
    float                  contactRotationAngle;
    int                    maxNumberOfContacts;
    private int            updateRadar;
    private Vector2f       contacts[][];
    private RadarLiSN2     radar;
    private int            oldGain;
    private int            oldMode;
    private long           oldTime;
    private Cockpit        cockpit;
}
