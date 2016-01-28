package com.trixo.engine.systems.content;

import java.util.HashMap;

public class ResourceSystem {
    private static HashMap<String, Resource> cache = new HashMap<String, Resource>();

    public static void loadResource(String resourceID, String path, int type) {
        Resource resource = new Resource(resourceID);
        resource.type = type;
        resource.load(path);

        cache.put(resourceID, resource);
    }

    public static Object getResource(String resourceID) {
        return cache.get(resourceID).getResource();
    }
}
