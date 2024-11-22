package object;

import Main.GamePanel;
import entity.Entity;
import entity.Projectile;

import java.awt.*;

public class OBJ_Shuriken extends Projectile {
    GamePanel gp;
    public OBJ_Shuriken(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Shuriken";
        speed = 4;
        maxLife = 60;
        life = maxLife;
        att = 1;
        usageCost = 1;
        alive = false;
        getImage();
    }

    public void getImage(){
        up1 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        down1 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        left1 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        right1 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectiles/shuriken1", gp.tileSize, gp.tileSize);
    }

    public boolean hasResource(Entity user){
        boolean hasResource = false;
        if(user.mana >= usageCost){
            hasResource = true;
        }
        return hasResource;
    }


    public void subtractResource(Entity user){
        user.mana -= usageCost;
    }

    public Color getParticleColor(){
        Color color = new Color(27, 19, 14);
        return  color;
    }
    public int getParticleSize(){
        int size = 6;// this is just 6 pixels
        return size;
    }
    public int getParticleSpeed(){
        int speed = 1;
        return speed;
    }
    public int getParticleMaxLife(){
        int maxLife = 20;
        return maxLife;
    }
}
