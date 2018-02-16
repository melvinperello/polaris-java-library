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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * @throws IllegalArgumentException If the preconditions on the parameters
     * do not hold
     *
     * @throws NonReadableChannelException If the source channel was not opened
     * for reading
     *
     * @throws NonWritableChannelException If this channel was not opened for
     * writing
     *
     * @throws ClosedChannelException If either this channel or the source
     * channel is closed
     *
     * @throws AsynchronousCloseException If another thread closes either
     * channel while the transfer is in progress
     *
     * @throws ClosedByInterruptException If another thread interrupts the
     * current thread while the transfer is in progress, thereby closing both
     * channels and setting the current thread's interrupt status
     *
     * @throws IOException If some other I/O error occurs
     */
    public static boolean copyChannel(File source, File destination)
            throws IOException, IllegalArgumentException, NonWritableChannelException,
            ClosedChannelException, AsynchronousCloseException,
            ClosedByInterruptException, NonReadableChannelException {
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
     * @see FileTool#createDirectory(java.io.File)
     */
    public static boolean createDirectory(String directory) throws SecurityException {
        return createDirectory(new File(directory));
    }

    /**
     * Creates a directory and all its parent directories.
     *
     * @param directory
     * @return
     */
    public static boolean createDirectory(File directory) throws SecurityException {
        if (!directory.exists()) {
            return directory.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * Creates a folder and all of its parent folders.
     *
     * @param folderPath
     * @return true if successfully created or it is already existing.
     * @throws IOException
     */
    public static boolean createFolder(String folderPath) throws IOException {
        if (FileTool.isFileExisting(folderPath)) {
            return true;
        }
        Path path = Files.createDirectories(Paths.get(folderPath));
        return path != null;
    }

    /**
     * Creates a folder and all of its parent folders.
     *
     * @param folder
     * @return true if successfully created or it is already existing.
     * @throws IOException
     */
    public static boolean createFolder(File folder) throws IOException {
        if (FileTool.isFileExisting(folder)) {
            return true;
        }
        Path path = Files.createDirectories(Paths.get(folder.toURI()));
        return path != null;
    }

    /**
     * As with the delete(Path) method, an implementation may need to examine
     * the file to determine if the file is a directory. Consequently this
     * method may not be atomic with respect to other file system operations. If
     * the file is a symbolic link, then the symbolic link itself, not the final
     * target of the link, is deleted. If the file is a directory then the
     * directory must be empty. In some implementations a directory has entries
     * for special files or links that are created when the directory is
     * created. In such implementations a directory is considered empty when
     * only the special entries exist. On some operating systems it may not be
     * possible to remove a file when it is open and in use by this Java virtual
     * machine or other programs.
     *
     * @param filePath Absolute or relative String path
     * @return true if the file was deleted by this method; false if the file
     * could not be deleted because it did not exist
     * @throws NoSuchFileException when the file does not exists.
     * @throws DirectoryNotEmptyException when the directory is not empty.
     * @throws IOException Invalid Permission or IO error.
     */
    public static boolean deleteFileIfExists(String filePath) throws NoSuchFileException,
            DirectoryNotEmptyException, IOException {
        return Files.deleteIfExists(Paths.get(filePath));
    }

    /**
     * As with the delete(Path) method, an implementation may need to examine
     * the file to determine if the file is a directory. Consequently this
     * method may not be atomic with respect to other file system operations. If
     * the file is a symbolic link, then the symbolic link itself, not the final
     * target of the link, is deleted. If the file is a directory then the
     * directory must be empty. In some implementations a directory has entries
     * for special files or links that are created when the directory is
     * created. In such implementations a directory is considered empty when
     * only the special entries exist. On some operating systems it may not be
     * possible to remove a file when it is open and in use by this Java virtual
     * machine or other programs.
     *
     * @param file File object to delete.
     * @return true if the file was deleted by this method; false if the file
     * could not be deleted because it did not exist
     * @throws NoSuchFileException when the file does not exists.
     * @throws DirectoryNotEmptyException when the directory is not empty.
     * @throws IOException Invalid Permission or IO error.
     */
    public static boolean deleteFileIfExists(File file) throws NoSuchFileException,
            DirectoryNotEmptyException, IOException {
        return deleteFileIfExists(file.getAbsolutePath());
    }

    /**
     * Checks if a file or directory is existing.
     *
     * @param file
     * @return true if existing.
     */
    public static boolean isFileExisting(File file) {
        return Files.exists(Paths.get(file.toURI()));
    }

    /**
     * Checks if a file or directory is existing.
     *
     * @param filePath path.
     * @return true if existing.
     */
    public static boolean isFileExisting(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

}
