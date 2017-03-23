//from https://github.com/kwhat/jnativehook/wiki/Keyboard

package captchasolver;

import java.awt.event.KeyEvent;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {
	public void nativeKeyPressed(NativeKeyEvent e) {
		//System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Enter")) {
			System.out.println("Enter pressed");
			CaptchaHandlerUI.openAndTakeNext();
		}
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public static void run() {
		try {
			GlobalScreen.registerNativeHook();

			GlobalKeyListener listener = new GlobalKeyListener();

			// Get the logger for "org.jnativehook" and set the level to off.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);

			// Change the level for all handlers attached to the default logger.
			Handler[] handlers = Logger.getLogger("").getHandlers();
			for (int i = 0; i < handlers.length; i++) {
				handlers[i].setLevel(Level.OFF);
			}

			GlobalScreen.addNativeKeyListener(listener);

		} catch (NativeHookException ex) {
			System.err.println(ex.getMessage());
		}
	}
}