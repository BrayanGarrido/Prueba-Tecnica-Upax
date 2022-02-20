package com.bornidea.pruebatecnicaupax.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.FragmentPeliculasBinding
import com.bornidea.pruebatecnicaupax.model.data.Pelicula
import com.bornidea.pruebatecnicaupax.model.data.toDataBase
import com.bornidea.pruebatecnicaupax.model.database.PeliculasDataBase
import com.bornidea.pruebatecnicaupax.model.database.entities.toDataBase
import com.bornidea.pruebatecnicaupax.model.network.PeliculasRepository
import com.bornidea.pruebatecnicaupax.ui.adapters.PeliculasAdapter
import com.bornidea.pruebatecnicaupax.viewmodel.PeliculasViewModel
import com.bornidea.pruebatecnicaupax.viewmodel.PeliculasViewModelFactory

class PeliculasFragment : Fragment() {

    private lateinit var peliculasViewModel: PeliculasViewModel
    private lateinit var adapter: PeliculasAdapter
    lateinit var binding: FragmentPeliculasBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_peliculas, container, false)

        /**ViewModel*/
        val dao = PeliculasDataBase.getInstance(requireActivity().application).peliculaDAO
        val repository = PeliculasRepository(dao)
        val factory = PeliculasViewModelFactory(repository)
        peliculasViewModel =
            ViewModelProvider(requireActivity(), factory)[PeliculasViewModel::class.java]

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        /**RecyclerView*/
        binding.recyclerFilms.setHasFixedSize(true)
        binding.recyclerFilms.layoutManager = GridLayoutManager(context, 3)
        adapter = PeliculasAdapter { selectedItem: Pelicula -> listItemClicked(selectedItem) }
        binding.recyclerFilms.adapter = adapter
        displayFilms()

    }

    private fun displayFilms() {
        /**Iniciar Retrofit*/
        peliculasViewModel.getAllFilms(requireActivity().applicationContext)
            .observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.setList(it)
                    adapter.notifyDataSetChanged()

                    val filmList = it.map { it.toDataBase() }
                    peliculasViewModel.deleteAll()
                    peliculasViewModel.insertAll(filmList)
                } else {
                    peliculasViewModel.peliculasDB.observe(viewLifecycleOwner, Observer {
                        val filmList = it.map { it.toDataBase() }
                        adapter.setList(filmList)
                        adapter.notifyDataSetChanged()
                    })
                }
            })
    }


    private fun listItemClicked(selectedItem: Pelicula) {
        Toast.makeText(context, "Titulo: ${selectedItem.title}", Toast.LENGTH_SHORT).show()
    }

}