package mb.ganesh.fingerprintlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import com.google.android.material.button.MaterialButton;


public class HomeActivity extends AppCompatActivity {

    MaterialButton addBtn;

    boolean isChecked = false;


    //SharedPref
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    final String KEY = "FP_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addBtn = findViewById(R.id.addFingerPrintBtnId);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this , AddFIngerPrintActivity.class));
                finish();
            }
        });

    }

    private void checkSysReq() {
        BiometricManager biometricManager = BiometricManager.from(HomeActivity.this);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                addBtn.setEnabled(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                addBtn.setEnabled(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                addBtn.setEnabled(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                addBtn.setEnabled(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                addBtn.setEnabled(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                addBtn.setEnabled(false);
                break;
            case  BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                addBtn.setEnabled(false);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkSysReq();

        isChecked = getIntent().getBooleanExtra("FP" , false);
        isChecked = loadData(KEY);
        if (isChecked){
            saveData(KEY , true);
            startActivity(new Intent(HomeActivity.this , LoginActivity.class));
            finish();
        }else {
            saveData(KEY , false);
            return;
        }
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

}