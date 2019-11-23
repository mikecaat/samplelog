package org.example;

import org.apache.ignite.*;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sampleLog {
    public static void main(String[] args) throws Exception {
        Logger logger = LoggerFactory.getLogger(sampleLog.class);

        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");

        Ignite igniteS = createIgnite(false);
        logger.info("server is started(" + igniteS.cluster().localNode().id());

        Ignite ignite = createIgnite(true);
        logger.info("client is started(" + ignite.cluster().localNode().id());

        String cacheName = "mycache";
        int num = 10000;
        try (IgniteCache<Integer, String> cache = ignite.getOrCreateCache(cacheName)) {
            try (IgniteDataStreamer<Integer, String> stmr = ignite.dataStreamer(cacheName)) {
                stmr.perNodeParallelOperations(1);
                stmr.perNodeBufferSize(4096);
                for (int i = 0; i < num; i++) {
                    stmr.addData(1, Integer.toString(i));
                }
            }

            logger.info("key size:" + cache.size());
        }

        System.exit(0);
    }

    private static Ignite createIgnite(boolean isClient) {
        IgniteConfiguration cfg = new IgniteConfiguration();

        cfg.setClientMode(isClient);
        cfg.setIgniteInstanceName("IsClient" + isClient);

        IgniteLogger log = new Slf4jLogger();
        cfg.setGridLogger(log);

        Ignite ignite = Ignition.getOrStart(cfg);
        return ignite;
    }
}
