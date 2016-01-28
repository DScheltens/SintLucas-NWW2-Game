package com.trixo.engine.test;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.examples.spaceinvaders.SoundManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import com.trixo.engine.entity.Entity;
import com.trixo.engine.graphics.Camera2D;
import com.trixo.engine.graphics.SpriteBatch;
import com.trixo.engine.graphics.Window;
import com.trixo.engine.math.Vector;
import com.trixo.engine.math.Vector4;
import com.trixo.engine.physics.CollisionManager;
import com.trixo.engine.systems.content.ResourceSystem;
import com.trixo.engine.systems.particle.ParticleEmitter;
import com.trixo.engine.utils.EventUtils.KeyboardEvents;
import com.trixo.engine.utils.EventUtils.MouseEvents;

public class Start {
	public static SoundManager soundManager = null;
	private static Camera2D camera = new Camera2D();
	private static ArrayList<ParticleEmitter> particleEmitters = new ArrayList<ParticleEmitter>();
	private static CollisionManager cManager = new CollisionManager();

	public static void main(String[] args) {
		Window window = new Window();
		window.init("Test", new Vector(950, 500));
		window.create();

		{
			ResourceSystem.loadResource("textures.background.space", "data/assets/textures/bg.png", 0);
			ResourceSystem.loadResource("textures.particle.spark", "data/assets/textures/spark.png", 0);
			ResourceSystem.loadResource("textures.misc.empty", "data/assets/textures/empty.png", 0);

			ResourceSystem.loadResource("shaders.basic.texture",
					"./data/assets/shaders/BasicTexture.vert, ./data/assets/shaders/BasicTexture.frag", 1);
		}

		SpriteBatch batch = new SpriteBatch();
		batch.init();

		camera.init((int) window.getDimensions().x, (int) window.getDimensions().y);

		byte color[] = { (byte) 255, (byte) 255, (byte) 255, (byte) 255 };
		Texture tex = (Texture) ResourceSystem.getResource("textures.background.space");

		float speed = 1.0F;

		Entity e1 = new Entity(0, new Vector4(250, 250, 50, 50));
		Entity player = new Entity(1, new Vector4(0, 0, 50, 50));

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			{
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			}

			camera.update();
			
			Vector mp = MouseEvents.getPosition();
			mp = camera.convertScreenToWorld((float) mp.x, (float) mp.y);

			{
				if (KeyboardEvents.keyDown(Keyboard.KEY_A)) {
					camera.setPosition((float) camera.getPosition().x - speed, (float) camera.getPosition().y);
				} else if (KeyboardEvents.keyDown(Keyboard.KEY_D)) {
					camera.setPosition((float) camera.getPosition().x + speed, (float) camera.getPosition().y);
				}

				if (KeyboardEvents.keyDown(Keyboard.KEY_S)) {
					camera.setPosition((float) camera.getPosition().x, (float) camera.getPosition().y - speed);
				} else if (KeyboardEvents.keyDown(Keyboard.KEY_W)) {
					camera.setPosition((float) camera.getPosition().x, (float) camera.getPosition().y + speed);
				}
			}
			
			batch.begin();
			batch.draw(tex.getTextureID(), (float)player.getCenterPosition().x, (float)player.getCenterPosition().y, (float) window.getDimensions().x,
					(float) window.getDimensions().y, 0.0f, 0.0f, tex.getWidth(), tex.getHeight(), 0.0f, color);

			if (MouseEvents.buttonDown(0)) {
				ParticleEmitter emitter = new ParticleEmitter();
				emitter.init((Texture) ResourceSystem.getResource("textures.particle.spark"), (float) mp.x,
						(float) mp.y, 0.0f, 1.0f);
				emitter.spawnAngle = (float) Math.PI / 128.0f;
				emitter.particlesPerSpawn = 1;
				emitter.framesUntilSpawn = 10;
				emitter.setColor((byte) new Random().nextInt(255), (byte) new Random().nextInt(255),
						(byte) new Random().nextInt(255), (byte) 255);
				emitter.particleHeight = 50;
				emitter.particleWidth = 50;
				particleEmitters.add(emitter);
			}

			for (ParticleEmitter e : particleEmitters) {
				e.render(batch);
				e.update();
			}

			{
				player.setPosition(camera.getPosition().subtract(player.getSize().x / 2.0, player.getSize().y / 2.0));

				e1.render(null);
				e1.update(null);

				player.render(null);
				player.update(null);

				ArrayList<Entity> e = new ArrayList<Entity>();
				e.add(player);
				e.add(e1);
				cManager.collide(e);
			}

			batch.end();
			batch.render(camera);

			Display.update();
		}
	}
}