package com.unemployedgames.wolfcraft.misc;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class  ApiRequests {

    public static class PlayerBan {
        public boolean banned;
        @SerializedName("banDetails")
        public BanDetails banDetails;
    }

    public static class BanDetails {
        public String reason;
        public Date banDate;
    }

    public static PlayerBan getBan(String username) {
        PlayerBan json = null;
        try {
            URL url = new URL(Wolfcraft.DataAPIUrl + "get_player_ban_details/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String res = response.toString();

                json = new Gson().fromJson(res, PlayerBan.class);
            } else {
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static CompletableFuture<PlayerBan> getBanAsync(String username) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerBan json = null;
            try {
                json = getBan(username); // Call the existing synchronous method
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        });
    }
}
