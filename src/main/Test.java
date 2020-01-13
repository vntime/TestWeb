package main;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File file = new File(Test.class.getClassLoader().getResource("mail").getFile());
        System.out.println(file.getName());
        File[] f2s = file.listFiles();
        for (File f : f2s) {
            System.out.println(f.getName());
        }

        System.out.println(new File(Test.class.getClassLoader().getResource("mail").getFile()).getAbsolutePath());
    }
}
