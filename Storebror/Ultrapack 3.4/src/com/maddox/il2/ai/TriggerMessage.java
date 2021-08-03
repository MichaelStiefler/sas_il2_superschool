// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import com.maddox.sas1946.il2.util.TrueRandom;

class TriggerMessage extends Trigger {

    public TriggerMessage(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability, String linkActorName,
            String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTriggerClass(TYPE_MESSAGE);
        if (this.getDisplayMessage() == "" || this.getDisplayMessage() == null) this.destroy();
    }

    protected void execute() {
        if (TrueRandom.nextFloat(100F) < this.getProbability()) this.doExecute();
        super.execute();
    }
    
    protected void doExecute() {
        if (this.isTriggered()) return;
        this.setTriggered(true);
        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
            EventLog.onTriggerActivate(null, this);
            this.doSendMsg(false);
        } else {
            EventLog.onTriggerActivateLink(null, this);
            this.doSendMsg(true);
        }
        super.doExecute();
    }
}
