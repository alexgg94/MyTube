package Client;

import Content.Content;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import Server.Server_Interface;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;


public class Client extends UnicastRemoteObject implements Client_Interface{

    protected Client() throws RemoteException, NotBoundException, MalformedURLException ,IOException
        {
         super();
        }

    /**
     * Gets the server's object and asks the client about which operation he wants
     * to use each time
     * @param user_name
     * @param user_port
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws IOException 
     */
    protected void run(String user_name , String user_port) throws NotBoundException, MalformedURLException, RemoteException,IOException
    {
        try
        {
         Server_Interface mt = (Server_Interface)Naming.lookup("rmi://localhost:5098/mytube");
        
         if(!mt.subscribe(user_name, user_port))
            {
             System.out.println("Could not subscribe");
             System.exit(0);
            }

         String input;
         Scanner scanIn = new Scanner(System.in);  
         while(true)
            {
              System.out.println("What would you like to do?");
              System.out.println("1 - Upload Content");
              System.out.println("2 - Get content by description");
              System.out.println("3 - Get content by id");
              System.out.println("4 - Get Description");
              System.out.println("5 - Get contents with similar description");
              System.out.println("6 - Exit");

              input = scanIn.nextLine();
              
              if(input.equals("1"))
                {
                 System.out.println("Describe the content");
                 String description = scanIn.nextLine();
                 System.out.println("Content's name");
                 String name = scanIn.nextLine();
                 System.out.println("Content's released year");
                 int year;
                 while(true)
                    {
                     try
                        {
                         year = Integer.parseInt(scanIn.nextLine());
                         break;
                        }
                     catch(Exception Ex)
                        {
                            System.out.println("Year format not correct. Try again please");
                        }
                    }
                 System.out.println("Content's duration");
                 double duration;
                 while(true)
                    {
                     try
                        {
                         duration = Double.parseDouble(scanIn.nextLine());
                         break;
                        }
                     catch(Exception Ex)
                        {
                            System.out.println("Duration format not correct. Try again please");
                        }
                    }
                 
                 System.out.println("Content uploaded successfully. The id is: "
                         + mt.upload(description, new Content(name, year, duration)));
                }
              
              else if(input.equals("2"))
                {
                 System.out.println("Introduce the description of the content");
                 String description = scanIn.nextLine();
                 
                 Content c = mt.getContent(description);
                 
                 if(c != null)
                 System.out.println(c.toString());
                 
                 else
                 System.out.println("Could not find such description");   
                }
              
              else if(input.equals("3"))
                {
                 System.out.println("Introduce the id of the content");
                 int id;
                 while(true)
                    {
                     try
                        {
                         id = Integer.parseInt(scanIn.nextLine());
                         break;
                        }
                     catch(Exception Ex)
                        {
                            System.out.println("Id format not correct. Try again please");
                        }
                    }
                 
                 Content c = mt.getContent(id);
                 
                 if(c != null)
                 System.out.println(c.toString());
                 
                 else
                 System.out.println("Could not find such id");
                }
              
              else if(input.equals("4"))
                {
                 System.out.println("Introduce the id of the content");
                 int id;
                 while(true)
                    {
                     try
                        {
                         id = Integer.parseInt(scanIn.nextLine());
                         break;
                        }
                     catch(Exception Ex)
                        {
                            System.out.println("Id format not correct. Try again please");
                        }
                    }
                 String result = mt.getDescription(id);
                 
                 if(result != null)
                 System.out.println(result);
                 
                 else
                 System.out.println("Could not find such id");
                 
                }
              
              else if(input.equals("5"))
                {
                 System.out.println("Introduce the description of the content");
                 String description = scanIn.nextLine();
                 
                 List<Content> l = mt.getSimilar(description);
                 
                 if(l.isEmpty())
                    {
                     System.out.println("No content with similar description");
                    }
                 
                 else
                 for(Content c : l)
                    {
                     System.out.println(c.toString());
                    }
                }
              
              else if(input.equals("6")){scanIn.close(); System.exit(0);}
              
              else{
                  System.out.println("\nThe Option is not correct.\n");
              }
              
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            System.exit(0);
        }
    }

    /**
     * Remote method
     * Prints the notification received from the server
     * @param notification
     * @throws RemoteException 
     */
    @Override
    public void getNotified(String notification) throws RemoteException {
        System.out.println("\nA new content has been ulpoad:\t " + notification);
    }

}