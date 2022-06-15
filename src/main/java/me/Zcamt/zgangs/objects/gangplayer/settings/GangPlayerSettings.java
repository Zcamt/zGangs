package me.Zcamt.zgangs.objects.gangplayer.settings;

import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;

public class GangPlayerSettings {

    private GangPlayer gangPlayer;
    private boolean receiveGangChat;
    private boolean receiveAllyChat;
    private boolean receiveMemberConnectNotification;
    private boolean isReceiveMemberDisconnectNotification;

    public GangPlayerSettings(boolean receiveGangChat, boolean receiveAllyChat, boolean receiveMemberConnectNotification, boolean isReceiveMemberDisconnectNotification) {
        this.receiveGangChat = receiveGangChat;
        this.receiveAllyChat = receiveAllyChat;
        this.receiveMemberConnectNotification = receiveMemberConnectNotification;
        this.isReceiveMemberDisconnectNotification = isReceiveMemberDisconnectNotification;
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
        this.isReceiveMemberDisconnectNotification = !this.isReceiveMemberDisconnectNotification;
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
        return isReceiveMemberDisconnectNotification;
    }

    public void setGangPlayer(GangPlayer gangPlayer) {
        if(gangPlayer != null) {
            this.gangPlayer = gangPlayer;
        }
    }
}
