package com.trixo.engine.ui;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

public class Text {
	private Font font = null;
	private TrueTypeFont ttf = null;
	private int fontSize = 0, fontStyle = 0;
	private String fontName = "Arial";

	/** Text Functions **/
	public void updateFont() {
		this.font = new Font(this.fontName, this.fontStyle, this.fontSize);
		this.ttf = new TrueTypeFont(this.font, true);
	}

	public void drawString(int x, int y, String text, Color color) {
		TextureImpl.bindNone();

		this.ttf.drawString(x, y, text, color);
	}

	/** Getters, Setters **/
	/* Getters */
	public int getFontSize() {
		return this.fontSize;
	}

	public int getFontStyle() {
		return this.fontSize;
	}

	public String getFontName() {
		return this.fontName;
	}

	/* Setters */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
}
