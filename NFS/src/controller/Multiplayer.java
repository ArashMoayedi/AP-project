package controller;

import model.City;
import model.Machine;

import javax.swing.*;
import java.net.*;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import static model.City.machines;

public class Multiplayer {

    City city;

    public static class Client{

        public Socket socket = null;
        public DataInputStream in = null;
        public DataOutputStream out = null;
        public int udpPort = -1;
        public String address = null;
    }

    public static class State {
        public float x;
        public float y;
        public double angle;
    }

    public ArrayList<Client> clients = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private DatagramSocket receiverSocket = null;
    private DatagramPacket receiverPacket = null;
    private DatagramSocket senderSocket = null;
    private DatagramPacket[] senderPackets;
    private final int PACKET_SIZE = 17;
    private final byte[] senderData = new byte[ PACKET_SIZE ];
    private State[] vehicleStates = { new State() };
    private boolean[] receivedDataFrom = { true };
    private byte[] receivedStatuses = { (byte)-1 };
    private long[] receivedTimes = { -1 };
    private boolean isHost = true;
    String hostAddress;

    private java.awt.TextArea textArea = new java.awt.TextArea();

    public Multiplayer(City city, int port){

        this.city = city;
        senderPackets = null;
        try {
            hostAddress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            int attempts = 0;
            while ( serverSocket == null ) {

                try {
                    serverSocket = new ServerSocket( port );
                    System.out.println( "made server socket" );
                }
                catch( Exception e ) {
                    port++;
                    attempts++;
                    if ( attempts > 50 ) break;
                    System.out.println( "attempting to rebind port" );
                }
            }
            System.out.println("making datagrams");
            receiverSocket = new DatagramSocket();
            receiverPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);

            senderSocket = new DatagramSocket();
            System.out.println("made datagrams");

            java.awt.Frame frame = new java.awt.Frame(InetAddress.getLocalHost().toString() + ":" + receiverSocket.getLocalPort());
            frame.add(textArea);
            frame.setSize(200, 200);


            WindowAdapter close = new WindowAdapter() {

                public void windowClosing(WindowEvent we) {
                    Object source = we.getSource();
                    ((Frame) source).dispose();
                }

                public void windowClosed(WindowEvent we) {
                    System.exit(0);
                }

            };

            frame.addWindowListener(close);

            frame.show();
        }
        catch ( IOException ioe ){

            System.err.println( ioe );
        }
    }

    public synchronized void addSender( String addressString, InetAddress address, int port ) {

        byte[] data = new byte[ PACKET_SIZE ];
        DatagramPacket packet = new DatagramPacket( data, PACKET_SIZE, address, port );

        if ( senderPackets == null )
            senderPackets = new DatagramPacket[ 0 ];

        DatagramPacket[] oldsenders = senderPackets;

        DatagramPacket[] newsenders = new DatagramPacket[ oldsenders.length +1 ];

        System.arraycopy( oldsenders, 0, newsenders, 0, oldsenders.length );

        newsenders[ oldsenders.length ] = packet;

        senderPackets = newsenders;
    }

    private byte status = 1;

    private int decodeInt( byte[] data, int pos ) {
        int b1 = 0xFF & data[ pos    ];
        int b2 = 0xFF & data[ pos +1 ];
        int b3 = 0xFF & data[ pos +2 ];
        int b4 = 0xFF & data[ pos +3 ];

        int anInt = (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;

        return anInt;
    }

    private int encodeInt( int anInt, byte[] data, int pos ) {
        data[ pos    ] = (byte)((0xFF000000 & anInt) >> 24);
        data[ pos +1 ] = (byte)((0x00FF0000 & anInt) >> 16);
        data[ pos +2 ] = (byte)((0x0000FF00 & anInt) >> 8);
        data[ pos +3 ] = (byte)((0x000000FF & anInt));
        return pos + 4;
    }


    public void broadcastState() {

            try {
                int pos = 0;
                byte status = 1;
                senderData[ pos ] = status;
                pos++;
                pos = encodeInt(city.getCurrentPlayer().getCurrentMachineNumber(), senderData, pos);
                pos = encodeInt( (int)city.getCurrentPlayer().getCurrentMachine().getX_center(), senderData, pos );
                pos = encodeInt( (int)city.getCurrentPlayer().getCurrentMachine().getY_center(), senderData, pos );
                pos = encodeInt( (int)city.getCurrentPlayer().getCurrentMachine().getTeta(), senderData, pos );
                for (int i=0; i<senderPackets.length; i++) {
                    DatagramPacket packet = senderPackets[i];
                    packet.setData(senderData);
                    senderSocket.send(packet);
                }
            }
            catch( IOException ioe ){
                return;
            }
            catch (NullPointerException e){
                return;
            }
            catch (ArrayIndexOutOfBoundsException e){

                return;
            }
    }

    private volatile Thread receiveThread = null;
    public void start() {

        receiveThread = new Thread() {
            public void run() {
                while( Thread.currentThread() == receiveThread ) {
                    receiveState();
                    Thread.yield();
                }
            }
        };
        receiveThread.setPriority( Thread.MIN_PRIORITY );
        if ( serverSocket != null )
            receiveThread.start();
    }

    public void stop() {
        receiveThread = null;
    }

    public synchronized void addClient( InetAddress address, int tcpPort, int udpPort ) {
        try {
            textArea.append( "adding client " + address.getHostAddress() + "\n" );
            System.out.println( "adding client" );
            Socket socket = new Socket( address, tcpPort );
            socket.setSoTimeout( 10000 ); // 5 seconds
            System.out.println( "client socket bound" );
            DataInputStream  in  = new DataInputStream( new BufferedInputStream( socket.getInputStream() ) );
            DataOutputStream out = new DataOutputStream( new BufferedOutputStream( socket.getOutputStream() ) );
            Client client = new Client();
            client.socket = socket;
            client.in  = in;
            client.out = out;
            client.udpPort = udpPort;
            client.address = address.getHostAddress();
            clients.add( client );
        }
        catch( IOException ioe ) {
            System.err.println( ioe );
        }
    }

    public synchronized void sendNewClientDetails( String address, int port ) {

        for ( int i = 0; i < clients.size(); i++ ) {
            Client client = clients.get( i );
            try {
                client.out.writeUTF( "NEW CONNECTION" );
                client.out.writeUTF( address );
                client.out.writeInt( port );
                client.out.flush();
            }
            catch( IOException ioe ) {
                System.err.println( ioe );
            }
        }

    }

    public String listenAsServer() {

        try {

            while( true ) {

                Socket socket = serverSocket.accept();

                DataInputStream in = new DataInputStream( new BufferedInputStream( socket.getInputStream() ) );

                String addressString = in.readUTF();

                System.out.println( addressString );

                int tcpPort = in.readInt();

                System.out.println( "tcp: " + tcpPort );

                int udpPort = in.readInt();

                System.out.println( "udp: " + udpPort );
                socket.close();

                InetAddress address = socket.getInetAddress();

                addSender( addressString, address, udpPort );

                sendNewClientDetails( address.getHostAddress(), udpPort );

                addClient( address, tcpPort, udpPort );
            }
        }
        catch( IOException ioe ) {

        }

        return "CONNECTED";
    }

    public String connectToServer( String address, int port ) {

        isHost = false;
        hostAddress = address;
        try {
            System.out.println( "connecting to server" );
            Socket socket = new Socket( address, port );
            textArea.append( "connected to host\n" );
            System.out.println( "connected to server" );

            DataOutputStream out = new DataOutputStream( new BufferedOutputStream( socket.getOutputStream() ) );
            DataInputStream in = new DataInputStream( new BufferedInputStream(socket.getInputStream()));
            out.writeUTF( socket.getLocalAddress().getHostAddress() );

            out.writeInt( serverSocket.getLocalPort() );

            out.writeInt( receiverSocket.getLocalPort() );

            out.flush();
//            System.out.println("xxxx");
            int udpPort = in.readInt();
//            System.out.println(udpPort);

            socket.close();

            System.out.println( "sent data to server" );
            InetAddress address1 = socket.getInetAddress();
//            System.out.println(socket.getInetAddress().toString());
            byte[] data = new byte[ PACKET_SIZE ];

            DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE, address1, udpPort);
            Thread thread = new Thread(){

                public void run(){

                    while(true){
                        try {
                            int pos = 0;
                            byte status = 1;
                            senderData[ pos ] = status;
                            pos++;
                            pos = encodeInt(city.getCurrentPlayer().getCurrentMachineNumber(), senderData, pos);
                            pos = encodeInt( (int)city.getCurrentPlayer().getCurrentMachine().getX_center(), senderData, pos );
                            pos = encodeInt( (int)city.getCurrentPlayer().getCurrentMachine().getY_center(), senderData, pos );
                            pos = encodeInt( (int)city.getCurrentPlayer().getCurrentMachine().getTeta(), senderData, pos );
//                            System.out.println((int)city.getCurrentPlayer().getCurrentMachine().getX_center());
                            packet.setData(senderData);
                            senderSocket.send(packet);
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                }
            };
            thread.start();
            return listenAsClient();
        }
        catch( IOException ioe ) {
            System.err.println( ioe );
        }
        return "";
    }

    private DataInputStream connectionToHost = null;

    public String listenAsClient() {

        try {

            System.out.println( "listening as client" );
            Socket socket = serverSocket.accept();
            socket.setSoTimeout( 10000 ); // 10 seconds
            System.out.println( "client got a server" );

            connectionToHost = new DataInputStream( new BufferedInputStream( socket.getInputStream() ) );
        }
        catch( IOException ioe ) {
            System.err.println( ioe );
            return "IOException";
        }

        return "CONNECTED";
    }

    public State[] getVehicleStates() {
        return vehicleStates;
    }

    public void receiveState() {

        try {
            receiverSocket.setSoTimeout( 150 );
            receiverSocket.receive( receiverPacket );
        }
        catch( Exception ioe ) {

            return;
        }

        byte[] receivedData = receiverPacket.getData();

        int pos = 0;

        byte receivedstatus = receivedData[ pos ];
        pos++;
        int machineNumber = decodeInt( receivedData, pos);
        int x = decodeInt( receivedData, pos+4 );
        int y = decodeInt( receivedData, pos +8 );
        int angle = decodeInt( receivedData, pos +12 );
        if (x==0){

            return;
        }
        city.machines.get(machineNumber).setX((float)x);
        city.machines.get(machineNumber).setY((float)y);
        city.machines.get(machineNumber).setTeta((double)angle);
    }

    public void chatServer(){

        Thread chattingServer = null;
        try {
            chattingServer = new Thread(){

                ServerSocket listener = new ServerSocket(9001);
                public void run(){

                    try {

                        while (true) {

                            new Chat.Handler(listener.accept()).start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            listener.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        chattingServer.start();
    }

    public void chatClient(){

        ChatClient client = new ChatClient(hostAddress);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        try {
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
