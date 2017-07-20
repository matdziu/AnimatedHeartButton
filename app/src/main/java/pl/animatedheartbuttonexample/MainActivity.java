package pl.animatedheartbuttonexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimatedHeartButton animatedHeartButton = findViewById(R.id.animated_heart_button);
        animatedHeartButton.setChecked(true, false);
        animatedHeartButton.setOnHeartButtonCheckedChangeListener(isChecked -> {
            Toast.makeText(this, "isChecked = " + isChecked, Toast.LENGTH_SHORT).show();
        });
    }
}
