package com.example.firebaseapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType{
    BASIC
}


class Dashboard : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var modalList = ArrayList<Modal>()
    private var names = arrayOf("honey","juice","coffie_cup", "potato","panacotta","red_velvet")
    private var images = intArrayOf(R.mipmap.honey, R.mipmap.juice, R.mipmap.coffie_cup,
        R.mipmap.potato, R.mipmap.panacotta, R.mipmap.red_velvet)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        val bundle:Bundle?=intent.extras
        val email:String?= bundle?.getString("email")
        val provider:String?= bundle?.getString("provider")

                setup(email?: "", provider?:"")
    }


    private fun setup(email:String, provider:String){
        tv_usuarioId.text = email
        tv_other.text = provider


        for (i in names.indices){
            modalList.add(Modal(names[i], images[i]))
        }

        val customAdapter = CustomAdapter(modalList, this)

        gridView.adapter = customAdapter

        btn_guardar.setOnClickListener{
            db.collection("users").document(email).set(
                hashMapOf("provider" to provider, "address" to tv_direccion.text.toString(),
                    "phone" to tv_telefono.text.toString(), "rol" to tv_rol.text.toString()
                )
            )
        }

        btn_ingresar_camara.setOnClickListener{
            val camraIntent = Intent (this, Camara::class.java)
            startActivity(camraIntent)
        }

        btn_gps.setOnClickListener{
            val gpsIntent = Intent(this, Gps::class.java)
            startActivity(gpsIntent)
        }

        btn_logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

    class CustomAdapter(
        private var itemModel: ArrayList<Modal>,
        private var context: Context
    ):BaseAdapter(){

        private var layOutInFlater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return itemModel.size
        }

        override fun getItem(position: Int): Any {
            return itemModel[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            var view = convertView
            if( view == null ){
                view = layOutInFlater.inflate(R.layout.row_items, viewGroup, false)
            }
            val tvImageName = view?.findViewById<TextView>(R.id.imageName)
            val imageView = view?.findViewById<ImageView>(R.id.iv_camara)

            tvImageName?.text = itemModel[position].name
            imageView?.setImageResource(itemModel[position].image!!)

            return view!!

        }

    }

}