package com.android.phoneagent.conroller;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.android.phoneagent.entities.DeviceContact;
import com.android.phoneagent.factory.AppFactory;
import com.android.phoneagent.utils.Logging;

import java.util.List;

public class CallListenerManager extends PhoneStateListener
{
    private AudioStateManager audioStateManager;
    private ContactManager contactManager;
    private CallLogManager callLogManager;
    private int currentRingerMode;
    private boolean ringerModeChangedFlag=false;
    private String lastCallNo;

    public CallListenerManager(Context context)
    {
        audioStateManager = new AudioStateManager(context);
        contactManager = AppFactory.getContactManager(context);
        callLogManager = new CallLogManager(context);
    }


    @Override
    public void onCallStateChanged (int state, String incomingNumber)
    {
        if(TelephonyManager.CALL_STATE_RINGING == state)
        {
            int code = getNumberStatus(incomingNumber);
            lastCallNo=incomingNumber;
            Logging.debug("CallListener: Incoming call from " + incomingNumber + ", contactStatus: " + code);

            if(code==0)
                return;

            currentRingerMode = audioStateManager.getRingerMode();
            audioStateManager.changeRingerMode(code);
            callLogManager.setAirplaneMode(true);
            ringerModeChangedFlag=true;
        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state)
        {
            Logging.debug("CallListener: Outgoing");
        }
        if(TelephonyManager.CALL_STATE_IDLE == state && ringerModeChangedFlag)
        {
            audioStateManager.resetRingerMode(currentRingerMode);
            Logging.debug("Ringer reset to: " + currentRingerMode);
            ringerModeChangedFlag=false;
            callLogManager.deleteCallLogs(lastCallNo);
            callLogManager.setAirplaneMode(false);
        }
    }

    private int getNumberStatus(String incomingNo)
    {
        List<DeviceContact> list = contactManager.getRestrictedContactList();
        for(DeviceContact contact: list)
        {
            Logging.debug("Restricted: " + contact.getContactNumber());
            if(incomingNo.contains(contact.getContactLastDigitsFromNumber()))
            {
                return -1;
            }
        }

        list = contactManager.getFavouriteList();
        for(DeviceContact contact: list)
        {
            Logging.debug("Favourite: " + contact.getContactNumber());
            if(incomingNo.contains(contact.getContactLastDigitsFromNumber()))
            {
                return 1;
            }
        }

        return 0;
    }
}