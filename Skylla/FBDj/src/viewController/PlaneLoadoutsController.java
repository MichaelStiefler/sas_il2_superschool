package viewController;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import mainController.IL2PlaneLoadoutController;
import mainController.MainController;
import model.MissionParameters;
import model.PlaneLoadoutRestriction;
import model.Weapon;
import utility.CheckBoxList;
import view.MissionBuilderPanel;
import view.PlaneLoadoutsPopup;

public class PlaneLoadoutsController {

    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    public static void displayLoadouts(int army, String plane) {
        String armyDesc = "Unknown";
        if (army == MainController.REDARMY) {
            armyDesc = "Red";
        } else if (army == MainController.BLUEARMY) {
            armyDesc = "Blue";
        }
        MissionParameters missionParameters = MissionBuilderPanel.getMissionDetails();
        PlaneLoadoutRestriction weaponsDescription = null;
        JList list = new CheckBoxList();
        DefaultListModel defModel = new DefaultListModel();
        list.setModel(defModel);
        ArrayList<Weapon> planeLoadouts = new ArrayList<Weapon>();
        planeLoadouts = IL2PlaneLoadoutController.getPlaneWeapons(plane);
        ArrayList<Integer> selectedWeaponsList = new ArrayList<Integer>();
        if (planeLoadouts != null) {
            for (int i = 0; i < planeLoadouts.size(); i++) {
                defModel.addElement(planeLoadouts.get(i).getWeaponDescription());
                weaponsDescription = missionParameters.getPlaneLoadoutRestriction(army, plane, planeLoadouts.get(i).getWeapon());
                if (weaponsDescription != null) {
                    selectedWeaponsList.add(i);
                }
            }
            for (int i = 0; i < selectedWeaponsList.size(); i++) {
                list.setSelectedIndex((Integer) selectedWeaponsList.get(i));
            }

            JOptionPane pane = new JOptionPane("Check the Weapon Loadout(s) that are\n " + "to be restricted for " + armyDesc + "-( " + plane + " )", JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog = pane.createDialog(null, "Weapon Loadouts");
            Container grabbedContent = dialog.getContentPane();
            JScrollPane scroller = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            PlaneLoadoutsPopup mip = new PlaneLoadoutsPopup(scroller, grabbedContent);
            dialog.setContentPane(mip);
            dialog.pack();
            dialog.setVisible(true);
            missionParameters.removeLoadoutRestrictionsForPlane(army, plane);
            Object[] selectedWeapons = list.getSelectedValues();
            Iterator<Object> it = Arrays.asList(selectedWeapons).iterator();
            while (it.hasNext()) {
                String weaponDesc = (String) it.next();
                Weapon weapon = MainController.getWeapon(plane, weaponDesc);
                missionParameters.addPlaneLoadoutRestriction(army, plane, weapon.getWeapon());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Loadouts found for Plane Selected(" + plane + ")", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
