package monster;

import Main.GamePanel;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import object.OBJ_Shuriken;

import java.util.Random;

public class MON_GreenSlime extends Entity {
    GamePanel gp;

    public MON_GreenSlime(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_Monster;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        att = 2;
        exp = 5;
        coin = 5;
        projectile = new OBJ_Shuriken(gp) ;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage(){
        up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
    }

    public void setAction(){
        //Borrowing this from old man class

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

        int i = new Random().nextInt(10) + 1;
        if(i > 9 && projectile.alive == false && shotCounter == 30){
            projectile.set(worldX,worldY,direction,true,this);
            gp.projectileList.add(projectile);
            shotCounter = 0;
        }
    }

    public void damageReaction(){
        actionLockCounter = 0;
        direction = gp.player.direction;
    }
}
