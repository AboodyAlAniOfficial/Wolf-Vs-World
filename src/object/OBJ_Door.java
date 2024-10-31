package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class OBJ_Door extends SuperObject {
    GamePanel gp;

    public OBJ_Door(GamePanel gp){
        name = "Door";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (Exception e) {
            e.printStackTrace();
        }

        collision = true;
    }
}