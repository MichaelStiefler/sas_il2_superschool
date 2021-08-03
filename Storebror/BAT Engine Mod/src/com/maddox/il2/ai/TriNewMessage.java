package com.maddox.il2.ai;

import java.util.Random;

class TriNewMessage extends Trigger {

    public TriNewMessage(String zname, int i, int j, int posx, int posy, int r, int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, String zsLink, String sTextDisplay, int zTextDuree) {
        super(zname, i, j, posx, posy, r, zmin, zmax, ziaHumans, bSortie, zAvionMin, zProba, zsLink, sTextDisplay, zTextDuree);
        if ((super.textDisplay == "") || (super.textDisplay == null)) {
            this.destroy();
        }
    }

    protected boolean checkPeriodic() {
        return super.checkPeriodic();
    }

    protected void execute() {
        Random r = new Random();
        float f = (r.nextFloat() * 100F) + 1.0F;
        if (f <= super.proba) {
            super.declanche = true;
            if ((super.sLink == "") || (super.sLink == null)) {
                EventLog.onTriggerActivate(null, this);
                this.doSendMsg(false);
            } else {
                EventLog.onTriggerActivateLink(null, this);
                this.doSendMsg(true);
            }
        }
        super.execute();
    }

    public void destroy() {
        super.destroy();
    }
}
