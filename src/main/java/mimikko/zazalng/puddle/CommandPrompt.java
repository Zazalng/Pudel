/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mimikko.zazalng.puddle;

import java.util.Scanner;

/**
 *
 * @author User
 */
public class CommandPrompt extends Thread{
    private final PuddleWorld puddleWorld;
    private final Scanner prompt;
    private String inputer;
    
    public CommandPrompt(PuddleWorld puddleWorld){
        this.puddleWorld = puddleWorld;
        this.prompt = new Scanner(System.in);
        this.inputer = "";
    }
    
    public PuddleWorld getPuddleWorld(){
        return this.puddleWorld;
    }
}
