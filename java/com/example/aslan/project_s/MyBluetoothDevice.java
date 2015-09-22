package com.example.aslan.project_s;

/**
 * Created by caozj on 15/7/18.
 */
public class MyBluetoothDevice {
    public enum server_or_client{
        CLIENT,
        NONE,
        SERVER
    }

    public static server_or_client serverOrClient = server_or_client.NONE;
    public static String bluetooth_Mac = null;
    public boolean is_open = false;
}
