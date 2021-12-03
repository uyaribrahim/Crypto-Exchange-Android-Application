package com.ibrhm.cryptoexchange.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.databinding.ActivityLoginBinding
import com.ibrhm.cryptoexchange.databinding.ActivityRegisterBinding
import com.ibrhm.cryptoexchange.view.MainActivity
import com.ibrhm.cryptoexchange.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.getUserLiveData().observe(this, Observer {
            if(it != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.btnRegister.setOnClickListener {
            var email: String = binding.txtEmail.text.toString()
            var password: String = binding.txtPassword.text.toString()
            var verifyPassword: String = binding.txtVerifyPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty()){
                if(password == verifyPassword){
                    loginRegisterViewModel.register(email, password)
                }else{
                    Toast.makeText(this,"Giriğiniz şifreler birbiri ile uyuşmuyor!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"E-posta Adresi ve Şifre Girilmelidir", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}