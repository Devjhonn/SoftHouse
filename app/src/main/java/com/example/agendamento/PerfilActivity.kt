package com.example.agendamento

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agendamento.databinding.ActivityMainBinding
import com.example.agendamento.databinding.ActivityPerfilBinding
import com.example.agendamento.usuario.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class PerfilActivity : AppCompatActivity() {

    private lateinit var nomeUsuario: TextView
    private lateinit var emailUsuario: TextView
    private lateinit var contatoUsuario: TextView

    private lateinit var bt_sair : Button

    private lateinit var binding: ActivityPerfilBinding

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var usuarioID:String
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


    override fun onStart() {
        super.onStart()
        val email = FirebaseAuth.getInstance().currentUser?.email
        usuarioID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val documenteReference: DocumentReference = db.collection("Usuarios").document(usuarioID)
        documenteReference.addSnapshotListener{ documentSnapShot, error ->
            if(documentSnapShot != null){
                nomeUsuario.setText(documentSnapShot.getString("nome"))
                emailUsuario.setText(email)
            }
        }

    }
    private fun IniciarComponente(){
        nomeUsuario = findViewById(R.id.edt_nomeUsuario)
        emailUsuario = findViewById(R.id.edt_emailUsuario)
        contatoUsuario = findViewById(R.id.edt_contatoUsuario)
        bt_sair = findViewById(R.id.btn_Sair)
    }
}