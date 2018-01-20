package org.afterschoolcreatives.polaris.java.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * A helper class for files.
 *
 * @author Jhon Melvin
 */
public class FileTool {

    /**
     * Transfers bytes into this channel's file from the given readable byte
     * channel. Recommended when transferring large files.
     *
     * <p>
     * An attempt is made to read up to <tt>count</tt> bytes from the source
     * channel and write them to this channel's file starting at the given
     * <tt>position</tt>. An invocation of this method may or may not transfer
     * all of the requested bytes; whether or not it does so depends upon the
     * natures and states of the channels. Fewer than the requested number of
     * bytes will be transferred if the source channel has fewer than
     * <tt>count</tt> bytes remaining, or if the source channel is non-blocking
     * and has fewer than <tt>count</tt> bytes immediately available in its
     * input buffer.
     *
     * <p>
     * This method does not modify this channel's position. If the given
     * position is greater than the file's current size then no bytes are
     * transferred. If the source channel has a position then bytes are read
     * starting at that position and then the position is incremented by the
     * number of bytes read.
     *
     * <p>
     * This method is potentially much more efficient than a simple loop that
     * reads from the source channel and writes to this channel. Many operating
     * systems can transfer bytes directly from the source channel into the file
     * system cache without actually copying them.  </p>
     *
     * @param source File object as source.
     * @param destination File object as destination.
     * @return if the size of the source and the destination matches.
     * @throws java.io.IOException
     */
    public static boolean copyChannel(File source, File destination) throws IOException {
        // create channels
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;

        try {
            // open channels
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            // start transfer
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            return destinationChannel.size() == sourceChannel.size();
        } finally {
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
            } catch (IOException e) {
                // ignore
            }

            try {
                if (destinationChannel != null) {
                    destinationChannel.close();
                }
            } catch (IOException e) {
                // ignore
            }

        }
    }

    /**
     * Creates a directory and all its parent directories.
     *
     * @param directory
     * @return true if already exist.
     */
    public static boolean createDirectory(String directory) {
        File dirName = new File(directory);
        if (!dirName.exists()) {
            return dirName.mkdirs();
        } else {
            return true;
        }
    }

}
