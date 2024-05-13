package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.client.AbstractClientHandler;

import java.rmi.Remote;

//used from the server to communicate with the client
//this interface is implemented by the client remote object
//this is the stub (proxy of the client)
public interface ClientRMIInterface extends Remote, AbstractClientHandler {

}
