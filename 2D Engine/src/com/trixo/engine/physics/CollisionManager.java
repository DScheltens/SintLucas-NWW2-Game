package com.trixo.engine.physics;

import java.util.ArrayList;

import com.trixo.engine.entity.Entity;
import com.trixo.engine.math.Vector;
import com.trixo.engine.utils.math.VectorHelper;

public class CollisionManager {
	private double TILE_WIDTH = 1.0;

	public void collide(ArrayList<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity tmp = entities.get(i);

			// Collide with entities
			for (int j = i + 1; j < entities.size(); j++) {
				this.collideWithEntity(entities.get(i), entities.get(j));
			}

			// Collide with world
			{
				ArrayList<Vector> collideTilePositions = new ArrayList<Vector>();

				this.checkTilePosition(collideTilePositions, tmp.getPosition().x, tmp.getPosition().y);
				this.checkTilePosition(collideTilePositions, tmp.getPosition().x + tmp.getSize().x,
						tmp.getPosition().y);
				this.checkTilePosition(collideTilePositions, tmp.getPosition().x,
						tmp.getPosition().y + tmp.getSize().y);
				this.checkTilePosition(collideTilePositions, tmp.getPosition().x + tmp.getSize().x,
						tmp.getPosition().y + tmp.getSize().y);

				for (int k = 0; k < collideTilePositions.size(); k++) {
					this.collideWithTile(tmp, collideTilePositions.get(k));
				}
			}
		}
	}

	private void checkTilePosition(ArrayList<Vector> collideTilePositions, double x, double y) {
		// Vector gridPos = new Vector(Math.floor(x / TILE_WIDTH), Math.floor(y
		// / TILE_WIDTH));

		// If we are outside the world, just return
		// if (gridPos.x < 0 || gridPos.x >= levelData[0].size() || gridPos.y <
		// 0 || gridPos.y >= levelData.size()) { return; }

		// If this is not an air tile, we should collide with it if
		/*
		 * if (levelData[gridPos.y][gridPos.x] != '.') { double newX =
		 * (gridPos.x * TILE_WIDTH) + (TILE_WIDTH / 2.0f); double newY =
		 * (gridPos.x * TILE_WIDTH) + (TILE_WIDTH / 2.0f);
		 * 
		 * collideTilePositions.add(new Vector(newX, newY)); }
		 */
	}

	private void collideWithTile(Entity entity, Vector tilePos) {
		double TILE_RADIUS = TILE_WIDTH / 2.0f;
		double MIN_DISTANCEX = (entity.getSize().x / 2.0) + TILE_RADIUS;
		double MIN_DISTANCEY = (entity.getSize().y / 2.0) + TILE_RADIUS;

		Vector newPosition = new Vector(entity.getPosition().x, entity.getPosition().y);
		Vector centerAgentPos = newPosition.add(new Vector((entity.getSize().x / 2.0), (entity.getSize().y / 2.0)));
		Vector distVec = VectorHelper.sub(centerAgentPos, tilePos);

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
		}

		entity.getPosition().x = newPosition.x;
		entity.getPosition().y = newPosition.y;
	}

	private void collideWithEntity(Entity e1, Entity e2) {
		double MIN_DISTANCEX = (e1.getSize().x / 2.0) + (e2.getSize().x / 2.0);
		double MIN_DISTANCEY = (e1.getSize().y / 2.0) + (e2.getSize().y / 2.0);

		{
			Vector newPosition = new Vector(e1.getPosition().x, e1.getPosition().y);
			Vector centerAgentPos = newPosition.add(new Vector((e1.getSize().x / 2.0), (e1.getSize().y / 2.0)));
			Vector distVec = VectorHelper.sub(centerAgentPos, new Vector(e2.getPosition().x + (e2.getSize().x * 0.5f),
					e2.getPosition().y + (e2.getSize().y * 0.5f)));

			double xDepth = (MIN_DISTANCEX - Math.abs(distVec.x)) / 2.0;
			double yDepth = (MIN_DISTANCEY - Math.abs(distVec.y)) / 2.0;

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
			}

			e1.getPosition().x = newPosition.x;
			e1.getPosition().y = newPosition.y;
		}

		{
			Vector newPosition = new Vector(e2.getPosition().x, e2.getPosition().y);
			Vector centerAgentPos = newPosition.add(new Vector((e2.getSize().x / 2.0), (e2.getSize().y / 2.0)));
			Vector distVec = VectorHelper.sub(centerAgentPos, new Vector(e1.getPosition().x + (e1.getSize().x * 0.5f),
					e1.getPosition().y + (e1.getSize().y * 0.5f)));

			double xDepth = (MIN_DISTANCEX - Math.abs(distVec.x)) / 2.0;
			double yDepth = (MIN_DISTANCEY - Math.abs(distVec.y)) / 2.0;

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
			}

			e2.getPosition().x = newPosition.x;
			e2.getPosition().y = newPosition.y;
		}
	}
}