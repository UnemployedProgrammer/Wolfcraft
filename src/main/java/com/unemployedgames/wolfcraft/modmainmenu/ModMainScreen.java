package com.unemployedgames.wolfcraft.modmainmenu;

import com.unemployedgames.wolfcraft.Wolfcraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.TextAndImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ModMainScreen extends Screen {

    private ImageWidget title;

    private TextAndImageButton suggestion;
    private TextAndImageButton issue;
    private TextAndImageButton youtube;
    private TextAndImageButton tiktok;
    private TextAndImageButton modrinth;
    private TextAndImageButton github;


    public ModMainScreen() {
        super(Component.literal("Wolfcrafts Homepage"));
    }

    @Override
    protected void init() {
        super.init();

        title = addRenderableOnly(new ImageWidget((width / 2) - (300 / 2), 20, 300, 60, new ResourceLocation(Wolfcraft.MODID, "textures/gui/wolfcraft_title.png")));
        //suggestion = addRenderableWidget(new TextAndImageButton.Builder(Component.translatable("gui.wolfcraft.issues_and_suggestions.title.suggestion"), MainMenuIcons.SUGGESTIONS, (press) -> {this.minecraft.setScreen(new IssuesAndSuggestionsScreen(false));}).textureSize());

        //CHANGE THE WHOLE ****
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
