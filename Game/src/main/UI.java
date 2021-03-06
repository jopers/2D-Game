package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import entity.Entity;
import entity.NPC_Book;
import entity.NPC_Computer;
import entity.NPC_Flower;
import entity.NPC_SpaceChicken;
import entity.Player;
import object.OBJ_Heart;
import object.OBJ_Key;

public class UI {

	GamePanel gamePanel;
	Graphics2D g2;
	Font smallPixelFont;
	BufferedImage heart_full, heart_half, heart_empty;
	BufferedImage key_empty, key_full;
	BufferedImage flower_face, computer_face, chicken_face, book_face;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	public String currentDialogue = "";
	public int commandNum = 0;
	public int tttHeight = 0;
	public int tttWidth = 0;
	public int tttX = 0;
	public int tttY = 0;
	public HashMap<Rectangle, String> ttt = new HashMap<Rectangle, String>();
	public boolean setUpStage = true;
	public boolean playerMoved = false;
	public int playerMoves = 0;
	public int flowerMoves = 0;
	public LinkedList<Rectangle> movesPlayer = new LinkedList<Rectangle>();
	public LinkedList<Rectangle> movesFlower = new LinkedList<Rectangle>();
	public Rectangle one, zero, submit;
	public String digitAttempt = "";
	public int digCounter = 0;
	ArrayList<TripleValue> digits = new ArrayList<TripleValue>();
	
