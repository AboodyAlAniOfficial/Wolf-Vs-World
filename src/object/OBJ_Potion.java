package object;

import Main.GamePanel;
import entity.Entity;

public class OBJ_Potion extends Entity {

    GamePanel gp;


    public OBJ_Potion(GamePanel gp) {
        super(gp);

        value = 5;
        type = type_Consumable;
        name = "Red Pot";
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nHeals by " + value;

    }

    public void use(Entity entity){
        //CANT WORK WHEN CHARACTER IS IN CHARACTER STATE
//        gp.ui.addMessage("Healed by " + val);

        entity.life += value;
        if(entity.life >= entity.maxLife){
            entity.life = entity.maxLife;
        }

    }
}
