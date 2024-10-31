package Main;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B, lgc;
    BufferedImage heart_full, heart_half, heart_blank;
//    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    double playTime;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    //Inert objects
    //DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp){
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
        SuperObject heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(lgc);
        //Added anti aliasing cuz why not *skull_emoji*
        //JK it doesn't work
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);

        //TITLE STATE
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        //PLAY STATE
        if(gp.gameState == gp.playState) {
            //DO PLAYSTATE STUFF LATER
            drawPlayerLife();
        }
        //PAUSE STATE
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
            drawPlayerLife();
        }
        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
            drawPlayerLife();
        }

    }

    public void drawPlayerLife(){

        // DEBUGGING: gp.player.life = 3;


        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        //This draws the blank hearts(i.e. max life)
        while(i < gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y, null);
            i++;
            x+= gp.tileSize;
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //This is where we draw the current hearts(kind of like coloring in the blank hearts)
        while (i < gp.player.life){
            //This will draw a half heart and is designed to be drawn over
            g2.drawImage(heart_half,x,y,null);
            i++;

            if(i < gp.player.life){
                //if life is an even number it will draw a full heart
                g2.drawImage(heart_full,x ,y,null);
            }
            i++;
            x += gp.tileSize;

        }


    }
    public void drawTitleScreen(){
        //TITLE NAME
        //THIS WORKS FOR NOW, BUT SHOULD CHANGE THE FONT LATER
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Wolf Vs World";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        //Accent Colors
        g2.setColor(Color.gray);
        //We draw this first so the main colors will be drawn over it
        g2.drawString(text, x+2,y+3);

        //Main Colors
        g2.setColor(Color.white);
        g2.drawString(text,x,y);

        //Display character image
        //SHOULD WORK, BUT DONT WANT TO USE IT FOR NOW

        x = gp.screenWidth / 2 - (gp.tileSize*2)/2;
        y += gp.tileSize *2;
        //g2.drawImage(gp.player.down1,x,y,gp.tileSize*2,gp.tileSize*2, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "New";
        x = getXforCenteredText(text);
        y+= gp.tileSize*3;
        g2.drawString(text,x ,y);
        if(commandNum == 0){
            g2.drawString(">",x-gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Load";
        x = getXforCenteredText(text);
        y+= gp.tileSize;
        g2.drawString(text,x ,y);
        if(commandNum == 1){
            g2.drawString(">",x-gp.tileSize, y);
        }


        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Quit";
        x = getXforCenteredText(text);
        y+= gp.tileSize;
        g2.drawString(text,x ,y);
        if(commandNum == 2){
            g2.drawString(">",x-gp.tileSize, y);
        }

    }

    public void drawPauseScreen(){
        String text = "PAUSED";
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 ;

        g2.drawString(text, x, y);

    }
    public void drawDialogueScreen(){
        //Drawing a big rectangle to display dialogue
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize;
        y += gp.tileSize;

        //This will split dialogue every '\n' and will put it into 'line'
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y+=40;
        }


    }

    public void drawSubWindow(int x, int y, int width, int height){
        //Using RGB to make the color this time
        //The last parameter sets the opacity of the window
        Color c = new Color(0,0,0, 220);
        g2.setColor(c);
        //the arc dimensions are arbitrary chosen to make the rounded rectangle look nice
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        //This will outline the above rectangle in white
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width -10, height-10, 25, 25);

    }

    public int getXforCenteredText(String text){


        //THIS MAKES X BE THE CENTRE OF THE SCREEN REGARDLESS OF TEXT LENGTH
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = (gp.screenWidth/2) - (length/2);

        return x;
    }
}
