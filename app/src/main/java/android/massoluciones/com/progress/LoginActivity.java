package android.massoluciones.com.progress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnRegister=(Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        TextView txtLogin=(TextView) findViewById(R.id.registro_btnAlreadySigned);
        txtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnRegister:{
                Intent mainIntent = new Intent().setClass(
                        LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                break;
            }
            case R.id.registro_btnAlreadySigned:{
                final Dialog login = new Dialog(this);
                login.setContentView(R.layout.login_dialog);
                login.setTitle(R.string.login_title);
                Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
                Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
                final EditText txtUsername = (EditText)login.findViewById(R.id.txtUsername);
                final EditText txtPassword = (EditText)login.findViewById(R.id.txtPassword);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this,"click en login", Toast.LENGTH_SHORT).show();
                        login.dismiss();

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this,"click en cancelar", Toast.LENGTH_SHORT).show();
                        login.dismiss();
                    }
                });
                login.show();
                break;
            }
        }
    }
}
