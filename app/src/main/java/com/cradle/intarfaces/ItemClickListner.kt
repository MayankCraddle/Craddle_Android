package com.cradle.intarfaces

interface ItemClickListner {
    /**
     * Will be called when login button gets clicked
     */
    fun onClickItem(position: Int,requestcode: Int)
}