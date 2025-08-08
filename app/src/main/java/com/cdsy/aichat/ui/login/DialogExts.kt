/*
package com.cdsy.aichat.ui.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zzyyff.xrecord.R
import com.zzyyff.xrecord.databinding.DialogAddPersonBinding
import com.zzyyff.xrecord.databinding.DialogAddRecordBinding
import com.zzyyff.xrecord.model.api.Person
import com.zzyyff.xrecord.model.api.Record
import com.zzyyff.xrecord.ui.recordlist.PersonChooseRecyclerAdapter

//登陆/注册弹窗
fun showLoginDialog(
    context: Context,
    onClickLogin: () -> Unit
) {
    val dialogBinding by lazy { DialogAddRecordBinding.inflate(LayoutInflater.from(context)) }
    val addRecordDialog by lazy { BottomSheetDialog(context) }
    var payWayIndex = 0
    addRecordDialog.setContentView(dialogBinding.root)
    addRecordDialog.delegate.findViewById<View>(R.id.design_bottom_sheet)
        ?.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
    dialogBinding.etAmount.setText("")
    dialogBinding.etContent.setText("")
    dialogBinding.tvPayWay.setOnClickListener {
        payWayIndex++
        val i = if (payWayIndex >= 3) payWayIndex % PAY_WAY_LIST.size else payWayIndex
        dialogBinding.tvPayWay.text = PAY_WAY_LIST[i]
    }
    dialogBinding.rvPerson.apply {
        layoutManager = GridLayoutManager(context, 3)
        adapter = PersonChooseRecyclerAdapter {}.apply {}
    }

    (dialogBinding.rvPerson.adapter as PersonChooseRecyclerAdapter).replaceData(personList)

    dialogBinding.btnLeft.apply {
        text = "取消"
        setOnClickListener { addRecordDialog.dismiss() }
    }
    dialogBinding.btnConfirm.text = "添加"
    dialogBinding.btnConfirm.setOnClickListener {
        when {
            dialogBinding.etContent.text.isEmpty() -> {
                Toast.makeText(context, "请输入消费内容", Toast.LENGTH_SHORT).show()
            }

            (dialogBinding.rvPerson.adapter as PersonChooseRecyclerAdapter).getSelectedItem()
                .isEmpty() -> {
                Toast.makeText(context, "至少选择一位分账人", Toast.LENGTH_SHORT).show()
            }

            dialogBinding.etAmount.text.isEmpty() -> {
                Toast.makeText(context, "请输入金额", Toast.LENGTH_SHORT).show()
            }

            else -> {
                addRecord?.invoke(
                    dialogBinding.etContent.text.toString(),
                    dialogBinding.etAmount.text.toString().toFloat(),
                    dialogBinding.tvPayWay.text.toString(),
                    (dialogBinding.rvPerson.adapter as PersonChooseRecyclerAdapter)
                        .getSelectedItem()
                        .map { it.id ?: 0 }
                )
                addRecordDialog.dismiss()
            }
        }

    }
    addRecordDialog.show()
}

//账单详情弹窗
fun showRecordDetailDialog(
    context: Context,
    record: Record,
    allPersonInRoom: List<Person>,
    settlementRecord: (() -> Unit)? = null,
    updateRecord: ((String, Float, String) -> Unit)? = null
) {

    val dialogBinding by lazy { DialogAddRecordBinding.inflate(LayoutInflater.from(context)) }
    val recordDetailDialog by lazy { BottomSheetDialog(context) }

    var payWayIndex = PAY_WAY_LIST.indexOf(record.payWay)

    recordDetailDialog.setContentView(dialogBinding.root)

    recordDetailDialog.delegate.findViewById<View>(R.id.design_bottom_sheet)
        ?.setBackgroundColor(context.resources.getColor(android.R.color.transparent))

    dialogBinding.etAmount.setText(record.amount.toString())
    dialogBinding.etAmount.addTextChangedListener { money ->
        val adapter = dialogBinding.rvPerson.adapter as PersonChooseRecyclerAdapter
        adapter.replaceData(
            adapter.baseList.map { person ->
                person.copy(
                    shouldPayMoney = (money.toString().toFloatOrNull()
                        ?: 0f) / adapter.baseList.size
                )
            }
        )
    }
    dialogBinding.etContent.setText(record.content)
    dialogBinding.tvPerson.text = "分账人"
    dialogBinding.tvPayWay.text = record.payWay
    dialogBinding.tvPayWay.setOnClickListener {
        payWayIndex++
        val i = if (payWayIndex >= 3) payWayIndex % PAY_WAY_LIST.size else payWayIndex
        dialogBinding.tvPayWay.text = PAY_WAY_LIST[i]
    }
    dialogBinding.rvPerson.apply {
        layoutManager = GridLayoutManager(context, 3)
        adapter = PersonChooseRecyclerAdapter(false)
            .apply {
                val resultList = record.personList.split(",")
                    .mapNotNull { idString -> allPersonInRoom.find { p -> p.id == idString.toInt() } }
                replaceData(resultList.map { person -> person.copy(shouldPayMoney = record.amount / resultList.size) })
            }
    }
    dialogBinding.btnLeft.apply {
        text = "结算"
        setOnClickListener {
            settlementRecord?.invoke()
            recordDetailDialog.dismiss()
        }
    }
    dialogBinding.btnConfirm.text = "更正"
    dialogBinding.btnConfirm.setOnClickListener {
        updateRecord?.invoke(
            dialogBinding.etContent.text.toString(),
            dialogBinding.etAmount.text.toString().toFloat(),
            dialogBinding.tvPayWay.text.toString()
        )
        recordDetailDialog.dismiss()
    }
    recordDetailDialog.show()
}

//添加分账人的弹窗
fun showAddPersonDialog(
    context: Context,
    addPerson: (String) -> Unit
) {
    val dialogAddPersonBinding by lazy { DialogAddPersonBinding.inflate(LayoutInflater.from(context)) }
    val addPersonDialog by lazy { BottomSheetDialog(context) }
    addPersonDialog.setContentView(dialogAddPersonBinding.root)
    addPersonDialog.delegate.findViewById<View>(R.id.design_bottom_sheet)
        ?.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
    dialogAddPersonBinding.etName.setText("")
    dialogAddPersonBinding.ivDelete.visibility = View.GONE
    dialogAddPersonBinding.btnCancel.setOnClickListener { addPersonDialog.dismiss() }
    dialogAddPersonBinding.btnConfirm.text = "添加"
    dialogAddPersonBinding.btnConfirm.setOnClickListener {
        if (dialogAddPersonBinding.etName.text.isEmpty()) {
            Toast.makeText(context, "请输入分账人昵称", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }
        addPerson(dialogAddPersonBinding.etName.text.toString())
        addPersonDialog.dismiss()
    }
    addPersonDialog.show()
}
*/
