package com.txus.pachangas.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.txus.pachangas.databinding.ActivityPartidaBinding

class PartidaActivity : AppCompatActivity() {

    private val TAG = "MAIN_ACTIVITY"
    private lateinit var binding: ActivityPartidaBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityPartidaBinding.inflate(layoutInflater)
        val view = binding.root
        auth=Firebase.auth

        setContentView(view)

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}