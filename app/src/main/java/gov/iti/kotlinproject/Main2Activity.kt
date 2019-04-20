package gov.iti.kotlinproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val   intent:Intent= getIntent()
        val athleteNameTxt: TextView = findViewById(R.id.athleteName)
        val athleteBriefTxt: TextView = findViewById(R.id.athleteBrief)
        val athleteImage: ImageView = findViewById(R.id.athleteAvatar)
        athleteNameTxt.text=intent.getStringExtra("name")
        athleteBriefTxt.text=intent.getStringExtra("brief")
        if(!intent.getStringExtra("image").isNullOrEmpty()){
            Picasso.get().load(intent.getStringExtra("image")
            ).into(athleteImage)
        }
    }
}
