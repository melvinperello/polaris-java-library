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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamAdapter;

/**
 * A wrapper class for Apache Commons Net FTP API. this class is intended to
 * manage the FTP client easily.
 *
 * Based on Apache Commons Net 3.6 and is tested in this version only.
 *
 * Please visit: https://commons.apache.org/proper/commons-net/index.html for
 * more information.
 *
 * @author Jhon Melvin
 */
public class ApacheFTPClientManager implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(ApacheFTPClientManager.class.getName());

    /**
     * FTP Client Connection.
     */
    private final FTPClient ftpClient;

    /**
     * Default Constructor.
     */
    private ApacheFTPClientManager() {
        throw new IllegalStateException("You are not allowed to do this.");
    }

    public ApacheFTPClientManager(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
        this.ftpClient.setBufferSize(4096);
    }

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return this.ftpClient.isConnected();
    }

    /**
     * logout and disconnects this client's connection.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (ftpClient != null) {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    /**
     * logout and disconnects this client's connection. Ignoring Exceptions.
     */
    public void closeQuietly() {
        try {
            this.close();
        } catch (IOException ex) {
            // ignore
            LOGGER.log(Level.WARNING, "Cannot close FTP Manager -> {0}", ex.toString());
        }
    }

    /**
     * Recommended maximum buffer size is 32768. this are the size of the chunks
     * of data individually sent.
     *
     * @param bufferSize
     */
    public void setTransferBufferSize(int bufferSize) {
        this.ftpClient.setBufferSize(bufferSize);
    }

    /**
     * Set Transfer Listener. cannot listen when transferring using streams.
     *
     * @param listener
     */
    public void setTransferListener(TransferListener listener) {
        this.ftpClient.setCopyStreamListener(new CopyStreamAdapter() {
            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                super.bytesTransferred(totalBytesTransferred, bytesTransferred, streamSize);
                listener.listen(totalBytesTransferred, bytesTransferred, streamSize);
            }
        });
    }

    /**
     * Transfer Listener Interface.
     */
    @FunctionalInterface
    public interface TransferListener {

        /**
         *
         * @param totalBytesTransferred Total size of the transferred file.
         * @param bufferSize size that being transferred
         * @param streamSize
         */
        void listen(long totalBytesTransferred, int bufferSize, long streamSize);
    }

    /**
     * Download a file from the ftp server. in case of failure of transfer the
     * temporary file is deleted.
     *
     * @param remoteFile
     * @param outputFile
     * @return
     * @throws IOException
     */
    public boolean download(String remoteFile, File outputFile) throws IOException {
        /**
         * Holds the downloaded file.
         */
        OutputStream outputFileBinary = null;
        boolean transferred = false;
        try {
            outputFileBinary = new BufferedOutputStream(new FileOutputStream(outputFile.getAbsolutePath()));
            transferred = ftpClient.retrieveFile(remoteFile, outputFileBinary);
        } finally {

            /**
             * Close the stream.
             */
            if (outputFileBinary != null) {
                try {
                    outputFileBinary.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, "Unable to close the output stream of the downloaded file the lock may not be released -> {0}", ex.toString());
                }
            }

            /**
             * If transferred was failed delete the downloaded file.
             */
            if (!transferred) {
                try {
                    Files.deleteIfExists(Paths.get(outputFile.toURI()));
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, "Unable to delete temporary file -> {0}", ex.toString());
                }
            }

        }
        /**
         * Return the result.
         */
        return transferred;
    }

    @Deprecated
    private boolean downloadStream(String remoteFile, File outputFile) {
        OutputStream outputStream2 = null;
        InputStream inputStream = null;

        try {
            // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = remoteFile;
            File downloadFile2 = outputFile;
            outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            inputStream = this.ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputStream2.write(bytesArray, 0, bytesRead);
            }

            boolean success = this.ftpClient.completePendingCommand();
            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                outputStream2.close();
                inputStream.close();
            } catch (Exception e) {
            }

        }
        return true;
    }

}
