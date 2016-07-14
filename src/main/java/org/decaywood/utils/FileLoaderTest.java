package org.decaywood.utils;

/**
 * Created by seven on 14/07/16.
 */
public class FileLoaderTest {
    public static void main(String[] args) {
        final String COOKIE_FLUSH_PATH = "cookie/index.txt";
        final String ROOT_PATH = FileLoaderTest.class.getClassLoader().getResource("").getPath();
        System.out.println(ROOT_PATH);
    }
}
