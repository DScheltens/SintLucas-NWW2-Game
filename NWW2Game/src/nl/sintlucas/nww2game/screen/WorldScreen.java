package nl.sintlucas.nww2game.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.trixo.engine.ui.UIObject;
import com.trixo.engine.ui.screen.Screen;
import com.trixo.engine.utils.Sorter;

import nl.sintlucas.nww2game.world.World;

public class WorldScreen extends Screen {
    /** Constructor **/
    public WorldScreen(int screenID) {
        super(screenID);

        this.setData("uiObjects", new LinkedHashMap<String, UIObject>());
        this.setData("world", new World());
    }

    /** Screen Functions **/
    @Override
    public void init(HashMap<String, Object> data) {
        this.setData("sortedUIObjects", Sorter.sortUIObjects(new ArrayList<UIObject>(this.getUIObjects().values())));

        for (UIObject obj : this.getSortedUIObjects()) {
            obj.init(data);
        }

        ((World) this.getData("world")).init(data);
    }

    @Override
    public void render(HashMap<String, Object> data) {
        for (UIObject obj : this.getSortedUIObjects()) {
            obj.render(data);
        }

        ((World) this.getData("world")).render(data);
    }

    @Override
    public void update(HashMap<String, Object> data) {
        for (UIObject obj : this.getSortedUIObjects()) {
            obj.update(data);
        }

        ((World) this.getData("world")).update(data);
    }
    
    /** Getters **/
    /* Getters */
    public World getWorld() {
        return (World) this.getData("world");
    }
}