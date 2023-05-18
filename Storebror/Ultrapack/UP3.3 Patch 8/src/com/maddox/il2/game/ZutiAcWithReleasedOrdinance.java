package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ZutiAcWithReleasedOrdinance {
    private class ReleasedOrdinance {
        private int ordinanceId  = -1;
        private int ordinanceBay = -1;
        private int nrOfBullets  = 0;
//        String      hashValue    = null; // TODO: Changed by SAS~Storebror, no need to internally recalculate a hash from String each and every time.
        private int hash         = -1;

        public ReleasedOrdinance(int ordinanceId, int ordinanceBay) {
            this.ordinanceId = ordinanceId;
            this.ordinanceBay = ordinanceBay;

//            this.hashValue = this.ordinanceId + "-" + this.ordinanceBay; // TODO: Changed by SAS~Storebror, no need to internally recalculate a hash from String each and every time.
            this.hash = (this.ordinanceId + "-" + this.ordinanceBay).hashCode();
        }

        public int getOrdinanceId() {
            return this.ordinanceId;
        }

        public int getOrdinanceBay() {
            return this.ordinanceBay;
        }

        public int getNrOfBullets() {
            return this.nrOfBullets;
        }

        public void increaseNrOfBullets(int nrOfBullets) {
            this.nrOfBullets += nrOfBullets;
        }

        // TODO: Added by SAS~Storebror for real "equals" comparison and to avoid infinite recursive loop on Object equals comaprison.
        public boolean equals(ReleasedOrdinance r) {
            if (r == null) return false;
            return this.ordinanceId == r.ordinanceId && this.ordinanceBay == r.ordinanceBay && this.nrOfBullets == r.nrOfBullets && this.hash == r.hash;
        }
        // ---

        public boolean equals(Object o) {
            if (o instanceof ReleasedOrdinance) {
                ReleasedOrdinance inKeyObject = (ReleasedOrdinance) o;
                if (this.equals(inKeyObject)) return true;
            }

            return false;
        }

        public int hashCode() {
//            return this.hashValue.hashCode(); // TODO: Changed by SAS~Storebror, no need to internally recalculate a hash from String each and every time.
            return this.hash;
        }
    }

    /**
     * Key=String (AcName), value=ZutiAcWithReleasedOrdinance
     */
    public static Map AC_THAT_RELEASED_BOMBS;

    /**
     * Reset class variables.
     */
    public static void resetClassVariables() {
        if (ZutiAcWithReleasedOrdinance.AC_THAT_RELEASED_BOMBS == null) ZutiAcWithReleasedOrdinance.AC_THAT_RELEASED_BOMBS = new HashMap();
        ZutiAcWithReleasedOrdinance.AC_THAT_RELEASED_BOMBS.clear();
    }

    private String acName;
    private Map    releasedOrdinances = new HashMap();

    public ZutiAcWithReleasedOrdinance() {
        this.acName = null;
    }

    /**
     * Get the name of the aircraft that represents owner of released ordinances.
     *
     * @return
     */
    public String getAcName() {
        return this.acName;
    }

    /**
     * Set the name of the aircraft that contains ordinance actions.
     *
     * @param acName
     */
    public void setAcName(String acName) {
        this.acName = acName;
    }

    /**
     * Call this method when ordinance from an aircraft was released.
     *
     * @param ordinanceId
     * @param ordinanceBay
     * @param nrOfBullets
     */
    public void setOrdinanceThatWasReleased(int ordinanceId, int ordinanceBay, int nrOfBullets) {
        String hashValue = ordinanceId + "-" + ordinanceBay;

        ReleasedOrdinance wr = (ReleasedOrdinance) this.releasedOrdinances.get(hashValue);
        if (wr == null) {
            wr = new ReleasedOrdinance(ordinanceId, ordinanceBay);
            this.releasedOrdinances.put(hashValue, wr);
        }
        wr.increaseNrOfBullets(nrOfBullets);

        // System.out.println("ZutiAcWithReleasedOrdinance - setOrdinanceThatWasReleased: hash=" + wr.hashValue + ", nr=" + wr.getNrOfBullets());
    }

    /**
     * List returned contains string lines. Those lines contain entries that indicate which ordinance was released from an aircraft. They are constructed like: ordinanceId,ordinanceBay,nrOfBullets. These entries are separated between each other with
     * spaces. Each line is aprox 200 characters long.
     *
     * @return
     */
    public List getReleasedOrdinancesList() {
        List lines = new ArrayList();
        StringBuffer sb = new StringBuffer();
        ReleasedOrdinance wr = null;

        Iterator iterator = this.releasedOrdinances.keySet().iterator();
        while (iterator.hasNext()) {
            wr = (ReleasedOrdinance) this.releasedOrdinances.get(iterator.next());
            sb.append(wr.getOrdinanceId());
            sb.append(",");
            sb.append(wr.getOrdinanceBay());
            sb.append(",");
            sb.append(wr.getNrOfBullets());
            sb.append(" ");

            if (sb.toString().length() > 200) {
                lines.add(sb.toString().trim());
                sb = new StringBuffer();
            }
        }

        // Add remainder of the weapon releases
        lines.add(sb.toString().trim());

        return lines;
    }
}