package Content;

import java.io.Serializable;

public class Content implements Serializable{
    
    private final String name;
    private final int year;
    private final double duration;
    
    public Content(String name, int year, double duration)
        {
         this.name=name;
         this.year=year;
         this.duration=duration;
        }
    
    /**
     * Returns class' variables merged in a String
     * @return String
     */
    
    @Override
    public String toString()
        {
         return this.name + " " + Integer.toString(this.year)+ " " + Double.toString(this.duration);
        }
}
