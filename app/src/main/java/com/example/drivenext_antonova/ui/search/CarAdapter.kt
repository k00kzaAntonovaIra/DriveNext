package com.example.drivenext_antonova.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext_antonova.R
import com.example.drivenext_antonova.data.CarData

/**
 * Adapter for displaying car list in RecyclerView
 */
class CarAdapter(
    private val onBook: (CarData) -> Unit,
    private val onDetails: (CarData) -> Unit
) : ListAdapter<CarData, CarAdapter.CarViewHolder>(CarDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for car items
     */
    inner class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvModel: TextView = view.findViewById(R.id.tvModel)
        private val tvBrand: TextView = view.findViewById(R.id.tvBrand)
        private val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        private val tvGearbox: TextView = view.findViewById(R.id.tvGearbox)
        private val tvFuel: TextView = view.findViewById(R.id.tvFuel)
        private val imgCar: ImageView = view.findViewById(R.id.imgCar)
        private val btnBook: Button = view.findViewById(R.id.btnBook)
        private val btnDetails: Button = view.findViewById(R.id.btnDetails)

        fun bind(car: CarData) {
            tvModel.text = car.model
            tvBrand.text = car.brand
            tvPrice.text = car.formattedPrice
            tvGearbox.text = car.gearbox.displayName
            tvFuel.text = car.fuel.displayName

            // Set car image or placeholder
            if (car.imageResId != null) {
                imgCar.setImageResource(car.imageResId)
            } else {
                imgCar.setImageResource(R.drawable.car)
            }

            btnBook.setOnClickListener { onBook(car) }
            btnDetails.setOnClickListener { onDetails(car) }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class CarDiffCallback : DiffUtil.ItemCallback<CarData>() {
        override fun areItemsTheSame(oldItem: CarData, newItem: CarData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarData, newItem: CarData): Boolean {
            return oldItem == newItem
        }
    }
}