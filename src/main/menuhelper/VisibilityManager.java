package main.menuhelper;

import javax.swing.JComponent;
import java.util.List;

public class VisibilityManager {

    // Iterates through a list of components and controls the visibility
    public static void setVisibilityOfComponents(List<JComponent> components, boolean isVisible) {
        for (JComponent component : components) {
            component.setVisible(isVisible);
        }
    }

    // Turns all the components visible
    public static void showComponents(List<JComponent> components) {
        setVisibilityOfComponents(components, true);
    }

    // Turns all the components invisible
    public static void hideComponents(List<JComponent> components) {
        setVisibilityOfComponents(components, false);
    }

}
