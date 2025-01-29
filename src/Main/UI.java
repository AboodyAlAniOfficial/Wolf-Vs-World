package Main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Shuriken;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B, lgc;
    BufferedImage heart_full, heart_half, heart_blank, shurikenImage;
    //    BufferedImage keyImage;
    public boolean messageOn = false;
    //    public String message = "";
//    int messageCounter = 0;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    double playTime;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int transitionCounter = 0;
    public Entity npc;

    //Inert objects
    //DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        //these 2 will probably be unused
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        //will most likely use this one
        try {
            InputStream is = getClass().getResourceAsStream("/font/Louis George Cafe.ttf");
            lgc = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        OBJ_Key key = new OBJ_Key(gp);
//        keyImage = key.image;

        //CREATING HUD OBJECTS
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity shuriken = new OBJ_Shuriken(gp);
        shurikenImage = shuriken.up1;
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(lgc);
        //Added anti aliasing cuz why not *skull_emoji*
        //JK it doesn't work
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        //PLAY STATE
        if (gp.gameState == gp.playState) {
            //DO PLAYSTATE STUFF LATER
            drawPlayerLife();
            drawMessage();
        }
        //PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
            drawPlayerLife();
        }
        //DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
            drawPlayerLife();
        }

        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        if (gp.gameState == gp.optionsState) {
            drawOptionScreen();
        }
