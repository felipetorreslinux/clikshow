package com.clikshow.ClikBIlheteria.Models;

import android.bluetooth.BluetoothDevice;

public class ImpressorasModel {
    private String name;
    private String mac;

    public ImpressorasModel(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
