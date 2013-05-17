/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package niftyController;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Mathias
 */
public class HighscoreScreenController implements ScreenController {

    private Nifty nifty;
    private Element image;

    @Override
    public void bind(final Nifty newNifty, final Screen newScreen) {
        this.nifty = newNifty;
        //System.out.println("Startar screenen");
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}