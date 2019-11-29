package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class multiThreadSample {

    private static Logger logger = LoggerFactory.getLogger(multiThreadSample.class);

    public static void main(String[] args) {
        logger.info("prepare threads");
        int threadNumber = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        logger.info("add tasks");
        List<Callable<Void>> tasks = new ArrayList<>();
        for(int i = 1; i <= threadNumber; i++){
            tasks.add(new Task(i*2));
        }

        logger.info("execute tasks");
        try {
            // wait all tasks are done
            executor.invokeAll(tasks);
        } catch (InterruptedException exp){
            logger.error("invodeAll error", exp);
        }

        System.exit(1);
    }

    private static class Task implements Callable<Void> {

        private int num;

        Task(int num){
            this.num = num;
        }

        @Override
        public Void call() throws Exception {
            logger.info("start");

            logger.info("sleep");
            Thread.sleep(num * 1000);

            logger.info("end");
            return null;
        }
    }
}
