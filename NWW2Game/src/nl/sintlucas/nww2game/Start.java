package nl.sintlucas.nww2game;

import java.util.HashMap;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.trixo.engine.graphics.SpriteBatch;
import com.trixo.engine.graphics.Window;
import com.trixo.engine.math.Vector;
import com.trixo.engine.systems.content.ResourceSystem;

import nl.sintlucas.nww2game.screen.WorldScreen;

public class Start {
    private static WorldScreen worldScreen = null;
    private static long lastFrame = 0;

    public static void main(String[] args) {
        Window window = new Window();
        window.init("Game", new Vector(950 * 1.5, 500 * 1.5));
        window.create();

        {
            ResourceSystem.loadResource("textures.particle.spark", "data/assets/textures/spark.png", 0);
            ResourceSystem.loadResource("textures.misc.empty", "data/assets/textures/empty.png", 0);
            
            ResourceSystem.loadResource("shaders.basic.texture",
                    "./data/assets/shaders/BasicTexture.vert, ./data/assets/shaders/BasicTexture.frag", 1);
        }

        SpriteBatch batch = new SpriteBatch();
        batch.init();

        worldScreen = new WorldScreen(2);
        worldScreen.setZIndex(2);
        worldScreen.init(null);

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("spriteBatch", batch);
        data.put("player", worldScreen.getWorld().getPlayer());
        data.put("world", worldScreen.getWorld());

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            data.put("deltatime", getDeltaTime());
            
            {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            worldScreen.update(data);

            {
                batch.begin();
                worldScreen.render(data);
                batch.end();
                batch.render(worldScreen.getWorld().getPlayer().getCamera());
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