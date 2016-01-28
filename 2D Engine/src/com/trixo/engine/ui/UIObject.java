package com.trixo.engine.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.trixo.engine.math.Vector;
import com.trixo.engine.math.Vector4;

@SuppressWarnings("unchecked")
public abstract class UIObject {
    private HashMap<String, Object> data = null;

    /** Constructor **/
    public UIObject(Vector position, Vector size) {
        this.data = new HashMap<String, Object>();
        this.data.put("startPosition", position);
        this.data.put("position", position);
        this.data.put("size", size);
    }

    /** UIObject Functions **/
    public abstract void init(HashMap<String, Object> data);

    public abstract void render(HashMap<String, Object> data);

    public abstract void update(HashMap<String, Object> data);

    /** Getters, Setters **/
    /* Getters */
    public String getTexture() {
        return (String) this.data.get("texture");
    }

    public ArrayList<Byte> getColour() {
        return (ArrayList<Byte>) this.data.get("colour");
    }

    public Vector getStartPosition() {
        return (Vector) this.data.get("startPosition");
    }
    
    public Vector getPosition() {
        return (Vector) this.data.get("position");
    }

    public Vector getCenterPosition() {
        return this.getPosition().add(this.getSize().x / 2.0, this.getSize().y / 2.0);
    }

    public Vector getSize() {
        return (Vector) this.data.get("size");
    }

    public Vector4 getAABB() {
        return new Vector4(this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
    }

    public int getZIndex() {
        return (int) this.data.get("zIndex");
    }

    public double getAngle() {
        return (this.data.get("angle") == null ? 0.0 : (double) this.data.get("angle"));
    }

    /* Setters */
    public UIObject setTexture(String texture) {
        this.data.put("texture", texture);

        return this;
    }

    public UIObject setColour(byte[] colour) {
        ArrayList<Byte> list = new ArrayList<Byte>();
        list.add(colour[0]);
        list.add(colour[1]);
        list.add(colour[2]);
        list.add(colour[3]);

        return this.setColour(list);
    }

    public UIObject setColour(ArrayList<Byte> colour) {
        this.data.put("colour", colour);

        return this;
    }

    public UIObject setBasePosition(Vector position) {
        this.data.put("startPosition", position);

        return this;
    }
    
    public UIObject setPosition(Vector position) {
        this.data.put("position", position);

        return this;
    }

    public UIObject setSize(Vector size) {
        this.data.put("size", size);

        return this;
    }

    public UIObject setAABB(Vector4 aabb) {
        this.setPosition(new Vector(aabb.x, aabb.y));
        this.setSize(new Vector(aabb.z, aabb.w));

        return this;
    }

    public UIObject setAngle(double angle) {
        this.data.put("angle", angle);

        return this;
    }

    public UIObject setZIndex(int zIndex) {
        this.data.put("zIndex", zIndex);

        return this;
    }
}
