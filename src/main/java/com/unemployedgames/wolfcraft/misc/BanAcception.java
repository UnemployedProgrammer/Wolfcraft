package com.unemployedgames.wolfcraft.misc;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BanAcception {
    public static void acceptBan() {
        // Get the Minecraft directory
        File minecraftDir = FMLPaths.GAMEDIR.get().toFile();

        // Create the file object
        File acceptedBanFile = new File(minecraftDir, "acceptedban.txt");

        try {
            // Create the file if it doesn't exist
            if (acceptedBanFile.createNewFile()) {
                System.out.println("File created: " + acceptedBanFile.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }

            // Write content into the file
            FileWriter writer = new FileWriter(acceptedBanFile);
            writer.write("true");
            writer.close();
            System.out.println("Content 'true' written to file.");
        } catch (IOException e) {
            System.err.println("Failed to create or write to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean accepedBan() {
        return new File(FMLPaths.GAMEDIR.get().toFile(), "acceptedban.txt").exists();
    }

    public static boolean deleteAcceptBan() {
        return new File(FMLPaths.GAMEDIR.get().toFile(), "acceptedban.txt").delete();
    }
}
