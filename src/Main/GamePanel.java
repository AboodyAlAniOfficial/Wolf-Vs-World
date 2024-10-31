package Main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.Tile;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int OriginalTileSize = 16; //16x16 tile
    final int scale = 3;
    public final int tileSize = OriginalTileSize * scale; //48x48 tile
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; //768 pixels
    public int screenHeight = tileSize * maxScreenRow; //576 pixels


    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //UNUSED VARS
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;


    //FPS (30 or 60)
    int FPS = 60;

    //SYSTEM STUFF
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cCheker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[10];


    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setUpGame(){

        aSetter.setObject();
        aSetter.setNpc();
        playMusic(0);
        stopMusic();
        //This makes it so the default game state is ingame(not paused)
        gameState = titleState;
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
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
        }
        if(gameState == pauseState){
            //nothing(for now, subject to change)
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //DEBUG STUFF
        long drawStart = 0;
        if(keyH.checkDrawTime == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState){
            ui.draw(g2);

        }else{
            //TILE
            tileM.draw(g2);

            //OBJECT
            for(int i =0; i < obj.length;i++){
                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }
            }

            //NPC
            for(int i = 0; i < npc.length;i++){
                if(npc[i] != null){
                    npc[i].draw(g2);
                }
            }
            //PLAYER
            player.draw(g2);

            //UI
            ui.draw(g2);
            g2.dispose();

        }

        //DEBUG
        if(keyH.checkDrawTime == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Time passed: " + passed,50, 400);
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
