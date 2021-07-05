package ipvc.estg.appcm.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.appcm.Note
import ipvc.estg.appcm.R

class ListNotesAdapter (val note: List<Note>): RecyclerView.Adapter<ListNotesAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_note, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        return holder.bind(note[position])
    }

    override fun getItemCount() : Int {
        return note.size
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val latitudeItemView: TextView = itemView.findViewById(R.id.latitude)
        val longitudeItemView: TextView = itemView.findViewById(R.id.longitude)
        val typeItemView: TextView = itemView.findViewById(R.id.type)
        val descriptionItemView: TextView = itemView.findViewById(R.id.description)

        fun bind(note : Note){
            latitudeItemView.text = "Latitude: " + note.latitude.toString()
            longitudeItemView.text = "Longitude: " + note.longitude.toString()
            typeItemView.text = "Type: " + note.category
            descriptionItemView.text = "Description: " +  note.description
        }
    }
}