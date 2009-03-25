package com.explodingpixels.macwidgets;

import com.explodingpixels.util.PlatformUtils;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class PreferencesTabBar {

//    private List<AbstractButton> fTabs = new ArrayList<AbstractButton>();

    private TriAreaComponent fPreferencesTabBar = new TriAreaComponent();

    private ButtonGroup fButtonGroup = new ButtonGroup();

    public PreferencesTabBar() {
        Border b = BorderFactory.createEmptyBorder(0, 4, 0, 4);
        fPreferencesTabBar.getComponent().setBorder(b);
        UnifiedToolBar.installUnifiedToolBarBorder(fPreferencesTabBar.getComponent());
        fixUnifiedToolBarOnMacIfNeccessary(fPreferencesTabBar);
    }

    public void addTab(String title, Icon icon, ActionListener listener) {
        AbstractButton button = MacButtonFactory.makePreferencesTabBarButton(
                new JToggleButton(title, icon));
        fButtonGroup.add(button);
        button.addActionListener(listener);

        fPreferencesTabBar.addComponentToLeft(button);
    }

    public void showTab(String title) {
        getButton(title).doClick();
    }

    public void installWindowDraggerOnWindow(Window window) {
        fPreferencesTabBar.installWindowDraggerOnWindow(window);
    }

    public JComponent getComponent() {
        return fPreferencesTabBar.getComponent();
    }

    private AbstractButton getButton(String title) {
        AbstractButton retVal = null;

        Enumeration<AbstractButton> buttons = fButtonGroup.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.getText().equals(title)) {
                retVal = button;
                break;
            }
        }

        checkButtonFound(retVal);

        return retVal;
    }

    private static void checkButtonFound(AbstractButton button) {
        if (button == null) {
            throw new IllegalArgumentException(
                    "The given button title does not represent a preferences tab.");
        }
    }

    /**
     * Installs a custom painter on the given {@link TriAreaComponent} that paints the Mac style
     * unified toolbar gradient on non-Mac platforms as well as Mac platforms running using Java 6.
     *
     * @param unifiedToolBar the {@link TriAreaComponent} to install the custom painter on if
     *                       necessary.
     */
    private static void fixUnifiedToolBarOnMacIfNeccessary(TriAreaComponent unifiedToolBar) {
        // install the custom painter if on non-Mac platforms or on a Mac running Java 1.6
        // (the version of Java with the textured window bug).
        if (!PlatformUtils.isMac() || PlatformUtils.isJava6OnMac()) {
            unifiedToolBar.setBackgroundPainter(MacPainterFactory.createTexturedWindowWorkaroundPainter());
        }
    }
}
