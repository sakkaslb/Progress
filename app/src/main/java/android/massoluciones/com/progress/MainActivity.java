package android.massoluciones.com.progress;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnGoals=(Button) findViewById(R.id.menu_btnGoals);
        Button btnSnapshot=(Button) findViewById(R.id.menu_btnSnapshot);
        Button btnSocial=(Button) findViewById(R.id.menu_btnSocial);
        Button btnMemoryLane=(Button) findViewById(R.id.menu_btnMemoryLane);
        Button btnProgress=(Button) findViewById(R.id.menu_btnProgress);
        btnGoals.setOnClickListener(this);
        btnMemoryLane.setOnClickListener(this);
        btnSnapshot.setOnClickListener(this);
        btnSocial.setOnClickListener(this);
        btnProgress.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.menu_btnGoals:{
                Toast.makeText(this,"GOALS", Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(this,GoalsActivity.class);
                startActivity(vintent);
                break;
            }
            case R.id.menu_btnMemoryLane:{
                Toast.makeText(this,"MEMORY LANE", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_btnProgress:{
                Toast.makeText(this,"PROGRESS", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_btnSnapshot:{
                Toast.makeText(this,"SNAPSHOT", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_btnSocial:{
                Toast.makeText(this,"SOCIAL", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
