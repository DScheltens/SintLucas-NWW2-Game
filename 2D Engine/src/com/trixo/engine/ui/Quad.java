package com.trixo.engine.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import com.trixo.engine.graphics.SpriteBatch;
import com.trixo.engine.math.Vector;
import com.trixo.engine.systems.content.ResourceSystem;

@SuppressWarnings("unchecked")
public class Quad extends UIObject {
	/** Constructor **/
	public Quad(Vector position, Vector size) {
		super(position, size);
	}

	/** UIObject Functions **/
	@Override
	public void init(HashMap<String, Object> data) {
		if (data != null) {
			if (data.containsKey("texture")) {
				this.setTexture((String) data.get("texture"));
			}

			if (data.containsKey("colour")) {
				this.setColour((ArrayList<Byte>) data.get("colour"));
			}
		}
	}

	@Override
	public void render(HashMap<String, Object> data) {
		if (this.getTexture() == null) {
			((SpriteBatch) data.get("spriteBatch")).draw(0, (float) this.getPosition().x,
					(float) this.getPosition().y, (float) this.getSize().x, (float) this.getSize().y, 0.0f, 0.0f,
					0, 0, (float) (this.getAngle() / 360.f), this.getColour().toArray());
		} else {
			Texture tex = (Texture) ResourceSystem.getResource(this.getTexture());

			((SpriteBatch) data.get("spriteBatch")).draw(tex.getTextureID(), (float) this.getPosition().x,
					(float) this.getPosition().y, (float) this.getSize().x, (float) this.getSize().y, 0.0f, 0.0f,
					tex.getWidth(), tex.getHeight(), (float) (this.getAngle() / 360.f), this.getColour().toArray());
		}
	}

	@Override
	public void update(HashMap<String, Object> data) {
	}
}
