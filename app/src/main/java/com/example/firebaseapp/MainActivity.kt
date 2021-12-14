package com.example.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()

    }
    private fun setup(){
        btn_registrar.setOnClickListener{
            if(tv_email.text.isNotEmpty() && tv_pass.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(tv_email.text.toString(),
                    tv_pass.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"", ProviderType.BASIC)
                        } else {
                            showAlertErrorRegister()
                        }
                    }
            }
        }
        btn_ingresar.setOnClickListener{
            if(tv_email.text.isNotEmpty() && tv_pass.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(tv_email.text.toString(),
                    tv_pass.text.toString()).addOnCompleteListener(){
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"", ProviderType.BASIC)
                        } else {
                            showAlertErrorLogin()
                        }
                    }
            }
        }


    }

    private fun showAlertErrorLogin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle( "Credenciales erroneas" )
        builder.setMessage("No se encontrado al usuario en el registro")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }

    private fun showAlertErrorRegister() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle( "Credenciales erroneas" )
        builder.setMessage("El registro no se puedo completar")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, Dashboard::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

}