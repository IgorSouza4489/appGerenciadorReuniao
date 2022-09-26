package com.infnet.pb.ui.reuniaoShow

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.infnet.pb.JavaMailAPI
import com.infnet.pb.R
import com.infnet.pb.dao.authDao
import com.infnet.pb.databinding.FragmentReuniaoShowBinding
import java.text.SimpleDateFormat
import java.util.*

class reuniaoShowFragment : Fragment() {


    var _binding: FragmentReuniaoShowBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ReuniaoShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReuniaoShowBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loadBar!!.visibility = View.GONE
        val documentId = arguments?.getString("documentId")
        val factory = reuniaoShowViewModelFactory(documentId!!)
        viewModel = ViewModelProvider(this, factory).get(ReuniaoShowViewModel::class.java)

        viewModel.status.observe(viewLifecycleOwner) {
            if (it)
                findNavController().popBackStack()
        }

        viewModel.msg.observe(viewLifecycleOwner) {
            if (it.isNotBlank())
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.reuniao.observe(viewLifecycleOwner){
            if (it != null){
                //view.findViewById<TextView>(R.id.input_categoria).text = it.categoria
                view.findViewById<TextView>(R.id.tvdetalhes).text = it.titulo
                view.findViewById<TextView>(R.id.tvData).text = it.data
                view.findViewById<TextView>(R.id.tvHora).text = it.horainicio
                view.findViewById<TextView>(R.id.tvHoraTermino).text = it.horatermino
                view.findViewById<TextView>(R.id.tvNomeReuniao).text = it.titulo
                view.findViewById<TextView>(R.id.inputTipo).text = it.tipo
            }
        }


        viewModel.ingressoStatus.observe(viewLifecycleOwner){

            if (it){
                binding.btnJoin.setTextColor(getResources().getColor(R.color.white))
                binding.btnJoin.setBackgroundColor(resources.getColor(R.color.Green))
                binding.btnJoin.text = "Participando"
            }






        }
        viewModel.checkParticipante()

        binding.btnParticipantes.setOnClickListener{

            findNavController()
                .navigate(
                    R.id.participantesFragment, bundleOf(
                        "documentId" to documentId
                    )
                )
//
//            val stringSenderEmail = "lokaiextreme@gmail.com"
//            val stringReceiverEmail = authDao.getCurrentUser()!!.email
//            val stringPasswordSenderEmail = "hnzymrwyoqmexrsm"
//            val stringHost = "smtp.gmail.com"
//
//            val properties = System.getProperties()
//            properties.put("mail.smtp.host", stringHost)
//            properties.put("mail.smtp.port", 465)
//            properties.put("mail.smtp.ssl.enable", "true")
//            properties.put("mail.smtp.auth", "true")




//
//
        }


        setup()
        return view
    }

    fun send(){

        viewModel.reuniao.observe(viewLifecycleOwner){
            val email = authDao.getCurrentUser()!!.email
            val message = "Você entrou na Reunião ${it.titulo} que será realizada na plataforma ${it.tipo}\n\nData: ${it.data}\nHora de início: ${it.horainicio}\nHora de término: ${it.horatermino}\n\nPara ver os detalhes da reunião entre em nosso aplicativo Lets Meet"
            val subject = "Reunião ${it.titulo}"

            val javaMailAPI = email?.let { JavaMailAPI(requireContext(), it, subject, message) }
            javaMailAPI!!.execute()
        }

    }

    fun setup(){
        setupClickListener()
        datePicker()
        hourPicker()
        configuraPapeis()
    }

    private fun setupClickListener() {

        binding.btnJoin.setOnClickListener {
            checarIngressar()
            send()
        }
        binding.btnCreate.setOnClickListener {
            editar()
        }
        binding.fBDeleteReuniao.setOnClickListener {
            excluir()
        }

        binding.imageBackstack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun checarIngressar(){
        viewModel.checkIngressar()
        //viewModel.ingressar()

//
//        }
    }

    private fun excluir() {
        //viewModel.excluir()
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm_delete))
        builder.setMessage(getString(R.string.delete_confirmation_message))
        builder.setPositiveButton("Sim", DialogInterface.OnClickListener{dialog, id -> viewModel.excluir()
        dialog.cancel()
        })
        builder.setNegativeButton("Não", DialogInterface.OnClickListener{dialog, id -> dialog.cancel()})
        var alert = builder.create()
        alert.show()



    }

    private fun editar() {
        val titulo = binding.tvNomeReuniao.text.toString()
        val data = binding.tvData.text.toString()
        val horainicio = binding.tvHora.text.toString()
        val horatermino = binding.tvHoraTermino.text.toString()
        val tipo = binding.inputTipo.text.toString()
        if (titulo.isNotEmpty() && data.isNotEmpty() && horainicio.isNotEmpty() && horatermino.isNotEmpty()){
             viewModel.atualizar(titulo, data, horainicio, horatermino, tipo)
//            //if (SimpleDateFormat("dd/M/yyyy").parse(data).after(Date()) or SimpleDateFormat("dd/M/yyyy").parse(data).equals(Date()) ){
//
//            }
//            else
//                Toast.makeText(requireContext(), "A data escolhida já passou, insira outra data", Toast.LENGTH_SHORT).show()
//


            //binding.loadBar.visibility = View.VISIBLE
        }

        else Toast.makeText(requireContext(), "Insira os campos", Toast.LENGTH_LONG).show()

    }

    var listaDePapeis = arrayOf(
        "Zoom",
        "Google Meets",
        "Teams",
        "Outro"

    )
    val calendar = Calendar.getInstance()

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
            val timePicker = TimePickerDialog(requireContext(),
                TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                binding.tvHora.setText("$i:$i2")
            },hour,minutes,true)
            timePicker.show()
        }

        binding.tvHoraTermino.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(),
                TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    binding.tvHoraTermino.setText("$i:$i2")
                },hour,minutes,true)
            timePicker.show()
        }

    }

    fun configuraPapeis(){
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.tipos_item_list, listaDePapeis)
        binding.inputTipo.setAdapter(adapter)
    }

    fun toastMessage(mensagem: String) {
        Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ReuniaoShowViewModel::class.java)
        // TODO: Use the ViewModel
    }






}