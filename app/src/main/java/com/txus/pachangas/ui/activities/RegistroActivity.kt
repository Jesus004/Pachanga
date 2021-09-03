package com.txus.pachangas.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.App
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import com.txus.pachangas.R
import com.txus.pachangas.databinding.ActivityRegistroBinding
import com.txus.pachangas.entities.Usuario
import com.txus.pachangas.utils.Constantes
import com.txus.pachangas.viewModel.UsuarioViewModel
import java.util.*

class RegistroActivity : AppCompatActivity() {


    private var TAG = "REGISTRO_ACTIVITY"
    private lateinit var binding: ActivityRegistroBinding

    private val model: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)



        binding.registroBtnRegistrar.setOnClickListener {

            val nombre = binding.registroTieNombre
            val email = binding.registroTieEmail
            val pass1 = binding.registroTiePass1
            val pass2 = binding.registroTiePass2


            if (nombre.getString().isNullOrBlank()) {

                Snackbar.make(
                    view,
                    getString(R.string.field_null),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener


            }

            if (email.getString().isNullOrBlank()) {

                Snackbar.make(
                    view,
                    getString(R.string.field_null),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener


            }
            if (pass1.getString().isNullOrBlank()) {

                Snackbar.make(
                    view,
                    getString(R.string.field_null),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener


            }
            if (pass2.getString().isNullOrBlank()) {

                Snackbar.make(
                    view,
                    getString(R.string.field_null),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener


            }

            if (pass1.getString() != pass2.getString()) {

                Snackbar.make(
                    view,
                    getString(R.string.error_equals_password),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val usuario = Usuario(
                binding.registroTieNombre.getString(),
                binding.registroTieEmail.getString(),
                18,
                "",
                3,
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis())
            )

            binding.registroProgressBar.miProgressBar.visibility=View.VISIBLE

            model.registro(usuario, pass1.getString()).observe(this, { exception ->

                if (exception==null) {

                    finish()

                } else {

                    binding.registroProgressBar.miProgressBar.visibility=View.GONE
                    Log.d(TAG, exception.toString())

                    when (exception) {

                        is FirebaseAuthUserCollisionException->{
                            Snackbar.make(
                                view,
                                "Este email ya es usado por otro usuario",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()

                        }

                        is FirebaseFirestoreException->{

                            Snackbar.make(
                                view,
                                "no se ha podido registrar el nuevo usuario a la collection",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }


                        is FirebaseAuthInvalidUserException -> {
                            Snackbar.make(
                                view,
                                "no se ha podido actualizar nombre de usuario",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        is FirebaseAuthWeakPasswordException->{

                            Snackbar.make(
                                view,
                                "La contraseña es muy debil, debes poner 6 caracteres",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        else -> {

                            Snackbar.make(view, R.string.fail_register, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }


                }
            })


        }

        binding.registroBtnCancelar.setOnClickListener {


            finish()
        }

    }


    fun TextInputEditText.getString(): String {

        return text.toString()
    }
}