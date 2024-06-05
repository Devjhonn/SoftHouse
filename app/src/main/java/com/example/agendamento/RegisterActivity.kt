package com.example.agendamento

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.agendamento.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var edit_nome_register: EditText
    private lateinit var edit_email_register: EditText
    private lateinit var edit_senha_register: EditText

    private lateinit var btn_criarConta: Button

    private lateinit var usuarioId: String

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        supportActionBar?.hide()
        IniciarComponentes()


        binding.btnCriarConta.setOnClickListener {
            val nome = binding.edtNomeRegister.text.toString()
            val email = binding.edtEmailRegister.text.toString()
            val senha = binding.edtSenhaRegister.text.toString()
            val confirmaSenha = binding.edtConfirmSenha.text.toString()

            when {
                nome.isEmpty() -> {
                    mensagem(it, "Insira o nome Completo!")
                }

                senha.isEmpty() -> {
                    mensagem(it, "Isira a senha! ")
                }

                senha.length <= 5 -> {
                    mensagem(it, "A senha precisa ter 6 digitos!")
                }
                email.isEmpty() ->{
                    mensagem(it, "Insira seu E-mail!!")
                }
                confirmaSenha.isEmpty()->{
                    mensagem(it, "Confirme sua senha!")
                }
                senha != confirmaSenha ->{
                mensagem(it, "Senhas diferentes!!")
            }
                else -> {
                    cadastrarUsuario()
                    salvarDados()
                    navegarParaLogin(nome)
                }
            }
        }
    }


    private fun cadastrarUsuario() {
        val email = binding.edtEmailRegister.text.toString()
        val senha = binding.edtSenhaRegister.text.toString()
        val confirmaSenha = binding.edtConfirmSenha.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this){ task ->
                if (senha == confirmaSenha) {
                    // Registro bem-sucedido
                    task.isSuccessful
                    Toast.makeText(this, "Registro bem-sucedido", Toast.LENGTH_SHORT).show()
                    // Aqui você pode direcionar para a próxima atividade
                } else {
                // Registro falhou
                Toast.makeText(this, "Falha ao registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun IniciarComponentes(){
        edit_nome_register = findViewById(R.id.edt_nome_register)
        edit_email_register = findViewById(R.id.edt_email_register)
        edit_senha_register = findViewById(R.id.edt_senha_register)
        btn_criarConta = findViewById(R.id.btn_criarConta)
    }

    private fun mensagem(view: View, mensagem: String) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun navegarParaLogin(nome: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nome", nome)
        startActivity(intent)
    }


    private fun salvarDados(){
        val nome = binding.edtNomeRegister.text.toString()
        val email = binding.edtEmailRegister.text.toString()

        val db = FirebaseFirestore.getInstance()

        val usuarios = mutableMapOf<String, Any>()
        usuarios["nome"] = nome
        usuarios["E-mail"] = email

        usuarioId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val documentReference: DocumentReference = db.collection("Usuarios").document(usuarioId)
        documentReference.set(usuarios).addOnSuccessListener (this){ task->
            Log.d("db", "Sucesso")
        }.addOnFailureListener(this){task ->
            Log.e("db", "Erro ao salvar")
        }
    }
}