package ai;

import Main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp){
        this.gp = gp;
        instantiateNode();
    }

    public void instantiateNode(){
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col< gp.maxWorldCol && row < gp.maxWorldRow){
            node[col][row] = new Node(col, row);

            col++;
            if(col == gp.maxWorldCol){
                row++;
                col = 0;
            }
        }
    }

    public void resetNodes(){
        int col = 0;
        int row = 0;

        while (col< gp.maxWorldCol && row < gp.maxWorldRow){
            node[col][row].open = false;
            node[col][row].solid = false;
            node[col][row].checked = false;


            col++;
            if(col == gp.maxWorldCol){
                row++;
                col = 0;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step =0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow){
        resetNodes();

        //Setting start/goal nodes
        startNode = node[startCol][startRow];
        currNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currNode);

        int col = 0;
        int row = 0;

        while (col< gp.maxWorldCol && row < gp.maxWorldRow){
            //setting solid tiles/nodes
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if(gp.tileM.tile[tileNum].collision == true){
                node[col][row].solid = true;
            }

            //Checking interactive tiles
            for(int i = 0; i < gp.iTile[1].length;i++){
                if(gp.iTile[gp.currentMap][i].destructible == true && gp.iTile[gp.currentMap][i] != null){
                    int iTileCol = gp.iTile[gp.currentMap][i].worldX/ gp.tileSize;
                    int iTileRow = gp.iTile[gp.currentMap][i].worldY/ gp.tileSize;
                    node[iTileCol][iTileRow].solid = true;
                }
            }
            getCost(node[col][row]);

            col++;
            if(col == gp.maxWorldCol){
                row++;
                col = 0;
            }
        }

    }

    public void getCost(Node node){
        //G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        //H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        //F cost
        node.fCost = node.hCost + node.gCost;
    }

    public boolean search(){

        while(goalReached == false && step < 500){
            int col = currNode.col;
            int row = currNode.row;

            //check the node
            currNode.checked = true;
            openList.remove(currNode);

            //open the nodes
            //up
            if(row - 1 >= 0){
                openNode(node[col][row-1]);
            }
            //left
            if(col -1 >= 0){
                openNode(node[col-1][row]);
            }
            //down
            if(row+1 <gp.maxWorldRow){
                openNode(node[col][row+1]);
            }
            //right
            if(col+1<gp.maxWorldCol){
                openNode(node[col+1][row]);
            }

            //Find best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++){

                //This checks if the open Node's F cost if better
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                else if(openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost == openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }

                if(openList.size() == 0){
                    break;
                }

                //After the loop ends, openlist[bestnodeindex] becomes currentNode
                currNode = openList.get(bestNodeIndex);

                if(currNode == goalNode){
                    goalReached = true;
                    trackThePath();
                }
                step++;
            }

        }
        return goalReached;
    }

    public void openNode(Node node){
        if(node.checked == false && node.open == false && node.solid == false){
            node.open = true;
            node.parent = currNode;
            openList.add(node);
        }
    }

    public void trackThePath(){
        Node current = goalNode;

        while (current != startNode){
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
