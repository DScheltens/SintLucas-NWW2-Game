package nl.sintlucas.nww2game;

import java.util.HashMap;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.trixo.engine.graphics.Camera2D;
import com.trixo.engine.graphics.SpriteBatch;
import com.trixo.engine.graphics.Window;
import com.trixo.engine.math.Vector;
import com.trixo.engine.systems.content.ResourceSystem;

public class Start {
	private static long lastFrame = 0;
	private static Camera2D camera = new Camera2D();
	public static final Double gravConst = 6.673e-11;

	public static void main(String[] args) {
		Window window = new Window();
		window.init("Newton's Law Of Universal Gravitation 2D", new Vector(950 * 1.5, 500 * 1.5));
		window.create();

		{
			ResourceSystem.loadResource("textures.misc.empty", "data/assets/textures/empty.png", 0);

			ResourceSystem.loadResource("shaders.basic.texture",
					"./data/assets/shaders/BasicTexture.vert, ./data/assets/shaders/BasicTexture.frag", 1);
		}

		camera.init((int) Display.getWidth() * 14, (int) Display.getHeight() * 14);

		SpriteBatch batch = new SpriteBatch();
		batch.init();

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("spriteBatch", batch);

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			data.put("deltatime", getDeltaTime());

			{
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}

			{
				camera.update();
			}

			{
				batch.begin();

				batch.end();
				batch.render(camera);
			}

			Display.update();
		}
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static int getDeltaTime() {
		long time = getTime();
		int delta = (int) (time - lastFrame);

		lastFrame = time;

		return delta;
	}
}