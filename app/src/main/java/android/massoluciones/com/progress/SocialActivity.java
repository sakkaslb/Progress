package android.massoluciones.com.progress;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SocialActivity extends Activity{
    Button btnFacebook, btnTwitter, btnInstagram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        btnFacebook=(Button) findViewById(R.id.btnSocialFacebook);
        btnTwitter=(Button) findViewById(R.id.btnSocialTwitter);
        btnInstagram=(Button) findViewById(R.id.btnSocialInstagram);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1623101651285178"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/keeprogress")));
                }
            }
        });
        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/keeprogress");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/keeprogress")));
                }
            }
        });
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("twitter://user?user_id=keeprogress_");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.twitter.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/keeprogress_")));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }
}
