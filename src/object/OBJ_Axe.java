package object;

import Main.GamePanel;
import entity.Entity;

public class OBJ_Axe extends Entity {
    public OBJ_Axe(GamePanel gp) {
        super(gp);

        name = "Woodcutter's Axe";
        type = type_Weapon;

        //CHANGE THE IMAGE ONCE AN AXE IS MADE
        attackValue= 3;
        attackArea.width = 24;
        attackArea.height = 24;
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
        description = "[" + name+"]\nFor cutting lumber";
        value = 10;
    }
}
