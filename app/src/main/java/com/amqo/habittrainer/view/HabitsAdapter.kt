package com.amqo.habittrainer.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amqo.habittrainer.model.Habit
import com.amqo.habittrainer.R
import kotlinx.android.synthetic.main.single_card.view.*

class HabitsAdapter(private val habits: List<Habit>): RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    class HabitViewHolder(val card: View): RecyclerView.ViewHolder(card)

    // Create new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return HabitViewHolder(view)
    }

    // Specify the contents for each item
    override fun onBindViewHolder(holder: HabitViewHolder?, position: Int) {
        if (holder != null) {
            val habit = habits[position]
            holder.card.tv_title.text = habit.title
            holder.card.tv_description.text = habit.description
            holder.card.iv_icon.setImageBitmap(habit.image)
        }
    }

    override fun getItemCount() = habits.size
}
