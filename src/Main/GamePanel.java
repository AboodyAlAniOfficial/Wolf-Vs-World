package Main;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int OriginalTileSize = 16; //16x16 tile
    final int scale = 3;
    public final int tileSize = OriginalTileSize * scale; //48x48 tile
    public int maxScreenCol = 20;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; //768 pixels
    public int screenHeight = tileSize * maxScreenRow; //576 pixels


    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;

    //FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    boolean fullScreenOn = false;

    //UNUSED VARS
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;


    //FPS (30 or 60)
    int FPS = 60;

    //SYSTEM STUFF
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cCheker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[][] = new Entity[maxMap][10];
    public Entity npc[][] = new Entity[maxMap][10];
    public InteractiveTile iTile [][] = new InteractiveTile[maxMap][50];
    public Entity monster[][] = new Entity[maxMap][20];
    //ONE BIG ENTITY LIST
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particlesList = new ArrayList<>();


    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int deathState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void restart(){
        player.setDefaultValues();
        player.inventory.clear();
        player.setItems();
        aSetter.setObject();
        aSetter.setNpc();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
    }

    public void setUpGame(){

        aSetter.setObject();
        aSetter.setNpc();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
//        playMusic(0);
//        stopMusic();
        //This makes it so the default game state is ingame(not paused)
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB_PRE);
        g2 = (Graphics2D)tempScreen.getGraphics();

        //setFullScreen();
    }

    public void setFullScreen(){
        //Get local screen enviroment
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //Get fullscreen height and width
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(gameThread != null){
            //gameloop stuff
            double drawInterval = 1000000000/FPS; // Draws every 0.0166667 Seconds
            double delta = 0;
            long lastTime = System.nanoTime();
            long currrentTime;

            //FPS display
            long timer = 0;
            int drawCount = 0;
            while(gameThread != null) {

                currrentTime = System.nanoTime();
                delta += (currrentTime - lastTime) / drawInterval;
                timer += (currrentTime - lastTime);
                lastTime = currrentTime;

                if(delta > 1){
                    //Two uses of this method
                    //1 Update info: this info such as positions and coords
                    update();
                    //2 Draw the screen: Draw the screen with the updated info

                   repaint();

                   //drawToTempScreen();//This draws everything to the buffered image
                    //drawToScreen();//This draws the buffered image to the screen
                    delta--;
                    drawCount++;
                }

                if(timer>1000000000){
                    System.out.println("FPS: " +drawCount);
                    drawCount = 0;
                    timer = 0;
                }

            }
        }
    }

    public void update(){
        //Update will only run if the game is not paused
        if(gameState == playState) {
            //PLAYER
            player.update();
            //NPC
            for(int i = 0; i < npc[1].length; i++){
                if(npc[currentMap][i] != null){
                    npc[currentMap][i].update();
                }
            }
            for(int i = 0; i < monster[1].length; i++){
                if(monster[currentMap][i] !=null){
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                        monster[currentMap][i].update();
                    }
                    if(monster[currentMap][i].alive == false){
                        monster[currentMap][i] = null;
                    }
                }
            }
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) !=null){
                    if(projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if(projectileList.get(i).alive == false){
                        projectileList.remove(i);
                    }
                }
            }
            for(int i = 0; i < particlesList.size(); i++){
                if(particlesList.get(i) !=null){
                    if(particlesList.get(i).alive == true) {
                        particlesList.get(i).update();
                    }
                    if(particlesList.get(i).alive == false){
                        particlesList.remove(i);
                    }
                }
            }


            for(int i = 0; i < iTile[1].length; i++){
                if(iTile[currentMap][i] != null){
                    iTile[currentMap][i].update();
                }
            }
        }
        if(gameState == pauseState){
            //nothing(for now, subject to change)
        }
        if(gameState == deathState){
            player.deathUpdate();
        }
    }

    public void drawToTempScreen(){

        //TO RE ADD FULL SCREEN
        //Uncomment setFullScreen(); from setup game, and the two methods calls in run()
        // , then comment out repaint from run() and paintcomponent()


        //DEBUG STUFF
        long drawStart = 0;
        if(keyH.showDebugText == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState){
            ui.draw(g2);

        }else{
            //TILE
            tileM.draw(g2);

            for(int i = 0; i < iTile[1].length; i++){
                if(iTile[i] != null){
                    iTile[currentMap][i].draw(g2);
                }
            }

            //ADDING ENTITIES TO THE LIST
            entityList.add(player);

            for(int i = 0; i < npc[1].length;i++){
                if(npc[i] != null){
                    entityList.add(npc[currentMap][i]);
                }
            }
            for(int i = 0; i < obj[1].length;i++){
                if(obj[i] != null){
                    entityList.add(obj[currentMap][i]);
                }
            }
            for(int i = 0; i < monster[1].length;i++){
                if(monster[i] != null){
                    entityList.add(monster[currentMap][i]);
                }
            }
            for(int i = 0; i < projectileList.size();i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }

            for(int i = 0; i < particlesList.size();i++){
                if(particlesList.get(i) != null){
                    entityList.add(particlesList.get(i));
                }
            }

            //SORT by WORLDY
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    int result = Integer.compare(o1.worldY, o2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITYLIST(OTHERWISE IT WILL KEEP GETTING LARGER EACH ITERATION)
            entityList.clear();






            //UI
            ui.draw(g2);

        }

        //DEBUG
        if(keyH.showDebugText == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;
            g2.drawString("WorldX: " + player.worldX,x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY,x, y);
            y += lineHeight;
            g2.drawString("Col " + (player.worldX + player.solidArea.x)/tileSize, x, y);
            y += lineHeight;
            g2.drawString("Row " + (player.worldY + player.solidArea.y)/tileSize, x, y);
            y += lineHeight;
            g2.drawString("Time passed: " + passed,x, y);
            System.out.println("Time passed: " + passed);
        }

    }

    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen,0,0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //DEBUG STUFF
        long drawStart = 0;
        if(keyH.showDebugText == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState){
            ui.draw(g2);

        }else{
            //TILE
            tileM.draw(g2);

            for(int i = 0; i < iTile[1].length; i++){
                if(iTile[currentMap][i] != null){
                    iTile[currentMap][i].draw(g2);
                }
            }

            //ADDING ENTITIES TO THE LIST
            entityList.add(player);

            for(int i = 0; i < npc[1].length;i++){
                if(npc[currentMap][i] != null){
                    entityList.add(npc[currentMap][i]);
                }
            }
            for(int i = 0; i < obj[1].length;i++){
                if(obj[currentMap][i] != null){
                    entityList.add(obj[currentMap][i]);
                }
            }
            for(int i = 0; i < monster[1].length;i++){
                if(monster[currentMap][i] != null){
                    entityList.add(monster[currentMap][i]);
                }
            }
            for(int i = 0; i < projectileList.size();i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }

            for(int i = 0; i < particlesList.size();i++){
                if(particlesList.get(i) != null){
                    entityList.add(particlesList.get(i));
                }
            }

            //SORT by WORLDY
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    int result = Integer.compare(o1.worldY, o2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITYLIST(OTHERWISE IT WILL KEEP GETTING LARGER EACH ITERATION)
            entityList.clear();






            //UI
            ui.draw(g2);

        }

        //DEBUG
        if(keyH.showDebugText == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;
            g2.drawString("WorldX: " + player.worldX,x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY,x, y);
            y += lineHeight;
            g2.drawString("Col " + (player.worldX + player.solidArea.x)/tileSize, x, y);
            y += lineHeight;
            g2.drawString("Row " + (player.worldY + player.solidArea.y)/tileSize, x, y);
            y += lineHeight;
            g2.drawString("Time passed: " + passed,x, y);
            System.out.println("Time passed: " + passed);
        }

        g2.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();


    }

    public void stopMusic(){
        music.stop();
    }

    //SE: SOUND EFFECT
    public void playSE(int i){
        se.setFile(i);
        se.play();

    }
}
