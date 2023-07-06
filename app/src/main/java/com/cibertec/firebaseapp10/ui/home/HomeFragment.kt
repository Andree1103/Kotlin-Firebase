package com.cibertec.firebaseapp10.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cibertec.firebaseapp10.databinding.FragmentHomeBinding
import com.cibertec.firebaseapp10.ui.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //colecciones -> varios documentos
    val collectionsUsers = Firebase.firestore.collection("User")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGrabar.setOnClickListener {
            val names = binding.edtName.text.toString()
            val lastname =  binding.edtLastName.text.toString()
            val age = binding.edtEdad.text.toString().toInt()

            val person = User(names,lastname,age)

            GlobalScope.launch(Dispatchers.Main) {
                val response = withContext(Dispatchers.IO) {
                    collectionsUsers.add(person).await()
                }
                println(response.id)
                Toast.makeText(requireContext(), "Usuario registrado", Toast.LENGTH_SHORT).show()
                clear()
            }

            /*collectionsUsers.add(person).addOnCompleteListener{

            }*/
        }
    }

    private fun clear() {
        binding.edtEdad.setText("")
        binding.edtName.setText("")
        binding.edtLastName.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}