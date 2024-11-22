package Main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.*;
import tile_interactive.IT_Tree;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        /*
        OLD CODE BUT I WORKED HARD ON IT SO IT STAYS

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 23* gp.tileSize;
        gp.obj[0].worldY = 7* gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 40 * gp.tileSize;

        gp.obj[2] = new OBJ_Key(gp);
        gp.obj[2].worldX = 37 * gp.tileSize;
        gp.obj[2].worldY = 7 * gp.tileSize;

        gp.obj[3] = new OBJ_Door(gp);
        gp.obj[3].worldX = 10 * gp.tileSize;
        gp.obj[3].worldY = 9 * gp.tileSize;



        gp.obj[6] = new OBJ_Chest(gp);
        gp.obj[6].worldX = 10 * gp.tileSize;
        gp.obj[6].worldY = 7 * gp.tileSize;

        gp.obj[7] = new OBJ_Boots(gp);
        gp.obj[7].worldX = 37 * gp.tileSize;
        gp.obj[7].worldY = 42 * gp.tileSize;
         */

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 25* gp.tileSize;
        gp.obj[0].worldY = 23* gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 21 * gp.tileSize;
        gp.obj[1].worldY = 19 * gp.tileSize;

        gp.obj[2] = new OBJ_Axe(gp);
        gp.obj[2].worldX = 33 * gp.tileSize;
        gp.obj[2].worldY = 21 * gp.tileSize;

        gp.obj[3] = new OBJ_Potion(gp);
        gp.obj[3].worldX = 22 * gp.tileSize;
        gp.obj[3].worldY = 27 * gp.tileSize;



    }

    public void setNpc(){
        //This will place the NPC on the map
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }

    public void setMonster(){
        gp.monster[0] = new MON_GreenSlime(gp);
        gp.monster[0].worldX = gp.tileSize* 23;
        gp.monster[0].worldY = gp.tileSize* 36;


        gp.monster[1] = new MON_GreenSlime(gp);
        gp.monster[1].worldX = gp.tileSize* 24;
        gp.monster[1].worldY = gp.tileSize* 36;

        gp.monster[2] = new MON_GreenSlime(gp);
        gp.monster[2].worldX = gp.tileSize* 23;
        gp.monster[2].worldY = gp.tileSize* 37;

    }

    public void setInteractiveTile(){
        int i = 0;

        gp.iTile[i] = new IT_Tree(gp,27,12);
        i++;
        gp.iTile[i] = new IT_Tree(gp,28, 12);
        i++;
        gp.iTile[i] = new IT_Tree(gp,29, 12);
        i++;
        gp.iTile[i] = new IT_Tree(gp,30,12);
        i++;
        gp.iTile[i] = new IT_Tree(gp,31,12);
        i++;
        gp.iTile[i] = new IT_Tree(gp,32,12);
        i++;
        gp.iTile[i] = new IT_Tree(gp,33,12);

    }
}
