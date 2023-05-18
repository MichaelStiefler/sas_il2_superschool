package model;

import constants.StringConstants;

/*
 * ---------------------------------------------------------------
 * This class influences how plane/pilot messages are delivered
 * to the server when a destruction event occurs,
 * based on user options.
 * 
 * NOTE: Should be instantiated or reset at the beginning
 * of each mission
 * ---------------------------------------------------------------
 */

public class PlanePilotsStatusMessages {
    public static enum Type {
        NONE, TEAM, ALL
    };

    // where messages are sent
    private PlanePilotsStatusMessages.Type m_iType;

    // number of incidents required before sending
    private int                            m_iNumIncidentsRequired;

    // counters for number of incidents that have occurred
    // since last message was sent
    private int                            m_iIncidentsForRed;
    private int                            m_iIncidentsForBlue;

    // ----------------------- Ctor -----------------------
    //
    // needs the message sending type, and the number of
    // incidents required before sending a message
    // -----------------------------------------------------
    public PlanePilotsStatusMessages(PlanePilotsStatusMessages.Type type, int numIncidentsRequired) {
        m_iType = type;
        m_iNumIncidentsRequired = numIncidentsRequired;
        m_iIncidentsForRed = 0;
        m_iIncidentsForBlue = 0;
    }

    // ----------------------- SendUpdate -----------------------
    //
    // updates the players with plane / pilot status
    // according to admin settings
    // -----------------------------------------------------------
    public String SendUpdate(String team, int redPlanesRemaining, int redPilotsRemaining, int bluePlanesRemaining, int bluePilotsRemaining) {
        // return null string if sending to none
        if (m_iType.equals(PlanePilotsStatusMessages.Type.NONE) || m_iNumIncidentsRequired == 0)
            return StringConstants.strNullMessage;

        // update counters
        if (team.equals(StringConstants.strRedTeam))
            ++m_iIncidentsForRed;
        if (team.equals(StringConstants.strBlueTeam))
            ++m_iIncidentsForBlue;

        // send to all
        if (m_iType.equals(PlanePilotsStatusMessages.Type.ALL) && m_iIncidentsForRed + m_iIncidentsForBlue >= m_iNumIncidentsRequired) {
            // reset counters
            m_iIncidentsForRed = 0;
            m_iIncidentsForBlue = 0;

            // compose message for ALL
            return composerAll(redPlanesRemaining, redPilotsRemaining, bluePlanesRemaining, bluePilotsRemaining);
        }

        // send to appropriate team
        else {
            // red
            if (team.equals(StringConstants.strRedTeam) && m_iIncidentsForRed >= m_iNumIncidentsRequired) {
                // reset counter
                m_iIncidentsForRed = 0;

                // construct red team's plane / pilot count string
                return composerTeam(team, redPlanesRemaining, redPilotsRemaining);
            }
            // blue
            if (team.equals(StringConstants.strBlueTeam) && m_iIncidentsForBlue >= m_iNumIncidentsRequired) {
                // reset counter
                m_iIncidentsForBlue = 0;

                // construct blue team's plane / pilot count string
                return composerTeam(team, bluePlanesRemaining, bluePilotsRemaining);
            }
        }

        return StringConstants.strNullMessage; // should never get here
    }

    // -------------------- message composers --------------------

    private String composerTeam(String team, int planes, int pilots) {
        return composerTeamPlanePilot(team, planes, pilots) + StringConstants.strRemaining + StringConstants.strTo + team;
    }

    private String composerAll(int rPlanes, int rPilots, int bPlanes, int bPilots) {
        return composerTeamPlanePilot(StringConstants.strRedTeam, rPlanes, rPilots) + composerTeamPlanePilot(StringConstants.strBlueTeam, bPlanes, bPilots) + StringConstants.strRemaining + StringConstants.strTo + StringConstants.strALL;
    }

    private String composerTeamPlanePilot(String team, int planes, int pilots) {
        return team + StringConstants.strOpenBrace + StringConstants.strPlanes + planes + StringConstants.strSpace + StringConstants.strPilots + pilots + StringConstants.strCloseBrace;
    }

    // -------------------- member accessors --------------------

    public PlanePilotsStatusMessages.Type MessageType() {
        return m_iType;
    }

    public void setMessageType(PlanePilotsStatusMessages.Type type) {
        m_iType = type;
    }

    public int NumIncidentsRequired() {
        return m_iNumIncidentsRequired;
    }

    public void setNumIncidentsRequired(int numIncidentsRequired) {
        m_iNumIncidentsRequired = numIncidentsRequired;
    }

    public int IncidentsForRed() {
        return m_iIncidentsForRed;
    }

    public int IncidentsForBlue() {
        return m_iIncidentsForBlue;
    }

    // -------------------- counter resets --------------------
    public void ResetRed() {
        m_iIncidentsForRed = 0;
    }

    public void ResetBlue() {
        m_iIncidentsForBlue = 0;
    }

    public void ResetAll() {
        ResetRed();
        ResetBlue();
    }

}
