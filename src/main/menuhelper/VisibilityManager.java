package main.menuhelper;

import javax.swing.JComponent;
import java.util.List;

public class VisibilityManager {
    public static void setVisibilityOfComponents(List<JComponent> components, boolean isVisible) {
        for (JComponent component : components) {
            component.setVisible(isVisible);
        }
    }

    public static void showComponents(List<JComponent> components) {
        setVisibilityOfComponents(components, true);
    }

    public static void hideComponents(List<JComponent> components) {
        setVisibilityOfComponents(components, false);
    }

}
