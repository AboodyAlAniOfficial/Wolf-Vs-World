package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;

public class OBJ_Chest extends Entity {
    public OBJ_Chest(GamePanel gp){
        super(gp);
        name = "Chest";
        down1 = setup("/objects/chest.png", gp.tileSize, gp.tileSize);

}

}
