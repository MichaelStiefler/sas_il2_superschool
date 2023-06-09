package com.sas1946.fac.tools.dservercontroller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RConClient {
    
    private class RConClientTimer extends TimerTask {
        @Override
        public void run() {
            RConClient.this.update();
        }
    }

    public static enum RETURN_CODE {
        RCONCLIENT_WRITE_ERROR(-3),
        RCONCLIENT_READ_ERROR(-2),
        RCONCLIENT_CONNECTION_FAILED(-1),
        RCONCLIENT_WAITING(0),
        RCONCLIENT_CONNECTED(1),
        RCONCLIENT_MESSAGE_RECEIVED(2),
        RCONCLIENT_MESSAGE_SENT(3);
        
        private int numVal;
        
        RETURN_CODE(int numVal) {
            this.numVal = numVal;
        }
        
        public int getNumVal() {
            return numVal;
        }
    }

    public static enum COMMAND {
        none, mystatus, auth, getconsole, getplayerlist, serverstatus, kick, ban, banuser, unbanall, serverinput, sendstatnow, cutchatlog, chatmsg, opensds, spsget, spsreset, shutdown
    };

    private static final RConClient         instance;
    private AsynchronousSocketChannel       rConSockChannel;
    private CallBack<Void, RETURN_CODE, String> rConCallback;
    private COMMAND                         lastCommand;
    private AtomicBoolean                   readyForSend;
    private AtomicBoolean                   connected;
    private List<COMMAND>                   updateCommands;
    private int                             curCommand;
    private int                             timeout;
    private Timer                           timer;

    private RConClient() {
    }

    public static RConClient getInstance() {
        return instance;
    }

    public void setRConCallback(CallBack<Void, RETURN_CODE, String> rConCallback) {
        this.rConCallback = rConCallback;
    }

    private void callback(RETURN_CODE val1, String val2) {
        this.rConCallback.call(val1, val2);
    }

    public COMMAND getLastCommand() {
        return this.lastCommand;
    }

    @Deprecated
    public void spsget() {
        this.lastCommand = COMMAND.spsget;
        RConClient.this.doStartWrite(this.rConSockChannel, "spsget");
    }

    @Deprecated
    public void auth(String user, String pass) {
        this.lastCommand = COMMAND.auth;
        RConClient.this.doStartWrite(this.rConSockChannel, "auth " + user + " " + pass);
    }
    
    public void sendCommand(COMMAND command, String... params) {
        this.lastCommand = command;
        RConClient.this.doStartWrite(this.rConSockChannel, Stream.concat(Stream.of(command.name()), Arrays.stream(params)).collect(Collectors.joining(" ")));
    }
    
    public void connect(String host, int port) {
        if (this.connected.get()) return;
        try {
            this.doConnect(host, port);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void doConnect(String host, int port) throws IOException {

        final AsynchronousSocketChannel sockChannel = AsynchronousSocketChannel.open();
        sockChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

        sockChannel.connect(new InetSocketAddress(host, port), sockChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RETURN_CODE.RCONCLIENT_CONNECTED, "remote console connected");
                RConClient.this.rConSockChannel = channel;
                RConClient.this.readyForSend.set(true);
                RConClient.this.connected.set(true);
                RConClient.this.doStartRead(channel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                RConClient.this.readyForSend.set(false);
                RConClient.this.connected.set(false);
                RConClient.this.callback(RETURN_CODE.RCONCLIENT_CONNECTION_FAILED, "failed to connect to remote console");
                RConClient.this.rConSockChannel = null;
            }

        });

    }

    private void doStartRead(final AsynchronousSocketChannel sockChannel) {
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        sockChannel.read(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                buf.flip();
                final short len = (short) (((buf.get() & 0xFF) + ((buf.get() & 0xFF) * 256)) - 1);

                if (len > 0) {
                    final StringBuilder message = new StringBuilder(len);
                    for (int i = 0; i < len; i++) {
                        message.append((char) buf.get());
                    }
                    RConClient.this.readyForSend.set(true);
                    RConClient.this.callback(RETURN_CODE.RCONCLIENT_MESSAGE_RECEIVED, "Read message:" + message);
                } else {
                    RConClient.this.callback(RETURN_CODE.RCONCLIENT_WAITING, "waiting...");
                }
                RConClient.this.doStartRead(channel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                RConClient.this.readyForSend.set(false);
                RConClient.this.connected.set(false);
                RConClient.this.callback(RETURN_CODE.RCONCLIENT_READ_ERROR, "fail to read message from server");
            }

        });

    }

    private void doStartWrite(final AsynchronousSocketChannel sockChannel, final String message) {
        final int messageLen = message.length() + 1;
        final ByteBuffer buf = ByteBuffer.allocate(messageLen + 2);
        buf.clear();
        buf.put((byte) (messageLen % 256));
        buf.put((byte) (messageLen / 256));
        buf.put(message.getBytes());
        buf.put((byte) 0);

        buf.flip();
        RConClient.this.readyForSend.set(false);
        sockChannel.write(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RETURN_CODE.RCONCLIENT_MESSAGE_SENT, "write message completed, result=" + result);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                RConClient.this.readyForSend.set(false);
                RConClient.this.connected.set(false);
                RConClient.this.callback(RETURN_CODE.RCONCLIENT_WRITE_ERROR, "Fail to write the message to server");
            }
        });
    }
    
    private void update() {
        this.sendCommand(this.updateCommands.get(curCommand++));
        if (curCommand >= this.updateCommands.size()) curCommand = 0;
    }
    
    public void setUpdateCommand(List <COMMAND> updateCommands) {
        Collections.copy(this.updateCommands, updateCommands);
    }

    public void addUpdateCommand(COMMAND command, boolean restartTimer) {
        if (restartTimer) this.stopTimer();
        this.updateCommands.add(command);
        if (restartTimer) this.startTimer();
    }

    public void stopTimer() {
        timer.cancel();
    }

    public void startTimer() {
        timer.schedule(new RConClientTimer(), 100, this.timeout / this.updateCommands.size());
    }
    
    static {
        instance = new RConClient();
        instance.lastCommand = COMMAND.none;
        instance.rConSockChannel = null;
        instance.readyForSend = new AtomicBoolean();
        instance.connected = new AtomicBoolean();
        instance.updateCommands = new ArrayList<COMMAND>(Arrays.asList(COMMAND.getplayerlist, COMMAND.spsget));
        instance.timeout = 1000;
        instance.timer = new Timer();
        instance.startTimer();
    }

}
