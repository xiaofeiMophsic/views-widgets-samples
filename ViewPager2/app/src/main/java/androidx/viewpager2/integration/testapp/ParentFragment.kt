package androidx.viewpager2.integration.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.integration.testapp.cards.Card
import androidx.viewpager2.integration.testapp.cards.CardView
import androidx.viewpager2.widget.ViewPager2

/**
 * 作者: xiaofei27
 * 日期: 2022年08月22日
 */
class ParentFragment : Fragment() {

    protected lateinit var viewPager: ViewPager2
    private lateinit var cardSelector: Spinner
    private lateinit var smoothScrollCheckBox: CheckBox
    private lateinit var rotateCheckBox: CheckBox
    private lateinit var translateCheckBox: CheckBox
    private lateinit var scaleCheckBox: CheckBox
    private lateinit var gotoPage: Button

    private val translateX
        get() = viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL &&
                translateCheckBox.isChecked
    private val translateY
        get() = viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL &&
                translateCheckBox.isChecked

    protected open val layoutId: Int = R.layout.activity_no_tablayout

    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            rotation = if (rotateCheckBox.isChecked) position * 360 else 0f
            translationY = if (translateY) absPos * 500f else 0f
            translationX = if (translateX) absPos * 350f else 0f
            if (scaleCheckBox.isChecked) {
                val scale = if (absPos > 1) 0F else 1 - absPos
                scaleX = scale
                scaleY = scale
            } else {
                scaleX = 1f
                scaleY = 1f
            }
        }
    }

    init {
        val test = "test"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        childFragmentManager.findFragmentByTag(CHILD_FRAGMENT_TAG) ?: kotlin.run {
            childFragmentManager.commit {
                val child = ChildFragment()
                replace(R.id.tab_container, child, CHILD_FRAGMENT_TAG)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.run {

            viewPager = findViewById(R.id.view_pager)
            cardSelector = findViewById(R.id.card_spinner)
            smoothScrollCheckBox = findViewById(R.id.smooth_scroll_checkbox)
            rotateCheckBox = findViewById(R.id.rotate_checkbox)
            translateCheckBox = findViewById(R.id.translate_checkbox)
            scaleCheckBox = findViewById(R.id.scale_checkbox)
            gotoPage = findViewById(R.id.jump_button)

            UserInputController(viewPager, findViewById(R.id.disable_user_input_checkbox)).setUp()
            OrientationController(viewPager, findViewById(R.id.orientation_spinner)).setUp()
            cardSelector.adapter = createCardAdapter()

            viewPager.setPageTransformer(mAnimator)

            gotoPage.setOnClickListener {
                val card = cardSelector.selectedItemPosition
                val smoothScroll = smoothScrollCheckBox.isChecked
                viewPager.setCurrentItem(card, smoothScroll)
            }

            rotateCheckBox.setOnClickListener { viewPager.requestTransform() }
            translateCheckBox.setOnClickListener { viewPager.requestTransform() }
            scaleCheckBox.setOnClickListener { viewPager.requestTransform() }

            viewPager.adapter = object : FragmentStateAdapter(this@ParentFragment) {
                override fun createFragment(position: Int): Fragment {
                    return CardFragment.create(Card.DECK[position])
                }

                override fun getItemCount(): Int {
                    return Card.DECK.size
                }
            }
        }

    }

    private fun createCardAdapter(): SpinnerAdapter {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Card.DECK)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    fun onNewIntent(stringExtra: String) {
        val childFragment = childFragmentManager.findFragmentByTag(CHILD_FRAGMENT_TAG)
        (childFragment as ChildFragment).onNewIntent()
    }

    companion object {
        const val CHILD_FRAGMENT_TAG = "CHILD_FRAGMENT"
    }

    class CardFragment : Fragment() {

        lateinit var card: Card

        init {
            val test = "just for debug"
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val cardView = CardView(layoutInflater, container)
            card = Card.fromBundle(arguments!!)
            cardView.bind(card)

            cardView.view.setOnClickListener {
                startActivity(Intent(requireActivity(), OtherActivity::class.java))
            }

            return cardView.view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val childFragment = parentFragmentManager.findFragmentByTag(CHILD_FRAGMENT_TAG)
            (childFragment as ChildFragment).changeText("change by card fragment")
        }

        override fun onStart() {
            super.onStart()
            Log.d(TAG, "onStart: ${card.cornerLabel}")
        }

        override fun onPause() {
            super.onPause()
            Log.d(TAG, "onPause: ${card.cornerLabel}")
        }

        override fun onResume() {
            super.onResume()
            Log.d(TAG, "onResume: ${card.cornerLabel}")
        }

        override fun onStop() {
            super.onStop()
            Log.d(TAG, "onStop: ${card.cornerLabel}")
        }

        companion object {

            const val TAG = "CardFragment"

            /** Creates a Fragment for a given [Card]  */
            fun create(card: Card): CardFragment {
                val fragment = CardFragment()
                fragment.arguments = card.toBundle()
                return fragment
            }
        }
    }
}