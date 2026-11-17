package com.example.core

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookmarkStore(
    context: Context,
    private val userId: String
) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val key = "bookmarks_$userId"

    private val _bookmarkedIds = MutableStateFlow(loadBookmarks())
    val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

    fun toggleBookmark(questionId: String) {
        val updated = _bookmarkedIds.value.toMutableSet().apply {
            if (!add(questionId)) {
                remove(questionId)
            }
        }
        prefs.edit().putStringSet(key, updated).apply()
        _bookmarkedIds.value = updated
    }

    fun isBookmarked(questionId: String): Boolean = _bookmarkedIds.value.contains(questionId)

    private fun loadBookmarks(): Set<String> {
        return prefs.getStringSet(key, emptySet())?.toSet() ?: emptySet()
    }

    private companion object {
        const val PREFS_NAME = "xels_logistik_bookmarks"
    }
}
