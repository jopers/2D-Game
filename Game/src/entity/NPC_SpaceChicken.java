package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class NPC_SpaceChicken extends Entity{
	
	BufferedImage sprite = null;
	BufferedImage dialogue = null;

	public NPC_SpaceChicken(GamePanel gamePanel) {
		super(gamePanel);
		name = "Chicken";
		
		direction = "down";
		speed = 0;
		
		getNPCImage();
		setDialogue();
	}
	
	public void getNPCImage() {
		loadSprite();
		down1 = setUp(96, 16);
		down2 = setUp(112, 16);
		
		dialogueFace = setUpDialogue(64, 0);
	}
	
	public void loadSprite() {
		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/npc/npc_sprite_sheet.png"));
			dialogue = ImageIO.read(getClass().getResourceAsStream("/npc/npc_dialogue_sprite_sheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage setUp(int x, int y) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		image = sprite.getSubimage(x, y, 16, 16);
		image = uTool.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
		return image;
	}
	
	public BufferedImage setUpDialogue(int x, int y) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		image = dialogue.getSubimage(x, y, 32, 32);
		image = uTool.scaledImage(image, gamePanel.tileSize*2, gamePanel.tileSize*2);
		return image;
	}
	
	public void setDialogue() {
		dialogues[0] = "42...";
	}
	
	public void speak() {
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
	}
}
