package com.egci428.a11262


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.egci428.a11262.models.Fortune
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main2.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity2 : AppCompatActivity() {

    var wish:String = ""
    var color:String = ""
    var date:String = ""
    private val dateFormat: SimpleDateFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.setTitle(
            Html.fromHtml("<font color='#000000'>" +
                "\t".repeat(7) +
                "Fortune Cookies</font>"))

        imageView3.setImageResource(R.drawable.closed_cookie)

        //Below Code Reference: Lab RSS_Http
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        wishBtn.setOnClickListener {
            Toast.makeText(this,"Waiting",Toast.LENGTH_SHORT).show()
            imageView3.setImageResource(R.drawable.opened_cookie)

            val ran = kotlin.random.Random.nextInt(0, 9).toString()
            val jsonURL = "https://egco428-json.firebaseio.com/fortunecookies/" + ran + ".json"

            //Below Code Reference: Lab JSON
            val client = OkHttpClient()
            var asyncTask = object: AsyncTask<String, String, String>(){
                override fun onPreExecute() {
                    super.onPreExecute()
                }

                override fun doInBackground(vararg arg: String?): String {
                    val  builder = Request.Builder()
                    builder.url(arg[0].toString())
                    val request = builder.build()
                    try {
                        val response = client.newCall(request).execute()
                        return response.body!!.string()

                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                    return ""
                }
                override fun onPostExecute(result: String?) {

                    val FORTUNE = Gson().fromJson(result, Fortune::class.java)
                    //to get the wish from website
                    wish = FORTUNE.message
                    textView6.text=wish
                    textView3.text ="Result: "+ wish


                    //to get the status from website
                    color = FORTUNE.status
                    if (color=="positive"){
                        textView3.setTextColor(Color.parseColor("#149CFC"))
                    }
                    else{
                        textView3.setTextColor(Color.parseColor("#FF9800"))
                    }

                    //with the help of Internet
                    val current = LocalDateTime.now()

                    val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")
                    val formatted = current.format(formatter)
                    date=formatted
                    dateText.text = "Date: "+date

                }
            }
            asyncTask.execute(jsonURL)
            //To hide the wish button and show save btn
            wishBtn.visibility = View.GONE
        }

        //This function send the value to main page using intent
        saveBtn.setOnClickListener{
            var intent = Intent()
            intent.putExtra("WISH",wish)
            intent.putExtra("STATUS",color)
            intent.putExtra("DATE",date)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    //This function is used to return to main page without saving
    //This happen when user press the back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            val output = Intent()
            setResult(Activity.RESULT_CANCELED, output)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}