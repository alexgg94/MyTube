package Server;

import Content.Content;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Server_Interface extends Remote{
   
    public boolean subscribe(String name, String port) throws RemoteException, NotBoundException, MalformedURLException;
    public int upload(String description, Content content) throws RemoteException, IOException;
    public Content getContent(String description) throws RemoteException;
    public Content getContent(int id) throws RemoteException;
    public String getDescription(int id) throws RemoteException;
    public List<Content> getSimilar(String description) throws RemoteException;
 
}
