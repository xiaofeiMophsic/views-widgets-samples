/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.viewpager2.integration.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.coroutineScope

import kotlinx.coroutines.launch

/**
 * Shows how to use a [androidx.viewpager2.widget.ViewPager2] with Fragments, via a
 * [androidx.viewpager2.adapter.FragmentStateAdapter]
 *
 * @see CardViewActivity for an example of using {@link androidx.viewpager2.widget.ViewPager2} with
 * Views.
 */
class CardFragmentActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_no_tablayout_fragment)

        lifecycle.coroutineScope.launch {
            supportFragmentManager.findFragmentByTag(PARENT_FRAGMENT_TAG) ?: run {
                val parentFragment = ParentFragment()
                supportFragmentManager.commit {
                    replace(
                        R.id.view_pager_container,
                        parentFragment,
                        PARENT_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?.let {
            val stringExtra = intent.getStringExtra(OtherActivity.EXTRA_NAME)
            Log.d("CardFragmentActivity", "onNewIntent: $stringExtra")
            val parentFragment = supportFragmentManager.findFragmentByTag(PARENT_FRAGMENT_TAG)
            stringExtra?.let {
                (parentFragment as ParentFragment).onNewIntent(stringExtra)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val PARENT_FRAGMENT_TAG = "PARENT_FRAGMENT"
    }
}
