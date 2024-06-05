package com.example.agendamento

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.agendamento.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    lateinit var txtCadastro: TextView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txtCadastro = findViewById<TextView>(R.id.txt_criar_conta)
        txtCadastro.setOnClickListener {
            val txtCadastroIntent = Intent(this, RegisterActivity::class.java)
            startActivity(txtCadastroIntent)
        }

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val senha = binding.edtSenhaLogin.text.toString()

            when {
                email.isEmpty() -> {
                    mensagem(it, "Insira seu e-mail!")
                }
                senha.isEmpty() -> {
                    mensagem(it, "Isira a senha! ")
                }
                senha.length <= 5 -> {
                    mensagem(it, "A senha precisa ter 6 digitos!")
                }
                else -> {
                    authenticarUsuario()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val usuarioAtual: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            abrirHome()
        }
    }

    private fun abrirHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun mensagem(view: View, mensagem: String) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun navegarParaHome(email: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    private fun authenticarUsuario(){
        val email = binding.edtEmailLogin.text.toString()
        val senha = binding.edtSenhaLogin.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                // Aqui você pode direcionar para a próxima atividade
                navegarParaHome(email)
            } else {
                Toast.makeText(this, "Falha ao Logar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}