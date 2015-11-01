package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Creates a registry on a specified port, rebinds the registry and creates 
 * a new Server process
 * @throws RemoteException 
 * @throws FileNotFoundException
 * @throws IOException 
 */
public class ServerApp {
    public static void main(String[] args) throws RemoteException, FileNotFoundException, IOException
    {
     if(System.getSecurityManager() == null)
        {
         try
            {
             System.setSecurityManager(new RMISecurityManager());
            }
         catch (Exception ex)
            {
             System.out.println(ex);
             System.exit(0);
            }
        }   
     System.setProperty("java.rmi.server.hostname", "localhost");    
     Registry registry = LocateRegistry.createRegistry(5098); 
     System.out.println("Server has started successfully");
     registry.rebind("mytube", new Server());
    } 
}

