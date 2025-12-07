package com.example.drivenext_antonova.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext_antonova.R
import com.example.drivenext_antonova.data.CarData
import com.example.drivenext_antonova.data.FuelType
import com.example.drivenext_antonova.data.GearboxType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HomepageFragment : Fragment(R.layout.fragment_homepage) {

    private lateinit var tilSearch: TextInputLayout
    private lateinit var etSearch: TextInputEditText
    private lateinit var tvSectionTitle: TextView
    private lateinit var rvCars: RecyclerView
    private lateinit var ivHome: ImageView
    private lateinit var ivNotes: ImageView
    private lateinit var ivSettings: ImageView

    private lateinit var adapter: CarAdapter
    private val allCars = mutableListOf<CarData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilSearch = view.findViewById(R.id.tilSearch)
        etSearch = view.findViewById(R.id.etSearch)
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle)
        rvCars = view.findViewById(R.id.rvCars)
        ivHome = view.findViewById(R.id.ivHome)
        ivNotes = view.findViewById(R.id.ivNotes)
        ivSettings = view.findViewById(R.id.ivSettings)

        setupRecycler()
        setupSearch()
        setupBottomNav()

        allCars.clear()
        allCars.addAll(mockCars())
        adapter.submitList(allCars.toList())
    }

    private fun setupRecycler() {
        adapter = CarAdapter(
            onBook = { car ->
                Toast.makeText(requireContext(), "Забронировать: ${car.fullName}", Toast.LENGTH_SHORT).show()
            },
            onDetails = { _ ->
                // В дальнейшем перейти на экран деталей
            }
        )
        rvCars.layoutManager = LinearLayoutManager(requireContext())
        rvCars.adapter = adapter
    }

    private fun setupSearch() {
        tilSearch.endIconMode = TextInputLayout.END_ICON_CUSTOM
        tilSearch.setEndIconOnClickListener { performSearch() }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupBottomNav() {
        ivHome.setOnClickListener { /* already here */ }
        ivNotes.setOnClickListener {
            findNavController().navigate(R.id.action_homepageFragment_to_bookmarksFragment)
        }
        ivSettings.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_homepageFragment_to_settingsFragment)
            } catch (_: Exception) {
                // fallback to direct destination id in case action is missing
                try { findNavController().navigate(R.id.settingsFragment) } catch (_: Exception) {}
            }
        }
    }

    private fun performSearch() {
        val q = etSearch.text?.toString()?.trim().orEmpty()
        if (q.isEmpty()) {
            adapter.submitList(allCars.toList())
            tvSectionTitle.text = getString(R.string.find_car_title)
            return
        }
        val filtered = allCars.filter { it.model.contains(q, true) || it.brand.contains(q, true) }
        tvSectionTitle.text = getString(R.string.search_results_title)
        adapter.submitList(filtered)
    }

    private fun mockCars(): List<CarData> = listOf(
        CarData("1", "S 500 Sedan", "Mercedes-Benz", 2500, GearboxType.AUTOMATIC, FuelType.GASOLINE, imageResId = R.drawable.car),
        CarData("2", "AMG GT", "Mercedes-Benz", 4500, GearboxType.AUTOMATIC, FuelType.GASOLINE, imageResId = R.drawable.car),
        CarData("3", "Model 3", "Tesla", 3500, GearboxType.AUTOMATIC, FuelType.ELECTRIC, imageResId = R.drawable.car),
        CarData("4", "X5", "BMW", 4000, GearboxType.AUTOMATIC, FuelType.GASOLINE, imageResId = R.drawable.car),
        CarData("5", "Civic", "Honda", 2800, GearboxType.MANUAL, FuelType.GASOLINE, imageResId = R.drawable.car)
    )
}