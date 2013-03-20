/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author forssenm
 */
public class SplashScreenController implements ScreenController {

    private Nifty nifty;
    private Element image;

    @Override
    public void bind(final Nifty newNifty, final Screen newScreen) {
        this.nifty = newNifty;
        this.image = newScreen.findElementByName("imageId");
    }

    @Override
    public void onStartScreen() {
        image.startEffect(EffectEventId.onCustom, new FadeInEnd(), "fadeIn");
    }

    @Override
    public void onEndScreen() {
    }

    class FadeInEnd implements EndNotify {

        @Override
        public void perform() {
            System.out.println("fadeIn has ended.");
            image.startEffect(EffectEventId.onCustom, new FadeOutEnd(), "fadeOut");
        }
    }

    class FadeOutEnd implements EndNotify {

        @Override
        public void perform() {
            System.out.println("fadeOut has ended.");
            nifty.fromXml("/xmlgui/MainMenuScreen.xml", "mainMenuScreen" );
        }
    }
}
