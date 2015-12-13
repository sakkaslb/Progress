package progress.massoluciones.com.progress;

import android.content.Intent;
import android.massoluciones.com.progress.R;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView link = (TextView) findViewById(R.id.txtPrivacyPolicy);
        String linkText = "<a href='http://www.keeprogress.com'>Privacy Policy</a>";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());
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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

  /*  @Override
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
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.menu_btnGoals:{
                /*Toast.makeText(this,getResources().getText(R.string.menu_goals), Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(this,GoalsActivity.class);
                startActivity(vintent);*/
                break;
            }
            case R.id.menu_btnMemoryLane:{
                Toast.makeText(this,getResources().getText(R.string.menu_memory), Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(this, MemoryActivity.class);
                startActivity(vintent);
                break;
            }
            case R.id.menu_btnProgress:{
               /* Toast.makeText(this,getResources().getText(R.string.menu_progress), Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(this,ProgressActivity.class);
                startActivity(vintent);*/
                break;
            }
            case R.id.menu_btnSnapshot:{
                Toast.makeText(this,getResources().getText(R.string.menu_snapshot), Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(this, WeightActivity.class);
                startActivity(vintent);

                break;
            }
            case R.id.menu_btnSocial:{
                Toast.makeText(this,getResources().getText(R.string.menu_social), Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(this, SocialActivity.class);
                startActivity(vintent);
                break;
            }
        }
    }
}
