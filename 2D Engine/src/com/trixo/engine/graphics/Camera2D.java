package com.trixo.engine.graphics;

import com.trixo.engine.math.Matrix4;
import com.trixo.engine.math.Vector;

public class Camera2D implements Camera {
    private double x = 0.0f, y = 0.0f;
    private double scaleX = 1.0f, scaleY = 1.0f;
    private Matrix4 matrix;
    private Matrix4 orthoMatrix;
    private int screenWidth;
    private int screenHeight;
    private boolean needsMatrixUpdate = true;

    public void init(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        needsMatrixUpdate = true;

        orthoMatrix = new Matrix4();
        matrix = new Matrix4();

        orthoMatrix.setToOrtho(0.0, screenWidth, 0.0, screenHeight, 0.0, 1000.0);
    }

    public void update() {
        if (needsMatrixUpdate) {
            Matrix4 tmp = new Matrix4();
            tmp.setToTranslation(-x + screenWidth / 2.0, -y + screenHeight / 2.0, 0.0);

            orthoMatrix.mult(tmp, matrix);
            needsMatrixUpdate = false;
        }
    }

    public Vector convertScreenToWorld(double x, double y) {
        x -= screenWidth / 2.0f;
        y -= screenHeight / 2.0f;
        x /= scaleX;
        y /= scaleY;
        x += this.x;
        y += this.y;
        
        return new Vector(x, y);
    }

    @Override
    public Matrix4 getCameraMatrix() {
        return matrix;
    }

    public void setPosition(Vector position) {
        this.x = position.x;
        this.y = position.y;
        needsMatrixUpdate = true;
    }
    
    public void setPosition(double d, double y2) {
        this.x = d;
        this.y = y2;
        needsMatrixUpdate = true;
    }

    public void setScale(double sx, double sy) {
        this.scaleX = x;
        this.scaleY = y;
        needsMatrixUpdate = true;
    }

    public Vector getPosition() {
        return new Vector(this.x, this.y);
    }

    public Vector getScale() {
        return new Vector(this.scaleX, this.scaleY);
    }
}