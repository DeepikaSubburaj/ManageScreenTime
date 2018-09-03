package com.example.android.screentimeout;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

/***
 *  Enable the Device Administrator when user uses the app for the first time
 *  Set the CountDown timer
 *
 */
public class SetScreenTimeActivity extends AppCompatActivity{

    private Button clickedButton;
    private TimePicker time_picker;
    private int hour;
    private int min;
    DevicePolicyManager deviceManger;
    ComponentName compName;
    protected static final int REQUEST_ADMIN_ACTIVE = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_screen_time);
        initViews();
        intitListeners();

}

    private void initViews() {

        clickedButton = (Button) findViewById(R.id.startButton);
        time_picker = (TimePicker) findViewById(R.id.tp);
        time_picker.setIs24HourView(true);
        time_picker.setHour(0);
        time_picker.setMinute(0);
        //lock screen
        compName = new ComponentName(this, AdminReceiver.class);
        deviceManger = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);


    }


    private void intitListeners() {
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean deviceAdmin = deviceManger.isAdminActive(compName);

                if (!deviceAdmin) {

                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "To lock screen");
                    intent.putExtra("force-locked", DeviceAdminInfo.USES_POLICY_FORCE_LOCK);
                    startActivityForResult(intent, REQUEST_ADMIN_ACTIVE);

                } else {
                    hour = time_picker.getHour();
                    min = time_picker.getMinute();
                    System.out.println("hr:" + hour);
                    System.out.println("min:" + min);

                    if (hour == 0 && min == 0) {
                        Toast.makeText(getApplicationContext(), "Please set the timer", Toast.LENGTH_LONG).show();

                    }

                    Intent intent_showCountDown = new Intent(getApplicationContext(), ShowCountdownActivity.class);
                    intent_showCountDown.putExtra("HOUR", hour);
                    intent_showCountDown.putExtra("MIN", min);
                    startActivity(intent_showCountDown);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ADMIN_ACTIVE) {

            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Please Enable Device Admin", Toast.LENGTH_LONG).show();
            }
        }

    }


}

