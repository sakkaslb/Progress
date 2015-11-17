package android.massoluciones.com.progress;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class WeightActivity extends Activity {
    EditText txtWeight;
    Spinner cmbMeasure;
    Button btnWeight;
    Integer weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weightcheckin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txtWeight=(EditText) findViewById(R.id.txtWeight);
        cmbMeasure=(Spinner) findViewById(R.id.cmbMeasure);
        btnWeight=(Button)findViewById(R.id.btnWeightCheck);
        ArrayAdapter<CharSequence> adaptador=ArrayAdapter.createFromResource(this, R.array.list_measure, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbMeasure.setAdapter(adaptador);
        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }
}
