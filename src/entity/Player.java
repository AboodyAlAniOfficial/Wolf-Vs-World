package entity;

import Main.*;
import object.OBJ_Axe;
import object.OBJ_Key;
import object.OBJ_Shuriken;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{

    KeyHandler keyH;

    public final  int screenX;
    public final int screenY;
    public boolean attackCancelled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;


//    public int hasKey = 0;
    public Player(GamePanel gp, KeyHandler keyH){

        //If we don't pass gp here, the Entity class cannot recieve gp
        //This is because Entity is abstract(Will never get instantiated)
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize /2);
        screenY = gp.screenHeight / 2 - (gp.tileSize /2);

    //        //Temp testing stats
    //        attackArea.width = 36;
    //        attackArea.height = 36;
        //collision hitbox dimension
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.height = 32;
        solidArea.width = 32;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void getPlayerImage(){
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage(){
        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
        attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
    }

    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(new OBJ_Key(gp));

    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
//        worldX = gp.tileSize * 10;
//        worldY = gp.tileSize * 13;
        speed = 5;
        direction = "down";

        //Player Stats
        maxLife = 2;
        life = maxLife;
        level = 1;
        exp = 0;
        nextLvlExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Axe(gp);
        projectile = new OBJ_Shuriken(gp);
        att = getAttack();//The total attack will be level * weapon dmg


        //TEMP?
        maxMana = 5;
        mana = maxMana;
    }

    public int getAttack(){
        attackArea = currentWeapon.attackArea;
        return level * currentWeapon.attackValue;
    }

    public void update() {
        if (attacking == true) {
            attacking();

        } else if (keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {

            if (keyH.upPressed == true) {
                direction = "up";
            } else if (keyH.downPressed == true) {
                direction = "down";
            } else if (keyH.leftPressed == true) {
                direction = "left";
            } else if (keyH.rightPressed == true) {
                direction = "right";
            }
            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cCheker.checkTile(this);
            //CHECK OBJECT COLLISION
            int objIndex = gp.cCheker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cCheker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cCheker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK ITILE COLLISION
            int iTileIndex = gp.cCheker.checkEntity(this, gp.iTile);


            //CHECK EVENTS
            gp.eHandler.checkEvent();

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false && keyH.enterPressed == false) {
                switch (direction) {
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

            if (keyH.enterPressed == true && attackCancelled == false) {
                //gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }
            attackCancelled = false;

            gp.keyH.enterPressed = false;
            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        if (gp.keyH.shotPressed == true && projectile.alive == false
                && shotCounter == 30 && projectile.hasResource(this) == true) {


            //SET DEFAULT COORDS and VALUES
            projectile.set(worldX, worldY, direction, true, this);

            //SUBTRACT THE RESOURCES FOR FIRING THE PROJECTILE
            projectile.subtractResource(this);
            //ADD IT TO ARRAYLIST
            gp.projectileList.add(projectile);
            shotCounter = 0;
        }

        //i frames
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        //Adds frames for the shot cooldown
        if (shotCounter < 30) {
            shotCounter++;
        }

        if (life <= 0) {
                gp.gameState = gp.deathState;
        }
    }
    public void deathUpdate(){
        if(deathScreenCounter < 180){
            gp.ui.drawDeathScreen();
            deathScreenCounter++;
        }
        if(deathScreenCounter >= 180){
            life = maxLife;
            mana = maxMana;
            invincible = false;
            gp.gameState = gp.playState;
            deathScreenCounter = 0;
            worldX = gp.tileSize * 23;
            worldY = gp.tileSize * 12;
            gp.aSetter.setMonster();

        }
    }
    public void attacking(){
        spriteCounter++;

        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            //HIT DETECTION FOR ATTACKS
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //ADJUST POSITION TO ACCOUNT FOR THE SWORD
            switch (direction){
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            //Attack area becomes the solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //Check collision with the monster list
            int monsterIndex = gp.cCheker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, att);

            //Check collision with iTile list
            int iTileIndex = gp.cCheker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);


            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i){
        if(i != 999) {

            if(inventory.size() != maxInventorySize){
                inventory.add(gp.obj[gp.currentMap][i]);
                gp.ui.addMessage("Picked up " + gp.obj[gp.currentMap][i].name);
                gp.obj[gp.currentMap][i] = null;
            }


        }

    }

    public void interactNPC(int i){
        if(gp.keyH.enterPressed){
            if(i != 999){
                attackCancelled = true;
                //Dialogue will only open if you hit enter and collide with the NPC
                    gp.gameState = gp.dialogueState;
                    gp.npc[gp.currentMap][i].speak();
            }
        }

    }

    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false && gp.monster[gp.currentMap][i].dying == false) {
//                gp.playSE(6);
                int damage = gp.monster[gp.currentMap][i].att - def;
                if(damage < 0){
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }

    }

    public void damageMonster(int i, int attack) {
        if (i != 999) {
            if (gp.monster[gp.currentMap][i].invincible == false) {
                //THIS MIGHT AFFECT PERFORMANCE
 //               gp.playSE(5);
                int damage = att - gp.monster[gp.currentMap][i].def;
                if(damage < 0){
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage +" damage!");
                gp.monster[gp.currentMap][i].invincible = true;
                //THIS MIGHT AFFECT PERFORMANCE
                //gp.monster[i].damageReaction();

                if(gp.monster[gp.currentMap][i].life <= 0){
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage(damage +" +exp!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                    coin += gp.monster[gp.currentMap][i].coin;
                    gp.ui.addMessage("+" + gp.monster[gp.currentMap][i].coin + " coin");
                }
            }
        }
    }

    public void damageInteractiveTile(int i){
        if(i != 999 && gp.iTile[gp.currentMap][i].destructible == true &&
                gp.iTile[gp.currentMap][i].isCorrectItem(this) == true
                && gp.iTile[gp.currentMap][i].invincible == false){
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            if(gp.iTile[gp.currentMap][i].life == 0){
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }


    public void checkLevelUp(){
        if(exp >= nextLvlExp){
            exp -= nextLvlExp;
            nextLvlExp += 5;
            maxLife += 2;
            level++;
            getAttack();
        }
    }

    public void selectItem(){
        int i = gp.ui.getItemIndexOnSlot();

        if(i < inventory.size()){
            Entity selected = inventory.get(i);

            if(selected.type == type_Weapon){
                currentWeapon = selected;
                att = getAttack();
            }
            if(selected.type == type_Consumable){
                selected.use(this);
                inventory.remove(i);
            }
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;

        //To offset the attack animations so they look better
        int tempScreenX = screenX;
        int tempScreenY = screenY;


        switch (direction) {
            case "up":
                if(attacking == false) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                if(attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    }
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if(attacking == false){
                    if(spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2){
                        image = down2;
                    }
                }
                if(attacking == true){
                    if(spriteNum == 1) {
                        image = attackDown1;
                    }
                    if (spriteNum == 2){
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if (attacking == false) {
                    if(spriteNum == 1) {
                        image = left1;
                    }
                    if(spriteNum == 2) {
                        image = left2;
                    }
                }
                if(attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if(spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if(attacking == false){
                    if(spriteNum == 1) {
                        image = right1;
                    }
                    if(spriteNum == 2) {
                        image = right2;
                    }
                }
                if(attacking == true){
                    if(spriteNum == 1) {
                        image = attackRight1;
                    }
                    if(spriteNum == 2){
                        image = attackRight2;
                    }
                    break;
                }
        }

        if(invincible == true){
            //MAKING THE PLAYER OPAQUE DURING IFRAMES
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        //THIS IS NECESSARY SO EVERYTHING ELSE IS OPAQUE
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));


    }
}
