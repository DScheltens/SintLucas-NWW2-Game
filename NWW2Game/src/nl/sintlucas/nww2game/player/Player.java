package nl.sintlucas.nww2game.player;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.trixo.engine.entity.Entity;
import com.trixo.engine.graphics.Camera2D;
import com.trixo.engine.math.Vector;
import com.trixo.engine.math.Vector4;
import com.trixo.engine.ui.UIObject;
import com.trixo.engine.utils.EventUtils.KeyboardEvents;

import nl.sintlucas.nww2game.physics.EntityCollisionManager;
import nl.sintlucas.nww2game.world.World;

public class Player extends Entity {
	private HashMap<String, Object> data = null;	
	private boolean jumped = false;
	
	private int timer = 200, lastKeyTime = 200;	
	private int animCounter = 0;
	private int spriteCounter = 0;
	private int maxSprites = 7;
	private int lastDirection = 0, direction = 0;

	/** Constructor **/
	public Player(int entityID, Vector4 aabb) {
		super(entityID, aabb);

		this.data = new HashMap<String, Object>();
		this.setCollisionManager(new EntityCollisionManager());
		this.data.put("camera", new Camera2D());
		this.data.put("speed", 1.0);
		this.data.put("isColliding", false);
	}

	public Player(int entityID, Vector position, Vector size) {
		this(entityID, new Vector4(position.x, position.y, size.x, size.y));
	}

	/** Entity Functions **/
	@Override
	public void init(HashMap<String, Object> data) {
		this.getCamera().init(Display.getWidth(), Display.getHeight());
	}

	@Override
	public void update(HashMap<String, Object> data) {
		this.getCamera().update();

		int delta = (int) data.get("deltatime");

		{
			if (KeyboardEvents.keyDown(Keyboard.KEY_A)) {
				this.getVelocity().x = -this.getSpeed() * delta;

				this.direction = -1;
				this.onMove(-1);
				this.lastDirection = direction;
			} else if (KeyboardEvents.keyDown(Keyboard.KEY_D)) {
				this.getVelocity().x = this.getSpeed() * delta;

				this.direction = 1;
				this.onMove(1);
				this.lastDirection = direction;
			} else {
				this.onMove(0);
			}

			if (this.isColliding()) {
				if (this.jumped) {
					this.onLand();
					this.jumped = false;
				}

				if (KeyboardEvents.keyDown(Keyboard.KEY_SPACE)) {
					if (this.timer == 50 && this.lastKeyTime >= 100) {
						this.getVelocity().y = 1.5;
						this.timer = 0;
						this.lastKeyTime = 0;
						this.jumped = true;

						this.onJump();
					}

					this.timer++;
				} else {
					this.timer = 50;
				}
			}

			this.lastKeyTime++;

			if (this.getVelocity().y > -1.0) {
				this.getVelocity().y -= 0.00725;
			} else {
				this.getVelocity().y = -1.0;
			}

			this.setPosition(this.getPosition().add(this.getVelocity()));
			this.setVelocity(new Vector(0, this.getVelocity().y));
		}

		((EntityCollisionManager) this.getCollisionManager()).collide(this, this.getWorld());
	}

	@Override
	public void render(HashMap<String, Object> data) {
		super.render(data);
	}

	/** Events **/
	/* Movement Events */
	public void onJump() {

	}

	public void onLand() {

	}

	public void onMove(int direction) {
		if (direction == 0) {
			this.setTexture("textures.misc.empty");			
			this.setColour(new byte[] {(byte) 0, (byte) 0, (byte) 255, (byte) 255});
		} else if (direction == 1) {
			this.animCounter++;

			if (this.animCounter % 10 == 0) {
				this.spriteCounter++;

				if (this.spriteCounter > this.maxSprites) {
					this.spriteCounter = 0;
				}

				this.setTexture("textures.misc.empty");
				this.setColour(new byte[] {(byte) 0, (byte) 255, (byte) 0, (byte) 255});
			}
		} else if (direction == -1) {
			this.animCounter++;

			if (this.animCounter % 10 == 0) {
				this.spriteCounter++;

				if (this.spriteCounter > this.maxSprites) {
					this.spriteCounter = 0;
				}

				this.setTexture("textures.misc.empty");
				this.setColour(new byte[] {(byte) 255, (byte) 0, (byte) 0, (byte) 255});
			}
		}
	}

	public boolean isInFrustum(UIObject object) {
		double cornerMinX = this.getCamera().getPosition().x - (Display.getWidth() / 2.0);
		double cornerMinY = this.getCamera().getPosition().y - (Display.getHeight() / 2.0);
		double cornerMaxX = this.getCamera().getPosition().x + (Display.getWidth() / 2.0);
		double cornerMaxY = this.getCamera().getPosition().y + (Display.getHeight() / 2.0);

		double spriteCornerMinX = object.getPosition().x + (object.getSize().x / 2.0);
		double spriteCornerMinY = object.getPosition().y + (object.getSize().y / 2.0);
		double spriteCornerMaxX = object.getPosition().x - (object.getSize().x / 2.0);
		double spriteCornerMaxY = object.getPosition().y - (object.getSize().y / 2.0);

		return (spriteCornerMinX >= cornerMinX && spriteCornerMinY >= cornerMinY)
				&& (spriteCornerMaxX <= cornerMaxX && spriteCornerMaxY <= cornerMaxY);
	}

	/** Getters, Setters, Adders, Removers **/
	/* Getters */
	public World getWorld() {
		return (World) this.data.get("world");
	}

	public Camera2D getCamera() {
		return (Camera2D) this.data.get("camera");
	}

	public double getSpeed() {
		return (double) this.data.get("speed");
	}

	/* Setters */
	public Player setWorld(World world) {
		this.data.put("world", world);

		return this;
	}

	public Player setCamera(Camera2D camera) {
		this.data.put("camera", camera);

		return this;
	}

	public Player setSpeed(double speed) {
		this.data.put("speed", speed);

		return this;
	}
}
