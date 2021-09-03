package com.txus.pachangas.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.App
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

import com.google.firebase.auth.FirebaseAuthWeakPasswordException

import com.txus.pachangas.databinding.ActivityLoginBinding
import com.txus.pachangas.viewModel.UsuarioViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val TAG = "MAIN_ACTIVITY"

    private val model: UsuarioViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.btnRegistro.setOnClickListener {

            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener {

            val email = binding.loginTieEmail
            val password = binding.loginTiePassword

            if (email.getString().isNullOrBlank()) {

                Snackbar.make(view, "los campos no pueden estar vacíos", Snackbar.LENGTH_SHORT)
                    .show()

                return@setOnClickListener
            }

            if (password.getString().isNullOrBlank()) {

                Snackbar.make(view, "los campos no pueden estar vacíos", Snackbar.LENGTH_SHORT)
                    .show()

                return@setOnClickListener
            }

            binding.loginProgressBar.miProgressBar.visibility=View.VISIBLE



            model.login(email.getString(), password.getString()).observe(this, { task ->


                if (task.isSuccessful) {
                    goToMain()
                    finish()


                } else {
                    binding.loginProgressBar.miProgressBar.visibility=View.GONE
                    when (task.exception) {

                        is FirebaseAuthWeakPasswordException -> {
                            Snackbar.make(
                                view,
                                "La contraseña es muy debil, debes poner 6 caracteres",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        else -> {

                            Snackbar.make(
                                view,
                                "Error al iniciar sesion",
                                Snackbar.LENGTH_SHORT
                            ).show()


                        }
                    }


                }
            })


        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = App.getAuth().currentUser



        if (currentUser != null) {

            Log.d(TAG, currentUser!!.email!!)
            Log.d(TAG, currentUser!!.displayName!!)
            Log.d(TAG, currentUser!!.uid!!)
            goToMain()
        }
    }


    fun TextInputEditText.getString(): String {

        return text.toString()
    }

    private fun goToMain() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}