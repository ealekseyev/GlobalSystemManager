package ml.fusion.server.FileTransfer;

import ml.fusion.server.Comms.SocketWrapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private String filePath = "";
    private volatile String fileName = "";
    private volatile long fileSize = 0;
    public void listen(SocketWrapper socket) throws Exception {
        try {
            ml.fusion.server.Support.OF.println(Constants.YELLOW + "Incoming connection from " + socket.getRemoteSocketAddress().toString() + Constants.RESET);

            // input stream from socket
            DataInputStream sockStream = new DataInputStream(socket.getInputStream());

            // get info
            fileName = sockStream.readUTF();
            fileSize = sockStream.readLong();

            // check if file exists, change name if necessary
            filePath = System.getenv("HOME") + "/Downloads/";
            while(true) {
                if (new File(filePath + fileName).exists()) {
                    fileName = OF.newFileName(fileName);
                } else {
                    filePath += fileName;
                    break;
                }
            }

            //Initialize the FileOutputStream to the output file's full path.
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(filePath));

            OF.printSize(fileSize);
            ml.fusion.server.Support.OF.println("Saving file to " + filePath);

            // TODO: for sending directories, simply send the file and its path.
            // TODO: If the path does not exist, create the required folders automatically.

            /*
            -----------START RETRIEVAL----------
             */

            // buffer for incoming data
            byte[] contents = new byte[Constants.bufLen];
            long dataRecv = 0;
            long prevDataRecv = 0;
            long prevCheckTime = ml.fusion.server.Support.OF.getUnixTimeMillis();
            // This will become the byte count from the read() call
            int byteCount = 0;

            // write data to file as it comes in
            long start = System.currentTimeMillis();
            ml.fusion.server.Support.OF.println(Constants.YELLOW + "Receiving file - 0% complete..." + Constants.RESET);
            while ((byteCount = sockStream.read(contents)) != -1) {
                outputStream.write(contents, 0, byteCount);
                dataRecv += byteCount;
                if(((dataRecv * 100) / fileSize) - ((prevDataRecv * 100) / fileSize) == 5 && dataRecv != fileSize) {
                    ml.fusion.server.Support.OF.print(Constants.YELLOW + "Receiving file - " + (dataRecv * 100) / fileSize + "% complete..." + Constants.RESET);
                    ml.fusion.server.Support.OF.printf(Constants.CYAN + " Speed: %.1f mbps\n" + Constants.RESET, ((dataRecv - prevDataRecv) / 1000000.0) / ((System.currentTimeMillis() - prevCheckTime) / 1000.0));
                    prevDataRecv = dataRecv;
                    prevCheckTime = System.currentTimeMillis();
                }
            }
            if(dataRecv != fileSize) {
                ml.fusion.server.Support.OF.println(Constants.RED + "An error occurred during retreival of " + fileName + "." + Constants.RESET);
                //System.exit(0);
            }
            ml.fusion.server.Support.OF.println(Constants.YELLOW + "Recieving file - 100% complete!" + Constants.RESET);
            ml.fusion.server.Support.OF.printf(Constants.CYAN + "Time elapsed: %.1f minutes\n" + Constants.RESET, (System.currentTimeMillis() - start) / 60000.0);
            ml.fusion.server.Support.OF.printf(Constants.CYAN + "Average speed: %.1f megabytes per second.\n" + Constants.RESET, (fileSize/1000000.0) / ((System.currentTimeMillis() - start) / 1000.0));


            /*
            -----------END RETRIEVAL----------
             */

            // close data streams
            outputStream.flush();
            outputStream.close();
            socket.close();

            ml.fusion.server.Support.OF.println(Constants.GREEN + "Success!" + Constants.RESET);
        } catch (Exception e) {
            System.out.println(Constants.RED + e + Constants.RESET);
        }
    }
}