/* Note by |ZUTI|: Class version: v3.0 - UP3.0
*/
package com.maddox.il2.game;

import com.maddox.il2.objects.ships.ZutiTypeAircraftCarrier;

public class ZutiStayPoint {
    public com.maddox.il2.ai.air.Point_Stay pointStay              = null;
    public double                           delta_Angle            = 0.0D;
    public double                           distanceFromShipCenter = 0.0D;
    public double                           pribitek               = 0.0D;

    private double                          deckType1_Angles[]     = { 6.12, -5.19, 4.51, -3.99, 3.58, -3.24 };
    private double                          deckType1_Distance[]   = { 70.4, 82.84, 95.3, 107.76, 120.23, 132.71 };

    private double                          deckType2_Angles[]     = { 2.12, -6.52, 2.79, -4.86, 2.16 };
    private double                          deckType2_Distance[]   = { 67.55, 88.07, 102.62, 117.92, 132.59 };

    private double                          deckType3_Angles[]     = { 10.78, -9.46, 7.59 };
    private double                          deckType3_Distance[]   = { 42.76, 48.66, 60.53 };

    private double                          deskType4_Angles[]     = { 33.69, -23.96, 14.93, -12.53, 9.46, -8.43, 6.91, -6.34 };
    private double                          deckType4_Distance[]   = { 14.42, 19.7, 31.05, 36.88, 48.66, 54.59, 66.48, 72.44 };

    private double                          deckType5_Angles[]     = { 0.0, -5.19, 4.09, 0.0, 0.0 };
    private double                          deckType5_Distance[]   = { 37.5, 55.23, 70.18, 87.5, 107.5 };

    private double                          deckType6_Angles[]     = { -19.98, 15.95, -11.31, 9.87, -7.85, 7.13, 0.0, 0.0 };
    private double                          deckType6_Distance[]   = { 29.26, 36.4, 50.99, 58.36, 73.19, 80.62, 97.5, 120.0 };

    private double                          deckType7_Angles[]     = { 10.78, -9.46, 7.59, -6.91, 0.0, 0.0 };
    private double                          deckType7_Distance[]   = { 53.44, 60.83, 75.66, 83.1, 100.0, 122.5 };

    public void PsVsShip(double shipX, double shipY, double shipSpawnYaw, int index, ZutiTypeAircraftCarrier actor) {
        this.pribitek = 180.0D - shipSpawnYaw;

        if (actor.getDeckTypeId() == 1) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType1_Distance.length) index = this.deckType1_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType1_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType1_Angles.length) index = this.deckType1_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deckType1_Angles[index];
        } else if (actor.getDeckTypeId() == 2) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType2_Distance.length) index = this.deckType2_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType2_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType2_Angles.length) index = this.deckType2_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deckType2_Angles[index];
        } else if (actor.getDeckTypeId() == 3) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType3_Distance.length) index = this.deckType3_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType3_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType3_Angles.length) index = this.deckType3_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deckType3_Angles[index];
        } else if (actor.getDeckTypeId() == 4) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType4_Distance.length) index = this.deckType4_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType4_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deskType4_Angles.length) index = this.deskType4_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deskType4_Angles[index];
        } else if (actor.getDeckTypeId() == 5) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType5_Distance.length) index = this.deckType5_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType5_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType5_Angles.length) index = this.deckType5_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deckType5_Angles[index];
        } else if (actor.getDeckTypeId() == 6) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType6_Distance.length) index = this.deckType6_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType6_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType6_Angles.length) index = this.deckType6_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deckType6_Angles[index];
        } else if (actor.getDeckTypeId() == 7) {
            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType7_Distance.length) index = this.deckType7_Distance.length - 1;

            this.distanceFromShipCenter = this.deckType7_Distance[index];

            // +++ MDS Hotfix by Storebror
            if (index >= this.deckType7_Angles.length) index = this.deckType7_Angles.length - 1;

            this.delta_Angle = shipSpawnYaw - this.deckType7_Angles[index];
        }
        // System.out.println("Angle to S(" + shipX + ", " + shipY + "): " + delta_Angle);
    }

    public void PsVsShipRefresh(double shipX, double shipY, double shipYaw) {
        shipYaw += this.pribitek;

        double x_diff = this.distanceFromShipCenter * Math.cos(Math.toRadians(shipYaw + this.delta_Angle));
        double y_diff = this.distanceFromShipCenter * Math.sin(Math.toRadians(shipYaw + this.delta_Angle));

        this.pointStay.set((float) (shipX + x_diff), (float) (shipY + y_diff));
        // System.out.println("Recalculated Stay point location: x=" + pointStay.x + ", y=" + pointStay.y);
    }
}