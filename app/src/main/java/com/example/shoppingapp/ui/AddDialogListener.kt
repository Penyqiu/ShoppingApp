package com.example.shoppingapp.ui

import com.example.shoppingapp.data.db.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item: ShoppingItem)
}