package org.whiskersapps.mordomo.mobile.core.features.screen

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FoldableRepository {
    private val _isOpen = MutableStateFlow(false)
    val isOpen = _isOpen.asStateFlow()

    fun setIsOpen(open: Boolean) {
        _isOpen.update { open }

        Log.d("Foldable", "Foldable open state: $isOpen")
    }
}