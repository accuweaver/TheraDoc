/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hospira.theradoc;

import static java.lang.Thread.currentThread;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newCachedThreadPool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example code from http://bmwieczorek.wordpress.com/2010/09/23/java-concurrency-executors-newcachedthreadpool/
 * 
 * Modified slightly to simulate the server working slowly and increasing the 
 * number of running threads ...
 * 
 * @author robweaver
 */
public class CachedThreadPoolExample {
    /** 
     * Logger for this class ...
     */
    private static final Logger logger = Logger.getLogger(CachedThreadPoolExample.class.getName());

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
    ExecutorService es = newCachedThreadPool();
  
    Runnable task = new Runnable() {

      @Override
      public void run() {
        logger.info(currentThread().getName());
        // Make server act as if it's busy ...
        sleep(100);
      }
    };

    logger.info("Requests with pause in between - match to server");
    for (int i = 0; i < 20; i++) {
      es.execute(task);
      // Delay between requests ..
      sleep(100); 
    }
    
    sleep(500);
    logger.info("Requests with no pause in between - busy server");
    for (int i = 0; i < 20; i++) {
      es.execute(task);
    }
    es.shutdown();
  }

  /**
   * Method to cause the thread to sleep for a specified period ...
   * 
   * @param value 
   */
  private static void sleep(int value) {
    try {
      Thread.sleep(value);
    } catch (InterruptedException e) {
      logger.log(Level.SEVERE, "Problem with sleeping", e);
    }
  }
}