//        //Death state
        if (gp.gameState == gp.deathState) {
            drawDeathScreen();
        }
        if(gp.gameState == gp.transitionState){
            drawTransitionScreen();
        }
        if(gp.gameState == gp.tradeState){
            drawTradeScreen();

        }

    }

    public void drawPlayerLife() {

        // DEBUGGING: gp.player.life = 3;


        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //This draws the blank hearts(i.e. max life)
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        //This is where we draw the current hearts(kind of like coloring in the blank hearts)
        while (i < gp.player.life) {
            //This will draw a half heart and is designed to be drawn over
            g2.drawImage(heart_half, x, y, null);
            i++;

            if (i < gp.player.life) {
                //if life is an even number it will draw a full heart
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;

        }

        //DRAW SHURIKENS
        x = gp.tileSize / 2;
        y = gp.tileSize * 2;
        g2.drawImage(shurikenImage, x, y, null);
        g2.setFont(new Font("Arial", Font.PLAIN, 32));
        g2.drawString("x " + gp.player.mana, x + gp.tileSize, y + gp.tileSize - 10);


    }

    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                //Shadow
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);
                //Actual message
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter); //This is essentially messagecounter++
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {
        //TITLE NAME
        //THIS WORKS FOR NOW, BUT SHOULD CHANGE THE FONT LATER
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Wolf Vs World";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        //Accent Colors
        g2.setColor(Color.gray);
        //We draw this first so the main colors will be drawn over it
        g2.drawString(text, x + 2, y + 3);

        //Main Colors
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //Display character image
        //SHOULD WORK, BUT DONT WANT TO USE IT FOR NOW

        x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        y += gp.tileSize * 2;
        //g2.drawImage(gp.player.down1,x,y,gp.tileSize*2,gp.tileSize*2, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "New";
        x = getXforCenteredText(text);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Load";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Quit";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void drawPauseScreen() {
        String text = "PAUSED";
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);

    }

    public void drawDialogueScreen() {
        //Drawing a big rectangle to display dialogue
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize;
        y += gp.tileSize;

        g2.setColor(new Color(76, 43, 8));
        //This will split dialogue every '\n' and will put it into 'line'
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }


    }

    public void drawCharacterScreen() {
        //CREATE A FRAME
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 6;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //CREATE TEXT
        g2.setColor(new Color(76, 43, 8));
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;//SAME AS FONT

        //NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight;

        //VALUES
        //We need to move textX to the right side and reset textY
        int tailX = frameX + frameWidth - 30;
        textY = frameY + gp.tileSize;

        String value;
        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        value = String.valueOf(gp.player.att);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        value = String.valueOf(gp.player.nextLvlExp);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize + 10, textY - 30, null);

    }

    public void drawInventory(Entity entity, boolean cursor) {

        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        if(entity == gp.player){
            frameX = gp.tileSize * 12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }else{
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;

        }

        //Frame
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //SLOTS
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;


        //DRAW ITEMS in INVENTORY
        for (int i = 0; i < entity.inventory.size(); i++) {
            //HIGHLIGHT EQUIPPED ITEMS
            if (entity.inventory.get(i) == entity.currentWeapon) {
                g2.setColor(new Color(109, 57, 20));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            slotX += gp.tileSize;

            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gp.tileSize;
            }

        }

        //CURSOR
        if(cursor == true){
            int cursorX = slotXStart + (gp.tileSize * slotCol);
            int cursorY = slotYStart + (gp.tileSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;
            //DRAW CURSOr
            g2.setColor(new Color(76, 43, 8));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            //DESCRIPTION FRAME
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;
            //Draw description frame here if you want it to stay when item slot is blank


            //DESCRIPTION TEXT
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(28F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if (itemIndex < entity.inventory.size()) {
                //Draw subwindow here so it will disappear when the item slot is blank
                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
                g2.setColor(new Color(76, 43, 8));
                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }

        }

    }

    public void drawOptionScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        //Sub Window
        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0:
                options_Top(frameX, frameY);
                break;
            case 1:
                options_FullScreenNotification(frameX, frameY);
                break;
            case 2:
                options_Controls(frameX, frameY);
                break;
            case 3:
                options_ConfirmQuit(frameX, frameY);
                break;
        }

        gp.keyH.enterPressed = false;

    }

    public void drawTransitionScreen(){

        transitionCounter++;
        g2.setColor(new Color(0,0,0,transitionCounter*5));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        if(transitionCounter == 50){
            transitionCounter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }

    public void options_Top(int frameX, int frameY) {
        int textX;
        int textY;

        //Title,
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //Fullscreen toggle
        text = "Full Screen";
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                if (gp.fullScreenOn == false) {
                    gp.fullScreenOn = true;
                } else if (gp.fullScreenOn == true) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        //Music
        textY += gp.tileSize;
        text = "Music";
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
        }

        //SE
        textY += gp.tileSize;
        text = "SE";
        g2.drawString(text, textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
        }
        //Control
        textY += gp.tileSize;
        text = "Controls";
        g2.drawString(text, textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }
        //Quit
        textY += gp.tileSize;
        text = "Quit";
        g2.drawString(text, textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }

        //Back
        textY += gp.tileSize * 2;
        text = "Back";
        g2.drawString(text, textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        //FullScreen checkbox
        textX = frameX + gp.tileSize * 4 + 24;
        textY = frameY + gp.tileSize * 2 + 24;
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(textX, textY, 24, 24);
        if (gp.fullScreenOn == true) {
            g2.fillRect(textX, textY, 24, 24);
        }

        //Music volume slider
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);//120/5 = 24
        int volWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volWidth, 24);

        //SE volume slider
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volWidth, 24);

        gp.config.saveConfig();

    }

    public void drawSubWindow(int x, int y, int width, int height) {
        //Using RGB to make the color this time
        //The last parameter sets the opacity of the window
        Color c = new Color(200, 173, 127, 219);
        g2.setColor(c);
        //the arc dimensions are arbitrary chosen to make the rounded rectangle look nice
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(150, 120, 89);
        g2.setColor(c);
        //This will outline the above rectangle in white
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }

    public void options_FullScreenNotification(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "The change will take \nplace after restart";

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //Back button
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }


    }

    public void options_Controls(int frameX, int frameY) {
        int textX;
        int textY;

        //Title
        String text = "Controls";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Movement", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Attack", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Throw", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Status Screen", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Pause", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Menu", textX, textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize * 5;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX, textY);
        textY += gp.tileSize;
        g2.drawString("ENT", textX, textY);
        textY += gp.tileSize;
        g2.drawString("F", textX, textY);
        textY += gp.tileSize;
        g2.drawString("C", textX, textY);
        textY += gp.tileSize;
        g2.drawString("P", textX, textY);
        textY += gp.tileSize;
        g2.drawString("ESC", textX, textY);
        textY += gp.tileSize;

        //Back
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 3;
            }
        }
    }

    public void options_ConfirmQuit(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Quit the game and \nreturn to title Screen?";

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //Yes
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }

        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 4;
            }
        }
    }

    public void drawDeathScreen() {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x, y;
        String text = "DEATH";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 150F));
        g2.setColor(Color.white);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);
        g2.setColor(Color.red);
        g2.drawString(text, x - 1, y - 1);

