package Main;




public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][][];

    int previousEventX, previousEventY;
    boolean cantTouchEvent = true;

    //Transition Stuff
    int tempMap, tempCol, tempRow;

    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while(map < gp.maxMap &&col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
                if(row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
            }
        }

    }

    public void checkEvent(){

        //Check if the player moved 1 tile since the last event
        int xDistance = Math.abs(gp.player.worldX -previousEventX);
        int yDistance = Math.abs(gp.player.worldY -previousEventY);
        //If the player moves a tile, then they can set off the same event
        int distance = Math.max(xDistance,yDistance);
        if(distance >gp.tileSize){
            cantTouchEvent = true;
        }

        if(cantTouchEvent == true){
            if(hit(0,27,16,"right") == true){
                damagePit( gp.dialogueState);
            }
            else if(hit(0,23,12, "up") == true){
                healingPool( gp.dialogueState);
            }
            else if(hit(0,26,12,"right") == true){
                teleport(1, 10,10);
            }
        }


    }

    public boolean hit(int map, int col, int row, String reqDirection){
        boolean hit = false;

        if(map == gp.currentMap){


            //Getting player's solid area position
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            //Getting event's solid area position
            eventRect[map][col][row].x = col*gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row*gp.tileSize + eventRect[map][col][row].y;

            if(gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false){
                if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            //After checking the collision reset the solidArea x and y
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

        }

        return hit;
    }

    public void damagePit(int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a pit";
        gp.player.life -= 1;
        //eventRect[col][row].eventDone = true;
        cantTouchEvent = false;

    }

    public void healingPool(int gameState){
        if(gp.keyH.enterPressed == true){
            gp.player.attackCancelled = true;

            gp.gameState = gameState;
            gp.ui.currentDialogue = "You drank the water\nYou've been healed";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.aSetter.setMonster();

        }

    }

    public void teleport(int map, int col, int row){

        gp.gameState = gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;

//      gp.currentMap = map;
//        gp.player.worldX = gp.tileSize*col;
//        gp.player.worldY = gp.tileSize*row;
//        previousEventX = gp.player.worldX;
//        previousEventY = gp.player.worldY;
        cantTouchEvent = false;
    }
}
