package com.example.agendamento

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.agendamento.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data: String = ""
    private var hora: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome")


        val calendario = binding.calendario
        calendario.setOnDateChangeListener { _, year, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            var dia  = dayOfMonth.toString()
            val mes: String

            if(dayOfMonth < 10){
                dia =  "0$dayOfMonth"
            }

            if(monthOfYear < 10){
                mes = "" + (monthOfYear + 1)
            }else{
                mes = (monthOfYear + 1).toString()
            }

            data = "$dia / $mes / $year"
        }


        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val minuto: String

            if (minute < 10){
                minuto = "0$minute"

            }else{
                minuto = minute.toString()
            }

            hora = "$hourOfDay:$minuto" //19:00
        }

        binding.timePicker.setIs24HourView(true)

        binding.btnAgendar.setOnClickListener {
            when{
                hora.isEmpty() -> {
                    mensagem(it, "Escolha o horario", cor = "#FF0000")
                }
                hora < "8:00" && hora > "18:00" ->{
                    mensagem(it, "Horario não permitido!", cor = "#FF0000")
                }
                data.isEmpty() ->{
                    mensagem(it, "Escolha uma data!", cor = "#FF0000")
                }
                data.isNotEmpty() && hora.isNotEmpty() && hora > "8:00" && hora < "19:00" ->{
                    salvarAgendamento(it,nome, data,hora)
                }
            }
        }

        binding.btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }


    }

    private fun mensagem(view: View, mensagem: String, cor: String){
        val  snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }


    private fun salvarAgendamento(view: View, usuario: String?, data: String, hora: String){
        val db = FirebaseFirestore.getInstance()

        val dadosUsuario = hashMapOf(
            "nome" to usuario,
            "data" to data,
            "hora" to hora
        )

        db.collection("agendamento").document(usuario.toString()).set(dadosUsuario).addOnCompleteListener{
            mensagem(view, "Agendamento realizado com sucesso!", "#FF03DAC5")
        }.addOnFailureListener{
            mensagem(view, "Erro no servidor!", "FF0000")
        }
    }



}