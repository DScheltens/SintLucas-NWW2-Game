package com.trixo.engine.systems.particle;

public class Particle2D {
    public double x, y;
    protected double vx, vy;
    protected double sx, sy;
    public int maxLife, currentLife;

    public void init(double x, double y, double vx, double vy, double sx, double sy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.sx = sx;
        this.sy = sy;
    }

    public void update() {
        x += vx;
        y += vy;
    }
}