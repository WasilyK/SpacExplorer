package com.wasilyk.app.core

sealed class AppState

class Success<T>(val data: List<T>) : AppState()
class Error(val throwable: Throwable) : AppState()
object Loading : AppState()
