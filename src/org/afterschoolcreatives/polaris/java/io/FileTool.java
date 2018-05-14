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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for files. NO SYMBOLIC LINKS HERE.
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
    public static boolean copy(File source, File destination)
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

    public static boolean copyQuietly(File source, File destination) {
        try {
            return FileTool.copy(source, destination);
        } catch (IOException | IllegalArgumentException | NonWritableChannelException | NonReadableChannelException ex) {
            // ignore
            return false;
        }
    }

    /**
     * Checks the existence of a folder. if the folder is not existing this
     * method will attempt to create the folder and its parent directories when
     * necessary.
     *
     * @param filePath relative or absolute path to the folder.
     * @return true if the folder is existing or created.
     * @throws IOException Some IO Exceptions.
     * @throws FileAlreadyExistsException if the file is existing as a file and
     * not a folder.
     */
    public static boolean checkFolders(String filePath) throws IOException, FileAlreadyExistsException {
        /**
         * Check if existing.
         */
        if (Files.exists(Paths.get(filePath), LinkOption.NOFOLLOW_LINKS)) {
            /**
             * Check if the existing file is a directory.
             */
            if (Files.isDirectory(Paths.get(filePath), LinkOption.NOFOLLOW_LINKS)) {
                return true;
            }
            // if the existing file is not a directory throw exception.
            throw new FileAlreadyExistsException(filePath);
        }
        /**
         * Attempt to create the file.
         */
        Path createdPath = Files.createDirectories(Paths.get(filePath));
        /**
         * Check existence.
         */
        return Files.exists(createdPath);
    }

    /**
     * Executes check folders and ignores exception.
     *
     * @param filePath
     * @return
     * @see FileTool#checkFolders(java.lang.String)
     */
    public static boolean checkFoldersQuietly(String filePath) {
        try {
            return FileTool.checkFolders(filePath);
        } catch (IOException ex) {
            // ignore
            return false;
        }
    }

    /**
     * Deletes a file or folder and its contents.
     *
     * @param dirPath folder path
     * @return the number of files that have failed to be deleted. 0 means that
     * all files are deleted.
     * @throws IOException
     */
    public static int deleteFileOrFolder(String dirPath) throws IOException {
        DeletingFileVisitor fv = new DeletingFileVisitor();
        Files.walkFileTree(Paths.get(dirPath), fv);
        return fv.getFailedToDelete().size();
    }

    /**
     * Deletes a file or folder and its contents.
     *
     * @param dirPath folder path
     * @return the number of files that have failed to be deleted. 0 for all
     * files are deleted -1 if error occurs.
     */
    public static int deleteFileOrFolderQuietly(String dirPath) {
        try {
            return deleteFileOrFolder(dirPath);
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * check the file if not existing.
     *
     * @param path
     * @return
     * @throws NoSuchFileException existing but not a file it is a directory.
     */
    public static boolean checkFile(String path) throws NoSuchFileException {
        return checkFile(Paths.get(path));
    }

    public static boolean checkFile(Path path) throws NoSuchFileException {
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                /**
                 * File is a directory throw exception.
                 */
                throw new NoSuchFileException("The file is existing but it is a directory.");
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Check a file if its already existing. this method will return false if
     * the file is already existing but a directory.
     *
     * this method will also delete the file if its a directory then return
     * false.
     *
     * @param path
     * @return true if its a file and it is existing.
     */
    public static boolean checkFileQuietly(String path) {
        try {
            return checkFile(path);
        } catch (NoSuchFileException e) {
            /**
             * Delete if its a directory.
             */
            deleteFileOrFolderQuietly(path);
            return false;
        }
    }

    /**
     * Deletes a file. if the file is a directory this will call
     * deleteFileOrFolder.
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
                int res = deleteFileOrFolder(path);
                return res == 0; // return true if res = 0;
            } else {
                // file is surely existing.
                return Files.deleteIfExists(filePath);
            }
        } else {
            // file not exist same as deleted.
            return true;
        }
    }

    /**
     * Deletes a file. if the file is a directory this will call
     * deleteFileOrFolder.
     *
     * @param path
     * @return
     */
    public static boolean deleteFileQuietly(String path) {
        try {
            return deleteFile(path);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Static File Visitor.
     */
    public static class DeletingFileVisitor extends SimpleFileVisitor<Path> {

        private final List<Path> failedToDelete;

        public DeletingFileVisitor() {
            super();
            this.failedToDelete = new ArrayList<>();
        }

        public List<Path> getFailedToDelete() {
            return failedToDelete;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            try {
                return super.preVisitDirectory(dir, attrs);
            } catch (IOException e) {
                // failed to visit this directory therefore failed to delete.
                this.failedToDelete.add(dir);
                return FileVisitResult.CONTINUE;
            }
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            // failed to visit
            this.failedToDelete.add(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                // failed to delete file
                this.failedToDelete.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            if (exc != null) {
                this.failedToDelete.add(dir);
            } else {
                try {
                    Files.delete(dir);
                } catch (IOException e) {
                    // failed to delete directory
                    this.failedToDelete.add(dir);
                }
            }
            return FileVisitResult.CONTINUE;
        }

    }

}
