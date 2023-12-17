package com.plasticbag.plasticbagadmin.presentation.login_signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.plasticbag.plasticbagadmin.presentation.main.MainActivity
import com.plasticbag.plasticbagadmin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginSignupViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish();
        }

        binding.txtCreateAccountLogin.setOnClickListener {
            val intoSignup = Intent(this, SignupActivity::class.java)
            startActivity(intoSignup)
        }

        binding.forgotPassword.setOnClickListener {
            val intoForgotPassword = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intoForgotPassword)
        }

        binding.btnLogin.setOnClickListener {

            binding.btnLogin.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE

            email = binding.emailLogin.editText?.text.toString()
            password = binding.passwordLogin.editText?.text.toString()

            if (email.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
            }else if (password.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
            }

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {

                viewModel.loginUser(email!!, password!!)
            }

            viewModel.authCallback = {
                val intoMain = Intent(this, MainActivity::class.java)
                startActivity(intoMain)
                finish()
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
            }

            viewModel.errorCallback = {
                Toast.makeText(this, "Invalid User", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
            }
        }

    }
}