package entity;

import Main.GamePanel;

public class Projectile extends Entity{
    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user){
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }
    public void update(){

        if(user == gp.player){
            int monsterIndex = gp.cCheker.checkEntity(this, gp.monster);
            if(monsterIndex!=999){
                gp.player.damageMonster(monsterIndex, att);
                generateParticle(user.projectile, gp.monster[monsterIndex]);
                alive = false;//THE PROJECTILE NEEDS TO DIE IF IT HITS
            }

        }
        if(user != gp.player){
            boolean cPlayer = gp.cCheker.checkPlayer(this);
            if(gp.player.invincible == false && cPlayer == true){
                damagePlayer(att);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }

        }

        switch (direction){
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }

        life--;
        if(life <= 0){
            alive = false;
        }

//        spriteCounter++;
//        if(spriteCounter>12){
//            if(spriteNum == 1){
//                spriteNum = 2;
//            }
//            else if(spriteNum ==2 ){
//                spriteNum = 1;
//            }
//            spriteCounter = 0;
//        }
    }
    public boolean hasResource(Entity user){
        boolean hasResource = false;

        return hasResource;
    }

    public void subtractResource(Entity user){
    }
}
