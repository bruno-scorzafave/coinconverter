package br.com.scorza5.coinconverter.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import br.com.scorza5.coinconverter.R
import br.com.scorza5.coinconverter.core.extensions.*
import br.com.scorza5.coinconverter.data.model.Coin
import br.com.scorza5.coinconverter.databinding.ActivityMainBinding
import br.com.scorza5.coinconverter.presentation.MainViewModel
import br.com.scorza5.coinconverter.ui.history.HistoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val dialog by lazy { createProgressDialog() }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindingAdapters()
        bindingListeners()
        bindingObserve()

        //setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    private fun bindingObserve() {
        viewModel.state.observe(this){
            when(it) {
                is MainViewModel.State.Loading -> dialog.show()
                is MainViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.error.message)
                    }.show()
                }
                is MainViewModel.State.Success -> success(it)
                is MainViewModel.State.Saved -> {
                    dialog.dismiss()
                    createDialog{
                        setMessage("Item salvo com sucesso!")
                    }.show()
                }
            }
        }
    }

    private fun success(it: MainViewModel.State.Success) {
        dialog.dismiss()
        binding.btnSave.isEnabled = true

        val selectedCoin = binding.tilTo.text
        val coin = Coin.getByName(selectedCoin)


        val result = it.exchange.bid * binding.tilValue.text.toDouble()

        binding.tvResult.text = result.formatCurrency(coin.locale)
        Log.e("TAG", "onCreate: ${it.exchange}")
    }

    private fun bindingAdapters() {
        val list = Coin.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        binding.tvFrom.setAdapter(adapter)
        binding.tvTo.setAdapter(adapter)

        binding.tvFrom.setText(Coin.USD.name, false)
        binding.tvTo.setText(Coin.BRL.name, false)
    }

    private fun bindingListeners() {
        binding.tilValue.editText?.doAfterTextChanged {
            binding.btnConverter.isEnabled = it != null && it.toString().isNotBlank()
            binding.btnSave.isEnabled = false
        }
        binding.btnConverter.setOnClickListener {
            it.hideSoftKeyboard()
            val search = "${binding.tilFrom.text}-${binding.tilTo.text}"
            viewModel.getExchangeValue(search)
        }
        binding.tvTo.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position)
            val newList = Coin.values().toMutableList()
            newList.remove(selected)

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, newList)
            binding.tvFrom.setAdapter(adapter)
        }
        binding.tvFrom.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position)
            val newList = Coin.values().toMutableList()
            newList.remove(selected)

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, newList)
            binding.tvTo.setAdapter(adapter)
        }
        binding.btnSave.setOnClickListener {
            val value = viewModel.state.value
            (value as? MainViewModel.State.Success)?.let {
                viewModel.saveExchange(it.exchange)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_history){
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}