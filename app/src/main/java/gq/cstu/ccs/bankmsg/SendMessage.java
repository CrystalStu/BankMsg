package gq.cstu.ccs.bankmsg;

import android.telephony.SmsManager;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendMessage {

    public static void BulkSending(View v) {
        Tools.messageSending(v);
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("MMdd").format(cDate);
        CSV.Guest[] gu = CSV.guests;
        for(int t = 0; t < gu.length; t++) {
            if(gu[t].birthday.equals(fDate)) {
                SendToClient(gu[t].name, gu[t].phone);
            }
        }
        Tools.messageSent(v);
    }

    private static void SendToClient(String name, String phone) {
        String message = name + "总，您好！我是，在这特别的日子里祝您生日快乐！祝福您事业顺利、阖家幸福安康！";
        Send(message, phone);
    }

    private static void Send(String message, String phone) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, message, null, null);
    }
}
