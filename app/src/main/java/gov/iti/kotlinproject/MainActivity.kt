package gov.iti.kotlinproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var athleteAdapter : AthletesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       athleteAdapter = AthletesAdapter( { partItem : Athlete -> partItemClicked(partItem) })
        athletes_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        athletes_list.adapter = athleteAdapter
        val base: String = "https://gist.githubusercontent.com"

        val retrofit: Retrofit = Retrofit.Builder().baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val apiMovies = retrofit.create(ApiAthletes::class.java)

        apiMovies.getAthletes()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ athleteAdapter.setAthletes(it.athletes) },
                        {
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                        })

    }


    inner class AthletesAdapter (val clickListener: (Athlete) -> Unit) : RecyclerView.Adapter<AthletesAdapter.AthleteViewHolder>() {

        private val athletes: MutableList<Athlete> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteViewHolder {
            return AthleteViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false))
        }

        override fun getItemCount(): Int {
            return athletes.size
        }

        override fun onBindViewHolder(holder: AthleteViewHolder, position: Int) {
            holder.bindModel(athletes[position],clickListener)
        }

        fun setAthletes(data: List<Athlete>) {
            athletes.addAll(data)
            notifyDataSetChanged()
        }

        inner class AthleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            val athleteNameTxt: TextView = itemView.findViewById(R.id.athleteName)
            val athleteBriefTxt: TextView = itemView.findViewById(R.id.athleteBrief)
            val athleteImage: ImageView = itemView.findViewById(R.id.athleteAvatar)

            fun bindModel(athlete: Athlete,clickListener: (Athlete) -> Unit) {
                itemView.setOnClickListener { clickListener(athlete)}
                athleteNameTxt.text = athlete.name
                athleteBriefTxt.text = athlete.brief
                if(!athlete.image.isNullOrEmpty()){
                    Picasso.get().load(athlete.image).into(athleteImage)
                }
            }
        }
    }
    fun partItemClicked(partItem : Athlete) {
        Toast.makeText(this, "Clicked: ${partItem.name}", Toast.LENGTH_LONG).show()

        val intent = Intent(this, Main2Activity::class.java)
        intent.putExtra("name",partItem.name)
        intent.putExtra("image",partItem.image)
        intent.putExtra("brief",partItem.brief)
        startActivity(intent)
    }
}
