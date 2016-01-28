package nl.sintlucas.nww2game.world;

import java.util.HashMap;

import com.trixo.engine.entity.Entity;
import com.trixo.engine.math.Vector;
import com.trixo.engine.math.Vector4;

import nl.sintlucas.nww2game.physics.EntityCollisionManager;

public class Collectable extends Entity {
    private HashMap<String, Object> data = null;

    /** Constructor **/
    public Collectable(int entityID, Vector4 aabb) {
        super(entityID, aabb);

        this.data = new HashMap<String, Object>();
        this.data.put("collisionManager", new EntityCollisionManager());
    }

    public Collectable(int entityID, Vector position, Vector size) {
        this(entityID, new Vector4(position.x, position.y, size.x, size.y));

        this.setPosition(position);
        this.setSize(size);
    }

    /** Collectable Functions **/
    public void init(HashMap<String, Object> data) {
        super.init(data);
    }

    public void render(HashMap<String, Object> data) {
        super.render(data);
    }

    public void update(HashMap<String, Object> data) {
        super.update(data);
    }

    /** Getters, Setters **/
    /* Getters */
    public World getWorld() {
        return (World) this.data.get("world");
    }

    public double getSpeed() {
        return (double) this.data.get("speed");
    }

    /* Setters */
    public Collectable setWorld(World world) {
        this.data.put("world", world);

        return this;
    }

    public Collectable setSpeed(double speed) {
        this.data.put("speed", speed);

        return this;
    }
}