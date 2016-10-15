package ui.dialog;
import android.app.Activity;
import com.afollestad.materialdialogs.MaterialDialog;
/**
 * Created by sandstranger on 14.10.16.
 */

public class MaterialDialogManager {
    private Activity activity;

    public MaterialDialogManager(Activity activity) {
        this.activity = activity;
    }

    public void showMessageDialogBox(String title, String message, String closeButtonName) {
        new MaterialDialog.Builder(activity)
                .title(title)
                .content(message)
                .negativeText(closeButtonName).show();
    }

    public void showDialog(String title, String message, String negativeButtonText, String positiveButtonText, MaterialDialogInterface dialogInterface) {
        new MaterialDialog.Builder(activity)
                .content(message)
                .positiveText(positiveButtonText)
                .negativeText(negativeButtonText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialogInterface.onPositiveButtonPressed();
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialogInterface.onNegativeButtonPressed();
                    }
                }).show();
    }
}
