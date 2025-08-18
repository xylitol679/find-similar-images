package com.ocbg.xyz.apps.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

object NetworkUtils {

    /**
     * Get the IP address of the device.
     *
     * @param context The application context.
     * @return The IP address of the device, or null if not found.
     */
    fun getIPAddress(context: Context): String? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected) {
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_MOBILE -> { // Current network is mobile data
                    try {
                        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                        while (networkInterfaces.hasMoreElements()) {
                            val intf = networkInterfaces.nextElement()
                            val enumIpAddr = intf.inetAddresses
                            while (enumIpAddr.hasMoreElements()) {
                                val inetAddress = enumIpAddr.nextElement()
                                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                    return inetAddress.hostAddress
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                ConnectivityManager.TYPE_WIFI -> { // Current network is Wi-Fi
                    val wifiManager =
                        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val ipAddress = wifiManager.connectionInfo.ipAddress
                    return String.format(
                        "%d.%d.%d.%d",
                        ipAddress and 0xFF,
                        ipAddress shr 8 and 0xFF,
                        ipAddress shr 16 and 0xFF,
                        ipAddress shr 24 and 0xFF
                    )
                }
            }
        }
        return null
    }

    /**
     * Get the subnet mask of the current network.
     * @param context The application context.
     * @return The subnet mask string (e.g., "255.255.255.0") or null if not found.
     */
    fun getSubnetMask(context: Context): String? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return null
        val linkProperties = connectivityManager.getLinkProperties(activeNetwork) ?: return null

        for (linkAddress in linkProperties.linkAddresses) {
            if (linkAddress.address is Inet4Address) {
                // The prefix length gives us the number of '1's in the subnet mask.
                val prefixLength = linkAddress.prefixLength
                val mask = -1 shl (32 - prefixLength)
                val bytes = byteArrayOf(
                    (mask ushr 24).toByte(),
                    (mask ushr 16).toByte(),
                    (mask ushr 8).toByte(),
                    mask.toByte()
                )
                return InetAddress.getByAddress(bytes).hostAddress
            }
        }
        return null
    }

    /**
     * Checks if two IP addresses are in the same subnet.
     * @param ip1 The first IP address.
     * @param ip2 The second IP address.
     * @param subnetMask The subnet mask.
     * @return True if they are in the same subnet, false otherwise.
     */
    fun areInSameSubnet(ip1: String, ip2: String, subnetMask: String): Boolean {
        return try {
            val ip1Addr = ipToLong(ip1)
            val ip2Addr = ipToLong(ip2)
            val maskAddr = ipToLong(subnetMask)

            (ip1Addr and maskAddr) == (ip2Addr and maskAddr)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun ipToLong(ipAddress: String): Long {
        var result: Long = 0
        val ipParts = ipAddress.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in 0..3) {
            result = result shl 8
            result = result or ipParts[i].toLong()
        }
        return result
    }

}