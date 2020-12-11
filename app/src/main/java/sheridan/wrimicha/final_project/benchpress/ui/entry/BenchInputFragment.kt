package sheridan.wrimicha.final_project.benchpress.ui.entry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sheridan.wrimicha.final_project.BenchEntity
import sheridan.wrimicha.final_project.JogInputFragmentDirections
import sheridan.wrimicha.final_project.R
import sheridan.wrimicha.final_project.benchpress.domain.BenchData
import sheridan.wrimicha.final_project.databinding.FragmentBenchInputBinding
import sheridan.wrimicha.final_project.databinding.FragmentJogInputBinding
import sheridan.wrimicha.final_project.databinding.FragmentLaunchBinding
import java.lang.Double.parseDouble
import java.util.*

class BenchInputFragment : Fragment() {
    private lateinit var binding: FragmentBenchInputBinding
    private val viewModel: BenchViewModel by activityViewModels()

    var year1 :Int=0
    var month1 :Int=0
    var dayOfMonth1 :Int=0

    private enum class EditingState {
        NEW_BENCH,
        EXISTING_BENCH
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBenchInputBinding.inflate(inflater, container, false)
        val c = Calendar.getInstance()
        val yearCurrent = c.get(Calendar.YEAR)
        val monthCurrent = c.get(Calendar.MONTH)
        val dayOfMonthCurrent = c.get(Calendar.DAY_OF_MONTH)

        binding.calendarView2.setOnDateChangeListener { view, year, month, dayOfMonth ->
            year1=year
            month1=month+1
            dayOfMonth1=dayOfMonth
        }

//        var jog: Jog? = null
//        val args: JogInputFragmentArgs by navArgs()
//        val editingState =
//            if (args.jogId > 0) EditingState.EXISTING_JOG
//            else EditingState.NEW_JOG


        var bench: BenchEntity? = null
        val args: BenchInputFragmentArgs by navArgs()
        val editingState =
            if (args.benchId > 0) EditingState.EXISTING_BENCH
            else EditingState.NEW_BENCH

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_BENCH) {
            // Request to edit an existing item, whose id was passed in as an argument.
            // Retrieve that item and populate the UI with its details
            viewModel.get(args.benchId).observe(viewLifecycleOwner) { benchItem ->
                binding.weightUsed.setText(benchItem.weight.toString())
                binding.reps.setText(benchItem.reps.toString())
                binding.sets.setText(benchItem.sets.toString())
                bench = benchItem
            }
        }

        binding.send.setOnClickListener {

            var weight = 0.0
            var reps = 0.0
            var sets = 0.0

            if(year1==0 && month1==0 && dayOfMonth1==0){
                year1 = yearCurrent
                month1 = monthCurrent+1
                dayOfMonth1 = dayOfMonthCurrent
            }

            if(binding.weightUsed.text.isEmpty()){
                val required = getString(R.string.required)
                binding.weightUsed.error = required
                Toast.makeText(context, required, Toast.LENGTH_LONG).show()
            }

            else if(binding.reps.text.isEmpty()){
                val required = getString(R.string.required)
                binding.reps.error = required
                Toast.makeText(context, required, Toast.LENGTH_LONG).show()
            }
            else if(binding.sets.text.isEmpty()){
                val required = getString(R.string.required)
                binding.sets.error = required
                Toast.makeText(context, required, Toast.LENGTH_LONG).show()
            }
            else {
                weight = parseDouble(binding.weightUsed.toString())
                reps = parseDouble(binding.reps.toString())
                sets = parseDouble(binding.sets.text.toString())
            }

            viewModel.submit(
                bench?.id ?: 0,
                weight,
                reps,
                sets,
                year1,
                month1,
                dayOfMonth1,
                )
                findNavController().navigate(BenchInputFragmentDirections.actionBenchInputFragmentToBenchOutputFragment())
            }

        binding.back.setOnClickListener {
            findNavController().navigate(BenchInputFragmentDirections.actionBenchInputFragmentToLaunchFragment())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_jog, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.action_history -> {
                findNavController().navigate(R.id.action_global_benchListFragment)
                true
            }
            else-> super.onOptionsItemSelected(item)
        }
    }

     //   binding.lifecycleOwner = viewLifecycleOwner
      //  binding.viewModel = viewModel
}