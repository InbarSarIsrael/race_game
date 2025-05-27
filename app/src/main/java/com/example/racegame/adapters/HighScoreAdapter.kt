package com.example.racegame.adapters

import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.racegame.R
import com.example.racegame.models.Score
import java.util.Locale

class HighScoreAdapter(
    private val scores: List<Score>,
    private val callback: ((Score) -> Unit)? = null
) :
    RecyclerView.Adapter<HighScoreAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_LBL_name: TextView = itemView.findViewById(R.id.text_LBL_name)
        val text_LBL_score: TextView = itemView.findViewById(R.id.text_LBL_score)
        val text_LBL_location: TextView = itemView.findViewById(R.id.text_LBL_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_high_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.text_LBL_name.text = score.name
        holder.text_LBL_score.text = score.score.toString()

        val (lat, lon) = score.getLatLon()

        val geocoder = Geocoder(holder.itemView.context, Locale.ENGLISH)
        val city = try {
            geocoder.getFromLocation(lat, lon, 1)?.getOrNull(0)?.locality ?: score.location
        } catch (e: Exception) {
            score.location
        }

        holder.text_LBL_location.text = city

        holder.itemView.setOnClickListener {
            callback?.invoke(score)
        }
    }

    override fun getItemCount(): Int = scores.size
}