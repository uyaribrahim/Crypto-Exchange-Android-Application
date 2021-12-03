package com.ibrhm.cryptoexchange.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.ibrhm.cryptoexchange.view.MainActivity
import com.ibrhm.cryptoexchange.databinding.ActivityLoginBinding
import com.ibrhm.cryptoexchange.viewmodel.LoginRegisterViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.getUserLiveData().observe(this, Observer {
            if(it != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.btnRegister?.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin?.setOnClickListener {
            var email: String = binding.username.text.toString()
            var password: String = binding.password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                loginRegisterViewModel.login(email,password)
            }else{
              Toast.makeText(this,"E-posta Adresi ve Şifre Girilmelidir", Toast.LENGTH_SHORT).show()
            }
        }
    }
}