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
package org.afterschoolcreatives.polaris.java.net.ip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.CopyStreamEvent;

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
        // default buffer
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
     * Aborts any transfer activity in this connection.
     *
     * @throws IOException
     */
    public void abort() throws IOException {
        this.ftpClient.abort();
    }

    /**
     * Gets this manager FTP Connection Client.
     *
     * @return
     */
    public FTPClient getFtpClient() {
        return ftpClient;
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

    private TransferListener onTransferListener;

    /**
     * Set Transfer Listener.
     *
     * @param listener
     */
    public void setOnTransferListener(TransferListener listener) {
        this.onTransferListener = listener;
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
     * @param downloadFile
     * @return
     * @throws IOException
     */
    public boolean download(String remoteFile, String downloadFile) throws IOException {
        /**
         * Holds the downloaded file.
         */
        OutputStream outputFileBinary = null;
        boolean transferred = false;
        try {
            outputFileBinary = new BufferedOutputStream(new FileOutputStream(downloadFile));
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
                    Files.deleteIfExists(Paths.get(downloadFile));
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

    /**
     * Download a file from the ftp server using STREAMS. in case of failure of
     * transfer the temporary file is deleted.
     *
     * @param remoteFile
     * @param downloadFile
     * @return
     * @throws IOException
     */
    public boolean downloadStream(String remoteFile, String downloadFile) throws IOException {

        int bufferSize = this.ftpClient.getBufferSize();
        long bytesWritten = 0;
        boolean transferred = false;
        /**
         * Initialize Streams.
         */
        OutputStream outputFileStream = null;
        InputStream inputStream = null;

        try {
            /**
             * Local output stream where to write data.
             */
            outputFileStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            /**
             * Remote input stream where to read data.
             */
            inputStream = this.ftpClient.retrieveFileStream(remoteFile);
            /**
             * Individual byte transfer and size.
             */
            byte[] bytesArray = new byte[bufferSize];

            int bytesRead;

            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputFileStream.write(bytesArray, 0, bytesRead);
                bytesWritten += bytesRead;
                /**
                 * Invoke Listener for streams.
                 */
                this.onTransferListener.listen(bytesWritten, bytesRead, CopyStreamEvent.UNKNOWN_STREAM_SIZE);
            }
            transferred = this.ftpClient.completePendingCommand();
        } finally {
            /**
             * Close output stream.
             */
            if (outputFileStream != null) {
                try {
                    outputFileStream.close();
                } catch (IOException e) {
                    // ignore
                    LOGGER.log(Level.WARNING, "Unable to close the output file stream of the downloaded file the lock may not be released -> {0}", e.toString());
                }
            }

            /**
             * Close input stream.
             */
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                    LOGGER.log(Level.WARNING, "Unable to close the input file stream of the downloaded file the lock may not be released -> {0}", e.toString());
                }
            }

            /**
             * If transferred was failed delete the downloaded file.
             */
            if (!transferred) {
                try {
                    Files.deleteIfExists(Paths.get(downloadFile));
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, "Unable to delete temporary file -> {0}", ex.toString());
                }
            }

        }
        return transferred;
    }

    /**
     * Uploads a file to the FTP server.
     *
     * @param localFile
     * @param uploadFile
     * @return
     * @throws IOException
     */
    public boolean upload(String localFile, String uploadFile) throws IOException {
        /**
         * Local File Input Stream.
         */
        InputStream inputStream = null;
        boolean uploaded = false;
        try {
            inputStream = new FileInputStream(localFile);

            uploaded = this.ftpClient.storeFile(uploadFile, inputStream);

            return uploaded;
        } finally {
            /**
             * Close the stream.
             */
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                    LOGGER.log(Level.WARNING, "Unable to close the input stream of the uploaded file the lock may not be released -> {0}", e.toString());
                }
            }
            /**
             * Add some mechanism to delete the file if the upload was not
             * successful.
             */
            if (!uploaded) {
                // if upload fail delete file in ftp if posible.
                LOGGER.log(Level.WARNING, "Failed to upload file. No Temporary File Cleanup Mechanism Found.");
            }
        }

    }

    /**
     * Uploads a file to the FTP server using Streams.
     *
     * @param localFile
     * @param uploadFile
     * @return
     * @throws IOException
     */
    public boolean uploadStream(String localFile, String uploadFile) throws IOException {

        int totalUploadedSize = 0;
        InputStream localFileInputStream = null;
        OutputStream remoteFileOutputStream = null;
        boolean uploaded = false;
        try {
            /**
             * Open input stream.
             */
            localFileInputStream = new FileInputStream(localFile);
            /**
             * Open FTP output stream.
             */
            remoteFileOutputStream = this.ftpClient.storeFileStream(uploadFile);

            byte[] bytesIn = new byte[this.ftpClient.getBufferSize()];

            int read;

            while ((read = localFileInputStream.read(bytesIn)) != -1) {
                remoteFileOutputStream.write(bytesIn, 0, read);
                /**
                 * Add read count.
                 */
                totalUploadedSize += read;
                /**
                 * Invoke listener.
                 */
                this.onTransferListener.listen(totalUploadedSize, read, CopyStreamEvent.UNKNOWN_STREAM_SIZE);
            }
            localFileInputStream.close();
            remoteFileOutputStream.close();

            uploaded = ftpClient.completePendingCommand();

            return uploaded;
        } finally {
            /**
             * Close connection output stream.
             */
            if (remoteFileOutputStream != null) {
                try {
                    remoteFileOutputStream.close();
                } catch (IOException e) {
                    //ignore
                    LOGGER.log(Level.WARNING, "Unable to close the remote file output stream of the downloaded file the lock may not be released -> {0}", e.toString());
                }
            }

            /**
             * Close local stream.
             */
            if (localFileInputStream != null) {
                try {
                    localFileInputStream.close();
                } catch (IOException e) {
                    //ignore
                    LOGGER.log(Level.WARNING, "Unable to close the local file input stream of the downloaded file the lock may not be released -> {0}", e.toString());
                }
            }

            /**
             * Add some mechanism to delete the file if the upload was not
             * successful.
             */
            if (!uploaded) {
                // if upload fail delete file in ftp if posible.
                LOGGER.log(Level.WARNING, "Failed to upload file. No Temporary File Cleanup Mechanism Found.");
            }
        }

    }

}
