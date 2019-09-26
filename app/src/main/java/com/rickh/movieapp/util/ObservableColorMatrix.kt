package com.rickh.movieapp.util


import android.graphics.ColorMatrix
import android.util.Property


/**
 * An extension to [ColorMatrix] which caches the saturation value for animation purposes.
 */
class ObservableColorMatrix : ColorMatrix() {

    private var saturation = 1f

    internal fun getSaturation(): Float {
        return saturation
    }

    override fun setSaturation(saturation: Float) {
        this.saturation = saturation
        super.setSaturation(saturation)
    }

    companion object {
        val SATURATION: Property<ObservableColorMatrix, Float> = AnimUtils.createFloatProperty(
            object : AnimUtils.FloatProp<ObservableColorMatrix>("saturation") {
                override operator fun get(ocm: ObservableColorMatrix): Float {
                    return ocm.getSaturation()
                }

                override operator fun set(ocm: ObservableColorMatrix, saturation: Float) {
                    ocm.setSaturation(saturation)
                }
            })
    }
}