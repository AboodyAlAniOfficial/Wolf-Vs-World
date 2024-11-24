package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {
    GamePanel gp;

    public boolean upPressed, leftPressed, downPressed, rightPressed, enterPressed, shotPressed;
    //DEBUG
    boolean showDebugText = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLESTATE CONTROLS
        if(gp.gameState == gp.titleState){
            titleState(code);
        }

        //PLAYSTATE CONTROLS
        else if (gp.gameState == gp.playState) {
            playState(code);
        }

        //PAUSE STATE CONTROLS
     //This will unpause
    else if(gp.gameState ==gp.pauseState){
        pauseState(code);
    }
    //DIALOGUE STATE CONTROLS
    else if(gp.gameState == gp.dialogueState){
        dialogueState(code);
        }
    else if(gp.gameState == gp.characterState){
        characterState(code);
        }
    else if(gp.gameState == gp.optionsState){
            optionsState(code);
        }
    else if(gp.gameState == gp.deathState){
        deathState(code);
        }

    }

    public void titleState(int code){
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2){
                //This makes it so the cursor won't disappear, it will just loop back into the top
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                //This will start the game
                gp.restart();
                gp.gameState = gp.playState;
                gp.playMusic(0);

            }
            if(gp.ui.commandNum == 1){
                //Add load functionality later
            }
            if(gp.ui.commandNum == 2){
                System.exit(0);
            }

        }
    }

    public void playState(int code){

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        //USING P to pause, may change to ESC
        if (code == KeyEvent.VK_P) {
            //This will pause
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }
        if (code == KeyEvent.VK_ENTER) {
            //Added this is in so dialogue won't open accidentally
            enterPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            shotPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
        }
        //DEBUG
        if (code == KeyEvent.VK_T) {
            if (showDebugText == false) {
                showDebugText = true;
            } else if (showDebugText == true) {
                showDebugText = false;
            }
        }
        if(code == KeyEvent.VK_U){
            switch (gp.currentMap){
                case 0:gp.tileM.loadMap("/maps/worldV2.txt",0); break;
            }

        }
    }
    public void pauseState(int code){
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W){
            if(gp.ui.slotRow !=0){
                gp.ui.slotRow--;
            }
        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.slotCol !=0){
                gp.ui.slotCol--;
            }

        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.slotRow !=3){
                gp.ui.slotRow++;
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.slotCol !=4){
                gp.ui.slotCol++;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }

    public void optionsState(int code){

        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        int maxCommandNum = 0;
        switch (gp.ui.subState){
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > maxCommandNum){
                gp.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_A){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(1);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.playSE(1);
                }
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(1);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.playSE(1);
                }
            }
        }


    }
    public void deathState(int e){

    }

    public void showDebugText(){

    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            shotPressed = false;
        }
    }
}
