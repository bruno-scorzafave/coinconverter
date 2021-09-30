package br.com.scorza5.coinconverter.ui.history

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.scorza5.coinconverter.core.extensions.createDialog
import br.com.scorza5.coinconverter.core.extensions.createProgressDialog
import br.com.scorza5.coinconverter.databinding.ActivityHistoryBinding
import br.com.scorza5.coinconverter.presentation.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity: AppCompatActivity() {

    private val adapter by lazy { HistoryListAdapter() }
    private val viewModel by viewModel<HistoryViewModel>()
    private val dialog by lazy { createProgressDialog() }
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(binding.root)

        binding.rvHistory.adapter = adapter
        binding.rvHistory.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        )

        setSupportActionBar(binding.toolbarHist)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        bindObserve()

        lifecycle.addObserver(viewModel)
    }

    private fun bindObserve() {
        viewModel.state.observe(this){
            when(it){
                HistoryViewModel.State.Loading -> dialog.show()
                is HistoryViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.error.message)
                    }.show()
                }
                is HistoryViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(it.list)
                }
            }
        }
    }
}