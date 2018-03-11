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
package org.afterschoolcreatives.polaris.java.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Jhon Melvin
 */
public class AddressResolver {

    private int timeout = 2500;
    private int maxConcurrentSearch = 50;
    private String subnet = "192.168.254";

    public void search() {
        SearchHostThreadManager threadManager = new SearchHostThreadManager(maxConcurrentSearch);
        for (int x = 1; x < 255; x++) {
            String host = subnet + "." + x;
            SearchHostThread ahd = new SearchHostThread();
            ahd.setTimeout(timeout);
            ahd.setHost(host);

            threadManager.addThreadQuery(ahd);
        }
        threadManager.resolveAddresses();
    }

    public static void main(String[] args) {
        try {
            String localHostName = InetAddress.getLocalHost().getHostName();
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.getHostName().equals(localHostName)) {
                        if (i.getHostAddress().contains(":")) {

                        } else {
                            System.out.println(i.getHostName() + " -> " + i.getHostAddress());
                        }

                    }

                }
            }
        } catch (Exception e) {
        }

//        AddressResolver ar = new AddressResolver();
//        ar.search();
    }

    //--------------------------------------------------------------------------
    /**
     * Manages the Search Host Threads.
     */
    private final static class SearchHostThreadManager {

        private final ArrayList<SearchHostThread> threadList;
        private final AtomicInteger activeThreadCount;
        private final AtomicInteger finishedThreadCount;
        private final List<String> activeHost;
        /**
         * Maximum number of concurrent threads to be spawn to check for active
         * host.
         */
        private final int maxConcurrentThreads;

        /**
         * Default Constructor.
         */
        private SearchHostThreadManager(int concurrent) {
            this.threadList = new ArrayList<>();
            this.activeThreadCount = new AtomicInteger(0);
            this.finishedThreadCount = new AtomicInteger(0);
            this.activeHost = Collections.synchronizedList(new ArrayList());
            /**
             * MAX CONCURRENCY.
             */
            this.maxConcurrentThreads = concurrent; // DEFAULT MAX
        }

        public void addThreadQuery(SearchHostThread thread) {
            this.threadList.add(thread);
        }

        public void resolveAddresses() {
            /**
             * Create a thread monitor.
             */
            final Thread factory = new Thread(() -> {
                /**
                 * Iterate all thread queries.
                 */
                for (SearchHostThread searchHostThread : threadList) {
                    //----------------------------------------------------------
                    // set callback method when finished
                    searchHostThread.setCallback((hostRes, online) -> {
                        //System.out.println(hostRes + "\t->\t" + online);
                        this.activeThreadCount.decrementAndGet();
                        // increment finished thread.
                        this.finishedThreadCount.incrementAndGet();
                        if (online) {
                            this.activeHost.add(hostRes);
                        }
                    });
                    //----------------------------------------------------------
                    /**
                     * check current active thread count. pause until some
                     * threads are dead.
                     */
                    while (true) {
                        if (this.activeThreadCount.get() <= this.maxConcurrentThreads) {
                            break;
                        } else {
                            //System.out.println("WAITING FOR OTHER THREADS TO DISSOLVE");
                        }
                        // pause for sometime
                        try {
                            Thread.sleep(528);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    //----------------------------------------------------------
                    searchHostThread.start();
                    this.activeThreadCount.getAndIncrement();
                    //----------------------------------------------------------
                }
            });

            final Thread monitor = new Thread(() -> {
//                final int size = this.threadList.size();
//                while (true) {
//                    if (size == this.finishedThreadCount.get()) {
//                        break;
//                    }
//                    // pause for sometime
//                    try {
//                        Thread.sleep(918);
//                    } catch (InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
                while (factory.isAlive()) {
                    // pause for sometime
                    try {
                        Thread.sleep(918);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                //--------------------------------------------------------------
                // Do Something Here.
                System.out.println("LIST OF ACTIVE HOST");
                this.activeHost.forEach(action -> {
                    System.out.println(action);
                });
                //--------------------------------------------------------------
            });
            //------------------------------------------------------------------
            factory.setName("sht-factory-thread");
            factory.start();
            //------------------------------------------------------------------
            monitor.setName("sht-monitor-thread");
            monitor.start();
        }

    }

    /**
     * Non-Blocking thread to send ping request to a specific host.
     */
    private final static class SearchHostThread extends Thread {

        /**
         * Call back interface to execute command after ping.
         */
        @FunctionalInterface
        public interface SearchHostCallback {

            /**
             * Method.
             *
             * @param host destination host.
             * @param online host reachable state.
             */
            void afterSearch(String host, boolean online);
        }

        /**
         * Destination host.
         */
        private String host;
        /**
         * Timeout to wait.
         */
        private int timeout;
        /**
         * Callback variable.
         */
        private SearchHostCallback callback;

        /**
         * Default constructor with default values.
         */
        private SearchHostThread() {
            this.timeout = 1000;
            this.setDaemon(true);
            this.setName("sht-thread");
            this.callback = (host, online) -> {
                // do nothing
            };
        }

        /**
         * sets the host.
         *
         * @param host
         */
        public void setHost(String host) {
            this.host = host;
        }

        /**
         * Overrides the default timeout of 1000 mills.
         *
         * @param timeout
         */
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        /**
         * Assigns a new callback method.
         *
         * @param callback
         */
        public void setCallback(SearchHostCallback callback) {
            this.callback = callback;
        }

        /**
         * Run.
         */
        @Override
        public void run() {
            boolean hostOnline = false;
            try {
                hostOnline = InetAddress.getByName(host).isReachable(timeout);
            } catch (IOException ex) {
                // ignore exception
                hostOnline = false;
            } finally {
                // do something
                this.callback.afterSearch(this.host, hostOnline);
            }
        }

    }
}
