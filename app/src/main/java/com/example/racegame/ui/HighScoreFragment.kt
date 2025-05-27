package com.example.racegame.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.racegame.R
import com.example.racegame.adapters.HighScoreAdapter
import com.example.racegame.interfaces.Callback_HighScoreItemClicked
import com.example.racegame.models.Score
import com.example.racegame.utilities.HighScoreManager

class HighScoreFragment : Fragment() {

    interface OnScoreSelectedListener {
        fun onScoreSelected(score: Score)
    }

    private var listener: OnScoreSelectedListener? = null
    private lateinit var highScoresRecyclerView: RecyclerView
    private lateinit var highScoresAdapter: HighScoreAdapter
    var highScoreItemClicked: Callback_HighScoreItemClicked? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnScoreSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnScoreSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(view)
        initViews()
        return view
    }

    private fun findViews(view: View) {
        highScoresRecyclerView = view.findViewById(R.id.highScore_LST_scores)
    }

    private fun initViews() {
        highScoresRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val scores = HighScoreManager.loadHighScores(requireContext()).filter {  it.score > 0 }

        highScoresAdapter = HighScoreAdapter(scores.toMutableList()) { selectedScore ->
            listener?.onScoreSelected(selectedScore)
        }
        highScoresRecyclerView.adapter = highScoresAdapter
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
