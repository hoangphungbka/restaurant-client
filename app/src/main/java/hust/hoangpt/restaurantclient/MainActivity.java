package hust.hoangpt.restaurantclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private RadioGroup ragRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        edtUsername = (EditText) this.findViewById(R.id.edtUsername);
        edtPassword = (EditText) this.findViewById(R.id.edtPassword);
        ragRoles = (RadioGroup) this.findViewById(R.id.ragRoles);
    }

    public void actionLogin(View view) {
        String username = edtUsername.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be blank!", Toast.LENGTH_LONG).show();
            return;
        }
        String password = edtPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be blank!", Toast.LENGTH_LONG).show();
            return;
        }
        int idChecked = ragRoles.getCheckedRadioButtonId();
        String status = "0";
        switch (idChecked) {
            case R.id.rabWaiter: status = "1"; break;
            case R.id.rabChef: status = "2"; break;
        }
        (new LoginAsyncTask(this)).execute(username, password, status);
    }
}
