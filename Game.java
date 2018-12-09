import javafoundations.*;
import java.util.*;
import javafx.util.Pair;// for pair class

/**
 * Implements the details for playing the game, using ChristmasTree
 * and Item class
 *
 * @author Chloe Moon
 * @version 12/08/2018
 */
public class Game {
    // instance variables
    ChristmasTree tree;
    ArrayQueue<Item> dormant;  // stores the dorman Item objects
    Vector<Item> active; // add Item objects from "dormant" to this vector
    int score; 
    boolean isStart; // indicates whether the game has started or not
    // change later *******
    final int screenWidth = 500; final int screenHeight = 700;
    
    /**
     * Constructor for objects of class Game
     */
    public Game(ChristmasTree tree) {
        // tree
        dormant = new ArrayQueue<Item>();
        active = new Vector<Item>();
        isStart = false;
        score = 0;
        this.tree = tree;
    }

    // if it doesn't work maybe put a tree as a parameter
    /**
     * Checks if tree collides with gift or bomb and sets collision var to
     * the respective value (1 if collides with present, 0 if no collision,
     * -1 if collides with bomb)
     * 
     * 
     * @param dropped item, christmas tree
     */
    public int doCollide(Item drop){
        if (active.contains(drop)){
            // get location of the item
            int itemX = drop.getX();
            int itemY = drop.getY();
            // get location of the tree 
            Pair<Integer, Integer> treeloc = tree.getLocation();
            int treeX = treeloc.getKey();
            int treeY = treeloc.getValue();
            // calculate the distance between the two objects
            int treeW = tree.getWidth();
            int treeH = tree.getHeight();
            int itemW = drop.getWidth();
            // int itemH = drop.getHeight();
            // range
            double xRange = (treeW+itemW)/2.0;
            double yRange = (treeH+treeW)/2.0;
            
            // if within a certain range, they collide
            if (Math.abs(itemX-treeX)<=xRange && Math.abs(itemY-treeY)<=yRange){
                if(drop.isGift()){ // if it's a gift
                    // does setting the drop directly makes sense?
                    // change the itemCollided status of the item to true
                    active.get(active.indexOf(drop)).setItemCollided();
                    // update the game-wide indicator
                    score = score+50; // update score
                    active.remove(drop);
                    return 1;
                } else if(!drop.isGift()){ // if it's a bomb
                    // change the itemCollided status of the item to true
                    active.get(active.indexOf(drop)).setItemCollided();
                    score = score-100; // update score
                    active.remove(drop);
                    return -1;
                }
            } else {
                return 0;
            }
        } else { // if active vector (currently dropping) doesn't have this item
            System.out.println("Item not being dropped (not in 'Active')");
        }
        return 0;
    }
    
    
    /**
     * Ends the game if the score reches 1000 or if the score <0.
     */
    public void endGame(){
        if (score == 1000 || score < 0){
            isStart = false;
        }
    }
    
    /**
     * Retrieve Item objects from the dormant queue and adds them to 
     * the active Vector if the size of the active vector is less than 6.
     */
    public void addItem(){
        while (active.size() < 6){
            if (dormant.isEmpty()) prepareItem();
            Item toAdd = dormant.dequeue(); // gets the item from the dormant queue
            active.add(toAdd);
        }
    }
    
    /**
     * Initialize Item objects to be added to the dormant Queue.
     * Using the Random class, the method will create gifts and bombs
     * in a 1:2 ratio.
     */
    public void prepareItem() {
        Item toAdd; 
        Random rand = new Random();
        int n = 1 + rand.nextInt(3); // gives 1+(0~2)
        // int cwidth = tree.getWidth();
        // Random class to generate the x-location of the object
        // y-location: same as the height of the screen
        //int x = rand.nextInt(screenWidth + 1); //0~screenwidth
        int x = 50;
        int y = 30;
        //int y = screenHeight; 
        if (n == 3){ // when 3, add a present to the dormant queue
            toAdd = new Item(x,y,true); // is a gift
        } else { // so if n=1 or 2
            toAdd = new Item(x,y,false); // is a bomb
        }
        dormant.enqueue(toAdd); // adds to the dormant queue
    }
    

    public int getScore(){
        return score;
    }

    public Vector<Item> getActive() {
        return this.active;
    }

    public void drop() {
        addItem();
        System.out.println("Changing the location of the items.");
        if (this.isStart) {
            for (Item item : active) item.setY(item.getY() + 5);
        }

        // do collide
    }

    public void start() {
        this.isStart = true;
        drop();
    }
    
}