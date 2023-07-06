package com.cibertec.firebaseapp10.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cibertec.firebaseapp10.databinding.FragmentDashboardBinding
import com.cibertec.firebaseapp10.ui.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userAdapter = UserAdapter()
    val collectionUser = Firebase.firestore.collection("User")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUsers.adapter = userAdapter

        GlobalScope.launch(Dispatchers.Main) {
            val users =withContext(Dispatchers.IO){
                collectionUser.get().await().toObjects(User::class.java)
            }
            userAdapter.updateList(users)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}