package com.app.staffabcd.fragments.reportFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.R
import com.app.staffabcd.adapter.ReportAdapter
import com.app.staffabcd.databinding.FragmentLevelFiveBinding
import com.app.staffabcd.databinding.FragmentLevelOneBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Report
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LevelFiveFragment : Fragment() {

    lateinit var reportAdapter: ReportAdapter
    lateinit var binding: FragmentLevelFiveBinding
    lateinit var session: Session


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLevelFiveBinding.inflate(inflater, container, false)
        session = Session(requireActivity())
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.levelOneRecyclerView.layoutManager = linearLayoutManager
        reportOneList()
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            reportOneList()
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

    private fun reportOneList() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.REFER] = "5"



        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val reports: ArrayList<Report> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                // Extract the values from the JSON object
                                val id = jsonObject1.getString(Constant.ID)
                                val name = jsonObject1.getString(Constant.NAME)
                                val refer_code = jsonObject1.getString(Constant.REFER_CODE)
                                val total_codes = jsonObject1.getString(Constant.TOTAL_ADS)
                                val worked_days = jsonObject1.getString(Constant.WORKED_DAYS)
                                val mobile = jsonObject1.getString(Constant.MOBILE)
                                val total_referrals = jsonObject1.getString(Constant.TOTAL_REFERRALS)

                                // Create a new Report object and add it to the list
                                val report = Report(id, name,refer_code,total_codes,worked_days,mobile, total_referrals)
                                reports.add(report)


                            } else {
                                break
                            }
                        }
                        reportAdapter = ReportAdapter(requireActivity(), reports,"1")
                        binding.levelOneRecyclerView.setAdapter(reportAdapter)


                    }else{
                        Toast.makeText(
                            requireContext(), "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.REPORTS_LIST, params, true)
    }

}