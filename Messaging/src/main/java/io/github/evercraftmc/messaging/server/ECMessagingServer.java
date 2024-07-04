package io.github.evercraftmc.messaging.server;

import io.github.evercraftmc.messaging.server.netty.ECMessagingServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ECMessagingServer {
    protected final @NotNull Logger logger;

    protected final @NotNull InetSocketAddress address;

    protected final @NotNull Object statusLock = new Object();
    protected boolean running = false;

    protected Thread thread;
    protected EventLoopGroup serverWorker;
    protected EventLoopGroup connectionWorker;
    protected ServerChannel channel;

    public ECMessagingServer(@NotNull Logger logger, @NotNull InetSocketAddress address) {
        this.logger = logger;

        this.address = address;
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    public @NotNull InetSocketAddress getAddress() {
        return this.address;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void start() {
        synchronized (this.statusLock) {
            if (this.running) {
                throw new RuntimeException(this.getClass().getSimpleName() + " is already running!");
            }

            this.logger.info("Starting Messaging server on port {}", this.address.getPort());

            this.thread = new Thread(this::run, this.getClass().getSimpleName() + "[address=" + this.address + "]");
            this.thread.setDaemon(true);
            this.thread.start();
        }
    }

    public void stop() {
        synchronized (this.statusLock) {
            if (!this.running) {
                throw new RuntimeException(this.getClass().getSimpleName() + " is already running!");
            }
            this.running = false;

            this.logger.info("Stopping Messaging server");

            Future<?> serverFuture = null;
            Future<?> connectionFuture = null;
            if (this.serverWorker != null) {
                serverFuture = this.serverWorker.shutdownGracefully(500, 5000, TimeUnit.MILLISECONDS);
            }
            if (this.connectionWorker != null) {
                connectionFuture = this.connectionWorker.shutdownGracefully(500, 5000, TimeUnit.MILLISECONDS);
            }

            if (serverFuture != null) {
                serverFuture.syncUninterruptibly();
            }
            if (connectionFuture != null) {
                connectionFuture.syncUninterruptibly();
            }
        }
    }

    protected void run() {
        try {
            synchronized (this.statusLock) {
                this.running = true;

                ServerBootstrap bootstrap = new ServerBootstrap();

                this.serverWorker = new NioEventLoopGroup(8);
                this.connectionWorker = new NioEventLoopGroup(512);

                bootstrap.channel(NioServerSocketChannel.class).group(this.serverWorker, this.connectionWorker);

                bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    public void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new ECMessagingServerHandler(ECMessagingServer.this));
                    }
                });

                bootstrap.childOption(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_BACKLOG, 16).childOption(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.validate();

                {
                    this.channel = (ServerChannel) bootstrap.bind(this.address).syncUninterruptibly().channel();

                    this.logger.info("Successfully started Messaging server");
                }
            }

            {
                this.channel.closeFuture().syncUninterruptibly();

                this.logger.info("Successfully stopped Messaging server");
            }
        } catch (Exception e) {
            this.logger.error("Exception in Messaging server", e);

            throw e;
        }
    }
}