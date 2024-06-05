package com.example.agendamento

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agendamento.databinding.ActivityMainBinding
import com.example.agendamento.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth

class PerfilActivity : AppCompatActivity() {

    private lateinit var nomeUsuario: TextView
    private lateinit var emailUsuario: TextView
    private lateinit var contatoUsuario: TextView

    private lateinit var bt_sair : Button

    private lateinit var binding: ActivityPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        supportActionBar?.hide()
        IniciarComponente()

        binding.btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun IniciarComponente(){
        nomeUsuario = findViewById(R.id.edt_nomeUsuario)
        emailUsuario = findViewById(R.id.edt_emailUsuario)
        contatoUsuario = findViewById(R.id.edt_contatoUsuario)
        bt_sair = findViewById(R.id.btn_Sair)
    }
}