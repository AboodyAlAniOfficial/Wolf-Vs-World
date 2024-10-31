package object;

import Main.GamePanel;
import Main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image, image2, image3;
    public boolean collision = false;
    public int worldX, worldY;
    public String name;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    UtilityTool uTool = new UtilityTool();



    public void draw(Graphics2D g2, GamePanel gp){
        //'gp.player' stuff is to offset the MC to be in the centre of the screen
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //this condition ensures only the visible screen is drawn
        //the area to the left, right, above and below the player
        if(worldX  + gp.tileSize> gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize< gp.player.worldY + gp.player.screenY){
            g2.drawImage(image, screenX ,screenY, gp.tileSize, gp.tileSize, null);

        }

    }
}
