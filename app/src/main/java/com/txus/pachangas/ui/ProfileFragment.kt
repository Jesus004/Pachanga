package com.txus.pachangas.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.App
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.squareup.okhttp.internal.http.RequestException
import com.txus.pachangas.R
import com.txus.pachangas.databinding.FragmentHomeBinding
import com.txus.pachangas.databinding.FragmentProfileBinding
import com.txus.pachangas.entities.Usuario
import com.txus.pachangas.utils.Constantes
import com.txus.pachangas.viewModel.UsuarioViewModel

class ProfileFragment : Fragment() {

    private val TAG = "PROFILE_FRAGMENT"
    private val CODE_IMAGE_PICK = -1

    private var uriFoto: Uri? = null
    private var _binding: FragmentProfileBinding? = null
    private val model: UsuarioViewModel by viewModels()
    private lateinit var usuario: Usuario


    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val binding = _binding!!
        val view = binding.root

        binding.loginProgressBar.miProgressBar.visibility = View.VISIBLE

        model.findOneById(App.getAuth().currentUser.uid).observe(viewLifecycleOwner) {
            usuario = it

            if (!usuario.imageUrl.isNullOrBlank()){

                val circularProgress=CircularProgressDrawable(requireContext())
                circularProgress.strokeWidth=5f
                circularProgress.centerRadius=30f
                circularProgress.start()

                Glide             //recuperar la imagen de perfil de firestore
                    .with(this)
                    .load(usuario.imageUrl)
                    .centerCrop()
                    .placeholder(circularProgress)
                    .into(binding.image)

            }

            binding.loginProgressBar.miProgressBar.visibility = View.GONE

            binding.tieEdad.setText(it.edad.toString())
            binding.tieNombre.setText(it.nombre)
            binding.tieEmail.setText(it.email)
        }

        binding.image.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


            /* @Suppress("DEPRECATION")*/  startActivityForResult(intent, CODE_IMAGE_PICK)
        }

        binding.profileBtnActualizar.setOnClickListener {

            save()
            

        }

        return view
    }

    fun save() {

        if (uriFoto != null) {

            val storage = App.getStorage()
            val reference = storage.reference
                .child(Constantes.IMAGEN)
                .child(App.getAuth().currentUser.uid)
                .child("image_profile")
            reference.putFile(uriFoto!!)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { url ->

                        Log.d(TAG, url.toString())
                        usuario.imageUrl = url.toString()
                       saveUser()

                        //guardar nuevamente el usuario en firestore

                    }

                }
                .addOnFailureListener {

                    Snackbar.make(
                        _binding!!.root,
                        "No se ha podido subir la imagen a Soreage",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

        } else {
           saveUser()
        }
    }
    fun saveUser(){
        _binding!!.loginProgressBar.miProgressBar.visibility = View.VISIBLE

        model.updateUsuario(usuario).observe(viewLifecycleOwner,{

            if(it){
                _binding!!.loginProgressBar.miProgressBar.visibility = View.GONE
                Snackbar.make(_binding!!.root,"usuario actualizaaadooo!!",Snackbar.LENGTH_SHORT).show()
            }else{

                Snackbar.make(_binding!!.root,"no se ha podido actualizar naaaa",Snackbar.LENGTH_SHORT).show()

            }
        })
    }




     override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            CODE_IMAGE_PICK -> {

                if (requestCode == RESULT_OK) {
                    Log.d(TAG, requestCode.toString())

                    uriFoto = data?.data
                    _binding!!.image.setImageURI(uriFoto)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
