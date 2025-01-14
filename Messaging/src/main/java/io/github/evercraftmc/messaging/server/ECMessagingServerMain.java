package io.github.evercraftmc.messaging.server;

import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.ejcl.file.bjsl.StructuredBJSLFileConfig;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ECMessagingServerMain {
    private static class MessagingDetails {
        public String host = "127.0.0.1";
        public int port = 3000;
    }

    public static void main(String @NotNull [] args) {
        Logger logger = LoggerFactory.getLogger("Messaging");

        try {
            logger.info("Loading config...");

            StructuredBJSLFileConfig<MessagingDetails> messagingDetails = new StructuredBJSLFileConfig.Builder<>(MessagingDetails.class, Path.of("messaging.yml").toFile(), new YamlParser.Builder().build()).build();
            messagingDetails.load(true);

            ECMessagingServer server = new ECMessagingServer(logger, new InetSocketAddress(messagingDetails.get().host, messagingDetails.get().port));
            server.start();

            {
                Scanner stdin = new Scanner(System.in);

                while (true) {
                    String read = stdin.nextLine();

                    if (read.equalsIgnoreCase("q") || read.equalsIgnoreCase("quit")) {
                        server.stop();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception in program", e);
        }
    }
}