package entity;

import Main.GamePanel;
import object.OBJ_Axe;
import object.OBJ_Key;
import object.OBJ_Potion;

public class NPC_Merchant extends Entity{

    int x;
    public NPC_Merchant(GamePanel gp){
        //Once again, this is so the Entity class can recieve gp
        super(gp);

        direction = "down";
        speed = 1;
        getImage();
        setDialogue();
        setItems();
    }
    public void getImage(){
        up1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);

    }



    /////hello

    public void setDialogue(){
        dialogues[0] = "Trade?";

    }

    public void setItems(){
        inventory.add(new OBJ_Potion(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Key( gp));

    }

    public void speak(){
        super.speak();
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }
}
