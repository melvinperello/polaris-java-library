/**
 *
 * Polaris Java Library - Afterschool Creatives "Captivating Creativity"
 *
 * Copyright 2018 Jhon Melvin Perello
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package org.afterschoolcreatives.polaris.java.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * A wrapper class for Apache Commons Net FTP API. this class is intended to
 * create client connections easily.
 *
 * Based on Apache Commons Net 3.6 and is tested in this version only.
 *
 * Please visit: https://commons.apache.org/proper/commons-net/index.html for
 * more information.
 *
 * @author Jhon Melvin
 */
public class ApacheFTPClientFactory {

    private static final Logger LOGGER = Logger.getLogger(ApacheFTPClientFactory.class.getName());

    private String server;
    private int port;
    private String username;
    private String password;
    private boolean localPassiveMode;

    // modes
    private int fileTransferMode;
    private int fileType;
    private int fileFormat;
    private boolean nagling;

    /**
     * Default Constructor.
     */
    public ApacheFTPClientFactory() {
        this.localPassiveMode = true;
        this.fileTransferMode = FTP.STREAM_TRANSFER_MODE;
        this.fileType = FTP.BINARY_FILE_TYPE;
        this.fileFormat = FTP.BINARY_FILE_TYPE;
        this.nagling = false;
    }

    /**
     *
     * FTP uses two channels between client and server, the command channel and
     * the data channel, which are actually separate TCP connections. The
     * command channel is for commands and responses, the data channel is for
     * actually transferring files. It's a nifty way of sending commands to the
     * server without having to wait for the current data transfer to finish.
     *
     * In active mode, the client establishes the command channel (from client
     * port X to server port 21(b)) but the server establishes the data channel
     * (from server port 20(b) to client port Y, where Y has been supplied by
     * the client).
     *
     * In passive mode, the client establishes both channels. In that case, the
     * server tells the client which port should be used for the data channel.
     *
     * Passive mode is generally used in situations where the FTP server is not
     * able to establish the data channel. One of the major reasons for this is
     * network firewalls. While you may have a firewall rule which allows you to
     * open up FTP channels to ftp.microsoft.com, Microsoft's servers may not
     * have the power to open up the data channel back through your firewall.
     *
     * Passive mode solves this by opening up both types of channel from the
     * client side. In order to make this hopefully clearer:
     *
     * Active mode:
     *
     * Client opens up command channel from client port 2000(a) to server port
     * 21(b). Client sends PORT 2001(a) to server and server acknowledges on
     * command channel. Server opens up data channel from server port 20(b) to
     * client port 2001(a). Client acknowledges on data channel. Passive mode:
     *
     * Client opens up command channel from client port 2000(a) to server port
     * 21(b). Client sends PASV to server on command channel. Server sends back
     * (on command channel) PORT 1234(a) after starting to listen on that port.
     * Client opens up data channel from client 2001(a) to server port 1234(a).
     * Server acknowledges on data channel. At this point, the command and data
     * channels are both open.
     *
     * (a)Note that the selection of ports on the client side is up to the
     * client, as the selection of the server data channel port in passive mode
     * is up to the server.
     *
     * (b)Further note that the use of port 20 and 21 is only a convention
     * (although a strong one). There's no absolute requirement that those ports
     * be used although the client and server both have to agree on which ports
     * are being used. I've seen implementations that try to hide from clients
     * by using different ports (futile, in my opinion).
     *
     * source:
     * https://stackoverflow.com/questions/1699145/what-is-the-difference-between-active-and-passive-ftp
     *
     * @param passiveMode
     */
    public void setLocalPassiveMode(boolean passiveMode) {
        this.localPassiveMode = passiveMode;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFileTransferMode(int fileTransferMode) {
        this.fileTransferMode = fileTransferMode;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public void setFileFormat(int fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * By default, TCP connections wait a small fixed interval before actually
     * sending data, in order to permit small packets to be combined. This
     * algorithm is named after its inventor, John Nagle, and is often referred
     * to as 'Nagling'.
     *
     * While this reduces network resource utilization in these situations, it
     * imposes a delay on all outgoing message data, which can cause problems in
     * client/server situations.
     *
     * This function allows this feature to be disabled (de-Nagling,
     * value=FALSE) or enabled (Nagling, value=TRUE) for the specified socket.
     *
     * @param nagling true to enable false to disable.
     */
    public void setNagling(boolean nagling) {
        this.nagling = nagling;
    }

    /**
     * Creates a FTP Client Connection.
     *
     * @return
     * @throws IOException
     */
    public FTPClient createClient() throws IOException {
        // create a client
        FTPClient ftpClient = new FTPClient();
        // connect first to the server
        ftpClient.connect(this.server, this.port);
        LOGGER.log(Level.INFO, "[Start Connection >> {0}] -> {1}", new Object[]{ftpClient.getReplyCode(), ftpClient.getReplyString()});
        // login
        ftpClient.login(this.username, this.password);
        LOGGER.log(Level.INFO, "[Login >> {0}] -> {1}", new Object[]{ftpClient.getReplyCode(), ftpClient.getReplyString()});
        /**
         * Local Passive Mode.
         */
        if (this.localPassiveMode) {
            ftpClient.enterLocalPassiveMode();
            /**
             * Extended Passive mode with IPv4
             */
            ftpClient.setUseEPSVwithIPv4(true);
            LOGGER.log(Level.INFO, "[Enter Local Passive Mode >> {0}] -> {1}", new Object[]{ftpClient.getReplyCode(), ftpClient.getReplyString()});
        }
        /**
         * Binary File Type.
         */
        ftpClient.setFileType(this.fileType, this.fileFormat);
        LOGGER.log(Level.INFO, "[File Type >> {0}] -> {1}", new Object[]{ftpClient.getReplyCode(), ftpClient.getReplyString()});
        ftpClient.setFileTransferMode(this.fileTransferMode);
        LOGGER.log(Level.INFO, "[Transfer Mode >> {0}] -> {1}", new Object[]{ftpClient.getReplyCode(), ftpClient.getReplyString()});

        /**
         * Nagle Algorithm.
         */
        ftpClient.setTcpNoDelay(this.nagling);

        return ftpClient;
    }

    /**
     * Create a FTP Client Manager
     *
     * @return
     * @throws IOException
     */
    public ApacheFTPClientManager createClientManager() throws IOException {
        return new ApacheFTPClientManager(this.createClient());
    }

}
