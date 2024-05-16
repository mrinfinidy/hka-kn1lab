import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Die "Klasse" Sender liest einen String von der Konsole und zerlegt ihn in einzelne Worte. Jedes Wort wird in ein
 * einzelnes {@link Packet} verpackt und an das Medium verschickt. Erst nach dem Erhalt eines entsprechenden
 * ACKs wird das nächste {@link Packet} verschickt. Erhält der Sender nach einem Timeout von einer Sekunde kein ACK,
 * überträgt er das {@link Packet} erneut.
 */
public class Sender {
    /**
     * Hauptmethode, erzeugt Instanz des {@link Sender} und führt {@link #send()} aus.
     * @param args Argumente, werden nicht verwendet.
     */
    public static void main(String[] args) {
        Sender sender = new Sender();
        try {
            sender.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt neuen Socket. Liest Text von Konsole ein und zerlegt diesen. Packt einzelne Worte in {@link Packet}
     * und schickt diese an Medium. Nutzt {@link SocketTimeoutException}, um eine Sekunde auf ACK zu
     * warten und das {@link Packet} ggf. nochmals zu versenden.
     * @throws IOException Wird geworfen falls Sockets nicht erzeugt werden können.
     */
    private void send() throws IOException {
        int count = 0;
        int seq = 1;
        int ackNum = 1; 
        boolean ackFlag = false;
        
   	    //Text einlesen und in Worte zerlegen
        Scanner in = new Scanner(System.in);
        String msg = in.nextLine();
        String[] words = msg.split(" ");

        // Socket erzeugen auf Port 9998 und Timeout auf eine Sekunde setzen
        DatagramSocket clientSocket = new DatagramSocket(9998);
        clientSocket.setSoTimeout(1000);
        InetAddress address = InetAddress.getByName("localhost");

        // Iteration über den Konsolentext

        String resultReceiver = "";
        
        while (count <= words.length) {
            byte[] payloadOut;
            if(count == words.length) {
                String end = "EOT";
                payloadOut = end.getBytes(); 
            } else {
                payloadOut = words[count].getBytes();
            }


            // create new packet 
            Packet packetOut = new Packet(seq, ackNum, ackFlag, payloadOut);
            // serialize Packet for sending
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(packetOut);
            byte[] bufOut = b.toByteArray();
            DatagramPacket packet = new DatagramPacket(bufOut, bufOut.length, address, 9997);
            // Paket an Port 9997 senden                       
            clientSocket.send(packet);
            System.out.println("C -> S: " + " ACKnum: " + ackNum + " seq: " + seq + " length: " + payloadOut.length);
        	
            try {
                
                // Auf ACK warten und erst dann Schleifenzähler inkrementieren
                byte[] bufIn = new byte[256];
                DatagramPacket rcvPacketRaw = new DatagramPacket(bufIn, bufIn.length);
                clientSocket.receive(rcvPacketRaw);
                // deserialize Packet
                ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(rcvPacketRaw.getData()));
                Packet packetIn = (Packet) is.readObject();
                String rcvmsg = new String(packetIn.getPayload());
                System.out.println("S -> C: " + " ACKnum: " + packetIn.getAckNum() + " seq: " + packetIn.getSeq() 
                + " length: " + packetIn.getPayload().length + " payload: " + rcvmsg);

                if (!rcvmsg.equals("EOT")) {
                    resultReceiver = resultReceiver.concat(" " + rcvmsg);
                }

                if(packetIn.getAckNum() == seq + payloadOut.length) {
                    count ++;
                    seq = seq + payloadOut.length;
                    ackNum = packetIn.getSeq() + packetIn.getPayload().length;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
            	System.out.println("Receive timed out, retrying...");
            }
        }
        
        System.out.println("Receiver:" + resultReceiver);
        
        // Wenn alle Packete versendet und von der Gegenseite bestätigt sind, Programm beenden
        clientSocket.close();
        
        if(System.getProperty("os.name").equals("Linux")) {
            clientSocket.disconnect();
        }

        System.exit(0);
    }
}
