package entity;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gp;

    //MISC
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2,
            attackLeft1, attackLeft2,attackRight1, attackRight2;
    public Rectangle solidArea = new Rectangle(0,0, 48,48); //Default collision area
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX,solidAreaDefaultY;
    public String dialogues[] = new String[20];
    public BufferedImage image, image2, image3;
    public boolean collision = false;
    boolean attacking = false;

    //STATE
    public int worldX, worldY;
    public boolean collisionOn = false;
    public String direction = "down";
    public int dialogueIndex = 0;
    public int spriteNum = 1;
    public boolean invincible = false;
    public boolean alive = true;
    //This is for the death 'animation'
    public boolean dying = false;
    boolean hpBarOn = false;

    //Counters
    //This is to add an interval between action switching for NPC's
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int spriteCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    //cooldown between shots
    public int shotCounter = 0;
    public int deathScreenCounter = 0;

    //CHARACTER STATS
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;//WILL PROBABLY CHANGE THE NAME FOR THIS(WHATEVER IT WAS IN SEKIRO)
    public String name;
    public  int speed;
    public int level;
    public int att; // NOT FOR ITEMS
    public int def = 0;
    public int exp;
    public int nextLvlExp;
    public int coin;
    public Entity currentWeapon;
    public Entity projectile;

    //ITEM STATS
    public int value;
    public int attackValue; //FOR ITEMS
    public String description = "";
    public int usageCost;

    //TYPE
    public int type; //0 = player, 1 = npc, 2 = monster
    //SOME OF THESE PROBABLY WILL NEVER GET USED
    public final int type_Player = 0;
    public final int type_NPC = 1;
    public final int type_Monster = 2;
    public final int type_Weapon = 3;
    public final int type_Consumable = 4;
    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public void setAction(){}
    public Color getParticleColor(){Color color = null;return  color;
    }
    public int getParticleSize(){int size = 0;// this is just 6 pixels
        return size;
    }
    public int getParticleSpeed(){int speed = 0;return speed;
    }
    public int getParticleMaxLife(){int maxLife = 0;return maxLife;
    }
    public void generateParticle(Entity generator, Entity target){
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2,-1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2,-1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2,1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2,1);

        gp.particlesList.add(p1);
        gp.particlesList.add(p2);
        gp.particlesList.add(p3);
        gp.particlesList.add(p4);
    }
    public void use(Entity entity){}
    //INSHALLAH THIS WORKS
    public void set(int worldX, int worldY, String Direction, boolean alive, Entity user){}
    public void damageReaction(){
    }
    public void speak(){
        //This resets the dialogue, should change later to have them repeat their last line when the rest are exhausted
        if(dialogues[dialogueIndex] == null){
        dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        //This switch statement will make it so the NPC will face toward the player during dialogue
        switch (gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }

    }
    public boolean hasResource(Entity user){
        boolean hasResource = false;

        return hasResource;
    }
    public void subtractResource(Entity user){
    }
    public void update(){
        //If a subclass has the same method as super, it will override
        //So this will run the setAction of the subclass
        setAction();

        collisionOn = false;
        gp.cCheker.checkTile(this);
        gp.cCheker.checkObject(this, false);
        gp.cCheker.checkEntity(this,gp.npc);
        gp.cCheker.checkEntity(this,gp.monster);
        gp.cCheker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.cCheker.checkPlayer(this);

        if(this.type == type_Monster && contactPlayer == true){
            damagePlayer(att);
        }

        //IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(collisionOn == false){
            switch(direction){
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;

            }
        }

        spriteCounter++;
        if (spriteCounter > 15) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        //i frames
        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 20){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        //Adds frames for the shot cooldown
        if(shotCounter < 30){
            shotCounter++;
        }
    }

    public void damagePlayer(int att){
        if(gp.player.invincible == false){
            //gp.playSE(6);
            int damage = att - gp.player.def;
            if(damage < 0){
                damage = 0;
            }
            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        //'gp.player' stuff is to offset the MC to be in the centre of the screen
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //this condition ensures only the visible screen is drawn
        //the area to the left, right, above and below the player
        if(worldX  + gp.tileSize> gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize< gp.player.worldY + gp.player.screenY){
            switch (direction) {
                case "up":
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                    break;
                case "down":
                    if(spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2){
                        image = down2;
                    }
                    break;
                case "left":
                    if(spriteNum == 1) {
                        image = left1;
                    }
                    if(spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if(spriteNum == 1) {
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                    break;
            }

            //This is where the monster HP bar is drawn
            if(type == 2 && hpBarOn == true) {
                //If the bar is 48 pixels and hp is 2, the onescale is 24 pixels
                double oneScale = (double)gp.tileSize/maxLife;
                //Onescale*2 = 48, onescale*1 = 24 etc
                double hpBarValue = oneScale*life;
                //Draw the outline first and make it slightly larger
                g2.setColor(new Color(30,30,30));
                g2.fillRect(screenX-1,screenY-16,gp.tileSize+2,12);
                //Draw the smaller HP rectangle over it
                g2.setColor(new Color(255, 30, 30, 255));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }
            if(invincible == true){
                hpBarOn = true;
                //RESET THIS IF PLAYER HITS MONSTER
                hpBarCounter = 0;
                //MAKING THE PLAYER OPAQUE DURING IFRAMES
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            }
            if(dying == true){
                dyingAnimation(g2);
            }
            g2.drawImage(image, screenX ,screenY, null);

            //THIS IS NECESSARY SO NOTHING ELSE IS OPAQUE
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

    }

    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;

        if(dyingCounter % 2 == 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }
        if(dyingCounter % 2 == 1){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
        if(dyingCounter > 40){
            alive = false;
        }
    }
    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;


        try{
            image = ImageIO.read((getClass().getResourceAsStream(imagePath +".png")));
            image = uTool.scaleImage(image, width, height);

        }catch (IOException e){
            e.printStackTrace();
        }

        return image;
    }
}
