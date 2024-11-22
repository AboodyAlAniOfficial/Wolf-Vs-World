package object;

import Main.GamePanel;
import entity.Entity;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        name = "Normal Sword";
        type = type_Weapon;
        down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 3;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name +"]\nA regular sword";
    }
}
