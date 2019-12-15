package ru.skillbranch.gameofthrones.ui.houses.house

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.android.synthetic.main.item_character.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem

/*
 * Created by yasina on 2019-12-14
*/
class CharactersAdapter(private val listener: (CharacterItem) -> Unit) :
        RecyclerView.Adapter<CharactersAdapter.CharacterVH>(){

    var items: List<CharacterItem> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterVH {
        val containerView = from(parent.context).inflate(
            R.layout.item_character,
            parent,
            false
        )
        return CharacterVH(containerView)
    }

    override fun onBindViewHolder(holder: CharacterVH, position: Int) =
        holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    fun updateItems(data: List<CharacterItem>){
        val diffCallback =  object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].id == data[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition] == data[newItemPosition]

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    class CharacterVH(override val containerView: View) :RecyclerView.ViewHolder(containerView),
            LayoutContainer{
        fun bind(
            item: CharacterItem,
            listener: (CharacterItem) -> Unit
        ){
            item.name.also {
                tv_name.text = if(it.isBlank()) "Information is unknown" else it
            }
            item.titles
                .plus(item.aliases)
                .filter { it.isNotBlank() }
                .also {
                    tv_aliases.text = if (it.isEmpty()) "Information is unknown"
                    else it.joinToString(".")
                }

            iv_avatar.setImageResource(item.house.icon)

            itemView.setOnClickListener { listener(item) }
        }
    }
}

