package ipvc.estg.appcm.adapters

import android.content.ClipDescription
import android.content.Context
import android.location.Address
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.appcm.OnItemClickListener
import ipvc.estg.appcm.R
import ipvc.estg.appcm.entities.Note

class NoteAdapter internal constructor(
        context: Context,
        private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val noteItemView: TextView = itemView.findViewById(R.id.textView)
        val titleView: TextView = itemView.findViewById(R.id.title)
        val bodyView: TextView = itemView.findViewById(R.id.body)
        val addressView: TextView = itemView.findViewById(R.id.address)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.titleView.text = "Title: " + current.title
        holder.bodyView.text = "Body: " + current.body
        holder.addressView.text = "Address: " + current.address
        //holder.addressView.text = R.string.recyclerItemAddress.toString() + current.address
        holder.itemView.setOnClickListener{
            listener.onItemClickListener(notes[position])
        }
    }

    internal fun setNotes(notes: List<Note>){
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int{
      return notes.size
    }
}