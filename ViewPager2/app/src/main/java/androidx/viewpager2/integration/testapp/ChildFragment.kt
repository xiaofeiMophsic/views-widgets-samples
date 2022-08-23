package androidx.viewpager2.integration.testapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView

/**
 * 作者: xiaofei27
 * 日期: 2022年08月22日
 */
class ChildFragment: Fragment(){

    lateinit var textView: MaterialTextView

    private var changedByCardText = ""

    init {
        val test = "just for debug"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_child_layout, container, false)
        textView = view.findViewById(R.id.text_child_fragment)
        return view
    }

    fun onNewIntent() {
        textView.text = changedByCardText
    }

    fun changeText(text: String) {
        this.changedByCardText = text
    }
}