	public UI(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		InputStream is = getClass().getResourceAsStream("/font/small_pixel.ttf");
		try {
			smallPixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		// creating HUD object
		Entity heart = new OBJ_Heart(gamePanel);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_empty = heart.image3;
		
		Entity flower = new NPC_Flower(gamePanel);
		flower_face = flower.dialogueFace;
		Entity computer = new NPC_Computer(gamePanel);
		computer_face = computer.dialogueFace;
		Entity chicken = new NPC_SpaceChicken(gamePanel);
		chicken_face = chicken.dialogueFace;
		Entity book = new NPC_Book(gamePanel);
		book_face = book.dialogueFace;
		
		Entity key = new OBJ_Key(gamePanel);
		key_empty = key.image;
		key_full = key.down1;
		
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		g2.setFont(smallPixelFont);
		g2.setColor(Color.WHITE);
		
		// title state
		if(gamePanel.gameState == gamePanel.titleState) {
			drawTitleScreen();
		}
		
		// play state
		if(gamePanel.gameState == gamePanel.playState) { // play state
			drawPlayerLife();
			drawPlayerKeys();
		// pause state
		} else if(gamePanel.gameState == gamePanel.pauseState) { // pause state
			drawPauseScreen();
			drawPlayerLife();
			drawPlayerKeys();
		// dialogue state
		} else if (gamePanel.gameState == gamePanel.dialogueState) { // dialogue state
			drawDialogueScreen();
			drawPlayerLife();
			drawPlayerKeys();
		} else if (gamePanel.gameState == gamePanel.minigameState && gamePanel.currentMinigame == "tictactoe") {
			if(setUpStage) {
				setUpTicTacToe();
				setUpStage = false;
			}
			drawTicTacToe();
			//drawPlayerLife();
		} else if(gamePanel.gameState == gamePanel.minigameState && gamePanel.currentMinigame == "digit") {
			drawDigitMinigame();
		}
	}
	
	public void drawPlayerLife() {
		int x = gamePanel.tileSize/4;
		int y = gamePanel.tileSize/4;
		int i = 0;
		
		// draws max life
		while(i < gamePanel.player.maxLife/2) {
			g2.drawImage(heart_empty, x, y, null);
			i++;
			x += gamePanel.tileSize + gamePanel.tileSize/4;
		}
		
		x = gamePanel.tileSize/4;
		y = gamePanel.tileSize/4;
		i = 0;
		while(i < gamePanel.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if(i < gamePanel.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += gamePanel.tileSize + gamePanel.tileSize/4;
		}
		
	}
	
	public void drawPlayerKeys() {
		int x = gamePanel.tileSize/4;
		int y = gamePanel.tileSize + gamePanel.tileSize/3;
		int i = 0;
		
		// draws max available keys
		while(i < 3) {
			g2.drawImage(key_empty, x, y, null);
			i++;
			x += gamePanel.tileSize + gamePanel.tileSize/4;
		}
		
		x = gamePanel.tileSize/4;
		y = gamePanel.tileSize + gamePanel.tileSize/3;
		i = 0;
		while(i < gamePanel.player.keys) {
			g2.drawImage(key_full, x, y, null);
			i++;
			x += gamePanel.tileSize + gamePanel.tileSize/4;
		}
		
	}
	
	public void drawTitleScreen() {
		// title
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
		String text = "Adventure Game";
		int x = getXCenterText(text);
		int y = gamePanel.tileSize*3;
		g2.setColor(Color.GRAY); // shadow effect
		g2.drawString(text, x+5, y+5); // shadow effect
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		
		// menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
		
		text = "New Game";
		x = getXCenterText(text);
		y += gamePanel.tileSize*3;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", getXMarkerLeftSide(text), y);
			g2.drawString("<", getXMarkerRightSide(text), y);
		}
		
		text = "Load Game";
		x = getXCenterText(text);
		y += gamePanel.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", getXMarkerLeftSide(text), y);
			g2.drawString("<", getXMarkerRightSide(text), y);
		}
		
		text = "Quit";
		x = getXCenterText(text);
		y += gamePanel.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawString(">", getXMarkerLeftSide(text), y);
			g2.drawString("<", getXMarkerRightSide(text), y);
		}
	}
	
	public void drawPauseScreen() {
		String text = "Paused";
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		int x = getXCenterText(text);
		int y = gamePanel.screenHeight/2;
		g2.drawString(text, x, y);
	}
	
	public void drawDialogueScreen() {
		// window for dialogue
		int x = gamePanel.tileSize*3;
		int y = gamePanel.tileSize*8;
		int width = gamePanel.screenWidth - (gamePanel.tileSize*6);
		int height = gamePanel.tileSize*3;
		
		drawSubWindow(x, y, width, height);
		
		// TODO prob fix better implementation for this
		int npcIndex = gamePanel.cChecker.checkEntity(gamePanel.player, gamePanel.npc);
		if(npcIndex != 999) { // not sure this check is needed
			if(gamePanel.npc[npcIndex].name == "Flower") {
				int faceHeight = ((y + height/2) - gamePanel.tileSize);
				g2.drawImage(flower_face, null, x+30, faceHeight);
				x += gamePanel.tileSize*3;
				y += gamePanel.tileSize;
			} else if(gamePanel.npc[npcIndex].name == "Computer") {
				int faceHeight = ((y + height/2) - gamePanel.tileSize);
				g2.drawImage(computer_face, null, x+30, faceHeight);
				x += gamePanel.tileSize*3;
				y += gamePanel.tileSize;
			} else if(gamePanel.npc[npcIndex].name == "Chicken") {
				int faceHeight = ((y + height/2) - gamePanel.tileSize);
				g2.drawImage(chicken_face, null, x+30, faceHeight);
				x += gamePanel.tileSize*3;
				y += gamePanel.tileSize;
			} else if(gamePanel.npc[npcIndex].name == "Book") {
				int faceHeight = ((y + height/2) - gamePanel.tileSize);
				g2.drawImage(book_face, null, x+30, faceHeight);
				x += gamePanel.tileSize*3;
				y += gamePanel.tileSize;
			} else {
				x += gamePanel.tileSize;
				y += gamePanel.tileSize;
			}	
		}
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
		
	}
	
	public void drawTicTacToe() {
		Color c = new Color(0, 0, 0, 255);
		g2.setColor(c);
		g2.fillRoundRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight, 0, 0);
		g2.drawImage(flower_face, null, (gamePanel.screenWidth/2)-flower_face.getWidth()/2, gamePanel.tileSize);
		// window for tictactoe minigame
		drawSubWindow(tttX, tttY, tttWidth, tttHeight);
		g2.setColor(Color.WHITE);
		g2.drawLine(tttX+(tttWidth/3), tttY+2, tttX+(tttWidth/3), tttY+tttHeight-2);
		g2.drawLine(tttX+((tttWidth/3)*2), tttY+2, tttX+((tttWidth/3)*2), tttY+tttHeight-2);
		g2.drawLine(tttX+2, tttY+(tttHeight/3), tttX+tttWidth-2, tttY+(tttHeight/3));
		g2.drawLine(tttX+2, tttY+((tttHeight/3)*2), tttX+tttWidth-2, tttY+((tttHeight/3)*2));
        for(Rectangle index : ttt.keySet()) {
        	if(ttt.get(index).equals("player")) {
        		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        		g2.drawString("player", index.x, index.y + (index.height/2));
        	}
        	if(ttt.get(index).equals("flower")) {
        		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        		g2.drawString("flower", index.x, index.y + (index.height/2));
        	}
        }
        
        // TODO its prob super bad that this get called 60x per sec..
        if(tttPlayerWin()) {
        	gamePanel.gameState = gamePanel.playState;
        	gamePanel.currentMinigame = "";
        	gamePanel.stopMusic();
        	gamePanel.playMusic(0);
        	gamePanel.player.tttWon = true;
        	for(int i = 0; i < gamePanel.obj.size(); i++) {
				if(gamePanel.obj.get(i).name == "Vine") {
					gamePanel.obj.remove(i);
					i--;
				}
			}
        } else if(tttFlowerWin()) {
        	setUpStage = true;
        	gamePanel.gameState = gamePanel.playState;
        	gamePanel.currentMinigame = "";
        	gamePanel.stopMusic();
        	gamePanel.playMusic(0);
        }
	}
	
	private boolean tttPlayerWin() {
        LinkedList<String> result = new LinkedList<>(ttt.values());
        // rows
        if(result.get(1) == "player" && result.get(4) == "player" && result.get(7) == "player") { return true; }
        if(result.get(0) == "player" && result.get(3) == "player" && result.get(6) == "player") { return true; }
        if(result.get(2) == "player" && result.get(5) == "player" && result.get(8) == "player") { return true; }
        
        // cols
        if(result.get(7) == "player" && result.get(6) == "player" && result.get(2) == "player") { return true; }
        if(result.get(1) == "player" && result.get(0) == "player" && result.get(5) == "player") { return true; }
        if(result.get(4) == "player" && result.get(3) == "player" && result.get(8) == "player") { return true; }
        
        // diagonals
        if(result.get(7) == "player" && result.get(0) == "player" && result.get(8) == "player") { return true; }
        if(result.get(2) == "player" && result.get(0) == "player" && result.get(4) == "player") { return true; }
        
		return false;
	}
	
	private boolean tttFlowerWin() {
        LinkedList<String> result = new LinkedList<>(ttt.values());
        // rows
        if(result.get(1) == "flower" && result.get(4) == "flower" && result.get(7) == "flower") { return true; }
        if(result.get(0) == "flower" && result.get(3) == "flower" && result.get(6) == "flower") { return true; }
        if(result.get(2) == "flower" && result.get(5) == "flower" && result.get(8) == "flower") { return true; }
        
        // cols
        if(result.get(7) == "flower" && result.get(6) == "flower" && result.get(2) == "flower") { return true; }
        if(result.get(1) == "flower" && result.get(0) == "flower" && result.get(5) == "flower") { return true; }
        if(result.get(4) == "flower" && result.get(3) == "flower" && result.get(8) == "flower") { return true; }
        
        // diagonals
        if(result.get(7) == "flower" && result.get(0) == "flower" && result.get(8) == "flower") { return true; }
        if(result.get(2) == "flower" && result.get(0) == "flower" && result.get(4) == "flower") { return true; }
        
		return false;
	}

	public void setUpTicTacToe() {
		/*
		tttHeight = gamePanel.tileSize*5;
		tttWidth = gamePanel.tileSize*5;
		tttX = gamePanel.tileSize*5;
		tttY = gamePanel.tileSize*5;
		*/
		// TODO buggar om man centrerar
		tttHeight = gamePanel.tileSize*5;
		tttWidth = gamePanel.tileSize*5;
		tttX = (gamePanel.screenWidth/2)-(tttWidth/2);
		tttY = (gamePanel.screenHeight/2)-(tttHeight/2);
        int w = gamePanel.ui.tttWidth;
        int h = gamePanel.ui.tttHeight;
        int posX = gamePanel.ui.tttX;
        int posY = gamePanel.ui.tttY;
        // setup
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                int x = posX + ((w / 3) * col);
                int y = posY + ((h / 3) * row);
                Rectangle cell = new Rectangle(x, y, w / 3, h / 3);
                ttt.put(cell, "");
            }
        }
	}
	
	public void drawDigitMinigame() {
		Color c = new Color(0, 0, 0, 255);
		g2.setColor(c);
		g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
		g2.drawImage(computer_face, null, (gamePanel.screenWidth/2)-computer_face.getWidth()/2, gamePanel.tileSize);
		g2.setColor(Color.WHITE);
		
		one = new Rectangle(gamePanel.tileSize*7, gamePanel.tileSize*8, gamePanel.tileSize, gamePanel.tileSize);
		zero = new Rectangle(gamePanel.tileSize*8, gamePanel.tileSize*8, gamePanel.tileSize, gamePanel.tileSize);
		submit = new Rectangle(gamePanel.tileSize*9, gamePanel.tileSize*8, gamePanel.tileSize, gamePanel.tileSize);
		
		g2.draw(one);
		g2.draw(zero);
		g2.draw(submit);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
		g2.drawString(digitAttempt, gamePanel.tileSize*8, (gamePanel.tileSize*8)-50);
		
		
		//g2.setColor(Color.BLACK);
		g2.drawString("1", gamePanel.tileSize*7+(one.width/2)-5, gamePanel.tileSize*8+(one.height/2)-10);
		g2.drawString("0", gamePanel.tileSize*8+(zero.width/2)-5, gamePanel.tileSize*8+(zero.height/2)-10);
		g2.drawString("OK", gamePanel.tileSize*9+(submit.width/2)-10, gamePanel.tileSize*8+(submit.height/2)-10);
		
		digCounter++;
		if(digCounter > 15) {
			digits.clear();
			for(int i = 0; i < 40; i++) {
				digits.add(new TripleValue(RanBinNumber(), ranNumber(250), ranNumber(600)));
				digits.add(new TripleValue(RanBinNumber(), ranNumber(250)+500, ranNumber(600)));
			}
			digCounter = 0;
		}
		
		for(int i = 0; i < digits.size(); i++) {
			g2.drawString(digits.get(i).bin, digits.get(i).x, digits.get(i).y);
		}
		
		if(gamePanel.player.digitComplete) {
			gamePanel.npc[2].updateNPCImage();
			gamePanel.currentMinigame = "";
			gamePanel.gameState = gamePanel.playState;
        	gamePanel.stopMusic();
        	gamePanel.playMusic(0);
        	for(int i = 0; i < gamePanel.obj.size(); i++) {
				if(gamePanel.obj.get(i).name == "Laser") {
					gamePanel.obj.remove(i);
					i--;
				}
			}
		}
	}
	
	public String RanBinNumber() {
	    Random rg = new Random();
	    int n = rg.nextInt(64);
	    return Integer.toBinaryString(n);
	}
	
	public int ranNumber(int max) {
	    Random rg = new Random();
	    int n = rg.nextInt(max);
	    return n;
	}
	
	class TripleValue {
	    int x;
	    int y;
	    String bin;

	    public TripleValue(String bin,int x, int y) {
	        this.x = x;
	        this.y = y;
	        this.bin = bin;
	    }
	}
	
	
	public void drawSubWindow(int x, int y, int w, int h) {
		// little rectangle
		Color c = new Color(0, 0, 0, 255);
		g2.setColor(c);
		g2.fillRoundRect(x, y, w, h, 35, 35);
		
		// white border for the rectangle above
		c = new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(x+2, y+2, w-4, h-4, 25, 25);
	}
	
	public int getXCenterText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gamePanel.screenWidth/2 - length/2;
		return x;
	}
	
	public int getXMarkerRightSide(String text) {
		 return getXCenterText(text) + (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() + gamePanel.tileSize;
	}
	
	public int getXMarkerLeftSide(String text) {
		 return getXCenterText(text) - gamePanel.tileSize;
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
}
