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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * For systems running in network, it is more easy to use the Host Name of the
 * server rather than using a static IP.
 *
 * This class will get the host information and IP details from a remote host or
 * from this local machine.
 *
 * @author Jhon Melvin
 */
public class HostFinder {

    private String hostName;
    //
    private final List<HostData> ipv4List;
    private final List<HostData> ipv6List;

    public HostFinder() {
        this.hostName = ""; // blank will return loopback interface
        this.ipv4List = new ArrayList<>();
        this.ipv6List = new ArrayList<>();
    }

    /**
     * Sets the host to find. default or blank host will return the local
     * machine's information including loop back.
     *
     * @param hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Gets the list of all available IPv4 Addresses.
     *
     * @return
     */
    public List<HostData> getIpv4List() {
        return ipv4List;
    }

    /**
     * Gets the list of all available IPv6 Addresses.
     *
     * @return
     */
    public List<HostData> getIpv6List() {
        return ipv6List;
    }

    /**
     * Discovers the target host name and gathers information.
     *
     * @throws UnknownHostException
     * @throws SecurityException
     */
    public void find() throws UnknownHostException, SecurityException {
        this.ipv4List.clear();
        this.ipv6List.clear();
        //----------------------------------------------------------------------
        InetAddress[] host = InetAddress.getAllByName(this.hostName);
        for (InetAddress addr : host) {
            String hostAddress = addr.getHostAddress();
            if (hostAddress.contains(":")) {
                HostData data = new HostData(addr);
                this.ipv6List.add(data);
            } else if (hostAddress.contains(".")) {
                HostData data = new HostData(addr);
                this.ipv4List.add(data);
            }
        }

    }

    /**
     * Stores Host Data.
     */
    public static class HostData {

        private final InetAddress inetAddress;
        private final NetworkInterface networkInterface;

        /**
         * Default Constructor.
         *
         * @param inetAddress
         */
        private HostData(InetAddress inetAddress) {
            this.inetAddress = inetAddress;
            //------------------------------------------------------------------
            NetworkInterface localNI = null;
            try {
                localNI = NetworkInterface.getByInetAddress(inetAddress);
            } catch (SocketException ex) {
                // ignore
            } finally {
                this.networkInterface = localNI;
            }
            //------------------------------------------------------------------
        }

        /**
         * Get Address Information.
         *
         * @return
         */
        public InetAddress getInetAddress() {
            return inetAddress;
        }

        /**
         * Actual Name of the Hardware Device.
         *
         * @return
         */
        public String getInterfaceName() {
            if (this.networkInterface == null) {
                return "";
            }
            return this.networkInterface.getDisplayName();
        }

        /**
         * Code Representation of the interface.
         *
         * @return
         */
        public String getInterfaceCode() {
            if (this.networkInterface == null) {
                return "";
            }
            return this.networkInterface.getName();
        }

        /**
         * Usually the MAC address.
         *
         * @return
         */
        public String getInterfaceHardwareAddress() {
            if (this.networkInterface == null) {
                return "";
            }
            try {
                //--------------------------------------------------------------
                // translate to string
                byte[] mac = this.networkInterface.getHardwareAddress();
                StringBuilder sb = new StringBuilder(18);
                for (byte b : mac) {
                    if (sb.length() > 0) {
                        sb.append(':');
                    }
                    sb.append(String.format("%02x", b));
                }
                //--------------------------------------------------------------
                return sb.toString();
            } catch (SocketException ex) {
                return "";
            }
        }

    }

}
