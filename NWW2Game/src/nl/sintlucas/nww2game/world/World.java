package nl.sintlucas.nww2game.world;

import java.awt.Font;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import com.trixo.engine.entity.Entity;
import com.trixo.engine.math.Vector4;
import com.trixo.engine.ui.Text;

import nl.sintlucas.nww2game.player.Player;

@SuppressWarnings("unchecked")
public class World {
	private HashMap<String, Object> data = null;

	private Text text = new Text();

	/** Constructor **/
	public World() {
		this.data = new HashMap<String, Object>();
		this.data.put("player",
				new Player(0, new Vector4(0, 0, 64, 128)).setWorld(this)
						.setColour(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 })
						.setTexture("textures.misc.empty"));

		this.data.put("platforms", new ArrayList<Platform>());
		this.data.put("entities", new ArrayList<Entity>());
	}

	/** World Functions **/
	public void init(HashMap<String, Object> data) {
		this.getPlayer().init(data);

		for (Platform platform : this.getPlatforms()) {
			platform.init(data);
		}

		for (Entity entity : this.getEntities()) {
			entity.init(data);
		}

		text.setFontName("Arial");
		text.setFontSize(24);
		text.setFontStyle(Font.BOLD);
		text.updateFont();
	}

	public void render(HashMap<String, Object> data) {
		this.getPlayer().render(data);

		for (Platform platform : this.getPlatforms()) {
			if (this.getPlayer().isInFrustum(platform.getQuad())) {
				platform.render(data);
			}
		}

		for (Entity entity : this.getEntities()) {
			if (this.getPlayer().isInFrustum(entity.getQuad())) {
				entity.render(data);
			}
		}

		{
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			text.drawString(0, 0, "Haii",
					Color.white);
		}
	}

	public void update(HashMap<String, Object> data) {
		this.getPlayer().update(data);

		for (Platform platform : this.getPlatforms()) {
			platform.update(data);
		}

		try {
			for (Entity entity : this.getEntities()) {
				entity.update(data);

				if (entity instanceof Collectable) {
					if (entity.isTouching(this.getPlayer())) {
						this.removeEntity(this.getIDFromEntity(entity));

						break;
					}
				}
			}
		} catch (ConcurrentModificationException e) {
		}
	}

	public void spawnEntity(Entity entity) {
		this.addEntity(this.getEntities().size(), entity);
	}

	public void killEntity(Entity entity) {
		this.removeEntity(this.getIDFromEntity(entity));
	}

	/** Getters, Adders, Removers **/
	/* Getters */
	public ArrayList<Platform> getPlatforms() {
		return (ArrayList<Platform>) data.get("platforms");
	}

	public Platform getPlatform(int ID) {
		return this.getPlatforms().get(ID);
	}

	public ArrayList<Entity> getEntities() {
		return (ArrayList<Entity>) data.get("entities");
	}

	public Entity getEntity(int entityID) {
		return this.getEntities().get(entityID);
	}

	public Player getPlayer() {
		return (Player) this.data.get("player");
	}

	public int getIDFromEntity(Entity entity) {
		for (int i = 0; i < this.getEntities().size(); i++) {
			Entity tmpEntity = this.getEntities().get(i);

			if (tmpEntity == entity) {
				return i;
			}
		}

		return -1;
	}

	/* Adders */
	public World addPlatform(int ID, Platform platform) {
		this.getPlatforms().add(ID, platform);

		return this;
	}

	private World addEntity(int ID, Entity entity) {
		this.getEntities().add(ID, entity);

		return this;
	}

	/* Removers */
	public World removePlatform(UUID uuid) {
		this.getPlatforms().remove(uuid);

		return this;
	}

	private World removeEntity(int ID) {
		this.getEntities().remove(ID);

		return this;
	}
}
