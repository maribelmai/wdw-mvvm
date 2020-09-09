package ar.com.maribelmai.wdw.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ar.com.maribelmai.wdw.R
import ar.com.maribelmai.wdw.activities.ImageDetailActivity
import ar.com.maribelmai.wdw.model.Picture

class PicturesAdapter(private val values: List<Picture>) :
    RecyclerView.Adapter<PicturesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.fileName.text = item.name
        holder.fileSize.text = item.size.toString()
        holder.itemView.setOnClickListener { openDetail(holder.itemView.context, item) }
    }

    private fun openDetail(context: Context, picture: Picture) {
        val intent = Intent(context, ImageDetailActivity::class.java)
        intent.putExtra(ImageDetailActivity.EXTRA_PICTURE, picture)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView = view.findViewById(R.id.fileName)
        val fileSize: TextView = view.findViewById(R.id.fileSize)
    }
}