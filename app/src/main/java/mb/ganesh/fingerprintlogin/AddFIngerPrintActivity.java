package mb.ganesh.fingerprintlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AddFIngerPrintActivity extends AppCompatActivity {

    SwitchCompat switchBtn;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;

    String TAG = "BIOINFO";

    //SharedPref
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    final String KEY = "FP_INFO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_finger_print);

        switchBtn = findViewById(R.id.switchBtnId);
        switchBtn.setChecked(loadData(KEY));
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchBtn.isChecked()){
                    biometricPrompt.authenticate(promptInfo);
                }else {
                    saveData(KEY , false);
                    switchBtn.setChecked(false);
                }
            }
        });

        executor = ContextCompat.getMainExecutor(AddFIngerPrintActivity.this);

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                switchBtn.setChecked(false);
                Log.e(TAG, "Error : " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                switchBtn.setChecked(true);
                saveData(KEY , true);
                gotoMain();
                Log.d(TAG, "Success");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                switchBtn.setChecked(false);
                Log.e(TAG, "Failed");
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("FingerPrint Demo")
                .setSubtitle("Login with Fingerprint")
                .setNegativeButtonText("Cancel")
                .build();


    }

    private boolean loadData(String key) {
        sp = getSharedPreferences(KEY , MODE_PRIVATE);
        return sp.getBoolean(KEY , false);
    }

    private void saveData(String key, boolean value) {
        sp = getSharedPreferences(KEY , MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean(KEY , value);
        editor.apply();

    }

    private void gotoMain() {
        Intent intent = new Intent(AddFIngerPrintActivity.this , HomeActivity.class);
        intent.putExtra("FP" , true);
        startActivity(intent);
        finish();
    }


}