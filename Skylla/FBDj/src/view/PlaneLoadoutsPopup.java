package view;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PlaneLoadoutsPopup extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -3101062829890991890L;
    public Component          topComponent;
    public Component          bottomComponent;

    public PlaneLoadoutsPopup(Component tc, Component mic) {
        topComponent = tc;
        bottomComponent = mic;
        doMyLayout();
    }

    protected void doMyLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(topComponent);
        add(bottomComponent);
    }

}