//        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
//        text = "Continue";
//        x = getXforCenteredText(text);
//        y += gp.tileSize * 4;
//        g2.drawString(text,x,y);
//        if(commandNum == 0){
//            g2.drawString(">",x-40,y);
//        }
//
//        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
//        text = "Quit";
//        x = getXforCenteredText(text);
//        y += gp.tileSize ;
//        g2.drawString(text,x,y);
//        if(commandNum == 1){
//            g2.drawString(">",x-40,y);
//        }

    }

    public void drawTradeScreen(){
        switch (subState){
            case 0: trade_Select(); break;
            case 1: trade_Buy(); break;
            case 2: trade_Sell(); break;
        }

        gp.keyH.enterPressed = false;
    }

    public void trade_Select(){
        drawDialogueScreen();

        int x = gp.tileSize * 15;
        int y = gp.tileSize * 4;
        int width = gp.tileSize *3;
        int height = gp.tileSize*4;
        drawSubWindow(x,y,width,height);

        x += gp.tileSize /2;
        y += gp.tileSize;
        g2.drawString("Buy", x,y);
        if(commandNum == 0) {
            g2.drawString(">", x-12, y);
            if(gp.keyH.enterPressed == true){
                subState = 1;
            }
        }
        y += gp.tileSize;
        g2.drawString("Sell", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-12, y);
            if(gp.keyH.enterPressed ==  true){
                subState = 2;
            }
        }
        y += gp.tileSize;
        g2.drawString("Leave", x ,y);
        if(commandNum == 2) {
            g2.drawString(">", x-12, y);
            if(gp.keyH.enterPressed ==  true){
                commandNum = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "Thank you, Come again";

            }

        }

    }
    public void trade_Buy(){

        //PLAYER INVENTORY
        drawInventory(gp.player, false);
        //NPC
        drawInventory(npc, true);

        //
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(new Color(76, 43, 8));
        g2.drawString("[ESC]", x+24, y+60);

        //Player's coin
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(new Color(76, 43, 8));
        g2.drawString("Coin : " + gp.player.coin, x+24, y+60);

        //Price window
        int index = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if(index < npc.inventory.size()){
            x = (int)(gp.tileSize * 5);
            y = (int)(gp.tileSize * 5.5);
            width = gp.tileSize * 3;
            height = gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.setColor(new Color(76, 43, 8));
            g2.drawString("Price:", x+10, y+32);
            String text ="" + npc.inventory.get(index).value;
            x = getXForAlignToRight(text, gp.tileSize*8);
            g2.drawString(text, x - 12, y + 32);
            //Buying
            if(gp.keyH.enterPressed == true) {
                if (npc.inventory.get(index).value > gp.player.coin) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Insufficient Funds";
                    drawDialogueScreen();
                } else if (gp.player.inventory.size() == gp.player.maxInventorySize) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Inventory Full";
                    drawDialogueScreen();
                } else {
                    gp.player.coin -= npc.inventory.get(index).value;
                    gp.player.inventory.add(npc.inventory.get(index));
                }
            }
        }
    }
    public void trade_Sell(){
        drawInventory(gp.player, true);

        //
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(new Color(76, 43, 8));
        g2.drawString("[ESC]", x+24, y+60);

        //Player's coin
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.setColor(new Color(76, 43, 8));
        g2.drawString("Coin : " + gp.player.coin, x+24, y+60);

        //Price window
        int index = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if(index < gp.player.inventory.size()){
            x = (int)(gp.tileSize * 15);
            y = (int)(gp.tileSize * 5.5);
            width = gp.tileSize * 3;
            height = gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.setColor(new Color(76, 43, 8));
            g2.drawString("Price:", x+10, y+32);
            String text ="" + (gp.player.inventory.get(index).value * 0.8);
            x = getXForAlignToRight(text, gp.tileSize*18);
            g2.drawString(text, x - 12, y + 32);
            //Selling
            if(gp.keyH.enterPressed == true) {
                if(gp.player.inventory.get(index) == gp.player.currentWeapon){
                    commandNum = 0;
                    subState =0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Cannot sell equipped items";
                }else{
                    gp.player.inventory.remove(index);
                    gp.player.coin += gp.player.inventory.get(index).value;
                }

            }
        }

    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
//        0 1 2 3 4
//        5 6 7 8 9
//   10+  0 1 2 3 4
        int index = slotCol + (slotRow * 5);
        return index;
    }

    public int getXforCenteredText(String text) {
        //THIS MAKES X BE THE CENTRE OF THE SCREEN REGARDLESS OF TEXT LENGTH
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = (gp.screenWidth / 2) - (length / 2);

        return x;
    }

    public int getXForAlignToRight(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;

        return x;
    }
}
