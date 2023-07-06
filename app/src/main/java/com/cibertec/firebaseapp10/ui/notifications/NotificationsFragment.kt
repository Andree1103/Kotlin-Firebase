package com.cibertec.firebaseapp10.ui.notifications

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cibertec.firebaseapp10.databinding.FragmentNotificationsBinding
import com.cibertec.firebaseapp10.ui.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.local.LocalViewChanges
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val document = Firebase.firestore
        .collection("User")
        .document("pUeAfMXDo3RKqalWlitI")

    val collection = Firebase.firestore
        .collection("User")

    private var uriImage:Uri? = null

    val storageRef = Firebase.storage.reference

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        Picasso.get().load(uri).into(binding.imgPhoto)
        uriImage = uri
    }

    private val cameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {isGranted->
        when{
            isGranted -> startForResult.launch(Intent(
                MediaStore.ACTION_IMAGE_CAPTURE))
            else -> println("Permiso Denegado")
        }
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == RESULT_OK){
            val data = result.data?.extras?.get("data") as Bitmap
            binding.imgPhoto.setImageBitmap(data)
        val uri = getImageUri(requireContext(),data)
        uriImage = uri
    } else if (result.resultCode == RESULT_CANCELED){
        println("No tomo ninguna foto")
    }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap) : Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,inImage,"Title", null
            )
        return Uri.parse(path)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*document.addSnapshotListener{ snapshot, e->
            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                binding.tvUser.text="${user?.names} - ${user?.lastName} - ${user?.age}"
            }
        }*/

        collection.addSnapshotListener{snapshot, e ->
            if (snapshot != null && !snapshot.isEmpty){
                binding.tvUser.text=""
                val users = snapshot.toObjects(User::class.java)
                users.forEach{users ->
                    binding.tvUser.append("${users?.names} - ${users?.lastName} - ${users?.age}")
                    binding.tvUser.append("\n")
                }
            }
        }

        //firebase Storage

        binding.btnPick.setOnClickListener {
            //Galery - Cam
            pickImage.launch("image/*")
        }

        binding.btnLoad.setOnClickListener {
            val uplodad =storageRef.child("files/myImage").putFile(uriImage!!)

            uplodad.addOnSuccessListener {
                println("Imagen cargada")
                storageRef.child("files/myImage").downloadUrl.addOnSuccessListener { uri->
                    println(uri)
                }
            }. addOnFailureListener{
                println(it.message.toString())
            }
        }

        binding.btnTomar.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}