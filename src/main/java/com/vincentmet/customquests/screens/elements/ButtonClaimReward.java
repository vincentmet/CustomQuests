package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.IQuestReward;
import com.vincentmet.customquests.quests.QuestUserProgress;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class ButtonClaimReward implements IQuestingGuiElement {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private int quest;
    private List<IQuestReward> questRewards;
    private String textUnclaimed = "Claim";
    private String textClaimed = "Claimed!";

    public ButtonClaimReward(int posX, int posY, int quest, List<IQuestReward> questRewards){
        this.x = posX;
        this.y = posY;
        this.quest = quest;
        this.questRewards = questRewards;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        String text = QuestUserProgress.isRewardClaimed(Utils.getUUID("vincentmet"), quest) ? textClaimed : textUnclaimed;
        if(!QuestUserProgress.isRewardClaimed(Utils.getUUID("vincentmet"), quest) && QuestUserProgress.areAllRequirementsCompleted(Utils.getUUID("vincentmet"), quest)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_PRESSED);
            }else{
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED);
            }
        }else{
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_DISABLED);
        }
        gui.blit(x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(text)/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                text,
                0xFFFFFF,
                false,
                false
        ).render(gui, player, mouseX, mouseY);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(!QuestUserProgress.isRewardClaimed(Utils.getUUID("vincentmet"), quest) && QuestUserProgress.areAllRequirementsCompleted(Utils.getUUID("vincentmet"), quest)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                if(!QuestUserProgress.isRewardClaimed(Utils.getUUID("vincentmet"), quest) && QuestUserProgress.areAllRequirementsCompleted(Utils.getUUID("vincentmet"), quest)){
                    for(IQuestReward reward : questRewards){
                        reward.executeReward(player);
                        QuestUserProgress.setRewardsClaimed(Utils.getUUID("vincentmet"), quest);
                        Ref.shouldSaveNextTick = true;
                    }
                }
            }
        }
    }
}
