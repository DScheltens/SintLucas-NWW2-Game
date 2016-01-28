package com.trixo.engine.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.trixo.engine.ui.UIObject;
import com.trixo.engine.ui.screen.Screen;

public class Sorter {
    /** Sorter Functions **/
    public static ArrayList<Screen> sortScreens(ArrayList<Screen> input) {
        ArrayList<Screen> toSort = input;
        Collections.sort(toSort, new ScreenComparator());

        return toSort;
    }
    
    public static ArrayList<UIObject> sortUIObjects(ArrayList<UIObject> input) {
        ArrayList<UIObject> toSort = input;
        Collections.sort(toSort, new UIObjectComparator());

        return toSort;
    }

    /** Comparators **/
    static class ScreenComparator implements Comparator<Screen> {
        @Override
        public int compare(Screen a, Screen b) {
            return a.getZIndex() < b.getZIndex() ? -1 : a.getZIndex() == b.getZIndex() ? 0 : 1;
        }
    }
    
    static class UIObjectComparator implements Comparator<UIObject> {
        @Override
        public int compare(UIObject a, UIObject b) {
            return a.getZIndex() < b.getZIndex() ? -1 : a.getZIndex() == b.getZIndex() ? 0 : 1;
        }
    }
}
