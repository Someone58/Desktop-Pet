package com.example.desktoppet.Controller;

import com.example.desktoppet.Interfaces.MiniTimerWindowInterface;
import com.example.desktoppet.Model.PetData;
import com.example.desktoppet.UI.MiniTimerWindowGUI;

/**
 * Handles Mini Timer Window functionality, delegating UI operations to MiniTimerWindowInterface.
 */
public class MiniTimerWindow {
    private static MiniTimerWindowInterface miniTimerUI = null; // Only one instance allowed

    /**
     * Creates a new mini timer window or brings the existing one to front
     * @param timer the timer logic instance
     * @param petController the pet controller instance
     */
    public MiniTimerWindow(Timer timer, PetData petController) {
        // If a mini timer window already exists and is showing, bring it to front
        if (miniTimerUI != null && miniTimerUI.isShowing()) {
            miniTimerUI.toFront();
            return;
        }
        
        // Create a new mini timer window UI
        miniTimerUI = new MiniTimerWindowGUI();
        miniTimerUI.createMiniTimerWindow(timer, petController);
    }
    
    /**
     * Get the mini timer window UI implementation
     * @return the mini timer window UI interface
     */
    public static MiniTimerWindowInterface getMiniTimerUI() {
        return miniTimerUI;
    }
}
