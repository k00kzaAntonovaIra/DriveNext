package com.example.drivenext_antonova.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.drivenext_antonova.R
import com.example.drivenext_antonova.data.CarData

/**
 * Fragment for displaying search results
 */
class ResultFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CarAdapter
    private lateinit var btnBack: View
    private var brand: String? = null
    private var cars: List<CarData>? = null

    companion object {
        private const val ARG_BRAND = "brand"
        private const val ARG_CARS = "cars"
        
        fun newInstance(brand: String? = null, cars: List<CarData>? = null): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle().apply {
                brand?.let { putString(ARG_BRAND, it) }
                cars?.let { putParcelableArrayList(ARG_CARS, ArrayList(it)) }
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brand = arguments?.getString(ARG_BRAND)
        cars = arguments?.getParcelableArrayList<CarData>(ARG_CARS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search_result, container, false)
        setupViews(root)
        setupRecyclerView()
        setupClickListeners()
        displayCars()
        return root
    }

    private fun setupViews(root: View) {
        recycler = root.findViewById(R.id.recyclerResults)
        btnBack = root.findViewById(R.id.btnBack)
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = CarAdapter(
            onBook = { car -> onBookClicked(car) },
            onDetails = { car -> onDetailsClicked(car) }
        )
        recycler.adapter = adapter
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { 
            findNavController().popBackStack() 
        }
    }

    private fun displayCars() {
        cars?.let { carList ->
            adapter.submitList(carList)
        }
    }

    private fun onBookClicked(car: CarData) {
        // Placeholder navigation
        findNavController().navigate(R.id.homepageFragment)
    }

    private fun onDetailsClicked(car: CarData) {
        // Placeholder navigation
        findNavController().navigate(R.id.homepageFragment)
    }
}
