package Server;

import Client.Client_Interface;
import Content.Content;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class Server extends UnicastRemoteObject implements Server_Interface{

    private final Map<Integer, String> m1 = new HashMap();
    private final Map<String, Content> m2 = new HashMap();
    private final List<Client_Interface> clients = new ArrayList();
    
    public Server() throws RemoteException, FileNotFoundException, IOException{super(); this.load();}

    /**
     * Remote method
     * Uploads to the server a content and it's description
     * @param description
     * @param content
     * @return int
     * @throws RemoteException
     * @throws IOException 
     */
    @Override
    public int upload(String description, Content content) throws RemoteException ,IOException{  
        Random random = new Random();
        int key = random.nextInt(5000);
        while(m1.containsKey(key))
            {
             key = random.nextInt(5000);
            }
        m1.put(key, description);
        m2.put(description, content);
        System.out.println("New content has been upload: \t" + 
                Integer.toString(key)+ " " + content.toString());
        
        this.update(key, description, content); 
        this.notifyClients(content);
        return key;
    }

    /**
     * Remote method
     * Returns the content that has the same description as the parameter
     * if it exists
     * @param description
     * @return Content
     * @throws RemoteException 
     */
    @Override
    public Content getContent(String description) throws RemoteException {
        try
            {
             return m2.get(description);
            }
        
         catch(Exception ex) 
            {
             return null;  
            }
    }

    /**
     * Remote method
     * Returns the content that has the same id as the parameter
     * if it exists
     * @param id
     * @return Content
     * @throws RemoteException 
     */
    @Override
    public Content getContent(int id) throws RemoteException {  
        try
            {
             return m2.get(m1.get(id));
            }
        
         catch(Exception ex) 
            {
             return null;  
            }
    }

    /**
     * Remote method
     * Returns the description that has the same id as the parameter
     * if it exists
     * @param id
     * @return String
     * @throws RemoteException 
     */
    @Override
    public String getDescription(int id) throws RemoteException {
        try
            {
             return m1.get(id);
            }
        
        catch(Exception ex) 
            {
             return "No element with that id";  
            }
       
    }

    /**
     * Remote method
     * Returns a list of contents that have a similar description of the parameter.
     * @param description
     * @return List<Content>
     * @throws RemoteException 
     */
    @Override
    public List<Content> getSimilar(String description) throws RemoteException {  
        
        String tmp;
        String tmp2;
        List<Content> similar_description = new ArrayList();
        StringTokenizer st = new StringTokenizer(description);
                
        while(st.hasMoreTokens())
            {
              tmp = st.nextToken();
              if(tmp.length() > 3)
                { 
                 Iterator<String> it = m2.keySet().iterator(); 
                 while(it.hasNext())
                    {
                     tmp2 = it.next();
                     if(tmp2.toLowerCase().contains(tmp.toLowerCase()))
                        {
                         similar_description.add(m2.get(tmp2));
                        }
                    }
                }
            }       
        return similar_description;
    }

    /**
     * Gets the client's object for the callback
     * @param name
     * @param port
     * @return boolean
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException 
     */
    @Override
    public boolean subscribe(String name, String port) throws NotBoundException, MalformedURLException, RemoteException
        {
         String ip;
         try {
             ip = java.rmi.server.RemoteServer.getClientHost();
             String url = "rmi://" + ip + ":" + port + "/" + name;
             System.out.println(url);             
             Client_Interface tmp = (Client_Interface)Naming.lookup(url);
             this.clients.add(tmp);
             return true;
            } 
         catch (Exception ex) 
            {
             System.out.println(ex);
             return false;
            }
        }
    
    private void notifyClients(Content content) throws RemoteException
        {
         for(Client_Interface c : this.clients)
            {
             c.getNotified(content.toString());
            }
        }
    
    private void update(int key, String description, Content content) throws IOException
        {
         BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter("bbdd.txt", true));
         bufferedwriter.write(Integer.toString(key));
         bufferedwriter.newLine();
         bufferedwriter.write(description);
         bufferedwriter.newLine();
         bufferedwriter.write(content.toString());
         bufferedwriter.newLine();
         bufferedwriter.close();
        }
    
    private void load() throws FileNotFoundException, IOException
        { 
         String line1;
         String line2;
         String line3;
         String [] line3_splitted;
         
         File bbdd = new File("bbdd.txt");

         if (!bbdd.isFile() && !bbdd.createNewFile())
            {
             System.out.println("Could nou load the database");
            }
         
         BufferedReader bufferedReader = new BufferedReader(new FileReader(bbdd));
         while((line1 = bufferedReader.readLine()) != null) 
            {
             String name = "";   
             line2 = bufferedReader.readLine();  
             line3 = bufferedReader.readLine();
             line3_splitted = line3.split(" ");

             for(int i = 0; i < line3_splitted.length -2; i++)
                {
                 name += line3_splitted[i]+" ";
                }
             
             m1.put(Integer.parseInt(line1), line2);
             m2.put(line2, new Content(name, Integer.parseInt(line3_splitted[line3_splitted.length -2]), 
                     Double.parseDouble(line3_splitted[line3_splitted.length -1])));
            }

        }
}

