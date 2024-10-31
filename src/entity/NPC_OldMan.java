package entity;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp){
        //Once again, this is so the Entity class can recieve gp
        super(gp);

        direction = "down";
        speed = 1;
        getImage();
        setDialogue();
    }

    public void getImage(){
        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");
    }

    public void setDialogue(){
        dialogues[0] = "lil \nbro";
        dialogues[1] = "big \nbro";
        dialogues[2] = "bigger bro";
        dialogues[3] = "biggest bro";
    }

    public void setAction(){

        actionLockCounter++;
        if(actionLockCounter == 120) {
            Random random = new Random();
            //This will pick a random number from 1 to 100
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    public void speak(){

        //Calls the Abstract speak class
        super.speak();
    }


}