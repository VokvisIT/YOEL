package com.example.yoel_beta.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.yoel_beta.HomeActivity
import com.example.yoel_beta.MainActivity
import com.example.yoel_beta.R
import com.example.yoel_beta.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import kotlin.math.log

class SettingsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

private var _binding: FragmentSettingsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

    _binding = FragmentSettingsBinding.inflate(inflater, container, false)
    val root: View = binding.root
    return root
  }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        logout()
    }
    private fun init(){
        auth = FirebaseAuth.getInstance()
    }
    private fun logout(){
        binding.logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}