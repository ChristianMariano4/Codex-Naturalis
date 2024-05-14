//package it.polimi.ingsw.network.server;
//
//import it.polimi.ingsw.network.observer.EventManager;
//import it.polimi.ingsw.network.observer.Listener;
//import it.polimi.ingsw.network.messages.GameEvent;
//
//import java.util.List;
//
//public class ServerEventManager extends EventManager {
//    public <ErrorType extends Enum<ErrorType>> void sendToClients(GameEvent event) {
//        List<Listener<?>> eventListeners = listeners.get(GameEvent.class);
//        if(eventListeners != null) {
//            for(Listener<?> listener : eventListeners) {
//                GameListenerSocket gameListenerSocket = (GameListenerSocket) listener;
//                gameListenerSocket.update(event);
//            }
//        }
//    }
//}
