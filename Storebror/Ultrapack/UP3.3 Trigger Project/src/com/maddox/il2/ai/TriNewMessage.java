// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.Random;

class TriNewMessage extends Trigger {

    public TriNewMessage(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, int altitudeMin, int altitudeMax, int triggeredBy, boolean hasTriggerActor, int noObjectsMin, int probability, String linkActorName,
            String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        if (this.getDisplayMessage() == "" || this.getDisplayMessage() == null) this.destroy();
    }

    protected boolean checkPeriodic() {
        return super.checkPeriodic();
    }

    protected void execute() {
        Random r = new Random();
        float f = r.nextFloat() * 100F + 1.0F;
        if (f <= super.getProbability()) {
            this.setTriggered(true);
            if (super.getLinkActorName() == "" || super.getLinkActorName() == null) {
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
