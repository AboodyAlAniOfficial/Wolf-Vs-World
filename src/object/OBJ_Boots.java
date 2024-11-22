package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel gp){
        super(gp);
        down1 = setup("/objects/boots.png", gp.tileSize, gp.tileSize);
        name = "Boots";

    }
}
