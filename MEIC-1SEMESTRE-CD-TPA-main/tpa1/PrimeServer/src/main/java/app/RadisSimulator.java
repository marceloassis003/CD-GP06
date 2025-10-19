package app;

import java.util.HashMap;

public class RadisSimulator {
    private HashMap<Integer,Integer> radis = new HashMap<>();

    public Integer getPrime(int number){
        return radis.get(number);
    }

    public void setPrime(int number, int prime){
        radis.put(number,prime);
    }
}
