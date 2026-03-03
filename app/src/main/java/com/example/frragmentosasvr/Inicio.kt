package com.example.frragmentosasvr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inicio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)
        val mensaje2 = view.findViewById<Button>(R.id.mensaje2)
        mensaje2.setOnClickListener {
            (activity as MainActivity).mensaje()
        }
        val qr = view.findViewById<Button>(R.id.qr)
        qr.setOnClickListener {
            (activity as MainActivity).leerQR()
        }
        val gps = view.findViewById<Button>(R.id.gps)
        val ubicacion = view.findViewById<TextView>(R.id.gps_text)
        gps.setOnClickListener {
            (activity as MainActivity).obtenerUbicacion { ubicacionObtenida ->
                ubicacion.text = ubicacionObtenida.toString()
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Inicio.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Inicio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}