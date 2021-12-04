package com.workhub.commons.utils;

import de.bytefish.pgbulkinsert.util.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Slf4j
public final class InetAddressUtil {
    private static final String DEFAULT_IP_ADDRESS = "0.0.0.0";
    private static final String DEFAULT_HOST = "unknown";
    @Getter
    private static final String SERVER_IP;
    @Getter
    private static final String SERVER_HOST;

    static {
        String serverIp = DEFAULT_IP_ADDRESS;
        try {
            serverIp = getLocalIpAsString();
        } catch (Exception e) {
            log.warn("Failed to identify local IP address.", e);
        }
        SERVER_IP = serverIp;

        String serverHost = DEFAULT_HOST;
        try {
            serverHost = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Failed to identify local host name.", e);
        }
        SERVER_HOST = serverHost;
    }

    private static String getLocalIpAsString() throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces != null && interfaces.hasMoreElements()) {
            Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
            while (addresses != null && addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (isValidAddress(address)) {
                    return address.getHostAddress();
                }
            }
        }
        throw new UnknownHostException();
    }

    private static boolean isValidAddress(InetAddress address) {
        return address != null
            && !address.isLoopbackAddress()
            && !address.isAnyLocalAddress()
            && !address.isLinkLocalAddress();
    }

    public static String getHostOrIp() {
        if (SERVER_HOST.equals(DEFAULT_HOST) || StringUtils.isNullOrWhiteSpace(SERVER_HOST)) {
            return SERVER_IP;
        }
        return SERVER_HOST;
    }

}
