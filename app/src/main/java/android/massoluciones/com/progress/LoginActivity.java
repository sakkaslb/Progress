package android.massoluciones.com.progress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener{
    String nombre, email, medida;
    EditText txtNombre, txtEmail;
    Spinner cmbMeasure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnRegister=(Button) findViewById(R.id.btnRegister);
        txtNombre=(EditText) findViewById(R.id.registro_txtNombre);
        txtEmail=(EditText) findViewById(R.id.registro_txtEmail);
        btnRegister.setOnClickListener(this);
       /* TextView txtLogin=(TextView) findViewById(R.id.registro_btnAlreadySigned);
        txtLogin.setOnClickListener(this);*/
        ArrayAdapter<CharSequence> adaptador=ArrayAdapter.createFromResource(this, R.array.list_measure, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbMeasure=(Spinner) findViewById(R.id.cmbRegistroMeasure);
        cmbMeasure.setAdapter(adaptador);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnRegister:{
                nombre=txtNombre.getText().toString();
                email=txtEmail.getText().toString();
                medida=cmbMeasure.getSelectedItem().toString();
                if (nombre.length()>0 && email.length()>0 && isValidEmail(email)){
                    SharedPreferences prefs = this.getSharedPreferences("progressPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("nombre", nombre);
                    editor.putString("email",email);
                    editor.putString("medida",medida);
                    editor.commit();
                    Intent mainIntent = new Intent().setClass(
                            LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else{
                    Toast.makeText(this,R.string.register_CreateUser,Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.registro_btnAlreadySigned:{
               /* final Dialo
               g login = new Dialog(this);
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
                login.show();*/
                break;
            }
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
