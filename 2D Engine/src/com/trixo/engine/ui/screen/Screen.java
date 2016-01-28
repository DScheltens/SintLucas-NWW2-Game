package com.trixo.engine.ui.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.trixo.engine.ui.UIObject;

@SuppressWarnings("unchecked")
public abstract class Screen {
    private HashMap<String, Object> data = null;

    /** Constructors **/
    public Screen(int screenID) {
        this.data = new HashMap<String, Object>();
        this.data.put("screenID", screenID);
    }

    /** Screen Functions **/
    public abstract void init(HashMap<String, Object> data);
    public abstract void render(HashMap<String, Object> data);
    public abstract void update(HashMap<String, Object> data);

    /** Getters, Setters **/
    /* Getters */
    public Object getData(String key) {
        return this.data.get(key);
    }

    public int getZIndex() {
        return (int) this.data.get("zIndex");
    }
    
    public ArrayList<UIObject> getSortedUIObjects() {
        return (ArrayList<UIObject>) this.getData("sortedUIObjects");
    }

    public LinkedHashMap<String, UIObject> getUIObjects() {
        return (LinkedHashMap<String, UIObject>) this.getData("uiObjects");
    }

    public UIObject getUIObject(String name) {
        return this.getUIObjects().get(name);
    }

    /* Setters */
    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    public void setZIndex(int zIndex) {
        this.data.put("zIndex", zIndex);
    }

    /* Adders */
    public void addUIObject(String name, UIObject object) {
        this.getUIObjects().put(name, object);
    }
}
