package com.unemployedgames.wolfcraft.modmainmenu;

import com.mojang.blaze3d.platform.GlUtil;
import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class TelemetryDataCollector {
    private String GRAPHICS = "";
    private String OPERATING_SYS = "";
    private String JAVA = "";
    private String CPU = "";
    private String MAX_MEM = "";
    private String DISPLAY = "";
    private boolean COLLECTED_SENS_DATA = false;

    private String MINECRAFT_VERSION = "";
    private String MOD_VERSION = "";
    private String OTHER_INSTALLED_MODS = "";
    private String PLAYERNAME = "";
    private String SESSION_ID = "";
    private boolean IN_WORLD = false;
    private String COORDS = "Not In World";
    private String GAMETIME = "Not In World";

    public void collectStandardData() {
        MINECRAFT_VERSION = Wolfcraft.MINECRAFT_VERSION;

        MOD_VERSION = Wolfcraft.MOD_VERSION;

        for (IModInfo mod : ModList.get().getMods()) {
            OTHER_INSTALLED_MODS = OTHER_INSTALLED_MODS + mod.getDisplayName() + ":" + mod.getModId() + "|" + mod.getVersion().toString() + ",";
        }

        PLAYERNAME = Minecraft.getInstance().getUser().getName();

        SESSION_ID = Minecraft.getInstance().getUser().getSessionId();

        IN_WORLD = Minecraft.getInstance().level != null;

        if (IN_WORLD) {
            COORDS = "X: " + Minecraft.getInstance().player.getBlockX() + " Y: " + Minecraft.getInstance().player.getBlockY() +" Z: " + Minecraft.getInstance().player.getBlockZ();
            GAMETIME = "" + Minecraft.getInstance().level.getGameTime();
        }

        System.out.println(COORDS);
        System.out.println(GAMETIME);

    }

    public void collectSensitiveData() {
        MAX_MEM = String.valueOf(Runtime.getRuntime().maxMemory());
        DISPLAY = "W: " + Minecraft.getInstance().getWindow().getScreenWidth() + " H: " + Minecraft.getInstance().getWindow().getScreenHeight();
        CPU = GlUtil.getCpuInfo();
        GRAPHICS = GlUtil.getRenderer();
        JAVA = Runtime.version().toString();
        OPERATING_SYS = System.getProperty("os.name") + " " + System.getProperty("os.arch");
        COLLECTED_SENS_DATA = true;
    }

    public String getListInfo() {
        String tempData = "";

        HashMap<String, String> standard = getStandardAttributes();

        for (Map.Entry<String, String> key : standard.entrySet()) {
            tempData = tempData + key + "\n";
        }

        if(COLLECTED_SENS_DATA) {
            HashMap<String, String> sens = getSensitiveAttributes();

            for (Map.Entry<String, String> key : sens.entrySet()) {
                tempData = tempData + key + "\n";
            }
        }

        return tempData;
    }

    public HashMap<String, String> getStandardAttributes() {
        HashMap<String, String> temp = new HashMap<String, String>();

        temp.put("minecraft_version", MINECRAFT_VERSION);
        temp.put("mod_version", MOD_VERSION);
        temp.put("other_mods", OTHER_INSTALLED_MODS);
        temp.put("plrname", PLAYERNAME);
        temp.put("ss_id", SESSION_ID);
        temp.put("coords", COORDS);
        temp.put("gametime", GAMETIME);

        return temp;
    }

    public HashMap<String, String> getSensitiveAttributes() {
        HashMap<String, String> temp = new HashMap<String, String>();

        temp.put("graphics", GRAPHICS);
        temp.put("os", OPERATING_SYS);
        temp.put("java", JAVA);
        temp.put("cpu", CPU);
        temp.put("max_mem", MAX_MEM);
        temp.put("display", DISPLAY);

        return temp;
    }
}
