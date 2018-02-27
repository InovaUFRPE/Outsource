package com.outsource.inovaufrpe.usuario.solicitante.gui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;
import com.outsource.inovaufrpe.usuario.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button login;
    private EditText etCodigo;
    private EditText etTelefone;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String pNome;
    private String uNome;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(com.outsource.inovaufrpe.usuario.R.layout.activity_login);
        checaSessao();
        etTelefone = findViewById(R.id.etTelefoneID);
        etCodigo = findViewById(R.id.etCodigoID);
        login = findViewById(R.id.btLogarID);
        loginButton = findViewById(R.id.btFacebookID);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        etCodigo.setVisibility(View.INVISIBLE);
        login.setOnClickListener(this);
        loginButton.setOnClickListener(this);

    }

    private void checaSessao() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                    "Carregando dados...", true);
            DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("usuario");
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    } else {
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        dialog.dismiss();
                    }
                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
            });
        }
    }



                private void LoginFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        getUserDetailsFromFB(token);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Utils.criarToast(LoginActivity.this, "Logado com sucesso");
                            usuarioLogado();
                        } else {
                            // If sign in fails, display a message to the user.
                            Utils.criarToast(LoginActivity.this, "Ocorreu um erro na autenticação");
                        }
                    }
                });
    }

    private void logarComCredential() {
        String code = etCodigo.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        loginCelular(credential);
    }

    private void logar() {
        if (etTelefone.getText().toString().trim().length() != 0) {
            String numero = "+55" + etTelefone.getText().toString();
            cadastrartelefone(numero);
        } else {
            etTelefone.setError("Número inválido");
        }
    }


    private void cadastrartelefone(String phoneNumber) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);

                loginCelular(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number dinheiroformat is not valid.
                Log.w("TAG", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Utils.criarToast(LoginActivity.this, "Formato de Telefone Inválido");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Utils.criarToast(LoginActivity.this, "Erro código 002");
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                etCodigo.setVisibility(View.VISIBLE);
                etTelefone.setVisibility(View.INVISIBLE);
                login.setText(R.string.confirmar_codigo);
            }

        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void loginCelular(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Utils.criarToast(LoginActivity.this, "Logado com sucesso");
                            usuarioLogado();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void usuarioLogado() {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("usuario");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Intent it = new Intent(LoginActivity.this, CadastroActivity.class);
                    it.putExtra("pNome", pNome);
                    it.putExtra("uNome", uNome);
                    it.putExtra("email", email);
                    finish();
                    startActivity(it);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btLogarID:
                if (etCodigo.getVisibility() == View.VISIBLE) {
                    logarComCredential();
                } else {
                    logar();
                }
                break;
            case R.id.btFacebookID:
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        LoginFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Utils.criarToast(LoginActivity.this, "Falha ao logar: " + error.getMessage());
                    }
                });
                break;
        }
    }

    public void getUserDetailsFromFB(AccessToken accessToken) {

        GraphRequest req=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try{
                    email =  object.getString("email");
                    pNome = object.getString("first_name");
                    uNome = object.getString("last_name");

                }catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(),"graph request error : "+e.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,first_name,last_name");
        req.setParameters(parameters);
        req.executeAsync();
    }

}
