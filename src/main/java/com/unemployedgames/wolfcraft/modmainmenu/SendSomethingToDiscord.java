package com.unemployedgames.wolfcraft.modmainmenu;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;

public class SendSomethingToDiscord {
    public static final String ISSUE_API_CODE = "https://discord.com/api/webhooks/1209943902501609482/jVXFwAJirn4jJbpPabInYStWA8ZWwU1DbDXplgMrAMohKjvbTj_6ZOhac-GaMexY7pqR";
    public static final String SUGGESTION_API_CODE = "https://discord.com/api/webhooks/1209500195222454344/sqwoStbZm5oMfv67aH4twNkLKdE7eHVVLfc8_IgUEJPWzVimtwomkM39JnoVemeaYL1m";

    public static String concatFinalString(String title, String reproduce, String teledata) {
        return title + "\n" + reproduce + "\n\n" + teledata;
    }

    public static boolean sendToDiscordAsIssueAndCheckIfWentGood(String message) {
        boolean went = false;
        String webhookUrl = ISSUE_API_CODE; // Replace with your webhook URL
        String kpt_msg = message.replace("\n", "\\n");

        String messageJson = "{\"content\": \"" + kpt_msg + "\"}"; // Build JSON string manually
        System.out.println(message);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(webhookUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(messageJson))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.send.success"), (Component)null);
                went = true;
            } else {
                System.out.println("Error sending message: " + response.statusCode());
                ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                if(message.length() >= 2000) {
                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.send.long"), (Component)null);
                } else {
                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.send.fail"), (Component)null);
                }
            }
        } catch (Exception e) {
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.send.fail"), (Component)null);
            e.printStackTrace();
        }
        return went;
    }

    public static boolean sendToDiscordAsSuggestionAndCheckIfWentGood(String message) {
        boolean went = false;
        String webhookUrl = ISSUE_API_CODE; // Replace with your webhook URL
        String kpt_msg = message.replace("\n", "\\n");

        String messageJson = "{\"content\": \"" + kpt_msg + "\"}"; // Build JSON string manually
        System.out.println(message);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(webhookUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(messageJson))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.suggestion.send.success"), (Component)null);
                went = true;
            } else {
                System.out.println("Error sending message: " + response.statusCode());
                ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
                if(message.length() >= 2000) {
                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.suggestion.send.long"), (Component)null);
                } else {
                    SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.suggestion.send.fail"), (Component)null);
                }
            }
        } catch (Exception e) {
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("gui.wolfcraft.issues_and_suggestions.suggestion.send.fail"), (Component)null);
            e.printStackTrace();
        }
        return went;
    }
}
