package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client_Interface extends Remote{
    
    public void getNotified(String notification) throws RemoteException;
}
