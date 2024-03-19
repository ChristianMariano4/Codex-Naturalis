package it.polimi.ingsw;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.model.Player;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        System.out.println( "Vediamo" );
        System.out.println( "FUNZIONA" );

        Player test = new Player("G", Marker.RED);
        System.out.println(test.getUserName());
        System.out.println(test.getResourceAmount(Resource.ANIMAL));
    }
}
