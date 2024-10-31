package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class OBJ_Heart extends SuperObject{
    GamePanel gp;

    public OBJ_Heart(GamePanel gp){
        name = "Heart";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
