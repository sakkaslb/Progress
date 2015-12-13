package progress.massoluciones.com.progress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.massoluciones.com.progress.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WeightActivity extends Activity {
    EditText txtWeight;
    TextView cmbMeasure;
    Button btnWeight;
    Integer weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weightcheckin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txtWeight=(EditText) findViewById(R.id.txtWeight);
        cmbMeasure=(TextView) findViewById(R.id.txtMeasure);
        btnWeight=(Button)findViewById(R.id.btnWeightCheck);
        SharedPreferences prefs=getSharedPreferences("progressPrefs", Context.MODE_PRIVATE);
        String measure=prefs.getString("medida","lbs");
        cmbMeasure.setText(measure);
        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    weight=Integer.parseInt(txtWeight.getText().toString());
                    if (weight!=null && weight>0 &&weight<600){
                        Intent vintent=new Intent().setClass(WeightActivity.this,SnapshotActivity.class);
                        Bundle b=new Bundle();
                        b.putInt("peso",weight);
                        vintent.putExtras(b);
                        startActivity(vintent);
                        finish();
                    } else {

                        Toast.makeText(WeightActivity.this,getResources().getString(R.string.weight_validation),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(WeightActivity.this,getResources().getString(R.string.weight_validation),Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
