package tile_interactive;

import Main.GamePanel;
import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InteractiveTile extends Entity {

    GamePanel gp;
    public boolean destructible = false;
    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }

    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;

        return isCorrectItem;
    }

    public void playSE(){

    }

    public InteractiveTile getDestroyedForm( ){
        InteractiveTile tile = null;
        return tile;
    }

    public void update(){
        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 10){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }


}
