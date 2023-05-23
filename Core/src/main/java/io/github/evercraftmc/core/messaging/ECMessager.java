package io.github.evercraftmc.core.messaging;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.net.SocketFactory;
import io.github.evercraftmc.core.ECPlugin;

public class ECMessager {
    protected final ECPlugin parent;

    protected InetSocketAddress address;

    protected String id;

    protected List<ECMessageListener> listeners = new ArrayList<ECMessageListener>();

    protected Thread connectionThread;
    protected Socket connection;

    protected final Object READ_LOCK = new Object();
    protected final Object WRITE_LOCK = new Object();

    public ECMessager(ECPlugin parent, InetSocketAddress address, String id) {
        this.parent = parent;

        this.address = address;

        this.id = id;
    }

    public void connect() throws IOException {
        this.connectionThread = new Thread(() -> {
            try {
                this.connection = SocketFactory.getDefault().createSocket(this.address.getAddress(), this.address.getPort());

                ByteArrayOutputStream helloMessageData = new ByteArrayOutputStream();
                DataOutputStream helloMessage = new DataOutputStream(helloMessageData);
                helloMessage.writeInt(ECMessageType.HELLO.getCode());
                helloMessage.writeUTF(this.id);
                writeMessage(new ECMessage(this.id, helloMessageData.toByteArray()));
                helloMessage.close();

                while (this.connection.isConnected()) {
                    ECMessage message = readMessage();

                    for (ECMessageListener listener : this.listeners) {
                        listener.onMessage(message);
                    }
                }

                this.connection.shutdownInput();
                this.connection.shutdownOutput();
                this.connection.close();
            } catch (Exception e) {
                parent.getLogger().error("Error in messager", e);
            }
        }, "ECMessager");
        this.connectionThread.start();
    }

    public void addListener(ECMessageListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ECMessageListener listener) {
        this.listeners.remove(listener);
    }

    public void send(ECMessage message) {
        try {
            this.writeMessage(message);
        } catch (IOException e) {
            parent.getLogger().error("Error sending message", e);
        }
    }

    protected ECMessage readMessage() throws IOException {
        synchronized (READ_LOCK) {
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(this.connection.getInputStream()));

            String sender = inputStream.readUTF();

            int size = inputStream.readInt();
            byte[] data = new byte[size];
            inputStream.read(data, 0, size);

            return new ECMessage(sender, size, data);
        }
    }

    protected void writeMessage(ECMessage message) throws IOException {
        synchronized (WRITE_LOCK) {
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(this.connection.getOutputStream()));

            outputStream.writeUTF(message.getSender());

            outputStream.writeInt(message.getSize());
            outputStream.write(message.getData(), 0, message.getSize());
        }
    }
}