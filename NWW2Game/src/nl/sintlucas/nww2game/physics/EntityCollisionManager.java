package nl.sintlucas.nww2game.physics;

import java.util.ArrayList;

import com.trixo.engine.entity.Entity;
import com.trixo.engine.math.Vector;
import com.trixo.engine.physics.CollisionManager;

import nl.sintlucas.nww2game.player.Player;
import nl.sintlucas.nww2game.world.Platform;
import nl.sintlucas.nww2game.world.World;

public class EntityCollisionManager extends CollisionManager {
    private ArrayList<Platform> collisions = new ArrayList<Platform>();
    
    /** PlayerCollisionManager Functions **/
    public void collide(Entity entity, World world) {
        this.collisions.clear();
        
        for (Platform platform : world.getPlatforms()) {
            this.collideWithPlatform(entity, platform);
        }
    }

    private void collideWithPlatform(Entity entity, Platform platform) {
        double MIN_DISTANCEX = (entity.getSize().x / 2.0) + (platform.getSize().x / 2.0);
        double MIN_DISTANCEY = (entity.getSize().y / 2.0) + (platform.getSize().y / 2.0);

        Vector newPosition = new Vector(entity.getPosition().x, entity.getPosition().y);
        Vector distVec = entity.getPosition().subtract(platform.getPosition());

        double xDepth = MIN_DISTANCEX - Math.abs(distVec.x);
        double yDepth = MIN_DISTANCEY - Math.abs(distVec.y);

        if (xDepth > 0 && yDepth > 0) {
            if (Math.max(xDepth, 0.0f) < Math.max(yDepth, 0.0f)) {
                if (distVec.x < 0) {
                    newPosition.x -= xDepth;
                } else {
                    newPosition.x += xDepth;
                }
            } else {
                if (distVec.y < 0) {
                    newPosition.y -= yDepth;
                } else {
                    newPosition.y += yDepth;
                }
            }
            
            this.collisions.add(platform);
        }
        
        if(this.collisions.size() > 0) {
            entity.setIsColliding(true);
        } else {
            entity.setIsColliding(false);
        }

        entity.setPosition(newPosition);

        if(entity instanceof Player) {
            ((Player)entity).getCamera().setPosition(newPosition);
        }
    }
    
    /** Getters **/
    /* Getters */
    public ArrayList<Platform> getCollisionPlatforms() {
        return this.collisions;
    }
}