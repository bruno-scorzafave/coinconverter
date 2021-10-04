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
        setLists(Coin.USD, Coin.BRL)
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
            val toSelected = parent.getItemAtPosition(position) as Coin
            val fromSelected = Coin.values().filter { it.name == binding.tvFrom.text.toString() }
            setLists(fromSelected[0], toSelected)
        }
        binding.tvFrom.setOnItemClickListener { parent, _, position, _ ->
            val fromSelected = parent.getItemAtPosition(position) as Coin
            val toSelected = Coin.values().filter { it.name == binding.tvTo.text.toString() }
            setLists(fromSelected, toSelected[0])
        }
        binding.btnSave.setOnClickListener {
            val value = viewModel.state.value
            (value as? MainViewModel.State.Success)?.let {
                viewModel.saveExchange(it.exchange.copy(
                    result = it.exchange.bid * binding.tilValue.text.toDouble(),
                    exchangeValue = binding.tilValue.text.toDouble()
                ))
            }
        }
        binding.btnSwap.setOnClickListener {
            // The coin need to go from from to to and to to from = swap
            val coinFrom = Coin.values().filter { it.name == binding.tvTo.text.toString() }
            val coinTo = Coin.values().filter { it.name == binding.tvFrom.text.toString() }
            setLists(coinFrom[0], coinTo[0])
        }
    }

    private fun setLists(fromSelected: Coin, toSelected: Coin){
        val listTo = Coin.values().toMutableList()
        listTo.remove(fromSelected)
        val listFrom = Coin.values().toMutableList()
        listFrom.remove(toSelected)

        val adapterTo = ArrayAdapter(this, android.R.layout.simple_list_item_1, listTo)
        binding.tvTo.setAdapter(adapterTo)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listFrom)
        binding.tvFrom.setAdapter(adapter)

        binding.tvFrom.setText(fromSelected.toString(), false)
        binding.tvTo.setText(toSelected.toString(), false)
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