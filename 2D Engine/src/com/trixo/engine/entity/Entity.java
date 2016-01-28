package com.trixo.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.trixo.engine.math.Vector;
import com.trixo.engine.math.Vector4;
import com.trixo.engine.physics.CollisionManager;
import com.trixo.engine.ui.Quad;

@SuppressWarnings("unchecked")
public class Entity {
    private HashMap<String, Object> data = null;

    /** Constructors **/
    public Entity(int entityID, Vector4 aabb) {
        this.data = new HashMap<String, Object>();
        this.data.put("entityID", entityID);
        this.data.put("position", new Vector(aabb.x, aabb.y));
        this.data.put("size", new Vector(aabb.z, aabb.w));
        this.data.put("velocity", new Vector(0, 0));
        this.data.put("quad", new Quad(this.getPosition(), this.getSize()));
        this.data.put("isColliding", false);
    }

    public Entity(int entityID, Vector position, Vector size) {
        this(entityID, new Vector4(position.x, position.y, size.x, size.y));
    }

    /** Entity Functions **/
    public void init(HashMap<String, Object> data) {
        ((Quad) this.data.get("quad")).init(data);
    }

    public void render(HashMap<String, Object> data) {
        ((Quad) this.data.get("quad")).render(data);
    }

    public void update(HashMap<String, Object> data) {
        ((Quad) this.data.get("quad")).update(data);
        
        this.setPosition(this.getPosition().add(this.getVelocity()));
    }

    public void updateQuad() {
        Quad quad = (Quad) this.data.get("quad");

        quad.setPosition(this.getPosition());
        quad.setSize(this.getSize());
        quad.setColour(this.getColour());
        quad.setTexture(this.getTexture());
    }
    
    public void move(Vector direction, double step) {
        this.setPosition(this.getPosition().add(direction.x * step, direction.y * step));
    }
    
    public boolean isColliding() {
        return (boolean) this.data.get("isColliding");
    }
    
    public boolean isTouching(Entity entity) {
        double MIN_DISTANCEX = (entity.getSize().x / 2.0) + (this.getSize().x / 2.0);
        double MIN_DISTANCEY = (entity.getSize().y / 2.0) + (this.getSize().y / 2.0);
        
        Vector distVec = entity.getPosition().subtract(this.getPosition());

        double xDepth = MIN_DISTANCEX - Math.abs(distVec.x);
        double yDepth = MIN_DISTANCEY - Math.abs(distVec.y);

        if (xDepth > 0 && yDepth > 0) {
            return true;
        }
        
        return false;
    }

    /** Getters, Setters **/
    /* Getters */
    public Vector getPosition() {
        return (Vector) this.data.get("position");
    }

    public Vector getCenterPosition() {
        return this.getPosition().add(this.getSize().x / 2.0, this.getSize().y / 2.0);
    }

    public Vector getSize() {
        return (Vector) this.data.get("size");
    }
    
    public Vector getVelocity() {
        return (Vector) this.data.get("velocity");
    }

    public Vector4 getAABB() {
        return new Vector4(this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);
    }
    
    public ArrayList<Byte> getColour() {
        return (ArrayList<Byte>) this.data.get("colour");
    }
    
    public String getTexture() {
        return (String) this.data.get("texture");
    }
    
    public CollisionManager getCollisionManager() {
        return (CollisionManager) this.data.get("collisionManager");
    }
    
    public Quad getQuad() {
        return (Quad) this.data.get("quad");
    }

    /* Setters */
    public Entity setColour(byte[] colour) {
        ArrayList<Byte> colourList = new ArrayList<Byte>();
        colourList.add(colour[0]);
        colourList.add(colour[1]);
        colourList.add(colour[2]);
        colourList.add(colour[3]);

        this.data.put("colour", colourList);
        this.updateQuad();

        return this;
    }

    public Entity setColour(ArrayList<Byte> colour) {
        this.data.put("colour", colour);
        this.updateQuad();

        return this;
    }

    public Entity setTexture(String texture) {
        this.data.put("texture", texture);
        this.updateQuad();

        return this;
    }

    public Entity setPosition(Vector position) {
        this.data.put("position", position);
        this.updateQuad();

        return this;
    }

    public Entity setSize(Vector size) {
        this.data.put("size", size);
        this.updateQuad();

        return this;
    }
    
    public Entity setVelocity(Vector velocity) {
        this.data.put("velocity", velocity);
        
        return this;
    }

    public Entity setAABB(Vector4 aabb) {
        this.setPosition(new Vector(aabb.x, aabb.y));
        this.setSize(new Vector(aabb.z, aabb.w));
        
        return this;
    }
    
    public Entity setCollisionManager(CollisionManager collisionManager) {
        this.data.put("collisionManager", collisionManager);
        
        return this;
    }
    
    public Entity setIsColliding(boolean isColliding) {
        this.data.put("isColliding", isColliding);

        return this;
    }
}
