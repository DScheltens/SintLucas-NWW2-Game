package com.trixo.engine.graphics;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.trixo.engine.math.Vector;

public class Window {
    private String windowTitle = "JVox Application";
    private Vector windowDims = new Vector(800, 600);

    public void init(String title, Vector dims) {
        this.windowTitle = title;
        this.windowDims = dims;
    }

    public void create() {
        {
            String OS = System.getProperty("os.name");

            if (OS.toLowerCase().contains("windows")) {
                OS = "windows";
            } else if (OS.toLowerCase().contains("mac")) {
                OS = "macosx";
            } else if (OS.toLowerCase().contains("linux")) {
                OS = "linux";
            } else {
                OS = "Error";
            }

            if (OS == "Error") {
                System.exit(1);
            }

            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + File.separator + "libs"
                    + File.separator + "lwjgl" + File.separator + "native" + File.separator + OS);
        }

        try {
            Display.setDisplayMode(new DisplayMode((int)this.windowDims.x, (int)this.windowDims.y));
            Display.setTitle(this.windowTitle);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
    }
    
    public Vector getDimensions()
    {
        return this.windowDims;
    }
}
