// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String login;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String login, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.login = login;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      String[] m = message.split(" ");
      switch(m[0]) {

        /* Provoque l'arrêt normal du client. Assure que la connexion 
         au serveur est terminée avant de quitter le programme.*/
        case "#quit":
          quit();
          break;

        /* Provoque la déconnexion du client du serveur, mais pas la fermeture. */
        case "#logoff":
          clientUI.display("Logging off.");
          closeConnection();
          break;

        /* Appelle la méthode setHost dans le client. Cette commande est autorisée
        uniquement si le client est déconnecté; sinon, un message d'erreur s’affiche. */
        case "#sethost":
          if (isConnected() == false) {
            try {
              setHost(m[1]);
              clientUI.display("Host set to " + m[1]);
            } catch (NumberFormatException e) {
              clientUI.display("Could not set host.");
            }
          }
          break;

        /*Appelle la méthode setPort dans le client, avec les mêmes contraintes
        que #sethost. */
        case "#setport":
          if (isConnected() == false) {
            try {
              setPort(Integer.parseInt(m[1]));
              clientUI.display("Port set to " + m[1]);
            } catch (NumberFormatException e) {
              clientUI.display("Could not set port.");
            }
          }
          break;

        /*Force le client à se connecter au serveur. Cette commande est autorisée
        uniquement si le client n'est pas déjà connecté; sinon, un message d'erreur s’affiche. */
        case "#login":
          try {
            clientUI.display("Logging in.");
            openConnection();
            sendToServer(m[0] + " " + m[1]);
          } catch (Exception e) {
            clientUI.display("ERROR -  No login ID specified.");
          }
          break;

        
        /*Affiche le nom d'hôte actuel */
        case "#gethost":
          clientUI.display(getHost());
          break;

        /*Affiche le numéro de port actuel. */
        case "#getport":
          clientUI.display(Integer.toString(getPort()));
          break;
        default:
          sendToServer(message);
          break;
      }
      
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  protected void connectionClosed() {
    clientUI.display("Connection closed!");
    }
    
  protected void connectionException(Exception exception) {
    clientUI.display("Abnormal termination of connection.");
    System.exit(0);
    }
    
  protected void connectionEstablished() {
    clientUI.display("Connection established!");
    }  
}
//End of ChatClient class
