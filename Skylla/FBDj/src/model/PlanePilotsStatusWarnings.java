package model;

import java.util.ArrayList;
import java.util.Collections;

import constants.StringConstants;

/*
 * ---------------------------------------------------------------
 * Sends warning to team when they reach low count milestones
 * 
 * NOTE: Should be instantiated or reset at the beginning
 * of each mission
 * ---------------------------------------------------------------
 */

public class PlanePilotsStatusWarnings {
    public static enum Type {
        NONE, TEAM, ALL
    };

    // holds the milestones
    private ArrayList<Integer>     m_alMilestones;

    // stores the current position in the array
    private int                    m_iRedMilestonePlanePosition;
    private int                    m_iRedMilestonePilotPosition;
    private int                    m_iBlueMilestonePlanePosition;
    private int                    m_iBlueMilestonePilotPosition;

    // where to send the messages
    PlanePilotsStatusWarnings.Type m_Type;

    // ----------------------------- ctor -----------------------------
    //
    // needs an ArrayList of Integers containing the milestones
    // for when to send warnings, which is sorted in descending order
    // i.e. { 20, 10, 5, 2, 1 }
    //
    // and needs to know where to send the warnings
    // ----------------------------------------------------------------
    public PlanePilotsStatusWarnings(ArrayList<Integer> milestones, PlanePilotsStatusWarnings.Type type) {
        // copy arraylist and sort descending
        m_alMilestones = new ArrayList<Integer>(milestones);
        Collections.sort(m_alMilestones, Collections.reverseOrder());

        // recipient
        m_Type = type;

        // array positions
        m_iRedMilestonePlanePosition = 0;
        m_iRedMilestonePilotPosition = 0;
        m_iBlueMilestonePlanePosition = 0;
        m_iBlueMilestonePilotPosition = 0;
    }

    // ---------------------- CheckForPlaneWarning ----------------------
    //
    // needs the team string being checked, and the number of
    // planes that team has remaining
    //
    // if null string ("") is returned, then nothing needs to be sent
    // to the server
    // ------------------------------------------------------------------
    public String CheckForPlaneWarning(String team, int planes) {
        // if not sending warnings, just return null string
        if (m_Type.equals(PlanePilotsStatusWarnings.Type.NONE))
            return StringConstants.strNullMessage;

        // set appropriate sending TO
        String sendTo = SendTo(team);

        // red team warnings
        if (team.equals(StringConstants.strRedTeam)) {
            if (CheckMilestone(m_iRedMilestonePlanePosition, planes)) {
                ++m_iRedMilestonePlanePosition;
                return ComposeWarning(team, "planes", planes, sendTo);
            }
        }

        // blue team warnings
        else if (team.equals(StringConstants.strBlueTeam)) {
            if (CheckMilestone(m_iBlueMilestonePlanePosition, planes)) {
                ++m_iBlueMilestonePlanePosition;
                return ComposeWarning(team, "planes", planes, sendTo);
            }
        }

        return StringConstants.strNullMessage;
    }

    // ----------------------- CheckForPilotWarning -----------------------
    //
    // needs the team string being checked, and the number of
    // pilots that team has remaining
    //
    // if null string ("") is returned, then nothing needs to be sent
    // to the server
    // --------------------------------------------------------------------
    public String CheckForPilotWarning(String team, int pilots) {
        // if not sending warnings, just return null string
        if (m_Type.equals(PlanePilotsStatusWarnings.Type.NONE))
            return StringConstants.strNullMessage;

        // set appropriate sending TO
        String sendTo = SendTo(team);

        // red team warnings
        if (team.equals(StringConstants.strRedTeam)) {
            if (CheckMilestone(m_iRedMilestonePilotPosition, pilots)) {
                ++m_iRedMilestonePilotPosition;
                return ComposeWarning(team, "pilots", pilots, sendTo);
            }
        }

        // blue team warnings
        else if (team.equals(StringConstants.strBlueTeam)) {
            if (CheckMilestone(m_iBlueMilestonePilotPosition, pilots)) {
                ++m_iBlueMilestonePilotPosition;
                return ComposeWarning(team, "pilots", pilots, sendTo);
            }
        }

        return StringConstants.strNullMessage;
    }

    // --------------------- SendTo ---------------------
    //
    // returns appropriate send TO
    // --------------------------------------------------
    private String SendTo(String team) {
        if (m_Type.equals(PlanePilotsStatusWarnings.Type.TEAM))
            return team;
        else
            return StringConstants.strALL;
    }

    // --------------------- CheckMilestone ---------------------
    //
    // returns true if milestone reached
    // ----------------------------------------------------------
    private boolean CheckMilestone(int position, int count) {
        // make sure we don't overrun the array
        if (m_alMilestones.size() - 1 < position)
            return false;

        return (m_alMilestones.get(position) >= count);
    }

    // --------------------- ComposeWarning ---------------------
    //
    // generate the warning string
    // ----------------------------------------------------------
    private String ComposeWarning(String team, String msgType, int count, String recipient) {
        return StringConstants.strWarning + team + StringConstants.strHas + count + StringConstants.strSpace + msgType + StringConstants.strSpace + StringConstants.strRemaining + StringConstants.strTo + recipient;
    }

    // -------------------- counter resets --------------------
    public void ResetRed() {
        m_iRedMilestonePlanePosition = 0;
        m_iRedMilestonePilotPosition = 0;
    }

    public void ResetBlue() {
        m_iBlueMilestonePlanePosition = 0;
        m_iBlueMilestonePilotPosition = 0;
    }

    public void ResetAll() {
        ResetRed();
        ResetBlue();
    }

}
