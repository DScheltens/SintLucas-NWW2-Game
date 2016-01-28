package com.trixo.engine.utils;

public class SoundUtils {
    public static class SoundFlags {
        public static int NONE = 0x00;
        public static int LOOP = 0x01;
        public static int PRIORITY = 0x02;

        public static boolean checkFor(int flags, int flag) {
            return (flags & flag) == flag;
        }
    }
}
