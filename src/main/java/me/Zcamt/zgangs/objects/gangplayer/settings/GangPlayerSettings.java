package me.Zcamt.zgangs.objects.gangplayer.settings;

import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;

public class GangPlayerSettings {

    private GangPlayer gangPlayer;
    private boolean receiveGangChat;
    private boolean receiveAllyChat;
    private boolean receiveMemberConnectNotification;
    private boolean receiveMemberDisconnectNotification;
    private boolean receiveAllyConnectNotification;
    private boolean receiveAllyDisconnectNotification;


    public GangPlayerSettings(boolean receiveGangChat, boolean receiveAllyChat, boolean receiveMemberConnectNotification, boolean receiveMemberDisconnectNotification, boolean receiveAllyConnectNotification, boolean receiveAllyDisconnectNotification) {
        this.receiveGangChat = receiveGangChat;
        this.receiveAllyChat = receiveAllyChat;
        this.receiveMemberConnectNotification = receiveMemberConnectNotification;
        this.receiveMemberDisconnectNotification = receiveMemberDisconnectNotification;
        this.receiveAllyConnectNotification = receiveAllyConnectNotification;
        this.receiveAllyDisconnectNotification = receiveAllyDisconnectNotification;
    }


    public void toggleReceiveGangChat() {
        this.receiveGangChat = !this.receiveGangChat;
        gangPlayer.serialize();
    }

    public void toggleReceiveAllyChat() {
        this.receiveAllyChat = !this.receiveAllyChat;
        gangPlayer.serialize();
    }

    public void toggleReceiveMemberConnectNotification() {
        this.receiveMemberConnectNotification = !this.receiveMemberConnectNotification;
        gangPlayer.serialize();
    }

    public void toggleReceiveMemberDisconnectNotification() {
        this.receiveMemberDisconnectNotification = !this.receiveMemberDisconnectNotification;
        gangPlayer.serialize();
    }

    public void toggleReceiveAllyConnectNotification() {
        this.receiveAllyConnectNotification = !this.receiveAllyConnectNotification;
        gangPlayer.serialize();
    }

    public void toggleReceiveAllyDisconnectNotification() {
        this.receiveAllyDisconnectNotification = !this.receiveAllyDisconnectNotification;
        gangPlayer.serialize();
    }


    public boolean isReceiveGangChat() {
        return receiveGangChat;
    }

    public boolean isReceiveAllyChat() {
        return receiveAllyChat;
    }

    public boolean isReceiveMemberConnectNotification() {
        return receiveMemberConnectNotification;
    }

    public boolean isReceiveMemberDisconnectNotification() {
        return receiveMemberDisconnectNotification;
    }

    public boolean isReceiveAllyConnectNotification() {
        return receiveAllyConnectNotification;
    }

    public boolean isReceiveAllyDisconnectNotification() {
        return receiveAllyDisconnectNotification;
    }

    public void setGangPlayer(GangPlayer gangPlayer) {
        if(gangPlayer != null) {
            this.gangPlayer = gangPlayer;
        }
    }
}
