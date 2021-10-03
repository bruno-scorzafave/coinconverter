package br.com.scorza5.coinconverter.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.scorza5.coinconverter.core.extensions.formatCurrency
import br.com.scorza5.coinconverter.data.model.Coin
import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue
import br.com.scorza5.coinconverter.databinding.ItemHistoryBinding

class HistoryListAdapter: ListAdapter<ExchangeResponseValue, HistoryListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemHistoryBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(item: ExchangeResponseValue){
            binding.tvName.text = "${item.code}/${item.codein}"
            val coinTo = Coin.getByName(item.codein)
            val coinFrom = Coin.getByName(item.code)
            binding.tvCalculation.text = "${item.exchangeValue.formatCurrency(coinFrom.locale)} x ${"%.2f".format(item.bid)} ="
            binding.tvValue.text = "${item.result.formatCurrency(coinTo.locale)}"
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<ExchangeResponseValue>() {
    override fun areItemsTheSame( oldItem: ExchangeResponseValue, newItem: ExchangeResponseValue) = oldItem == newItem

    override fun areContentsTheSame( oldItem: ExchangeResponseValue, newItem: ExchangeResponseValue) = oldItem.id == newItem.id

}
