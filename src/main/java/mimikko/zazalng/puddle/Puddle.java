/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mimikko.zazalng.puddle;

/**
 *
 * @author User
 */

public class Puddle{
    public static void main(String[] args) throws Exception {
        PuddleWorld PrideofEden = new PuddleWorld();
        PrideofEden.PuddleLog("Getting World's Environment");
        PrideofEden.setPuddleWorldEnvironment("../.env");
        PrideofEden.startPuddleWorld();
    }
}