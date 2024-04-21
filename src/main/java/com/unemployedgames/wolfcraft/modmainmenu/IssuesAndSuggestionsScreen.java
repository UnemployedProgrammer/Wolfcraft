package com.unemployedgames.wolfcraft.modmainmenu;

import com.unemployedgames.wolfcraft.misc.ApiRequests;
import com.unemployedgames.wolfcraft.misc.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.event.sound.SoundEvent;

public class IssuesAndSuggestionsScreen extends Screen {

    public static boolean isIssue;
    public static Checkbox issuesSendTeleMax;
    public static EditBox issuesTitleBox;
    public static EditBox issuesReproduceBox;
    public static Button sendButton;

    ///////////////////////////////////

    public static EditBox suggestionBox;
    public static Button sendSuggButton;


    public IssuesAndSuggestionsScreen(boolean isIssue) {
        super(Component.translatable(isIssue ? "gui.wolfcraft.issues_and_suggestions.title.issue" : "gui.wolfcraft.issues_and_suggestions.title.suggestion"));
        this.isIssue = isIssue;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderBackground(pGuiGraphics);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.translatable(isIssue ? "gui.wolfcraft.issues_and_suggestions.title.issue" : "gui.wolfcraft.issues_and_suggestions.title.suggestion"), width / 2, 10, 0xFFFFFF);
        if(isIssue) {
            issuesTitleBox.render(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
            issuesReproduceBox.render(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
            issuesSendTeleMax.render(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
            sendButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.drawString(minecraft.font, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.title_box"), width / 2 - ((width - 30) / 2), height / 2 - 60, 0xFFFFFF);
            pGuiGraphics.drawString(minecraft.font, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.reproduce_box"), width / 2 - ((width - 30) / 2), height / 2 - 20, 0xFFFFFF);
            pGuiGraphics.drawString(minecraft.font, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.telebox_title"), (width / 2 - ((width - 30) / 2)) + 25, height / 2 + 15, 0xFFFFFF);
            pGuiGraphics.drawString(minecraft.font, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.telebox_subtitle"), (width / 2 - ((width - 30) / 2)) + 25, height / 2 + 25, 0xDBDBDB);
            pGuiGraphics.drawString(minecraft.font, Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.teleinfo"), width / 2 - ((width - 30) / 2), height / 2 + 37, 0xDBDBDB);
        } else {
            suggestionBox.render(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
            sendSuggButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.drawString(minecraft.font, Component.translatable("gui.wolfcraft.issues_and_suggestions.suggestion.tit"), width / 2 - ((width - 30) / 2), height / 2 - 60, 0xFFFFFF);
        }
    }

    @Override
    protected void init() {
        if(isIssue) {
            issuesTitleBox = addRenderableWidget(new EditBox(this.minecraft.font, width / 2 - ((width - 30) / 2), height / 2 - 50, width - 30, 20, Component.translatable("tk")));
            issuesTitleBox.setMaxLength(200);

            issuesReproduceBox = addRenderableWidget(new EditBox(this.minecraft.font, width / 2 - ((width - 30) / 2), height / 2 - 10, width - 30, 20, Component.translatable("tk")));
            issuesReproduceBox.setMaxLength(900);

            issuesSendTeleMax = addRenderableWidget(new Checkbox(width / 2 - ((width - 30) / 2), height / 2 + 15, 20, 20 , Component.empty(), true));

            sendButton = addRenderableWidget(Button.builder(Component.translatable("gui.wolfcraft.issues_and_suggestions.issue.send"), (press) -> {sendIssueData();}).bounds(width / 2 - ((width - 30) / 2), height / 2 + 50, width - 45, 20).build());
        } else {
            suggestionBox = addRenderableWidget(new EditBox(this.minecraft.font, width / 2 - ((width - 30) / 2), height / 2 - 50, width - 30, 20, Component.translatable("tk")));
            suggestionBox.setMaxLength(1900);

            sendSuggButton = addRenderableWidget(Button.builder(Component.translatable("gui.wolfcraft.issues_and_suggestions.suggestion.send"), (press) -> {sendSuggestionData();}).bounds(width / 2 - ((width - 30) / 2), height / 2 + 50, width - 45, 20).build());
        }
        playBackgroundMusic();
    }

    private void sendIssueData() {

        ApiRequests.PlayerBan pBan = ApiRequests.getBan(Minecraft.getInstance().getUser().getName());
        if (pBan == null) return;
        if(pBan.banned) {
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("ban.wolfcraft.cannotsend"), (Component)null);
            return;
        }

        TelemetryDataCollector col = new TelemetryDataCollector();
        col.collectStandardData();
        if(IssuesAndSuggestionsScreen.this.issuesSendTeleMax.selected()) {
            col.collectSensitiveData();
        }
        boolean good = SendSomethingToDiscord.sendToDiscordAsIssueAndCheckIfWentGood(SendSomethingToDiscord.concatFinalString(IssuesAndSuggestionsScreen.this.issuesTitleBox.getValue(), IssuesAndSuggestionsScreen.this.issuesReproduceBox.getValue(), col.getListInfo()));

        if(good) {
            this.minecraft.popGuiLayer();
        }
    }

    private void sendSuggestionData() {

        ApiRequests.PlayerBan pBan = ApiRequests.getBan(Minecraft.getInstance().getUser().getName());
        if (pBan == null) return;
        if(pBan.banned) {
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("ban.wolfcraft.cannotsend"), (Component)null);
            return;
        }

        boolean good = SendSomethingToDiscord.sendToDiscordAsSuggestionAndCheckIfWentGood(this.minecraft.getUser().getName() + ": " + IssuesAndSuggestionsScreen.this.suggestionBox.getValue());

        if(good) {
            this.minecraft.popGuiLayer();
        }
    }

    private void playBackgroundMusic() {
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        //manager.play(SimpleSoundInstance.forUI(ModSounds.HAL4_MUSIC.get(), 1.0F));
    }
}
