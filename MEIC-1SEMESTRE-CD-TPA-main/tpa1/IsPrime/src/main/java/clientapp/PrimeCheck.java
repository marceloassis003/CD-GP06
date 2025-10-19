package clientapp;

import redis.clients.jedis.Jedis;
import java.util.Random;

public class PrimeCheck {
    public static void main(String[] args) {
        long numero = Long.parseLong(args[0]);
        String redisAddress = args[1];
        int redisport=Integer.parseInt(args[2]);
        Jedis jedis = new Jedis(redisAddress, redisport);
        if (isPrime(numero))
            jedis.set(numero+"","true");
        else
            jedis.set(numero+"","false");
        System.out.println(jedis.get(numero+""));
    }

    public  static  boolean isPrime(long number) {
        if (number <= 1L) return false;
        if (number == 2L || number == 3L) return true;
        if (number % 2L == 0) return false;
        simulateExecutionTime();
        for (double i=3; i <= Math.sqrt(number); i+=2) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public static void simulateExecutionTime() {
        try {
            Thread.sleep(new Random().nextInt(1000) + 200);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}