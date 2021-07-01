package ipvc.estg.appcm

import ipvc.estg.appcm.entities.Note

interface CellClickListener {
    fun onCellClickListener (data: Note)
}