package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.client.ClientHandlerInterface;

import java.rmi.Remote;

/**
 * The ClientRMIInterface is used by the server to communicate with the client.
 * This interface is implemented by the client's remote object.
 * It serves as the stub (proxy of the client) in the RMI communication.
 * It extends the Remote interface, which is required for RMI communication, and the ClientHandlerInterface, which defines the methods that the client needs to handle.
 */
public interface ClientRMIInterface extends Remote, ClientHandlerInterface {

}