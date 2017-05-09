package hust.hoangpt.restaurantclient;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketHelper {

    private static Socket mSocket;

    static {
        try {
            mSocket = IO.socket("http://192.168.1.119:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket loadSocket() {
        return mSocket;
    }
}
