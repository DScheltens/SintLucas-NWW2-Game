package com.trixo.engine.systems.content;

import java.io.IOException;

import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.trixo.engine.graphics.Shader;

public class Resource {
    public String resourceID = "";
    public int type = 0;
    private Object resource = null;

    public Resource(String resourceID) {
        this.resourceID = resourceID;
    }

    public void load(String path) {
        switch (this.type) {
        case 0:
            try {
                this.resource = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            break;
        case 1:
            Shader s = new Shader();
            s.load(path.split(",")[0], path.split(",")[1]);

            if (!s.valid) {
                try {
                    throw (new Exception("Failed to load the shader program!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            this.resource = s;
            
            break;
        }
    }

    public Object getResource() {
        return this.resource;
    }
}
