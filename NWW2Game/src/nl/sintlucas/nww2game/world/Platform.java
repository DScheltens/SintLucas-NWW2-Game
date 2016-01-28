package nl.sintlucas.nww2game.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.trixo.engine.math.Vector;
import com.trixo.engine.ui.Quad;

@SuppressWarnings("unchecked")
public class Platform {
    private HashMap<String, Object> data = null;

    /** Constructor **/
    public Platform(Vector position, Vector size) {
        this.data = new HashMap<String, Object>();
        this.data.put("position", position);
        this.data.put("size", size);
        this.data.put("quad", new Quad(position, size));
    }

    /** Platform Functions **/
    public void init(HashMap<String, Object> data) {
        ((Quad) this.data.get("quad")).init(data);
    }

    public void render(HashMap<String, Object> data) {
        ((Quad) this.data.get("quad")).render(data);
    }

    public void update(HashMap<String, Object> data) {
        ((Quad) this.data.get("quad")).update(data);
    }

    public void updateQuad() {
        Quad quad = (Quad) this.data.get("quad");

        quad.setPosition(this.getPosition());
        quad.setSize(this.getSize());
        quad.setColour(this.getColour());
        quad.setTexture(this.getTexture());
    }

    /** Getters, Setters **/
    /* Getters */
    public Vector getPosition() {
        return (Vector) this.data.get("position");
    }

    public Vector getSize() {
        return (Vector) this.data.get("size");
    }

    public ArrayList<Byte> getColour() {
        return (ArrayList<Byte>) this.data.get("colour");
    }

    public String getTexture() {
        return (String) this.data.get("texture");
    }
    
    public Quad getQuad() {
        return (Quad) this.data.get("quad");
    }

    /* Setters */
    public Platform setColour(byte[] colour) {
        ArrayList<Byte> colourList = new ArrayList<Byte>();
        colourList.add(colour[0]);
        colourList.add(colour[1]);
        colourList.add(colour[2]);
        colourList.add(colour[3]);

        this.data.put("colour", colourList);
        this.updateQuad();

        return this;
    }

    public Platform setColour(ArrayList<Byte> colour) {
        this.data.put("colour", colour);
        this.updateQuad();

        return this;
    }

    public Platform setTexture(String texture) {
        this.data.put("texture", texture);
        this.updateQuad();

        return this;
    }

    public Platform setPosition(Vector position) {
        this.data.put("position", position);
        this.updateQuad();

        return this;
    }

    public Platform setSize(Vector size) {
        this.data.put("size", size);
        this.updateQuad();

        return this;
    }
}
