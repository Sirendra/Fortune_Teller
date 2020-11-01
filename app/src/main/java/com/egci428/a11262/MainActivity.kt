package com.egci428.a11262



import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.view.*


class MainActivity : AppCompatActivity() {

    private var wishArray = arrayListOf<String>()
    private var dateArray = arrayListOf<String>()
    private var colorArray= arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setTitle(Html.fromHtml("<font color='#000000'>" +
                "\t".repeat(15) +
                "Fortune Cookies</font>"))


        val adapter =myCustomAdapter()
        wishArray.add("You're Lucky")
        wishArray.add("You will get A")
        wishArray.add("Don't Panic")
        dateArray.add("10-Oct-2016 12:00")
        dateArray.add("10-Oct-2016 13:00")
        dateArray.add("10-Oct-2016 13:00")
        colorArray.add("positive")
        colorArray.add("positive")
        colorArray.add("negative")
        adapter.insertItem(wishArray,dateArray,colorArray)
        main_list.adapter = adapter


    }

    //Below Function Reference: Lab RSS_Http
    //This function activate Menu Layout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    //Below Function Reference: Lab RSS_Http
    //This Function start the intent to
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_next){
            var intent = Intent(this, MainActivity2::class.java)
            startActivityForResult(intent,1)
        }
        return super.onOptionsItemSelected(item)
    }

    //Below Function Reference: Lab File_Image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1 && resultCode === RESULT_OK && data != null) {
            val adapter =myCustomAdapter()
            wishArray.add(data.getStringExtra("WISH").toString())
            colorArray.add(data.getStringExtra("STATUS").toString())
            dateArray.add(data.getStringExtra("DATE").toString())
            Toast.makeText(this,"Saved!!!!", Toast.LENGTH_SHORT).show()
            adapter.insertItem(wishArray,dateArray,colorArray)
            main_list.adapter = adapter

        }
    }

    //Below Function Reference: Lab ListView03
    private class myCustomAdapter(): BaseAdapter() {

        private var arrWish = arrayListOf<String>()
        private var arrDate = arrayListOf<String>()
        private var arrColor = arrayListOf<String>()


        fun insertItem(input1:ArrayList<String>,input2:ArrayList<String>,input3:ArrayList<String>){
            arrWish=input1
            arrDate=input2
            arrColor=input3
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return  arrWish.size
        }
        override fun getItem(position: Int): Any {
            return arrWish[position]
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val rowMain: View

            if(convertView == null) {
                val layoutInflator = LayoutInflater.from(viewGroup!!.context)
                rowMain = layoutInflator.inflate(R.layout.row, viewGroup, false)
                val viewHolder =
                        ViewHolder(
                                rowMain.txtTitle,
                                rowMain.txtPubdate,
                                rowMain.textView
                        )
                rowMain.tag = viewHolder
            } else {
                rowMain = convertView
            }
            val viewHolder = rowMain.tag as ViewHolder
            viewHolder.wishTextView.text = arrWish[position]
            viewHolder.dateTextView.text ="Date: "+arrDate[position]
            viewHolder.wishImageView.text=arrWish[position]
            if (arrColor[position]=="positive"){
                rowMain.txtTitle.setTextColor(Color.parseColor("#149CFC"))
            }
            else{
                rowMain.txtTitle.setTextColor(Color.parseColor("#FF9800"))
            }

            rowMain.imageView2.setImageResource(R.drawable.opened_cookie)

            rowMain.setOnClickListener {

                rowMain.animate().setDuration(2000).alpha(0f).withEndAction(Runnable {
                    arrWish.removeAt(position)
                    arrDate.removeAt(position)
                    arrColor.removeAt(position)
                    notifyDataSetChanged()
                    rowMain.alpha = 1F
                })

            }
            return rowMain
        }
        private class ViewHolder(val wishTextView: TextView, val dateTextView: TextView,val wishImageView:TextView)
    }

}