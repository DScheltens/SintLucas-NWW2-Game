package com.trixo.engine.systems.particle;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import com.trixo.engine.graphics.SpriteBatch;
import com.trixo.engine.math.Vector;

public class ParticleEmitter {
    public double x, y;
    public int particlesPerSpawn = 1;
    public int framesUntilSpawn = 1;
    public int maxParticleLife = 500;
    public int minParticleLife = 250;
    public double particleWidth = 5.0f;
    public double particleHeight = 5.0f;
    public Vector direction = new Vector();
    public double spawnAngle = (double) Math.PI;
    public byte color[] = new byte[4];
    private int frame = 0;
    private static Random random = new Random();
    private ArrayList<Particle2D> particles = new ArrayList<Particle2D>();
    private Texture particleTexture = null;

    public void init(Texture texture, double x, double y, double dirX, double dirY) {
        particleTexture = texture;
        color[0] = (byte) 255;
        color[1] = (byte) 255;
        color[2] = (byte) 255;
        color[3] = (byte) 255;
        this.x = x;
        this.y = y;
        direction.x = dirX;
        direction.y = dirY;
        direction.normalizeLocal();
    }

    public void setColor(byte r, byte g, byte b, byte a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    public void update() {
        frame++;
        if (frame >= framesUntilSpawn) {
            for (int i = 0; i < particlesPerSpawn; i++) {
                Particle2D p = new Particle2D();

                double angle = ((random.nextInt(Integer.MAX_VALUE)) / (double) (Integer.MAX_VALUE) * 2.0f - 1.0f) * spawnAngle;
                double dx = (double) direction.x * (double) Math.cos(angle) - (double) direction.y * (double) Math.sin(angle);
                double dy = (double) direction.x * (double) Math.sin(angle) + (double) direction.y * (double) Math.cos(angle);

                p.init((double) x, (double) y, dx, dy, particleWidth, particleHeight);
                p.maxLife = random.nextInt((maxParticleLife - minParticleLife) + 1) + minParticleLife;

                particles.add(p);
            }
            frame = 0;
        }

        if (particles.size() > 0) {
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle2D tmp = particles.get(i);

                if (tmp != null) {
                    if (tmp.currentLife > tmp.maxLife) {

                        particles.set(i, particles.get(particles.size() - 1));
                        particles.remove(particles.size() - 1);
                    } else {
                        tmp.update();
                        tmp.currentLife++;
                    }
                }
            }
        } else {
            particles.trimToSize();
        }
    }

    public void render(SpriteBatch batch) {
        for (Particle2D p : particles) {
            batch.draw(particleTexture.getTextureID(), (float)(p.x + p.sx * 0.5f), (float)(p.y + p.sy * 0.5f), (float)p.sx, (float)p.sy, 0.0f, 0.0f,
                    particleTexture.getWidth(), particleTexture.getHeight(), 0.0f, color);
        }
    }
}