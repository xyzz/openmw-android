package ui.controls;

import com.libopenmw.openmw.R;

import constants.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class QuickPanel {

    private Activity a;
    public Button f1;
    public Button showPanel;
    public ImageButton key0;
    public ImageButton key1;
    public ImageButton key2;
    public ImageButton key3;
    public ImageButton key4;
    public ImageButton key5;
    public ImageButton key6;
    public ImageButton key7;
    public ImageButton key8;
    public ImageButton key9;
    public boolean enablePanel;

    private static QuickPanel instance = null;

    public QuickPanel(Activity a) {
        super();
        this.a = a;
        instance = this;

    }

    public static QuickPanel getInstance() {

        return instance;
    }

    public void showQuickPanel(boolean hide) {

        enablePanel = false;
        if (!hide) {
            f1 = (Button) a.findViewById(R.id.F1);
            showPanel = (Button) a.findViewById(R.id.showQuickPanel);
            key0 = (ImageButton) a.findViewById(R.id.key0);
            key1 = (ImageButton) a.findViewById(R.id.key1);
            key2 = (ImageButton) a.findViewById(R.id.key2);
            key3 = (ImageButton) a.findViewById(R.id.key3);
            key4 = (ImageButton) a.findViewById(R.id.key4);
            key5 = (ImageButton) a.findViewById(R.id.key5);
            key6 = (ImageButton) a.findViewById(R.id.key6);
            key7 = (ImageButton) a.findViewById(R.id.key7);
            key8 = (ImageButton) a.findViewById(R.id.key8);
            key9 = (ImageButton) a.findViewById(R.id.key9);
            setPanelParams();
            setPanelKeys();
            setPanelGone();

        }
    }

    private void setPanelGone() {
        f1.setVisibility(Button.GONE);
        key0.setVisibility(ImageButton.GONE);
        key1.setVisibility(ImageButton.GONE);
        key2.setVisibility(ImageButton.GONE);
        key3.setVisibility(ImageButton.GONE);
        key4.setVisibility(ImageButton.GONE);
        key5.setVisibility(ImageButton.GONE);
        key6.setVisibility(ImageButton.GONE);
        key7.setVisibility(ImageButton.GONE);
        key8.setVisibility(ImageButton.GONE);
        key9.setVisibility(ImageButton.GONE);
    }

    private void setPanelVisible() {
        f1.setVisibility(Button.VISIBLE);
        key0.setVisibility(ImageButton.VISIBLE);
        key1.setVisibility(ImageButton.VISIBLE);
        key2.setVisibility(ImageButton.VISIBLE);
        key3.setVisibility(ImageButton.VISIBLE);
        key4.setVisibility(ImageButton.VISIBLE);
        key5.setVisibility(ImageButton.VISIBLE);
        key6.setVisibility(ImageButton.VISIBLE);
        key7.setVisibility(ImageButton.VISIBLE);
        key8.setVisibility(ImageButton.VISIBLE);
        key9.setVisibility(ImageButton.VISIBLE);
    }

    private void setPanelKeys() {

        showPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!enablePanel) {
                            enablePanel = true;
                            setPanelVisible();
                        } else {
                            enablePanel = false;
                            setPanelGone();

                        }
                        return true;
                }
                return false;
            }
        });

        f1.setOnTouchListener(new ButtonTouchListener(131));
        key0.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_0));
        key1.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_1));
        key2.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_2));
        key3.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_3));
        key4.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_4));
        key5.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_5));
        key6.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_6));
        key7.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_7));
        key8.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_8));
        key9.setOnTouchListener(new ButtonTouchListener(KeyEvent.KEYCODE_9));

    }

    private void setPanelParams() {
        int controlsFlag;
        SharedPreferences Settings;

        Settings = a.getSharedPreferences(Constants.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        controlsFlag = Settings.getInt(
                Constants.APP_PREFERENCES_RESET_CONTROLS, -1);

        if (controlsFlag == -1 || controlsFlag == 1) {

            showPanel.setLayoutParams(ControlsParams.coordinates(showPanel, 68,
                    0, 65, 65));
            f1.setLayoutParams(ControlsParams.coordinates(f1, 68, 95, 55, 55));
            key0.setLayoutParams(ControlsParams.coordinates(key0, 0, 0, 55, 55));
            key1.setLayoutParams(ControlsParams
                    .coordinates(key1, 0, 75, 55, 55));
            key2.setLayoutParams(ControlsParams.coordinates(key2, 0, 150, 55,
                    55));
            key3.setLayoutParams(ControlsParams.coordinates(key3, 0, 225, 55,
                    55));
            key4.setLayoutParams(ControlsParams.coordinates(key4, 0, 300, 55,
                    55));
            key5.setLayoutParams(ControlsParams.coordinates(key5, 0, 375, 55,
                    55));
            key6.setLayoutParams(ControlsParams.coordinates(key6, 0, 450, 55,
                    55));
            key7.setLayoutParams(ControlsParams.coordinates(key7, 0, 525, 55,
                    55));
            key8.setLayoutParams(ControlsParams.coordinates(key8, 0, 600, 55,
                    55));
            key9.setLayoutParams(ControlsParams.coordinates(key9, 0, 675, 55,
                    55));

            AlphaView.setAlphaForView(showPanel, 0.5f);
            AlphaView.setAlphaForView(f1, 0.5f);
            AlphaView.setAlphaForView(key0, 1.0f);
            AlphaView.setAlphaForView(key1, 1.0f);
            AlphaView.setAlphaForView(key2, 1.0f);
            AlphaView.setAlphaForView(key3, 1.0f);
            AlphaView.setAlphaForView(key5, 1.0f);
            AlphaView.setAlphaForView(key6, 1.0f);
            AlphaView.setAlphaForView(key7, 1.0f);
            AlphaView.setAlphaForView(key8, 1.0f);
            AlphaView.setAlphaForView(key9, 1.0f);

        } else if (controlsFlag == 0) {

            AlphaView.setAlphaForView(f1, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_F1_OPACITY), 0.5f));

            AlphaView.setAlphaForView(showPanel, Settings
                    .getFloat(
                            (Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_OPASITY),
                            0.5f));

            AlphaView.setAlphaForView(key0, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_0_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key1, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_1_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key2, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_2_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key3, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_3_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key4, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_4_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key5, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_5_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key6, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_6_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key7, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_7_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key8, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_8_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key9, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_9_OPACITY), 1.0f));

            f1.setLayoutParams(ControlsParams.coordinatesConfigureControls(f1,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_SIZE, -1)));

            showPanel
                    .setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            showPanel,
                            Settings.getInt(
                                    Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_X,
                                    -1),
                            Settings.getInt(
                                    Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_Y,
                                    -1),
                            Settings.getInt(
                                    Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE,
                                    -1),
                            Settings.getInt(
                                    Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE,
                                    -1)));

            key0.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key0,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_0_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_0_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_0_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_0_SIZE, -1)));

            key1.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key1,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_1_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_1_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_1_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_1_SIZE, -1)));

            key2.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key2,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_2_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_2_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_2_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_2_SIZE, -1)));

            key3.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key3,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_3_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_3_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_3_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_3_SIZE, -1)));

            key4.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key4,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_4_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_4_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_4_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_4_SIZE, -1)));

            key5.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key5,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_5_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_5_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_5_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_5_SIZE, -1)));

            key6.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key6,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_6_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_6_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_6_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_6_SIZE, -1)));

            key7.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key7,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_7_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_7_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_7_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_7_SIZE, -1)));

            key8.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key8,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_8_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_8_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_8_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_8_SIZE, -1)));

            key9.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                    key9,
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_9_X, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_9_Y, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_9_SIZE, -1),
                    Settings.getInt(Constants.APP_PREFERENCES_KEY_9_SIZE, -1)));

        }
    }
}
