package com.infnet.pb.ui.reuniaoCreate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.google.android.material.snackbar.Snackbar
import com.infnet.pb.R
import com.infnet.pb.databinding.FragmentReuniaoCreateBinding
import java.util.*

class reuniaoCreateFragment : Fragment() {





    private var _binding: FragmentReuniaoCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReuniaoCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReuniaoCreateBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ReuniaoCreateViewModel::class.java)
        val view = binding.root
        binding.loadBar.visibility = View.GONE

        viewModel.msg
            .observe(viewLifecycleOwner){
                if ( it.isNotBlank()) {
                    showSnackbar(view, it)
                }
            }

        viewModel.status.observe(viewLifecycleOwner){
            if (it){

                findNavController().popBackStack()
            }
//            if (it){
//                binding.inputMarca.setText("")
//                binding.inputModelo.setText("")
//                binding.inputPlaca.setText("")
//            }
        }

        binding.imageBackstack.setOnClickListener {

            findNavController().popBackStack()
        }

        setup()
        return view
    }

    val calendar = Calendar.getInstance()

    fun setup(){
        configuraPapeis()
        datePicker()
        hourPicker()
        createReuniao()
    }



    fun createReuniao(){
        binding.btnCreate.setOnClickListener {
            val titulo = binding.tvNomeReuniao.text.toString()
            val data = binding.tvData.text.toString()
            val horainicio = binding.tvHora.text.toString()
            val horatermino = binding.tvHoraTermino.text.toString()
            val tipo = binding.inputTipo.text.toString()
            binding.btnCreate.isClickable = false
            if (titulo.isNotEmpty() && data.isNotEmpty() && horainicio.isNotEmpty() && horatermino.isNotEmpty() && tipo.isNotEmpty()){
                viewModel.inserir(titulo, data, horainicio, horatermino, tipo)
                binding.loadBar.visibility = View.VISIBLE

            }


            else {
                binding.btnCreate.isClickable = true
                Toast.makeText(requireContext(), "Insira os campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun datePicker(){
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day= calendar.get(Calendar.DAY_OF_MONTH)

        binding.tvData.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { view, mYear, mMonth, mDay -> binding.tvData.setText(""+ mDay + "/"+ mMonth +"/"+ mYear).toString()
                }, year, month, day)



            dpd.show()
        }
    }

    fun hourPicker(){
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)


        binding.tvHora.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(),TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                binding.tvHora.setText("$i:$i2")
            },hour,minutes,true)
            timePicker.show()
        }

        binding.tvHoraTermino.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(),TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                binding.tvHoraTermino.setText("$i:$i2")
            },hour,minutes,true)
            timePicker.show()
        }
    }

    var listaDePapeis = arrayOf(
        "Zoom",
        "Google Meets",
        "Teams",
        "Outro"

    )

    fun configuraPapeis(){
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.tipos_item_list, listaDePapeis)
        binding.inputTipo.setAdapter(adapter)
    }




    private fun showSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReuniaoCreateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}